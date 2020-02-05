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

import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio tunnel SSH.
 *
 */
public class SshLocalTunnel extends AbstractSshTunnel {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(SshLocalTunnel.class.toString());

  private boolean tunnelReturn = true;

  private void startTunnel() {
    try {
      connect().setPortForwardingL(((SshLocalConfig) configuration).bindHost, ((SshLocalConfig) configuration).bindPort,
          ((SshLocalConfig) configuration).redirectServer, ((SshLocalConfig) configuration).redirectPort);
      tunnelReturn = true;
    } catch (Exception e) {
      logger.logException(e);
      tunnelReturn = false;
    }
  }

  @Override
  public void init() {
    startTunnel();
  }

  @Override
  protected boolean isTunnelOk() {
    return tunnelReturn;
  }

}
