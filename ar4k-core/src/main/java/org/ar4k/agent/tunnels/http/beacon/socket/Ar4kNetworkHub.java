package org.ar4k.agent.tunnels.http.beacon.socket;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.ar4k.agent.exception.BeaconTunnelException;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.network.NetworkConfig;
import org.ar4k.agent.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.network.NetworkHub;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelServiceV1Grpc.TunnelServiceV1Stub;

import com.google.protobuf.ByteString;

import io.grpc.stub.StreamObserver;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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

public class Ar4kNetworkHub implements NetworkHub {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(Ar4kNetworkHub.class.toString());

  private long tunnelId = 0;
  private AtomicLong packetSend = new AtomicLong(0);
  private AtomicLong packetReceived = new AtomicLong(0);
  private TunnelServiceV1Stub asyncStubTunnel = null;
  private NetworkConfig config = null;
  private Agent me = null;
  private EventLoopGroup bossGroup = null;
  private EventLoopGroup workerGroup = null;
  private String uniqueBeaconNetwork = null;
  private NetworkMode myRoleMode = null;
  private BeaconObserver universalClientBeaconWriterObserver = null;
  private StreamObserver<TunnelMessage> universalClientBeaconReaderObserver = null;

  // per scrivere verso il beacon in async
  private class BeaconObserver implements StreamObserver<TunnelMessage> {

    @SuppressWarnings("unused")
    private final ChannelHandlerContext ctx;
    private final SocketChannel serverSocketChannel;
    // private ChannelFuture clientHandler;
    // private Bootstrap clientBootstrap;
    private final long serverSessionId;
    private final int remoteTargetPort;
    private final String remoteTargetHost;
    private final Map<Long, ChannelFuture> clientChannelHandler = new ConcurrentHashMap<>();

    // server chiama così
    public BeaconObserver(long serverSessionId, ChannelHandlerContext ctx, SocketChannel serverSocketChannel) {
      this.ctx = ctx;
      this.serverSessionId = serverSessionId;
      this.serverSocketChannel = serverSocketChannel;
      remoteTargetPort = 0;
      remoteTargetHost = null;
      logger.debug("WriteObserver of server created");
    }

    private ChannelFuture getClientHandler(long sessionId) {
      logger.trace("looking for client handler of session " + sessionId);
      if (clientChannelHandler.containsKey(sessionId))
        return clientChannelHandler.get(sessionId);
      else
        return null;
    }

    private void setClientHandler(long serialSessionId, ChannelFuture clientHandler) {
      clientChannelHandler.put(serialSessionId, clientHandler);
    }

    // client così
    public BeaconObserver() {
      this.remoteTargetHost = config.getClientIp();
      this.remoteTargetPort = config.getClientPort();
      serverSessionId = 0;
      this.serverSocketChannel = null;
      this.ctx = null;
      logger.debug("WriteObserver of client created");
    }

    // per client
    private void createSocketSessionClient(long serialSessionId) {
      // ChannelInitializer<SocketChannel> clientHandler;
      Bootstrap clientBootstrap = new Bootstrap();
      clientBootstrap.group(workerGroup);
      clientBootstrap.channel(NioSocketChannel.class);
      clientBootstrap.remoteAddress(new InetSocketAddress(remoteTargetHost, remoteTargetPort));
      clientBootstrap.handler(createClientInitializerHandlerForSession(serialSessionId));
      try {
        setClientHandler(serialSessionId, clientBootstrap.connect().sync());
      } catch (Exception e) {
        logger.logException("during connection ", e);
      }
      if (getClientHandler(serialSessionId).isSuccess())
        logger.info(
            "Client Netty session " + serialSessionId + " to " + config.getClientIp() + ":" + config.getClientPort()
                + " with id_target " + tunnelId + "/" + uniqueBeaconNetwork + " started successfully");
      else {
        throw new BeaconTunnelException("Client Netty to " + config.getClientIp() + ":" + config.getClientPort()
            + " with id_target " + tunnelId + "/" + uniqueBeaconNetwork + " error");
      }
    }

    // client handler
    ChannelInitializer<SocketChannel> createClientInitializerHandlerForSession(long serialId) {
      return new ChannelInitializer<SocketChannel>() {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
          ClientTCPChannelHandler clientTCPChannelHandler = new ClientTCPChannelHandler(socketChannel, serialId);
          socketChannel.pipeline().addLast(clientTCPChannelHandler);
          // attiva la connessione verso il beacon. Il client connettera verso il server
          // TCP esterno al primo pacchetto.
        }
      };
    }

    // per server
    private void checkSession(TunnelMessage message) {
      if (message.getSessionId() != serverSessionId) {
        throw new BeaconTunnelException("error in session " + myRoleMode + " " + tunnelId + "/" + uniqueBeaconNetwork
            + " " + serverSessionId + " != " + message.getSessionId());
      }
    }

    @Override
    public void onNext(TunnelMessage value) {
      packetReceived.incrementAndGet();
      // crea la connessione client se non esiste
      if (getClientHandler(value.getSessionId()) == null && myRoleMode.equals(NetworkMode.CLIENT)) {
        createSocketSessionClient(value.getSessionId());
      }
      // la connessione server dovrebbe essere aperta e con seriale
      if (serverSocketChannel == null && myRoleMode.equals(NetworkMode.SERVER)) {
        throw new BeaconTunnelException("error in session " + myRoleMode + " " + tunnelId + "/" + uniqueBeaconNetwork
            + " " + serverSessionId + " socketChannel for server is null");
      }
      // scrive sull handler
      if (value.getPayload() != null && !value.getPayload().isEmpty()) {
        // in caso di client
        if (myRoleMode.equals(NetworkMode.CLIENT)) {
          if (getClientHandler(value.getSessionId()) != null) {
            getClientHandler(value.getSessionId()).channel()
                .writeAndFlush(Unpooled.wrappedBuffer(value.getPayload().toByteArray()));
            logger.trace("Message from " + myRoleMode + " " + tunnelId + "/" + uniqueBeaconNetwork + " sent "
                + value.getPayload().toStringUtf8());
          } else {
            throw new BeaconTunnelException(
                "Client handler for " + myRoleMode + " " + tunnelId + "/" + uniqueBeaconNetwork + " is null");
          }
        } else {
          // in caso di server
          checkSession(value);
          if (serverSocketChannel != null) {
            serverSocketChannel.writeAndFlush(Unpooled.wrappedBuffer(value.getPayload().toByteArray()));
            logger.trace("Message from " + myRoleMode + " " + tunnelId + "/" + uniqueBeaconNetwork + " session:"
                + serverSessionId + " sent " + value.getPayload().toStringUtf8());
          } else {
            throw new BeaconTunnelException("Server handler for " + myRoleMode + " " + tunnelId + "/"
                + uniqueBeaconNetwork + " session:" + serverSessionId + " is null");
          }
        }
      } else {
        logger.debug("Message from " + myRoleMode + " " + tunnelId + "/" + uniqueBeaconNetwork + " session:"
            + serverSessionId + " is empty");
      }
    }

    @Override
    public void onError(Throwable t) {
      logger.logException(
          "Error for tunnel write " + tunnelId + "/" + uniqueBeaconNetwork + " session:" + serverSessionId, t);
    }

    @Override
    public void onCompleted() {
      logger.debug("Complete for tunnel write " + tunnelId + "/" + uniqueBeaconNetwork + " session:" + serverSessionId);
    }
  }

// handler lato client, uno per sessione
  private final class ClientTCPChannelHandler extends SimpleChannelInboundHandler<ByteBuf> implements AutoCloseable {
    private final SocketChannel socketChannel;
    private final long clientSerialId;

    public ClientTCPChannelHandler(SocketChannel socketChannel, long serverSessionId) {
      this.socketChannel = socketChannel;
      this.clientSerialId = serverSessionId; // serial id della sessione che arriva dal server
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf s) throws Exception {
      packetSend.incrementAndGet();
      byte[] resultByte = new byte[s.readableBytes()];
      s.readBytes(resultByte);
      logger.trace("Read from client " + tunnelId + "/" + uniqueBeaconNetwork + " value " + new String(resultByte));
      TunnelMessage tunnelMessage = TunnelMessage.newBuilder().setPayload(ByteString.copyFrom(resultByte)).setAgent(me)
          .setTargeId(tunnelId).setSessionId(clientSerialId).setUniqueId(getPrefixRoleString() + uniqueBeaconNetwork)
          .build();
      universalClientBeaconReaderObserver.onNext(tunnelMessage);
      logger.trace("client " + tunnelId + "/" + uniqueBeaconNetwork + " -> " + new String(resultByte));
    }

    @Override
    public void close() throws Exception {
      if (socketChannel != null) {
        socketChannel.close();
      }
    }
  }

//handler lato server, uno per sessione
  private final class ServerTCPHandler extends SimpleChannelInboundHandler<ByteBuf> implements AutoCloseable {
    private final long serverSessionId = UUID.randomUUID().getMostSignificantBits(); // battezza la sessione
    private StreamObserver<TunnelMessage> responseObserverReceiver = null;
    private BeaconObserver serverSocketBeaconObserver = null;
    private final SocketChannel serverSocketChannel;

    public ServerTCPHandler(SocketChannel serverSocketChannel) {
      this.serverSocketChannel = serverSocketChannel;
      logger.info(
          "Server socket with session " + serverSessionId + " to " + tunnelId + "/" + uniqueBeaconNetwork + " created");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
      logger.debug("Channel server with " + ctx.channel().remoteAddress() + "\n" + tunnelId + "/" + uniqueBeaconNetwork
          + " active");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, final ByteBuf s) throws Exception {
      createResponseObserver(ctx);
      packetSend.incrementAndGet();
      byte[] resultByte = new byte[s.readableBytes()];
      s.readBytes(resultByte);
      logger.debug("Channel server with " + ctx.channel().remoteAddress() + "\n" + tunnelId + "/" + uniqueBeaconNetwork
          + " value " + new String(resultByte));
      TunnelMessage tunnelMessage = TunnelMessage.newBuilder().setPayload(ByteString.copyFrom(resultByte)).setAgent(me)
          .setTargeId(tunnelId).setSessionId(serverSessionId).setUniqueId(getPrefixRoleString() + uniqueBeaconNetwork)
          .build();
      responseObserverReceiver.onNext(tunnelMessage);
      logger.debug("server " + tunnelId + "/" + uniqueBeaconNetwork + " -> " + new String(resultByte));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
      logger.debug("Channel server with " + ctx.channel().remoteAddress() + "\n" + tunnelId + "/" + uniqueBeaconNetwork
          + " inactive");
    }

    private synchronized void createResponseObserver(ChannelHandlerContext ctx) {
      if (responseObserverReceiver == null) {
        logger.info("open Beacon channel for server " + ctx + " with session " + serverSessionId);
        serverSocketBeaconObserver = new BeaconObserver(serverSessionId, ctx, serverSocketChannel);
        responseObserverReceiver = asyncStubTunnel.openNetworkChannel(serverSocketBeaconObserver);
      }
    }

    @Override
    public void close() throws Exception {
      if (serverSocketChannel != null) {
        serverSocketChannel.close();
      }
      if (serverSocketBeaconObserver != null) {
        serverSocketBeaconObserver.onCompleted();
        serverSocketBeaconObserver = null;
      }
      if (responseObserverReceiver != null) {
        responseObserverReceiver.onCompleted();
        responseObserverReceiver = null;
      }
    }

  }

  @Override
  public void setAgent(Agent me) {
    this.me = me;
  }

  @Override
  public void setUniqueNetworkLink(String uniqueBeaconNetwork) {
    this.uniqueBeaconNetwork = uniqueBeaconNetwork;
  }

  @Override
  public void setTunnelId(long tunnelId) {
    this.tunnelId = tunnelId;
  }

  @Override
  public void setConfig(NetworkConfig config) {
    this.config = config;

  }

  @Override
  public long getTunnelId() {
    return tunnelId;
  }

  @Override
  public NetworkStatus getStatus() {
    // TODO
    NetworkStatus status = NetworkStatus.INIT;
    return status;
  }

  @Override
  public void close() throws Exception {
    if (workerGroup != null)
      workerGroup.shutdownGracefully();
    if (bossGroup != null)
      bossGroup.shutdownGracefully();
    asyncStubTunnel = null;
    if (universalClientBeaconWriterObserver != null) {
      universalClientBeaconWriterObserver.onCompleted();
      universalClientBeaconWriterObserver = null;
    }
    if (universalClientBeaconReaderObserver != null) {
      universalClientBeaconReaderObserver.onCompleted();
      universalClientBeaconReaderObserver = null;
    }
  }

  @Override
  public void setAsyncStubTunnel(TunnelServiceV1Stub asyncStubTunnel) {
    this.asyncStubTunnel = asyncStubTunnel;
  }

  @Override
  public void runNetty(boolean owner) {
    if (owner) {
      if (config.getNetworkModeRequested().equals(NetworkConfig.NetworkMode.CLIENT)) {
        try {
          createServerSocket();
        } catch (InterruptedException e) {
          logger.logException(e);
        }
      } else {
        createClientSocket();
      }
    } else {
      if (config.getNetworkModeRequested().equals(NetworkConfig.NetworkMode.SERVER)) {
        try {
          createServerSocket();
        } catch (InterruptedException e) {
          logger.logException(e);
        }
      } else {
        createClientSocket();
      }
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

  private String getPrefixRoleString() {
    String prefixRole = "N_";
    if (myRoleMode != null && myRoleMode.equals(NetworkConfig.NetworkMode.SERVER)) {
      prefixRole = "S_";
    } else if (myRoleMode != null && myRoleMode.equals(NetworkConfig.NetworkMode.CLIENT)) {
      prefixRole = "C_";
    }
    return prefixRole;
  }

  private void createServerSocket() throws InterruptedException {
    myRoleMode = NetworkConfig.NetworkMode.SERVER;
    logger.debug("Starting server at: " + config.getServerPort());
    bossGroup = new NioEventLoopGroup();
    workerGroup = new NioEventLoopGroup();
    try {
      ServerTcpInitHandler serverInitHandler = new ServerTcpInitHandler();
      ServerBootstrap b = new ServerBootstrap().group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
          .childHandler(serverInitHandler).childOption(ChannelOption.SO_KEEPALIVE, true);
      ChannelFuture genericServerClientChannel = b.bind(config.getServerPort()).sync();
      if (genericServerClientChannel.isSuccess()) {
        logger.info("Server Netty on port " + config.getServerPort() + " with id_target " + tunnelId + "/"
            + uniqueBeaconNetwork + " started successfully");
      } else {
        throw new BeaconTunnelException("Server Netty on port " + config.getServerPort() + " with id_target " + tunnelId
            + "/" + uniqueBeaconNetwork + " error");
      }
    } catch (Exception a) {
      logger.logException("Fault creating server socket", a);
    }
  }

  // server inizializzatore, per ogni nuova sessione crea un
  // ServerTCPChannelHandler
  private final class ServerTcpInitHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel serverSocketChannel) throws Exception {
      ServerTCPHandler serverTCPSocketChannelHandler = new ServerTCPHandler(serverSocketChannel);
      serverSocketChannel.pipeline().addLast(serverTCPSocketChannelHandler);
    }
  }

//client inizializzatore, per ogni nuova sessione crea un
  // ClientTCPChannelHandler
  private void createClientSocket() {
    myRoleMode = NetworkConfig.NetworkMode.CLIENT;
    logger.info("Starting client to: " + config.getClientIp() + ":" + config.getClientPort());
    try {
      workerGroup = new NioEventLoopGroup();
      startBeaconClientTunnel(new BeaconObserver());
    } catch (Exception a) {
      logger.logException("Fault creating server socket", a);
    }

  }

  // avvia la connessione verso Beacon
  private void startBeaconClientTunnel(BeaconObserver writeObserver) {
    TunnelMessage tunnelMessage = TunnelMessage.newBuilder().setAgent(me).setTargeId(tunnelId)
        .setUniqueId(getPrefixRoleString() + uniqueBeaconNetwork).build();
    universalClientBeaconReaderObserver = asyncStubTunnel.openNetworkChannel(writeObserver);
    universalClientBeaconReaderObserver.onNext(tunnelMessage);
    logger.debug("Channel " + tunnelId + "/" + uniqueBeaconNetwork + " set client response observer "
        + universalClientBeaconReaderObserver);
  }

}
