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
package org.ar4k.gw.studio.tunnels.socket.ssl;

import org.ar4k.gw.studio.tunnels.socket.AbstractSocketFactoryComponent;
import org.ar4k.gw.studio.tunnels.socket.AbstractSocketFactoryConfig;
import org.json.JSONObject;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio tunnel SSL.
 */
public class SocketFactorySsl extends AbstractSocketFactoryComponent {

  // iniettata vedi set/get
  private SocketFactorySslConfig configuration = null;

  @Override
  public void init() {
    socketFactory = new SslSocketFactory(configuration);
  }

  @Override
  public void kill() {
    socketFactory = null;
  }

  @Override
  public String getStatusString() {
    return configuration.toString();
  }

  @Override
  public JSONObject getStatusJson() {
    JSONObject end = new JSONObject();
    end.put("status", getStatusString());
    return end;
  }

  @Override
  public AbstractSocketFactoryConfig getConfiguration() {
    return configuration;
  }

  public void setConfiguration(SocketFactorySslConfig configuration) {
    this.configuration = configuration;
  }
}
