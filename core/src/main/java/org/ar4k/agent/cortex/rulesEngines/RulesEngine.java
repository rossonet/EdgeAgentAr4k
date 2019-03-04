package org.ar4k.agent.cortex.rulesEngines;

import java.util.ArrayList;

import org.ar4k.agent.cortex.actions.Actions;
import org.ar4k.agent.cortex.conversationBot.exceptions.RuleException;
import org.ar4k.agent.cortex.conversationBot.messages.CortexMessage;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculus;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;
import org.ar4k.agent.cortex.memory.TimeContextConversation;

public interface RulesEngine {

  public void fireRule(
      Actions<? extends CortexMessage, ? extends TimeContextConversation, ? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>> actions)
      throws RuleException;

  public void start();

  public void reset();

  public void stop();
  
  public void setRules(ArrayList<DroolsRule> rules);

}
