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

import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.config.json.SshRemoteConfigJsonAdapter;

import com.beust.jcommander.Parameter;
import com.google.gson.TypeAdapter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione servizio tunnel SSH
 */
public class SshRemoteConfig extends AbstractSshConfig {

  private static final long serialVersionUID = 6322932417278452420L;

  @Parameter(names = "--redirectServer", description = "server to forward the connection")
  public String redirectServer = null;

  @Parameter(names = "--redirectPort", description = "port to forward the connection")
  public int redirectPort = 2200;

  @Parameter(names = "--bindHost", description = "If bindHost is an empty string or \"*\", the port should be available from all interfaces. If bind_address is \"localhost\" or null, the listening port will be bound for local use only.")
  public String bindHost = null;

  @Parameter(names = "--bindPort", description = "local port to bind for the connection")
  public int bindPort = 2200;

  public SshLocalTunnel instantiate() {
    SshLocalTunnel ss = new SshLocalTunnel();
    return ss;
  }

  @Override
  public TypeAdapter<? extends ConfigSeed> getJsonTypeAdapter() {
    return new SshRemoteConfigJsonAdapter();
  }

  @Override
  public boolean isSpringBean() {
    return false;
  }

}
