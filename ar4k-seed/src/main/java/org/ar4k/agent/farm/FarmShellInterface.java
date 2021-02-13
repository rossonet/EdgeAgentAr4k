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
package org.ar4k.agent.farm;

import javax.validation.Valid;

import org.ar4k.agent.farm.docker.DockerFarmConfig;
import org.ar4k.agent.farm.kubernetes.KubernetesFarmConfig;
import org.ar4k.agent.farm.local.LocalAccountFarmConfig;
import org.ar4k.agent.farm.openshift.OpenShiftFarmConfig;
import org.ar4k.agent.helper.AbstractShellHelper;
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
 *         Interfaccia da linea di comando per la gestione delle farm
 *
 */

@ShellCommandGroup("Farmer Commands")
@ShellComponent
//@EnableMBeanExport
//@ManagedResource(objectName = "bean:name=opcUaInterface", description = "Ar4k Agent Main Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "opcUaInterface")
@RestController
@RequestMapping("/farmInterface")
public class FarmShellInterface extends AbstractShellHelper {

	@ShellMethod(value = "Add a Local Account Farm", group = "Farmer Commands")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addFarmLocalAccount(@ShellOption(optOut = true) @Valid LocalAccountFarmConfig farm) {
		getWorkingConfig().pots.add(farm);
	}

	@ShellMethod(value = "Add a Local Docker Farm", group = "Farmer Commands")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addFarmDocker(@ShellOption(optOut = true) @Valid DockerFarmConfig farm) {
		getWorkingConfig().pots.add(farm);
	}

	@ShellMethod(value = "Add a Local Kubernetes Farm", group = "Farmer Commands")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addFarmKubernetes(@ShellOption(optOut = true) @Valid KubernetesFarmConfig farm) {
		getWorkingConfig().pots.add(farm);
	}

	@ShellMethod(value = "Add a Local OpenShift Farm", group = "Farmer Commands")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addFarmOpenShift(@ShellOption(optOut = true) @Valid OpenShiftFarmConfig farm) {
		getWorkingConfig().pots.add(farm);
	}

}
