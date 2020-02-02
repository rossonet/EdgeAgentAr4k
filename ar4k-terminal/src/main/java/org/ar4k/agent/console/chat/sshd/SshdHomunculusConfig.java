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
package org.ar4k.agent.console.chat.sshd;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.Ar4kComponent;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione servizio connessione IRC.
 *
 */
public class SshdHomunculusConfig extends AbstractServiceConfig {

  @Parameter(names = "--port", description = "the port for the SSHD service", required = true)
  public int port = 6661;
  @Parameter(names = "--bindHost", description = "bind which interface. All = 0.0.0.0", required = true)
  public String broadcastAddress = "0.0.0.0";

  private static final long serialVersionUID = 6301077946480730173L;

  @Override
  public Ar4kComponent instantiate() {
    SshdHomunculusService ss = new SshdHomunculusService();
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
}
