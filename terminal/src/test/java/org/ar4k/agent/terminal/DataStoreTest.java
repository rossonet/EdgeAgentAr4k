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
package org.ar4k.agent.terminal;

import java.util.UUID;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Anima.AnimaEvents;
import org.ar4k.gw.anima.TestApplicationRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = { ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false",
    InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false" })
@ComponentScan("org.ar4k.agent")
@Import(TestApplicationRunner.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DataStoreTest {

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
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }
  };

  @Test
  public void putInDataStore() throws InterruptedException {
    Thread.sleep(2000L);
    while (anima == null || !anima.getState().toString().equals("STAMINAL") || !anima.dataStoreExists()) {
      if (anima.getState().toString().equals("INIT")) {
        anima.sendEvent(AnimaEvents.BOOTSTRAP);
      }
      try {
        System.out.println(anima.getState().toString());
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
      Thread.sleep(2000L);
    }
    String stringa = UUID.randomUUID().toString();
    boolean primo = true;
    for (int i = 0; i < 10000; i++) {
      if (primo) {
        anima.setContextData(stringa, UUID.randomUUID().toString());
        primo = false;
      } else {
        anima.setContextData(UUID.randomUUID().toString(), UUID.randomUUID().toString());
      }
    }
    System.out.println(anima.getContextData(stringa));
  }

}
