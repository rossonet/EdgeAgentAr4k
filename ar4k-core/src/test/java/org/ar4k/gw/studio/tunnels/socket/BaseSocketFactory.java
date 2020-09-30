package org.ar4k.gw.studio.tunnels.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

public class BaseSocketFactory extends SocketFactory {

  public BaseSocketFactory(SocketFactoryConfig configuration) {
    config = configuration;
  }

  private SocketFactoryConfig config = null;

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

  public Socket createSocket() throws SocketException {
    return setSocketOptions(new Socket());
  }

  public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
    return setSocketOptions(new Socket(host, port));
  }

  public Socket createSocket(InetAddress address, int port) throws IOException {
    return setSocketOptions(new Socket(address, port));
  }

  public Socket createSocket(String host, int port, InetAddress clientAddress, int clientPort)
      throws IOException, UnknownHostException {
    return setSocketOptions(new Socket(host, port, clientAddress, clientPort));
  }

  public Socket createSocket(InetAddress address, int port, InetAddress clientAddress, int clientPort)
      throws IOException {
    return setSocketOptions(new Socket(address, port, clientAddress, clientPort));
  }

}
