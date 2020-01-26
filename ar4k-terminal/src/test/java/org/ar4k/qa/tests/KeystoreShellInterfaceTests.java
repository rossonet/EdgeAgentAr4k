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

import org.ar4k.agent.console.KeystoreShellInterface;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Anima.AnimaStates;
import org.ar4k.agent.core.AnimaHomunculus;
import org.ar4k.agent.core.AnimaStateMachineConfig;
import org.ar4k.agent.spring.Ar4kAuthenticationManager;
import org.ar4k.agent.spring.Ar4kuserDetailsService;
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
    FileValueProvider.class, AnimaStateMachineConfig.class, AnimaHomunculus.class, Ar4kuserDetailsService.class,
    Ar4kAuthenticationManager.class, BCryptPasswordEncoder.class, KeystoreShellInterface.class })
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class KeystoreShellInterfaceTests {

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
    Map<String, MethodTarget> listCommands = shell.listCommands();
    System.out.println("commands: " + listCommands);
    assertTrue(listCommands.containsKey("add-keystore"));
    assertTrue(listCommands.containsKey("add-keystore-runtime"));
    assertTrue(listCommands.containsKey("check-keystore"));
    assertTrue(listCommands.containsKey("clear"));
    assertTrue(listCommands.containsKey("create-self-signed-cert"));
    assertTrue(listCommands.containsKey("create-self-signed-cert-and-sign"));
    assertTrue(listCommands.containsKey("exit"));
    assertTrue(listCommands.containsKey("get-client-certificate-base64"));
    assertTrue(listCommands.containsKey("get-keystore-for-dns"));
    assertTrue(listCommands.containsKey("get-pkcs10certification-request-base64"));
    assertTrue(listCommands.containsKey("get-private-key-base64"));
    assertTrue(listCommands.containsKey("help"));
    assertTrue(listCommands.containsKey("history"));
    assertTrue(listCommands.containsKey("initialize-keystore-ca"));
    assertTrue(listCommands.containsKey("list-keys-in-keystore"));
    assertTrue(listCommands.containsKey("list-keystore-keys"));
    assertTrue(listCommands.containsKey("list-keystores"));
    assertTrue(listCommands.containsKey("quit"));
    assertTrue(listCommands.containsKey("script"));
    assertTrue(listCommands.containsKey("set-client-key-pair"));
    assertTrue(listCommands.containsKey("sign-certificate-base64"));
    assertTrue(listCommands.containsKey("stacktrace"));
    assertTrue(listCommands.containsKey("view-key-in-keystore"));
    // printCheckNow(listCommands);
  }

  @SuppressWarnings("unused")
  private void printCheckNow(Map<String, MethodTarget> listCommands) {
    for (String command : listCommands.keySet()) {
      System.out.println("assertTrue(listCommands.containsKey(\"" + command + "\"));");
    }

  }

}
