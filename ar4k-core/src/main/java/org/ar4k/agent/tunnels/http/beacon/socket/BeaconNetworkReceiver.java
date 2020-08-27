package org.ar4k.agent.tunnels.http.beacon.socket;

import static org.ar4k.agent.tunnels.http.beacon.socket.BeaconNetworkTunnel.trace;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.ar4k.agent.exception.ExceptionNetworkEvent;
import org.ar4k.agent.exception.TimeoutNetworkEvent;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.network.NetworkReceiver;
import org.ar4k.agent.tunnels.http.grpc.beacon.MessageStatus;
import org.ar4k.agent.tunnels.http.grpc.beacon.MessageType;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage;

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

public class BeaconNetworkReceiver implements NetworkReceiver {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconNetworkReceiver.class.toString());

	private final NetworkMode myRoleMode;
	private final long uniqueClassId;
	private final int chunkLimit;
	private final BeaconNetworkTunnel beaconNetworkTunnel;
	private final AtomicLong packetSend = new AtomicLong(0);
	private final AtomicLong packetReceived = new AtomicLong(0);
	private final AtomicLong packetError = new AtomicLong(0);
	private final AtomicLong packetControl = new AtomicLong(0);
	private final AtomicLong progressiveNetworkToBeacon = new AtomicLong(0);
	private final AtomicLong lastAckSent = new AtomicLong(0);
	private final AtomicLong lastAckReceived = new AtomicLong(0);
	private final AtomicLong lastNetworkActiviti = new AtomicLong(0);
	private EventLoopGroup bossGroup = null;
	private EventLoopGroup workerGroup = null;
	private ChannelFuture mainServerHandler = null;
	private final Map<Long, SocketChannel> serverChannelHandler = new ConcurrentHashMap<>();
	private final Map<Long, ChannelFuture> clientChannelHandler = new ConcurrentHashMap<>();
	private final ExecutorService executor = Executors.newCachedThreadPool();
	private final Map<Long, MessageCached> inputCachedMessages = new ConcurrentHashMap<>();
	private final Map<Long, CachedChunk> outputCachedDataBase64ByMessageId = new ConcurrentHashMap<>();
	private final Map<Long, MessageCached> outputCachedMessages = new ConcurrentHashMap<>();
	private boolean terminate = false;

	private long lastControlMessage = new Date().getTime();

	public BeaconNetworkReceiver(final BeaconNetworkTunnel tunnel, final long uniqueClassId, final int chunkLimit) {
		terminate = false;
		this.chunkLimit = chunkLimit;
		this.uniqueClassId = uniqueClassId;
		this.beaconNetworkTunnel = tunnel;
		if (beaconNetworkTunnel.imTheClient()) {
			myRoleMode = NetworkMode.CLIENT;
		} else {
			myRoleMode = NetworkMode.SERVER;
		}
		if (trace)
			logger.info("BeaconNetworkReceiver created [uuid:" + uniqueClassId + "/" + myRoleMode + "]");
	}

	private BeaconNetworkReceiver getBeaconNetworkReceiver() {
		return this;
	}

	@Override
	public long getTunnelId() {
		return beaconNetworkTunnel.getTunnelId();
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
	public long getPacketControl() {
		return packetControl.get();
	}

	@Override
	public int getWaitingPackagesCount() {
		return inputCachedMessages.size();
	}

	ExecutorService getExecutor() {
		return executor;
	}

	Map<Long, CachedChunk> getOutputCachedDataBase64ByMessageId() {
		return outputCachedDataBase64ByMessageId;
	}

	Map<Long, MessageCached> getOutputCachedMessages() {
		return outputCachedMessages;
	}

	NetworkMode getMyRoleMode() {
		return myRoleMode;
	}

	public long getLastAckSent() {
		return lastAckSent.get();
	}

	@Override
	public NetworkStatus getNetworkStatus() {
		if ((myRoleMode.equals(NetworkMode.SERVER) && mainServerHandler != null && mainServerHandler.channel() != null
				&& mainServerHandler.channel().isActive()) || myRoleMode.equals(NetworkMode.CLIENT)) {
			return NetworkStatus.ACTIVE;
		} else {
			if (trace)
				logger.info("Network receiver getStatus for uniqueClassId:" + uniqueClassId + " role " + myRoleMode
						+ " is inactive");
			return NetworkStatus.INACTIVE;
		}
	}

	synchronized void nextAction(final ChannelHandlerContext channelHandlerContext) {
		if (getNetworkStatus().equals(NetworkStatus.ACTIVE)) {
			if (lastControlMessage + BeaconNetworkTunnel.SYNC_TIME_OUT < new Date().getTime()) {
				sendAckOrControlMessage(0, beaconNetworkTunnel.getTunnelId(), lastAckSent.get(), true);
				lastControlMessage = new Date().getTime();
			}
			try {
				if (channelHandlerContext != null) {
					callNextReadOnSocket(channelHandlerContext);
				}
				if (beaconNetworkTunnel.isBeaconConnectionOk(beaconNetworkTunnel)) {
					if (!inputCachedMessages.isEmpty()) {
						final TreeMap<Long, MessageCached> sortedIn = new TreeMap<>();
						sortedIn.putAll(inputCachedMessages);
						boolean firstInput = true;
						for (final MessageCached mi : sortedIn.values()) {
							if (mi.isValidToRetry()) {
								if (mi.softChek()) {
									if (trace)
										logger.info((firstInput ? "first input " : "") + "in role " + myRoleMode
												+ " sessionId " + mi.getSessionID() + " resend message to Beacon "
												+ mi.getMessageId());
								} else {
									if (trace)
										logger.info("in " + myRoleMode + " " + mi.getSessionID()
												+ " cached to Beacon message " + mi.getMessageId());
								}
							} else {
								inputCachedMessages.remove(mi.getMessageId());
							}
							firstInput = false;
						}
					}
				} else {
					if (trace)
						logger.info("in nextAction of the " + myRoleMode + " waiting beacon connection...");
				}
				if (!outputCachedMessages.isEmpty()) {
					final TreeMap<Long, MessageCached> sortedOut = new TreeMap<>();
					sortedOut.putAll(outputCachedMessages);
					boolean firstOutput = true;
					for (final MessageCached mo : sortedOut.values()) {
						if (mo.isValidToRetry()) {
							if (mo.softChek()) {
								if (trace)
									logger.info((firstOutput ? "first input " : "") + "in " + myRoleMode + " "
											+ mo.getSessionID() + " resend message to network " + mo.getMessageId());
							} else {
								if (trace)
									logger.info("in " + myRoleMode + " " + mo.getSessionID()
											+ " cached to network message " + mo.getMessageId());
							}
						} else {
							outputCachedMessages.remove(mo.getMessageId());
						}
						firstOutput = false;
					}
				}
			} catch (final Exception nn) {
				logger.logException(nn);
			}
		} else {
			if (trace)
				logger.info("onNext in " + myRoleMode + " the status is not ACTIVE but " + getNetworkStatus());
		}
	}

	private void callNextReadOnSocket(final ChannelHandlerContext channelHandlerContext) {
		if (channelHandlerContext != null && channelHandlerContext.channel() != null) {
			channelHandlerContext.channel().read();
			if (trace)
				logger.info("next read on socket " + channelHandlerContext + " role " + myRoleMode + " called");
		}
	}

	ChannelFuture getOrCreateClientHandler(final long sessionId) {
		if (!clientChannelHandler.containsKey(sessionId)) {
			createSocketSessionClient(sessionId);
			if (trace)
				logger.info("created client socket for session " + sessionId);
		}
		final ChannelFuture channelFuture = clientChannelHandler.get(sessionId);
		if (channelFuture != null && !channelFuture.isDone()) {
			try {
				channelFuture.await(BeaconNetworkTunnel.SYNC_TIME_OUT);
			} catch (final InterruptedException e) {
				logger.logException(e);
			}
		} else {
			if (trace)
				logger.info("the client socket for session " + sessionId + " is null");
		}
		return channelFuture;
	}

	SocketChannel getOrCreateServerSocketChannel(final long sessionId) {
		if (trace)
			logger.info("opening server channel for session " + sessionId + ". Actual server channels "
					+ serverChannelHandler.size());
		if (mainServerHandler == null || mainServerHandler.isCancelled()) {
			try {
				createServerSocket();
				if (trace)
					logger.info("created server socket for session " + sessionId);
			} catch (final InterruptedException e) {
				logger.logException(e);
			}
		} else {
			if (trace)
				logger.info("the server socket for session " + sessionId + " exists");
		}
		return serverChannelHandler.get(sessionId);
	}

	private synchronized void createSocketSessionClient(final long sessionId) {
		if (workerGroup == null) {
			workerGroup = new NioEventLoopGroup();
		}
		final Bootstrap clientBootstrap = new Bootstrap();
		clientBootstrap.group(workerGroup);
		clientBootstrap.channel(NioSocketChannel.class);
		clientBootstrap.handler(createClientInitializerHandlerForSession(sessionId));
		clientBootstrap.option(ChannelOption.AUTO_READ, false);
		try {
			clientChannelHandler.put(sessionId, clientBootstrap.connect(beaconNetworkTunnel.getConfig().getClientIp(),
					beaconNetworkTunnel.getConfig().getClientPort()));
		} catch (final Exception e) {
			logger.logException("EXCEPTION during client socket creation for sessionId " + sessionId, e);
		}
	}

	private ChannelInitializer<SocketChannel> createClientInitializerHandlerForSession(final long serialId) {
		return new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				final ClientTCPChannelHandler clientTCPChannelHandler = new ClientTCPChannelHandler(serialId);
				socketChannel.pipeline().addLast(clientTCPChannelHandler);
			}
		};
	}

	private final class ClientTCPChannelHandler extends SimpleChannelInboundHandler<ByteBuf> implements AutoCloseable {
		private final long clientSerialId;

		public ClientTCPChannelHandler(long clientSerialId) {
			this.clientSerialId = clientSerialId;
		}

		@Override
		public void channelRead0(final ChannelHandlerContext ctx, final ByteBuf s)
				throws TimeoutNetworkEvent, ExceptionNetworkEvent {
			try {
				if (trace) {
					logger.info("** channelRead0 client for session Id " + clientSerialId + " and tunnel "
							+ beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " received message");
				}
				if (!terminate) {
					final long messageId = progressiveNetworkToBeacon.incrementAndGet();
					final byte[] bytes = ByteBufUtil.getBytes(s);
					inputCachedMessages.put(messageId,
							new MessageCached(MessageCached.MessageCachedType.TO_BEACON, getBeaconNetworkReceiver(),
									beaconNetworkTunnel, ctx, myRoleMode, clientSerialId, messageId, bytes, null,
									chunkLimit, bytes.length, null));
					nextAction(ctx);
				}
			} catch (final Exception ff) {
				incrementPacketError();
				logger.logException(ff);
			}
		}

		@Override
		public void close() throws Exception {
			if (trace)
				logger.info("close session " + clientSerialId + " role " + myRoleMode);
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel sessionId " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " channelActive");
			if (!terminate) {
				try {
					final TunnelMessage tunnelMessage = TunnelMessage.newBuilder()
							.setMessageStatus(MessageStatus.channelActive).setAgentSource(beaconNetworkTunnel.getMe())
							.setTargeId(beaconNetworkTunnel.getTunnelId()).setMessageType(MessageType.FROM_CLIENT)
							.setSessionId(clientSerialId).setUuid(uniqueClassId).build();
					beaconNetworkTunnel.sendMessageToBeaconTunnel(tunnelMessage);
				} catch (final Exception a) {
					logger.warn("sending message channelActive " + a.getMessage());
				}
				nextAction(ctx);
			}
			super.channelActive(ctx);
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel sessionId " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " channelInactive");
			if (!terminate) {
				try {
					final TunnelMessage tunnelMessage = TunnelMessage.newBuilder()
							.setMessageStatus(MessageStatus.channelInactive).setAgentSource(beaconNetworkTunnel.getMe())
							.setTargeId(beaconNetworkTunnel.getTunnelId()).setMessageType(MessageType.FROM_CLIENT)
							.setSessionId(clientSerialId).setUuid(uniqueClassId).build();
					beaconNetworkTunnel.sendMessageToBeaconTunnel(tunnelMessage);
				} catch (final Exception a) {
					logger.warn("sending message channelInactive " + a.getMessage());
				}
			}
			beaconNetworkTunnel.callChannelClientComplete(clientSerialId);
			super.channelInactive(ctx);
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel sessionId " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " channelReadComplete");
			super.channelReadComplete(ctx);
			nextAction(ctx);
		}

		@Override
		public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel sessionId " + clientSerialId + " from " + ctx.channel().localAddress() + "\n"
						+ beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " channelRegistered");
			super.channelRegistered(ctx);
			nextAction(ctx);
		}

		@Override
		public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel sessionId " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " channelUnregistered");
			super.channelUnregistered(ctx);
		}

		@Override
		public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel sessionId " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " channelWritabilityChanged");
			super.channelWritabilityChanged(ctx);
			nextAction(ctx);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			if (trace)
				logger.info("channel sessionId " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " exceptionCaught");
			if (!terminate) {
				beaconNetworkTunnel.callChannelClientException(clientSerialId);
			}
			super.exceptionCaught(ctx, cause);
			nextAction(ctx);
		}

		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			if (trace)
				logger.info("channel sessionId " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " userEventTriggered");
			super.userEventTriggered(ctx, evt);
			nextAction(ctx);
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
			mainServerHandler = b.bind(beaconNetworkTunnel.getConfig().getServerPort()).sync();
			if (mainServerHandler.isSuccess()) {
				if (trace)
					logger.info("server Netty on port " + beaconNetworkTunnel.getConfig().getServerPort()
							+ " with tunnelId " + beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode
							+ " started successfully");
			} else {
				closeServerMainSocket();
				logger.warn("server Netty on port " + beaconNetworkTunnel.getConfig().getServerPort()
						+ " with id_target " + beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode
						+ " error on mainServerHandler, isSuccess replies " + mainServerHandler.isSuccess());
			}
		} catch (final Exception a) {
			logger.logException("EXCEPTION creating server socket for role " + myRoleMode, a);
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
			serverSocketChannel.pipeline().addLast(serverTCPSocketChannelHandler);
		}
	}

	private final class ServerTCPHandler extends SimpleChannelInboundHandler<ByteBuf> implements AutoCloseable {
		private final long serverSessionId = UUID.randomUUID().getMostSignificantBits(); // battezza la sessione

		public ServerTCPHandler(SocketChannel serverSocketChannel) {
			serverChannelHandler.put(serverSessionId, serverSocketChannel);
			if (trace)
				logger.info("server socket for sessionId " + serverSessionId + " to "
						+ beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " created");
		}

		@Override
		protected void channelRead0(final ChannelHandlerContext ctx, final ByteBuf s) throws TimeoutNetworkEvent {
			try {
				if (trace)
					logger.info("** channelRead0 server for sessionId " + serverSessionId + " to "
							+ beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " received message");
				final long messageId = progressiveNetworkToBeacon.incrementAndGet();
				final byte[] bytes = ByteBufUtil.getBytes(s);
				if (!terminate) {
					inputCachedMessages.put(messageId,
							new MessageCached(MessageCached.MessageCachedType.TO_BEACON, getBeaconNetworkReceiver(),
									beaconNetworkTunnel, ctx, myRoleMode, serverSessionId, messageId, bytes, null,
									chunkLimit, bytes.length, null));
				}
				nextAction(ctx);
			} catch (final Exception ff) {
				incrementPacketError();
				logger.logException(ff);
			}
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel server with " + ctx.channel().remoteAddress() + "\n"
						+ beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " channelActive");
			if (!terminate) {
				try {
					final TunnelMessage tunnelMessage = TunnelMessage.newBuilder()
							.setMessageStatus(MessageStatus.channelActive).setAgentSource(beaconNetworkTunnel.getMe())
							.setTargeId(beaconNetworkTunnel.getTunnelId()).setSessionId(serverSessionId)
							.setMessageType(MessageType.FROM_SERVER).build();
					beaconNetworkTunnel.sendMessageToBeaconTunnel(tunnelMessage);
				} catch (final Exception a) {
					logger.warn("sending message channelActive " + a.getMessage());
				}
			}
			super.channelActive(ctx);
			nextAction(ctx);
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel server with " + ctx.channel().remoteAddress() + "\n"
						+ beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " channelInactive");
			if (!terminate) {
				try {
					final TunnelMessage tunnelMessage = TunnelMessage.newBuilder()
							.setMessageStatus(MessageStatus.channelInactive).setAgentSource(beaconNetworkTunnel.getMe())
							.setTargeId(beaconNetworkTunnel.getTunnelId()).setSessionId(serverSessionId)
							.setMessageType(MessageType.FROM_SERVER).build();
					beaconNetworkTunnel.sendMessageToBeaconTunnel(tunnelMessage);
				} catch (final Exception a) {
					logger.warn("sending message channelInactive " + a.getMessage());
				}
			}
			beaconNetworkTunnel.callChannelServerComplete(serverSessionId);
			super.channelInactive(ctx);
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel server with " + ctx.channel().remoteAddress() + "\n"
						+ beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " channelReadComplete");
			super.channelReadComplete(ctx);
			nextAction(ctx);
		}

		@Override
		public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel server from " + ctx.channel().localAddress() + "\n"
						+ beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " channelRegistered");
			super.channelRegistered(ctx);
			nextAction(ctx);
		}

		@Override
		public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel server with " + ctx.channel().remoteAddress() + "\n"
						+ beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " channelUnregistered");
			super.channelUnregistered(ctx);
		}

		@Override
		public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
			if (trace)
				logger.info("channel server with " + ctx.channel().remoteAddress() + "\n"
						+ beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " channelWritabilityChanged");
			super.channelWritabilityChanged(ctx);
			nextAction(ctx);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			logger.info("channel server with " + ctx.channel().remoteAddress() + "\n"
					+ beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " exceptionCaught");
			if (!terminate) {
				beaconNetworkTunnel.callChannelServerException(serverSessionId);
			}
			super.exceptionCaught(ctx, cause);
			nextAction(ctx);
		}

		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			if (trace)
				logger.info("channel server with " + ctx.channel().remoteAddress() + "\n"
						+ beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " userEventTriggered");
			super.userEventTriggered(ctx, evt);
			nextAction(ctx);
		}

		@Override
		public void close() throws Exception {
			if (trace)
				logger.info("server socket with session " + serverSessionId + " to " + beaconNetworkTunnel.getTunnelId()
						+ " role " + myRoleMode + " closed");
		}

	}

	@Override
	public void close() throws Exception {
		terminate = true;
		for (final MessageCached m : inputCachedMessages.values()) {
			m.end();
		}
		inputCachedMessages.clear();
		for (final MessageCached m : outputCachedMessages.values()) {
			m.end();
		}
		outputCachedMessages.clear();
		outputCachedDataBase64ByMessageId.clear();
		executor.shutdownNow();
		closeServerMainSocket();
		closeClientsSocket();
	}

	private void closeServerMainSocket() {
		if (trace)
			logger.info("server socket " + beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " closing");
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
			logger.info("client socket " + beaconNetworkTunnel.getTunnelId() + " role " + myRoleMode + " closed");
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

	synchronized void deleteClientHandler(long sessionId) {
		if (clientChannelHandler.containsKey(sessionId) && clientChannelHandler.get(sessionId).channel() != null
				&& clientChannelHandler.get(sessionId).channel().isActive()) {
			clientChannelHandler.get(sessionId).channel().disconnect();
			clientChannelHandler.get(sessionId).cancel(true);
		}
		clientChannelHandler.remove(sessionId);
	}

	synchronized void deleteServerSocketChannel(long sessionId) {
		if (serverChannelHandler.containsKey(sessionId) && serverChannelHandler.get(sessionId).isActive()) {
			serverChannelHandler.get(sessionId).disconnect();
			serverChannelHandler.get(sessionId).close();
		}
		serverChannelHandler.remove(sessionId);
	}

	@Override
	public void incrementPacketSend() {
		lastNetworkActiviti.set(new Date().getTime());
		packetSend.incrementAndGet();
	}

	@Override
	public void incrementPacketError() {
		lastNetworkActiviti.set(new Date().getTime());
		packetError.incrementAndGet();
	}

	@Override
	public void incrementPacketControl() {
		lastNetworkActiviti.set(new Date().getTime());
		packetControl.incrementAndGet();
	}

	@Override
	public void incrementPacketReceived() {
		lastNetworkActiviti.set(new Date().getTime());
		packetReceived.incrementAndGet();
	}

	synchronized boolean isNextMessageToNetwork(long serialId, long tunnelId, long messageId,
			MessageStatus messageStatus) {
		if (messageId - 1 == lastAckSent.get()) {
			if (trace)
				logger.info("################ for " + myRoleMode + " session " + serialId + " is next message "
						+ messageId + " [" + messageStatus + "]: TRUE, actual is " + lastAckSent.get());
			return true;
		} else {
			if (trace)
				logger.info("################ for " + myRoleMode + " session " + serialId + " is next message "
						+ messageId + " [" + messageStatus + "]: FALSE, actual is " + lastAckSent.get());
			return false;
		}
	}

	/*
	 * boolean isOlderMessageToNetwork(long serialId, long tunnelId, long messageId,
	 * MessageStatus messageStatus) { if (messageId - 1 < lastAckSent.get()) { if
	 * (trace) logger.info("################ is older message " + messageId + " [" +
	 * messageStatus + "]: TRUE, actual is " + lastAckSent.get()); return true; }
	 * else { if (trace) logger.info("################ is older message " +
	 * messageId + " [" + messageStatus + "]: FALSE, actual is " +
	 * lastAckSent.get()); return false; } }
	 */

	@Override
	public void confirmPacketReceived(long messageUuid, long lastReceived) {
		incrementPacketControl();
		for (final Long serialPacket : inputCachedMessages.keySet()) {
			if (serialPacket <= messageUuid) {
				final MessageCached lock = inputCachedMessages.get(serialPacket);
				if (trace)
					logger.info("################ confirmed packet to Beacon and remove it " + serialPacket
							+ " beacause ack received is " + messageUuid);
				lock.ackReceived();
				inputCachedMessages.remove(serialPacket);
				updateLastAckReceivedIfNeed(messageUuid);
			}
		}
		for (final Long serial : outputCachedMessages.keySet()) {
			if (serial <= lastReceived) {
				if (trace)
					logger.info("################ remove packet to network " + serial
							+ " beacause the last received on the other side is  " + lastReceived);
				outputCachedMessages.remove(serial);
				beaconNetworkTunnel.getNetworkReceiver().getOutputCachedDataBase64ByMessageId().remove(serial);
			} else {
				if (outputCachedMessages != null && outputCachedMessages.containsKey(serial))
					outputCachedMessages.get(serial).isCompleteOrTryToSend();
			}
		}
	}

	private synchronized void updateLastAckReceivedIfNeed(long messageUuid) {
		if (messageUuid > lastAckReceived.get()) {
			lastAckReceived.set(messageUuid);
		}
	}

	private synchronized void updateLastAckSentIfNeed(long messageUuid) {
		if (messageUuid > lastAckSent.get()) {
			lastAckSent.set(messageUuid);
		}
	}

	@Override
	public void exceptionPacketReceived(long messageUuid) {
		incrementPacketControl();
		final MessageCached lock = inputCachedMessages.get(messageUuid);
		if (lock != null) {
			if (trace)
				logger.info("################ exception packet " + messageUuid);
			lock.resetReceived();
		} else {
			logger.warn("request resend message " + messageUuid + " but the lock object is null");
		}
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("BeaconNetworkReceiver [");
		if (myRoleMode != null)
			builder.append("myRoleMode=").append(myRoleMode).append(", ");
		builder.append("uniqueClassId=").append(uniqueClassId).append(", chunkLimit=").append(chunkLimit).append(", ");
		if (beaconNetworkTunnel != null)
			builder.append("tunnel=").append(beaconNetworkTunnel).append(", ");
		if (packetSend != null)
			builder.append("packetSend=").append(packetSend).append(", ");
		if (packetReceived != null)
			builder.append("packetReceived=").append(packetReceived).append(", ");
		if (packetError != null)
			builder.append("packetError=").append(packetError).append(", ");
		if (packetControl != null)
			builder.append("packetControl=").append(packetControl).append(", ");
		if (progressiveNetworkToBeacon != null)
			builder.append("progressivePacketCounter=").append(progressiveNetworkToBeacon).append(", ");
		builder.append("terminate=").append(terminate);
		builder.append("]\n");
		builder.append("details channel=").append(reportDetails());
		return builder.toString();
	}

	String reportDetails() {
		final StringBuilder sb = new StringBuilder();
		if (trace) {
			sb.append("\n--------------------\nDETAILS BeaconNetworkReceiver\n");
			sb.append("ROLE: " + myRoleMode + "\n");
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
					+ inputCachedMessages.size() + "] -> " + inputCachedMessages.entrySet().stream()
							.map(n -> (n.getKey() + "[" + n.getValue() + "]")).collect(Collectors.joining(", "))
					+ "\n");
			sb.append("--------------------\n");
			if (!inputCachedMessages.isEmpty()) {
				sb.append("INPUT MESSAGE IN CACHE\n");
				for (final MessageCached m : inputCachedMessages.values()) {
					sb.append(". " + m.toString() + "\n");
				}
			}
			if (!outputCachedMessages.isEmpty()) {
				sb.append("OUTPUT MESSAGE IN CACHE\n");
				for (final MessageCached m : outputCachedMessages.values()) {
					sb.append(". " + m.toString() + "\n");
				}
			}
		}
		return sb.toString();
	}

	void sendExceptionMessage(long serverSessionId, long targertId, long messageUuid, Exception clientEx) {
		try {
			final TunnelMessage tunnelMessage = TunnelMessage.newBuilder()
					.setMessageStatus(MessageStatus.exceptionCaught).setAgentSource(beaconNetworkTunnel.getMe())
					.setTargeId(targertId).setSessionId(serverSessionId)
					.setMessageType(
							myRoleMode.equals(NetworkMode.CLIENT) ? MessageType.FROM_CLIENT : MessageType.FROM_SERVER)
					.setPayload(EdgeLogger.stackTraceToString(clientEx)).build();

			beaconNetworkTunnel.sendMessageToBeaconTunnel(tunnelMessage);
			incrementPacketControl();
			if (trace)
				logger.info("################ exception message sent " + messageUuid);

		} catch (final Exception a) {
			logger.warn("sending message exception " + a.getMessage());
		}
	}

	void sendAckOrControlMessage(long serverSessionId, long targertId, long messageUuid, boolean control) {
		try {
			final TunnelMessage tunnelMessage = TunnelMessage.newBuilder()
					.setMessageStatus(control ? MessageStatus.beaconMessageControl : MessageStatus.beaconMessageAck)
					.setAgentSource(beaconNetworkTunnel.getMe()).setTargeId(targertId).setSessionId(serverSessionId)
					.setPayload(String.valueOf(messageUuid)).setMessageUuid(lastAckReceived.get())
					.setMessageType(
							myRoleMode.equals(NetworkMode.CLIENT) ? MessageType.FROM_CLIENT : MessageType.FROM_SERVER)
					.build();
			beaconNetworkTunnel.sendMessageToBeaconTunnel(tunnelMessage);
			incrementPacketControl();
			updateLastAckSentIfNeed(messageUuid);
			if (trace)
				logger.info("################ " + (control ? "control" : "ack") + " message sent " + messageUuid);
		} catch (final Exception a) {
			logger.warn("sending message ack " + a.getMessage());
		}
	}

}
