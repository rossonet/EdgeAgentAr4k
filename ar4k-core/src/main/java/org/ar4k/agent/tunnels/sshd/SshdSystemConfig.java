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
package org.ar4k.agent.tunnels.sshd;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.Ar4kComponent;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione servizio connessione sshd.
 *
 */
public class SshdSystemConfig extends AbstractServiceConfig {

  private static final long serialVersionUID = -1567771487925577804L;

  @Parameter(names = "--port", description = "the port for the SSHD service")
  public int port = 6661;
  @Parameter(names = "--bindHost", description = "bind which interface. All = 0.0.0.0")
  public String bindHost = "0.0.0.0";
  @Parameter(names = "--cmd", description = "cmd to run when start a new session")
  public String cmd = "/bin/bash -i -l";
  @Parameter(names = "--authorizedKeys", description = "authorized keys file for ssh")
  public String authorizedKeys = "~/.ssh/authorized_keys";

  @Override
  public Ar4kComponent instantiate() {
    SshdSystemService ss = new SshdSystemService();
    ss.setConfiguration(this);
    return ss;
  }

  @Override
  public int getPriority() {
    return 15;
  }

  @Override
  public boolean isSpringBean() {
    return false;
  }

  @Override
  public String toString() {
    return "SshdSystemConfig [port=" + port + ", bindHost=" + bindHost + ", cmd=" + cmd + "]";
  }
}
