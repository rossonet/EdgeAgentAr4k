/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    */
package org.ar4k.agent.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.Ar4kComponent.ServiceStatus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;

/**
 * Classe astratta da implementare per i servizi gestiti dalla piattaforma Ar4k.
 *
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 * @see org.ar4k.agent.core.Ar4kComponent
 * @see org.ar4k.agent.core.ServiceComponent
 */
public abstract class AbstractAr4kService implements ServiceComponent<Ar4kComponent> {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(AbstractAr4kService.class.toString());

  protected Ar4kComponent pot;
  protected final Timer timerScheduler;
  protected ExecutorService executor = Executors.newScheduledThreadPool(6);
  protected int maxFaults;
  protected int watchDogInterval;
  protected AtomicLong startRetries = new AtomicLong(1);
  protected AtomicBoolean stopped = new AtomicBoolean(false);
  protected Instant lastRestart = null;

  protected int watchDogTimeout = 120000;
  private TimerTask watchDogTask = null;

  public AbstractAr4kService(Anima anima, ServiceConfig serviceConfig, Timer timerScheduler) {
    this.timerScheduler = timerScheduler;
    this.maxFaults = serviceConfig.getMaxRestartRetries();
    try {
      Method method = serviceConfig.getClass().getMethod("instantiate");
      pot = (Ar4kComponent) method.invoke(serviceConfig);
      pot.setConfiguration(serviceConfig);
      pot.setAnima(anima);
      pot.setDataAddress(new DataAddress(anima, serviceConfig.getDataNamePrefix()));
      if (pot.getDataAddress() != null) {
        anima.getDataAddress().registerSlave(pot.getDataAddress());
      }
      watchDogTimeout = serviceConfig.getWatchDogTimeout();
      watchDogInterval = serviceConfig.getWatchDogInterval();
      createTask(timerScheduler);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException e) {
      logger.logException("during service " + serviceConfig + " creation", e);
    }
  }

  private void createTask(Timer timerScheduler) {
    watchDogTask = new WatchDogTask();
    timerScheduler.schedule(watchDogTask, watchDogInterval, watchDogInterval);
  }

  private class WatchDogTask extends TimerTask {
    @Override
    public void run() {
      getUpdateFromPot();
    }
  }

  @Override
  public void close() throws Exception {
    stop();
    Future<?> callFuture = executor.submit(new Runnable() {
      @Override
      public void run() {
        try {
          if (pot.getDataAddress() != null) {
            pot.getAnima().getDataAddress().registerSlave(pot.getDataAddress());
          }
          pot.close();
        } catch (Exception e) {
          logger.logException(e);
        }
      }
    });
    callFuture.get(watchDogTimeout, TimeUnit.MILLISECONDS);
    executor.shutdownNow();
  }

  private void notifyException() {
    if (!stopped.get()) {
      executor.submit(new Runnable() {
        @Override
        public void run() {
          stop();
          if (maxFaults == 0 || startRetries.incrementAndGet() < maxFaults) {
            start();
          } else {
            logger.warn("service " + pot.getConfiguration() + " is fault for " + startRetries.get() + " times");
          }
        }
      });
    }
  }

  @Override
  public Ar4kComponent getPot() {
    return pot;
  }

  @Override
  public synchronized void start() {
    startRetries.set(1);
    stopped.set(false);
    Future<?> callFuture = executor.submit(new Runnable() {
      @Override
      public void run() {
        try {
          pot.init();
        } catch (Exception e) {
          logger.logException("in service " + pot.getConfiguration() + " init", e);
        }
      }
    });
    try {
      callFuture.get(watchDogTimeout, TimeUnit.MILLISECONDS);
      lastRestart = Instant.now();
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      logger.logException("service " + pot.getConfiguration() + " timeout during init", e);
    }
    if (watchDogTask == null) {

    }
    logger.info("started service " + getPot().getConfiguration());
  }

  @Override
  public synchronized void stop() {
    stopped.set(true);
    Future<?> callFuture = executor.submit(new Runnable() {
      @Override
      public void run() {
        try {
          pot.kill();
        } catch (Exception e) {
          logger.logException("in service " + pot.getConfiguration() + " kill", e);
        }
      }
    });
    try {
      callFuture.get(watchDogTimeout, TimeUnit.MILLISECONDS);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      logger.logException("service " + pot.getConfiguration() + " timeout during kill", e);
    }
    logger.info("stopped service " + getPot().getConfiguration());
  }

  @Override
  public boolean isRunning() {
    if (!stopped.get()) {
      return getUpdateFromPot().equals(ServiceStatus.RUNNING);
    } else {
      return false;
    }
  }

  private ServiceStatus getUpdateFromPot() {
    final Future<ServiceStatus> callFuture = executor.submit(new Callable<ServiceStatus>() {
      @Override
      public ServiceStatus call() {
        try {
          return pot.updateAndGetStatus();
        } catch (Exception e) {
          logger.logException("in service " + pot.getConfiguration() + " update", e);
          notifyException();
          return ServiceStatus.FAULT;
        }
      }
    });
    try {
      return callFuture.get(watchDogTimeout, TimeUnit.MILLISECONDS);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      logger.logException("service " + pot.getConfiguration() + " timeout during update", e);
      notifyException();
      return ServiceStatus.FAULT;
    }
  }

}
