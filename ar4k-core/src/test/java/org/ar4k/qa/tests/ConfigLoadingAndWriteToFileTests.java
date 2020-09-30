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
import java.nio.file.Files;
import java.nio.file.Paths;

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.Homunculus.HomunculusEvents;
import org.ar4k.agent.core.Homunculus.HomunculusStates;
import org.ar4k.agent.core.HomunculusSession;
import org.ar4k.agent.core.HomunculusStateMachineConfig;
import org.ar4k.agent.helper.ConfigHelper;
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
    FileValueProvider.class, HomunculusStateMachineConfig.class, HomunculusSession.class, EdgeUserDetailsService.class,
    EdgeAuthenticationManager.class, BCryptPasswordEncoder.class })
@TestPropertySource(locations = "classpath:application-file-write.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ConfigLoadingAndWriteToFileTests {

  @Autowired
  Homunculus homunculus;

  final String fileName = "/tmp/test-config.ar4k";

  @Before
  public void setUp() throws Exception {
    Thread.sleep(3000L);
    System.out.println(homunculus.getState());
  }

  @After
  public void tearDownAfterClass() throws Exception {
    Files.deleteIfExists(Paths.get(fileName));
  }

  @Rule
  public TestWatcher watcher = new TestWatcher() {
    @Override
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }
  };

  @Test
  public void checkConfigFileWithReload() throws InterruptedException, IOException {
    Thread.sleep(3000);
    assertEquals(homunculus.getState(), HomunculusStates.RUNNING);
    Files.deleteIfExists(Paths.get(fileName));
    assertTrue(!Files.exists(Paths.get(fileName)));
    homunculus.sendEvent(HomunculusEvents.COMPLETE_RELOAD);
    Thread.sleep(30000);
    assertTrue(Files.exists(Paths.get(fileName)));
    System.out.println(homunculus.getState());
    assertEquals(homunculus.getState(), HomunculusStates.RUNNING);
    homunculus.sendEvent(HomunculusEvents.COMPLETE_RELOAD);
    Thread.sleep(10000);
  }

  @Test
  public void createConfigBase64() throws IOException {
    EdgeConfig config = new EdgeConfig();
    config.author = "andrea";
    config.name = "base-config-with-write";
    config.tagVersion = "tat345";
    config.nextConfigFile = fileName;
    config.updateFileConfig = true;
    System.out.println(ConfigHelper.toBase64(config));
  }

}
