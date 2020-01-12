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
package org.ar4k.agent.xmpp;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.Ar4kComponent;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione servizio connessione XMPP.
 *
 */
public class JabberHomunculusConfig extends AbstractServiceConfig {

  private static final long serialVersionUID = -4642788033911032726L;

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

  @Override
  public Ar4kComponent instantiate() {
    // System.out.println("Serial service start");
    JabberHomunculusService ss = new JabberHomunculusService();
    return ss;
  }

  @Override
  public int getPriority() {
    return 6;
  }

  @Override
  public boolean isSpringBean() {
    return false;
  }
}
