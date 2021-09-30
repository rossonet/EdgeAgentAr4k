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
import java.util.List;

import javax.validation.Valid;

import org.apache.sshd.common.session.helpers.AbstractSession;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.ar4k.agent.core.EdgeAgentCore;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.tunnels.sshd.HomunculusShellFactory;
import org.ar4k.agent.tunnels.sshd.SshdHomunculusConfig;
import org.ar4k.agent.tunnels.sshd.SshdSystemConfig;
import org.ar4k.agent.tunnels.sshd.firstCommand.HomunculusProcessShellFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.shell.Availability;
import org.springframework.shell.Shell;
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
 *         Interfaccia gestione servizi rpc via ssh.
 */

@ShellCommandGroup("Ssh Server Commands")
@ShellComponent
@RestController
@RequestMapping("/sshdInterface")
public class SshdShellInterface extends AbstractShellHelper {
	@Autowired
	Shell shell;

	SshServer server = null;

	@SuppressWarnings("unused")
	private Availability testClientUsed() {
		return server == null ? Availability.available()
				: Availability.unavailable("the sshd server is running, please terminate before");
	}

	@SuppressWarnings("unused")
	private Availability testClientFree() {
		return server != null ? Availability.available() : Availability.unavailable("the sshd server is not running");
	}

	@ShellMethod(value = "Add a sshd remote management server to the selected configuration that replies with the agent RPC", group = "Ssh Server Commands")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addSshdManagerToSelectedConfig(@ShellOption(optOut = true) @Valid SshdHomunculusConfig service) {
		getWorkingConfig().pots.add(service);
	}

	@ShellMethod(value = "Add a sshd remote management server to the selected configuration that replies with the system shell", group = "Ssh Server Commands")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addSshdManagerSystemToSelectedConfig(@ShellOption(optOut = true) @Valid SshdSystemConfig service) {
		getWorkingConfig().pots.add(service);
	}

	@ShellMethod(value = "Start SSHD remote management server that use local bash process", group = "Ssh Server Commands")
	@ManagedOperation
	@ShellMethodAvailability("testClientUsed")
	public void startSshdRemoteSystemShell(
			@ShellOption(help = "the sshd server host", defaultValue = "0.0.0.0") String host,
			@ShellOption(help = "the sshd server port", defaultValue = "6666") int port) {
		server = SshServer.setUpDefaultServer();
		server.setHost(host);
		server.setPort(port);
		server.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
		ProcessShellFactory shellFactory = new HomunculusProcessShellFactory(new String[] { "/bin/bash", "-i", "-l" });
		server.setShellFactory(shellFactory);
		try {
			server.start();
		} catch (IOException e) {
			logger.logException(e);
		}
	}

	@ShellMethod(value = "Start SSHD remote management server that use Homunculus shell", group = "Ssh Server Commands")
	@ManagedOperation
	@ShellMethodAvailability("testClientUsed")
	public void startSshdRemoteHomunculus(
			@ShellOption(help = "the sshd server host", defaultValue = "0.0.0.0") String host,
			@ShellOption(help = "the sshd server port", defaultValue = "6666") int port) {
		server = SshServer.setUpDefaultServer();
		server.setHost(host);
		server.setPort(port);
		server.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
		HomunculusShellFactory shellFactory = new HomunculusShellFactory(
				EdgeAgentCore.getApplicationContextStatic().getBean(EdgeAgentCore.class), this.shell);
		server.setShellFactory(shellFactory);
		try {
			server.start();
		} catch (IOException e) {
			logger.logException(e);
		}
	}

	@ShellMethod(value = "List active SSHD remote management connections", group = "Ssh Server Commands")
	@ManagedOperation
	@ShellMethodAvailability("testClientFree")
	public List<AbstractSession> listSshdRemoteManager() {
		return server.getActiveSessions();
	}

	@ShellMethod(value = "Stop SSHD remote management connection", group = "Ssh Server Commands")
	@ManagedOperation
	@ShellMethodAvailability("testClientFree")
	public void stopSshdRemoteManager() {
		try {
			server.stop();
		} catch (IOException e) {
			logger.logException(e);
		}
		server = null;
	}

}
