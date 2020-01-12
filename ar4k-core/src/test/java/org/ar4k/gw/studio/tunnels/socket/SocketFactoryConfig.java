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
package org.ar4k.gw.studio.tunnels.socket;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Rappresentazione di singolo socket per connessione.
 *
 */
public class SocketFactoryConfig extends AbstractSocketFactoryConfig {

  private static final long serialVersionUID = -7113857848013750537L;

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

  public SocketFactoryComponent instantiate() {
    SocketFactoryComponent ss = new SocketFactoryComponent();
    ss.setConfiguration(this);
    return ss;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("soTimeout: " + soTimeout + "\n");
    sb.append("tcpNoDelay: " + tcpNoDelay + "\n");
    sb.append("keepAlive: " + keepAlive + "\n");
    sb.append("receiveBufferSize: " + receiveBufferSize + "\n");
    sb.append("reuseAddress: " + reuseAddress + "\n");
    return sb.toString();
  }

}
