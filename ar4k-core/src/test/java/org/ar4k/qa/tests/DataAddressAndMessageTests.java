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

import java.io.IOException;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.HomunculusSession;
import org.ar4k.agent.core.HomunculusStateMachineConfig;
import org.ar4k.agent.core.data.channels.INoDataChannel;
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
@Import({ SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class, Homunculus.class,
    JCommanderParameterResolverAutoConfiguration.class, LegacyAdapterAutoConfiguration.class,
    StandardAPIAutoConfiguration.class, StandardCommandsAutoConfiguration.class, Commands.class,
    FileValueProvider.class, HomunculusStateMachineConfig.class, HomunculusSession.class, EdgeUserDetailsService.class,
    EdgeAuthenticationManager.class, BCryptPasswordEncoder.class })
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DataAddressAndMessageTests {

  @Autowired
  Homunculus homunculus;

  @Before
  public void setUp() throws Exception {
    Thread.sleep(3000L);
    System.out.println(homunculus.getState());
  }

  @Rule
  public TestWatcher watcher = new TestWatcher() {
    @Override
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }
  };

  @Test
  public void checkTreeTest() throws InterruptedException, IOException {
    Thread.sleep(2000L);
    String scope = "test-scope";
    INoDataChannel root = new INoDataChannel();
    root.setBrowseName("root");
    INoDataChannel a = new INoDataChannel();
    a.setBrowseName("a");
    INoDataChannel b = new INoDataChannel();
    b.setBrowseName("b");
    INoDataChannel c = new INoDataChannel();
    c.setBrowseName("c");
    INoDataChannel a1 = new INoDataChannel();
    a1.setBrowseName("a1");
    INoDataChannel a2 = new INoDataChannel();
    a2.setBrowseName("a2");
    INoDataChannel a3 = new INoDataChannel();
    a3.setBrowseName("a3");
    INoDataChannel b1 = new INoDataChannel();
    b1.setBrowseName("b1");
    INoDataChannel b2 = new INoDataChannel();
    b2.setBrowseName("b2");
    INoDataChannel b2a = new INoDataChannel();
    b2a.setBrowseName("b2a");
    INoDataChannel b2b = new INoDataChannel();
    b2b.setBrowseName("b2b");
    INoDataChannel b2c = new INoDataChannel();
    b2c.setBrowseName("b2c");
    b2c.setFatherOfScope(scope, b2);
    b2b.setFatherOfScope(scope, b2);
    b2a.setFatherOfScope(scope, b2);
    b1.setFatherOfScope(scope, b);
    b2.setFatherOfScope(scope, b);
    a1.setFatherOfScope(scope, a);
    a2.setFatherOfScope(scope, a);
    a3.setFatherOfScope(scope, a);
    a.setFatherOfScope(scope, root);
    b.setFatherOfScope(scope, root);
    c.setFatherOfScope(scope, root);
    System.out.println("\n\nPrint string:");
    System.out.println(root.getScopeTreeChildren(scope, 10).toString());
    System.out.println("\n\nPrint json:");
    System.out.println(root.getScopeTreeChildren(scope, 10).toJson().toString(2));
    root.close();
    a.close();
    b.close();
    c.close();
    a1.close();
    a2.close();
    a3.close();
    b1.close();
    b2.close();
    b2a.close();
    b2b.close();
    b2c.close();
  }
}
