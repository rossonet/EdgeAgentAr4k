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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.helper.NetworkHelper;
import org.ar4k.agent.network.NetworkConfig;
import org.ar4k.agent.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.network.NetworkConfig.NetworkProtocol;
import org.ar4k.agent.rpc.process.xpra.XpraSessionProcess;
import org.ar4k.agent.tunnels.http.beacon.BeaconAgent;
import org.ar4k.agent.tunnels.http.beacon.BeaconClient;
import org.ar4k.agent.tunnels.http.beacon.BeaconNetworkConfig;
import org.ar4k.agent.tunnels.http.beacon.BeaconServer;
import org.ar4k.agent.tunnels.http.beacon.BeaconServiceConfig;
import org.ar4k.agent.tunnels.http.beacon.socket.TunnelRunnerBeaconServer;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.AgentRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.Command;
import org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply;
import org.springframework.beans.factory.annotation.Autowired;
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
 *         Interfaccia gestione servizi tunnel.
 */

@ShellCommandGroup("Beacon Server Commands")
@ShellComponent
//@EnableMBeanExport
//@ManagedResource(objectName = "bean:name=beaconInterface", description = "Ar4k Agent Beacon Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "beaconInterface")
@RestController
@RequestMapping("/beaconInterface")
public class BeaconShellInterface extends AbstractShellHelper implements AutoCloseable {

  @Autowired
  private Anima anima;

  private static final CharSequence COMPLETION_CHAR = "?";

  BeaconServer tmpServer = null;
  BeaconClient tmpClient = null;

  private Set<Process> openedProcess = new HashSet<>();

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
    return (tmpClient != null || anima.getBeaconClient() != null) ? Availability.available()
        : Availability.unavailable("no Beacon client are running local and in Anima");
  }

  protected Availability testBeaconClientRunningLocal() {
    return tmpClient != null ? Availability.available()
        : Availability.unavailable("no Beacon client are running local");
  }

  @ShellMethod(value = "Add Beacon service to the selected configuration", group = "Beacon Server Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public void addBeaconService(@ShellOption(optOut = true) @Valid BeaconServiceConfig service) {
    getWorkingConfig().pots.add(service);
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
      throws IOException, UnrecoverableKeyException {
    tmpServer = new BeaconServer.Builder().setAnima(anima).setPort(port).setDiscoveryPort(discoveryPort)
        .setStringDiscovery(discoveryMessage).setBroadcastAddress(discoveryAddress).setAcceptCerts(acceptAllCerts)
        .build();
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

  @ShellMethod(value = "List tunnels running to the Beacon server", group = "Beacon Server Commands")
  @ManagedOperation
  @ShellMethodAvailability("testBeaconServerRunning")
  public List<TunnelRunnerBeaconServer> listBeaconTunnels() {
    return tmpServer.getTunnels();
  }

  @ShellMethod(value = "List registration request on beacon server", group = "Beacon Server Commands")
  @ManagedOperation
  @ShellMethodAvailability("testBeaconServerRunning")
  public List<AgentRequest> listBeaconRegistrations() {
    return tmpServer.listAgentRequests();
  }

  // comandi client

  @ShellMethod(value = "Connect to a Beacon service as an agent", group = "Beacon Client Commands")
  @ManagedOperation
  @ShellMethodAvailability("testBeaconClientNull")
  public boolean connectToBeaconService(
      @ShellOption(help = "the target Beacon Serve URL. If the port is 0 work just the discovery", defaultValue = "http://127.0.0.1:6599") String beaconServer,
      @ShellOption(help = "the udp target port for the discovery message", defaultValue = "39666") int discoveryPort,
      @ShellOption(help = "the discovery message txt. It is filtered by the client", defaultValue = "AR4K-BEACON-CONSOLE") String discoveryMessage) {
    tmpClient = anima.connectToBeaconService(beaconServer, null, discoveryPort, discoveryMessage, true);
    return tmpClient != null ? true : false;
  }

  @ShellMethod(value = "List Agents connected to the Beacon server", group = "Beacon Client Commands")
  @ManagedOperation
  @ShellMethodAvailability("testBeaconClientRunning")
  public List<Agent> listBeaconAgents() {
    return resolveBeaconClient().listAgentsConnectedToBeacon();
  }

  @ShellMethod(value = "Create TCP tunnel over Beacon protocol", group = "Beacon Client Commands")
  @ManagedOperation
  @ShellMethodAvailability("testBeaconClientRunning")
  public void createBeaconTunnel(
      @ShellOption(help = "the unique ID of the remote agent on the other side of tunnel") String agentTarget,
      @ShellOption(help = "the port for the TCP Server bind") int sourceServerPort,
      @ShellOption(help = "the destination TCP port for the client side") int destinationIpPort,
      @ShellOption(help = "the destination ip for the client") String destinationIp,
      @ShellOption(help = "description of this tunnel") String description,
      @ShellOption(help = "tunnel name") String name,
      @ShellOption(help = "the role in TCP connection that the other side have. It should be SERVER or CLIENT") NetworkMode role) {
    BeaconNetworkConfig config = new BeaconNetworkConfig(name, description, role, NetworkProtocol.TCP, destinationIp,
        destinationIpPort, sourceServerPort);
    resolveBeaconClient().getNetworkTunnel(agentTarget, config);
  }

  // TODO CRUD channels Beacon

  @ShellMethod(value = "List commands on a remote agent connected by Beacon", group = "Beacon Client Commands")
  @ManagedOperation
  @ShellMethodAvailability("testBeaconClientRunning")
  public List<String> listCommandsOnRemoteAgent(
      @ShellOption(help = "the unique ID of the remote agent") String uniqueId) {
    ListCommandsReply reply = resolveBeaconClient().listCommadsOnAgent(uniqueId);
    List<String> result = new ArrayList<>();
    for (Command c : reply.getCommandsList()) {
      result.add(c.getCommand());
    }
    Collections.sort(result);
    return result;
  }

  @ShellMethod(value = "Stop the Beacon client connection", group = "Beacon Client Commands")
  @ManagedOperation
  @ShellMethodAvailability("testBeaconClientRunningLocal")
  public void stopBeaconClient() {
    tmpClient.close();
    tmpClient = null;
  }

  @ShellMethod(value = "Run command on a agent connected by Beacon", group = "Beacon Client Commands")
  @ManagedOperation
  @ShellMethodAvailability("testBeaconClientRunning")
  public String runCommandOnRemoteAgent(@ShellOption(help = "the unique ID of the remote agent") String uniqueId,
      @ShellOption(help = "command to run - you can use ? for completition") String command) {
    StringBuilder result = new StringBuilder();
    if (command.contains(COMPLETION_CHAR)) {
      CompleteCommandReply ccr = resolveBeaconClient().runCompletitionOnAgent(uniqueId, command);
      for (String singleLine : ccr.getRepliesList()) {
        result.append(singleLine + "\n");
      }
    } else {
      ElaborateMessageReply emr = resolveBeaconClient().runCommadsOnAgent(uniqueId, command);
      result.append(emr.getReply());
    }
    return result.toString();
  }

  @ShellMethod(value = "Run Xpra server on remote agent and connect to it with the Xpra client", group = "Beacon Client Commands")
  @ManagedOperation
  @ShellMethodAvailability("testBeaconClientRunning")
  public String runXpraServerOnAgentAndConnectByXpra(
      @ShellOption(help = "the unique ID of the remote agent") String uniqueId,
      @ShellOption(help = "label to identify the xpra server") String executorLabel,
      @ShellOption(help = "the command to start in the X server", defaultValue = "xterm") String cmd) throws Exception {
    String command = "run-xpra-server --executor-label " + executorLabel;
    ElaborateMessageReply returnStartingXpra = resolveBeaconClient().runCommadsOnAgent(uniqueId, command);
    logger.info("REMOTE XPRA " + returnStartingXpra);
    String commandListXpra = "list-xpra-servers";
    ElaborateMessageReply returnListXpra = resolveBeaconClient().runCommadsOnAgent(uniqueId, commandListXpra);
    logger.info("REMOTE XPRA LIST " + returnListXpra);
    int remoteXpraPort = Integer.valueOf(returnStartingXpra.getReply().replace("\n", ""));
    int localPort = NetworkHelper.findAvailablePort(14500);
    NetworkConfig remoteConfig = new BeaconNetworkConfig("beacon-xpra-" + remoteXpraPort, "tunnel xpra",
        NetworkMode.CLIENT, NetworkProtocol.TCP, "127.0.0.1", remoteXpraPort, localPort);
    resolveBeaconClient().getNetworkTunnel(uniqueId, remoteConfig);
    Thread.sleep(5000L);
    ProcessBuilder builder = new ProcessBuilder("xpra", "attach", "tcp://127.0.0.1:" + localPort + "/");
    Process processXpra = builder.start();
    openedProcess.add(processXpra);
    // processXpra.waitFor();
    // logger.info("LOCAL XPRA " + getOutput(processXpra));
    // logger.info("LOCAL XPRA ERR" + getErrors(processXpra));
    // networkTunnel.close();
    return "xpra " + "attach " + "tcp://127.0.0.1:" + localPort + "/";
  }

  @SuppressWarnings("unused")
  private String getOutput(Process process) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String reply = null;
    try {
      if (reader.ready())
        reply = reader.lines().collect(Collectors.joining());
    } catch (IOException e) {
      logger.logException(e);
    }
    return reply;
  }

  @SuppressWarnings("unused")
  private String getErrors(Process process) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
    String reply = null;
    try {
      if (reader.ready())
        reply = reader.lines().collect(Collectors.joining());
    } catch (IOException e) {
      logger.logException(e);
    }
    return reply;
  }

  @ShellMethod(value = "Run Xpra server on remote agent and connect to it with the browser", group = "Beacon Client Commands")
  @ManagedOperation
  @ShellMethodAvailability("testBeaconClientRunning")
  public boolean runXpraServerOnAgentAndConnectByBrowser(
      @ShellOption(help = "label to identify the xpra server") String executorLabel,
      @ShellOption(help = "the tcp port for the HTML5 console", defaultValue = "0") int port,
      @ShellOption(help = "the command to start in the X server", defaultValue = "xterm") String cmd) {
    XpraSessionProcess p = new XpraSessionProcess();
    p.setLabel(executorLabel);
    p.setTcpPort(port);
    p.setCommand(cmd);
    p.eval(cmd);
    ((RpcConversation) anima.getRpc(getSessionId())).getScriptSessions().put(executorLabel, p);
    return true;
  }

  @ShellMethod(value = "Connect to remote agent with ssh by Beacon tunnel", group = "Beacon Client Commands")
  @ManagedOperation
  @ShellMethodAvailability("testBeaconClientRunning")
  public String connectToAgentOnStandardSsh(@ShellOption(help = "the unique ID of the remote agent") String uniqueId,
      @ShellOption(help = "ssh user to login on the agent", defaultValue = "root") String sshUser,
      @ShellOption(help = "ssh destination port", defaultValue = "22") String sshPort) throws Exception {
    int localPort = NetworkHelper.findAvailablePort(14500);
    NetworkConfig remoteConfig = new BeaconNetworkConfig("beacon-ssh-22", "tunnel xpra", NetworkMode.CLIENT,
        NetworkProtocol.TCP, "127.0.0.1", Integer.valueOf(sshPort), localPort);
    resolveBeaconClient().getNetworkTunnel(uniqueId, remoteConfig);
    Thread.sleep(5000L);
    ProcessBuilder builder = new ProcessBuilder("xterm", "-e", "ssh", sshUser + "@127.0.0.1", "-p",
        String.valueOf(localPort));
    Process processSh = builder.start();
    openedProcess.add(processSh);
    // processSh.waitFor();
    // logger.info("LOCAL SSH " + getOutput(processSh));
    // logger.info("LOCAL SSH ERR " + getErrors(processSh));
    // networkTunnel.close();
    return "xterm " + "-e " + "ssh " + sshUser + "@127.0.0.1 " + "-p " + String.valueOf(localPort);
  }

  @ShellMethod(value = "Complete reload remote node", group = "Beacon Client Commands")
  @ManagedOperation
  public String completeReloadRemoteAgent(@ShellOption(help = "target agent") String agentName) {
    return anima.getBeaconClient().runCommadsOnAgent(agentName, "complete-reload").getReply();
  }

  @ShellMethod(value = "Restart remote node", group = "Beacon Client Commands")
  @ManagedOperation
  public String restartRemoteAgent(@ShellOption(help = "target agent") String agentName) {
    return anima.getBeaconClient().runCommadsOnAgent(agentName, "restart").getReply();
  }

  private BeaconClient resolveBeaconClient() {
    return tmpClient != null ? tmpClient : anima.getBeaconClient();
  }

  @Override
  public void close() throws Exception {
    if (tmpServer != null) {
      tmpServer.close();
    }
    if (tmpClient != null) {
      tmpClient.close();
    }

    if (openedProcess != null && !openedProcess.isEmpty()) {
      for (Process p : openedProcess) {
        p.destroyForcibly();
      }
      openedProcess.clear();
    }

  }

}
