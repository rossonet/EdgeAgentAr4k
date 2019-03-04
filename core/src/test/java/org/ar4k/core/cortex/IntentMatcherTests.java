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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.ar4k.agent.cortex.conversationBot.ai.IntentMatched;
import org.ar4k.agent.cortex.conversationBot.ai.RegExIntent;
import org.ar4k.agent.cortex.conversationBot.ai.RegExIntentMatcher;
import org.ar4k.agent.cortex.conversationBot.exceptions.IntentMatcherException;
import org.ar4k.agent.cortex.conversationBot.exceptions.RuleException;
import org.ar4k.agent.cortex.conversationBot.exceptions.TrainingIntentMatcherException;
import org.ar4k.agent.cortex.conversationBot.messages.BotMessage;
import org.ar4k.agent.cortex.conversationBot.messages.CortexMessage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class IntentMatcherTests {

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
  public void testBaseNLP() throws InstantiationException, IllegalAccessException, RuleException,
      TrainingIntentMatcherException, IntentMatcherException {
    RegExIntentMatcher im = new RegExIntentMatcher();
    RegExIntent iAnimali = new RegExIntent();
    RegExIntent iMobili = new RegExIntent();
    RegExIntent iCibi = new RegExIntent();
    Collection<String> eAnimali = new HashSet<String>();
    Collection<String> eMobili = new HashSet<String>();
    Collection<String> eCibi = new HashSet<String>();
    eAnimali.add("cane");
    eAnimali.add("cani");
    eAnimali.add("gatt");
    eAnimali.add("pesc");
    eMobili.add("tavolo");
    eMobili.add("sedia");
    eMobili.add("letto");
    eCibi.add("pane");
    eCibi.add("pomodoro");
    eCibi.add("pasta");
    iAnimali.setExamples(eAnimali);
    iMobili.setExamples(eMobili);
    iCibi.setExamples(eCibi);
    im.addIntent(iAnimali);
    im.addIntent(iMobili);
    im.addIntent(iCibi);
    im.train();
    if (im.isTrained()) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      CortexMessage m = new BotMessage();
      m.setMessage("Vorrei un piatto di pasta al sugo di pomodoro");
      List<IntentMatched> r = im.parse(m);
      System.out.println(gson.toJson(r));
      CortexMessage m1 = new BotMessage();
      m1.setMessage("Ho visto un gatto sulla sedia");
      List<IntentMatched> r1 = im.parse(m1);
      System.out.println(gson.toJson(r1));
    }
  }

  @Rule
  public TestWatcher watcher = new TestWatcher() {
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }
  };

}
