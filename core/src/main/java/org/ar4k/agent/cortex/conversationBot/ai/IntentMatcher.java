package org.ar4k.agent.cortex.conversationBot.ai;

import java.util.Collection;
import java.util.List;

import org.ar4k.agent.cortex.conversationBot.exceptions.IntentMatcherException;
import org.ar4k.agent.cortex.conversationBot.exceptions.TrainingIntentMatcherException;
import org.ar4k.agent.cortex.conversationBot.messages.CortexMessage;

/**
 * Rappresenta l'interfaccia astratta per l'utilizzo delle NLU implementa le
 * funzioni di query e trainning
 * 
 */
public interface IntentMatcher {

  public boolean isOnline();

  public boolean isTrained();

  public void train() throws TrainingIntentMatcherException;

  public List<? extends IntentMatched> parse(CortexMessage message) throws IntentMatcherException;

  public Collection<? extends Intent> getIntents();

  public EntitySynonyms getEntities();

  public void addIntent(Intent intent);

  public void delIntent(Intent intent);

}
