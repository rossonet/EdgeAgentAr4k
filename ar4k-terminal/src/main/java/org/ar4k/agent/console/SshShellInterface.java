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

import javax.validation.Valid;

import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.tunnels.ssh.client.SshLocalConfig;
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
 *         Interfaccia a linea di comando per gestione tunnel SSH.
 */

@ShellCommandGroup("Tunnel Commands")
@ShellComponent
//@EnableMBeanExport
//@ManagedResource(objectName = "bean:name=sshInterface", description = "Ar4k Agent Ssh Tunnel", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "sshInterface")
@RestController
@RequestMapping("/sshInterface")
public class SshShellInterface extends AbstractShellHelper {

  @ShellMethod(value = "Add a ssh endpoint to the selected configuration", group = "Tunnel Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public void addSshNetworkPoint(@ShellOption(optOut = true) @Valid SshLocalConfig service) {
    getWorkingConfig().pots.add(service);
  }

}
