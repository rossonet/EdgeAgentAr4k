package org.ar4k.agent.cortex.conversationBot.nodes;

import java.util.Collection;

import org.ar4k.agent.cortex.actions.DefaultActions;
import org.ar4k.agent.cortex.conversationBot.AbstractBotConfiguration;
import org.ar4k.agent.cortex.conversationBot.AbstractBrocaBot;
import org.ar4k.agent.cortex.conversationBot.ai.IntentMatcher;
import org.ar4k.agent.cortex.conversationBot.ai.RegExIntent;
import org.ar4k.agent.cortex.conversationBot.ai.RegExIntentMatcher;
import org.ar4k.agent.cortex.conversationBot.exceptions.LockedHomunculusException;
import org.ar4k.agent.cortex.conversationBot.exceptions.TrainingIntentMatcherException;
import org.ar4k.agent.cortex.conversationBot.messages.CortexMessage;
import org.ar4k.agent.cortex.memory.TimeContextConversation;

/**
 * implementa un nodo Homunculus con parse RegEX
 * 
 * @author andrea
 *
 */
public class RegExHomunculus<T extends RegExHomunculusConfiguration> extends AbstractHomunculus<T> {

  private transient RegExIntentMatcher im = null;
  private boolean debug = false;

  public RegExHomunculus(AbstractBrocaBot<? extends AbstractBotConfiguration> bot, T config, String name)
      throws InstantiationException, IllegalAccessException {
    super(bot, config, name);
  }

  public RegExHomunculus(AbstractBrocaBot<? extends AbstractBotConfiguration> bot, T config)
      throws InstantiationException, IllegalAccessException {
    super(bot, config);
  }

  @Override
  public void train() throws TrainingIntentMatcherException, LockedHomunculusException {
    droolsTraining();
    im = new RegExIntentMatcher();
    for (AbstractHomunculus<? extends AbstractHomunculusConfiguration> figlio : getChildren()) {
      RegExIntent intento = new RegExIntent();
      intento.setChild(figlio);
      intento.setExamples(figlio.getIntentExamples());
      im.addIntent(intento);
    }
    im.train();
    if (debug == true)
      System.out.println("train of " + getShortName() + " => " + ((im.isTrained() == true) ? "OK" : "KO"));
  }

  @Override
  public Collection<String> checkConfig(T configuration) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IntentMatcher getIntentMatcher() {
    return im;
  }

  @Override
  public DefaultActions<CortexMessage, TimeContextConversation, AbstractHomunculus<? extends AbstractHomunculusConfiguration>> newActions(
      CortexMessage message) {
    return new DefaultActions<CortexMessage, TimeContextConversation, AbstractHomunculus<?>>(message, this);
  }

}
