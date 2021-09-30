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

import java.util.UUID;

import org.ar4k.agent.core.EdgeAgentCore;
import org.ar4k.agent.core.HomunculusSession;
import org.ar4k.agent.core.HomunculusStateMachineConfig;
import org.ar4k.agent.core.Homunculus.HomunculusEvents;
import org.ar4k.agent.spring.EdgeAuthenticationManager;
import org.ar4k.agent.spring.EdgeUserDetailsService;
import org.jline.builtins.Commands;
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
		EdgeUserDetailsService.class, EdgeAuthenticationManager.class, BCryptPasswordEncoder.class })
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DataStoreTests {

	@Autowired
	EdgeAgentCore edgeAgentCore;

	@Before
	public void setUp() throws Exception {
		Thread.sleep(3000L);
		System.out.println(edgeAgentCore.getState());
	}

	@Rule
	public TestWatcher watcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
		}
	};

	@Test
	public void putInDataStore() throws InterruptedException {
		Thread.sleep(2000L);
		while (edgeAgentCore == null || !edgeAgentCore.getState().toString().equals("STAMINAL")
				|| !edgeAgentCore.dataStoreExists()) {
			if (edgeAgentCore != null && edgeAgentCore.getState().toString().equals("INIT")) {
				edgeAgentCore.sendEvent(HomunculusEvents.BOOTSTRAP);
			}
			try {
				System.out.println("STATE A: " + edgeAgentCore.getState());
			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread.sleep(2000L);
		}
		String stringa = UUID.randomUUID().toString();
		boolean first = true;
		for (int i = 0; i < 10000; i++) {
			if (first) {
				edgeAgentCore.setContextData(stringa, UUID.randomUUID().toString());
				first = false;
			} else {
				edgeAgentCore.setContextData(UUID.randomUUID().toString(), UUID.randomUUID().toString());
			}
		}
		System.out.println("STATE B: " + edgeAgentCore.getContextData(stringa));
	}

}
