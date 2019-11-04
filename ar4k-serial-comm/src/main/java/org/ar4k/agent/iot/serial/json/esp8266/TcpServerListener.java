package org.ar4k.agent.iot.serial.json.esp8266;

import org.json.JSONObject;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface TcpServerListener {

  JSONObject onMessage(ChannelHandlerContext ctx, FullHttpRequest message);

}
