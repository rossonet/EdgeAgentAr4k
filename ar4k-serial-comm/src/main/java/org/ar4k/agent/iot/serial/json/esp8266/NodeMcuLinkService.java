package org.ar4k.agent.iot.serial.json.esp8266;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.AbstractAr4kService;
import org.ar4k.agent.core.data.Ar4kChannel;
import org.ar4k.agent.core.data.channels.INoDataChannel;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.messages.JSONMessage;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.json.JSONObject;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
public class NodeMcuLinkService extends AbstractAr4kService implements TcpServerListener {

  private static final String DEVICE_QUEUE_SEPARATOR = "-";

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(NodeMcuLinkService.class.toString());

  private EventLoopGroup bossGroup = null;
  private EventLoopGroup workerGroup = null;
  private int intBossGroup = 50;
  private int intWorkerGroup = 50;
  private ServerBootstrap serverBootstrap = null;
  private NodeMcuLinkConfig configuration = null;
  private DatagramSocket socketUdpDiscovery = null;

  private final Map<String, JSONObject> cacheDevicesMessages = new HashMap<>();

  @Override
  public void loop() {
    sendDiscoveryUdp();
    try {
      Thread.sleep(4500L);
    } catch (InterruptedException e) {
      logger.logException(e);
    }
  }

  private void startTcpServer() throws InterruptedException {
    bossGroup = new NioEventLoopGroup(intBossGroup);
    workerGroup = new NioEventLoopGroup(intWorkerGroup);
    serverBootstrap = new ServerBootstrap();
    serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
        .handler(new LoggingHandler(LogLevel.DEBUG)).childHandler(new Esp8266ListenServerInitializer(this));
    serverBootstrap.bind(configuration.serverPort).sync().channel().closeFuture();
  }

  @Override
  public void stop() {
    if (bossGroup != null) {
      bossGroup.shutdownGracefully();
    }
    if (workerGroup != null) {
      workerGroup.shutdownGracefully();
    }
    bossGroup = null;
    workerGroup = null;
    serverBootstrap = null;
    cacheDevicesMessages.clear();
  }

  @Override
  public void init() {
    try {
      startTcpServer();
    } catch (InterruptedException e) {
      logger.logException(e);
    }
  }

  @Override
  public void setConfiguration(ConfigSeed configuration) {
    this.configuration = (NodeMcuLinkConfig) configuration;
  }

  @Override
  public JSONObject getStatusJson() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void close() throws Exception {
    stop();
  }

  @Override
  public JSONObject onMessage(ChannelHandlerContext ctx, FullHttpRequest message) {
    String destination = configuration.dafaultChannel;
    JSONObject messageDecoded = new JSONObject();
    String deviceName = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress()
        + DEVICE_QUEUE_SEPARATOR;
    try {
      messageDecoded = new JSONObject(message.content().toString(StandardCharsets.UTF_8));
      if (configuration.optStringRoute != null && !configuration.optStringRoute.isEmpty()) {
        if (messageDecoded.optString(configuration.optStringRoute) != null) {
          if (configuration.insertDeviceInIdChannel) {
            destination = deviceName + DEVICE_QUEUE_SEPARATOR + messageDecoded.optString(configuration.optStringRoute);
          } else {
            destination = messageDecoded.optString(configuration.optStringRoute);
          }
        }
      }
    } catch (Exception a) {
      messageDecoded.put("status", false);
      messageDecoded.put("error-message", a.getMessage());
      messageDecoded.put("stack-trace", Ar4kLogger.stackTraceToString(a));
    }
    Ar4kChannel channelRoot = anima.getDataAddress().createOrGetDataChannel(configuration.fatherOfChannels,
        INoDataChannel.class, (String) null, null);
    IPublishSubscribeChannel channelTarget = (IPublishSubscribeChannel) anima.getDataAddress().createOrGetDataChannel(
        destination, IPublishSubscribeChannel.class, channelRoot, configuration.scopeOfChannels);
    JSONMessage internalMessage = new JSONMessage();
    internalMessage.setPayload(messageDecoded);
    channelTarget.send(internalMessage);
    return sendReplyInCacheToDevice(deviceName);
  }

  private JSONObject sendReplyInCacheToDevice(String device) {
    return cacheDevicesMessages.get(device);
  }

  private void sendDiscoveryUdp() {
    try {
      // Open a random port to send the package
      if (socketUdpDiscovery == null) {
        socketUdpDiscovery = new DatagramSocket();
        socketUdpDiscovery.setBroadcast(true);
      }
      byte[] sendData = String.valueOf(configuration.serverPort).getBytes();
      try {
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
            InetAddress.getByName(configuration.broadcastAddress), configuration.discoveryPort);
        socketUdpDiscovery.send(sendPacket);
        logger.debug(getClass().getName() + ">>> Request packet sent to: " + configuration.broadcastAddress);
      } catch (Exception e) {
        logger.logException(e);
        logger.warn("Error sending discovery udp " + e.getMessage());
      }
      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
      while (interfaces.hasMoreElements()) {
        NetworkInterface networkInterface = interfaces.nextElement();
        if (networkInterface.isLoopback() || !networkInterface.isUp()) {
          continue; // Don't want to broadcast to the loopback interface
        }
        for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
          InetAddress broadcast = interfaceAddress.getBroadcast();
          if (broadcast == null) {
            continue;
          }
          try {
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast,
                configuration.discoveryPort);
            socketUdpDiscovery.send(sendPacket);
          } catch (Exception e) {
            logger.logException(e);
            logger.warn("Error sending discovery udp on " + broadcast.getHostName() + " -> " + e.getMessage());
          }
          logger.debug(getClass().getName() + ">>> Request packet sent to: " + broadcast.getHostAddress()
              + "; Interface: " + networkInterface.getDisplayName());
        }
      }
    } catch (IOException ex) {
      logger.logException(ex);
      logger.warn("Exception in discovery flash " + ex.getMessage());
    }
  }

}
