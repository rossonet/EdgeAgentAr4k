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
package org.ar4k.core.cortex;

import org.ar4k.agent.cortex.actions.DefaultActions;
import org.ar4k.agent.cortex.conversationBot.exceptions.RuleException;
import org.ar4k.agent.cortex.conversationBot.messages.BotMessage;
import org.ar4k.agent.cortex.conversationBot.nodes.RegExHomunculus;
import org.ar4k.agent.cortex.conversationBot.nodes.RegExHomunculusConfiguration;
import org.ar4k.agent.cortex.memory.BotTimeContextConversation;
import org.ar4k.agent.cortex.rulesEngines.DroolsRule;
import org.ar4k.agent.cortex.rulesEngines.DroolsRulesEngine;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class DroolsTests {

  @BeforeClass
  public static void setUpBeforeClass() {
  }

  @AfterClass
  public static void tearDownAfterClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testBaseRule() throws InstantiationException, IllegalAccessException, RuleException {
    DroolsRulesEngine dre = new DroolsRulesEngine();
    DroolsRule ruoloA = new DroolsRule();
    ruoloA.setWhen("DefaultActions($message:getAttribute(\"caldaia\"))");
 ruoloA.setThen("$actions" + ".setAttribute(\"primo\",\"A\");" + "\n$actions" + ".logMessage($message);");
    DroolsRule ruoloB = new DroolsRule();
    ruoloB.setWhen("DefaultActions($primo: getAttribute(\"primo\"))");
    ruoloB.setThen(
        "$actions" + ".setAttribute(\"secondo\",\"B\");" + "\n$actions" + ".logMessage(String.valueOf($primo));");
    DroolsRule ruoloC = new DroolsRule();
    ruoloC.setWhen("eval(true)");
    ruoloC.setThen("$actions" + ".setAttribute(\"terzo\",\"C\");");
    BotMessage messaggio = new BotMessage();
    messaggio.setMessage("Messagio di prova da riportare nei log...");
    DefaultActions<BotMessage, BotTimeContextConversation, RegExHomunculus<RegExHomunculusConfiguration>> azione = new DefaultActions<BotMessage, BotTimeContextConversation, RegExHomunculus<RegExHomunculusConfiguration>>(
        messaggio, null);
    dre.addRule(ruoloA);
    dre.addRule(ruoloB);
    dre.addRule(ruoloC);
    dre.start();
    dre.fireRule(azione);
    dre.stop();
    assert (azione.getAttribute("primo").equals("A"));
    assert (azione.getAttribute("secondo").equals("B"));
    assert (azione.getAttribute("terzo").equals("C"));
  }

  @Rule
  public TestWatcher watcher = new TestWatcher() {
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }
  };

}
