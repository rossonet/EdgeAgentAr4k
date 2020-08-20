package org.ar4k.agent.tunnels.http.beacon.socket;

import static org.ar4k.agent.tunnels.http.beacon.socket.BeaconNetworkTunnel.trace;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.ar4k.agent.exception.ExceptionNetworkEvent;
import org.ar4k.agent.exception.TimeoutNetworkEvent;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.network.NetworkReceiver;
import org.ar4k.agent.tunnels.http.grpc.beacon.MessageStatus;
import org.ar4k.agent.tunnels.http.grpc.beacon.MessageType;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage.Builder;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class BeaconNetworkReceiver implements NetworkReceiver {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconNetworkReceiver.class.toString());

	private final NetworkMode myRoleMode;
	private final long uniqueClassId;
	private final int chunkLimit;
	private final BeaconNetworkTunnel tunnel;
	private final AtomicLong packetSend = new AtomicLong(0);
	private final AtomicLong packetReceived = new AtomicLong(0);
	private final AtomicLong packetError = new AtomicLong(0);
	private EventLoopGroup bossGroup = null;
	private EventLoopGroup workerGroup = null;
	private final Map<Long, SocketChannel> serverChannelHandler = new ConcurrentHashMap<>();
	private final Map<Long, ChannelFuture> clientChannelHandler = new ConcurrentHashMap<>();
	private ChannelFuture mainServerHandler = null;
	private final Map<Long, LockUntilAck> packetSemaphores = new ConcurrentHashMap<>();

	public BeaconNetworkReceiver(BeaconNetworkTunnel beaconNetworkTunnel, long uniqueClassId, int chunkLimit) {
		this.chunkLimit = chunkLimit;
		this.uniqueClassId = uniqueClassId;
		this.tunnel = beaconNetworkTunnel;
		if ((tunnel.getConfig().getNetworkModeRequested().equals(NetworkMode.CLIENT) && tunnel.getOwner())
				|| tunnel.getConfig().getNetworkModeRequested().equals(NetworkMode.SERVER) && !tunnel.getOwner()) {
			myRoleMode = NetworkMode.CLIENT;
		} else {
			myRoleMode = NetworkMode.SERVER;
		}
		if (trace)
			logger.info("Network receiver for uuid:" + uniqueClassId + "/" + myRoleMode + " created");
	}

	private void reportDetails(long idMessage) {
		if (trace) {
			logger.info("receive message " + idMessage);
			final StringBuilder sb = new StringBuilder();
			sb.append("\n--------------------\nDETAILS BeaconNetworkReceiver\n");
			sb.append("ROLE: " + myRoleMode + " -> " + "\n");
			if (myRoleMode.equals(NetworkMode.CLIENT)) {
				sb.append("- clientChannelHandler -> " + clientChannelHandler.entrySet().stream()
						.map(n -> (n.getKey() + "["
								+ ((n.getValue().channel() != null) ? n.getValue().channel().isActive() : "disabled")
								+ "]"))
						.collect(Collectors.joining(", ")) + "\n");
			}
			if (myRoleMode.equals(NetworkMode.SERVER)) {
				sb.append("- serverChannelHandler -> " + serverChannelHandler.entrySet().stream()
						.map(n -> (n.getKey() + "[" + (n.getValue().isActive()) + "]"))
						.collect(Collectors.joining(", ")) + "\n");
				sb.append(
						"- mainServerHandler -> " + ((mainServerHandler != null && mainServerHandler.channel() != null)
								? mainServerHandler.channel().isActive()
								: "disabled") + "\n");
			}
			sb.append("- packetSemaphores ["
					+ packetSemaphores.size() + "] -> " + packetSemaphores.entrySet().stream()
							.map(n -> (n.getKey() + "[" + n.getValue() + "]")).collect(Collectors.joining(", "))
					+ "\n");
			sb.append("\n--------------------\n");
			logger.info(sb.toString());
		}
	}

	@Override
	public long getTunnelId() {
		return tunnel.getTunnelId();
	}

	@Override
	public NetworkStatus getStatus() {
		if ((myRoleMode.equals(NetworkMode.SERVER) && mainServerHandler != null && mainServerHandler.channel() != null
				&& mainServerHandler.channel().isActive()) || myRoleMode.equals(NetworkMode.CLIENT)) {
			return NetworkStatus.ACTIVE;
		} else {
			if (trace)
				logger.info("Network receiver for uniqueClassId:" + uniqueClassId + "/" + myRoleMode + " is inactive");
			return NetworkStatus.INACTIVE;
		}
	}

	@Override
	public ChannelFuture getOrCreateClientHandler(long sessionId) {
		if (trace)
			logger.info("opening clients channel for session " + sessionId + ". Actual channels: "
					+ clientChannelHandler.size());
		if (!clientChannelHandler.containsKey(sessionId)) {
			createSocketSessionClient(sessionId);
		}
		final ChannelFuture channelFuture = clientChannelHandler.get(sessionId);
		if (channelFuture != null && !channelFuture.isDone()) {
			try {
				channelFuture.await(BeaconNetworkTunnel.SYNC_TIME_OUT);
			} catch (final InterruptedException e) {
				logger.logException(e);
			}
		}
		if (trace) {
			if (channelFuture != null) {
				logger.info("GET CLIENT CHANNEL ::: " + channelFuture.isDone() + " ::: " + channelFuture.isSuccess()
						+ " ### " + channelFuture.channel().isActive() + " @@@ " + channelFuture.channel().isOpen()
						+ " --- " + channelFuture.channel().isWritable());
			} else {
				logger.info("GET CLIENT CHANNEL ::: NULL");
				logger.info("Values in clientChannelHandler ->\n" + clientChannelHandler.keySet().stream()
						.map(n -> n.toString()).collect(Collectors.joining(",")));
			}
		}
		return channelFuture;
	}

	@Override
	public SocketChannel getOrCreateServerSocketChannel(long sessionId) {
		if (trace)
			logger.info("opening server channel for session " + sessionId + ". Actual server channels "
					+ serverChannelHandler.size());
		if (mainServerHandler == null || mainServerHandler.isCancelled()) {
			try {
				createServerSocket();
			} catch (final InterruptedException e) {
				logger.logException(e);
			}
		}
		final SocketChannel channelFuture = serverChannelHandler.get(sessionId);
		if (trace) {
			if (serverChannelHandler != null) {
				logger.info("Values in serverChannelHandler ->\n" + serverChannelHandler.keySet().stream()
						.map(n -> n.toString()).collect(Collectors.joining(",")));
			}
			if (channelFuture != null) {
				logger.info("GET SERVER CHANNEL ::: " + channelFuture.isRegistered() + " ::: "
						+ !channelFuture.isShutdown() + " ### " + channelFuture.isActive() + " @@@ "
						+ channelFuture.isOpen() + " --- " + channelFuture.isWritable());
			} else {
				logger.info("GET SERVER CHANNEL ::: NULL");
				logger.info("Values in serverChannelHandler ->\n" + serverChannelHandler.keySet().stream()
						.map(n -> n.toString()).collect(Collectors.joining(",")));
			}
		}
		return channelFuture;
	}

	private synchronized void createSocketSessionClient(long sessionId) {
		if (workerGroup == null) {
			workerGroup = new NioEventLoopGroup();
		}
		final Bootstrap clientBootstrap = new Bootstrap();
		clientBootstrap.group(workerGroup);
		clientBootstrap.channel(NioSocketChannel.class);
		clientBootstrap.handler(createClientInitializerHandlerForSession(sessionId));
		clientBootstrap.option(ChannelOption.AUTO_READ, false);
		try {
			clientChannelHandler.put(sessionId,
					clientBootstrap.connect(tunnel.getConfig().getClientIp(), tunnel.getConfig().getClientPort()));
			if (trace)
				logger.info("created socket client for session " + sessionId + ". It points to "
						+ tunnel.getConfig().getClientIp() + ":" + tunnel.getConfig().getClientPort());
		} catch (final Exception e) {
			logger.logException("during connection ", e);
		}
	}

	private ChannelInitializer<SocketChannel> createClientInitializerHandlerForSession(long serialId) {
		return new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				final ClientTCPChannelHandler clientTCPChannelHandler = new ClientTCPChannelHandler(serialId);
				socketChannel.pipeline().addLast(clientTCPChannelHandler);
				if (trace)
					logger.info("ChannelInitializer for socket channel " + myRoleMode + "/" + serialId + " called");
			}
		};
	}

	private final class ClientTCPChannelHandler extends SimpleChannelInboundHandler<ByteBuf> implements AutoCloseable {
		private final long clientSerialId;

		public ClientTCPChannelHandler(long clientSerialId) {
			this.clientSerialId = clientSerialId;
			if (trace)
				logger.info("new ClientTCPChannelHandler created, serial session id:" + clientSerialId);
		}

		@Override
		public void channelRead0(ChannelHandlerContext ctx, ByteBuf s)
				throws TimeoutNetworkEvent, ExceptionNetworkEvent {
			if (trace) {
				logger.info("channel socket with session " + clientSerialId + " for tunnel " + tunnel.getTunnelId()
						+ "/" + myRoleMode + " received message");
			}
			clientReadWork(ctx, s, BeaconNetworkTunnel.RETRY_LIMIT, 0);
			ctx.channel().read();
		}

		private void clientReadWork(ChannelHandlerContext ctx, ByteBuf s, int retry, long messageId)
				throws TimeoutNetworkEvent, ExceptionNetworkEvent {
			if (messageId == 0)
				messageId = UUID.randomUUID().getMostSignificantBits();
			reportDetails(messageId);
			try {
				final byte[] bytesData = ByteBufUtil.getBytes(s);
				byte[] compressedByteData = null;
				if (bytesData.length > chunkLimit) {
					compressedByteData = compressData(bytesData);
				}
				final String base64Data = Base64.getEncoder()
						.encodeToString((compressedByteData == null) ? bytesData : compressedByteData);
				if (!base64Data.isEmpty()) {
					final int chunkSize = (base64Data.length() / chunkLimit) + 1;
					int chunk = 1;
					while (chunk <= chunkSize) {
						final int from = chunkLimit * (chunk - 1);
						final int offset = (chunk < chunkSize) ? chunkLimit : (base64Data.length() - (from));
						if (trace)
							logger.info("elaborate chunk " + chunk + "/" + chunkSize + " from:" + from + " offset:"
									+ offset);
						final Builder tunnelBuilder = TunnelMessage.newBuilder()
								.setPayload(base64Data.substring(from, from + offset)).setAgentSource(tunnel.getMe())
								.setTargeId(tunnel.getTunnelId()).setMessageType(MessageType.FROM_CLIENT)
								.setSessionId(clientSerialId);
						if (compressedByteData == null) {
							tunnelBuilder.setMessageStatus(MessageStatus.channelTransmission);
						} else {
							tunnelBuilder.setMessageStatus(MessageStatus.channelTransmissionCompressed)
									.setOriginalSize(bytesData.length);
						}
						final TunnelMessage tunnelMessage = tunnelBuilder.setUuid(uniqueClassId).setChunk(chunk)
								.setTotalChunks(chunkSize).setMessageUuid(messageId).build();
						waitBeacon(tunnel);
						tunnel.getToBeaconServer().onNext(tunnelMessage);
						chunk++;
					}
					final LockUntilAck timedSemaphore = new LockUntilAck();
					packetSemaphores.put(messageId, timedSemaphore);
					timedSemaphore.waitAck(messageId);
					if (trace)
						logger.info("** messageId " + messageId + " on client " + tunnel.getTunnelId() + "/"
								+ clientSerialId + " size " + base64Data.length() + " chunks: " + chunkSize + " data:\n"
								+ base64Data + "\n");
					incrementPacketSend();
				} else {
					if (trace)
						logger.info("NetworkHub " + myRoleMode + " empty message");
				}
			} catch (final Exception f) {
				logger.warn("EXCEPTION READING IN BEACON CLIENT AND WRITING TO SOCK -CLIENT MODE-",
						EdgeLogger.stackTraceToString(f));
				packetSemaphores.remove(messageId);
				incrementPacketError();
				if (f instanceof TimeoutNetworkEvent) {
					if (retry > 0) {
						logger.info("TimeoutNetworkEvent - RETRY MESSAGE. " + retry + " FAULTS REMAIN");
						clientReadWork(ctx, s, --retry, messageId);
					} else {
						throw new TimeoutNetworkEvent("timeout in packet " + messageId);
					}
				}
				if (f instanceof ExceptionNetworkEvent) {
					if (retry > 0) {
						logger.info("ExceptionNetworkEvent - RETRY MESSAGE. " + retry + " FAULTS REMAIN");
						clientReadWork(ctx, s, --retry, messageId);
					} else {
						throw new ExceptionNetworkEvent("exception in packet " + messageId);
					}
				}
			}
		}

		@Override
		public void close() throws Exception {
			if (trace)
				logger.info("ClientTCPChannelHandler for " + clientSerialId + "/" + myRoleMode + " closed");
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel client " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ tunnel.getTunnelId() + "/" + myRoleMode + " channelActive");
			try {
				final TunnelMessage tunnelMessage = TunnelMessage.newBuilder()
						.setMessageStatus(MessageStatus.channelActive).setAgentSource(tunnel.getMe())
						.setTargeId(tunnel.getTunnelId()).setMessageType(MessageType.FROM_CLIENT)
						.setSessionId(clientSerialId).setUuid(uniqueClassId).build();
				waitBeacon(tunnel);
				tunnel.getToBeaconServer().onNext(tunnelMessage);
			} catch (final Exception a) {
				logger.warn("in message channelActive " + a.getMessage());
			}
			super.channelActive(ctx);
			ctx.read();
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel client " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ tunnel.getTunnelId() + "/" + myRoleMode + " channelInactive");
			try {
				final TunnelMessage tunnelMessage = TunnelMessage.newBuilder()
						.setMessageStatus(MessageStatus.channelInactive).setAgentSource(tunnel.getMe())
						.setTargeId(tunnel.getTunnelId()).setMessageType(MessageType.FROM_CLIENT)
						.setSessionId(clientSerialId).setUuid(uniqueClassId).build();
				waitBeacon(tunnel);
				tunnel.getToBeaconServer().onNext(tunnelMessage);
			} catch (final Exception a) {
				logger.warn("in message channelInactive " + a.getMessage());
			}
			tunnel.callChannelClientComplete(clientSerialId);
			super.channelInactive(ctx);
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel client " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ tunnel.getTunnelId() + "/" + myRoleMode + " channelReadComplete");
			super.channelReadComplete(ctx);
		}

		@Override
		public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel client " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ tunnel.getTunnelId() + "/" + myRoleMode + " channelRegistered");
			super.channelRegistered(ctx);
		}

		@Override
		public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel client " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ tunnel.getTunnelId() + "/" + myRoleMode + " channelUnregistered");
			super.channelUnregistered(ctx);
		}

		@Override
		public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel client " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ tunnel.getTunnelId() + "/" + myRoleMode + " channelWritabilityChanged");
			super.channelWritabilityChanged(ctx);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			if (trace)
				logger.info("channel client " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ tunnel.getTunnelId() + "/" + myRoleMode + " exceptionCaught");
			tunnel.callChannelClientException(clientSerialId);
			super.exceptionCaught(ctx, cause);
		}

		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			if (trace)
				logger.info("channel client " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ tunnel.getTunnelId() + "/" + myRoleMode + " userEventTriggered");
			super.userEventTriggered(ctx, evt);
		}
	}

	private synchronized void createServerSocket() throws InterruptedException {
		if (bossGroup == null) {
			bossGroup = new NioEventLoopGroup();
		}
		if (workerGroup == null) {
			workerGroup = new NioEventLoopGroup();
		}
		try {
			final ServerTcpInitHandler serverInitHandler = new ServerTcpInitHandler();
			final ServerBootstrap b = new ServerBootstrap().group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class).childHandler(serverInitHandler)
					.childOption(ChannelOption.SO_KEEPALIVE, true).childOption(ChannelOption.SO_REUSEADDR, true)
					.childOption(ChannelOption.AUTO_READ, false);
			mainServerHandler = b.bind(tunnel.getConfig().getServerPort()).sync();
			if (mainServerHandler.isSuccess()) {
				if (trace)
					logger.info("server Netty on port " + tunnel.getConfig().getServerPort() + " with id_target "
							+ tunnel.getTunnelId() + "/" + myRoleMode + " started successfully");
			} else {
				closeServerMainSocket();
				logger.warn("server Netty on port " + tunnel.getConfig().getServerPort() + " with id_target "
						+ tunnel.getTunnelId() + "/" + myRoleMode + " error -> " + mainServerHandler.isSuccess());
				if (trace) {
					logger.info("started server socket " + tunnel.getConfig().getServerPort());
				}
			}
		} catch (final Exception a) {
			logger.logException("fault creating server socket", a);
			try {
				closeServerMainSocket();
			} catch (final Exception e) {
				logger.logException(e);
			}
		}
	}

	private final class ServerTcpInitHandler extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel serverSocketChannel) throws Exception {
			final ServerTCPHandler serverTCPSocketChannelHandler = new ServerTCPHandler(serverSocketChannel);
			serverSocketChannel.pipeline().addLast(serverTCPSocketChannelHandler).addLast("logger",
					new LoggingHandler());
		}
	}

	private final class ServerTCPHandler extends SimpleChannelInboundHandler<ByteBuf> implements AutoCloseable {
		private final long serverSessionId = UUID.randomUUID().getMostSignificantBits(); // battezza la sessione

		public ServerTCPHandler(SocketChannel serverSocketChannel) {
			serverChannelHandler.put(serverSessionId, serverSocketChannel);
			if (trace)
				logger.info("server socket with session " + serverSessionId + " to " + tunnel.getTunnelId() + "/"
						+ myRoleMode + " created");
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, final ByteBuf s) throws TimeoutNetworkEvent {
			if (trace)
				logger.info("server socket with session " + serverSessionId + " to " + tunnel.getTunnelId() + "/"
						+ myRoleMode + " received message");
			serverReadWork(ctx, s, BeaconNetworkTunnel.RETRY_LIMIT, 0);
			ctx.channel().read();
		}

		private void serverReadWork(ChannelHandlerContext ctx, final ByteBuf s, int retry, long messageId)
				throws TimeoutNetworkEvent {
			if (messageId == 0)
				messageId = UUID.randomUUID().getMostSignificantBits();
			reportDetails(messageId);
			try {
				final byte[] bytesData = ByteBufUtil.getBytes(s);
				byte[] compressedByteData = null;
				if (bytesData.length > chunkLimit) {
					compressedByteData = compressData(bytesData);
				}
				final String base64Data = Base64.getEncoder()
						.encodeToString((compressedByteData == null) ? bytesData : compressedByteData);
				if (!base64Data.isEmpty()) {
					final int chunkSize = (base64Data.length() / chunkLimit) + 1;
					int chunk = 1;
					while (chunk <= chunkSize) {
						final int from = chunkLimit * (chunk - 1);
						final int offset = (chunk < chunkSize) ? chunkLimit : (base64Data.length() - (from));
						if (trace)
							logger.info("elaborate chunk " + chunk + "/" + chunkSize + " from:" + from + " offset:"
									+ offset);
						final Builder tunnelBuilder = TunnelMessage.newBuilder()
								.setPayload(base64Data.substring(from, from + offset)).setAgentSource(tunnel.getMe())
								.setTargeId(tunnel.getTunnelId()).setSessionId(serverSessionId)
								.setMessageType(MessageType.FROM_SERVER);
						if (compressedByteData == null) {
							tunnelBuilder.setMessageStatus(MessageStatus.channelTransmission);
						} else {
							tunnelBuilder.setMessageStatus(MessageStatus.channelTransmissionCompressed)
									.setOriginalSize(bytesData.length);
						}

						final TunnelMessage tunnelMessage = tunnelBuilder.setUuid(uniqueClassId).setChunk(chunk)
								.setTotalChunks(chunkSize).setMessageUuid(messageId).build();
						waitBeacon(tunnel);
						tunnel.getToBeaconServer().onNext(tunnelMessage);
						chunk++;
					}
					final LockUntilAck timedSemaphore = new LockUntilAck();
					packetSemaphores.put(messageId, timedSemaphore);
					timedSemaphore.waitAck(messageId);
					if (trace)
						logger.info("** messageId " + messageId + " on server " + ctx.channel().remoteAddress() + "\n"
								+ tunnel.getTunnelId() + "/" + myRoleMode + " size " + base64Data.length() + " chunks: "
								+ chunkSize + " data:\n" + base64Data + "\n");
					incrementPacketSend();
				} else {
					if (trace)
						logger.info("NetworkHub " + myRoleMode + " empty message");
				}
			} catch (final Exception f) {
				// trace = true;
				logger.warn("EXCEPTION READING IN BEACON CLIENT -SERVER MODE-", f.getMessage());
				packetSemaphores.remove(messageId);
				incrementPacketError();
				if (f instanceof TimeoutNetworkEvent) {
					if (retry > 0) {
						logger.info("RETRY MESSAGE. " + retry + " FAULTS REMAIN");
						serverReadWork(ctx, s, --retry, messageId);
					} else {
						throw new TimeoutNetworkEvent("timeout for packet " + messageId);
					}
				}
				if (f instanceof ExceptionNetworkEvent) {
					if (retry > 0) {
						logger.info("ExceptionNetworkEvent - RETRY MESSAGE. " + retry + " FAULTS REMAIN");
						serverReadWork(ctx, s, --retry, messageId);
					} else {
						throw new ExceptionNetworkEvent("exception for packet " + messageId);
					}
				}
			}
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel server with " + ctx.channel().remoteAddress() + "\n" + tunnel.getTunnelId() + "/"
						+ myRoleMode + " channelActive");
			try {
				final TunnelMessage tunnelMessage = TunnelMessage.newBuilder()
						.setMessageStatus(MessageStatus.channelActive).setAgentSource(tunnel.getMe())
						.setTargeId(tunnel.getTunnelId()).setSessionId(serverSessionId)
						.setMessageType(MessageType.FROM_SERVER).build();
				waitBeacon(tunnel);
				tunnel.getToBeaconServer().onNext(tunnelMessage);
			} catch (final Exception a) {
				logger.warn("in message channelActive " + a.getMessage());
			}
			super.channelActive(ctx);
			ctx.read();
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel server with " + ctx.channel().remoteAddress() + "\n" + tunnel.getTunnelId() + "/"
						+ myRoleMode + " channelInactive");
			try {
				if (tunnel.getToBeaconServer() != null) {
					final TunnelMessage tunnelMessage = TunnelMessage.newBuilder()
							.setMessageStatus(MessageStatus.channelInactive).setAgentSource(tunnel.getMe())
							.setTargeId(tunnel.getTunnelId()).setSessionId(serverSessionId)
							.setMessageType(MessageType.FROM_SERVER).build();
					waitBeacon(tunnel);
					tunnel.getToBeaconServer().onNext(tunnelMessage);
				}
			} catch (final Exception a) {
				logger.warn("in message channelInactive " + a.getMessage());
			}
			tunnel.callChannelServerComplete(serverSessionId);
			super.channelInactive(ctx);
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel server with " + ctx.channel().remoteAddress() + "\n" + tunnel.getTunnelId() + "/"
						+ myRoleMode + " channelReadComplete");
			super.channelReadComplete(ctx);
		}

		@Override
		public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel server with " + ctx.channel().remoteAddress() + "\n" + tunnel.getTunnelId() + "/"
						+ myRoleMode + " channelRegistered");
			super.channelRegistered(ctx);
		}

		@Override
		public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel server with " + ctx.channel().remoteAddress() + "\n" + tunnel.getTunnelId() + "/"
						+ myRoleMode + " channelUnregistered");
			super.channelUnregistered(ctx);
		}

		@Override
		public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel server with " + ctx.channel().remoteAddress() + "\n" + tunnel.getTunnelId() + "/"
						+ myRoleMode + " channelWritabilityChanged");
			super.channelWritabilityChanged(ctx);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			logger.info("channel server with " + ctx.channel().remoteAddress() + "\n" + tunnel.getTunnelId() + "/"
					+ myRoleMode + " exceptionCaught");
			tunnel.callChannelServerException(serverSessionId);
			super.exceptionCaught(ctx, cause);
		}

		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			if (trace)
				logger.info("channel server with " + ctx.channel().remoteAddress() + "\n" + tunnel.getTunnelId() + "/"
						+ myRoleMode + " userEventTriggered");
			super.userEventTriggered(ctx, evt);
		}

		@Override
		public void close() throws Exception {
			if (trace)
				logger.info("server socket with session " + serverSessionId + " to " + tunnel.getTunnelId() + "/"
						+ myRoleMode + " closed");
		}

	}

	@Override
	public void close() throws Exception {
		closeServerMainSocket();
		closeClientsSocket();
	}

	private void closeServerMainSocket() {
		if (trace)
			logger.info("server socket " + tunnel.getTunnelId() + "/" + myRoleMode + " closed");
		if (!serverChannelHandler.isEmpty()) {
			for (final SocketChannel client : serverChannelHandler.values()) {
				client.disconnect();
				client.close();
			}
			serverChannelHandler.clear();
		}
		if (mainServerHandler != null) {
			mainServerHandler.cancel(true);
			mainServerHandler = null;
		}
		if (workerGroup != null) {
			try {
				workerGroup.shutdownGracefully().sync();
			} catch (final InterruptedException e) {
				logger.logException("interrupt during worker group shutdown", e);
			}
			workerGroup = null;
		}
		if (bossGroup != null) {
			try {
				bossGroup.shutdownGracefully().sync();
			} catch (final InterruptedException e) {
				logger.logException("interrupt during boss group shutdown", e);
			}
			bossGroup = null;
		}
	}

	private void closeClientsSocket() {
		if (trace)
			logger.info("client socket " + tunnel.getTunnelId() + "/" + myRoleMode + " closed");
		if (!clientChannelHandler.isEmpty()) {
			for (final ChannelFuture client : clientChannelHandler.values()) {
				if (client.channel() != null) {
					client.channel().disconnect();
				}
				client.cancel(true);
			}
			clientChannelHandler.clear();
		}
		if (workerGroup != null) {
			try {
				workerGroup.shutdownGracefully().sync();
			} catch (final InterruptedException e) {
				logger.logException("interrupt during worker group shutdown", e);
			}
			workerGroup = null;
		}
		if (bossGroup != null) {
			try {
				bossGroup.shutdownGracefully().sync();
			} catch (final InterruptedException e) {
				logger.logException("interrupt during boss group shutdown", e);
			}
			bossGroup = null;
		}
	}

	@Override
	public synchronized void deleteClientHandler(long sessionId) {
		if (clientChannelHandler.containsKey(sessionId) && clientChannelHandler.get(sessionId).channel() != null
				&& clientChannelHandler.get(sessionId).channel().isActive()) {
			clientChannelHandler.get(sessionId).channel().disconnect();
			clientChannelHandler.get(sessionId).cancel(true);
		}
		clientChannelHandler.remove(sessionId);
	}

	@Override
	public synchronized void deleteServerSocketChannel(long sessionId) {
		if (serverChannelHandler.containsKey(sessionId) && serverChannelHandler.get(sessionId).isActive()) {
			serverChannelHandler.get(sessionId).disconnect();
			serverChannelHandler.get(sessionId).close();
		}
		serverChannelHandler.remove(sessionId);
	}

	@Override
	public void incrementPacketSend() {
		packetSend.incrementAndGet();
	}

	@Override
	public void incrementPacketError() {
		packetError.incrementAndGet();
	}

	@Override
	public void incrementPacketReceived() {
		packetReceived.incrementAndGet();
	}

	@Override
	public void confirmPacketReceived(long messageUuid) {
		final LockUntilAck lock = packetSemaphores.get(messageUuid);
		if (lock != null) {
			if (trace)
				logger.info("################ confirmed packet " + messageUuid);
			lock.ackReceived();
			packetSemaphores.remove(messageUuid);
		}
	}

	@Override
	public void exceptionPacketReceived(long messageUuid) {
		final LockUntilAck lock = packetSemaphores.get(messageUuid);
		if (lock != null) {
			if (trace)
				logger.info("################ exception packet " + messageUuid);
			lock.resetReceived();
			packetSemaphores.remove(messageUuid);
		}
	}

	@Override
	public long getPacketSend() {
		return packetSend.get();
	}

	@Override
	public long getPacketReceived() {
		return packetReceived.get();
	}

	@Override
	public long getPacketError() {
		return packetError.get();
	}

	@Override
	public int getWaitingPackagesCount() {
		return packetSemaphores.size();
	}

	private static final byte[] compressData(byte[] bytesData) throws IOException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		try (OutputStream dos = new DeflateCompressorOutputStream(os)) {
			dos.write(bytesData);
		}
		if (trace)
			logger.info("compress report: original size:" + bytesData.length + " compressed size:" + os.size());
		return os.toByteArray();
	}

	public static void waitBeacon(BeaconNetworkTunnel beaconNetworkTunnel) throws InterruptedException {
		final long startCheck = new Date().getTime();
		while (((beaconNetworkTunnel.getFromBeaconServer() == null)
				|| (!beaconNetworkTunnel.getFromBeaconServer().isOnline()))
				&& (startCheck + BeaconNetworkTunnel.SYNC_TIME_OUT) > new Date().getTime()) {
			Thread.sleep(BeaconNetworkTunnel.WAIT_WHILE_DELAY);
			// if (trace)
			logger.info("waiting on client " + beaconNetworkTunnel.getFromBeaconServer());
		}
		logger.info("waiting on client done -> " + beaconNetworkTunnel.getFromBeaconServer().isOnline());
	}

}
