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
package org.ar4k.agent.tunnels.socket;

import org.json.JSONObject;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Gestore servizio per connessioni socket.
 *
 */
public class SocketFactoryComponent extends AbstractSocketFactoryComponent {

  SocketFactoryConfig configuration = null;

  @Override
  public void init() {
    socketFactory = new BaseSocketFactory(configuration);
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
  public SocketFactoryConfig getConfiguration() {
    return configuration;
  }

  public void setConfiguration(SocketFactoryConfig configuration) {
    this.configuration = configuration;
  }
}
