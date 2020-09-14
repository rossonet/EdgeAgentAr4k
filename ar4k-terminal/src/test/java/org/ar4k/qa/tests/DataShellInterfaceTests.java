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

import org.ar4k.agent.console.DataShellInterface;
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
		DataShellInterface.class })
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DataShellInterfaceTests {

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
		assertTrue(listCommands.containsKey("add-data-direct-channel"));
		assertTrue(listCommands.containsKey("add-data-executor-channel"));
		assertTrue(listCommands.containsKey("add-data-priority-channel"));
		assertTrue(listCommands.containsKey("add-data-pub-sub-channel"));
		assertTrue(listCommands.containsKey("add-data-queue-channel"));
		assertTrue(listCommands.containsKey("add-data-rendezvous-channel"));
		assertTrue(listCommands.containsKey("clear"));
		assertTrue(listCommands.containsKey("clear-data-channels-in-address-space"));
		assertTrue(listCommands.containsKey("exit"));
		assertTrue(listCommands.containsKey("get-data-channel-details"));
		assertTrue(listCommands.containsKey("help"));
		assertTrue(listCommands.containsKey("history"));
		assertTrue(listCommands.containsKey("list-data-channels"));
		assertTrue(listCommands.containsKey("list-spring-data-channels"));
		assertTrue(listCommands.containsKey("poll-data-channel"));
		assertTrue(listCommands.containsKey("quit"));
		assertTrue(listCommands.containsKey("remove-data-channel"));
		assertTrue(listCommands.containsKey("script"));
		assertTrue(listCommands.containsKey("send-to-data-channel"));
		assertTrue(listCommands.containsKey("stacktrace"));
		assertTrue(listCommands.containsKey("subscribe-data-channel"));
		assertTrue(listCommands.containsKey("unsubscribe-data-channel"));
		printCheckNow(listCommands);
	}

	@SuppressWarnings("unused")
	private void printCheckNow(Map<String, MethodTarget> listCommands) {
		for (final String command : listCommands.keySet()) {
			System.out.println("assertTrue(listCommands.containsKey(\"" + command + "\"));");
		}

	}

}
