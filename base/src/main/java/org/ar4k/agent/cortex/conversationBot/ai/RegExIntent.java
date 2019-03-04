package org.ar4k.agent.cortex.conversationBot.ai;

import java.util.Collection;
import java.util.HashSet;

import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculus;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;

/**
 * intento per la gestione del Matcher RegEx
 * 
 * @author andrea
 *
 */
public class RegExIntent implements Intent {

  private Collection<String> regexExamples = new HashSet<String>();
  private AbstractHomunculus<? extends AbstractHomunculusConfiguration> child = null;

  @Override
  public Collection<String> getExamples() {
    return regexExamples;
  }

  public void setExamples(Collection<String> regexExamples) {
    this.regexExamples = regexExamples;
  }

  @Override
  public AbstractHomunculus<? extends AbstractHomunculusConfiguration> getChild() {
    return child;
  }

  public void setChild(AbstractHomunculus<? extends AbstractHomunculusConfiguration> child) {
    this.child = child;
  }

}
