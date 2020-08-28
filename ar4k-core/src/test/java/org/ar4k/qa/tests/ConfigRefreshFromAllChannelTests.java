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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Anima.AnimaEvents;
import org.ar4k.agent.core.Anima.AnimaStates;
import org.ar4k.agent.core.AnimaHomunculus;
import org.ar4k.agent.core.AnimaStateMachineConfig;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.spring.EdgeAuthenticationManager;
import org.ar4k.agent.spring.EdgekuserDetailsService;
import org.jline.builtins.Commands;
import org.joda.time.Instant;
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
		EdgeAuthenticationManager.class, BCryptPasswordEncoder.class })
@TestPropertySource(locations = "classpath:application-file.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ConfigRefreshFromAllChannelTests {

	@Autowired
	Anima anima;

	final String fileName = "/tmp/test-config.ar4k";
	final String fileNameSecond = "/tmp/test-second-config.ar4k";
	final String fileNameEnd = "/tmp/test-end-config.ar4k";
	final String webConfig = "https://www.rossonet.net/dati/ar4k/config-to-file.ar4k";
	final String dnsConfig = "config-to-web.bottegaio.net";

	@Before
	public void setUp() throws Exception {
		Thread.sleep(3000L);
		System.out.println(anima.getState());
	}

	private void deleteDir(File dir) {
		final File[] files = dir.listFiles();
		if (files != null) {
			for (final File file : files) {
				deleteDir(file);
			}
		}
		dir.delete();
	}

	@After
	public void tearDownAfterClass() throws Exception {
		Files.deleteIfExists(Paths.get(fileName));
		Files.deleteIfExists(Paths.get(fileNameSecond));
		Files.deleteIfExists(Paths.get(fileNameEnd));
		deleteDir(new File("./tmp"));
	}

	@Rule
	public TestWatcher watcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
		}
	};

	@Test
	public void checkConfigNextInAllChannelWithReloadAndRestart() throws InterruptedException, IOException {
		Thread.sleep(3000);
		final EdgeConfig c1 = new EdgeConfig();
		final String check = UUID.randomUUID().toString();
		c1.name = "test aggiornamento configurazione";
		c1.creationDate = Instant.ofEpochMilli(1452797215000L);
		c1.lastUpdate = Instant.ofEpochMilli(1452797215000L);
		c1.nextConfigDns = dnsConfig;
		assertEquals(AnimaStates.STAMINAL, anima.getState());
		Files.write(Paths.get(fileName), ConfigHelper.toBase64(c1).getBytes(), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
		anima.sendEvent(AnimaEvents.COMPLETE_RELOAD);
		Thread.sleep(80000);
		assertEquals(AnimaStates.RUNNING, anima.getState());
		assertEquals("dnsToFile", anima.getRuntimeConfig().name);
		assertEquals(fileNameSecond, anima.getRuntimeConfig().nextConfigFile);
		final EdgeConfig c2 = new EdgeConfig();
		c2.name = "test aggiornamento configurazione";
		c2.tagVersion = check;
		c2.nextConfigFile = fileNameEnd;
		Files.write(Paths.get(fileNameSecond), ConfigHelper.toBase64(c2).getBytes(), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
		Thread.sleep(30000);
		assertEquals(AnimaStates.RUNNING, anima.getState());
		assertEquals(anima.getRuntimeConfig().tagVersion, check);
		final EdgeConfig c3 = new EdgeConfig();
		c3.name = "ultima configurazione";
		c3.author = check;
		c3.nextConfigReload = true;
		Thread.sleep(30000);
		Files.write(Paths.get(fileNameEnd), ConfigHelper.toBase64(c3).getBytes(), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
		Thread.sleep(60000);
		assertEquals(AnimaStates.RUNNING, anima.getState());
		assertEquals(check, anima.getRuntimeConfig().author);
	}

	@Test
	public void createConfigWeb() throws IOException {
		final EdgeConfig config = new EdgeConfig();
		config.name = "dnsToFile";
		config.nextConfigFile = fileNameSecond;
		final Path path = Paths.get("config-to-file.ar4k");
		Files.write(path, ConfigHelper.toBase64(config).getBytes(), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
		assertTrue(Files.exists(path));
	}

	@Test
	public void createConfigDns() throws IOException {
		final EdgeConfig config = new EdgeConfig();
		config.name = "DnsToWeb";
		config.nextConfigWeb = webConfig;
		final String base64ForDns = ConfigHelper.toBase64ForDns("config-to-web", config);
		System.out.println(base64ForDns); // bottegaio.net
		assertTrue(base64ForDns.length() > 20);
	}

}
