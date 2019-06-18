package org.ar4k.agent.core.data.handler;

import org.ar4k.agent.core.data.channels.IDirectChannel;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;

import io.grpc.netty.shaded.io.netty.channel.ChannelHandlerContext;
import io.grpc.netty.shaded.io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerSocketHandler extends ChannelInboundHandlerAdapter {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(NettyServerSocketHandler.class.toString());

  private ChannelHandlerContext ctx = null;
  private IDirectChannel springChannel = null;

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    super.channelActive(ctx);
    this.ctx = ctx;
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
    super.exceptionCaught(ctx, e);
    logger.info("Netty server error. Context is: " + ctx.toString());
    logger.logException((Exception) e);
  }

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    super.handlerAdded(ctx);
    logger.info("Netty server context " + ctx.toString() + " added");
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    super.handlerRemoved(ctx);
    logger.info("Netty server context " + ctx.toString() + " removed");
    ctx.close();
    ctx=null;
  }

}
