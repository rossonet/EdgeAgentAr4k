package org.ar4k.agent.core.data.handler;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import io.grpc.netty.shaded.io.netty.bootstrap.Bootstrap;
import io.grpc.netty.shaded.io.netty.channel.ChannelInitializer;
import io.grpc.netty.shaded.io.netty.channel.ChannelOption;
import io.grpc.netty.shaded.io.netty.channel.EventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.nio.NioEventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.socket.SocketChannel;
import io.grpc.netty.shaded.io.netty.channel.socket.nio.NioSocketChannel;
import io.grpc.netty.shaded.io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.grpc.netty.shaded.io.netty.handler.codec.bytes.ByteArrayEncoder;

public class NettyClientMessageEndpoint implements MessageHandler, Closeable {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(NettyClientMessageEndpoint.class.toString());

  private int srcPort = 10664;
  private int poolSize = 10;
  private EventLoopGroup workerGroup = null;
  private Bootstrap b = new Bootstrap();

  private Map<Integer, NettyClientSocketHandler> activeSocketHandler = new HashMap<>();

  @Override
  public void handleMessage(Message<?> message) throws MessagingException {
    NettyClientSocketHandler tctx = new NettyClientSocketHandler();
    if (!message.getHeaders().containsKey(Anima.NETTY_CTX_CLIENT)) {
      if (workerGroup == null) {
        workerGroup = new NioEventLoopGroup(poolSize);
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        activeSocketHandler.put((Integer.valueOf(tctx.hashCode())), tctx);
        b.handler(new ChannelInitializer<SocketChannel>() {
          @Override
          public void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new ByteArrayDecoder());
            ch.pipeline().addLast(new ByteArrayEncoder());
            ch.pipeline().addLast(tctx);
          }
        });
        try {
          b.connect(message.getHeaders().get("host").toString(),
              Integer.valueOf(message.getHeaders().get("port").toString()));
          // ChannelFuture f = b.connect(message.getHeaders().get("host").toString(),
          // Integer.valueOf(message.getHeaders().get("port").toString())).sync();
          // f.channel().closeFuture().sync();
        } catch (Exception e) {
          logger.logException(e);
        }
      }
      tctx.sendMessage(message);
    } else {
      ((NettyClientSocketHandler) message.getHeaders().get(Anima.NETTY_CTX_CLIENT)).sendMessage(message);
    }
  }

  public int getSrcPort() {
    return srcPort;
  }

  public void setSrcPort(int srcPort) {
    this.srcPort = srcPort;
  }

  public int getPoolSize() {
    return poolSize;
  }

  public void setPoolSize(int poolSize) {
    this.poolSize = poolSize;
  }

  @Override
  public void close() throws IOException {
    activeSocketHandler.clear();
    workerGroup.shutdownGracefully();
  }

}
