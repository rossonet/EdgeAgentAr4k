package org.ar4k.agent.iot.serial.json.esp8266;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.net.HttpHeaders.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.rtsp.RtspHeaderNames.CONNECTION;
import static io.netty.handler.codec.rtsp.RtspHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.rtsp.RtspResponseStatuses.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.json.JSONObject;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;

public class Esp8266ListenServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(Esp8266ListenServerHandler.class.toString());
  private final TcpServerListener listener;

  public Esp8266ListenServerHandler(TcpServerListener listener) {
    this.listener = listener;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
    try {
      final byte[] bytes = new byte[msg.content().readableBytes()];
      msg.content().readBytes(bytes);
      if (msg.method().equals(HttpMethod.POST) || msg.method().equals(HttpMethod.PUT)
          || msg.method().equals(HttpMethod.DELETE)) {
        logger.debug("-- RECEIVED MESSAGE FROM DEVICE --");
        logger.debug(msg.toString());
        logger.debug("-- RECEIVED MESSAGE FROM DEVICE --");
        JSONObject s = listener.onMessage(ctx, msg);
        final byte[] js = s.toString().getBytes(UTF_8);
        DefaultFullHttpResponse resp = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(js));
        resp.headers().set(CONTENT_TYPE, APPLICATION_JSON_UTF8);
        resp.headers().set(CONTENT_LENGTH, js.length);
        resp.headers().set(CONNECTION, CLOSE);
        ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE_ON_FAILURE)
            .addListener(ChannelFutureListener.CLOSE);
      }
    } catch (Exception e) {
      logger.logException(e);
    }
  }
}