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
package org.ar4k.agent.core.services;

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

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.core.interfaces.ServiceComponent;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.core.interfaces.EdgeComponent.ServiceStatus;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

/**
 * Classe astratta da implementare per i servizi gestiti dalla piattaforma Ar4k.
 *
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 * @see org.ar4k.agent.core.interfaces.EdgeComponent
 * @see org.ar4k.agent.core.interfaces.ServiceComponent
 */
public abstract class AbstractEdgeService implements ServiceComponent<EdgeComponent> {

	private class WatchDogTask extends TimerTask {
		@Override
		public void run() {
			Thread.currentThread().setName("wg " + pot.getServiceName());
			getUpdateFromPot();
		}
	}

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(AbstractEdgeService.class.toString());
	protected ExecutorService executor = Executors.newScheduledThreadPool(6);
	protected Instant lastRestart = null;
	protected int maxFaults;
	protected EdgeComponent pot;
	protected AtomicLong startRetries = new AtomicLong(1);
	protected AtomicBoolean stopped = new AtomicBoolean(false);
	protected final Timer timerScheduler;

	protected int watchDogInterval;
	protected int watchDogTimeout = 120000;

	private TimerTask watchDogTask = null;

	public AbstractEdgeService(Homunculus homunculus, ServiceConfig serviceConfig, Timer timerScheduler) {
		this.timerScheduler = timerScheduler;
		this.maxFaults = serviceConfig.getMaxRestartRetries();
		try {
			Method method = serviceConfig.getClass().getMethod("instantiate");
			pot = (EdgeComponent) method.invoke(serviceConfig);
			pot.setConfiguration(serviceConfig);
			pot.setHomunculus(homunculus);
			pot.setDataAddress(new DataAddress(homunculus));
			if (pot.getDataAddress() != null) {
				homunculus.getDataAddress().registerSlave(pot);
			}
			watchDogTimeout = serviceConfig.getWatchDogTimeout();
			watchDogInterval = serviceConfig.getWatchDogInterval();
			createTask(timerScheduler);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			logger.logException("during service " + serviceConfig + " creation", e);
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
						pot.getHomunculus().getDataAddress().registerSlave(pot);
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

	@Override
	public EdgeComponent getPot() {
		return pot;
	}

	@Override
	public boolean isRunning() {
		if (!stopped.get()) {
			return getUpdateFromPot().equals(ServiceStatus.RUNNING);
		} else {
			return false;
		}
	}

	@Override
	public synchronized void start() {
		startRetries.set(1);
		stopped.set(false);
		Future<?> callFuture = executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.currentThread().setName("init " + pot.getServiceName());
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
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AbstractAr4kService [pot=").append(pot).append(", maxFaults=").append(maxFaults)
				.append(", watchDogInterval=").append(watchDogInterval).append(", startRetries=").append(startRetries)
				.append(", stopped=").append(stopped).append(", lastRestart=").append(lastRestart)
				.append(", watchDogTimeout=").append(watchDogTimeout).append(", watchDogTask=").append(watchDogTask)
				.append("]");
		return builder.toString();
	}

	private void createTask(Timer timerScheduler) {
		watchDogTask = new WatchDogTask();
		timerScheduler.schedule(watchDogTask, watchDogInterval, watchDogInterval);
	}

	private ServiceStatus getUpdateFromPot() {
		final Future<ServiceStatus> callFuture = (executor != null) ? (executor.submit(new Callable<ServiceStatus>() {
			@Override
			public ServiceStatus call() {
				try {
					return pot.updateAndGetStatus();
				} catch (Exception e) {
					logger.info(
							"in service " + pot.getConfiguration() + " update\n" + EdgeLogger.stackTraceToString(e, 6));
					notifyException();
					return ServiceStatus.FAULT;
				}
			}
		})) : null;
		try {
			return callFuture.get(watchDogTimeout, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			logger.info("service " + pot.getConfiguration() + " during update\n" + EdgeLogger.stackTraceToString(e, 6));
			notifyException();
			return ServiceStatus.FAULT;
		}
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
						logger.warn(
								"service " + pot.getConfiguration() + " is fault for " + startRetries.get() + " times");
					}
				}
			});
		}
	}

}
