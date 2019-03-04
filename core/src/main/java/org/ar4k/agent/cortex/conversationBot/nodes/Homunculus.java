package org.ar4k.agent.cortex.conversationBot.nodes;

import java.util.ArrayList;
import java.util.Collection;

import org.ar4k.agent.cortex.Meme;
import org.ar4k.agent.cortex.actions.Actions;
import org.ar4k.agent.cortex.conversationBot.ai.IntentMatcher;
import org.ar4k.agent.cortex.conversationBot.exceptions.HomunculusException;
import org.ar4k.agent.cortex.conversationBot.exceptions.LockedHomunculusException;
import org.ar4k.agent.cortex.conversationBot.exceptions.TrainingIntentMatcherException;
import org.ar4k.agent.cortex.conversationBot.messages.ConversationFact;
import org.ar4k.agent.cortex.conversationBot.messages.CortexMessage;
import org.ar4k.agent.cortex.memory.TimeContextConversation;
import org.ar4k.agent.cortex.rulesEngines.DroolsRule;
import org.ar4k.agent.cortex.rulesEngines.RulesEngine;
import org.springframework.messaging.MessageHandler;

/**
 * implementa un nodo per l'albero decisionale del bot
 * 
 * @author andrea
 *
 */

public interface Homunculus<T extends Meme> extends MessageHandler {

  public String getShortName();

  public Actions<? extends CortexMessage, ? extends TimeContextConversation, AbstractHomunculus<? extends AbstractHomunculusConfiguration>> newActions(
      CortexMessage message);

  public IntentMatcher getIntentMatcher();

  public void setShortName(String name) throws LockedHomunculusException;

  /**
   * ritorna le regole antecedenti all'esecuzione dell'nlp
   * 
   * @return
   */
  public RulesEngine getPreNlpRules();

  /**
   * regole eseguiti dopo nlp e prima del passaggio ai figli
   * 
   * @return
   */
  public RulesEngine getPostNlpRules();

  /**
   * regole finali eseguite dopo i children
   * 
   * @return
   */
  public RulesEngine getPostChildrensRules();

  /**
   * Ritorna ConversationFact perchè potrebbe non esserci una risposta
   * 
   * @return
   * @throws HomunculusException
   */
  public ConversationFact parseMessage(CortexMessage message) throws HomunculusException;

  /**
   * frasi di esempio per l'intento associato a questo nodo
   * 
   * @return
   */
  public Collection<String> getIntentExamples();

  /**
   * aggiunge frasi di esempio alla programmazione
   * 
   * @param intent
   */
  public void deleteIntentExample(String intent);

  public void addIntentExample(String intent);

  /**
   * esegue il train
   * 
   * @throws TrainingIntentMatcherException
   * @throws LockedHomunculusException
   */
  public void train() throws TrainingIntentMatcherException, LockedHomunculusException;

  public boolean isOnline();

  /**
   * esegue un check per verificare effettivamente lo stato
   * 
   * @return
   */
  public boolean selfCheck();

  /**
   * prepara nlp e rules engine
   * 
   * @return
   * @throws TrainingIntentMatcherException
   * @throws LockedHomunculusException
   */
  public void bootstrap() throws TrainingIntentMatcherException, LockedHomunculusException;

  /**
   * ritorna la configurazione del nodo
   * 
   * @return
   */
  public T getConfig();

  /**
   * permete l'inserimento della configurazione. Per attuarla è necessario chimare
   * bootstrap()
   * 
   * @param configuration
   * @throws LockedHomunculusException
   */
  public void setConfig(T configuration) throws LockedHomunculusException;

  /**
   * valida la configurazione senza salvarla in memoria
   * 
   * @param configuration
   * @return lista vuota se non si presentano errori, altrimenti lista stringhe
   *         errore
   */
  public Collection<String> checkConfig(T configuration);

  /**
   * ritorna i children del nodo
   * 
   * @return
   */
  public Collection<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>> getChildren();

  public void addChild(AbstractHomunculus<? extends AbstractHomunculusConfiguration> child)
      throws LockedHomunculusException;

  /**
   * ritorna il padre reale del nodo
   * 
   * @return
   */
  public AbstractHomunculus<? extends AbstractHomunculusConfiguration> getParent();

  public void setParent(AbstractHomunculus<? extends AbstractHomunculusConfiguration> father)
      throws LockedHomunculusException;

  public AbstractHomunculus<? extends AbstractHomunculusConfiguration> getRealParent();

  public void setRealParent(AbstractHomunculus<? extends AbstractHomunculusConfiguration> realFather)
      throws LockedHomunculusException;

  public void setPreNlpRules(ArrayList<DroolsRule> preNlpRules) throws LockedHomunculusException;

  public void setPostNlpRules(ArrayList<DroolsRule> postNlpRules) throws LockedHomunculusException;

  public void setPostChildrenRules(ArrayList<DroolsRule> postChildrenRules) throws LockedHomunculusException;

  public boolean isSymbolicLink();

}
