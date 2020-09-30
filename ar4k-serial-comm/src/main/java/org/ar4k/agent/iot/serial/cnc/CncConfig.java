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
package org.ar4k.agent.iot.serial.cnc;

import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.iot.serial.SerialConfig;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione connessione macchina CNC.
 */
public class CncConfig extends SerialConfig {

  private static final long serialVersionUID = -117482035560530235L;

  // mappa con espressione regolare il testo dei messaggi della console su
  // particolari code
  @Parameter(names = "--replies", description = "List of triggered message to route to differents channels")
  public List<RouterMessagesCnc> repliesAnalizer = new ArrayList<>();

  @Parameter(names = "--cronCommands", description = "List of commands send in loop")
  public List<TriggerCommand> cronCommands = new ArrayList<>();

  @Parameter(names = "--defaultTimeoutCommand", description = "Default timeout for the command sent to the CNC")
  public long defaultTimeoutCommand = 5000L;

  public CncConfig() {
    repliesAnalizer.add(new RouterMessagesCnc());
    cronCommands.add(new TriggerCommand());
  }

  @Override
  public CncService instantiate() {
    CncService ss = new CncService();
    ss.setConfiguration(this);
    return ss;
  }

}
