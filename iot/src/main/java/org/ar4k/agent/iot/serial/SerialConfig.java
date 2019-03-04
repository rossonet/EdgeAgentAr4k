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
package org.ar4k.agent.iot.serial;

import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.iot.serial.SerialService.BaudRate;
import org.ar4k.agent.iot.serial.SerialService.ConventionalNotation;
import org.ar4k.agent.iot.serial.BaudRateValidator;
import org.ar4k.agent.iot.serial.ConventionalNotationValidator;
import org.ar4k.agent.iot.serial.SerialService;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 * 
 *         Configurazione porta seriale collegata all'agente.
 */
public class SerialConfig extends ServiceConfig {

	private static final long serialVersionUID = -864164279161787378L;

	@Parameter(names = "--serial", description = "serial port")
	public String serial = "/dev/ttyACM0";

	@Parameter(names = "--baudRate", description = "baudrate", validateWith = BaudRateValidator.class)
	public BaudRate baudRate = BaudRate.bs115200;

	@Parameter(names = "--convenionalNotation", description = "convenionalNotation", validateWith = ConventionalNotationValidator.class)
	public ConventionalNotation conventionalNotation = ConventionalNotation._8N1;

	@Parameter(names = "--queueSize", description = "Queue size for the message received", validateWith = ConventionalNotationValidator.class)
	public int queueSize = 10000;

	@Parameter(names = "--camelEndpointWrite", description = "Camel label for the consumer of the write")
	public String camelEndpointWriteSerial = "log:?level=INFO&showBody=true";

	@Parameter(names = "--camelEndpointRead", description = "Camel label for the producer of the read")
	public String camelEndpointReadSerial = "log:?level=INFO&showBody=true";

	@Parameter(names = "--camelEndpointWriteOk", description = "Camel label for the producer of the write confirm")
	public String camelEndpointWriteOk = "log:?level=INFO&showBody=true";

	@Parameter(names = "--camelEndpointReadByte", description = "Camel label for the producer of the read byte arrived")
	public String camelEndpointReadOk = "log:?level=INFO&showBody=true";

	@Parameter(names = "--camelEndpointResetSerial", description = "Camel label for the consumer queue for reset the serial interface")
	public String camelResetSerial = "log:?level=INFO&showBody=true";

	public SerialService instanziate() {
		// System.out.println("Serial service start");
		SerialService ss = new SerialService();
		return ss;
	}
}
