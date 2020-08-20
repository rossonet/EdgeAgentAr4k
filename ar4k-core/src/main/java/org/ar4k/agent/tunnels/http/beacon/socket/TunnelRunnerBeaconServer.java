package org.ar4k.agent.tunnels.http.beacon.socket;

import static org.ar4k.agent.tunnels.http.beacon.socket.BeaconNetworkTunnel.trace;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.MessageStatus;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage;

import io.grpc.stub.StreamObserver;

/**
 * gestione tunnel lato server
 *
 * @author andrea
 *
 */
public class TunnelRunnerBeaconServer implements AutoCloseable {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(TunnelRunnerBeaconServer.class.toString());

	private final long tunnelId;
	private final Agent serverAgent;
	private final Agent clientAgent;
	private StreamObserver<TunnelMessage> serverObserver = null;
	private StreamObserver<TunnelMessage> clientObserver = null;
	private long serverUuid = 0;
	private long clientUuid = 0;
	private final ExecutorService executor = Executors.newCachedThreadPool();

	public TunnelRunnerBeaconServer(long tunnelId, Agent server, Agent client) {
		this.serverAgent = server;
		this.clientAgent = client;
		if (trace)
			logger.info("tunnel id " + tunnelId + " created");
		this.tunnelId = tunnelId;
	}

	public long getTunnelId() {
		return tunnelId;
	}

	public Agent getServerAgent() {
		return serverAgent;
	}

	public void onNext(TunnelMessage value, StreamObserver<TunnelMessage> responseObserver) {
		try {
			cleanFromMessage(value, responseObserver);
			reportDetails();
			executor.submit(new Runnable() {
				@Override
				public void run() {
					try {
						deliveryMessage(value, responseObserver);
					} catch (final Exception e) {
						logger.logException("IN DELIVERY MESSAGE", e);
					}

				}
			});
		} catch (final Exception sr) {
			logger.logException("ERROR ON NEXT BEACON HUB", sr);
		}
	}

	private void deliveryMessage(TunnelMessage value, StreamObserver<TunnelMessage> responseObserver)
			throws InterruptedException {
		final StreamObserver<TunnelMessage> nextAgent = waitNextAgentObserver(value, responseObserver);
		if (nextAgent != null) {
			if (trace) {
				logger.info("Stream observer found");
			}
			nextAgent.onNext(value);
			if (trace)
				logger.info("DONE ON NEXT\nPAYLOAD: " + value);

		} else {
			if (trace)
				logger.info("Stream observer not found for " + value);
		}
	}

	private StreamObserver<TunnelMessage> waitNextAgentObserver(TunnelMessage value,
			StreamObserver<TunnelMessage> responseObserver) throws InterruptedException {
		final long startCheck = new Date().getTime();
		StreamObserver<TunnelMessage> nextAgent = getNextAgentObserver(value, responseObserver);
		while (nextAgent == null && (startCheck + BeaconNetworkTunnel.SYNC_TIME_OUT) > new Date().getTime()) {
			Thread.sleep(BeaconNetworkTunnel.WAIT_WHILE_DELAY);
			logger.info("waiting on server hub");
			nextAgent = getNextAgentObserver(value, responseObserver);
		}
		return nextAgent;
	}

	private void cleanFromMessage(TunnelMessage value, StreamObserver<TunnelMessage> responseObserver) {
		if (value.getMessageStatus().equals(MessageStatus.closeRequestClient)
				&& clientObserver.equals(responseObserver)) {
			cleanClient();
		}
		if (value.getMessageStatus().equals(MessageStatus.closeRequestServer)
				&& serverObserver.equals(responseObserver)) {
			cleanServer();
		}
	}

	private synchronized StreamObserver<TunnelMessage> getNextAgentObserver(TunnelMessage value,
			StreamObserver<TunnelMessage> responseObserver) {
		reportDetails();
		StreamObserver<TunnelMessage> nextAgentObserver = null;
		if (!serverAgent.equals(clientAgent)) {
			if (trace)
				logger.info("client and service host are different");
			if (value.getAgentSource().equals(serverAgent)) {
				if (trace)
					logger.info("request from server by Agent [s:" + value.getAgentSource() + " d:"
							+ value.getAgentDestination() + "]");
				saveServerIfNull(value, responseObserver);
				nextAgentObserver = assignClientToNext(value, nextAgentObserver);
			}
			if (value.getAgentSource().equals(clientAgent)) {
				if (trace)
					logger.info("request from client by Agent [s:" + value.getAgentSource() + " d:"
							+ value.getAgentDestination() + "]");
				saveClientIfNull(value, responseObserver);
				nextAgentObserver = assignServerToNext(nextAgentObserver);
			}
		}
		if (nextAgentObserver == null && serverAgent.equals(clientAgent)) {
			// TODO da provare il giro su una sola macchina
			if (trace)
				logger.info("client and service host uniqueId are equal");
			switch (value.getMessageType()) {
			case FROM_CLIENT:
				if (trace) {
					logger.info("request from client by UUID");
					logger.info("UUID Message " + value.getUuid());
				}
				saveClientIfNull(value, responseObserver);
				nextAgentObserver = assignServerToNext(nextAgentObserver);
				break;
			case FROM_SERVER:
				if (trace) {
					logger.info("request from server by UUID");
					logger.info("UUID Message " + value.getUuid());
				}
				saveServerIfNull(value, responseObserver);
				nextAgentObserver = assignClientToNext(value, nextAgentObserver);
				break;
			case UNRECOGNIZED:
				logger.warn(value.getMessageType() + " is unrecognized");
				break;
			default:
				logger.warn(value.getMessageType() + " is not valid");
			}
		}
		if (trace)
			logger.info("nextAgentObserver -> " + (nextAgentObserver != null ? nextAgentObserver.hashCode() : "NaN"));
		reportDetails();
		return nextAgentObserver;
	}

	private StreamObserver<TunnelMessage> assignServerToNext(StreamObserver<TunnelMessage> nextAgentObserver) {
		if (serverObserver != null) {
			nextAgentObserver = serverObserver;
		} else {
			if (trace)
				logger.info("serverObserver not found in agent\nserverAgent -> " + serverAgent.toString()
						+ "\nclientAgent -> " + clientAgent.toString());
		}
		return nextAgentObserver;
	}

	private void saveClientIfNull(TunnelMessage value, StreamObserver<TunnelMessage> responseObserver) {
		if (clientObserver == null) {
			clientObserver = responseObserver;
			if (trace)
				logger.info("save client on null " + clientObserver + " [sessionId:" + value.getSessionId()
						+ ",tunnelId:" + value.getTargeId() + "]");
		}
	}

	private StreamObserver<TunnelMessage> assignClientToNext(TunnelMessage value,
			StreamObserver<TunnelMessage> nextAgentObserver) {
		if (clientObserver != null) {
			nextAgentObserver = clientObserver;
		} else {
			if (trace)
				logger.info(
						"assign null client on sessionId:" + value.getSessionId() + ",tunneltId:" + value.getTargeId());
		}
		return nextAgentObserver;
	}

	private void saveServerIfNull(TunnelMessage value, StreamObserver<TunnelMessage> responseObserver) {
		if (serverObserver == null) {
			serverObserver = responseObserver;
			if (trace)
				logger.info("save server " + serverObserver + " [sessionId:" + value.getSessionId() + ",tunnelId:"
						+ value.getTargeId() + "]");
		}
	}

	private void reportDetails() {
		if (trace) {
			final StringBuilder sb = new StringBuilder();
			sb.append("\n--------------------\nDETAILS TunnelRunnerBeaconServer\n");
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

	public Agent getClientAgent() {
		return clientAgent;
	}

	public void onError(Throwable t, StreamObserver<TunnelMessage> responseObserver) {
		logger.logException("Error on TunnelRunnerBeaconServer id " + responseObserver.hashCode() + " -> " + tunnelId,
				t);
		if (responseObserver.equals(serverObserver)) {
			cleanServer();
		}
		if (responseObserver.equals(clientObserver)) {
			cleanClient();
		}

	}

	public void onCompleted(StreamObserver<TunnelMessage> responseObserver) {
		if (trace)
			logger.info("Complete on TunnelRunnerBeaconServer id " + responseObserver.hashCode() + " -> " + tunnelId);
		closeChannelAction();
	}

	@Override
	public void close() throws Exception {
		closeChannelAction();
		executor.shutdownNow();
		if (trace)
			logger.info("TunnelRunnerBeaconServer id " + tunnelId + " closed");
	}

	private void closeChannelAction() {
		cleanServer();
		cleanClient();
	}

	private void cleanClient() {
		if (clientObserver != null) {
			if (trace)
				logger.info("############### TunnelRunnerBeaconServer id " + tunnelId + " remove client");
			clientObserver = null;
		}
	}

	private void cleanServer() {
		if (serverObserver != null) {
			if (trace)
				logger.info("############### TunnelRunnerBeaconServer id " + tunnelId + " remove server");
			serverObserver = null;
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
}
