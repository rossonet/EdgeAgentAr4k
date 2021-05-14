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
package org.ar4k.agent.opcua;

import javax.validation.Valid;

import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.opcua.client.OpcUaClientConfig;
import org.ar4k.agent.opcua.server.OpcUaServerConfig;
import org.ar4k.agent.opcua.server.OpcUaServerService;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Interfaccia da linea di comando per configurazione della connessione
 *         OPC UA e MQTT
 *
 */

@ShellCommandGroup("Industrial Commands")
@ShellComponent
@RestController
@RequestMapping("/industrialInterface")
public class IndustrialShellInterface extends AbstractShellHelper {

	OpcUaServerService opcUaServer = null;

	protected Availability testActiveOpcNull() {
		return opcUaServer == null ? Availability.available()
				: Availability.unavailable("a ActiveMQ broker exists with name "
						+ opcUaServer.getServer().getServer().getDiagnosticsSummary());
	}

	protected Availability testActiveOpcRunning() {
		return opcUaServer != null ? Availability.available()
				: Availability.unavailable("activeMQ broker is not running");
	}

	@ShellMethod(value = "Add a OPCUA service client to the selected configuration", group = "OPC UA Commands")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addOpcUaClientService(@ShellOption(optOut = true) @Valid OpcUaClientConfig service) {
		getWorkingConfig().pots.add(service);
	}

	@ShellMethod(value = "Add a OPCUA service server to the selected configuration", group = "OPC UA Commands")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addOpcUaServerService(@ShellOption(optOut = true) @Valid OpcUaServerConfig service) {
		getWorkingConfig().pots.add(service);
	}

	@ShellMethod(value = "Start OPC UA Server", group = "OPC UA Commands")
	@ManagedOperation
	@ShellMethodAvailability("testActiveOpcNull")
	public void serverOpcUaStart(@ShellOption(optOut = true) @Valid OpcUaServerConfig opcUaConfig) {
		final OpcUaServerService opcUaInstance = (OpcUaServerService) opcUaConfig.instantiate();
		opcUaInstance.init();
		opcUaServer = opcUaInstance;
	}

	@ShellMethod(value = "Stop OPC UA Server", group = "OPC UA Commands")
	@ManagedOperation
	@ShellMethodAvailability("testActiveOpcRunning")
	public void serverOpcUaStop() throws Exception {
		opcUaServer.kill();
		opcUaServer = null;
	}

}
