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

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 * 
 *         Filtro con espressione regolare per l'input ottenuto via
 *         seriale/telnet da una CNC con endpoint camel di destinazione.
 *
 */
public class RouterMessagesCnc implements Serializable {

	private static final long serialVersionUID = 7183637837535639684L;

	@Parameter(names = "--regExp", description = "regular expression to find")
	public String regExp = ".*";

	@Parameter(names = "--camelEndpoint", description = "Camel label that rappresent the destination for the message")
	public String camelEndpoint = "log:?level=INFO&showBody=true";

}
