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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.ar4k.agent.cortex.conversationBot.RegExBotConfiguration;
import org.ar4k.agent.cortex.conversationBot.RegExBrocaBot;
import org.ar4k.agent.cortex.conversationBot.exceptions.BotBootException;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;
import org.ar4k.agent.helper.SymbolicLinksTree;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class HomunculusTests {

  private String getTreeFromFile(String fileName) throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    File treeBaseFile = new File(classLoader.getResource(fileName).getFile());
    String treeBaseData = FileUtils.readFileToString(treeBaseFile, StandardCharsets.UTF_8);
    return treeBaseData;
  }

  @SuppressWarnings("unchecked")
  private RegExBrocaBot<RegExBotConfiguration> getBotFromTreeFile(String fileName)
      throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException, BotBootException {
    RegExBotConfiguration.homunculusClassName = (Class<? extends AbstractHomunculusConfiguration>) Class
        .forName("org.ar4k.agent.cortex.conversationBot.nodes.RegExHomunculusConfiguration");
    RegExBotConfiguration cbot = new RegExBotConfiguration();
    cbot.botName = "RegExBot Test";
    cbot.botDescription = "bot per test automatici";
    cbot.symbolicLinksTree = getTreeFromFile(fileName);
    return cbot.getBot();
  }

  @Test
  public void testBotFromFile()
      throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, BotBootException {
    RegExBrocaBot<RegExBotConfiguration> bot = getBotFromTreeFile("ws_tree_simple.json");
    System.out.println(bot.getConfigAsJson());
    bot.createRuntimeTreeFromConfig();
    System.out.println(bot.getRuntimeSymbolicLinksTreeAsJson());
  }

  @Test
  public void TreeCheckFather()
      throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, BotBootException {
    RegExBrocaBot<RegExBotConfiguration> bot = getBotFromTreeFile("ws_tree_simple.json");
    // System.out.println(bot.getConfigAsJson());
    bot.createRuntimeTreeFromConfig();
    @SuppressWarnings("unchecked")
    SymbolicLinksTree<AbstractHomunculusConfiguration> tree = (SymbolicLinksTree<AbstractHomunculusConfiguration>) bot
        .getRuntimeSymbolicLinksTree(); // = demoTree();
    Collection<AbstractHomunculusConfiguration> lista = tree.getAllHomunculusConfigurations();
    int count = 0;
    System.out.print("Parse nodes: " + tree);
    for (AbstractHomunculusConfiguration a : lista) {
      System.out.print(a.name + " ");
      if (a.name != null && a.name.equals("ABB")) {
        count++;
        assert (tree.getTree(a).getParent().getHead().name.equals("AB"));
      }
      if (a.name != null && a.name.equals("ADAA")) {
        count++;
        assert (tree.getTree(a).getParent().getHead().name.equals("ADA"));
      }
      if (a.name != null && a.name.equals("ADAAA")) {
        count++;
        assert (tree.getTree(a).getParent().getHead().name.equals("ADAA"));
      }
      if (a.name != null && a.name.equals("ABCB")) {
        count++;
        assert (tree.getTree(a).getParent().getHead().name.equals("ABC"));
      }
      if (a.name != null && a.name.equals("AB")) {
        count++;
        assert (tree.getTree(a).getParent().getHead().name.equals("A"));
      }
    }
    System.out.println("\nend fathers analisys");
    assert (count == 5);
  }

  @Test
  public void TreeCheckChildrens()
      throws ClassNotFoundException, InstantiationException, IllegalAccessException, BotBootException, IOException {
    RegExBrocaBot<RegExBotConfiguration> bot = getBotFromTreeFile("ws_tree_simple.json");
    // System.out.println(bot.getConfigAsJson());
    bot.createRuntimeTreeFromConfig();
    @SuppressWarnings("unchecked")
    SymbolicLinksTree<AbstractHomunculusConfiguration> tree = (SymbolicLinksTree<AbstractHomunculusConfiguration>) bot
        .getRuntimeSymbolicLinksTree(); // = demoTree();
    Collection<AbstractHomunculusConfiguration> lista = tree.getDescent();
    int count = 0;
    System.out.print("Parse nodes: ");
    for (AbstractHomunculusConfiguration a : lista) {
      System.out.print(a.name + "\n");
      if (a.name.equals("AB")) {
        count++;
        Set<String> tests = new HashSet<String>();
        tests.add("ABC");
        tests.add("ABA");
        tests.add("ABB");
        assert (tree.getTree(a).getSubTrees().size() == 3);
        for (SymbolicLinksTree<AbstractHomunculusConfiguration> check : tree.getTree(a).getSubTrees()) {
          assert (tests.contains(check.getHead().name));
        }
      }
      if (a.name.equals("ABCAC")) {
        count++;
        assert (tree.getTree(a).getSubTrees().isEmpty());
      }
      if (a.name.equals("ABCA")) {
        count++;
        Set<String> tests = new HashSet<String>();
        tests.add("ABCAA");
        tests.add("ABCAB");
        tests.add("ABCAC");
        tests.add("ABCAD");
        assert (tree.getTree(a).getSubTrees().size() == 4);
        for (SymbolicLinksTree<AbstractHomunculusConfiguration> check : tree.getTree(a).getSubTrees()) {
          assert (tests.contains(check.getHead().name));
        }
      }
      if (a.name.equals("ADAA")) {
        count++;
        Set<String> tests = new HashSet<String>();
        tests.add("ADAAA");
        tests.add("ADAAB");
        assert (tree.getTree(a).getSubTrees().size() == 2);
        for (SymbolicLinksTree<AbstractHomunculusConfiguration> check : tree.getTree(a).getSubTrees()) {
          assert (tests.contains(check.getHead().name));
        }
      }
    }
    System.out.println("\nend childrens analisys");
    assert (count == 4);
  }

  @Rule
  public TestWatcher watcher = new TestWatcher() {
    protected void starting(Description description) {
      System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
    }
  };

}
