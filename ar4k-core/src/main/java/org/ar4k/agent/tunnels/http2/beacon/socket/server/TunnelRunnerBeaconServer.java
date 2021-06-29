package org.ar4k.agent.tunnels.http2.beacon.socket.server;

import static org.ar4k.agent.tunnels.http2.beacon.socket.netty.BeaconNettyNetworkTunnel.TRACE_LOG_IN_INFO;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http2.beacon.socket.netty.BeaconNettyNetworkTunnel;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http2.grpc.beacon.MessageStatus;
import org.ar4k.agent.tunnels.http2.grpc.beacon.TunnelMessage;

import io.grpc.stub.StreamObserver;

public class TunnelRunnerBeaconServer implements AutoCloseable {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(TunnelRunnerBeaconServer.class);

	private final long tunnelId;
	private final Agent serverAgent;
	private final Agent clientAgent;
	private StreamObserver<TunnelMessage> serverObserver = null;
	private StreamObserver<TunnelMessage> clientObserver = null;
	private AtomicLong serverObserverLastTime = new AtomicLong(0);
	private AtomicLong clientObserverLastTime = new AtomicLong(0);
	private long serverUuid = 0;
	private long clientUuid = 0;
	private final ExecutorService executor = Executors.newCachedThreadPool();
	private Future<Void> pingWorker = null;

	private boolean closed = false;

	private final class RunnableOnNext implements Runnable {
		private final TunnelMessage value;
		private final StreamObserver<TunnelMessage> responseObserver;

		private RunnableOnNext(TunnelMessage value, StreamObserver<TunnelMessage> responseObserver) {
			this.value = value;
			this.responseObserver = responseObserver;
		}

		private final void deliveryMessage(TunnelMessage value, StreamObserver<TunnelMessage> responseObserver)
				throws InterruptedException {
			final ObserverAndLastData nextAgent = waitNextAgentObserver(value, responseObserver);
			if (nextAgent != null) {
				onNextSynchro(value, nextAgent.getObserver());
				nextAgent.getCounter().set(new Date().getTime());
				if (TRACE_LOG_IN_INFO)
					logger.info("DONE ON NEXT - PAYLOAD:\n{}", value);

			} else {
				if (TRACE_LOG_IN_INFO)
					logger.info("Stream observer not found for {}", value);
			}
		}

		private ObserverAndLastData waitNextAgentObserver(TunnelMessage value,
				StreamObserver<TunnelMessage> responseObserver) throws InterruptedException {
			final long startCheck = new Date().getTime();
			ObserverAndLastData nextAgent = getNextAgentObserver(value, responseObserver);
			boolean firstLoop = true;
			while (!closed && nextAgent == null
					&& (startCheck + BeaconNettyNetworkTunnel.SYNC_TIME_OUT) > new Date().getTime()) {
				if (!firstLoop) {
					Thread.sleep(BeaconNettyNetworkTunnel.WAIT_WHILE_DELAY);
					if (TRACE_LOG_IN_INFO)
						logger.info("waiting on server hub");
				}
				nextAgent = getNextAgentObserver(value, responseObserver);
				firstLoop = false;
			}
			return nextAgent;
		}

		@Override
		public void run() {
			Thread.currentThread().setName("t-" + value.getMessageId());
			try {
				deliveryMessage(value, responseObserver);
			} catch (final Exception e) {
				logger.logException("IN DELIVERY MESSAGE", e);
			}

		}
	}

	final class ObserverAndLastData {
		private final StreamObserver<TunnelMessage> observer;
		private final AtomicLong counter;

		ObserverAndLastData(StreamObserver<TunnelMessage> observer, AtomicLong counter) {
			this.observer = observer;
			this.counter = counter;
		}

		StreamObserver<TunnelMessage> getObserver() {
			return observer;
		}

		AtomicLong getCounter() {
			return counter;
		}

	}

	public TunnelRunnerBeaconServer(long tunnelId, Agent server, Agent client) {
		this.serverAgent = server;
		this.clientAgent = client;
		if (TRACE_LOG_IN_INFO)
			logger.info("tunnel id {} created", tunnelId);
		this.tunnelId = tunnelId;
		createPingChecker();
	}

	private void createPingChecker() {
		pingWorker = executor.submit(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				while (pingWorker != null) {
					Thread.sleep(BeaconNettyNetworkTunnel.LAST_MESSAGE_FROM_BEACON_SERVER_TIMEOUT);
					final TunnelMessage message = TunnelMessage.newBuilder()
							.setMessageStatus(MessageStatus.beaconLocalPing).build();
					if (serverObserver != null && serverObserverLastTime.get() != 0
							&& (serverObserverLastTime.get()
									+ BeaconNettyNetworkTunnel.LAST_MESSAGE_FROM_BEACON_SERVER_TIMEOUT < new Date()
											.getTime())) {
						onNextSynchro(message, serverObserver);
						serverObserverLastTime.set(new Date().getTime());
					}
					if (clientObserver != null && clientObserverLastTime.get() != 0
							&& (clientObserverLastTime.get()
									+ BeaconNettyNetworkTunnel.LAST_MESSAGE_FROM_BEACON_SERVER_TIMEOUT < new Date()
											.getTime())) {
						onNextSynchro(message, clientObserver);
						clientObserverLastTime.set(new Date().getTime());
					}
				}
				return null;
			}

		});
	}

	public long getTunnelId() {
		return tunnelId;
	}

	public Agent getServerAgent() {
		return serverAgent;
	}

	public void onNext(TunnelMessage value, StreamObserver<TunnelMessage> responseObserver) {
		if (!closed)
			try {
				cleanFromMessage(value);
				reportDetails();
				final Runnable runnable = new RunnableOnNext(value, responseObserver);
				executor.submit(runnable);
			} catch (final Exception sr) {
				logger.logException("ERROR ON NEXT BEACON HUB", sr);
			}
	}

	private synchronized void onNextSynchro(TunnelMessage value, final StreamObserver<TunnelMessage> nextAgent) {
		nextAgent.onNext(value);
	}

	private void cleanFromMessage(TunnelMessage value) {
		if (value.getMessageStatus().equals(MessageStatus.closeRequestClient) && clientObserver != null) {
			cleanClient();
		}
		if (value.getMessageStatus().equals(MessageStatus.closeRequestServer) && serverObserver != null) {
			cleanServer();
		}
	}

	private synchronized ObserverAndLastData getNextAgentObserver(TunnelMessage value,
			StreamObserver<TunnelMessage> responseObserver) {
		reportDetails();
		ObserverAndLastData nextAgentObserver = null;
		if (!serverAgent.equals(clientAgent)) {
			if (TRACE_LOG_IN_INFO)
				logger.info("client and service host are different");
			if (value.getAgentSource().equals(serverAgent)) {
				saveServerIfNull(value, responseObserver);
				nextAgentObserver = assignClientToNext();
			}
			if (value.getAgentSource().equals(clientAgent)) {
				saveClientIfNull(value, responseObserver);
				nextAgentObserver = assignServerToNext();
			}
		}
		if (nextAgentObserver == null && serverAgent.equals(clientAgent)) {
			// TODO da provare il giro su una sola macchina
			if (TRACE_LOG_IN_INFO)
				logger.info("client and service host uniqueId are equal");
			switch (value.getMessageType()) {
			case FROM_CLIENT:
				if (TRACE_LOG_IN_INFO) {
					logger.info("request from client by UUID");
					logger.info("UUID Message " + value.getClassUuid());
				}
				saveClientIfNull(value, responseObserver);
				nextAgentObserver = assignServerToNext();
				break;
			case FROM_SERVER:
				if (TRACE_LOG_IN_INFO) {
					logger.info("request from server by UUID");
					logger.info("UUID Message " + value.getClassUuid());
				}
				saveServerIfNull(value, responseObserver);
				nextAgentObserver = assignClientToNext();
				break;
			case UNRECOGNIZED:
				logger.warn(value.getMessageType() + " is unrecognized");
				break;
			default:
				logger.warn(value.getMessageType() + " is not valid");
			}
		}
		if (TRACE_LOG_IN_INFO)
			logger.info("nextAgentObserver -> {}", (nextAgentObserver != null ? nextAgentObserver.hashCode() : "NaN"));
		reportDetails();
		return nextAgentObserver;
	}

	private ObserverAndLastData assignServerToNext() {
		ObserverAndLastData nextAgentObserver = null;
		if (serverObserver != null) {
			nextAgentObserver = new ObserverAndLastData(serverObserver, serverObserverLastTime);
		} else {
			if (TRACE_LOG_IN_INFO)
				logger.info("serverObserver not found in agent\nserverAgent -> " + serverAgent.toString()
						+ "\nclientAgent -> " + clientAgent.toString());
		}
		return nextAgentObserver;
	}

	private void saveClientIfNull(TunnelMessage value, StreamObserver<TunnelMessage> responseObserver) {
		if (clientObserver == null) {
			clientObserver = responseObserver;
			if (TRACE_LOG_IN_INFO)
				logger.info("save client on null " + clientObserver + " [sessionId:" + value.getSessionId()
						+ ",tunnelId:" + value.getTunnelId() + "]");
		}
	}

	private ObserverAndLastData assignClientToNext() {
		ObserverAndLastData nextAgentObserver = null;
		if (clientObserver != null) {
			nextAgentObserver = new ObserverAndLastData(clientObserver, clientObserverLastTime);
		} else {
			if (TRACE_LOG_IN_INFO)
				logger.info("clientObserver not found in agent\nserverAgent -> " + serverAgent.toString()
						+ "\nclientAgent -> " + clientAgent.toString());
		}
		return nextAgentObserver;
	}

	private void saveServerIfNull(TunnelMessage value, StreamObserver<TunnelMessage> responseObserver) {
		if (serverObserver == null) {
			serverObserver = responseObserver;
			if (TRACE_LOG_IN_INFO)
				logger.info("save server " + serverObserver + " [sessionId:" + value.getSessionId() + ",tunnelId:"
						+ value.getTunnelId() + "]");
		}
	}

	public Agent getClientAgent() {
		return clientAgent;
	}

	public void onError(Throwable t, StreamObserver<TunnelMessage> responseObserver) {
		if (responseObserver.equals(serverObserver)) {
			cleanServer();
		}
		if (responseObserver.equals(clientObserver)) {
			cleanClient();
		}
		logger.logException("Error on TunnelRunnerBeaconServer id " + responseObserver.hashCode() + " -> " + tunnelId,
				t);
	}

	public void onCompleted(StreamObserver<TunnelMessage> responseObserver) {
		closeChannelAction();
		if (TRACE_LOG_IN_INFO)
			logger.info("Completed on TunnelRunnerBeaconServer id {} -> {}", responseObserver.hashCode(), tunnelId);
	}

	@Override
	public void close() throws Exception {
		closed = true;
		closeChannelAction();
		if (pingWorker != null) {
			pingWorker.cancel(false);
			pingWorker = null;
		}
		executor.shutdownNow();
		if (TRACE_LOG_IN_INFO)
			logger.info("TunnelRunnerBeaconServer id {} closed", tunnelId);
	}

	private void closeChannelAction() {
		cleanServer();
		cleanClient();
	}

	private void cleanClient() {
		if (clientObserver != null) {
			clientObserver = null;
			if (TRACE_LOG_IN_INFO)
				logger.info("############### TunnelRunnerBeaconServer id {} removed client", tunnelId);
		}
	}

	private void cleanServer() {
		if (serverObserver != null) {
			serverObserver = null;
			if (TRACE_LOG_IN_INFO)
				logger.info("############### TunnelRunnerBeaconServer id {} removed server", tunnelId);
		}
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("TunnelRunnerBeaconServer [tunnelId=").append(tunnelId).append(", ");
		if (serverAgent != null)
			builder.append("serverAgent=").append(serverAgent).append(", ");
		if (clientAgent != null)
			builder.append("clientAgent=").append(clientAgent).append(", ");
		if (serverObserver != null)
			builder.append("serverObserver=").append(serverObserver).append(", ");
		if (clientObserver != null)
			builder.append("clientObserver=").append(clientObserver).append(", ");
		builder.append("serverUuid=").append(serverUuid).append(", clientUuid=").append(clientUuid).append("]");
		return builder.toString();
	}

	private void reportDetails() {
		if (TRACE_LOG_IN_INFO && logger.isInfoEnabled()) {
			final StringBuilder sb = new StringBuilder();
			sb.append("\n--------------------\n");
			sb.append(toString());
			sb.append("\nDETAILS OBSERVERS\n");
			sb.append("CLIENT uuid: " + clientUuid + " -> "
					+ (clientObserver != null ? clientObserver.hashCode() + " -> " + clientObserver.toString() : "NaN")
					+ "\n");
			sb.append("SERVER uuid: " + serverUuid + " -> "
					+ (serverObserver != null ? serverObserver.hashCode() + " -> " + serverObserver.toString() : "NaN")
					+ "\n");
			sb.append("\n--------------------\n");
			logger.info(sb.toString());
		}
	}
}
