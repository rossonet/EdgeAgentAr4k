/*
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
package org.ar4k.qa.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.ar4k.agent.console.BeaconShellInterface;
import org.ar4k.agent.console.SshShellInterface;
import org.ar4k.agent.core.Homunculus.HomunculusStates;
import org.ar4k.agent.core.EdgeAgentCore;
import org.ar4k.agent.core.HomunculusSession;
import org.ar4k.agent.core.HomunculusStateMachineConfig;
import org.ar4k.agent.spring.EdgeAuthenticationManager;
import org.ar4k.agent.spring.EdgeUserDetailsService;
import org.jline.builtins.Commands;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.shell.MethodTarget;
import org.springframework.shell.Shell;
import org.springframework.shell.SpringShellAutoConfiguration;
import org.springframework.shell.jcommander.JCommanderParameterResolverAutoConfiguration;
import org.springframework.shell.jline.JLineShellAutoConfiguration;
import org.springframework.shell.legacy.LegacyAdapterAutoConfiguration;
import org.springframework.shell.standard.FileValueProvider;
import org.springframework.shell.standard.StandardAPIAutoConfiguration;
import org.springframework.shell.standard.commands.StandardCommandsAutoConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({ SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class, EdgeAgentCore.class,
		JCommanderParameterResolverAutoConfiguration.class, LegacyAdapterAutoConfiguration.class,
		StandardAPIAutoConfiguration.class, StandardCommandsAutoConfiguration.class, Commands.class,
		FileValueProvider.class, HomunculusStateMachineConfig.class, HomunculusSession.class,
		EdgeUserDetailsService.class, EdgeAuthenticationManager.class, BCryptPasswordEncoder.class,
		BeaconShellInterface.class, SshShellInterface.class })
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class BeaconShellInterfaceTests {

	@Autowired
	Shell shell;

	@Autowired
	EdgeAgentCore homunculus;

	@Before
	public void setUp() throws Exception {
		Thread.sleep(3000L);
		System.out.println(homunculus.getState());
	}

	@After
	public void tearDownAfterClass() throws Exception {

	}

	@Rule
	public TestWatcher watcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
		}
	};

	@Test
	public void listCommandAndCheck() throws InterruptedException, IOException {
		Thread.sleep(10000);
		assertEquals(homunculus.getState(), HomunculusStates.STAMINAL);
		final Map<String, MethodTarget> listCommands = shell.listCommands();
		System.out.println("commands: " + listCommands);
		printCheckNow(listCommands);
		assertTrue(listCommands.containsKey("add-beacon-service"));
		assertTrue(listCommands.containsKey("add-ssh-tunnel-local-port-to-remote"));
		assertTrue(listCommands.containsKey("add-ssh-tunnel-remote-port-to-locale"));
		assertTrue(listCommands.containsKey("approve-provisioning-request"));
		assertTrue(listCommands.containsKey("clear"));
		assertTrue(listCommands.containsKey("complete-reload-remote-agent"));
		assertTrue(listCommands.containsKey("connect-to-agent-on-standard-ssh"));
		assertTrue(listCommands.containsKey("connect-to-beacon-service"));
		assertTrue(listCommands.containsKey("create-beacon-tunnel-classic"));
		assertTrue(listCommands.containsKey("create-beacon-tunnel-netty"));
		assertTrue(listCommands.containsKey("create-ssh-mirror-tunnel"));
		assertTrue(listCommands.containsKey("exit"));
		assertTrue(listCommands.containsKey("help"));
		assertTrue(listCommands.containsKey("history"));
		assertTrue(listCommands.containsKey("list-beacon-agents"));
		assertTrue(listCommands.containsKey("list-beacon-agents-connected"));
		assertTrue(listCommands.containsKey("list-beacon-agents-human-readable"));
		assertTrue(listCommands.containsKey("list-beacon-registrations"));
		assertTrue(listCommands.containsKey("list-beacon-tunnels"));
		assertTrue(listCommands.containsKey("list-commands-on-remote-agent"));
		assertTrue(listCommands.containsKey("list-provisioning-requests"));
		assertTrue(listCommands.containsKey("list-ssh-tunnels"));
		assertTrue(listCommands.containsKey("quit"));
		assertTrue(listCommands.containsKey("remove-ssh-tunnels"));
		assertTrue(listCommands.containsKey("restart-remote-agent"));
		assertTrue(listCommands.containsKey("run-beacon-server"));
		assertTrue(listCommands.containsKey("run-command-on-remote-agent"));
		assertTrue(listCommands.containsKey("run-ssh-tunnel-local-to-remote-ssh"));
		assertTrue(listCommands.containsKey("run-ssh-tunnel-remote-to-local-ssh"));
		assertTrue(listCommands.containsKey("script"));
		assertTrue(listCommands.containsKey("set-selected-config-on-remote-node"));
		assertTrue(listCommands.containsKey("stacktrace"));
		assertTrue(listCommands.containsKey("stop-beacon-client"));
		assertTrue(listCommands.containsKey("stop-beacon-server"));
	}

	private void printCheckNow(Map<String, MethodTarget> listCommands) {
		for (final String command : listCommands.keySet()) {
			System.out.println("assertTrue(listCommands.containsKey(\"" + command + "\"));");
		}

	}

}
