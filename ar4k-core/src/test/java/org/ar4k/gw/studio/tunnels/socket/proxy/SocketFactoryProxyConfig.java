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
package org.ar4k.gw.studio.tunnels.socket.proxy;

import org.ar4k.agent.config.validator.ProxyValidator;
import org.ar4k.gw.studio.tunnels.socket.AbstractSocketFactoryConfig;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione connessione a proxy socks o http
 */
public class SocketFactoryProxyConfig extends AbstractSocketFactoryConfig {

  private static final long serialVersionUID = -3747867877505874335L;

  @Parameter(names = "--soTimeout", description = "enable/disable SO_TIMEOUT with the specified timeout, in milliseconds")
  public Integer soTimeout = null;
  @Parameter(names = "--tcpNoDelay", description = "enable/disable TCP_NODELAY (disable/enable Nagle's algorithm)")
  public Boolean tcpNoDelay = null;
  @Parameter(names = "--keepAlive", description = "enable/disable SO_KEEPALIVE")
  public Boolean keepAlive = null;
  @Parameter(names = "--receiveBufferSize", description = "sets the SO_RCVBUF option to the specified value for this Socket")
  public Integer receiveBufferSize = null;
  @Parameter(names = "--reuseAddress", description = "enable/disable the SO_REUSEADDR socket option")
  public Boolean reuseAddress = null;

  @Parameter(names = "--ipHost", description = "the host address of the proxy", required = true)
  public String ipHost = null;

  @Parameter(names = "--port", description = "the port of the proxy", required = true)
  public int port = 0;

  @Parameter(names = "--protocol", description = "the protocol for the connection to the proxy", validateWith = ProxyValidator.class, required = true)
  public String protocol = "HTTP";

  @Parameter(names = "--username", description = "the username for the connection to the proxy", required = false)
  public String username = null;

  @Parameter(names = "--password", description = "the password for the connection to the proxy", required = false)
  public String password = null;

  public SocketFactoryProxy instantiate() {
    SocketFactoryProxy ss = new SocketFactoryProxy();
    ss.setConfiguration(this);
    return ss;
  }

}
