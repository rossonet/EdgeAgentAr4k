package org.ar4k.agent.cortex.conversationBot.nodes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.ar4k.agent.cortex.actions.Actions;
import org.ar4k.agent.cortex.actions.Actions.ChildrenRequestTemplate;
import org.ar4k.agent.cortex.actions.Actions.FatherReplyTemplate;
import org.ar4k.agent.cortex.actions.Actions.WorkingSteps;
import org.ar4k.agent.cortex.conversationBot.AbstractBotConfiguration;
import org.ar4k.agent.cortex.conversationBot.AbstractBrocaBot;
import org.ar4k.agent.cortex.conversationBot.ai.IntentMatched;
import org.ar4k.agent.cortex.conversationBot.exceptions.HomunculusException;
import org.ar4k.agent.cortex.conversationBot.exceptions.LockedHomunculusException;
import org.ar4k.agent.cortex.conversationBot.exceptions.TrainingIntentMatcherException;
import org.ar4k.agent.cortex.conversationBot.messages.BotMessage;
import org.ar4k.agent.cortex.conversationBot.messages.ConversationFact;
import org.ar4k.agent.cortex.conversationBot.messages.CortexMessage;
import org.ar4k.agent.cortex.memory.TimeContextConversation;
import org.ar4k.agent.cortex.rulesEngines.DroolsRule;
import org.ar4k.agent.cortex.rulesEngines.DroolsRulesEngine;
import org.ar4k.agent.cortex.rulesEngines.RulesEngine;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

/**
 * Classe astratta con le implementazioni base per il nodo che compone l'albero
 * del chatbot
 * 
 * @author andrea
 *
 */

public abstract class AbstractHomunculus<T extends AbstractHomunculusConfiguration> implements Homunculus<T> {

  private T configuration;
  public transient AbstractBrocaBot<? extends AbstractBotConfiguration> bot = null;
  private transient boolean online = false;
  private transient RulesEngine preNlpRules = null;
  private transient RulesEngine postNlpRules = null;
  private transient RulesEngine postChildrensRules = null;
  private transient AbstractHomunculus<? extends AbstractHomunculusConfiguration> father = null;
  private transient AbstractHomunculus<? extends AbstractHomunculusConfiguration> realFather = null;
  private transient ArrayList<AbstractHomunculus<? extends AbstractHomunculusConfiguration>> children = new ArrayList<AbstractHomunculus<? extends AbstractHomunculusConfiguration>>();
  private int corePoolSize = 5;
  private String replyConcatSeparator = "\n";

  public AbstractHomunculus(AbstractBrocaBot<? extends AbstractBotConfiguration> bot, T config)
      throws InstantiationException, IllegalAccessException {
    this.bot = bot;
    this.configuration = config;
  }

  public AbstractHomunculus(AbstractBrocaBot<? extends AbstractBotConfiguration> bot, T config, String name)
      throws InstantiationException, IllegalAccessException {
    this.bot = bot;
    this.configuration = config;
  }

  // classe per chiamate con timout
  private class QueryChild implements Runnable {
    CortexMessage message = null;
    AbstractHomunculus<? extends AbstractHomunculusConfiguration> child = null;
    Integer finished = 0;

    @SuppressWarnings("unused")
    public Integer getFinished() {
      return finished;
    }

    @Override
    public void run() {
      try {
        child.parseMessage(message);
      } catch (HomunculusException e) {
        message.addException(child, e);
      }
      finished = 1;
    }

  }

  @Override
  public ConversationFact parseMessage(CortexMessage message) throws HomunculusException {

    // creare l'actions class per il messaggio ricevuto (la classe accompagna tutta
    // la lavorazione )
    Actions<? extends CortexMessage, ? extends TimeContextConversation, AbstractHomunculus<? extends AbstractHomunculusConfiguration>> azione = newActions(
        message);
    // gestisce le risposte alla fine degli if/try-catch
    Map<AbstractHomunculus<? extends AbstractHomunculusConfiguration>, BotMessage> mapReplies = new HashMap<AbstractHomunculus<? extends AbstractHomunculusConfiguration>, BotMessage>();
    // pre nlp - serve per impostare la classe actions da configurazione,
    // eventualmente cambiare il messaggio da sottoporre alla NLU, i templates,
    // System.out.println("work end at boot? " + azione.isWorkEnded());
    // etc...
    try {
      azione.setWorkingStep(WorkingSteps.PRE_NLP);
      getPreNlpRules().start();
      getPreNlpRules().fireRule(azione);
      getPreNlpRules().stop();
      // tutte le eccezioni che abbiamo visto in Actions sono sotto classi di
      // HomunculusException
    } catch (HomunculusException aa) {
      // se viene generata un eccezione, viene registrata e la lavorazione finisce
      aa.printStackTrace();
      azione.getWorkMessage().addException(this, aa);
      azione.workEnded();
    }
    if (azione.isWorkEnded() == false) {
      // nlp parser usando la classe astratta, già implementata in RegEx e Rasa
      for (IntentMatched nlp : getIntentMatcher().parse(azione.getWorkMessage())) {
        System.out.println("node " + this + " r -> " + nlp);
        azione.getWorkMessage().addNlpReply(this, nlp);
      }
      // passaggio in postnlp
      try {
        azione.setWorkingStep(WorkingSteps.POST_NLP);
        getPostNlpRules().start();
        getPostNlpRules().fireRule(azione);
        getPostNlpRules().stop();
      } catch (HomunculusException aa) {
        // se viene generata un eccezione, viene registrata e la lavorazione finisce
        aa.printStackTrace();
        azione.getWorkMessage().addException(this, aa);
        azione.workEnded();
      }
      if (azione.isWorkEnded() != true) {
        // definisce quali figli interpellare in funzione delle impostazioni
        // quando una richiesta viene mandata a un figlio il CortexMessage è lo stesso
        // (con tutti i campi caricati)
        // possibili: ALL_CHILDREN, BETTER_UNDERSTAND, BETTER_UNDERSTAND_AND_ANSWER,
        // ALL_WITH_FOUND_ENTITY,
        // ALL_WITH_FOUND_ENTITY_AND_ANSWER, OVER_UNDERSTAND_LIMIT,
        // OVER_UNDERSTAND_LIMIT_AND_ANSWER
        ChildrenRequestTemplate template = azione.getChildrenDispatcherTemplate();
        HashSet<AbstractHomunculus<? extends AbstractHomunculusConfiguration>> activeChildren = new HashSet<AbstractHomunculus<? extends AbstractHomunculusConfiguration>>();
        Collection<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>> allChildren = getChildren();
        if (template.equals(ChildrenRequestTemplate.ALL_CHILDREN)) {
          for (AbstractHomunculus<? extends AbstractHomunculusConfiguration> c : allChildren) {
            activeChildren.add(c);
            System.out.println(c + " all " + c.hashCode());
          }
        } else {
          // se è stata richiesta una risposta e il template lo prevede
          if (template.name().contains("_AND_ANSWER")) {
            for (AbstractHomunculus<? extends AbstractHomunculusConfiguration> c : allChildren) {
              if (azione.getWorkMessage().answerRequests().size() > 0
                  && azione.getWorkMessage().answerRequests().contains(c)) {
                activeChildren.add(c);
                System.out.println(c + " and_answer " + c.hashCode());
              }
            }
          }
          // il miglior nlp score
          if (template.name().contains("BETTER_UNDERSTAND")) {
            activeChildren.add(azione.getBestNlpReply().getChild());
            System.out.println(
                azione.getBestNlpReply().getChild() + " better " + azione.getBestNlpReply().getChild().hashCode());
          }
          // tutti i figli di cui sono stati trovati entità presenti negli esempi degli
          // intenti con notazione {{entita:valore}}. Per esempio: "Il {{animale:gatto}}
          // mangia il {{cibo:prosciutto}}" contiene le entità animale e cibo.
          if (template.name().contains("ALL_WITH_FOUND_ENTITY")) {
            // TODO da implementare dopo poc
          }
          if (template.name().contains("OVER_UNDERSTAND_LIMIT")
              && azione.getWorkMessage().nlpReplies().get(this) != null) {
            for (IntentMatched c : azione.getWorkMessage().nlpReplies().get(this)) {
              if (c.getScore() > azione.getUnderstandLimit()) {
                activeChildren.add(c.getChild());
                System.out.println(c.getChild() + " over " + allChildren);
              }
            }
          }
        }
        // pulisce i doppi in activeChildren
        HashSet<AbstractHomunculus<? extends AbstractHomunculusConfiguration>> candidateChildren = new HashSet<AbstractHomunculus<? extends AbstractHomunculusConfiguration>>();
        for (AbstractHomunculus<? extends AbstractHomunculusConfiguration> checkChildren : allChildren) {
          if (activeChildren.contains(checkChildren)) {
            candidateChildren.add(checkChildren);
          }
        }

        /////////////////////////////////////
        // chiamata figli ///////////////////
        /////////////////////////////////////
        for (AbstractHomunculus<? extends AbstractHomunculusConfiguration> childToCall : activeChildren) {
          System.out.println(childToCall + " " + childToCall.hashCode());
          // gestisce il timeout e le eccezioni (per ogni invio viene clonato il
          // CortexMessage - il messaggio di ritorno sarà popolato con le lavorazioni del
          // sotto albero
          mapReplies.put(childToCall, new BotMessage());
          try {
            // assegna le variabili
            mapReplies.get(childToCall).setContext(azione.getContext());
            mapReplies.get(childToCall).setSender(this.toString());
            mapReplies.get(childToCall).setReceiver(childToCall.toString());
            if (azione.getWorkMessage().isGodMessage()) {
              mapReplies.get(childToCall).setGodMessage(true);
            }
            mapReplies.get(childToCall).setMessage(azione.getWorkMessage().getMessage());
            mapReplies.get(childToCall).setMapNlpReplies(azione.getWorkMessage().nlpReplies());
            mapReplies.get(childToCall).setMapChildrenExceptions(azione.getWorkMessage().childrenExceptions());
            mapReplies.get(childToCall).setMapImportanceReplies(azione.getWorkMessage().importanceReplies());
            mapReplies.get(childToCall).setMapUnderstandingReplies(azione.getWorkMessage().understandingReplies());
            mapReplies.get(childToCall).setMapDebugData(azione.getWorkMessage().debugData());
            mapReplies.get(childToCall).setAnswerRequests(azione.getWorkMessage().answerRequests());
            QueryChild queryChild = new QueryChild();
            queryChild.child = childToCall;
            queryChild.message = mapReplies.get(childToCall);
            // ScheduledExecutorService executor =
            // Executors.newScheduledThreadPool(corePoolSize);
            // executor.execute(queryChild);
            // executor.awaitTermination(azione.getChildrenTimeout(),
            // TimeUnit.MILLISECONDS);
            queryChild.run();
          } catch (Exception aa) {
            aa.printStackTrace();
            throw new HomunculusException(aa.getMessage());
          }
        }
      }
      ////////////////////////////////////////////////////////////////////
      // passaggio dopo i figli per prepare la risposta verso il padre. //
      ////////////////////////////////////////////////////////////////////
      try {
        getPostChildrensRules().start();
        getPostChildrensRules().fireRule(azione);
        getPostChildrensRules().stop();
        //System.out.println("mk -> " + azione.getWorkMessage().getMessage());
      } catch (HomunculusException aa) {
        aa.printStackTrace();
        // se viene generata un eccezione, viene registrata e la lavorazione finisce
        azione.getWorkMessage().addException(this, aa);
        azione.workEnded(); // anche se qui non servirebbe, permette comunque di tenere traccia dell'evento.
      }
    }
    ////////////////////// infine genera la risposta verso il padre (rimandando lo
    ////////////////////// stesso CortexMessage ricevuto e completato///////////////
    /*
     * ALL, BEST_IMPORTANCE_LIMIT_ANSWER, BEST_UNDERSTAND_LIMIT_ANSWER,
     * RULES_DEFINED, RULES_DEFINED_AND_ALL,
     * RULES_DEFINED_AND_BEST_UNDERSTAND_LIMIT_ANSWER,
     * RULES_DEFINED_AND_BEST_IMPORTANCE_LIMIT_ANSWER,
     * BEST_IMPORTANCE_LIMIT_ANSWER_AND_BEST_UNDERSTAND_LIMIT_ANSWER,
     * RULES_DEFINED_AND_BEST_IMPORTANCE_LIMIT_ANSWER_AND_BEST_UNDERSTAND_LIMIT_ANSWER
     */
    FatherReplyTemplate templateReply = azione.getFatherReplyTemplate();
    if (templateReply.equals(FatherReplyTemplate.ALL)) {
      for (AbstractHomunculus<? extends AbstractHomunculusConfiguration> repli : mapReplies.keySet()) {
        String originale = azione.getWorkMessage().getMessage();
        azione.getWorkMessage().setMessage(originale + replyConcatSeparator + mapReplies.get(repli).getMessage());
      }
    }
    return azione.getWorkMessage();
  }

  public void droolsTraining() throws LockedHomunculusException {
    preNlpRules = new DroolsRulesEngine();
    preNlpRules.setRules(configuration.preNlpRules);
    postNlpRules = new DroolsRulesEngine();
    postNlpRules.setRules(configuration.postNlpRules);
    postChildrensRules = new DroolsRulesEngine();
    postChildrensRules.setRules(configuration.postChildrenRules);
  }

  @Override
  public String toString() {
    return getShortName();
  }

  @Override
  public RulesEngine getPreNlpRules() {
    return preNlpRules;
  }

  @Override
  public RulesEngine getPostNlpRules() {
    return postNlpRules;
  }

  @Override
  public RulesEngine getPostChildrensRules() {
    return postChildrensRules;
  }

  @Override
  public Collection<String> getIntentExamples() {
    return this.configuration.intentExamples;
  }

  @Override
  public void deleteIntentExample(String intent) {
    this.configuration.intentExamples.remove(intent);
  }

  @Override
  public void addIntentExample(String intent) {
    this.configuration.intentExamples.add(intent);
  }

  @Override
  public boolean isOnline() {
    return online;
  }

  @Override
  public T getConfig() {
    return configuration;
  }

  @Override
  public void setConfig(T configuration) throws LockedHomunculusException {
    if (online == true)
      throw new LockedHomunculusException("Homunculus " + getShortName() + " is online...");
    if (this.checkConfig(configuration) == null || this.checkConfig(configuration).isEmpty()) {
      this.configuration = configuration;
    }
  }

  @Override
  public void handleMessage(Message<?> message) throws MessagingException {
    CortexMessage cortexMessage = new BotMessage();
    ConversationFact ritorno = null;
    try {
      ritorno = parseMessage(cortexMessage);
    } catch (HomunculusException e) {
      e.printStackTrace();
    }
    // TODO send reply message
    System.out.println(ritorno.toString());
  }

  @Override
  public String getShortName() {
    return configuration.name;
  }

  @Override
  public void setShortName(String name) throws LockedHomunculusException {
    if (online == true)
      throw new LockedHomunculusException("Homunculus " + getShortName() + " is online...");
    this.configuration.name = name;
  }

  @Override
  protected void finalize() throws Throwable {
    configuration = null;
    preNlpRules = null;
    postNlpRules = null;
    postChildrensRules = null;
    super.finalize();
  }

  @Override
  public void setPreNlpRules(ArrayList<DroolsRule> preNlpRules) throws LockedHomunculusException {
    if (online == true)
      throw new LockedHomunculusException("Homunculus " + getShortName() + " is online...");
    configuration.preNlpRules = preNlpRules;
  }

  @Override
  public void setPostNlpRules(ArrayList<DroolsRule> postNlpRules) throws LockedHomunculusException {
    if (online == true)
      throw new LockedHomunculusException("Homunculus " + getShortName() + " is online...");
    configuration.postNlpRules = postNlpRules;
  }

  @Override
  public void setPostChildrenRules(ArrayList<DroolsRule> postChildrenRules) throws LockedHomunculusException {
    if (online == true)
      throw new LockedHomunculusException("Homunculus " + getShortName() + " is online...");
    configuration.postChildrenRules = postChildrenRules;
  }

  @Override
  public boolean isSymbolicLink() {
    boolean r = false;
    if (configuration.realHomunculusName != null) {
      r = true;
    }
    return r;
  }

  @Override
  public boolean selfCheck() {
    return online;
  }

  @Override
  public void bootstrap() throws TrainingIntentMatcherException, LockedHomunculusException {
    train();
    online = true;
  }

  @Override
  public AbstractHomunculus<? extends AbstractHomunculusConfiguration> getRealParent() {
    return realFather;
  }

  @Override
  public Collection<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>> getChildren() {
    return children;
  }

  @Override
  public AbstractHomunculus<? extends AbstractHomunculusConfiguration> getParent() {
    return father;
  }

  @Override
  public void addChild(AbstractHomunculus<? extends AbstractHomunculusConfiguration> child)
      throws LockedHomunculusException {
    if (online == true)
      throw new LockedHomunculusException("Homunculus " + getShortName() + " is online...");
    children.add(child);

  }

  @Override
  public void setParent(AbstractHomunculus<? extends AbstractHomunculusConfiguration> father)
      throws LockedHomunculusException {
    if (online == true)
      throw new LockedHomunculusException("Homunculus " + getShortName() + " is online...");
    this.father = father;
  }

  @Override
  public void setRealParent(AbstractHomunculus<? extends AbstractHomunculusConfiguration> realFather)
      throws LockedHomunculusException {
    if (online == true)
      throw new LockedHomunculusException("Homunculus " + getShortName() + " is online...");
    this.realFather = realFather;
  }

}
