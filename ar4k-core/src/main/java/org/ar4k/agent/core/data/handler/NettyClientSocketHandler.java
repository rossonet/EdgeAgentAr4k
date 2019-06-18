package org.ar4k.agent.core.data.handler;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.data.channels.IDirectChannel;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import io.grpc.netty.shaded.io.netty.buffer.ByteBuf;
import io.grpc.netty.shaded.io.netty.channel.ChannelHandlerContext;
import io.grpc.netty.shaded.io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyClientSocketHandler extends ChannelInboundHandlerAdapter {

  private ChannelHandlerContext ctx = null;
  private IDirectChannel springChannel = null;

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(Anima.class.toString());

  public void sendMessage(Message<?> message) {
    if (ctx != null) {
      ctx.writeAndFlush(message.getPayload());
    } else {
      logger.info("Try to send message via Netty before the context is ok");
    }
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    super.channelActive(ctx);
    this.ctx = ctx;
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
    super.exceptionCaught(ctx, e);
    logger.info("Netty error. Context is: " + ctx.toString());
    logger.logException((Exception) e);
  }

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    super.handlerAdded(ctx);
    logger.info("Netty context " + ctx.toString() + " added");
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    super.handlerRemoved(ctx);
    logger.info("Netty context " + ctx.toString() + " removed");
    ctx.close();
    ctx = null;
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    ByteBuf m = (ByteBuf) msg;
    try {
      final Message<Object> msgToSend = MessageBuilder.withPayload(msg)
          .setHeader(Anima.NETTY_CTX_CLIENT, this.hashCode()).build();
      springChannel.send(msgToSend);
    } finally {
      m.release();
    }
  }

}
