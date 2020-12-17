package org.ar4k.agent.tunnels.http2.beacon.socket.classic;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.ar4k.agent.config.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http2.grpc.beacon.MessageStatus;
import org.ar4k.agent.tunnels.http2.grpc.beacon.TunnelMessage;

import io.grpc.stub.StreamObserver;

public class BeaconClassicEndpointFromObserver implements StreamObserver<TunnelMessage>, AutoCloseable {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconClassicEndpointFromObserver.class.toString());

	private final BeaconClassicConnection beaconClassicConnection;
	private boolean active = false;
	private long lastMessageReceivedFromServerBeacon = new Date().getTime();

	BeaconClassicEndpointFromObserver(BeaconClassicConnection beaconClassicConnection) {
		this.beaconClassicConnection = beaconClassicConnection;
		active = true;
	}

	@Override
	public void close() throws Exception {
		active = false;
		if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO) {
			logger.info("close endpoint observer");
		}
	}

	final boolean isOnline() {
		return active;
	}

	private final Map<Long, CachedChunkClassic> getOutputCachedDataBase64ByMessageId() {
		return getNetworkReceiver().getOutputCachedDataBase64ByMessageId();
	}

	private final BeaconNetworkClassicTunnel getNetworkReceiver() {
		return getBeaconNetworkTunnel();
	}

	private final BeaconNetworkClassicTunnel getBeaconNetworkTunnel() {
		return beaconClassicConnection.getBeaconNetworkTunnel();
	}

	@Override
	public void onNext(final TunnelMessage value) {
		if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO) {
			final String reportDetails = getBeaconNetworkTunnel().reportDetails();
			logger.info("*********** onNext ( {} tunnel id {} )\nMESSAGE\n{}\nSTATUS CACHE{}\n",
					getNetworkReceiver().getMyRoleMode(), getBeaconNetworkTunnel().getTunnelId(), value, reportDetails);
		}
		if (value.getMessageStatus().equals(MessageStatus.beaconLocalPing)) {
			lastMessageReceivedFromServerBeacon = new Date().getTime();
		} else {
			try {
				elaborateStatusMessageActions(value, value.getSessionId());
			} catch (final Exception a) {
				logger.logException(a);
			}
			final long messageUuid = value.getMessageId();
			if (active) {
				try {
					final long sessionId = value.getSessionId();
					if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO)
						logger.info(
								"onNext start working trasmission for {} TunnelMessage->[tunnelId: {}, sessionId: {}, payload: {} ]",
								getNetworkReceiver().getMyRoleMode(), value.getTunnelId(), sessionId,
								value.getPayload().length());
					if ((value.getMessageStatus().equals(MessageStatus.channelTransmission)
							|| value.getMessageStatus().equals(MessageStatus.channelTransmissionCompressed))
							&& value.getPayload() != null) {
						if (value.getPayload().hashCode() == value.getMessageHashCode()) {
							if (getNetworkReceiver().getMyRoleMode().equals(NetworkMode.SERVER)
									&& (getNetworkReceiver().getOrCreateServerSocketChannel(sessionId) == null
											|| !getNetworkReceiver().isActive(sessionId))) {
								logger.warn("error in session server " + getNetworkReceiver().getMyRoleMode()
										+ " tunnel id " + getBeaconNetworkTunnel().getTunnelId() + "/" + sessionId);
							}
							if (!value.getPayload().isEmpty()) {
								final String stringData = value.getPayload();
								final int totalChunks = value.getTotalChunks();
								if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO)
									logger.info("chunk progress: " + value.getChunk() + "/" + totalChunks + " id:"
											+ messageUuid);
								appendBytesToCache(value, stringData);
								if (getOutputCachedDataBase64ByMessageId().containsKey(messageUuid)
										&& getOutputCachedDataBase64ByMessageId().get(messageUuid).isComplete()
										&& getNetworkReceiver().getLastAckSent(sessionId) < messageUuid
										&& !containsOutputCachedMessage(messageUuid, sessionId)) {
									/*
									 * getBeaconNetworkTunnel().addOutputCachedMessages(sessionId, messageUuid,
									 * 
									 * new MessageCached(MessageCached.MessageCachedType.TO_NETWORK,
									 * getNetworkReceiver(), getBeaconNetworkTunnel(), null,
									 * getNetworkReceiver().getMyRoleMode(), sessionId, messageUuid, null,
									 * getOutputCachedDataBase64ByMessageId().get(messageUuid) .getCompleteData(),
									 * totalChunks, value.getOriginalSize(), value.getMessageStatus()));
									 */
									// TODO cahe message
									getBeaconNetworkTunnel().nextAction();
								} else {
									if (containsOutputCachedMessage(messageUuid, sessionId)) {
										if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO)
											logger.info("- message - " + getBeaconNetworkTunnel()
													.getOutputCachedMessages(sessionId).get(messageUuid).toString());
									}
									if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO)
										logger.info("onNext handler for " + getNetworkReceiver().getMyRoleMode()
												+ " tunnel id " + getBeaconNetworkTunnel().getTunnelId() + "/"
												+ sessionId + "\nis not able to cache message " + messageUuid
												+ " because\ngetOutputCachedDataBase64ByMessageId().containsKey(messageUuid) "
												+ getOutputCachedDataBase64ByMessageId().containsKey(messageUuid)
												+ "\ngetOutputCachedDataBase64ByMessageId().get(messageUuid).isComplete() "
												+ getOutputCachedDataBase64ByMessageId().get(messageUuid).isComplete()
												+ "\ngetLastAckSent() < messageUuid "
												+ (getNetworkReceiver().getLastAckSent(sessionId) < messageUuid)
												+ "\n!getOutputCachedMessages().containsKey(messageUuid) "
												+ !containsOutputCachedMessage(messageUuid, sessionId));
								}
							} else {
								logger.warn("onNext handler for " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
										+ getBeaconNetworkTunnel().getTunnelId() + "/" + sessionId
										+ " hashcode is wrong\nhashcode calculate on payload:"
										+ value.getPayload().hashCode() + "\n hashcode on message:"
										+ value.getMessageHashCode());
							}
						} else {
							logger.warn("onNext handler for " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
									+ getBeaconNetworkTunnel().getTunnelId() + "/" + sessionId + " has null payload: "
									+ value.getPayload());
						}
					} else {
						if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO)
							logger.info("message with no data of type {}", value.getMessageStatus());
					}
				} catch (final Exception clientEx) {
					logger.warn("onNext handler for " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
							+ getBeaconNetworkTunnel().getTunnelId() + " exception on message " + messageUuid + "\n"
							+ EdgeLogger.stackTraceToString(clientEx));
					getNetworkReceiver().sendExceptionMessage(value.getSessionId(),
							getBeaconNetworkTunnel().getTunnelId(), value.getMessageId(), clientEx);
				}
			} else {
				logger.warn("--------- received message on closed observer. Message: {}", value);

			}
		}
	}

	private boolean containsOutputCachedMessage(final long messageUuid, final long sessionId) {
		return getBeaconNetworkTunnel().getOutputCachedMessages(sessionId).containsKey(messageUuid);
	}

	private void elaborateStatusMessageActions(final TunnelMessage value, final long sessionId)
			throws InterruptedException, ExecutionException, TimeoutException {
		lastMessageReceivedFromServerBeacon = new Date().getTime();
		switch (value.getMessageStatus()) {
		case UNRECOGNIZED:
			logger.warn("STATUS: UNRECOGNIZED ( " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
					+ getBeaconNetworkTunnel().getTunnelId() + " sessionId " + sessionId + " )");
			break;
		case beaconChannelRequest:
			if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO)
				logger.info("REQUEST: beaconChannelRequest ( " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
						+ getBeaconNetworkTunnel().getTunnelId() + " sessionId " + sessionId + " )");
			break;
		case beaconMessageAck:
			if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO)
				logger.info("STATUS: beaconMessageAck ( " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
						+ getBeaconNetworkTunnel().getTunnelId() + " sessionId " + sessionId + " )");
			if (value.getMessageAckId() != 0 && getBeaconNetworkTunnel() != null && getNetworkReceiver() != null) {
				getNetworkReceiver().confirmPacketReceived(sessionId, value.getMessageAckId(),
						value.getMessageAckReceivedId());
			} else {
				logger.warn("ack message with null payload or closed network");
			}
			break;
		case beaconMessageControl:
			if (value.getMessageAckId() != 0) {
				getNetworkReceiver().confirmPacketReceived(sessionId, value.getMessageAckId(),
						value.getMessageAckReceivedId());
			} else {
				logger.warn("control message with null payload!");
			}
			break;
		case channelActive:
			if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO)
				logger.info("STATUS: channelActive ( " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
						+ getBeaconNetworkTunnel().getTunnelId() + "/" + sessionId + " )");
			if (getNetworkReceiver().getMyRoleMode().equals(NetworkMode.SERVER)) {
				getNetworkReceiver().getOrCreateServerSocketChannel(sessionId);
			} else {
				getNetworkReceiver().getOrCreateClientHandler(sessionId);
			}
			break;
		case channelInactive:
			if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO)
				logger.info("STATUS: channelInactive ( " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
						+ getBeaconNetworkTunnel().getTunnelId() + "/" + sessionId + " )");
			// TODO close channel
			break;
		case channelReadComplete:
			if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO)
				logger.info("STATUS: channelReadComplete ( " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
						+ getBeaconNetworkTunnel().getTunnelId() + "/" + sessionId + " )");
			break;
		case channelRegistered:
			if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO)
				logger.info("STATUS: channelRegistered ( " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
						+ getBeaconNetworkTunnel().getTunnelId() + "/" + sessionId + " )");
			break;
		case channelTransmission:
			break;
		case channelTransmissionCompressed:
			break;
		case channelUnregistered:
			if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO)
				logger.info("STATUS: channelUnregistered ( " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
						+ getBeaconNetworkTunnel().getTunnelId() + "/" + sessionId + " )");
			break;
		case channelWritabilityChanged:
			if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO)
				logger.info("STATUS: channelWritabilityChanged ( " + getNetworkReceiver().getMyRoleMode()
						+ " tunnel id " + getBeaconNetworkTunnel().getTunnelId() + "/" + sessionId + " )");
			break;
		case exceptionCaught:
			logger.warn(
					"STATUS: exceptionCaught " + value.getMessageAckId() + " ( " + getNetworkReceiver().getMyRoleMode()
							+ " tunnel id " + getBeaconNetworkTunnel().getTunnelId() + "/" + sessionId + " )");
			if (value.getMessageAckId() != 0) {
				getNetworkReceiver().exceptionPacketReceived(sessionId, value.getMessageAckId());
			} else {
				logger.warn("exception message with null payload!");
			}
			break;
		case userEventTriggered:
			if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO)
				logger.info("STATUS: userEventTriggered " + value.getMessageAckId() + " ( "
						+ getNetworkReceiver().getMyRoleMode() + " tunnel id " + getBeaconNetworkTunnel().getTunnelId()
						+ "/" + sessionId + " )");
			break;
		default:
			break;
		}
	}

	private void appendBytesToCache(final TunnelMessage tunnel, final String data) {
		if (getOutputCachedDataBase64ByMessageId().containsKey(tunnel.getMessageId())
				&& getOutputCachedDataBase64ByMessageId().get(tunnel.getMessageId()) != null) {
			getOutputCachedDataBase64ByMessageId().get(tunnel.getMessageId()).addData(tunnel.getChunk(), data);
		} else {
			getOutputCachedDataBase64ByMessageId().put(tunnel.getMessageId(),
					new CachedChunkClassic(tunnel.getChunk(), tunnel.getTotalChunks(), data));
		}
	}

	@Override
	public void onError(Throwable t) {
		if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO) {
			logger.info("onError in BeaconEndpointObserver for message error ", t.getMessage());
		}
		if (active) {
			logger.warn("error for tunnel id {} message\n{}", getBeaconNetworkTunnel().getTunnelId(), t.getMessage());
			beaconClassicConnection.receivedOnErrorInFromObserver();
			try {
				close();
			} catch (final Exception e) {
				logger.logException(e);
			}
		}
		active = false;
	}

	@Override
	public void onCompleted() {
		active = false;
		if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO) {
			logger.info("onCompleted in BeaconEndpointObserver");
		}
		if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO)
			logger.info("complete for tunnel id ", getBeaconNetworkTunnel().getTunnelId());
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("BeaconEndpointObserver [tunnelId=").append(getBeaconNetworkTunnel().getTunnelId()).append(", ");
		if (getNetworkReceiver().getMyRoleMode() != null)
			builder.append("myRoleMode=").append(getNetworkReceiver().getMyRoleMode()).append(", ");
		builder.append("active=").append(active).append("]");
		return builder.toString();
	}

	long getLastMessageReceivedFromServerBeacon() {
		return lastMessageReceivedFromServerBeacon;
	}

}
