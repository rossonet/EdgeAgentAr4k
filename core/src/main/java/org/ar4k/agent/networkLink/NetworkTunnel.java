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
package org.ar4k.agent.networkLink;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.AbstractTunnelComponent;
import org.json.JSONObject;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 * 
 *         Gestore servizio per connessioni socket.
 * 
 */
public class NetworkTunnel extends AbstractTunnelComponent {

  // iniettata vedi set/get
  private NetworkConfig configuration = null;
  private SocketChannel channel = null;

  private void connetti() {
    if (configuration.port != 0 && configuration.ipHost != null) {
      try {
        InetSocketAddress a = new InetSocketAddress(configuration.ipHost, configuration.port);
        channel = SocketChannel.open();
        channel.connect(a);
        socket = channel.socket();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public synchronized int checkConnession() {
    int ritorno = 0;
    if (socket == null || (!channel.isConnected()) || socket.isClosed()) {
      ritorno = 100;
      connetti();
    }
    return ritorno;
  }

  @Override
  public NetworkConfig getConfiguration() {
    return configuration;
  }

  @Override
  public void setConfiguration(ConfigSeed configuration) {
    super.setConfiguration(configuration);
    this.configuration = ((NetworkConfig) configuration);
  }

  @Override
  public void kill() {
    socket = null;
  }

  @Override
  protected void finalize() {
    socket = null;
  }

  @Override
  public void init() {
    checkConnession();
  }

  @Override
  public String getStatusString() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JSONObject getStatusJson() {
    // TODO Auto-generated method stub
    return null;
  }
}
