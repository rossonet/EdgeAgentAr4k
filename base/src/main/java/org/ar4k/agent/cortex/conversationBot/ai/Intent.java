package org.ar4k.agent.cortex.conversationBot.ai;

import java.util.Collection;

import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculus;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;

/**
 * intento generico, richiesti solo gli esempi.
 * 
 * @author andrea
 *
 */
public interface Intent {

  public Collection<String> getExamples();

  public AbstractHomunculus<? extends AbstractHomunculusConfiguration> getChild();

}
