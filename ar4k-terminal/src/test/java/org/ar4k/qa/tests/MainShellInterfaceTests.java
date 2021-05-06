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

import org.ar4k.agent.console.ShellInterface;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.Homunculus.HomunculusStates;
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
@Import({ SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class, Homunculus.class,
		JCommanderParameterResolverAutoConfiguration.class, LegacyAdapterAutoConfiguration.class,
		StandardAPIAutoConfiguration.class, StandardCommandsAutoConfiguration.class, Commands.class,
		FileValueProvider.class, HomunculusStateMachineConfig.class, HomunculusSession.class,
		EdgeUserDetailsService.class, EdgeAuthenticationManager.class, BCryptPasswordEncoder.class,
		ShellInterface.class })
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class MainShellInterfaceTests {

	@Autowired
	Shell shell;

	@Autowired
	Homunculus homunculus;

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
		assertTrue(listCommands.containsKey("add-mathermost-chat-service"));
		assertTrue(listCommands.containsKey("clear"));
		assertTrue(listCommands.containsKey("clone-config"));
		assertTrue(listCommands.containsKey("clone-runtime-config"));
		assertTrue(listCommands.containsKey("close-session-and-logout"));
		assertTrue(listCommands.containsKey("complete-reload"));
		assertTrue(listCommands.containsKey("create-selected-config"));
		assertTrue(listCommands.containsKey("create-user-account"));
		assertTrue(listCommands.containsKey("delete-user-account"));
		assertTrue(listCommands.containsKey("exit"));
		assertTrue(listCommands.containsKey("get-agent-status"));
		assertTrue(listCommands.containsKey("get-beans-info"));
		assertTrue(listCommands.containsKey("get-environment-variables"));
		assertTrue(listCommands.containsKey("get-free-port"));
		assertTrue(listCommands.containsKey("get-hardware-info"));
		assertTrue(listCommands.containsKey("get-homunculus"));
		assertTrue(listCommands.containsKey("get-log-level"));
		assertTrue(listCommands.containsKey("get-roles-authority"));
		assertTrue(listCommands.containsKey("get-runtime-config-json"));
		assertTrue(listCommands.containsKey("get-runtime-config-yaml"));
		assertTrue(listCommands.containsKey("get-selected-config-base64"));
		assertTrue(listCommands.containsKey("get-selected-config-base64crypto"));
		assertTrue(listCommands.containsKey("get-selected-config-for-dns"));
		assertTrue(listCommands.containsKey("get-selected-config-for-dns-encrypted"));
		assertTrue(listCommands.containsKey("get-selected-config-json"));
		assertTrue(listCommands.containsKey("get-selected-config-yaml"));
		assertTrue(listCommands.containsKey("get-storage-drivers"));
		assertTrue(listCommands.containsKey("get-threads-info"));
		assertTrue(listCommands.containsKey("get-unique-name"));
		assertTrue(listCommands.containsKey("get-users-list"));
		assertTrue(listCommands.containsKey("goodbye"));
		assertTrue(listCommands.containsKey("help"));
		assertTrue(listCommands.containsKey("history"));
		assertTrue(listCommands.containsKey("import-selected-config-base64"));
		assertTrue(listCommands.containsKey("import-selected-config-base64crypted"));
		assertTrue(listCommands.containsKey("import-selected-config-json"));
		assertTrue(listCommands.containsKey("import-selected-config-yaml"));
		assertTrue(listCommands.containsKey("kill-process"));
		assertTrue(listCommands.containsKey("list-configs"));
		assertTrue(listCommands.containsKey("list-jmx-endpoints"));
		assertTrue(listCommands.containsKey("list-jsr223script-engines-in-runtime"));
		assertTrue(listCommands.containsKey("list-processes"));
		assertTrue(listCommands.containsKey("list-services"));
		assertTrue(listCommands.containsKey("list-services-selected-config"));
		assertTrue(listCommands.containsKey("list-sessions"));
		assertTrue(listCommands.containsKey("load-selected-config-base64"));
		assertTrue(listCommands.containsKey("load-selected-config-base64crypted"));
		assertTrue(listCommands.containsKey("load-selected-config-from-json-string"));
		assertTrue(listCommands.containsKey("load-selected-config-from-yaml-string"));
		assertTrue(listCommands.containsKey("load-selected-config-json"));
		assertTrue(listCommands.containsKey("load-selected-config-yaml"));
		assertTrue(listCommands.containsKey("login"));
		assertTrue(listCommands.containsKey("logout"));
		assertTrue(listCommands.containsKey("me"));
		assertTrue(listCommands.containsKey("pause"));
		assertTrue(listCommands.containsKey("quit"));
		assertTrue(listCommands.containsKey("remove-service-selected-config"));
		assertTrue(listCommands.containsKey("restart"));
		assertTrue(listCommands.containsKey("run-command-line"));
		assertTrue(listCommands.containsKey("run-jsr223script"));
		assertTrue(listCommands.containsKey("save-application-properties-template-as-bootstrap"));
		assertTrue(listCommands.containsKey("save-selected-config-as-bootstrap-config"));
		assertTrue(listCommands.containsKey("save-selected-config-base64"));
		assertTrue(listCommands.containsKey("save-selected-config-base64crypto"));
		assertTrue(listCommands.containsKey("save-selected-config-json"));
		assertTrue(listCommands.containsKey("save-selected-config-to-remote"));
		assertTrue(listCommands.containsKey("save-selected-config-yaml"));
		assertTrue(listCommands.containsKey("script"));
		assertTrue(listCommands.containsKey("select-config"));
		assertTrue(listCommands.containsKey("send-log-message-error"));
		assertTrue(listCommands.containsKey("send-log-message-info"));
		assertTrue(listCommands.containsKey("set-agent-status"));
		assertTrue(listCommands.containsKey("set-log-level"));
		assertTrue(listCommands.containsKey("set-selected-config-as-runtime"));
		assertTrue(listCommands.containsKey("stacktrace"));
		assertTrue(listCommands.containsKey("unset-selected-config"));
	}

	private void printCheckNow(Map<String, MethodTarget> listCommands) {
		for (final String command : listCommands.keySet()) {
			System.out.println("assertTrue(listCommands.containsKey(\"" + command + "\"));");
		}

	}

}
