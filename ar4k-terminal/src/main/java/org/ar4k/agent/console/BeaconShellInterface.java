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

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.UnrecoverableKeyException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.IBeaconClient;
import org.ar4k.agent.core.valueProvider.Ar4kRemoteAgentProvider;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.helper.NetworkHelper;
import org.ar4k.agent.network.NetworkConfig;
import org.ar4k.agent.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.network.NetworkConfig.NetworkProtocol;
import org.ar4k.agent.rpc.process.xpra.XpraSessionProcess;
import org.ar4k.agent.tunnels.http.beacon.BeaconAgent;
import org.ar4k.agent.tunnels.http.beacon.BeaconClient;
import org.ar4k.agent.tunnels.http.beacon.BeaconServer;
import org.ar4k.agent.tunnels.http.beacon.BeaconServiceConfig;
import org.ar4k.agent.tunnels.http.beacon.socket.BeaconNetworkConfig;
import org.ar4k.agent.tunnels.http.beacon.socket.server.TunnelRunnerBeaconServer;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.AgentRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.Command;
import org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply;
import org.ar4k.agent.tunnels.ssh.client.SshLocalConfig;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
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

	@Autowired
	private SshShellInterface sshShellInterface;

	private static final CharSequence COMPLETION_CHAR = "?";

	private BeaconServer tmpServer = null;
	private BeaconClient tmpClient = null;

	private final Set<Process> openedProcess = new HashSet<>();

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
				.setStringDiscovery(discoveryMessage).setBroadcastAddress(discoveryAddress)
				.setAcceptCerts(acceptAllCerts).build();
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

	@ShellMethod(value = "List Agents connected to the Beacon server with health in JSON", group = "Beacon Client Commands")
	@ManagedOperation
	@ShellMethodAvailability("testBeaconClientRunning")
	public String listBeaconAgentsHumanReadable(
			@ShellOption(help = "print health details", defaultValue = "false") boolean printHealth,
			@ShellOption(help = "print record in table format", defaultValue = "true") boolean printAsTable,
			@ShellOption(help = "the logic for the filter. Default and. Should be 'and' or 'or'", defaultValue = "and") String filterLogic,
			@ShellOption(help = "filter on name field", defaultValue = ".*") String filterOnName,
			@ShellOption(help = "filter on description field", defaultValue = ".*") String filterOnDescription,
			@ShellOption(help = "filter on health field", defaultValue = ".*") String filterOnHealth,
			@ShellOption(help = "table width in chars", defaultValue = "80") int totalAvailableWidth) {
		final Pattern patternName = Pattern.compile(filterOnName);
		final Pattern patternDescription = Pattern.compile(filterOnDescription);
		final Pattern patternHealth = Pattern.compile(filterOnHealth);
		final StringBuilder resultAgents = new StringBuilder();
		final List<String[]> lines = new ArrayList<>();
		final DateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
		for (final Agent a : resolveBeaconClient().listAgentsConnectedToBeacon()) {
			final Matcher mName = patternName.matcher(a.getAgentUniqueName());
			final Matcher mDescription = patternDescription.matcher(a.getShortDescription());
			final Matcher mHealth = patternHealth.matcher(a.getJsonHardwareInfo());
			if ((filterLogic.equals("and") && mName.find() && mDescription.find() && mHealth.find())
					|| (filterLogic.equals("or") && (mName.find() || mDescription.find() || mHealth.find()))) {
				final StringBuilder agentTxt = new StringBuilder();
				if (!printAsTable) {
					agentTxt.append("id: " + a.getAgentUniqueName() + "\n");
					agentTxt.append("description: " + a.getShortDescription() + "\n");
					agentTxt.append("last_contact: " + df.format(new Date(a.getLastContact().getSeconds() * 1000)));
					if (printHealth) {
						try {
							agentTxt.append("health: " + new JSONObject(a.getJsonHardwareInfo()).toString(2) + "\n");
						} catch (final Exception aa) {
							logger.logException(aa);
						}
					}
					resultAgents.append(agentTxt.toString());
				} else {
					if (!printHealth) {
						lines.add(new String[] { a.getAgentUniqueName(), a.getShortDescription(),
								df.format(new Date(a.getLastContact().getSeconds() * 1000)) });
					} else {
						final StringBuilder health = new StringBuilder();
						final JSONObject hw = new JSONObject(a.getJsonHardwareInfo());
						for (final String k : hw.keySet()) {
							health.append(k + ": " + hw.opt(k) + "\n");
						}
						lines.add(new String[] { a.getAgentUniqueName(), a.getShortDescription(),
								df.format(new Date(a.getLastContact().getSeconds() * 1000)), health.toString() });
					}
				}
			}
		}
		if (!printAsTable) {
			return resultAgents.toString();
		} else {
			final Object[][] data = new Object[lines.size() + 1][printHealth ? 3 : 4];
			if (printHealth) {
				data[0] = new String[] { "agent id", "description", "last contact", "last health" };
			} else {
				data[0] = new String[] { "agent id", "description", "last contact" };
			}
			int c = 1;
			for (final String[] l : lines) {
				data[c] = l;
				c++;
			}
			final TableModel model = new ArrayTableModel(data);
			final TableBuilder tableBuilder = new TableBuilder(model);
			tableBuilder.addFullBorder(BorderStyle.fancy_light);
			tableBuilder.addHeaderBorder(BorderStyle.fancy_heavy);
			final Table table = tableBuilder.build();
			return table.render(totalAvailableWidth);
		}
	}

	@ShellMethod(value = "Create TCP tunnel over Beacon protocol", group = "Beacon Client Commands")
	@ManagedOperation
	@ShellMethodAvailability("testBeaconClientRunning")
	public void createBeaconTunnel(
			@ShellOption(help = "the unique ID of the remote agent on the other side of tunnel", valueProvider = Ar4kRemoteAgentProvider.class) String agentTarget,
			@ShellOption(help = "the port for the TCP Server bind") int sourceServerPort,
			@ShellOption(help = "the destination TCP port for the client side") int destinationIpPort,
			@ShellOption(help = "the destination ip for the client") String destinationIp,
			@ShellOption(help = "description of this tunnel") String description,
			@ShellOption(help = "tunnel name") String name,
			@ShellOption(help = "the role in TCP connection that the other side have. It should be SERVER or CLIENT") NetworkMode role) {
		final BeaconNetworkConfig config = new BeaconNetworkConfig(name, description, role, NetworkProtocol.TCP,
				destinationIp, destinationIpPort, sourceServerPort);
		resolveBeaconClient().getNetworkTunnel(agentTarget, config);
	}

	public void closeBeaconTunnel(@ShellOption(help = "the target Id of beacon network tunnel") long targetId) {
		resolveBeaconClient().closeNetworkTunnel(targetId);
	}

	@ShellMethod(value = "List commands on a remote agent connected by Beacon", group = "Beacon Client Commands")
	@ManagedOperation
	@ShellMethodAvailability("testBeaconClientRunning")
	public List<String> listCommandsOnRemoteAgent(
			@ShellOption(help = "the unique ID of the remote agent", valueProvider = Ar4kRemoteAgentProvider.class) String uniqueId) {
		final ListCommandsReply reply = resolveBeaconClient().listCommadsOnAgent(uniqueId);
		final List<String> result = new ArrayList<>();
		for (final Command c : reply.getCommandsList()) {
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
	public String runCommandOnRemoteAgent(
			@ShellOption(help = "the unique ID of the remote agent", valueProvider = Ar4kRemoteAgentProvider.class) String uniqueId,
			@ShellOption(help = "command to run - you can use ? for completition") String command) {
		final StringBuilder result = new StringBuilder();
		if (command.contains(COMPLETION_CHAR)) {
			final CompleteCommandReply ccr = resolveBeaconClient().runCompletitionOnAgent(uniqueId, command);
			logger.info("Beacon id used: " + resolveBeaconClient().getAgentUniqueName());
			for (final String singleLine : ccr.getRepliesList()) {
				result.append(singleLine + "\n");
			}
		} else {
			final ElaborateMessageReply emr = resolveBeaconClient().runCommadsOnAgent(uniqueId, command);
			result.append(emr.getReply());
		}
		return result.toString();
	}

	@ShellMethod(value = "Run Xpra server on remote agent and connect to it via beacon by web client", group = "Tunnel Commands")
	@ManagedOperation
	@ShellMethodAvailability("testBeaconClientRunning")
	public String runXpraServerOnAgentAndConnectToViaBeacon(
			@ShellOption(help = "the unique ID of the remote agent", valueProvider = Ar4kRemoteAgentProvider.class) String uniqueId,
			@ShellOption(help = "xpra web port", defaultValue = "0") int port,
			@ShellOption(help = "local port to expose xpra web", defaultValue = "0") int localPort,
			@ShellOption(help = "label to identify the xpra server on remote host", defaultValue = "xpra") String executorLabel,
			@ShellOption(help = "the command to start in the X server", defaultValue = "xterm") String cmd)
			throws Exception {
		final String command = "run-xpra-server --executor-label " + executorLabel + " --port " + port;
		final String returnStartingXpra = runCommandOnRemoteAgent(uniqueId, command);
		logger.info("REMOTE XPRA " + returnStartingXpra);
		final int remoteXpraPort = Integer.valueOf(returnStartingXpra.replace("\n", ""));
		if (localPort == 0) {
			localPort = NetworkHelper.findAvailablePort(14600);
		}
		final NetworkConfig remoteConfig = new BeaconNetworkConfig("beacon-xpra-" + remoteXpraPort, "tunnel xpra",
				NetworkMode.CLIENT, NetworkProtocol.TCP, "127.0.0.1", remoteXpraPort, localPort);
		resolveBeaconClient().getNetworkTunnel(uniqueId, remoteConfig);
		Thread.sleep(5000L);
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			Desktop.getDesktop().browse(new URI("http://127.0.0.1:" + localPort));
		}
		return "xpra " + "attach " + "tcp:127.0.0.1:" + localPort + "\nhttp://127.0.0.1:" + localPort;
	}

	private static int getRandomNumberInRange(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}
		final Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}

	@ShellMethod(value = "Run Xpra server on remote agent and connect to it via ssh by web client", group = "Tunnel Commands")
	@ManagedOperation
	@ShellMethodAvailability("testBeaconClientRunning")
	public String runXpraServerOnAgentAndConnectToViaSsh(
			@ShellOption(help = "the unique ID of the remote agent", valueProvider = Ar4kRemoteAgentProvider.class) String uniqueId,
			@ShellOption(help = "label to identify the xpra server on remote host") String executorLabel,
			@ShellOption(help = "the command to start in the X server", defaultValue = "xterm") String cmd,
			@ShellOption(help = "ssh tunnel server host") String sshTargetHost,
			@ShellOption(help = "ssh tunnel server port", defaultValue = "22") int sshTargetPort,
			@ShellOption(help = "ssh tunnel server username") String sshTargetUserName,
			@ShellOption(help = "ssh tunnel server password", defaultValue = "") String sshTargetPassword,
			@ShellOption(help = "path of private key", defaultValue = "~/.ssh/id_rsa") String keyFile)
			throws Exception {
		final String returnSessions = runCommandOnRemoteAgent(uniqueId, "list-sessions");
		logger.info("REMOTE SESSIONS " + returnSessions);
		final String command = "run-xpra-server";
		final String returnStartingXpra = runCommandOnRemoteAgent(uniqueId, command);
		logger.info("REMOTE XPRA " + returnStartingXpra);
		final int remoteXpraPort = Integer.valueOf(returnStartingXpra.replace("\n", ""));
		final String commandListXpra = "list-xpra-servers";
		final String returnListXpra = runCommandOnRemoteAgent(uniqueId, commandListXpra);
		logger.info("REMOTE XPRA LIST " + returnListXpra);
		final int localPort = NetworkHelper.findAvailablePort(14500);
		final String commandFindFreePort = "netstat -lant | grep tcp\\  | grep LISTEN\n";
		final String replyFindPortFreeOnServer = SshShellInterface.execCommandOnRemoteSshHost(keyFile,
				sshTargetUserName, sshTargetPassword, sshTargetHost, sshTargetPort, commandFindFreePort);
		logger.info("REMOTE PORT SSH REPLY " + commandFindFreePort);
		int serverSshPort = 0;
		int counterLoop = 0;
		while (replyFindPortFreeOnServer.contains(String.valueOf(serverSshPort)) && counterLoop < 50) {
			counterLoop++;
			serverSshPort = getRandomNumberInRange(40000, 50000);
			logger.info("try ssh port " + serverSshPort);
		}
		if (serverSshPort != 0) {
			final String remotePortCommand = "run-ssh-tunnel-remote-to-local-ssh "
					+ ((keyFile != null && !keyFile.isEmpty()) ? "--authkey " + keyFile : "")
					+ " --bindHost 127.0.0.1 --bindPort " + serverSshPort + " --host " + sshTargetHost + " --port "
					+ sshTargetPort
					+ ((sshTargetPassword != null && !sshTargetPassword.isEmpty()) ? " --password " + sshTargetPassword
							: "")
					+ " --redirectPort " + remoteXpraPort + " --redirectServer 127.0.0.1 --username "
					+ sshTargetUserName + " --name " + remoteXpraPort + "_" + serverSshPort + " --dataAddressPrefix "
					+ remoteXpraPort + "_" + serverSshPort;
			logger.info("RUN CMD SSH REMOTE -> " + remotePortCommand);
			final String returnSshTunnelRemoteToLocalSsh = runCommandOnRemoteAgent(uniqueId, remotePortCommand);
			logger.info("SSH REMOTE REPLY " + returnSshTunnelRemoteToLocalSsh);
			final SshLocalConfig sshLocalConfig = new SshLocalConfig();
			if (keyFile != null && !keyFile.isEmpty()) {
				sshLocalConfig.authkey = keyFile;
			}
			sshLocalConfig.username = sshTargetUserName;
			sshLocalConfig.host = sshTargetHost;
			sshLocalConfig.port = sshTargetPort;
			if (sshTargetPassword != null && !sshTargetPassword.isEmpty()) {
				sshLocalConfig.password = sshTargetPassword;
			}
			sshLocalConfig.bindHost = "127.0.0.1";
			sshLocalConfig.bindPort = localPort;
			sshLocalConfig.redirectServer = "127.0.0.1";
			sshLocalConfig.redirectPort = serverSshPort;
			logger.info("RUN CMD SSH LOCALE -> " + remotePortCommand);
			sshShellInterface.runSshTunnelLocalToRemoteSsh(sshLocalConfig);
			Thread.sleep(5000L);
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().browse(new URI("http://127.0.0.1:" + localPort));
			}
			return "xpra " + "attach " + "tcp:127.0.0.1:" + localPort + "\nhttp://127.0.0.1:" + localPort;
		} else {
			return "port not found on ssh server";
		}
	}

	@ShellMethod(value = "Create a tunnel from this machine to the remote one using a ssh bridge", group = "Tunnel Commands")
	@ManagedOperation
	@ShellMethodAvailability("testBeaconClientRunning")
	public String createSshMirrorTunnel(
			@ShellOption(help = "the unique ID of the remote agent", valueProvider = Ar4kRemoteAgentProvider.class) String uniqueId,
			@ShellOption(help = "agent port to bind", defaultValue = "22") int portToBind,
			@ShellOption(help = "ssh tunnel server host") String sshTargetHost,
			@ShellOption(help = "ssh tunnel server port", defaultValue = "22") int sshTargetPort,
			@ShellOption(help = "ssh tunnel server username") String sshTargetUserName,
			@ShellOption(help = "ssh tunnel server password", defaultValue = "") String sshTargetPassword,
			@ShellOption(help = "path of private key", defaultValue = "~/.ssh/id_rsa") String keyFile,
			@ShellOption(help = "remote agent username", defaultValue = "admin") String username,
			@ShellOption(help = "remote agent password") String password) throws Exception {
		final String commandLogin = "login --username " + username + " --password " + password;
		// final BeaconClient resolveBeaconClient = (BeaconClient)
		// resolveBeaconClient();
		logger.info("LOGIN: " + runCommandOnRemoteAgent(uniqueId, commandLogin));
		final String returnSessions = runCommandOnRemoteAgent(uniqueId, "list-sessions");
		logger.info("REMOTE SESSIONS " + returnSessions);
		final int localPort = NetworkHelper.findAvailablePort(14500);
		final String commandFindFreePort = "netstat -lant | grep tcp\\  | grep LISTEN\n";
		final String replyFindPortFreeOnServer = SshShellInterface.execCommandOnRemoteSshHost(keyFile,
				sshTargetUserName, sshTargetPassword, sshTargetHost, sshTargetPort, commandFindFreePort);
		logger.info("REMOTE PORT SSH REPLY " + commandFindFreePort);
		int serverSshPort = 0;
		int counterLoop = 0;
		while (replyFindPortFreeOnServer.contains(String.valueOf(serverSshPort)) && counterLoop < 50) {
			counterLoop++;
			serverSshPort = getRandomNumberInRange(40000, 50000);
			logger.info("try ssh port " + serverSshPort);
		}
		if (serverSshPort != 0) {
			final String remotePortCommand = "run-ssh-tunnel-remote-to-local-ssh "
					+ ((keyFile != null && !keyFile.isEmpty()) ? "--authkey " + keyFile : "")
					+ " --bindHost 127.0.0.1 --bindPort " + serverSshPort + " --host " + sshTargetHost + " --port "
					+ sshTargetPort
					+ ((sshTargetPassword != null && !sshTargetPassword.isEmpty()) ? " --password " + sshTargetPassword
							: "")
					+ " --redirectPort " + portToBind + " --redirectServer 127.0.0.1 --username " + sshTargetUserName
					+ " --name " + portToBind + "_" + serverSshPort + " --dataAddressPrefix " + portToBind + "_"
					+ serverSshPort;
			logger.info("RUN CMD SSH REMOTE -> " + remotePortCommand);
			final String returnSshTunnelRemoteToLocalSsh = runCommandOnRemoteAgent(uniqueId, remotePortCommand);
			logger.info("SSH REMOTE REPLY " + returnSshTunnelRemoteToLocalSsh);
			final SshLocalConfig sshLocalConfig = new SshLocalConfig();
			if (keyFile != null && !keyFile.isEmpty()) {
				sshLocalConfig.authkey = keyFile;
			}
			sshLocalConfig.username = sshTargetUserName;
			sshLocalConfig.host = sshTargetHost;
			sshLocalConfig.port = sshTargetPort;
			if (sshTargetPassword != null && !sshTargetPassword.isEmpty()) {
				sshLocalConfig.password = sshTargetPassword;
			}
			sshLocalConfig.bindHost = "127.0.0.1";
			sshLocalConfig.bindPort = localPort;
			sshLocalConfig.redirectServer = "127.0.0.1";
			sshLocalConfig.redirectPort = serverSshPort;
			logger.info("RUN CMD SSH LOCALE -> " + remotePortCommand);
			sshShellInterface.runSshTunnelLocalToRemoteSsh(sshLocalConfig);
			Thread.sleep(5000L);
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().browse(new URI("http://127.0.0.1:" + localPort));
			}
			return "done local port " + localPort + " is bound to the " + portToBind + " of " + uniqueId + "\n"
					+ " and the port " + serverSshPort + " is bound to the " + portToBind + " of " + uniqueId;
		} else {
			return "port not found on ssh server";
		}
	}

	@SuppressWarnings("unused")
	private String getOutput(Process process) {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String reply = null;
		try {
			if (reader.ready())
				reply = reader.lines().collect(Collectors.joining());
		} catch (final IOException e) {
			logger.logException(e);
		}
		return reply;
	}

	@SuppressWarnings("unused")
	private String getErrors(Process process) {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		String reply = null;
		try {
			if (reader.ready())
				reply = reader.lines().collect(Collectors.joining());
		} catch (final IOException e) {
			logger.logException(e);
		}
		return reply;
	}

	@ShellMethod(value = "Run Xpra server on local agent", group = "Tunnel Commands")
	@ManagedOperation
	@ShellMethodAvailability("testBeaconClientRunning")
	public int runXpraServer(
			@ShellOption(help = "label to identify the xpra server", defaultValue = "xpra-service") String executorLabel,
			@ShellOption(help = "the tcp port for the HTML5 console", defaultValue = "0") int port,
			@ShellOption(help = "the command to start in the X server", defaultValue = "xterm") String cmd)
			throws IOException, URISyntaxException {
		logger.info("Xpra server request -> port:" + port + " cmd:" + cmd);
		final XpraSessionProcess xpraSessionProcess = new XpraSessionProcess();
		xpraSessionProcess.setLabel(executorLabel);
		xpraSessionProcess.setTcpPort(port);
		xpraSessionProcess.setCommand(cmd);
		xpraSessionProcess.eval(cmd);
		resolveBeaconClient().getLocalExecutor().getScriptSessions().put(executorLabel, xpraSessionProcess);
		logger.info("Xpra server started on port " + xpraSessionProcess.getTcpPort());
		return xpraSessionProcess.getTcpPort();
	}

	@ShellMethod(value = "Connect to remote agent with ssh by Beacon tunnel", group = "Tunnel Commands")
	@ManagedOperation
	@ShellMethodAvailability("testBeaconClientRunning")
	public String connectToAgentOnStandardSsh(
			@ShellOption(help = "the unique ID of the remote agent", valueProvider = Ar4kRemoteAgentProvider.class) String uniqueId,
			@ShellOption(help = "ssh user to login on the agent", defaultValue = "root") String sshUser,
			@ShellOption(help = "ssh destination port", defaultValue = "22") String sshPort) throws Exception {
		final int localPort = NetworkHelper.findAvailablePort(14500);
		final NetworkConfig remoteConfig = new BeaconNetworkConfig("beacon-ssh-22", "tunnel ssh", NetworkMode.CLIENT,
				NetworkProtocol.TCP, "127.0.0.1", Integer.valueOf(sshPort), localPort);
		resolveBeaconClient().getNetworkTunnel(uniqueId, remoteConfig);
		Thread.sleep(5000L);
		return "ssh " + sshUser + "@127.0.0.1 " + "-p " + String.valueOf(localPort);
	}

	@ShellMethod(value = "Complete reload remote node", group = "Beacon Client Commands")
	@ManagedOperation
	@ShellMethodAvailability("testBeaconClientRunning")
	public String completeReloadRemoteAgent(
			@ShellOption(help = "target agent", valueProvider = Ar4kRemoteAgentProvider.class) String agentName) {
		return runCommandOnRemoteAgent(agentName, "complete-reload");
	}

	@ShellMethod(value = "Restart remote node", group = "Beacon Client Commands")
	@ManagedOperation
	@ShellMethodAvailability("testBeaconClientRunning")
	public String restartRemoteAgent(
			@ShellOption(help = "target agent", valueProvider = Ar4kRemoteAgentProvider.class) String agentName) {
		return runCommandOnRemoteAgent(agentName, "restart");
	}

	@ShellMethod(value = "send selected config to remote node", group = "Beacon Client Commands")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public String setSelectedConfigOnRemoteNode(
			@ShellOption(help = "target agent", valueProvider = Ar4kRemoteAgentProvider.class) String agentName) {
		anima.getBeaconClient().sendConfigToAgent(agentName, getWorkingConfig());
		return "sent";
	}

	private IBeaconClient resolveBeaconClient() {
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
			for (final Process p : openedProcess) {
				p.destroyForcibly();
			}
			openedProcess.clear();
		}

	}

}
