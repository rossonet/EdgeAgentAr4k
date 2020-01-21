package org.ar4k.agent.tunnels.http.beacon;

import java.net.InetSocketAddress;
import java.util.UUID;
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

  // per scrivere verso il beacon in async
  private class WriterObserver implements StreamObserver<TunnelMessage> {

    private void checkSession(TunnelMessage message) {
      if (session == 0 && message.getSessionId() != 0) {
        session = message.getSessionId();
        logger.info("new session " + myRoleMode + " " + tunnelId + "/" + uniqueBeaconNetwork + " " + session);
      } else {
        if (message.getSessionId() != session) {
          throw new BeaconTunnelException("error in session " + myRoleMode + " " + tunnelId + "/" + uniqueBeaconNetwork
              + " " + session + "!=" + message.getSessionId());
        }
      }

    }

    @SuppressWarnings("unused")
    private ChannelHandlerContext ctx;
    private SocketChannel socketChannel;
    private ChannelFuture clientHandler;
    private Bootstrap clientBootstrap;
    private long session = 0;

    public WriterObserver(ChannelHandlerContext ctx, SocketChannel socketChannel) {
      this.ctx = ctx;
      this.socketChannel = socketChannel;
      logger.info("WriteObserver of server created");
    }

    public WriterObserver(Bootstrap clientBootstrap) {
      this.clientBootstrap = clientBootstrap;
      logger.info("WriteObserver of client created");
    }

    @Override
    public void onNext(TunnelMessage value) {
      packetReceived.incrementAndGet();
      checkSession(value);
      // crea la connessione cient se non esiste
      if (clientHandler == null && myRoleMode.equals(NetworkMode.CLIENT)) {
        logger.debug("creating clientHandler for client");
        try {
          clientHandler = clientBootstrap.connect().sync();
        } catch (Exception e) {
          logger.logException("during connection ", e);
        }
        if (clientHandler.isSuccess())
          logger.info("Client Netty to " + config.getClientIp() + ":" + config.getClientPort() + " with id_target "
              + tunnelId + "/" + uniqueBeaconNetwork + " started successfully");
        else {
          throw new BeaconTunnelException("Client Netty to " + config.getClientIp() + ":" + config.getClientPort()
              + " with id_target " + tunnelId + "/" + uniqueBeaconNetwork + " error");
        }
      }
      // la connessione server dovrebbe essere aperta
      if (socketChannel == null && myRoleMode.equals(NetworkMode.SERVER)) {
        throw new BeaconTunnelException("error in session " + myRoleMode + " " + tunnelId + "/" + uniqueBeaconNetwork
            + " " + session + " socketChannel for server is null");
      }
      // scrive sull handler
      if (value.getPayload() != null && !value.getPayload().isEmpty()) {
        // in caso di client
        if (myRoleMode.equals(NetworkMode.CLIENT)) {
          if (clientHandler != null) {
            clientHandler.channel().writeAndFlush(Unpooled.wrappedBuffer(value.getPayload().toByteArray()));
            logger.trace("Message from " + myRoleMode + " " + tunnelId + "/" + uniqueBeaconNetwork + " sent "
                + value.getPayload().toStringUtf8());
          } else {
            throw new BeaconTunnelException(
                "Client handler for " + myRoleMode + " " + tunnelId + "/" + uniqueBeaconNetwork + " is null");
          }
        } else {
          // in caso di server
          if (socketChannel != null) {
            socketChannel.writeAndFlush(Unpooled.wrappedBuffer(value.getPayload().toByteArray()));
            logger.trace("Message from " + myRoleMode + " " + tunnelId + "/" + uniqueBeaconNetwork + " sent "
                + value.getPayload().toStringUtf8());
          } else {
            throw new BeaconTunnelException(
                "Server handler for " + myRoleMode + " " + tunnelId + "/" + uniqueBeaconNetwork + " is null");
          }
        }
      } else {
        logger.debug("Message from " + myRoleMode + " " + tunnelId + "/" + uniqueBeaconNetwork + " is empty");
      }
    }

    @Override
    public void onError(Throwable t) {
      logger.logException("Error for tunnel write " + tunnelId + "/" + uniqueBeaconNetwork + " session:" + session, t);
    }

    @Override
    public void onCompleted() {
      logger.info("Complete for tunnel write " + tunnelId + "/" + uniqueBeaconNetwork + " session:" + session);
    }
  }

// handler lato client, uno per sessione
  private final class ClientTCPChannelHandler extends SimpleChannelInboundHandler<ByteBuf> implements AutoCloseable {
    private final SocketChannel socketChannel;
    private StreamObserver<TunnelMessage> writeObserver = null;

    public ClientTCPChannelHandler(SocketChannel socketChannel, StreamObserver<TunnelMessage> writeObserver) {
      this.socketChannel = socketChannel;
      this.writeObserver = writeObserver;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf s) throws Exception {
      packetSend.incrementAndGet();
      byte[] resultByte = new byte[s.readableBytes()];
      s.readBytes(resultByte);
      logger.trace("Read from client " + tunnelId + "/" + uniqueBeaconNetwork + " value " + new String(resultByte));
      TunnelMessage tunnelMessage = TunnelMessage.newBuilder().setPayload(ByteString.copyFrom(resultByte)).setAgent(me)
          .setTargeId(tunnelId).setUniqueId(getPrefixRoleString() + uniqueBeaconNetwork).build();
      writeObserver.onNext(tunnelMessage);
      logger.trace("client " + tunnelId + "/" + uniqueBeaconNetwork + " -> " + new String(resultByte));
    }

    @Override
    public void close() throws Exception {
      if (socketChannel != null) {
        socketChannel.close();
      }
      if (writeObserver != null) {
        writeObserver.onCompleted();
        writeObserver = null;
      }
    }
  }

//handler lato server, uno per sessione
  private final class ServerTCPChannelHandler extends SimpleChannelInboundHandler<ByteBuf> implements AutoCloseable {
    private final long sessionId = UUID.randomUUID().getMostSignificantBits();
    private StreamObserver<TunnelMessage> responseObserverReceiver = null;
    private final SocketChannel socketChannel;
    private WriterObserver writeObserver = null;

    public ServerTCPChannelHandler(SocketChannel socketChannel) {
      this.socketChannel = socketChannel;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
      createResponseObserver(ctx);
      logger.info("Channel server with " + ctx.channel().remoteAddress() + "\n" + tunnelId + "/" + uniqueBeaconNetwork
          + " active");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, final ByteBuf s) throws Exception {
      packetSend.incrementAndGet();
      createResponseObserver(ctx);
      byte[] resultByte = new byte[s.readableBytes()];
      s.readBytes(resultByte);
      logger.trace("Channel server with " + ctx.channel().remoteAddress() + "\n" + tunnelId + "/" + uniqueBeaconNetwork
          + " value " + new String(resultByte));
      TunnelMessage tunnelMessage = TunnelMessage.newBuilder().setPayload(ByteString.copyFrom(resultByte)).setAgent(me)
          .setTargeId(tunnelId).setSessionId(sessionId).setUniqueId(getPrefixRoleString() + uniqueBeaconNetwork)
          .build();
      responseObserverReceiver.onNext(tunnelMessage);
      logger.trace("server " + tunnelId + "/" + uniqueBeaconNetwork + " -> " + new String(resultByte));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
      createResponseObserver(ctx);
      logger.info("Channel server with " + ctx.channel().remoteAddress() + "\n" + tunnelId + "/" + uniqueBeaconNetwork
          + " inactive");
    }

    private void createResponseObserver(ChannelHandlerContext ctx) {
      if (responseObserverReceiver == null) {
        logger.info("open Beacon channel " + ctx);
        writeObserver = new WriterObserver(ctx, socketChannel);
        responseObserverReceiver = asyncStubTunnel.openNetworkChannel(writeObserver);
      }
    }

    @Override
    public void close() throws Exception {
      if (socketChannel != null) {
        socketChannel.close();
      }
      if (writeObserver != null) {
        writeObserver.onCompleted();
        writeObserver = null;
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
      ServerTcpHandler childHandler = new ServerTcpHandler();
      ServerBootstrap b = new ServerBootstrap().group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
          .childHandler(childHandler).childOption(ChannelOption.SO_KEEPALIVE, true);
      ChannelFuture clientHandler = b.bind(config.getServerPort()).sync();
      if (clientHandler.isSuccess()) {
        logger.info("Server Netty on port " + config.getServerPort() + " with id_target " + tunnelId + "/"
            + uniqueBeaconNetwork + " started successfully");
      } else {
        throw new BeaconTunnelException("Server Netty on port " + config.getServerPort() + " with id_target " + tunnelId
            + "/" + uniqueBeaconNetwork + " error");
      }
      // f.channel().closeFuture().sync();
      // startBeaconServerTunnel(childHandler);
    } catch (Exception a) {
      logger.logException("Fault creating server socket", a);
    }
  }

  // server inizializzatore, per ogni nuova sessione crea un
  // ServerTCPChannelHandler
  private final class ServerTcpHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
      ServerTCPChannelHandler serverTCPChannelHandler = new ServerTCPChannelHandler(socketChannel);
      socketChannel.pipeline().addLast(serverTCPChannelHandler);
    }
  }

//client inizializzatore, per ogni nuova sessione crea un
  // ClientTCPChannelHandler
  private void createClientSocket() {
    myRoleMode = NetworkConfig.NetworkMode.CLIENT;
    logger.debug("Starting client to: " + config.getClientIp() + ":" + config.getClientPort());
    workerGroup = new NioEventLoopGroup();
    Bootstrap clientBootstrap = new Bootstrap();
    StreamObserver<TunnelMessage> responseObserver = startBeaconClientTunnel(new WriterObserver(clientBootstrap));
    try {
      ChannelInitializer<SocketChannel> handler = new ChannelInitializer<SocketChannel>() {
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
          ClientTCPChannelHandler clientTCPChannelHandler = new ClientTCPChannelHandler(socketChannel,
              responseObserver);
          socketChannel.pipeline().addLast(clientTCPChannelHandler);
          // attiva la connessione verso il beacon. Il client connettera verso il server
          // TCP esterno al primo pacchetto.

        }
      };
      clientBootstrap.group(workerGroup);
      clientBootstrap.channel(NioSocketChannel.class);
      clientBootstrap.remoteAddress(new InetSocketAddress(config.getClientIp(), config.getClientPort()));
      clientBootstrap.handler(handler);
    } catch (Exception a) {
      logger.logException("Fault creating server socket", a);
    }

  }

  // avvia la connessione verso Beacon
  private StreamObserver<TunnelMessage> startBeaconClientTunnel(StreamObserver<TunnelMessage> writeObserver) {
    TunnelMessage tunnelMessage = TunnelMessage.newBuilder().setAgent(me).setTargeId(tunnelId)
        .setUniqueId(getPrefixRoleString() + uniqueBeaconNetwork).build();
    StreamObserver<TunnelMessage> responseObserver = asyncStubTunnel.openNetworkChannel(writeObserver);
    logger
        .info("Channel " + tunnelId + "/" + uniqueBeaconNetwork + " set client response observer " + responseObserver);
    responseObserver.onNext(tunnelMessage);
    return responseObserver;
  }

}
