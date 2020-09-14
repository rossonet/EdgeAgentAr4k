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
import java.nio.file.StandardOpenOption;

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.Homunculus.HomunculusStates;
import org.ar4k.agent.core.HomunculusSession;
import org.ar4k.agent.core.HomunculusStateMachineConfig;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.spring.EdgeAuthenticationManager;
import org.ar4k.agent.spring.EdgeUserDetailsService;
import org.ar4k.agent.tunnels.http.beacon.BeaconServiceConfig;
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
@TestPropertySource(locations = "classpath:application-web.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ConfigLoadingWeb {

  private final String checkString = "f5463152-cd79-4e3d-915e-1e9a3b15633a";

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
  public void checkConfigWeb() throws InterruptedException, IOException {
    Thread.sleep(10000);
    assertEquals(homunculus.getState(), HomunculusStates.RUNNING);
    System.out.println("CONFIG -> " + ConfigHelper.toYaml(homunculus.getRuntimeConfig()));
    assertTrue(checkString.equals(homunculus.getRuntimeConfig().author));
    System.out.println("NOTE 0 -> " + ((BeaconServiceConfig) homunculus.getRuntimeConfig().pots.toArray()[0]).note);
    assertTrue(checkString.equals(((BeaconServiceConfig) homunculus.getRuntimeConfig().pots.toArray()[0]).note));
    System.out.println("NOTE 1 -> " + ((BeaconServiceConfig) homunculus.getRuntimeConfig().pots.toArray()[1]).note);
    assertTrue(checkString.equals(((BeaconServiceConfig) homunculus.getRuntimeConfig().pots.toArray()[1]).note));
  }

  @Test
  public void createConfigWeb() throws IOException {
    EdgeConfig config = new EdgeConfig();
    config.author = checkString;
    config.name = "config-web-test";
    BeaconServiceConfig s0 = new BeaconServiceConfig();
    s0.setNote(checkString);
    s0.name = "socket-0";
    BeaconServiceConfig s1 = new BeaconServiceConfig();
    s1.setNote(checkString);
    s1.name = "socket-1";
    config.pots.add(s0);
    config.pots.add(s1);
    Files.write(Paths.get("test-config.ar4k"), ConfigHelper.toBase64(config).getBytes(), StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING);
  }

}
