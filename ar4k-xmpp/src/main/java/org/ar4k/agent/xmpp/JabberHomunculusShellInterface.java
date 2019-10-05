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
package org.ar4k.agent.xmpp;

import javax.validation.Valid;

import org.ar4k.agent.config.PotConfig;
import org.ar4k.agent.helper.AbstractShellHelper;
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
 *         Interfaccia gestione servizi tunnel.
 */

@ShellCommandGroup("Homunculus Jabber")
@ShellComponent
@EnableMBeanExport
@ManagedResource(objectName = "bean:name=jabberInterface", description = "Ar4k Agent Jabber Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "jabberInterface")
@RestController
@RequestMapping("/jabberInterface")
public class JabberHomunculusShellInterface extends AbstractShellHelper {

  @ShellMethod(value = "Add a Jabber endpoint to the selected configuration")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public void addJabberConnection(@ShellOption(optOut = true) @Valid JabberHomunculusConfig service) {
    getWorkingConfig().pots.add((PotConfig) service);
  }

}