package org.ar4k.agent.tunnels.socket.proxy;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

public class ProxySocketFactory extends SocketFactory {

  public ProxySocketFactory(SocketFactoryProxyConfig configuration) {
    config = configuration;
  }

  private SocketFactoryProxyConfig config = null;

  private Socket setSocketOptions(Socket s) throws SocketException {
    if (config.keepAlive != null) {
      s.setKeepAlive(config.keepAlive);
    }
    if (config.tcpNoDelay != null) {
      s.setTcpNoDelay(config.tcpNoDelay);
    }

    if (config.soTimeout != null) {
      s.setSoTimeout(config.soTimeout);
    }
    if (config.receiveBufferSize != null) {
      s.setReceiveBufferSize(config.receiveBufferSize);
    }
    if (config.reuseAddress != null) {
      s.setReuseAddress(config.reuseAddress);
    }
    return s;
  }

  private Socket getProxySocket() throws IOException {
    Proxy proxy = null;
    Socket proxySocket = null;
    if (config.protocol == "HTTP") {
      proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config.ipHost, config.port));
    } else if (config.protocol == "SOCKS") {
      proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(config.ipHost, config.port));
    }
    if (config.protocol == "DIRECT") {
      proxy = new Proxy(Proxy.Type.DIRECT, null);
    }
    proxySocket = new Socket(proxy);
    // proxySocket.connect(destination);
    return setSocketOptions(proxySocket);
  }

  public Socket createSocket() throws IOException {
    return getProxySocket();
  }

  public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
    Socket proxy = getProxySocket();
    proxy.connect(new InetSocketAddress(host, port));
    return proxy;
  }

  public Socket createSocket(InetAddress address, int port) throws IOException {
    Socket proxy = getProxySocket();
    proxy.connect(new InetSocketAddress(address, port));
    return proxy;
  }

  public Socket createSocket(String host, int port, InetAddress clientAddress, int clientPort)
      throws IOException, UnknownHostException {
    Socket proxy = getProxySocket();
    proxy.connect(new InetSocketAddress(host, port));
    return proxy;
  }

  public Socket createSocket(InetAddress address, int port, InetAddress clientAddress, int clientPort)
      throws IOException {
    Socket proxy = getProxySocket();
    proxy.connect(new InetSocketAddress(address, port));
    return proxy;
  }

}
