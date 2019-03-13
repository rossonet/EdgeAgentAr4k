/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    */
package org.ar4k.gw.studio.stunnel;

import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SocketChannel;
import java.security.KeyStore;

import javax.annotation.PostConstruct;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

///////////////////////////////////////////////////////

////////////////////////////////////////////////////

public class Stunnel {

  boolean running = true;

  /** Stunnel Settings */
  private String stunnelCert;
  private String stunnnelKeyPass;

  private Thread connector;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() throws InterruptedException {
    StunnelConnection("ipa.ar4k.net", 443);
    connector.start();
    // while (running) {
    Thread.sleep(10000L);
    // }
  }

  @Rule
  public TestWatcher watcher = new TestWatcher() {
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }
  };

  @SuppressWarnings("unused")
  public void StunnelConnection(String server, int port) {
    String s = server;
    int i = port;
    connector = stunnelConnector;
  }

  /**
   * Set the certificate to use when connecting to stunnel
   * 
   * @param path - Path to the certificate
   */
  public void setStunnelCert(String path) {
    stunnelCert = path;
  }

  /**
   * Password to open the stunnel key
   * 
   * @param pass
   */
  public void setStunnelKey(String pass) {
    stunnnelKeyPass = pass;
  }

  /**
   * Connects to the server(via stunnel) in a new thread, so we can interrupt it
   * if we want to cancel the connection
   */

  private Thread stunnelConnector = new Thread(new Runnable() {

    @PostConstruct
    public void post() {
      System.out.println("Start thread");
    }

    @SuppressWarnings("unused")
    @Override
    public void run() {
      SSLContext context = null;
      KeyStore keyStore = null;
      TrustManagerFactory tmf = null;
      KeyStore keyStoreCA = null;
      KeyManagerFactory kmf = null;
      try {

        FileInputStream pkcs12in = new FileInputStream(new File(stunnelCert));

        context = SSLContext.getInstance("TLS");

        // Local client certificate and key and server certificate
        keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(pkcs12in, stunnnelKeyPass.toCharArray());

        // Build a TrustManager, that trusts only the server certificate
        keyStoreCA = KeyStore.getInstance("BKS");
        keyStoreCA.load(null, null);
        keyStoreCA.setCertificateEntry("Server", keyStore.getCertificate("Server"));
        tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(keyStoreCA);

        // Build a KeyManager for Client auth
        kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, null);
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
      } catch (Exception e) {
        e.printStackTrace();
        return;
      }

      SSLSocketFactory socketFactory = context.getSocketFactory();
      try {
        SocketChannel channel = SocketChannel.open();
        // channel.connect(new InetSocketAddress(server, port));
        /*
         * sock = socketFactory.createSocket(channel.socket(), server, port, true);
         * out_stream = sock.getOutputStream(); in_stream = sock.getInputStream();
         * connected = true; notifyHandlers(STATE.CONNECTED);
         */
      } catch (ClosedByInterruptException e) {
        e.printStackTrace();
        // Thread interrupted during connect.
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  });

}
