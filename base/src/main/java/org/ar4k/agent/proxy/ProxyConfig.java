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
package org.ar4k.agent.proxy;

import org.ar4k.agent.config.tunnel.TunnelConfig;
import org.ar4k.agent.config.validator.ProxyValidator;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione connessione a proxy socks o http
 */
public class ProxyConfig extends TunnelConfig {

  private static final long serialVersionUID = -3747867877505874335L;

  @Parameter(names = "--ipHost", description = "the host address of the proxy")
  public String ipHost = null;

  @Parameter(names = "--port", description = "the port of the proxy")
  public int port = 0;

  @Parameter(names = "--protocol", description = "the protocol for the connection to the proxy", validateWith = ProxyValidator.class)
  public String protocol = "HTTP";

  @Parameter(names = "--username", description = "the username for the connection to the proxy")
  public String username = null;

  @Parameter(names = "--password", description = "the password for the connection to the proxy")
  public String password = null;

  public ProxyTunnel instanziate() {
    // System.out.println("Serial service start");
    ProxyTunnel ss = new ProxyTunnel();
    return ss;
  }
}
