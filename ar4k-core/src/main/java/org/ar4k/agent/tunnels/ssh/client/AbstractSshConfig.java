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
package org.ar4k.agent.tunnels.ssh.client;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.tunnels.socket.ISocketFactoryComponent;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione servizio tunnel SSH
 */
public abstract class AbstractSshConfig extends AbstractServiceConfig {

  private static final long serialVersionUID = -5164761698374285171L;

  @Parameter(names = "--socketFactory", description = "the socket factory for the ssh connection -can includes proxy-")
  public ISocketFactoryComponent socketFactory = null;

  @Parameter(names = "--host", description = "host for the connection")
  public String host = null;

  @Parameter(names = "--port", description = "port for the connection")
  public int port = 0;

  @Parameter(names = "--username", description = "username for the connection")
  public String username = null;

  @Parameter(names = "--password", description = "password for the connection")
  public String password = null;

  @Parameter(names = "--authkey", description = "private auth key for the connection")
  public String authkey = null;

}
