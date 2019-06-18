package org.ar4k.agent.core.data.handler;

import java.io.Closeable;
import java.io.IOException;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import io.grpc.netty.shaded.io.netty.bootstrap.ServerBootstrap;
import io.grpc.netty.shaded.io.netty.channel.ChannelFuture;
import io.grpc.netty.shaded.io.netty.channel.ChannelInitializer;
import io.grpc.netty.shaded.io.netty.channel.ChannelOption;
import io.grpc.netty.shaded.io.netty.channel.EventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.nio.NioEventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.socket.SocketChannel;
import io.grpc.netty.shaded.io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServerMessageEndpoint implements MessageHandler, Closeable {

  private int serverPort = 10654;
  private String serverHost = "0.0.0.0";
  private int poolSize = 10;
  private int bossPoolSize = 10;
  private EventLoopGroup workerGroup = null;
  private EventLoopGroup bossGroup = null;
  private ServerBootstrap b = new ServerBootstrap();

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(Anima.class.toString());

  @Override
  public void close() throws IOException {
    // TODO Auto-generated method stub

  }

  private void startServer() {
    try {
      workerGroup = new NioEventLoopGroup(poolSize);
      bossGroup = new NioEventLoopGroup(bossPoolSize);
      b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
          .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
              ch.pipeline().addLast(new NettyServerSocketHandler());
            }
          }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
      // Bind and start to accept incoming connections.
      ChannelFuture f = b.bind(serverPort).sync();
      // f.channel().closeFuture().sync()
    } catch (Exception e) {
      logger.logException(e);
    }
  }

  @Override
  public void handleMessage(Message<?> message) throws MessagingException {

  }

  public int getServerPort() {
    return serverPort;
  }

  public void setServerPort(int serverPort) {
    this.serverPort = serverPort;
  }

  public String getServerHost() {
    return serverHost;
  }

  public void setServerHost(String serverHost) {
    this.serverHost = serverHost;
  }

  public int getPoolSize() {
    return poolSize;
  }

  public void setPoolSize(int poolSize) {
    this.poolSize = poolSize;
  }

  public int getBossPoolSize() {
    return bossPoolSize;
  }

  public void setBossPoolSize(int bossPoolSize) {
    this.bossPoolSize = bossPoolSize;
  }

}
