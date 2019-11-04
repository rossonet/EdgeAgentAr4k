package org.ar4k.agent.iot.serial.json.esp8266;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class Esp8266ListenServerInitializer extends ChannelInitializer<SocketChannel> {

  private TcpServerListener tcpServerListener = null;

  public Esp8266ListenServerInitializer(TcpServerListener tcpServerListener) {
    this.tcpServerListener = tcpServerListener;
  }

  @Override
  public void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    pipeline.addLast("http", new HttpServerCodec());
    pipeline.addLast("dechunker", new HttpObjectAggregator(65536));
    pipeline.addLast("handler", new Esp8266ListenServerHandler(tcpServerListener));
  }
}
