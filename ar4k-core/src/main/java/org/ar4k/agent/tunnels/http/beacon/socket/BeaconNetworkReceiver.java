package org.ar4k.agent.tunnels.http.beacon.socket;

import static org.ar4k.agent.tunnels.http.beacon.socket.BeaconNetworkTunnel.TRACE_LOG_IN_INFO;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

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

	private final class ServerTCPHandler extends SimpleChannelInboundHandler<ByteBuf> implements AutoCloseable {
		private final long serverSessionId = UUID.randomUUID().getMostSignificantBits(); // battezza la sessione

		public ServerTCPHandler(SocketChannel serverSocketChannel) {
			getServerChannelHandler().put(serverSessionId, serverSocketChannel);
			if (TRACE_LOG_IN_INFO)
				logger.info("server socket for sessionId {} to {} role {} created", serverSessionId,
						getBeaconNetworkTunnel().getTunnelId(), myRoleMode);
		}

		@Override
		protected void channelRead0(final ChannelHandlerContext ctx, final ByteBuf s) {
			try {
				if (TRACE_LOG_IN_INFO) {
					final String reportDetails = getBeaconNetworkTunnel().reportDetails();
					logger.info("** channelRead0 server for sessionId {} to {} role {} received message\n{}",
							serverSessionId, getBeaconNetworkTunnel().getTunnelId(), myRoleMode, reportDetails);
				}
				final long messageId = getBeaconNetworkTunnel().getProgressiveNetworkToBeacon(serverSessionId);
				final byte[] bytes = ByteBufUtil.getBytes(s);
				if (!terminate) {
					getBeaconNetworkTunnel().addInputCachedMessage(serverSessionId, messageId,
							new MessageCached(MessageCached.MessageCachedType.TO_BEACON, getBeaconNetworkReceiver(),
									getBeaconNetworkTunnel(), ctx, myRoleMode, serverSessionId, messageId, bytes, null,
									chunkLimit, bytes.length, null));
				}
				nextAction(serverSessionId, ctx);
			} catch (final Exception ff) {
				getBeaconNetworkTunnel().incrementPacketError();
				logger.logException(ff);
			}
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			if (TRACE_LOG_IN_INFO)
				logger.info("channel server with " + ctx.channel().remoteAddress() + "\n"
						+ getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode + " channelActive");
			if (!terminate) {
				try {
					getBeaconNetworkTunnel().registerSocketContext(serverSessionId, ctx);
					final TunnelMessage tunnelMessage = TunnelMessage.newBuilder()
							.setMessageStatus(MessageStatus.channelActive)
							.setAgentSource(getBeaconNetworkTunnel().getMe())
							.setTunnelId(getBeaconNetworkTunnel().getTunnelId()).setSessionId(serverSessionId)
							.setMessageType(MessageType.FROM_SERVER).build();
					getBeaconNetworkTunnel().sendMessageToBeaconTunnel(tunnelMessage);
				} catch (final Exception a) {
					logger.warn("sending message channelActive " + a.getMessage());
				}
			}
			super.channelActive(ctx);
			nextAction(serverSessionId, ctx);
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			if (TRACE_LOG_IN_INFO)
				logger.info("channel server with " + ctx.channel().remoteAddress() + "\n"
						+ getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode + " channelInactive");
			if (!terminate) {
				try {
					final TunnelMessage tunnelMessage = TunnelMessage.newBuilder()
							.setMessageStatus(MessageStatus.channelInactive)
							.setAgentSource(getBeaconNetworkTunnel().getMe())
							.setTunnelId(getBeaconNetworkTunnel().getTunnelId()).setSessionId(serverSessionId)
							.setMessageType(MessageType.FROM_SERVER).build();
					getBeaconNetworkTunnel().sendMessageToBeaconTunnel(tunnelMessage);
				} catch (final Exception a) {
					logger.warn("sending message channelInactive " + a.getMessage());
				}
			}
			getBeaconNetworkTunnel().callChannelServerComplete(serverSessionId);
			super.channelInactive(ctx);
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			if (TRACE_LOG_IN_INFO)
				logger.info("channel server with " + ctx.channel().remoteAddress() + "\n"
						+ getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode + " channelReadComplete");
			super.channelReadComplete(ctx);
			nextAction(serverSessionId, ctx);
		}

		@Override
		public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
			if (TRACE_LOG_IN_INFO)
				logger.info("channel server from " + ctx.channel().localAddress() + "\n"
						+ getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode + " channelRegistered");
			super.channelRegistered(ctx);
			nextAction(serverSessionId, ctx);
		}

		@Override
		public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
			if (TRACE_LOG_IN_INFO)
				logger.info("channel server with " + ctx.channel().remoteAddress() + "\n"
						+ getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode + " channelUnregistered");
			super.channelUnregistered(ctx);
		}

		@Override
		public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
			if (TRACE_LOG_IN_INFO)
				logger.info("channel server with " + ctx.channel().remoteAddress() + "\n"
						+ getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode
						+ " channelWritabilityChanged");
			super.channelWritabilityChanged(ctx);
			nextAction(serverSessionId, ctx);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			logger.info("channel server with " + ctx.channel().remoteAddress() + "\n"
					+ getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode + " exceptionCaught");
			if (!terminate) {
				getBeaconNetworkTunnel().callChannelServerException(serverSessionId);
			}
			super.exceptionCaught(ctx, cause);
			nextAction(serverSessionId, ctx);
		}

		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			if (TRACE_LOG_IN_INFO)
				logger.info("channel server with " + ctx.channel().remoteAddress() + "\n"
						+ getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode + " userEventTriggered");
			super.userEventTriggered(ctx, evt);
			nextAction(serverSessionId, ctx);
		}

		@Override
		public void close() throws Exception {
			if (TRACE_LOG_IN_INFO)
				logger.info("server socket with session " + serverSessionId + " to "
						+ getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode + " closed");
		}

	}

	private final class ServerTcpInitHandler extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel serverSocketChannel) throws Exception {
			final ServerTCPHandler serverTCPSocketChannelHandler = new ServerTCPHandler(serverSocketChannel);
			serverSocketChannel.pipeline().addLast(serverTCPSocketChannelHandler);
		}
	}

	private final class ClientTCPChannelHandler extends SimpleChannelInboundHandler<ByteBuf> implements AutoCloseable {
		private final long clientSerialId;

		public ClientTCPChannelHandler(long clientSerialId) {
			this.clientSerialId = clientSerialId;
		}

		@Override
		public void channelRead0(final ChannelHandlerContext ctx, final ByteBuf s) {
			try {
				if (TRACE_LOG_IN_INFO) {
					final String reportDetails = getBeaconNetworkTunnel().reportDetails();
					logger.info("** channelRead0 client for session Id {} and tunnel {} role {} received message\n{}",
							clientSerialId, getBeaconNetworkTunnel().getTunnelId(), myRoleMode, reportDetails);
				}
				if (!terminate) {
					final long messageId = getBeaconNetworkTunnel().getProgressiveNetworkToBeacon(clientSerialId);
					final byte[] bytes = ByteBufUtil.getBytes(s);
					getBeaconNetworkTunnel().addInputCachedMessage(clientSerialId, messageId,
							new MessageCached(MessageCached.MessageCachedType.TO_BEACON, getBeaconNetworkReceiver(),
									getBeaconNetworkTunnel(), ctx, myRoleMode, clientSerialId, messageId, bytes, null,
									chunkLimit, bytes.length, null));
					nextAction(clientSerialId, ctx);
				}
			} catch (final Exception ff) {
				getBeaconNetworkTunnel().incrementPacketError();
				logger.logException(ff);
			}
		}

		@Override
		public void close() throws Exception {
			if (TRACE_LOG_IN_INFO)
				logger.info("close session " + clientSerialId + " role " + myRoleMode);
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			if (TRACE_LOG_IN_INFO)
				logger.info("channel sessionId " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode + " channelActive");
			if (!terminate) {
				try {
					getBeaconNetworkTunnel().registerSocketContext(clientSerialId, ctx);
					final TunnelMessage tunnelMessage = TunnelMessage.newBuilder()
							.setMessageStatus(MessageStatus.channelActive)
							.setAgentSource(getBeaconNetworkTunnel().getMe())
							.setTunnelId(getBeaconNetworkTunnel().getTunnelId()).setMessageType(MessageType.FROM_CLIENT)
							.setSessionId(clientSerialId).setClassUuid(uniqueClassId).build();
					getBeaconNetworkTunnel().sendMessageToBeaconTunnel(tunnelMessage);
				} catch (final Exception a) {
					logger.warn("sending message channelActive {}", a.getMessage());
				}
				nextAction(clientSerialId, ctx);
			}
			super.channelActive(ctx);
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			if (TRACE_LOG_IN_INFO)
				logger.info("channel sessionId " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode + " channelInactive");
			if (!terminate) {
				try {
					final TunnelMessage tunnelMessage = TunnelMessage.newBuilder()
							.setMessageStatus(MessageStatus.channelInactive)
							.setAgentSource(getBeaconNetworkTunnel().getMe())
							.setTunnelId(getBeaconNetworkTunnel().getTunnelId()).setMessageType(MessageType.FROM_CLIENT)
							.setSessionId(clientSerialId).setClassUuid(uniqueClassId).build();
					getBeaconNetworkTunnel().sendMessageToBeaconTunnel(tunnelMessage);
				} catch (final Exception a) {
					logger.warn("sending message channelInactive {}", a.getMessage());
				}
			}
			getBeaconNetworkTunnel().callChannelClientComplete(clientSerialId);
			super.channelInactive(ctx);
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			if (TRACE_LOG_IN_INFO)
				logger.info("channel sessionId " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode + " channelReadComplete");
			super.channelReadComplete(ctx);
			nextAction(clientSerialId, ctx);
		}

		@Override
		public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
			if (TRACE_LOG_IN_INFO)
				logger.info("channel sessionId " + clientSerialId + " from " + ctx.channel().localAddress() + "\n"
						+ getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode + " channelRegistered");
			super.channelRegistered(ctx);
			nextAction(clientSerialId, ctx);
		}

		@Override
		public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
			if (TRACE_LOG_IN_INFO)
				logger.info("channel sessionId " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode + " channelUnregistered");
			super.channelUnregistered(ctx);
		}

		@Override
		public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
			if (TRACE_LOG_IN_INFO)
				logger.info("channel sessionId " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode
						+ " channelWritabilityChanged");
			super.channelWritabilityChanged(ctx);
			nextAction(clientSerialId, ctx);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			if (TRACE_LOG_IN_INFO)
				logger.info("channel sessionId " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode + " exceptionCaught");
			if (!terminate) {
				getBeaconNetworkTunnel().callChannelClientException(clientSerialId);
			}
			super.exceptionCaught(ctx, cause);
			nextAction(clientSerialId, ctx);
		}

		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			if (TRACE_LOG_IN_INFO)
				logger.info("channel sessionId " + clientSerialId + " with " + ctx.channel().remoteAddress() + "\n"
						+ getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode + " userEventTriggered");
			super.userEventTriggered(ctx, evt);
			nextAction(clientSerialId, ctx);
		}
	}

	private final NetworkMode myRoleMode;
	private final long uniqueClassId;
	private final int chunkLimit;
	private final BeaconNetworkTunnel beaconNetworkTunnel;
	private final AtomicLong lastNetworkActiviti = new AtomicLong(0);
	private EventLoopGroup bossGroup = null;
	private EventLoopGroup workerGroup = null;
	private ChannelFuture mainServerHandler = null;
	private final Map<Long, SocketChannel> serverChannelHandler = new ConcurrentHashMap<>();
	private final Map<Long, ChannelFuture> clientChannelHandler = new ConcurrentHashMap<>();
	private final ExecutorService executor = Executors.newCachedThreadPool();
	private final Map<Long, CachedChunk> outputCachedDataBase64ByMessageId = new ConcurrentHashMap<>();
	private boolean terminate = false;

	public BeaconNetworkReceiver(final BeaconNetworkTunnel tunnel, final long uniqueClassId, final int chunkLimit) {
		terminate = false;
		this.chunkLimit = chunkLimit;
		this.uniqueClassId = uniqueClassId;
		this.beaconNetworkTunnel = tunnel;
		if (getBeaconNetworkTunnel().imTheClient()) {
			myRoleMode = NetworkMode.CLIENT;
		} else {
			myRoleMode = NetworkMode.SERVER;
		}
		if (TRACE_LOG_IN_INFO)
			logger.info("BeaconNetworkReceiver created [ uuid: {} role: {} ]", uniqueClassId, myRoleMode);
	}

	private BeaconNetworkReceiver getBeaconNetworkReceiver() {
		return this;
	}

	@Override
	public long getTunnelId() {
		return getBeaconNetworkTunnel().getTunnelId();
	}

	ExecutorService getExecutor() {
		return executor;
	}

	public BeaconNetworkTunnel getBeaconNetworkTunnel() {
		return beaconNetworkTunnel;
	}

	Map<Long, CachedChunk> getOutputCachedDataBase64ByMessageId() {
		return outputCachedDataBase64ByMessageId;
	}

	NetworkMode getMyRoleMode() {
		return myRoleMode;
	}

	long getLastAckSent(long sessionId) {
		return getBeaconNetworkTunnel().getLastAckSent(sessionId);
	}

	@Override
	public NetworkStatus getNetworkStatus() {
		if ((myRoleMode.equals(NetworkMode.SERVER) && getMainServerHandler() != null
				&& getMainServerHandler().channel() != null && getMainServerHandler().channel().isActive())
				|| myRoleMode.equals(NetworkMode.CLIENT)) {
			return NetworkStatus.ACTIVE;
		} else {
			if (TRACE_LOG_IN_INFO)
				logger.info("Network receiver getStatus for uniqueClassId: {} role {} is inactive", uniqueClassId,
						myRoleMode);
			return NetworkStatus.INACTIVE;
		}
	}

	Map<Long, ChannelFuture> getClientChannelHandler() {
		return clientChannelHandler;
	}

	synchronized void nextAction(final long sessionId, final ChannelHandlerContext channelHandlerContext) {
		if (getNetworkStatus().equals(NetworkStatus.ACTIVE)) {
			if (getBeaconNetworkTunnel().getLastControlMessage() + BeaconNetworkTunnel.SYNC_TIME_OUT < new Date()
					.getTime()) {
				sendAckOrControlMessage(sessionId, getBeaconNetworkTunnel().getTunnelId(),
						getBeaconNetworkTunnel().getLastAckSent(sessionId), true);
				getBeaconNetworkTunnel().setLastControlMessage();
			}
			try {
				if (channelHandlerContext != null) {
					// callNextReadOnSocket(channelHandlerContext);
				}
				if (getBeaconNetworkTunnel().isBeaconConnectionOk()) {
					if (!getBeaconNetworkTunnel().getInputCachedMessages(sessionId).isEmpty()) {
						final TreeMap<Long, MessageCached> sortedIn = new TreeMap<>();
						sortedIn.putAll(getBeaconNetworkTunnel().getInputCachedMessages(sessionId));
						boolean firstInput = true;
						for (final MessageCached mi : sortedIn.values()) {
							if (mi.isValidToRetry()) {
								if (mi.softChek()) {
									if (TRACE_LOG_IN_INFO)
										logger.info("{} in sessionId {} resend message {} to Beacon ",
												(firstInput ? "first input " : ""), mi.getSessionID(),
												mi.getMessageId());
								} else {
									if (TRACE_LOG_IN_INFO)
										logger.info("in role {} sessionId {} messageId {} cached to Beacon message ",
												myRoleMode, mi.getSessionID(), mi.getMessageId());
								}
							} else {
								getBeaconNetworkTunnel().removeIputCachedMessage(sessionId, mi.getMessageId());
							}
							firstInput = false;
						}
					}
				} else {
					if (TRACE_LOG_IN_INFO)
						logger.info("in nextAction of the " + myRoleMode + " waiting beacon connection...");
				}
				if (!getBeaconNetworkTunnel().getOutputCachedMessages(sessionId).isEmpty()) {
					final TreeMap<Long, MessageCached> sortedOut = new TreeMap<>();
					sortedOut.putAll(getBeaconNetworkTunnel().getOutputCachedMessages(sessionId));
					boolean firstOutput = true;
					for (final MessageCached mo : sortedOut.values()) {
						if (mo.isValidToRetry()) {
							if (mo.softChek()) {
								if (TRACE_LOG_IN_INFO)
									logger.info((firstOutput ? "first input " : "") + "in " + myRoleMode + " "
											+ mo.getSessionID() + " resend message to network " + mo.getMessageId());
							} else {
								if (TRACE_LOG_IN_INFO)
									logger.info("in " + myRoleMode + " " + mo.getSessionID()
											+ " cached to network message " + mo.getMessageId());
							}
						} else {
							getBeaconNetworkTunnel().removeOutputCachedMessage(sessionId, mo.getMessageId());
						}
						firstOutput = false;
					}
				}
			} catch (final Exception nn) {
				logger.logException(nn);
			}
		} else {
			if (TRACE_LOG_IN_INFO)
				logger.info("onNext in {} the status is not ACTIVE but {}", myRoleMode, getNetworkStatus());
		}
	}

	private void callNextReadOnSocket(final ChannelHandlerContext channelHandlerContext) {
		if (channelHandlerContext != null && channelHandlerContext.channel() != null) {
			channelHandlerContext.channel().read();
			if (TRACE_LOG_IN_INFO)
				logger.info("next read on socket " + channelHandlerContext + " role " + myRoleMode + " called");
		}
	}

	ChannelFuture getOrCreateClientHandler(final long sessionId) throws InterruptedException {
		if (!getClientChannelHandler().containsKey(sessionId)) {
			createSocketSessionClient(sessionId);
			if (TRACE_LOG_IN_INFO)
				logger.info("created client socket for session {}", sessionId);
		}
		final ChannelFuture channelFuture = getClientChannelHandler().get(sessionId);
		if (channelFuture != null && !channelFuture.isDone()) {

			channelFuture.await(BeaconNetworkTunnel.SYNC_TIME_OUT);
		} else {
			if (TRACE_LOG_IN_INFO)
				logger.info("the client socket for session {} is null", sessionId);
		}
		return channelFuture;
	}

	SocketChannel getOrCreateServerSocketChannel(final long sessionId) throws InterruptedException {
		if (TRACE_LOG_IN_INFO)
			logger.info("opening server channel for session " + sessionId + ". Actual server channels "
					+ getServerChannelHandler().size());
		if (getMainServerHandler() == null || getMainServerHandler().isCancelled()) {
			createServerSocket();
			if (TRACE_LOG_IN_INFO)
				logger.info("created server socket for session {}", sessionId);
		} else {
			if (TRACE_LOG_IN_INFO)
				logger.info("the server socket for session exists {}", sessionId);
		}
		return getServerChannelHandler().get(sessionId);
	}

	private synchronized void createSocketSessionClient(final long sessionId) {
		if (workerGroup == null) {
			workerGroup = new NioEventLoopGroup();
		}
		final Bootstrap clientBootstrap = new Bootstrap();
		clientBootstrap.group(workerGroup);
		clientBootstrap.channel(NioSocketChannel.class);
		clientBootstrap.handler(createClientInitializerHandlerForSession(sessionId));
		// clientBootstrap.option(ChannelOption.AUTO_READ, false);
		try {
			getClientChannelHandler().put(sessionId,
					clientBootstrap.connect(getBeaconNetworkTunnel().getConfig().getClientIp(),
							getBeaconNetworkTunnel().getConfig().getClientPort()));
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
					.childOption(ChannelOption.SO_KEEPALIVE, true).childOption(ChannelOption.SO_REUSEADDR, true);// .childOption(ChannelOption.AUTO_READ,
																													// false);
			mainServerHandler = b.bind(getBeaconNetworkTunnel().getConfig().getServerPort()).sync();
			if (getMainServerHandler().isSuccess()) {
				if (TRACE_LOG_IN_INFO)
					logger.info("server Netty on port " + getBeaconNetworkTunnel().getConfig().getServerPort()
							+ " with tunnelId " + getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode
							+ " started successfully");
			} else {
				closeServerMainSocket();
				logger.warn("server Netty on port " + getBeaconNetworkTunnel().getConfig().getServerPort()
						+ " with id_target " + getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode
						+ " error on mainServerHandler, isSuccess replies " + getMainServerHandler().isSuccess());
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

	@Override
	public void close() throws Exception {
		terminate = true;
		outputCachedDataBase64ByMessageId.clear();
		executor.shutdownNow();
		closeServerMainSocket();
		closeClientsSocket();
	}

	private void closeServerMainSocket() throws InterruptedException {
		if (TRACE_LOG_IN_INFO)
			logger.info("server socket " + getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode + " closing");
		if (!getServerChannelHandler().isEmpty()) {
			for (final SocketChannel client : getServerChannelHandler().values()) {
				client.disconnect();
				client.close();
			}
			getServerChannelHandler().clear();
		}
		if (getMainServerHandler() != null) {
			getMainServerHandler().cancel(true);
			mainServerHandler = null;
		}
		if (workerGroup != null) {
			workerGroup.shutdownGracefully().sync();
			workerGroup = null;
		}
		if (bossGroup != null) {
			bossGroup.shutdownGracefully().sync();
			bossGroup = null;
		}
	}

	private void closeClientsSocket() {
		if (TRACE_LOG_IN_INFO)
			logger.info("client socket " + getBeaconNetworkTunnel().getTunnelId() + " role " + myRoleMode + " closed");
		if (!getClientChannelHandler().isEmpty()) {
			for (final ChannelFuture client : getClientChannelHandler().values()) {
				if (client.channel() != null) {
					client.channel().disconnect();
				}
				client.cancel(true);
			}
			getClientChannelHandler().clear();
		}
		if (workerGroup != null) {
			try {
				workerGroup.shutdownGracefully().sync();
			} catch (final Exception e) {
				logger.logException("exception during worker group shutdown", e);
			}
			workerGroup = null;
		}
		if (bossGroup != null) {
			try {
				bossGroup.shutdownGracefully().sync();
			} catch (final Exception e) {
				logger.logException("exception during boss group shutdown", e);
			}
			bossGroup = null;
		}
	}

	synchronized void deleteClientHandler(long sessionId) {
		if (getClientChannelHandler().containsKey(sessionId)
				&& getClientChannelHandler().get(sessionId).channel() != null
				&& getClientChannelHandler().get(sessionId).channel().isActive()) {
			getClientChannelHandler().get(sessionId).channel().disconnect();
			getClientChannelHandler().get(sessionId).cancel(true);
		}
		getClientChannelHandler().remove(sessionId);
	}

	synchronized void deleteServerSocketChannel(long sessionId) {
		if (getServerChannelHandler().containsKey(sessionId) && getServerChannelHandler().get(sessionId).isActive()) {
			getServerChannelHandler().get(sessionId).disconnect();
			getServerChannelHandler().get(sessionId).close();
		}
		getServerChannelHandler().remove(sessionId);
	}

	synchronized boolean isNextMessageToNetwork(long serialId, long tunnelId, long messageId,
			MessageStatus messageStatus) {
		if (messageId - 1 == getBeaconNetworkTunnel().getLastAckSent(serialId)) {
			if (TRACE_LOG_IN_INFO)
				logger.info("################ for " + myRoleMode + " session " + serialId + " is next message "
						+ messageId + " [" + messageStatus + "]: TRUE, actual is "
						+ getBeaconNetworkTunnel().getLastAckSent(serialId));
			return true;
		} else {
			if (TRACE_LOG_IN_INFO)
				logger.info("################ for " + myRoleMode + " session " + serialId + " is next message "
						+ messageId + " [" + messageStatus + "]: FALSE, actual is "
						+ getBeaconNetworkTunnel().getLastAckSent(serialId));
			return false;
		}
	}

	@Override
	public void confirmPacketReceived(long sessionId, long ackMessageId, long lastReceivedAck) {
		getBeaconNetworkTunnel().incrementPacketControl();
		for (final Long serialPacket : getBeaconNetworkTunnel().getInputCachedMessages(sessionId).keySet()) {
			if (serialPacket <= ackMessageId) {
				final MessageCached lock = getBeaconNetworkTunnel().getInputCachedMessages(sessionId).get(serialPacket);
				if (TRACE_LOG_IN_INFO)
					logger.info("################ confirmed packet to Beacon and remove it " + serialPacket
							+ " beacause ack received is " + ackMessageId);
				lock.ackReceived();
				getBeaconNetworkTunnel().removeIputCachedMessage(sessionId, serialPacket);
				updateLastAckReceivedIfNeed(sessionId, ackMessageId);
			}
		}
		for (final Long idCachedMessage : getBeaconNetworkTunnel().getOutputCachedMessages(sessionId).keySet()) {
			if (idCachedMessage <= lastReceivedAck) {
				if (TRACE_LOG_IN_INFO)
					logger.info("################ remove packet to network " + idCachedMessage
							+ " beacause the last received on the other side is  " + lastReceivedAck);
				getBeaconNetworkTunnel().removeOutputCachedMessage(sessionId, idCachedMessage);
				getBeaconNetworkTunnel().getNetworkReceiver().getOutputCachedDataBase64ByMessageId()
						.remove(idCachedMessage);
			} else {
				if (getBeaconNetworkTunnel().getOutputCachedMessages(sessionId) != null
						&& getBeaconNetworkTunnel().getOutputCachedMessages(sessionId).containsKey(idCachedMessage))
					getBeaconNetworkTunnel().getOutputCachedMessages(sessionId).get(idCachedMessage)
							.isCompleteOrTryToSend();
			}
		}
	}

	@Override
	public void exceptionPacketReceived(long sessionId, long messageId) {
		getBeaconNetworkTunnel().incrementPacketControl();
		final MessageCached lock = getBeaconNetworkTunnel().getInputCachedMessages(sessionId).get(messageId);
		if (lock != null) {
			if (TRACE_LOG_IN_INFO)
				logger.info("################ exception packet {}", messageId);
			lock.resetReceived();
		} else {
			logger.warn("request resend messageId {} but the lock object is null", messageId);
		}
	}

	private synchronized void updateLastAckReceivedIfNeed(long sessionId, long messageUuid) {
		getBeaconNetworkTunnel().getOrCreateSessionById(sessionId).updateLastAckReceivedIfNeed(messageUuid);
	}

	private synchronized void updateLastAckSentIfNeed(long sessionId, long messageUuid) {
		getBeaconNetworkTunnel().getOrCreateSessionById(sessionId).updateLastAckSentIfNeed(messageUuid);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("BeaconNetworkReceiver [");
		if (myRoleMode != null)
			builder.append("myRoleMode=").append(myRoleMode).append(", ");
		builder.append("uniqueClassId=").append(uniqueClassId).append(", chunkLimit=").append(chunkLimit).append(", ");
		builder.append("terminate=").append(terminate);
		builder.append("]");

		return builder.toString();
	}

	void sendExceptionMessage(long serverSessionId, long targertId, long messageUuid, Exception clientEx) {
		try {
			final TunnelMessage tunnelMessage = TunnelMessage.newBuilder()
					.setMessageStatus(MessageStatus.exceptionCaught).setAgentSource(getBeaconNetworkTunnel().getMe())
					.setTunnelId(targertId).setSessionId(serverSessionId)
					.setMessageType(
							myRoleMode.equals(NetworkMode.CLIENT) ? MessageType.FROM_CLIENT : MessageType.FROM_SERVER)
					.setPayload(EdgeLogger.stackTraceToString(clientEx)).build();

			getBeaconNetworkTunnel().sendMessageToBeaconTunnel(tunnelMessage);
			getBeaconNetworkTunnel().incrementPacketControl();
			if (TRACE_LOG_IN_INFO)
				logger.info("################ exception message sent {}", messageUuid);

		} catch (final Exception a) {
			logger.warn("sending message exception {}", a.getMessage());
		}
	}

	void sendAckOrControlMessage(long sessionId, long tunnelId, long messageUuid, boolean control) {
		try {
			final TunnelMessage tunnelMessage = TunnelMessage.newBuilder()
					.setMessageStatus(control ? MessageStatus.beaconMessageControl : MessageStatus.beaconMessageAck)
					.setAgentSource(getBeaconNetworkTunnel().getMe()).setTunnelId(tunnelId).setSessionId(sessionId)
					.setMessageAckId(messageUuid)
					.setMessageAckReceivedId(getBeaconNetworkTunnel().getLastAckReceived(sessionId))
					.setMessageType(
							myRoleMode.equals(NetworkMode.CLIENT) ? MessageType.FROM_CLIENT : MessageType.FROM_SERVER)
					.build();
			getBeaconNetworkTunnel().sendMessageToBeaconTunnel(tunnelMessage);
			getBeaconNetworkTunnel().incrementPacketControl();
			updateLastAckSentIfNeed(sessionId, messageUuid);
			if (TRACE_LOG_IN_INFO) {
				logger.info("################ {} message sent {}", (control ? "control" : "ack"), messageUuid);
			}
		} catch (final Exception a) {
			logger.warn("sending message ack {}", a.getMessage());
		}
	}

	void updateNetworkActiviti() {
		lastNetworkActiviti.set(new Date().getTime());
	}

	Map<Long, SocketChannel> getServerChannelHandler() {
		return serverChannelHandler;
	}

	ChannelFuture getMainServerHandler() {
		return mainServerHandler;
	}

}
