package org.ar4k.agent.tunnels.http.beacon.socket;

import static org.ar4k.agent.tunnels.http.beacon.socket.BeaconNetworkTunnel.trace;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.io.IOUtils;
import org.ar4k.agent.exception.ExceptionNetworkEvent;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.network.NetworkReceiver;
import org.ar4k.agent.tunnels.http.grpc.beacon.MessageStatus;
import org.ar4k.agent.tunnels.http.grpc.beacon.MessageType;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage;

import com.google.common.io.ByteSource;

import io.grpc.stub.StreamObserver;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;

public class BeaconEndpointObserver implements StreamObserver<TunnelMessage>, AutoCloseable {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconEndpointObserver.class.toString());

	// valore unico per differenziare gli oggetti
	private final int uniqueId = new Random().nextInt();

	private final NetworkReceiver networkReceiver;
	private final long tunnelId;
	private final NetworkMode myRoleMode;
	private final BeaconNetworkTunnel beaconNetworkTunnel;

	private boolean active = false;

	private final Map<Long, CachedChunk> cachedDataBase64ByMessageId = new ConcurrentHashMap<>();

	private final ExecutorService executor = Executors.newCachedThreadPool();

	public BeaconEndpointObserver(BeaconNetworkTunnel beaconNetworkTunnel, long tunnelId, NetworkReceiver hub,
			NetworkMode myRoleMode, long uuidRandom) {
		this.tunnelId = tunnelId;
		this.networkReceiver = hub;
		this.beaconNetworkTunnel = beaconNetworkTunnel;
		this.myRoleMode = myRoleMode;
		active = true;
	}

	@Override
	public void close() throws Exception {
		active = false;
		executor.shutdownNow();
		cachedDataBase64ByMessageId.clear();
		if (trace) {
			logger.info("close Observer");
		}
	}

	public boolean isOnline() {
		return active;
	}

	@Override
	public void onNext(TunnelMessage value) {
		if (trace) {
			logger.info("onNext in BeaconEndpointObserver for message id " + value.getMessageUuid());
		}
		if (active) {
			try {
				elaborateStatusMessage(value, value.getSessionId());
			} catch (final Exception a) {
				logger.logException(a);
			}
			try {
				final long sessionId = value.getSessionId();
				if (trace)
					logger.info("In role " + myRoleMode + " TunnelMessage->[target:" + value.getTargeId() + ", session:"
							+ sessionId + ", payload:" + value.getPayload().length() + "]");
				if (value.getPayload() != null && (value.getMessageStatus().equals(MessageStatus.channelTransmission)
						|| value.getMessageStatus().equals(MessageStatus.channelTransmissionCompressed))) {
					if (myRoleMode.equals(NetworkMode.SERVER)
							&& (networkReceiver.getOrCreateServerSocketChannel(sessionId) == null
									|| !networkReceiver.getOrCreateServerSocketChannel(sessionId).isActive())) {
						logger.warn(
								"error in session server " + myRoleMode + " tunnel id " + tunnelId + "/" + sessionId);
						networkReceiver.close();
						throw new ExceptionNetworkEvent("NO SERVER SOCKET " + myRoleMode + " tunnel id " + tunnelId
								+ "/" + sessionId + " for message id " + value.getMessageUuid());
					}
					if (!value.getPayload().isEmpty()) {
						final String stringData = value.getPayload();
						if (trace)
							logger.info("chunk progress: " + value.getChunk() + "/" + value.getTotalChunks() + " id:"
									+ value.getMessageUuid());
						appendBytesToCache(sessionId, value, stringData);
						if (cachedDataBase64ByMessageId.containsKey(value.getMessageUuid())
								&& cachedDataBase64ByMessageId.get(value.getMessageUuid()).isComplete()) {
							executor.submit(new Runnable() {
								@Override
								public void run() {
									try {
										deliveryMessageToNetwork(value, sessionId, stringData);
									} catch (final Exception e) {
										logger.logException("IN DELIVERY MESSAGE", e);
										exceptionMessage(value.getSessionId(), tunnelId, value.getMessageUuid(), e);
										throw new ExceptionNetworkEvent(
												"IN DELIVERY " + myRoleMode + " tunnel id " + tunnelId + "/" + sessionId
														+ " for message id " + value.getMessageUuid());
									}

								}
							});
							// thread.start();
						}
					} else {
						logger.warn("server handler for " + myRoleMode + " tunnel id " + tunnelId + "/" + "/"
								+ sessionId + " has null payload: " + value.getPayload());
					}
				} else {
					if (trace)
						logger.info("drop message with empty payload");
				}
			} catch (final Exception clientEx) {
				// exceptionMessage(value.getSessionId(), tunnelId, value.getMessageUuid(),
				// clientEx);
				throw new ExceptionNetworkEvent(
						"[" + value.getMessageUuid() + "] ERROR IN MESSAGE ON BEACON CLIENT " + clientEx.toString());
			}
		} else {
			logger.warn("--------- received message on closed observer. Message: " + value);
			final ExceptionNetworkEvent exception = new ExceptionNetworkEvent(
					"received message on closed observer. Message: " + value);
			// exceptionMessage(value.getSessionId(), tunnelId, value.getMessageUuid(),
			// exception);
			throw exception;
		}
	}

	private final void deliveryMessageToNetwork(TunnelMessage value, final long sessionId, final String stringData)
			throws IOException, InterruptedException {
		if (trace)
			logger.info("MESSAGE COMPLETED\n" + cachedDataBase64ByMessageId.get(value.getMessageUuid()) + "\n");
		final byte[] primitiveBytes = Base64.getDecoder()
				.decode(cachedDataBase64ByMessageId.get(value.getMessageUuid()).getCompleteData());
		byte[] decompressedBytes = null;
		if (value.getMessageStatus().equals(MessageStatus.channelTransmissionCompressed)) {
			decompressedBytes = decompress(primitiveBytes, value.getOriginalSize());
		}
		if (myRoleMode.equals(NetworkMode.CLIENT)) {
			if (networkReceiver.getOrCreateClientHandler(sessionId) != null) {
				networkReceiver.getOrCreateClientHandler(sessionId).channel().writeAndFlush(
						Unpooled.wrappedBuffer((decompressedBytes == null) ? primitiveBytes : decompressedBytes));
				// .await(BeaconNetworkTunnel.SYNC_TIME_OUT);
				if (trace) {
					logger.info("sent to channel " + networkReceiver.getOrCreateClientHandler(sessionId).isSuccess());
					logger.info("message on " + myRoleMode + " tunnel id " + tunnelId + "/" + sessionId + " sent\n"
							+ Hex.encodeHexString((decompressedBytes == null) ? primitiveBytes : decompressedBytes));
				}
			} else {
				logger.warn("client handler for " + myRoleMode + " tunnel id " + tunnelId + "/" + "/" + sessionId
						+ " is null");
				throw new ExceptionNetworkEvent("CLIENT HANDLER IS NULL " + myRoleMode + " tunnel id " + tunnelId + "/"
						+ sessionId + " for message id " + value.getMessageUuid());
			}
		} else {
			if (networkReceiver.getOrCreateServerSocketChannel(sessionId) != null) {
				networkReceiver.getOrCreateServerSocketChannel(sessionId).writeAndFlush(
						Unpooled.wrappedBuffer((decompressedBytes == null) ? primitiveBytes : decompressedBytes));
				// .await(BeaconNetworkTunnel.SYNC_TIME_OUT);
				if (trace)
					logger.info("message on " + myRoleMode + " tunnel id " + tunnelId + "/" + sessionId + " sent\n"
							+ Hex.encodeHexString((decompressedBytes == null) ? primitiveBytes : decompressedBytes));
			} else {
				logger.warn("server handler for " + myRoleMode + " tunnel id " + tunnelId + "/" + "/" + sessionId
						+ " is null");
				throw new ExceptionNetworkEvent("SERVER HANDLER IS NULL " + myRoleMode + " tunnel id " + tunnelId + "/"
						+ sessionId + "for message id " + value.getMessageUuid());
			}
		}
		ackMessage(sessionId, tunnelId, value.getMessageUuid());
		networkReceiver.incrementPacketReceived();
		cachedDataBase64ByMessageId.remove(value.getMessageUuid());
	}

	private void exceptionMessage(long serverSessionId, long targertId, long messageUuid, Exception clientEx) {
		active = false;
		try {
			final TunnelMessage tunnelMessage = TunnelMessage.newBuilder()
					.setMessageStatus(MessageStatus.exceptionCaught).setAgentSource(beaconNetworkTunnel.getMe())
					.setTargeId(targertId).setSessionId(serverSessionId)
					.setMessageType(
							myRoleMode.equals(NetworkMode.CLIENT) ? MessageType.FROM_CLIENT : MessageType.FROM_SERVER)
					.setPayload(EdgeLogger.stackTraceToString(clientEx)).build();
			BeaconNetworkReceiver.waitBeacon(beaconNetworkTunnel);
			beaconNetworkTunnel.getToBeaconServer().onNext(tunnelMessage);
		} catch (final Exception a) {
			logger.warn("in message exception " + a.getMessage());
		}
	}

	private void ackMessage(long serverSessionId, long targertId, long messageUuid) {
		try {
			final TunnelMessage tunnelMessage = TunnelMessage.newBuilder()
					.setMessageStatus(MessageStatus.beaconMessageAck).setAgentSource(beaconNetworkTunnel.getMe())
					.setTargeId(targertId).setSessionId(serverSessionId).setPayload(String.valueOf(messageUuid))
					.setMessageType(
							myRoleMode.equals(NetworkMode.CLIENT) ? MessageType.FROM_CLIENT : MessageType.FROM_SERVER)
					.build();
			BeaconNetworkReceiver.waitBeacon(beaconNetworkTunnel);
			beaconNetworkTunnel.getToBeaconServer().onNext(tunnelMessage);
		} catch (final Exception a) {
			logger.warn("in message ack " + a.getMessage());
		}
	}

	private void elaborateStatusMessage(TunnelMessage value, final long sessionId) {// throws InterruptedException {
		final String messageUuid = value.getPayload();
		switch (value.getMessageStatus()) {
		case UNRECOGNIZED:
			logger.warn(
					"STATUS: UNRECOGNIZED ( " + myRoleMode + " tunnel id " + tunnelId + "/" + "/" + sessionId + " )");
			break;
		case beaconChannelRequest:
			if (trace)
				logger.info("REQUEST: beaconChannelRequest ( " + myRoleMode + " tunnel id " + tunnelId + "/" + "/"
						+ sessionId + " )");
			break;
		case beaconMessageAck:
			if (messageUuid != null) {
				networkReceiver.confirmPacketReceived(Long.valueOf(messageUuid));
			} else {
				logger.warn("ack message with null payload!");
			}
			break;
		case channelActive:
			if (trace)
				logger.info("STATUS: channelActive ( " + myRoleMode + " tunnel id " + tunnelId + "/" + "/" + sessionId
						+ " )");
			break;
		case channelInactive:
			if (trace)
				logger.info("STATUS: channelInactive ( " + myRoleMode + " tunnel id " + tunnelId + "/" + "/" + sessionId
						+ " )");
			if (myRoleMode.equals(NetworkMode.CLIENT)) {
				if (networkReceiver.getOrCreateClientHandler(sessionId) != null
						&& networkReceiver.getOrCreateClientHandler(sessionId).channel() != null
						&& networkReceiver.getOrCreateClientHandler(sessionId).channel().isActive()) {
					networkReceiver.getOrCreateClientHandler(sessionId).channel().writeAndFlush(Unpooled.EMPTY_BUFFER)
							.addListener(ChannelFutureListener.CLOSE);
					networkReceiver.deleteClientHandler(sessionId);
				} else {
					if (networkReceiver.getOrCreateServerSocketChannel(sessionId) != null
							&& networkReceiver.getOrCreateServerSocketChannel(sessionId).isActive()) {
						networkReceiver.getOrCreateServerSocketChannel(sessionId).writeAndFlush(Unpooled.EMPTY_BUFFER)
								.addListener(ChannelFutureListener.CLOSE);
					}
				}
			}
			break;
		case channelReadComplete:
			if (trace)
				logger.info("STATUS: channelReadComplete ( " + myRoleMode + " tunnel id " + tunnelId + "/" + "/"
						+ sessionId + " )");
			break;
		case channelRegistered:
			if (trace)
				logger.info("STATUS: channelRegistered ( " + myRoleMode + " tunnel id " + tunnelId + "/" + "/"
						+ sessionId + " )");
			break;
		case channelTransmission:
			break;
		case channelTransmissionCompressed:
			break;
		case channelUnregistered:
			if (trace)
				logger.info("STATUS: channelUnregistered ( " + myRoleMode + " tunnel id " + tunnelId + "/" + "/"
						+ sessionId + " )");
			break;
		case channelWritabilityChanged:
			if (trace)
				logger.info("STATUS: channelWritabilityChanged ( " + myRoleMode + " tunnel id " + tunnelId + "/" + "/"
						+ sessionId + " )");
			break;
		case exceptionCaught:
			logger.warn("STATUS: exceptionCaught " + messageUuid + " ( " + myRoleMode + " tunnel id " + tunnelId + "/"
					+ "/" + sessionId + " )");
			if (messageUuid != null) {
				networkReceiver.exceptionPacketReceived(Long.valueOf(messageUuid));
			} else {
				logger.warn("exception message with null payload!");
			}
			break;
		case userEventTriggered:
			if (trace)
				logger.info("STATUS: userEventTriggered " + messageUuid + " ( " + myRoleMode + " tunnel id " + tunnelId
						+ "/" + "/" + sessionId + " )");
			break;
		default:
			break;
		}
	}

	private void appendBytesToCache(long sessionId, TunnelMessage tunnel, String data) {
		if (cachedDataBase64ByMessageId.containsKey(tunnel.getMessageUuid())
				&& cachedDataBase64ByMessageId.get(tunnel.getMessageUuid()) != null) {
			cachedDataBase64ByMessageId.get(tunnel.getMessageUuid()).addData(tunnel.getChunk(), data);
		} else {
			cachedDataBase64ByMessageId.put(tunnel.getMessageUuid(),
					new CachedChunk(tunnel.getChunk(), tunnel.getTotalChunks(), data));
		}
	}

	@Override
	public void onError(Throwable t) {
		// trace = true;
		active = false;
		if (trace) {
			logger.info("onError in BeaconEndpointObserver for message error " + t.getMessage());
		}
		logger.warn("error for tunnel id " + String.valueOf(tunnelId) + " " + t.getMessage());
		beaconNetworkTunnel.beaconObserverFault(tunnelId);
	}

	@Override
	public void onCompleted() {
		active = false;
		if (trace) {
			logger.info("onCompleted in BeaconEndpointObserver");
		}
		if (trace)
			logger.info("complete for tunnel id " + String.valueOf(tunnelId));
	}

	private static byte[] decompress(byte[] compressedBytes, int size) throws IOException {
		return IOUtils.toByteArray(new DeflateCompressorInputStream(ByteSource.wrap(compressedBytes).openStream()));
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("BeaconEndpointObserver [tunnelId=").append(tunnelId).append(", ");
		if (myRoleMode != null)
			builder.append("myRoleMode=").append(myRoleMode).append(", ");
		builder.append("active=").append(active).append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		return uniqueId;
	}

	private class CachedChunk {

		private final String[] datas;

		public CachedChunk(int chunk, int totalChunks, String data) {
			this.datas = new String[totalChunks];
			addData(chunk, data);
		}

		public boolean isComplete() {
			for (final String data : datas) {
				if (data == null) {
					return false;
				}
			}
			return true;
		}

		public String getCompleteData() {

			final StringBuilder sb = new StringBuilder();
			for (final String data : datas) {
				sb.append(data);
			}
			return sb.toString();
		}

		public void addData(int chunk, String data) {
			datas[chunk - 1] = data;

		}

	}

}
