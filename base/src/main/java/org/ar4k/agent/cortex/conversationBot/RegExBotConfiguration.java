package org.ar4k.agent.cortex.conversationBot;

import org.ar4k.agent.cortex.conversationBot.exceptions.BotBootException;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;
import org.ar4k.agent.cortex.conversationBot.nodes.RegExHomunculusConfiguration;

public class RegExBotConfiguration extends AbstractBotConfiguration {

  public String testAdditionalField = "prova"; // non togliere!!! per i test

  private static final long serialVersionUID = 7037040339721931719L;

  public static Class<? extends AbstractHomunculusConfiguration> homunculusClassName = RegExHomunculusConfiguration.class;

  @Override
  public RegExBrocaBot<RegExBotConfiguration> getBot() throws BotBootException {
    RegExBrocaBot<RegExBotConfiguration> botTarget = null;
    botTarget = new RegExBrocaBot<RegExBotConfiguration>(this, bootstrapData);
    return botTarget;
  }

}
