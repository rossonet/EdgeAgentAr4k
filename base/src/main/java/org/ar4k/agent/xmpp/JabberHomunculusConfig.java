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

import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.Ar4kComponent;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 * 
 *         Configurazione servizio connessione XMPP.
 *
 */
public class JabberHomunculusConfig extends ServiceConfig {

  private static final long serialVersionUID = -4642788033911032726L;

  public Ar4kComponent instanziate() {
    // System.out.println("Serial service start");
    JabberHomunculusService ss = new JabberHomunculusService();
    return (Ar4kComponent) ss;
  }
}
