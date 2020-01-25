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
package org.ar4k.tests.shellWeb;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.ar4k.agent.core.Anima;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.shell.Input;
import org.springframework.shell.InputProvider;
import org.springframework.shell.MethodTarget;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

//TODO TEST test concole da implementare -> TunnelTest
@RunWith(SpringRunner.class)
@SpringBootTest(properties = { ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false",
    InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false" })
@ComponentScan("org.ar4k.agent")
//@Import(TestApplicationRunner.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Ignore
public class TunnelTest {

  @Autowired
  private Shell shell;

  @Autowired
  Anima anima;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Rule
  public TestWatcher watcher = new TestWatcher() {
    @Override
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }
  };

  @Test
  public void helpTest() {
    boolean ok = false;
    try {
      Map<String, MethodTarget> commands = shell.listCommands();
      System.out.println("\nCOMMANDS\n---------------------------\n" + String.join(", ", commands.keySet()));
      List<String> comandi = new ArrayList<>();
      comandi.add("help");
      for (String hi : commands.keySet()) {
        comandi.add("help " + hi);
      }
      testCommands(comandi);
      ok = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertTrue(ok);
  }

  @Test
  @Ignore
  public void testActivationAr4kNet() throws InterruptedException {
    boolean ok = false;
    try {
      List<String> comandi = new ArrayList<>();
      List<String> post = new ArrayList<>();
      String labelTest = UUID.randomUUID().toString();
      String ksFile = labelTest + ".keystore";
      comandi.add("add-keystore --label " + labelTest + " --filePath " + ksFile);
      comandi.add("initialize-keystore-ca --keystore-label " + labelTest);
      comandi.add("create-selected-config --name test_stunnel");
      comandi.add("add-inet-network-point --ipHost ipa.ar4k.net --port 443 --name ipa_endpoint");
      comandi.add("add-inet-network-point --ipHost errante.lab.ar4k.net --port 22 --name errante");
      comandi.add(
          "add-stunnel-service --connectionSock ipa_endpoint --redirectServer 127.0.0.1 --redirectPort 2200 --keystoreAuth "
              + labelTest + " --keystoreTrust " + labelTest + " --name test_stunnel_service");
      comandi.add(
          "add-ssh-network-point --name ssh_test --connectionSock errante --bindHost localhost --bindPort 5676 --username ombra --password ciaone");
      comandi.add("set-selected-config-as-runtime");
      post.add("get-ar4k-agent-status");
      post.add("list-tunnels-selected-config");
      post.add("list-tunnels-runtime");
      testCommands(comandi);
      Thread.sleep(3000L);
      testCommands(post);
      Thread.sleep(30000L);
      testCommands(post);
      ok = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertTrue(ok);
  }

  @Test
  @Ignore
  public void testActivationProxy() throws InterruptedException {
    boolean ok = false;
    try {
      List<String> comandi = new ArrayList<>();
      List<String> post = new ArrayList<>();
      String labelTest = UUID.randomUUID().toString();
      String ksFile = labelTest + ".keystore";
      comandi.add("add-keystore --label " + labelTest + " --filePath " + ksFile);
      comandi.add("initialize-keystore-ca --keystore-label " + labelTest);
      comandi.add("create-selected-config --name test_stunnel");
      comandi.add("add-inet-network-point --ipHost ipa.ar4k.net --port 443 --name ipa_endpoint");
      comandi.add("add-inet-network-point --ipHost errante.lab.ar4k.net --port 22 --name errante");
      comandi.add(
          "add-stunnel-service --connectionSock ipa_endpoint --redirectServer 127.0.0.1 --redirectPort 2200 --keystoreAuth "
              + labelTest + " --keystoreTrust " + labelTest + " --name test_stunnel_service");
      comandi.add(
          "add-ssh-network-point --name ssh_test --connectionSock errante --bindHost localhost --bindPort 5676 --username ombra --password ciaone");
      comandi.add("set-selected-config-as-runtime");
      post.add("get-ar4k-agent-status");
      post.add("list-tunnels-selected-config");
      post.add("list-tunnels-runtime");
      testCommands(comandi);
      Thread.sleep(3000L);
      testCommands(post);
      Thread.sleep(30000L);
      testCommands(post);
      ok = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertTrue(ok);
  }

  @Test
  @Ignore
  public void testActivationSsh() throws InterruptedException {
    boolean ok = false;
    try {
      List<String> comandi = new ArrayList<>();
      List<String> post = new ArrayList<>();
      String labelTest = UUID.randomUUID().toString();
      String ksFile = labelTest + ".keystore";
      comandi.add("add-keystore --label " + labelTest + " --filePath " + ksFile);
      comandi.add("initialize-keystore-ca --keystore-label " + labelTest);
      comandi.add("create-selected-config --name test_stunnel");
      comandi.add("add-inet-network-point --ipHost ipa.ar4k.net --port 443 --name ipa_endpoint");
      comandi.add("add-inet-network-point --ipHost errante.lab.ar4k.net --port 22 --name errante");
      comandi.add(
          "add-stunnel-service --connectionSock ipa_endpoint --redirectServer 127.0.0.1 --redirectPort 2200 --keystoreAuth "
              + labelTest + " --keystoreTrust " + labelTest + " --name test_stunnel_service");
      comandi.add(
          "add-ssh-network-point --name ssh_test --connectionSock errante --bindHost localhost --bindPort 5676 --username ombra --password ciaone");
      comandi.add("set-selected-config-as-runtime");
      post.add("get-ar4k-agent-status");
      post.add("list-tunnels-selected-config");
      post.add("list-tunnels-runtime");
      testCommands(comandi);
      Thread.sleep(3000L);
      testCommands(post);
      Thread.sleep(30000L);
      testCommands(post);
      ok = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertTrue(ok);
  }

  @Test
  @Ignore
  public void testActivationStunnel() throws InterruptedException {
    boolean ok = false;
    try {
      List<String> comandi = new ArrayList<>();
      List<String> post = new ArrayList<>();
      String labelTest = UUID.randomUUID().toString();
      String ksFile = labelTest + ".keystore";
      comandi.add("add-keystore --label " + labelTest + " --filePath " + ksFile);
      comandi.add("initialize-keystore-ca --keystore-label " + labelTest);
      comandi.add("create-selected-config --name test_stunnel");
      comandi.add("add-inet-network-point --ipHost ipa.ar4k.net --port 443 --name ipa_endpoint");
      comandi.add("add-inet-network-point --ipHost errante.lab.ar4k.net --port 22 --name errante");
      comandi.add(
          "add-stunnel-service --connectionSock ipa_endpoint --redirectServer 127.0.0.1 --redirectPort 2200 --keystoreAuth "
              + labelTest + " --keystoreTrust " + labelTest + " --name test_stunnel_service");
      comandi.add(
          "add-ssh-network-point --name ssh_test --connectionSock errante --bindHost localhost --bindPort 5676 --username ombra --password ciaone");
      comandi.add("set-selected-config-as-runtime");
      post.add("get-ar4k-agent-status");
      post.add("list-tunnels-selected-config");
      post.add("list-tunnels-runtime");
      testCommands(comandi);
      Thread.sleep(3000L);
      testCommands(post);
      Thread.sleep(30000L);
      testCommands(post);
      ok = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertTrue(ok);
  }

  private void testCommands(List<String> cmd) throws IOException {
    // Thread.sleep(5000L);
    shell.run(new InputProvider() {
      private int invoked = 0;

      @Override
      public Input readInput() {
        invoked++;
        if (cmd.size() >= invoked) {
          return () -> cmd.get(invoked - 1);
        } else {
          return () -> "exit";
        }
      }
    });
  }
}
