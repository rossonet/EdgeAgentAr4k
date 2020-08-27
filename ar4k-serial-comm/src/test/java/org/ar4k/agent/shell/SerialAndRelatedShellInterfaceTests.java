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
package org.ar4k.agent.shell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Anima.AnimaStates;
import org.ar4k.agent.core.AnimaHomunculus;
import org.ar4k.agent.core.AnimaStateMachineConfig;
import org.ar4k.agent.iot.serial.SerialShellInterface;
import org.ar4k.agent.iot.serial.cnc.CncShellInterface;
import org.ar4k.agent.iot.serial.json.SerialJsonShellInterface;
import org.ar4k.agent.iot.serial.marlin.MarlinShellInterface;
import org.ar4k.agent.spring.EdgeAuthenticationManager;
import org.ar4k.agent.spring.EdgekuserDetailsService;
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
@Import({ SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class, Anima.class,
		JCommanderParameterResolverAutoConfiguration.class, LegacyAdapterAutoConfiguration.class,
		StandardAPIAutoConfiguration.class, StandardCommandsAutoConfiguration.class, Commands.class,
		FileValueProvider.class, AnimaStateMachineConfig.class, AnimaHomunculus.class, EdgekuserDetailsService.class,
		EdgeAuthenticationManager.class, BCryptPasswordEncoder.class, SerialShellInterface.class,
		CncShellInterface.class, SerialJsonShellInterface.class, MarlinShellInterface.class })
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SerialAndRelatedShellInterfaceTests {

	@Autowired
	Shell shell;

	@Autowired
	Anima anima;

	@Before
	public void setUp() throws Exception {
		Thread.sleep(3000L);
		System.out.println(anima.getState());
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
		assertEquals(anima.getState(), AnimaStates.STAMINAL);
		final Map<String, MethodTarget> listCommands = shell.listCommands();
		System.out.println("commands: " + listCommands);
		assertTrue(listCommands.containsKey("add-cnc-service"));
		assertTrue(listCommands.containsKey("add-json-serial-service"));
		assertTrue(listCommands.containsKey("add-marlin-service"));
		assertTrue(listCommands.containsKey("add-serial-service"));
		assertTrue(listCommands.containsKey("clear"));
		assertTrue(listCommands.containsKey("create-serial-json-service"));
		assertTrue(listCommands.containsKey("create-serial-service"));
		assertTrue(listCommands.containsKey("exit"));
		assertTrue(listCommands.containsKey("help"));
		assertTrue(listCommands.containsKey("history"));
		assertTrue(listCommands.containsKey("list-serial-ports"));
		assertTrue(listCommands.containsKey("quit"));
		assertTrue(listCommands.containsKey("script"));
		assertTrue(listCommands.containsKey("serial-instance-json-stop"));
		assertTrue(listCommands.containsKey("serial-instance-stop"));
		assertTrue(listCommands.containsKey("stacktrace"));
		// printCheckNow(listCommands);
	}

	@SuppressWarnings("unused")
	private void printCheckNow(Map<String, MethodTarget> listCommands) {
		for (final String command : listCommands.keySet()) {
			System.out.println("assertTrue(listCommands.containsKey(\"" + command + "\"));");
		}

	}

}
