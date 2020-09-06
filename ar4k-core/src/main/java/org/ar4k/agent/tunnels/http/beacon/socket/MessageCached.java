package org.ar4k.agent.tunnels.http.beacon.socket;

import static org.ar4k.agent.tunnels.http.beacon.socket.BeaconNetworkTunnel.TRACE_LOG_IN_INFO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.channels.ClosedChannelException;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.io.IOUtils;
import org.ar4k.agent.exception.ExceptionNetworkEvent;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.tunnels.http.grpc.beacon.MessageStatus;
import org.ar4k.agent.tunnels.http.grpc.beacon.MessageType;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage.Builder;

import com.google.common.io.ByteSource;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

final class MessageCached implements Serializable {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(MessageCached.class.toString());

	enum MessageCachedType {
		TO_BEACON, TO_NETWORK
	}

	private static final long serialVersionUID = 3207807089900926005L;
	private final long timeRequest = new Date().getTime();
	private final long messageId;
	private final transient BeaconNetworkReceiver networkReceiver;
	private final transient ChannelHandlerContext channelHandlerContext;
	private final NetworkMode myRoleMode;
	private final long serialId;
	private final transient ExecutorService executor;
	private final byte[] inputbytes;
	private final transient BeaconNetworkTunnel tunnel;
	private final int chunkLimit;
	private final MessageCachedType messageCachedType;
	private final String outputData;
	private final int originalSize;
	private final MessageStatus messageStatus;
	private int retry = 0;
	private long lastRetry = 0;
	private AtomicLong completed = new AtomicLong(0);

	MessageCached(final MessageCachedType messageCachedType, final BeaconNetworkReceiver networkReceiver,
			final BeaconNetworkTunnel tunnel, final ChannelHandlerContext ctx, final NetworkMode myRoleMode,
			final long serialId, final long messageId, final byte[] bytes, final String outputData, int chunkLimit,
			int originalSize, MessageStatus messageStatus) {
		this.messageCachedType = messageCachedType;
		this.messageId = messageId;
		this.networkReceiver = networkReceiver;
		this.tunnel = tunnel;
		this.channelHandlerContext = ctx;
		this.myRoleMode = myRoleMode;
		this.serialId = serialId;
		this.executor = networkReceiver.getExecutor();
		this.inputbytes = bytes;
		this.chunkLimit = chunkLimit;
		this.outputData = outputData;
		this.originalSize = originalSize;
		this.messageStatus = messageStatus;
	}

	long getTimeRequest() {
		return timeRequest;
	}

	long getCompleted() {
		return completed.get();
	}

	boolean isCompleted() {
		return getCompleted() != 0;

	}

	long getMessageId() {
		return messageId;
	}

	long getSessionID() {
		return serialId;
	}

	void end() {
		completed.set(new Date().getTime());
	}

	synchronized boolean softChek() {
		final long time = new Date().getTime();
		if (lastRetry == 0 || (lastRetry + BeaconNetworkTunnel.DELAY_SOFT_CHECK < time)) {
			lastRetry = time;
			if (TRACE_LOG_IN_INFO)
				logger.info("soft check called for {}", this);
			isCompleteOrTryToSend();
			return true;
		} else {
			return false;
		}
	}

	private boolean isCompleteOrTryToSend() {
		if (isValidToRetry()) {
			if (!isCompleted()) {
				switch (messageCachedType) {
				case TO_BEACON:
					runActionSendToBeacon();
					break;
				case TO_NETWORK:
					if (networkReceiver.isNextMessageToNetwork(serialId, tunnel.getTunnelId(), messageId,
							messageStatus)) {
						runActionSendToNetwork(3);
					}
					break;
				default:
					break;
				}
			} else {
				networkReceiver.sendAckOrControlMessage(serialId, tunnel.getTunnelId(), messageId, true);
			}
		}
		return isCompleted();
	}

	private void runActionSendToBeacon() {
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					sendPacketToBeacon();
				} catch (final Exception a) {
					logger.logException(a);
				}
			}
		});
	}

	private void runActionSendToNetwork(final int retryTimes) {
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					deliveryMessageToNetwork();
				} catch (final ClosedChannelException c) {
					retryAction();
					if (myRoleMode.equals(NetworkMode.CLIENT)) {
						networkReceiver.deleteClientHandler(getSessionID());
					} else {
						networkReceiver.deleteServerSocketChannel(getSessionID());
					}
					logger.logException("IN ACTION SEND TO NETWORK FOUND NETWORK CLOSED", c);
				} catch (final Exception e) {
					logger.logException("IN ACTION SEND TO NETWORK", e);
					networkReceiver.sendExceptionMessage(serialId, tunnel.getTunnelId(), messageId, e);
				}

			}

			private void retryAction() {
				if (retryTimes > 0) {
					runActionSendToNetwork(retryTimes - 1);
				}

			}
		});
	}

	private final void sendPacketToBeacon() {
		try {
			byte[] compressedByteData = null;
			if (inputbytes.length > chunkLimit) {
				compressedByteData = compressData(inputbytes);
			}
			final String base64Data = Base64.getEncoder()
					.encodeToString((compressedByteData == null) ? inputbytes : compressedByteData);
			if (!base64Data.isEmpty()) {
				final int chunkSize = (base64Data.length() / chunkLimit)
						+ ((base64Data.length() == chunkLimit) ? 0 : 1);
				retry++;
				sendAllChunkToBeaconServer(compressedByteData, base64Data, chunkSize);
				if (TRACE_LOG_IN_INFO)
					logger.info("** messageId " + messageId + " sent to Beacon server "
							+ channelHandlerContext.channel().remoteAddress() + "\n" + tunnel.getTunnelId() + "/"
							+ myRoleMode + " size " + base64Data.length() + " chunks: " + chunkSize + " data:\n"
							+ base64Data + "\n");
				networkReceiver.getBeaconNetworkTunnel().incrementPacketSend();
			} else {
				logger.warn("error sending empty message " + myRoleMode + " to Beacon server");
			}
		} catch (final Exception f) {
			logger.warn("EXCEPTION SENDING TO BEACON SERVER AS " + myRoleMode + " " + EdgeLogger.stackTraceToString(f));
			networkReceiver.getBeaconNetworkTunnel().incrementPacketError();
			throw new ExceptionNetworkEvent(f);
		}
	}

	private void sendAllChunkToBeaconServer(final byte[] compressedByteData, final String base64Data,
			final int chunkSize) {
		int chunk = 1;
		while (chunk <= chunkSize) {
			final int from = chunkLimit * (chunk - 1);
			final int offset = (chunk < chunkSize) ? chunkLimit : (base64Data.length() - from);
			if (TRACE_LOG_IN_INFO)
				logger.info("elaborate chunk " + chunk + "/" + chunkSize + " from:" + from + " offset:" + offset);
			final String payload = base64Data.substring(from, from + offset);
			final Builder tunnelBuilder = TunnelMessage.newBuilder().setPayload(payload)
					.setMessageHashCode(payload.hashCode()).setAgentSource(tunnel.getMe())
					.setTunnelId(tunnel.getTunnelId()).setSessionId(serialId)
					.setMessageType((myRoleMode.equals(NetworkMode.SERVER)) ? MessageType.FROM_SERVER
							: MessageType.FROM_CLIENT);
			if (compressedByteData == null) {
				tunnelBuilder.setMessageStatus(MessageStatus.channelTransmission);
			} else {
				tunnelBuilder.setMessageStatus(MessageStatus.channelTransmissionCompressed)
						.setOriginalSize(inputbytes.length);
			}

			final TunnelMessage tunnelMessage = tunnelBuilder.setClassUuid(tunnel.getUniqueClassId()).setChunk(chunk)
					.setTotalChunks(chunkSize).setMessageId(messageId).build();
			tunnel.sendMessageToBeaconTunnel(tunnelMessage);
			chunk++;
		}
	}

	private final void deliveryMessageToNetwork()
			throws IOException, InterruptedException, ExecutionException, TimeoutException {
		if (TRACE_LOG_IN_INFO) {
			logger.info("delivery message...");
		}
		final byte[] primitiveBytes = Base64.getDecoder().decode(outputData);
		byte[] decompressedBytes = null;
		if (messageStatus.equals(MessageStatus.channelTransmissionCompressed)) {
			decompressedBytes = decompress(primitiveBytes);
		}
		retry++;
		if (myRoleMode.equals(NetworkMode.CLIENT)) {
			if (networkReceiver.getOrCreateClientHandler(serialId) != null) {
				sendToNetworkClient(primitiveBytes, decompressedBytes);
				if (TRACE_LOG_IN_INFO) {
					logger.info("sent to network channel "
							+ networkReceiver.getOrCreateClientHandler(serialId).isSuccess());
					logger.info("message as " + myRoleMode + " tunnel id " + tunnel.getTunnelId() + "/" + serialId
							+ " sent (Hex bytes)\n"
							+ Hex.encodeHexString((decompressedBytes == null) ? primitiveBytes : decompressedBytes));
				}
			} else {
				logger.warn("client handler for " + myRoleMode + " tunnel id " + tunnel.getTunnelId() + "/" + "/"
						+ serialId + " is null during write");
				throw new ExceptionNetworkEvent("CLIENT HANDLER IS NULL " + myRoleMode + " tunnel id "
						+ tunnel.getTunnelId() + " serialId " + serialId + " for message id " + messageId);
			}
		} else {
			if (networkReceiver.getOrCreateServerSocketChannel(serialId) != null) {
				sendToNetworkServer(primitiveBytes, decompressedBytes);
				if (TRACE_LOG_IN_INFO)
					logger.info("message as " + myRoleMode + " tunnel id " + tunnel.getTunnelId() + " serialId "
							+ serialId + " sent to network (Hex bytes)\n"
							+ Hex.encodeHexString((decompressedBytes == null) ? primitiveBytes : decompressedBytes));
			} else {
				logger.warn("server handler for " + myRoleMode + " tunnel id " + tunnel.getTunnelId() + "/" + "/"
						+ serialId + " is null");
				throw new ExceptionNetworkEvent("SERVER HANDLER IS NULL " + myRoleMode + " tunnel id "
						+ tunnel.getTunnelId() + " serialId " + serialId + "for message id " + messageId);
			}
		}
		networkReceiver.getBeaconNetworkTunnel().incrementPacketReceived();
		networkReceiver.sendAckOrControlMessage(serialId, tunnel.getTunnelId(), messageId, false);
	}

	private synchronized void sendToNetworkServer(final byte[] primitiveBytes, byte[] decompressedBytes)
			throws InterruptedException, ExecutionException, TimeoutException {
		if (!isCompleted()) {
			networkReceiver.getOrCreateServerSocketChannel(serialId)
					.writeAndFlush(
							Unpooled.wrappedBuffer((decompressedBytes == null) ? primitiveBytes : decompressedBytes))
					.get(BeaconNetworkTunnel.LAST_MESSAGE_FROM_BEACON_SERVER_TIMEOUT, TimeUnit.MILLISECONDS);
			end();
		}
	}

	private synchronized void sendToNetworkClient(final byte[] primitiveBytes, byte[] decompressedBytes)
			throws InterruptedException, ExecutionException, TimeoutException {
		if (!isCompleted()) {
			networkReceiver.getOrCreateClientHandler(serialId).channel()
					.writeAndFlush(
							Unpooled.wrappedBuffer((decompressedBytes == null) ? primitiveBytes : decompressedBytes))
					.get(BeaconNetworkTunnel.LAST_MESSAGE_FROM_BEACON_SERVER_TIMEOUT, TimeUnit.MILLISECONDS);
			end();
		}
	}

	void ackReceived() {
		end();
		if (TRACE_LOG_IN_INFO)
			logger.info("ack register for {}", this);
	}

	void resetReceived() {
		if (!isCompleted()) {
			if (isValidToRetry()) {
				runActionSendToBeacon();
			} else {
				logger.warn("for message " + messageId + " received " + retry + " reset but the limit is "
						+ BeaconNetworkTunnel.RETRY_LIMIT);
			}
		} else {
			logger.info("for message " + messageId + " received the retry but it is complete at "
					+ new Date(getCompleted()).toString());
		}
	}

	boolean isValidToRetry() {
		return retry < BeaconNetworkTunnel.RETRY_LIMIT;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("MessageCached [timeRequest=").append(timeRequest).append(", messageId=").append(messageId)
				.append(", ");
		if (myRoleMode != null)
			builder.append("myRoleMode=").append(myRoleMode).append(", ");
		builder.append("serialId=").append(serialId).append(", chunkLimit=").append(chunkLimit).append(", ");
		if (messageCachedType != null)
			builder.append("messageCachedType=").append(messageCachedType).append(", ");
		builder.append("originalSize=").append(originalSize).append(", ");
		if (messageStatus != null)
			builder.append("messageStatus=").append(messageStatus).append(", ");
		builder.append("retry=").append(retry).append(", lastRetry=").append(lastRetry).append(", completed=")
				.append(completed).append("]");
		return builder.toString();
	}

	private static final byte[] compressData(byte[] bytesData) throws IOException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		try (OutputStream dos = new DeflateCompressorOutputStream(os)) {
			dos.write(bytesData);
		}
		if (TRACE_LOG_IN_INFO)
			logger.info("compress report: original size: {} compressed size: {}", bytesData.length, os.size());
		return os.toByteArray();
	}

	private static byte[] decompress(byte[] compressedBytes) throws IOException {
		return IOUtils.toByteArray(new DeflateCompressorInputStream(ByteSource.wrap(compressedBytes).openStream()));
	}

}
