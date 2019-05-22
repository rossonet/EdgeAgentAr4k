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

import java.io.IOException;

import javax.validation.Valid;

import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.tunnels.http.grpc.BeaconServer;
import org.ar4k.agent.tunnels.http.grpc.BeaconServiceConfig;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
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
 *         Interfaccia gestione servizi tunnel.
 */

@ShellCommandGroup("Beacon Commands")
@ShellComponent
@EnableMBeanExport
@ManagedResource(objectName = "bean:name=beaconInterface", description = "Ar4k Agent Beacon Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "beaconInterface")
@RestController
@RequestMapping("/beaconInterface")
public class BeaconShellInterface extends AbstractShellHelper {

  BeaconServer tmpServer = null;

  protected Availability testBeaconNull() {
    return tmpServer == null ? Availability.available()
        : Availability.unavailable("a Beacom server exist on port " + tmpServer.getPort());
  }

  protected Availability testBeaconRunning() {
    return tmpServer != null ? Availability.available() : Availability.unavailable("no Beacon servers are running");
  }

  @ShellMethod(value = "Add Beacon configuration", group = "Beacon Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public void addBeaconService(@ShellOption(optOut = true) @Valid BeaconServiceConfig service) {
    getWorkingConfig().services.add(service);
  }

  @ShellMethod(value = "Start Beacon server on the enviroment in where the agent is running", group = "Beacon Commands")
  @ManagedOperation
  @ShellMethodAvailability("testBeaconNull")
  public boolean runBeaconServer(
      @ShellOption(help = "the tcp port for the Beacon server", defaultValue = "6599") int port) throws IOException {
    tmpServer = new BeaconServer(port);
    tmpServer.start();
    return true;
  }

  @ShellMethod(value = "Stop Beacon server on the enviroment in where the agent is running", group = "Beacon Commands")
  @ManagedOperation
  @ShellMethodAvailability("testBeaconRunning")
  public boolean stopBeaconServer() {
    tmpServer.stop();
    tmpServer = null;
    return true;
  }

}
