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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.validation.Valid;

import org.ar4k.agent.helper.AbstractShellHelper;
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
 *         Interfaccia a linea di comando per la gestione delle CNC collegate
 *         via seriale/telnet.
 */
@ShellCommandGroup("CNC Commands")
@ShellComponent
@RestController
@RequestMapping("/cncInterface")
public class CncShellInterface extends AbstractShellHelper {

	protected Availability sessionCncOk() {
		return (sessionOk().equals(Availability.available()) && getWorkingService() instanceof CncConfig)
				? Availability.available()
				: Availability.unavailable("you must select a CNC configuration service");
	}

	@ShellMethod(value = "Add a CNC interface service to the selected configuration", group = "CNC Commands")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addCncService(@ShellOption(optOut = true) @Valid CncConfig service) {
		getWorkingConfig().pots.add(service);
	}

	@ShellMethod(value = "List router message cnc config")
	@ManagedOperation
	@ShellMethodAvailability("sessionCncOk")
	public Collection<String> cncListRouterMessages() {
		Collection<String> result = new HashSet<>();
		for (final RouterMessagesCnc rmc : ((CncConfig) getWorkingService()).repliesAnalizer) {
			result.add(rmc.toString());
		}
		return result;
	}

	@ShellMethod(value = "Remove message cnc config")
	@ManagedOperation
	@ShellMethodAvailability("sessionCncOk")
	public void cncRemoveRouterMessages(@ShellOption(help = "message cnc uuid") String uuid) {
		RouterMessagesCnc target = null;
		final List<RouterMessagesCnc> repliesAnalizer = ((CncConfig) getWorkingService()).repliesAnalizer;
		for (final RouterMessagesCnc rmc : repliesAnalizer) {
			if (rmc.uuid.equals(uuid)) {
				target = rmc;
			}
		}
		if (target != null) {
			repliesAnalizer.remove(target);
		}
	}

	@ShellMethod(value = "Add message cnc config")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void cncAddRouterMessage(@ShellOption(optOut = true) @Valid RouterMessagesCnc messageService) {
		((CncConfig) getWorkingService()).repliesAnalizer.add(messageService);
	}

	@ShellMethod(value = "List regular query cnc config")
	@ManagedOperation
	@ShellMethodAvailability("sessionCncOk")
	public Collection<String> cncListCronQuery() {
		Collection<String> result = new HashSet<>();
		for (final TriggerCommand cronTask : ((CncConfig) getWorkingService()).cronCommands) {
			result.add(cronTask.toString());
		}
		return result;
	}

	@ShellMethod(value = "Remove regular query cnc config")
	@ManagedOperation
	@ShellMethodAvailability("sessionCncOk")
	public void cncRemoveCronQuery(@ShellOption(help = "trigger cnc uuid") String uuid) {
		TriggerCommand target = null;
		final List<TriggerCommand> triggers = ((CncConfig) getWorkingService()).cronCommands;
		for (final TriggerCommand tc : triggers) {
			if (tc.uuid.equals(uuid)) {
				target = tc;
			}
		}
		if (target != null) {
			triggers.remove(target);
		}
	}

	@ShellMethod(value = "Add regular query cnc config")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void cncAddCronQuery(@ShellOption(optOut = true) @Valid TriggerCommand trigger) {
		((CncConfig) getWorkingService()).cronCommands.add(trigger);
	}

}
