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

@RunWith(SpringRunner.class)
@SpringBootTest(properties = { ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false",
    InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false" })
@ComponentScan("org.ar4k.agent.console,org.ar4k.agent.core.web,org.ar4k.agent.spring.autoconfig,org.ar4k.agent.spring.autoconfig.web")
//@Import(TestApplicationRunner.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ConsoleWebTest {

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
  public void runForFiveMinutesTest() throws InterruptedException {
    Thread.sleep(5 * 60 * 1000);
  }

  @Test
  public void testBase() {
    boolean ok = false;
    try {
      List<String> comandi = new ArrayList<>();
      comandi.add("test --test-string " + UUID.randomUUID().toString());
      testCommands(comandi);
      ok = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertTrue(ok);
  }

  @Test
  public void createBaseConfigAndExport() {
    boolean ok = false;
    try {
      List<String> comandi = new ArrayList<>();
      comandi.add("create-selected-config --name testconf --promptColor RED --prompt test");
      comandi.add("get-selected-config-base64");
      comandi.add("get-selected-config-json");
      comandi.add("save-selected-config-base64 --filename test_base64");
      comandi.add("save-selected-config-json --filename test_json");
      comandi.add("list-config");
      comandi.add("unset-selected-config");
      testCommands(comandi);
      ok = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertTrue(ok);
  }

  @Ignore
  @Test
  public void cloneBaseConfig() {
    boolean ok = false;
    try {
      List<String> comandi = new ArrayList<>();
      comandi.add("create-selected-config --name testconf --promptColor RED --prompt test4");
      comandi.add("set-selected-config-as-runtime");
      comandi.add("list-config");
      Thread.sleep(2000L);
      comandi.add("clone-runtime-config --new-prompt test2 --new-name clone-target");
      comandi.add("list-config");
      // String reload = anima.configs.get(0).uniqueId.toString();
      // comandi.add("select-config --id-config " + reload);
      testCommands(comandi);
      ok = true;
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    assertTrue(ok);
  }

  @Test
  public void createBaseConfigExportAndLoad() {
    String tagCheck = UUID.randomUUID().toString();
    String fileConfJson = "test_json_" + tagCheck;
    String fileConfBase64 = "test_base_" + tagCheck;
    boolean ok = false;
    try {
      List<String> comandiCreate = new ArrayList<>();
      comandiCreate.add("create-selected-config --name testconf --promptColor YELLOW --prompt test --tag " + tagCheck);
      comandiCreate.add("get-selected-config-base64");
      comandiCreate.add("get-selected-config-json");
      comandiCreate.add("save-selected-config-base64 --filename " + fileConfBase64);
      comandiCreate.add("save-selected-config-json --filename " + fileConfJson);
      comandiCreate.add("list-config");
      comandiCreate.add("unset-selected-config");
      testCommands(comandiCreate);
      List<String> comandiReloadJson = new ArrayList<>();
      comandiReloadJson.add("load-selected-config-base64 --filename " + fileConfBase64);
      comandiCreate.add("unset-selected-config");
      // comandiReloadJson.add("save-selected-config-json --filename " +
      // fileCheckBase);
      testCommands(comandiReloadJson);
      List<String> comandiReloadBase = new ArrayList<>();
      comandiReloadBase.add("load-selected-config-json --filename " + fileConfJson);
      comandiCreate.add("unset-selected-config");
      // comandiReloadBase.add("save-selected-config-json --filename " +
      // fileCheckJson);
      testCommands(comandiReloadJson);
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
