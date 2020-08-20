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
package org.ar4k.agent.opcua;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Anima.AnimaEvents;
import org.ar4k.agent.core.AnimaHomunculus;
import org.ar4k.agent.core.AnimaStateMachineConfig;
import org.ar4k.agent.core.data.generator.DataGeneratorConfig;
import org.ar4k.agent.core.data.generator.SingleDataGeneratorPointConfig;
import org.ar4k.agent.core.data.generator.SingleDataGeneratorPointConfig.ChannelType;
import org.ar4k.agent.core.data.generator.SingleDataGeneratorPointConfig.DataGeneratorMode;
import org.ar4k.agent.core.data.generator.SingleDataGeneratorPointConfig.DataType;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.opcua.server.OpcUaServerConfig;
import org.ar4k.agent.spring.Ar4kAuthenticationManager;
import org.ar4k.agent.spring.Ar4kuserDetailsService;
import org.assertj.core.util.Lists;
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
		FileValueProvider.class, AnimaStateMachineConfig.class, AnimaHomunculus.class, Ar4kuserDetailsService.class,
		Ar4kAuthenticationManager.class, BCryptPasswordEncoder.class, OpcUaShellInterface.class })
@TestPropertySource(locations = "classpath:application-opc-ua.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class OpcUaServerTests {

	@Autowired
	Anima anima;

	@Autowired
	Shell shell;

	final String fileName = "/tmp/test-opc-server.ar4k";

	@Before
	public void setUp() throws Exception {
		Files.deleteIfExists(Paths.get(fileName));
		Thread.sleep(3000L);
		System.out.println(anima.getState());
	}

	@After
	public void tearDownAfterClass() throws Exception {
		Files.deleteIfExists(Paths.get(fileName));
		anima.close();
	}

	@Rule
	public TestWatcher watcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
		}
	};

	@Test
	public void startOpcServerTest() throws IOException, InterruptedException {
		final Ar4kConfig c = new Ar4kConfig();
		final String check = UUID.randomUUID().toString();
		c.name = "test opc server";
		c.author = check;
		final DataGeneratorConfig s1 = new DataGeneratorConfig();
		s1.name = "opc-server";
		final SingleDataGeneratorPointConfig sp = new SingleDataGeneratorPointConfig();
		sp.delta = 1;
		sp.description = "test point";
		sp.rangeHi = 10;
		sp.rangeLower = 5;
		sp.frequency = 500;
		sp.nodeId = "test-data";
		sp.namespace = "testing";
		sp.tags = Lists.list("test", "prova", "single-point", "opc-data");
		sp.typeChannel = ChannelType.PublishSubscribe;
		sp.typeData = DataType.LONG;
		sp.typeSimulator = DataGeneratorMode.RANDOM;
		s1.datas.add(sp);
		final OpcUaServerConfig opcConfig = new OpcUaServerConfig();
		c.pots.add(opcConfig);
		c.pots.add(s1);
		Files.write(Paths.get(fileName), ConfigHelper.toBase64(c).getBytes(), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
		anima.sendEvent(AnimaEvents.COMPLETE_RELOAD);
		Thread.sleep(3000);
		System.out.println(anima.getState());
		Thread.sleep(2000000);

	}

}
