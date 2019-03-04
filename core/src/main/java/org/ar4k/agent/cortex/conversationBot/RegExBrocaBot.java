package org.ar4k.agent.cortex.conversationBot;

import java.util.Map;

import org.ar4k.agent.cortex.conversationBot.exceptions.BotBootException;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;
import org.ar4k.agent.cortex.conversationBot.nodes.RegExHomunculusConfiguration;

public class RegExBrocaBot<T extends RegExBotConfiguration> extends AbstractBrocaBot<T> {

  public RegExBrocaBot(T configuration, Map<String, Object> bootstrapData) throws BotBootException {
    super(configuration, bootstrapData);
  }

  public RegExBrocaBot(T configuration) throws BotBootException {
    super(configuration);
  }

  @Override
  public AbstractHomunculusConfiguration newHomunculusConfiguration(String name) throws BotBootException {
    return new RegExHomunculusConfiguration(name);
  }

  @Override
  public Class<? extends AbstractHomunculusConfiguration> homunculusConfigurationClass() {
    return RegExHomunculusConfiguration.class;
  }

  @Override
  public void nextStep() {
    // bot passivo (non implementato)
  }

  @Override
  public void reset() {
    // TODO Auto-generated method stub    
  }
}
