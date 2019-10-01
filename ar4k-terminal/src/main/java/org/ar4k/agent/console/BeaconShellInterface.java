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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.ar4k.agent.config.PotConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.tunnels.http.grpc.BeaconAgent;
import org.ar4k.agent.tunnels.http.grpc.BeaconClient;
import org.ar4k.agent.tunnels.http.grpc.BeaconServer;
import org.ar4k.agent.tunnels.http.grpc.BeaconServiceConfig;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.Command;
import org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply;
import org.springframework.beans.factory.annotation.Autowired;
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

@ShellCommandGroup("Beacon Server Commands")
@ShellComponent
@EnableMBeanExport
@ManagedResource(objectName = "bean:name=beaconInterface", description = "Ar4k Agent Beacon Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "beaconInterface")
@RestController
@RequestMapping("/beaconInterface")
public class BeaconShellInterface extends AbstractShellHelper {

  @Autowired
  private Anima anima;

  private static final CharSequence COMPLETION_CHAR = "?";

  BeaconServer tmpServer = null;
  BeaconClient tmpClient = null;

  protected Availability testBeaconServerNull() {
    return tmpServer == null ? Availability.available()
        : Availability.unavailable("a Beacom server exists on port " + tmpServer.getPort());
  }

  protected Availability testBeaconServerRunning() {
    return tmpServer != null ? Availability.available() : Availability.unavailable("no Beacon servers are running");
  }

  protected Availability testBeaconClientNull() {
    return tmpClient == null ? Availability.available()
        : Availability.unavailable("a Beacom client exists with id " + tmpClient.getAgentUniqueName());
  }

  protected Availability testBeaconClientRunning() {
    return tmpClient != null ? Availability.available() : Availability.unavailable("no Beacon client are running");
  }

  @ShellMethod(value = "Add Beacon service to the selected configuration", group = "Beacon Server Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public void addBeaconService(@ShellOption(optOut = true) @Valid BeaconServiceConfig service) {
    getWorkingConfig().pots.add((PotConfig) service);
  }

  @ShellMethod(value = "Start Beacon server on the enviroment in where the agent is running", group = "Beacon Server Commands")
  @ManagedOperation
  @ShellMethodAvailability("testBeaconServerNull")
  public boolean runBeaconServer(
      @ShellOption(help = "the tcp port for the Beacon server", defaultValue = "6599") int port,
      @ShellOption(help = "the udp target port for the discovery message", defaultValue = "39666") int discoveryPort,
      @ShellOption(help = "the broadcast target for the discovery message", defaultValue = "255.255.255.255") String discoveryAddress,
      @ShellOption(help = "accept all certificate (true) or managed by sign flow (false)", defaultValue = "true") boolean acceptAllCerts,
      @ShellOption(help = "the discovery message txt. It is filtered by the client", defaultValue = "AR4K-BEACON-CONSOLE") String discoveryMessage)
      throws IOException {
    tmpServer = new BeaconServer(anima, port, discoveryPort, discoveryAddress, acceptAllCerts, discoveryMessage, null,
        null, null, null);
    tmpServer.start();
    return true;
  }

  @ShellMethod(value = "Stop Beacon server on the enviroment in where the agent is running", group = "Beacon Server Commands")
  @ManagedOperation
  @ShellMethodAvailability("testBeaconServerRunning")
  public boolean stopBeaconServer() {
    tmpServer.stop();
    tmpServer = null;
    return true;
  }

  @ShellMethod(value = "List Agents connected to the Beacon server", group = "Beacon Server Commands")
  @ManagedOperation
  @ShellMethodAvailability("testBeaconServerRunning")
  public List<BeaconAgent> listBeaconAgentsConnected() {
    return tmpServer.getAgentLabelRegisterReplies();
  }

  @ShellMethod(value = "Connect to a Beacon service as an agent", group = "Beacon Client Commands")
  @ManagedOperation
  @ShellMethodAvailability("testBeaconClientNull")
  public boolean connectToBeaconService(
      @ShellOption(help = "the target Beacon Serve URL. If the port is 0 work just the discovery", defaultValue = "http://127.0.0.1:6599") String beaconServer,
      @ShellOption(help = "the udp target port for the discovery message", defaultValue = "39666") int discoveryPort,
      @ShellOption(help = "the discovery message txt. It is filtered by the client", defaultValue = "AR4K-BEACON-CONSOLE") String discoveryMessage) {
    tmpClient = anima.connectToBeaconService(beaconServer, discoveryPort, discoveryMessage);
    return true;
  }

  @ShellMethod(value = "List Agents connected to the Beacon server", group = "Beacon Client Commands")
  @ManagedOperation
  @ShellMethodAvailability("testBeaconClientRunning")
  public List<Agent> listBeaconAgents() {
    return tmpClient.listAgentsConnectedToBeacon();
  }

  @ShellMethod(value = "List commands on a remote agent connected by Beacon", group = "Beacon Client Commands")
  @ManagedOperation
  @ShellMethodAvailability("testBeaconClientRunning")
  public List<String> listCommandsOnRemoteAgent(
      @ShellOption(help = "the unique ID of the remote agent") String uniqueId) {
    ListCommandsReply reply = tmpClient.listCommadsOnAgent(uniqueId);
    List<String> result = new ArrayList<>();
    for (Command c : reply.getCommandsList()) {
      result.add(c.getCommand());
    }
    Collections.sort(result);
    return result;
  }

  @ShellMethod(value = "Run command on a agent connected by Beacon", group = "Beacon Client Commands")
  @ManagedOperation
  @ShellMethodAvailability("testBeaconClientRunning")
  public String runCommandOnRemoteAgent(@ShellOption(help = "the unique ID of the remote agent") String uniqueId,
      @ShellOption(help = "command to run - you can use ? for completition") String command) {
    StringBuilder result = new StringBuilder();
    if (command.contains(COMPLETION_CHAR)) {
      CompleteCommandReply ccr = tmpClient.runCompletitionOnAgent(uniqueId, command);
      for (String singleLine : ccr.getRepliesList()) {
        result.append(singleLine + "\n");
      }
    } else {
      ElaborateMessageReply emr = tmpClient.runCommadsOnAgent(uniqueId, command);
      result.append(emr.getReply());
    }
    return result.toString();
  }

}
