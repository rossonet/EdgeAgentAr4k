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

import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Anima.AnimaStates;
import org.ar4k.agent.core.AnimaHomunculus;
import org.ar4k.agent.core.AnimaStateMachineConfig;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.spring.Ar4kAuthenticationManager;
import org.ar4k.agent.spring.Ar4kuserDetailsService;
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
@Import({ SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class, Anima.class,
    JCommanderParameterResolverAutoConfiguration.class, LegacyAdapterAutoConfiguration.class,
    StandardAPIAutoConfiguration.class, StandardCommandsAutoConfiguration.class, Commands.class,
    FileValueProvider.class, AnimaStateMachineConfig.class, AnimaHomunculus.class, Ar4kuserDetailsService.class,
    Ar4kAuthenticationManager.class, BCryptPasswordEncoder.class })
@TestPropertySource(locations = "classpath:application-dns.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ConfigLoadingDns {

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
  public void checkConfigDns() throws InterruptedException, IOException {
    Thread.sleep(10000);
    assertEquals(anima.getState(), AnimaStates.RUNNING);
    assertTrue("prova56H1".equals(anima.getRuntimeConfig().author));
    assertTrue("dnsconfig".equals(anima.getRuntimeConfig().name));
    assertTrue("EF56T".equals(anima.getRuntimeConfig().tagVersion));
    System.out.println("NOTE 0 -> " + ((BeaconServiceConfig) anima.getRuntimeConfig().pots.toArray()[0]).note);
    assertTrue("345F".equals(((BeaconServiceConfig) anima.getRuntimeConfig().pots.toArray()[0]).note));
    System.out.println("NOTE 1 -> " + ((BeaconServiceConfig) anima.getRuntimeConfig().pots.toArray()[1]).note);
    assertTrue("345F".equals(((BeaconServiceConfig) anima.getRuntimeConfig().pots.toArray()[1]).note));
  }

  @Test
  public void createConfigDns() throws IOException {
    Ar4kConfig config = new Ar4kConfig();
    config.author = "prova56H1";
    config.name = "dnsconfig";
    config.tagVersion = "EF56T";
    BeaconServiceConfig s0 = new BeaconServiceConfig();
    s0.setNote("345F");
    s0.name = "socket-0";
    BeaconServiceConfig s1 = new BeaconServiceConfig();
    s1.setNote("345F");
    s1.name = "socket-1";
    config.pots.add(s0);
    config.pots.add(s1);
    System.out.println(ConfigHelper.toBase64ForDns("test-conf", config)); // bottegaio.net
  }

}
