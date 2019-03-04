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

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.ar4k.agent.cortex.conversationBot.RegExBotConfiguration;
import org.ar4k.agent.cortex.conversationBot.RegExBrocaBot;
import org.ar4k.agent.cortex.conversationBot.exceptions.BotBootException;
import org.ar4k.agent.cortex.conversationBot.exceptions.HomunculusBootException;
import org.ar4k.agent.cortex.conversationBot.exceptions.HomunculusException;
import org.ar4k.agent.cortex.conversationBot.exceptions.LockedHomunculusException;
import org.ar4k.agent.cortex.conversationBot.exceptions.TrainingIntentMatcherException;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;
import org.ar4k.agent.cortex.rulesEngines.DroolsRule;
import org.ar4k.agent.helper.SymbolicLinksTree;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class BotTests {

  List<SymbolicLinksTree<AbstractHomunculusConfiguration>> listTree = null;

 // ArrayList<DroolsRule> regole = null;

  private RegExBrocaBot<RegExBotConfiguration> getBotFromTreeFile(String fileName)
      throws ClassNotFoundException, IOException, BotBootException {
    RegExBotConfiguration.homunculusClassName = (Class<? extends AbstractHomunculusConfiguration>) Class
        .forName("org.ar4k.agent.cortex.conversationBot.nodes.RegExHomunculusConfiguration");
    RegExBotConfiguration cbot = new RegExBotConfiguration();
    cbot.botName = "RegExBot Test";
    cbot.botDescription = "bot per test automatici";
    cbot.symbolicLinksTree = getTreeFromFile(fileName);
    return cbot.getBot();
  }

  private String getTreeFromFile(String fileName) throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    File treeBaseFile = new File(classLoader.getResource(fileName).getFile());
    String treeBaseData = FileUtils.readFileToString(treeBaseFile, StandardCharsets.UTF_8);
    return treeBaseData;
  }

  @Test
  public void importExportConfig() throws ClassNotFoundException, BotBootException, IOException,
      HomunculusBootException, TrainingIntentMatcherException {
    RegExBrocaBot<RegExBotConfiguration> bot = getBotFromTreeFile("ws_tree_base.json");
    bot.createRuntimeTreeFromConfig();
    // System.out.println(bot.getRuntimeSymbolicLinksTreeAsJson());
    bot.bootstrapBot();
    try {
      bot.getMasterNode().setShortName("errore");
      fail("Non blocca quando online (o non è andato online)");
    } catch (LockedHomunculusException attesa) {
    }
  }

  @Test
  public void importExportConfigView() throws ClassNotFoundException, BotBootException, IOException,
      HomunculusBootException, TrainingIntentMatcherException {
    RegExBrocaBot<RegExBotConfiguration> bot = getBotFromTreeFile("ws_tree_simple.json");
    bot.createRuntimeTreeFromConfig();
    // System.out.println(bot.getRuntimeSymbolicLinksTreeAsJson());
    bot.bootstrapBot();
    System.out.println(bot.getRuntimeSymbolicLinksTreeAsJson());
    assert (bot.getRuntimeSymbolicLinksTreeAsJson().contains("testAdditionalField"));
  }

  @Test
  public void domandeRisposteRegEx()
      throws ClassNotFoundException, IOException, HomunculusException, InterruptedException {
    DroolsRule ruoloA = new DroolsRule();
    ruoloA.setWhen("DefaultActions($message:getMessage())");
    ruoloA.setThen("$actions.setFatherReplyTemplate(FatherReplyTemplate.ALL);"
        + "\n$actions.logMessage(\"preNLP: \"+$message+\" da \"+$actions.getWorkerHomunculus());");
    DroolsRule ruoloB = new DroolsRule();
    ruoloB.setWhen("DefaultActions($message:getMessage())");
    ruoloB.setThen("$actions.logMessage(\"postNLP: \"+$message+\" da \"+$actions.getWorkerHomunculus());");
    DroolsRule ruoloC = new DroolsRule();
    ruoloC.setWhen("DefaultActions($message:getMessage())");
    ruoloC.setThen("$actions.setMessageReply(\"rispondo dal nodo \"+$actions.getWorkerHomunculus());");
    // carica il bot
    RegExBrocaBot<RegExBotConfiguration> bot = getBotFromTreeFile("ws_tree_simple.json");
    bot.createRuntimeTreeFromConfig();
    for (AbstractHomunculusConfiguration nodo : bot.getRuntimeSymbolicLinksTree().getAllHomunculusConfigurations()) {
      nodo.preNlpRules.add(ruoloA);
      nodo.postNlpRules.add(ruoloB);
      nodo.postChildrenRules.add(ruoloC);
    }
    System.out.println(bot.getRuntimeSymbolicLinksTreeAsJson());
    bot.bootstrapBot();
    // System.out.println("miei figli -> " + bot.getMasterNode().getChildren());
    String rispota = bot.consoleGodQuery("ABV");
    System.out.println(rispota);
    assert (rispota.equals("rispondo dal nodo A\nrispondo dal nodo AB"));
  }

  @Rule
  public TestWatcher watcher = new TestWatcher() {
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }
  };

}
