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

import org.ar4k.agent.config.tunnel.TunnelConfig;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 * 
 *         Rappresentazione di singolo socket per connessione.
 *
 */
public class NetworkConfig extends TunnelConfig {

  private static final long serialVersionUID = -7113857848013750537L;

  @Parameter(names = "--ipHost", description = "the host address to connect to")
  public String ipHost = null;

  @Parameter(names = "--port", description = "the port to connect to")
  public int port = 0;

  public NetworkTunnel instanziate() {
    NetworkTunnel ss = new NetworkTunnel();
    return ss;
  }

}
