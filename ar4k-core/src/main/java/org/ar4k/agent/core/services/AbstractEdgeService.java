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

import org.ar4k.agent.core.EdgeAgentCore;
import org.ar4k.agent.core.data.DataAddressBase;
import org.ar4k.agent.core.services.EdgeComponent.ServiceStatus;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

/**
 * Classe astratta da implementare per i servizi gestiti dalla piattaforma Ar4k.
 *
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 * @see org.ar4k.agent.core.services.EdgeComponent
 * @see org.ar4k.agent.core.services.ServiceComponent
 */

//TODO gestione configurazione isSpringBean
public abstract class AbstractEdgeService implements ServiceComponent<EdgeComponent> {

	private class WatchDogTask extends TimerTask {
		@Override
		public void run() {
			Thread.currentThread().setName("wg " + pot.getServiceName());
			getUpdateFromPot();
		}
	}

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(AbstractEdgeService.class);
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

	public AbstractEdgeService(EdgeAgentCore edgeAgentCore, ServiceConfig serviceConfig, Timer timerScheduler) {
		this.timerScheduler = timerScheduler;
		this.maxFaults = serviceConfig.getMaxRestartRetries();
		try {
			Method method = serviceConfig.getClass().getMethod("instantiate");
			pot = (EdgeComponent) method.invoke(serviceConfig);
			pot.setConfiguration(serviceConfig);
			pot.setHomunculus(edgeAgentCore);
			pot.setDataAddress(new DataAddressBase(edgeAgentCore, pot));
			if (pot.getDataAddress() != null) {
				edgeAgentCore.getDataAddress().registerSlave(pot);
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
					logger.logException("in service " + pot + " init", e);
				}
			}
		});
		try {
			callFuture.get(watchDogTimeout, TimeUnit.MILLISECONDS);
			lastRestart = Instant.now();
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			logger.logException("service " + pot + " timeout during init", e);
		}
		if (watchDogTask == null) {

		}
		logger.info("started service " + getPot());
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
					logger.logException("in service " + pot + " kill", e);
				}
			}
		});
		try {
			callFuture.get(watchDogTimeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			logger.logException("service " + pot + " timeout during kill", e);
		}
		logger.info("stopped service " + getPot());
	}

	private void createTask(Timer timerScheduler) {
		watchDogTask = new WatchDogTask();
		timerScheduler.schedule(watchDogTask, watchDogInterval, watchDogInterval);
	}

	private ServiceStatus getUpdateFromPot() {
		if (!stopped.get()) {
			final Future<ServiceStatus> callFuture = (executor != null)
					? (executor.submit(new Callable<ServiceStatus>() {
						@Override
						public ServiceStatus call() {
							try {
								return pot.updateAndGetStatus();
							} catch (Exception e) {
								logger.info("in service " + pot.getConfiguration() + " update\n"
										+ EdgeLogger.stackTraceToString(e, 6));
								notifyException();
								return ServiceStatus.FAULT;
							}
						}
					}))
					: null;
			try {
				return callFuture.get(watchDogTimeout, TimeUnit.MILLISECONDS);
			} catch (Exception e) {
				logger.info(
						"service " + pot.getConfiguration() + " during update\n" + EdgeLogger.stackTraceToString(e, 6));
				notifyException();
				return ServiceStatus.FAULT;
			}
		} else {
			return ServiceStatus.STOPPED;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AbstractEdgeService [");
		if (executor != null) {
			builder.append("executor=");
			builder.append(executor);
			builder.append(", ");
		}
		if (lastRestart != null) {
			builder.append("lastRestart=");
			builder.append(lastRestart);
			builder.append(", ");
		}
		builder.append("maxFaults=");
		builder.append(maxFaults);
		builder.append(", ");
		if (pot != null) {
			builder.append("pot=");
			builder.append(pot);
			builder.append(", ");
		}
		if (startRetries != null) {
			builder.append("startRetries=");
			builder.append(startRetries);
			builder.append(", ");
		}
		if (stopped != null) {
			builder.append("stopped=");
			builder.append(stopped);
			builder.append(", ");
		}
		builder.append("watchDogInterval=");
		builder.append(watchDogInterval);
		builder.append(", watchDogTimeout=");
		builder.append(watchDogTimeout);
		builder.append("]");
		return builder.toString();
	}

}
