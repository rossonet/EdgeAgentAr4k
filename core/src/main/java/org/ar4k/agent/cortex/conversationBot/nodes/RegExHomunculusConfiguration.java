package org.ar4k.agent.cortex.conversationBot.nodes;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.ar4k.agent.cortex.conversationBot.AbstractBotConfiguration;
import org.ar4k.agent.cortex.conversationBot.AbstractBrocaBot;
import org.ar4k.agent.cortex.rulesEngines.DroolsRule;

public class RegExHomunculusConfiguration extends AbstractHomunculusConfiguration {

  public RegExHomunculusConfiguration(Set<String> intentExamples, Map<String, String> entitySynonyms,
      Map<String, String> globalEntitySynonyms, String name, ArrayList<DroolsRule> preNlpRules,
      ArrayList<DroolsRule> postNlpRules, ArrayList<DroolsRule> postChildrenRules, String realHomunculusName) {
    super(intentExamples, entitySynonyms, globalEntitySynonyms, name, preNlpRules, postNlpRules, postChildrenRules,
        realHomunculusName);
    homunculusClassName = "org.ar4k.agent.cortex.conversationBot.nodes.RegExHomunculus";
  }

  public RegExHomunculusConfiguration(Set<String> intentExamples, Map<String, String> entitySynonyms, String name,
      Map<String, String> globalEntitySynonyms, ArrayList<DroolsRule> preNlpRules, ArrayList<DroolsRule> postNlpRules,
      ArrayList<DroolsRule> postChildrenRules, String realHomunculusName) {
    super(intentExamples, entitySynonyms, globalEntitySynonyms, name, preNlpRules, postNlpRules, postChildrenRules,
        realHomunculusName);
    homunculusClassName = "org.ar4k.agent.cortex.conversationBot.nodes.RegExHomunculus";
  }

  private static final long serialVersionUID = -4739177763157235987L;

  public RegExHomunculusConfiguration(String name) {
    super(name);
    homunculusClassName = "org.ar4k.agent.cortex.conversationBot.nodes.RegExHomunculus";
  }

  public RegExHomunculusConfiguration() {
    super();
    homunculusClassName = "org.ar4k.agent.cortex.conversationBot.nodes.RegExHomunculus";
  }

  @Override
  public RegExHomunculus<RegExHomunculusConfiguration> getHomunculus(
      AbstractBrocaBot<? extends AbstractBotConfiguration> bot) throws InstantiationException, IllegalAccessException {
    RegExHomunculus<RegExHomunculusConfiguration> hTarget = new RegExHomunculus<RegExHomunculusConfiguration>(bot, this,
        name);
    return hTarget;
  }

}
