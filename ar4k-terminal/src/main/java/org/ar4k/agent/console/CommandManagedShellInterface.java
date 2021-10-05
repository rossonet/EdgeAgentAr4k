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
package org.ar4k.agent.console;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.ar4k.agent.core.EdgeAgentCore;
import org.ar4k.agent.core.services.EdgeComponent.ServiceStatus;
import org.ar4k.agent.core.services.ServiceComponent;
import org.ar4k.agent.core.services.ServiceConfig;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.service.commandManaged.CommandManagedConfig;
import org.ar4k.agent.service.commandManaged.CommandManagedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
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
 *         Interfaccia da linea di comando per la gestione delle installazioni remote
 *
 */

//TODO completare gestione modulo da gestione comandi locali come servizi (esempio Staer SG e OpenVPN)

@ShellCommandGroup("Command Managed Executor Commands")
@ShellComponent
@RestController
@RequestMapping("/commandManagedExecutorInterface")
public class CommandManagedShellInterface extends AbstractShellHelper {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(CommandManagedShellInterface.class);

	@Autowired
	private EdgeAgentCore edgeAgentCore;

	final public Map<ServiceConfig, ServiceComponent<CommandManagedService>> localServices = new HashMap<>();

	@ShellMethod(value = "Add a command service to the selected configuration")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addCommandService(@ShellOption(optOut = true) @Valid CommandManagedConfig service) {
		getWorkingConfig().pots.add(service);
	}

	@ShellMethod(value = "Create a temporary command service")
	@ManagedOperation
	public void createTemporaryCommandService(@ShellOption(optOut = true) @Valid CommandManagedConfig service) {
		localServices.put(service, configureCommandService(service));
	}

	private ServiceComponent<CommandManagedService> configureCommandService(CommandManagedConfig service) {
		// TODO Auto-generated method stub
		return null;
	}

	@ShellMethod(value = "Delete a temporary command service")
	@ManagedOperation
	public void deleteTemporaryCommandService(String serviceName) {
		ServiceConfig serviceToDelete = findLocalConfigCommandServiceByName(serviceName);
		if (serviceToDelete != null) {
			localServices.remove(serviceToDelete);
		}
	}

	@ShellMethod(value = "List temporary command services")
	@ManagedOperation
	public Collection<ServiceConfig> listTemporaryCommandServices() {
		return localServices.keySet();
	}

	@ShellMethod(value = "List all command services")
	@ManagedOperation
	public Collection<ServiceComponent<CommandManagedService>> listCommandServices() {
		Collection<ServiceComponent<CommandManagedService>> result = getAgentCommandServices();
		result.addAll(localServices.values());
		return result;
	}

	private Collection<ServiceComponent<CommandManagedService>> getAgentCommandServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@ShellMethod(value = "Start command service")
	@ManagedOperation
	public void startCommandService(String serviceName) {
		findService(serviceName).start();
	}

	private ServiceComponent<CommandManagedService> findService(String serviceName) {
		// TODO Auto-generated method stub
		return null;
	}

	@ShellMethod(value = "Stop command service")
	@ManagedOperation
	public void stopCommandService(String serviceName) {
		findService(serviceName).stop();
	}

	@ShellMethod(value = "Pulse command service")
	@ManagedOperation
	public void pulseCommandService(String serviceName) {
		findService(serviceName).getPot().pulse();
	}

	@ShellMethod(value = "Check command service")
	@ManagedOperation
	public ServiceStatus checkCommandService(String serviceName) {
		return findService(serviceName).getPot().updateAndGetStatus();
	}

	@ShellMethod(value = "Get status of command service")
	@ManagedOperation
	public String statusCommandService(String serviceName) {
		final String descriptionStatus = findService(serviceName).getPot().getDescriptionStatus();
		logger.info("The status of service " + serviceName + " is " + descriptionStatus);
		return descriptionStatus;
	}

	@ShellMethod(value = "Save command service")
	@ManagedOperation
	public void saveCommandService(String serviceName) {
		findService(serviceName).getPot().saveStatus();
	}

	@ShellMethod(value = "Load command service")
	@ManagedOperation
	public void loadCommandService(String serviceName) {
		findService(serviceName).getPot().loadStatus();
	}

	private ServiceConfig findLocalConfigCommandServiceByName(String serviceName) {
		ServiceConfig foundService = null;
		for (ServiceConfig s : localServices.keySet()) {
			if (serviceName.equals(s.getName())) {
				foundService = s;
				break;
			}
		}
		return foundService;
	}

	@ShellMethod(value = "Add a OpenVPN service to the selected configuration")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addOpenvpnService() {
		// TODO Auto-generated method stub
	}

	@ShellMethod(value = "Add a SmartGateway service to the selected configuration")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addSmartgatewayService() {
		// TODO Auto-generated method stub
	}

	@ShellMethod(value = "Add a dhcpd service to the selected configuration")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addDhcpdService() {
		// TODO Auto-generated method stub
	}

	@ShellMethod(value = "Add a operating system ssh tunnel service to the selected configuration")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addSystemSshClientService() {
		// TODO Auto-generated method stub
	}

}
