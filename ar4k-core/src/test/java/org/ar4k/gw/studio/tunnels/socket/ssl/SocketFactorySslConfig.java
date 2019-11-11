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

import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.config.validator.ProxyValidator;
import org.ar4k.gw.studio.tunnels.socket.AbstractSocketFactoryConfig;

import com.beust.jcommander.Parameter;
import com.google.gson.TypeAdapter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 * Configurzione tunnel SSL
 *
 */
public class SocketFactorySslConfig extends AbstractSocketFactoryConfig {

  private static final long serialVersionUID = 707686741192996924L;

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

  @Parameter(names = "--proxyHost", description = "the host address of the proxy", required = true)
  public String proxyHost = null;

  @Parameter(names = "--proxyPort", description = "the port of the proxy", required = true)
  public int proxyPort = 0;

  @Parameter(names = "--proxyProtocol", description = "the protocol for the connection to the proxy", validateWith = ProxyValidator.class, required = true)
  public String protocol = "HTTP";

  @Parameter(names = "--proxyUsername", description = "the username for the connection to the proxy", required = false)
  public String proxyUsername = null;

  @Parameter(names = "--proxyPassword", description = "the password for the connection to the proxy", required = false)
  public String proxyPassword = null;
  /*
   * @Parameter(names = "--keystoreAuth", description =
   * "keystore to authenticate to the server", validateWith =
   * KeystoreValidator.class) public String keystoreAuth = null;
   * 
   * @Parameter(names = "--keystoreTrust", description =
   * "keystore to checking the server cert", validateWith =
   * KeystoreValidator.class) public String keystoreTrust = null;
   */
  @Parameter(names = "--algorithms", description = "crypto algorithms to use")
  public String algorithms = "TLS";

  @Parameter(names = "--userAgent", description = "the user agent for the tunnel")
  public String userAgent = "Mozilla/5.0 (X11; U; Linux i686; it; rv:1.9.0.10) Gecko/2009042513 Ubuntu/8.04 (hardy) Firefox/3.0.10";

  public SocketFactorySsl instantiate() {
    SocketFactorySsl ss = new SocketFactorySsl();
    ss.setConfiguration(this);
    return ss;
  }

  @Override
  public TypeAdapter<? extends ConfigSeed> getJsonTypeAdapter() {
    // TODO Auto-generated method stub
    return null;
  }

}
