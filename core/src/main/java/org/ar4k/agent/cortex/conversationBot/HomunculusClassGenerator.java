package org.ar4k.agent.cortex.conversationBot;

import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;

public interface HomunculusClassGenerator {

  public static Class<? extends AbstractHomunculusConfiguration> homunculusClassName = AbstractHomunculusConfiguration.class;

}
