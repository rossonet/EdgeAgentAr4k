package org.ar4k.agent.tunnels.socket.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class SslSocketFactory extends SocketFactory {

  public static enum Algorithms {
    SSL, SSLv2, SSLv3, TLS, TLSv1_1, TLSv1_2
  }

  public SslSocketFactory(SocketFactorySslConfig configuration) {
    config = configuration;
  }

  private SocketFactorySslConfig config = null;

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

  private SSLSocketFactory initSsl() {
    SSLContext context = null;
    TrustManagerFactory tmf = null;
    KeyManagerFactory kmf = null;
    try {
      context = SSLContext.getInstance(config.algorithms);
      kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      /*
       * kmf.init( KeystoreLoader.getKeyStoreAfterLoad(config.keystoreAuth, ((Anima)
       * Anima.getApplicationContext().getBean("Anima")).getKeyStores()),
       * KeystoreLoader.getPasswordKeystore(config.keystoreAuth, ((Anima)
       * Anima.getApplicationContext().getBean("Anima")).getKeyStores()).toCharArray()
       * ); tmf.init(KeystoreLoader.getKeyStoreAfterLoad(config.keystoreAuth, ((Anima)
       * Anima.getApplicationContext().getBean("Anima")).getKeyStores()));
       */
      context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return context.getSocketFactory();
  }

  private Socket getSslSocket(String host, int port) throws IOException {
    Socket result = null;
    if (config.proxyHost == null || config.proxyHost.isEmpty() && config.proxyPort == 0) {
      result = initSsl().createSocket();
    } else {
      Socket tunnel = new Socket(config.proxyHost, config.proxyPort);
      doTunnelHandshake(tunnel, host, port);
      result = initSsl().createSocket(tunnel, host, port, true);
    }
    return setSocketOptions(result);
  }

  @Override
  public Socket createSocket() throws IOException {
    return getSslSocket(null, 0);
  }

  @Override
  public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
    return getSslSocket(host, port);
  }

  @Override
  public Socket createSocket(InetAddress address, int port) throws IOException {
    return getSslSocket(address.getHostName(), port);
  }

  @Override
  public Socket createSocket(String host, int port, InetAddress clientAddress, int clientPort)
      throws IOException, UnknownHostException {
    return getSslSocket(host, port);
  }

  @Override
  public Socket createSocket(InetAddress address, int port, InetAddress clientAddress, int clientPort)
      throws IOException {
    return getSslSocket(address.getHostName(), port);
  }

  /*
   * Tell our tunnel where we want to CONNECT, and look for the right reply. Throw
   * IOException if anything goes wrong.
   */
  private void doTunnelHandshake(Socket tunnel, String host, int port) throws IOException {
    OutputStream out = tunnel.getOutputStream();
    String msg = "CONNECT " + host + ":" + port + " HTTP/1.0\n" + "User-Agent: " + config.userAgent + "\r\n\r\n";
    byte b[];
    try {
      /*
       * We really do want ASCII7 -- the http protocol doesn't change with locale.
       */
      b = msg.getBytes("ASCII7");
    } catch (UnsupportedEncodingException ignored) {
      /*
       * If ASCII7 isn't there, something serious is wrong, but Paranoia Is Good (tm)
       */
      b = msg.getBytes();
    }
    out.write(b);
    out.flush();

    /*
     * We need to store the reply so we can create a detailed error message to the
     * user.
     */
    byte reply[] = new byte[200];
    int replyLen = 0;
    int newlinesSeen = 0;
    boolean headerDone = false; /* Done on first newline */

    InputStream in = tunnel.getInputStream();

    while (newlinesSeen < 2) {
      int i = in.read();
      if (i < 0) {
        throw new IOException("Unexpected EOF from proxy");
      }
      if (i == '\n') {
        headerDone = true;
        ++newlinesSeen;
      } else if (i != '\r') {
        newlinesSeen = 0;
        if (!headerDone && replyLen < reply.length) {
          reply[replyLen++] = (byte) i;
        }
      }
    }

    /*
     * Converting the byte array to a string is slightly wasteful in the case where
     * the connection was successful, but it's insignificant compared to the network
     * overhead.
     */
    String replyStr;
    try {
      replyStr = new String(reply, 0, replyLen, "ASCII7");
    } catch (UnsupportedEncodingException ignored) {
      replyStr = new String(reply, 0, replyLen);
    }

    /* We asked for HTTP/1.0, so we should get that back */
    if (!replyStr.startsWith("HTTP/1.0 200")) {
      throw new IOException("Unable to tunnel through " + config.proxyHost + ":" + config.proxyPort
          + ".  Proxy returns \"" + replyStr + "\"");
    }

    /* tunneling Handshake was successful! */
  }
}
