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
package org.ar4k.agent.tunnels.socket.proxy;

import org.ar4k.agent.tunnels.socket.AbstractSocketFactoryComponent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio di connessione proxy.
 *
 */
public class SocketFactoryProxy extends AbstractSocketFactoryComponent {
  SocketFactoryProxyConfig configuration = null;

  @Override
  public void init() {
    socketFactory = new ProxySocketFactory(configuration);
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
  public JsonElement getStatusJson() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJsonTree(configuration);
  }

  public SocketFactoryProxyConfig getConfiguration() {
    return configuration;
  }

  public void setConfiguration(SocketFactoryProxyConfig configuration) {
    this.configuration = configuration;
  }

}
