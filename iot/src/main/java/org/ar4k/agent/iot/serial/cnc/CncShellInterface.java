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

import javax.validation.Valid;

import org.ar4k.agent.core.Anima;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
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
@EnableMBeanExport
@ManagedResource(objectName = "bean:name=cncInterface", description = "Ar4k Agent CNC Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "cncInterface")
@RestController
@RequestMapping("/cncInterface")
@ConditionalOnProperty(name = "ar4k.cnc", havingValue = "true")
public class CncShellInterface {

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	Anima anima;

	@Override
	protected void finalize() {
	}

	@ShellMethod(value = "Add a CNC interface service to the selected configuration", group = "CNC Commands")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addCncService(@ShellOption(optOut = true) @Valid CncConfig service) {
		anima.getWorkingConfig().services.add(service);
	}

}
