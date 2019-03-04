package org.ar4k.agent.cortex.conversationBot.nodes;

import org.ar4k.agent.cortex.conversationBot.AbstractBotConfiguration;
import org.ar4k.agent.cortex.conversationBot.AbstractBrocaBot;

public interface HomunculusMaker {

  public AbstractHomunculus<? extends AbstractHomunculusConfiguration> getHomunculus(
      AbstractBrocaBot<? extends AbstractBotConfiguration> bot) throws InstantiationException, IllegalAccessException;

}
