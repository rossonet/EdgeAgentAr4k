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

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione servizio tunnel SSH
 */
public class SshRemoteConfig extends AbstractSshConfig {

  @Override
  public String toString() {
    return "SshRemoteConfig [redirectServer=" + redirectServer + ", redirectPort=" + redirectPort + ", bindHost="
        + bindHost + ", bindPort=" + bindPort + "]";
  }

  private static final long serialVersionUID = 6322932417278452420L;

  @Parameter(names = "--redirectServer", description = "server to forward the connection")
  public String redirectServer = null;

  @Parameter(names = "--redirectPort", description = "port to forward the connection")
  public int redirectPort = 2200;

  @Parameter(names = "--bindHost", description = "If bindHost is an empty string or \"*\", the port should be available from all interfaces. If bind_address is \"localhost\" or null, the listening port will be bound for local use only.")
  public String bindHost = null;

  @Parameter(names = "--bindPort", description = "local port to bind for the connection, should be 0 for auto discovery")
  public int bindPort = 0;

  @Override
  public SshRemoteTunnel instantiate() {
    SshRemoteTunnel ss = new SshRemoteTunnel();
    ss.setConfiguration(this);
    return ss;
  }

  @Override
  public boolean isSpringBean() {
    return false;
  }

}
