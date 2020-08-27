package org.ar4k.agent.tunnels.http.beacon.socket;

import static org.ar4k.agent.tunnels.http.beacon.socket.BeaconNetworkTunnel.trace;

import java.util.Date;
import java.util.Map;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.tunnels.http.grpc.beacon.MessageStatus;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage;

import io.grpc.stub.StreamObserver;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;

public class BeaconEndpointFromObserver implements StreamObserver<TunnelMessage>, AutoCloseable {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconEndpointFromObserver.class.toString());

	private final BeaconConnection beaconConnection;
	private boolean active = false;
	private long lastMessageReceivedFromServerBeacon = new Date().getTime();

	public BeaconEndpointFromObserver(BeaconConnection beaconConnection) {
		this.beaconConnection = beaconConnection;
		active = true;
	}

	@Override
	public void close() throws Exception {
		active = false;
		if (trace) {
			logger.info("close endpoint observer");
		}
	}

	public final boolean isOnline() {
		return active;
	}

	private final Map<Long, CachedChunk> getOutputCachedDataBase64ByMessageId() {
		return getNetworkReceiver().getOutputCachedDataBase64ByMessageId();
	}

	private final BeaconNetworkReceiver getNetworkReceiver() {
		return getBeaconNetworkTunnel().getNetworkReceiver();
	}

	private final BeaconNetworkTunnel getBeaconNetworkTunnel() {
		return beaconConnection.getBeaconNetworkTunnel();
	}

	@Override
	public void onNext(final TunnelMessage value) {
		if (trace) {
			logger.info("*********** onNext ( " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
					+ getBeaconNetworkTunnel().getTunnelId() + " )\nMESSAGE\n" + value + "\nSTATUS CACHE\n"
					+ getNetworkReceiver().reportDetails());
		}
		if (value.getMessageStatus().equals(MessageStatus.beaconLocalPing)) {
			lastMessageReceivedFromServerBeacon = new Date().getTime();
		} else {
			try {
				elaborateStatusMessageActions(value, value.getSessionId());
			} catch (final Exception a) {
				logger.logException(a);
			}
			final long messageUuid = value.getMessageUuid();
			if (active) {
				try {
					final long sessionId = value.getSessionId();
					if (trace)
						logger.info("onNext start working trasmission for " + getNetworkReceiver().getMyRoleMode()
								+ " TunnelMessage->[target:" + value.getTargeId() + ", session:" + sessionId
								+ ", payload:" + value.getPayload().length() + "]");
					if ((value.getMessageStatus().equals(MessageStatus.channelTransmission)
							|| value.getMessageStatus().equals(MessageStatus.channelTransmissionCompressed))
							&& value.getPayload() != null) {
						if (value.getPayload().hashCode() == value.getMessageHashCode()) {
							if (getNetworkReceiver().getMyRoleMode().equals(NetworkMode.SERVER) && (getNetworkReceiver()
									.getOrCreateServerSocketChannel(sessionId) == null
									|| !getNetworkReceiver().getOrCreateServerSocketChannel(sessionId).isActive())) {
								logger.warn("error in session server " + getNetworkReceiver().getMyRoleMode()
										+ " tunnel id " + getBeaconNetworkTunnel().getTunnelId() + "/" + sessionId);
							}
							if (!value.getPayload().isEmpty()) {
								final String stringData = value.getPayload();
								final int totalChunks = value.getTotalChunks();
								if (trace)
									logger.info("chunk progress: " + value.getChunk() + "/" + totalChunks + " id:"
											+ messageUuid);
								appendBytesToCache(sessionId, value, stringData);
								if (getOutputCachedDataBase64ByMessageId().containsKey(messageUuid)
										&& getOutputCachedDataBase64ByMessageId().get(messageUuid).isComplete()
										&& getNetworkReceiver().getLastAckSent() < messageUuid
										&& !getNetworkReceiver().getOutputCachedMessages().containsKey(messageUuid)) {
									getNetworkReceiver().getOutputCachedMessages().put(messageUuid,
											new MessageCached(MessageCached.MessageCachedType.TO_NETWORK,
													getNetworkReceiver(), getBeaconNetworkTunnel(), null,
													getNetworkReceiver().getMyRoleMode(), sessionId, messageUuid, null,
													getOutputCachedDataBase64ByMessageId().get(messageUuid)
															.getCompleteData(),
													totalChunks, value.getOriginalSize(), value.getMessageStatus()));
									getBeaconNetworkTunnel().nextAction(null);
								} else {
									if (getNetworkReceiver().getOutputCachedMessages().containsKey(messageUuid)) {
										if (trace)
											logger.info("- message - " + getNetworkReceiver().getOutputCachedMessages()
													.get(messageUuid).toString());
									}
									if (trace)
										logger.info("onNext handler for " + getNetworkReceiver().getMyRoleMode()
												+ " tunnel id " + getBeaconNetworkTunnel().getTunnelId() + "/"
												+ sessionId + "\nis not able to cache message " + messageUuid
												+ " because\ngetOutputCachedDataBase64ByMessageId().containsKey(messageUuid) "
												+ getOutputCachedDataBase64ByMessageId().containsKey(messageUuid)
												+ "\ngetOutputCachedDataBase64ByMessageId().get(messageUuid).isComplete() "
												+ getOutputCachedDataBase64ByMessageId().get(messageUuid).isComplete()
												+ "\ngetLastAckSent() < messageUuid "
												+ (getNetworkReceiver().getLastAckSent() < messageUuid)
												+ "\n!getOutputCachedMessages().containsKey(messageUuid) "
												+ !getNetworkReceiver().getOutputCachedMessages()
														.containsKey(messageUuid));
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
						if (trace)
							logger.info("message with no data of type " + value.getMessageStatus());
					}
				} catch (final Exception clientEx) {
					logger.warn("onNext handler for " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
							+ getBeaconNetworkTunnel().getTunnelId() + " exception on message " + messageUuid + "\n"
							+ EdgeLogger.stackTraceToString(clientEx));
					getNetworkReceiver().sendExceptionMessage(value.getSessionId(),
							getBeaconNetworkTunnel().getTunnelId(), value.getMessageUuid(), clientEx);
				}
			} else {
				logger.warn("--------- received message on closed observer. Message: " + value);
			}
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void elaborateStatusMessageActions(TunnelMessage value, final long sessionId) {
		lastMessageReceivedFromServerBeacon = new Date().getTime();
		final String messageUuid = value.getPayload();
		switch (value.getMessageStatus()) {
		case UNRECOGNIZED:
			logger.warn("STATUS: UNRECOGNIZED ( " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
					+ getBeaconNetworkTunnel().getTunnelId() + " sessionId " + sessionId + " )");
			break;
		case beaconChannelRequest:
			if (trace)
				logger.info("REQUEST: beaconChannelRequest ( " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
						+ getBeaconNetworkTunnel().getTunnelId() + " sessionId " + sessionId + " )");
			break;
		case beaconMessageAck:
			if (trace)
				logger.info("STATUS: beaconMessageAck ( " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
						+ getBeaconNetworkTunnel().getTunnelId() + " sessionId " + sessionId + " )");
			if (messageUuid != null && getBeaconNetworkTunnel() != null && getNetworkReceiver() != null) {
				getNetworkReceiver().confirmPacketReceived(Long.valueOf(messageUuid), value.getMessageUuid());
			} else {
				logger.warn("ack message with null payload or closed network");
			}
			break;
		case beaconMessageControl:
			if (messageUuid != null) {
				getNetworkReceiver().confirmPacketReceived(Long.valueOf(messageUuid), value.getMessageUuid());
			} else {
				logger.warn("control message with null payload!");
			}
			break;
		case channelActive:
			if (trace)
				logger.info("STATUS: channelActive ( " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
						+ getBeaconNetworkTunnel().getTunnelId() + "/" + sessionId + " )");
			break;
		case channelInactive:
			if (trace)
				logger.info("STATUS: channelInactive ( " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
						+ getBeaconNetworkTunnel().getTunnelId() + "/" + sessionId + " )");
			if (getNetworkReceiver().getMyRoleMode().equals(NetworkMode.CLIENT)) {
				if (getNetworkReceiver().getOrCreateClientHandler(sessionId) != null
						&& getNetworkReceiver().getOrCreateClientHandler(sessionId).channel() != null
						&& getNetworkReceiver().getOrCreateClientHandler(sessionId).channel().isActive()) {
					getNetworkReceiver().getOrCreateClientHandler(sessionId).channel()
							.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
					getNetworkReceiver().deleteClientHandler(sessionId);
				} else {
					if (getNetworkReceiver().getOrCreateServerSocketChannel(sessionId) != null
							&& getNetworkReceiver().getOrCreateServerSocketChannel(sessionId).isActive()) {
						getNetworkReceiver().getOrCreateServerSocketChannel(sessionId)
								.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
					}
				}
			}
			break;
		case channelReadComplete:
			if (trace)
				logger.info("STATUS: channelReadComplete ( " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
						+ getBeaconNetworkTunnel().getTunnelId() + "/" + sessionId + " )");
			break;
		case channelRegistered:
			if (trace)
				logger.info("STATUS: channelRegistered ( " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
						+ getBeaconNetworkTunnel().getTunnelId() + "/" + sessionId + " )");
			break;
		case channelTransmission:
			break;
		case channelTransmissionCompressed:
			break;
		case channelUnregistered:
			if (trace)
				logger.info("STATUS: channelUnregistered ( " + getNetworkReceiver().getMyRoleMode() + " tunnel id "
						+ getBeaconNetworkTunnel().getTunnelId() + "/" + sessionId + " )");
			break;
		case channelWritabilityChanged:
			if (trace)
				logger.info("STATUS: channelWritabilityChanged ( " + getNetworkReceiver().getMyRoleMode()
						+ " tunnel id " + getBeaconNetworkTunnel().getTunnelId() + "/" + sessionId + " )");
			break;
		case exceptionCaught:
			logger.warn("STATUS: exceptionCaught " + messageUuid + " ( " + getNetworkReceiver().getMyRoleMode()
					+ " tunnel id " + getBeaconNetworkTunnel().getTunnelId() + "/" + sessionId + " )");
			if (messageUuid != null) {
				getNetworkReceiver().exceptionPacketReceived(Long.valueOf(messageUuid));
			} else {
				logger.warn("exception message with null payload!");
			}
			break;
		case userEventTriggered:
			if (trace)
				logger.info("STATUS: userEventTriggered " + messageUuid + " ( " + getNetworkReceiver().getMyRoleMode()
						+ " tunnel id " + getBeaconNetworkTunnel().getTunnelId() + "/" + sessionId + " )");
			break;
		}
	}

	private void appendBytesToCache(long sessionId, TunnelMessage tunnel, String data) {
		if (getOutputCachedDataBase64ByMessageId().containsKey(tunnel.getMessageUuid())
				&& getOutputCachedDataBase64ByMessageId().get(tunnel.getMessageUuid()) != null) {
			getOutputCachedDataBase64ByMessageId().get(tunnel.getMessageUuid()).addData(tunnel.getChunk(), data);
		} else {
			getOutputCachedDataBase64ByMessageId().put(tunnel.getMessageUuid(),
					new CachedChunk(tunnel.getChunk(), tunnel.getTotalChunks(), data));
		}
	}

	@Override
	public void onError(Throwable t) {
		active = false;
		if (trace) {
			logger.info("onError in BeaconEndpointObserver for message error " + t.getMessage());
		}
		if (active) {
			logger.warn("error for tunnel id " + String.valueOf(getBeaconNetworkTunnel().getTunnelId()) + " "
					+ t.getMessage());
			beaconConnection.receivedOnErrorInFromObserver();
			try {
				close();
			} catch (final Exception e) {
				logger.logException(e);
			}
		}
	}

	@Override
	public void onCompleted() {
		active = false;
		if (trace) {
			logger.info("onCompleted in BeaconEndpointObserver");
		}
		if (trace)
			logger.info("complete for tunnel id " + String.valueOf(getBeaconNetworkTunnel().getTunnelId()));
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

	public long getLastMessageReceivedFromServerBeacon() {
		return lastMessageReceivedFromServerBeacon;
	}

}
