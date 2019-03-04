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

import java.io.Serializable;

import org.joda.time.Instant;
import org.joda.time.Period;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Comando eseguito periodicamente via seriale/telnet per recuperare
 *         informazioni da una CNC.
 */
public class TriggerCommand implements Serializable {

	private static final long serialVersionUID = 970930400109105077L;

	private Instant lastCall = new Instant();

	@Parameter(names = "--command", description = "Command to send")
	public String command = "M115\n";

	@Parameter(names = "--timer", description = "one shot every seconds")
	public int timer = 120;

	public void fire(CncService chiamante) {
		if (new Period(lastCall, new Instant()).toStandardSeconds().getSeconds() > timer) {
			chiamante.onMessageString(command);
			// System.out.println("invio: [" + command + "]");
			lastCall = new Instant();
		}
	}
}
