/*
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
package org.ar4k.agent.core;

import java.net.Socket;

import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.config.tunnel.TunnelConfig;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Classe astratta base da implementare per la realizzazione di tunnel
 *         gestiti dall'agente.
 *
 */
public abstract class AbstractTunnelComponent implements Ar4kComponent {

  // iniettata vedi set/get
  private TunnelConfig configuration = null;

  public Socket socket = null;

  public static Socket getSocks(String alias) {
    Anima anima = (Anima) Anima.getApplicationContext().getBean("anima");
    Socket ok = null;
    for (AbstractTunnelComponent b : anima.getTunnels()) {
      if (b.getConfiguration().getName().equals(alias)) {
        ok = b.socket;
        System.out.println("ok socks!!");
      }
      System.out.println(((TunnelConfig) b.getConfiguration()).getName());
    }
    return ok;
  }

  @Override
  public TunnelConfig getConfiguration() {
    return configuration;
  }

  @Override
  public void setConfiguration(ConfigSeed configuration) {
    this.configuration = ((TunnelConfig) configuration);
  }

  public int checkConnession() {
    return 500;
  }
}
