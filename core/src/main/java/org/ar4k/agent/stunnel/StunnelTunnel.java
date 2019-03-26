/**
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
package org.ar4k.agent.stunnel;

import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.keystore.KeystoreLoader;
import org.ar4k.agent.tunnel.AbstractTunnelComponent;
import org.json.JSONObject;

import com.google.gson.JsonElement;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio tunnel SSL.
 */
public class StunnelTunnel extends AbstractTunnelComponent {

  private SSLContext context = null;
  // TODO: implementare il controllo custom
  private TrustManagerFactory tmf = null;
  private KeyManagerFactory kmf = null;
  private SocketChannel channel = null;

  // iniettata vedi set/get
  private StunnelConfig configuration = null;

  // iniettata vedi set/get
  private Anima anima = null;

  public static enum Algorithms {
    SSL, SSLv2, SSLv3, TLS, TLSv1_1, TLSv1_2
  }

  private void connetti() throws NoSuchAlgorithmException {
    try {
      context = SSLContext.getInstance("TLS");
      kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      kmf.init(KeystoreLoader.getKeyStoreAfterLoad(configuration.keystoreAuth, anima.getKeyStores()),
          KeystoreLoader.getPasswordKeystore(configuration.keystoreAuth, anima.getKeyStores()).toCharArray());
      tmf.init(KeystoreLoader.getKeyStoreAfterLoad(configuration.keystoreAuth, anima.getKeyStores()));
      context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
      // SSLSocketFactory socketFactory = context.getSocketFactory();
      // socket =
      // socketFactory.createSocket(AbstractTunnelComponent.getSocks(configuration.connectionSock),
      // configuration.redirectServer, configuration.redirectPort, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private synchronized void check() {
    /*
     * if (context == null || channel == null || socket == null ||
     * (!channel.isConnected()) || socket.isClosed()) { try { connetti(); } catch
     * (NoSuchAlgorithmException e) { e.printStackTrace(); } }
     */
  }

  @Override
  public StunnelConfig getConfiguration() {
    return configuration;
  }

  @Override
  public void setConfiguration(ConfigSeed configuration) {
    super.setConfiguration(configuration);
    this.configuration = ((StunnelConfig) configuration);
  }

  @Override
  public void kill() {
    kmf = null;
    tmf = null;
    context = null;
  }

  @Override
  protected void finalize() {
    kmf = null;
    tmf = null;
    context = null;
  }

  @Override
  public void init() {
    check();
  }

  @Override
  public String getStatusString() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JsonElement getStatusJson() {
    // TODO Auto-generated method stub
    return null;
  }
}
