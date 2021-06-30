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
package org.ar4k.agent.industrial;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.validation.Valid;

import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.mqtt.client.MqttTopicConfig;
import org.ar4k.agent.mqtt.client.PahoClientConfig;
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
 *         MQTT
 *
 */

@ShellCommandGroup("Industrial Commands")
@ShellComponent
@RestController
@RequestMapping("/industrialInterfaceMqtt")
public class MqttShellInterface extends AbstractShellHelper {

	protected Availability sessionClientPahoOk() {
		return (sessionOk().equals(Availability.available()) && getWorkingService() instanceof PahoClientConfig)
				? Availability.available()
				: Availability.unavailable("you must select a MQTT client configuration service");
	}

	@ShellMethod(value = "Add a MQTT service client to the selected configuration", group = "MQTT Commands")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addMqttClientService(@ShellOption(optOut = true) @Valid PahoClientConfig service) {
		getWorkingConfig().pots.add(service);
	}

	@ShellMethod(value = "List topics in mqtt client config", group = "MQTT Commands")
	@ManagedOperation
	@ShellMethodAvailability("sessionClientPahoOk")
	public Collection<String> mqttClientListNodes() {
		Collection<String> result = new HashSet<>();
		for (final MqttTopicConfig singleSubscription : ((PahoClientConfig) getWorkingService()).subscriptions) {
			result.add(singleSubscription.toString());
		}
		return result;
	}

	@ShellMethod(value = "Remove topic in mqtt client config", group = "MQTT Commands")
	@ManagedOperation
	@ShellMethodAvailability("sessionClientPahoOk")
	public void mqttClientRemoveNode(@ShellOption(help = "topic uuid") String uuid) {
		MqttTopicConfig target = null;
		final List<MqttTopicConfig> topics = ((PahoClientConfig) getWorkingService()).subscriptions;
		for (final MqttTopicConfig n : topics) {
			if (n.uuid.equals(uuid)) {
				target = n;
			}
		}
		if (target != null) {
			topics.remove(target);
		}
	}

	@ShellMethod(value = "Add topic to mqtt config", group = "MQTT Commands")
	@ManagedOperation
	@ShellMethodAvailability("sessionClientPahoOk")
	public void mqttClientAddNode(@ShellOption(optOut = true) @Valid MqttTopicConfig node) {
		((PahoClientConfig) getWorkingService()).subscriptions.add(node);
	}

}
