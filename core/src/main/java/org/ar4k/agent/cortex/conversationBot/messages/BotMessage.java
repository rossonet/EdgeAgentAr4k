package org.ar4k.agent.cortex.conversationBot.messages;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.ar4k.agent.cortex.OntologyTag;
import org.ar4k.agent.cortex.conversationBot.ai.IntentMatched;
import org.ar4k.agent.cortex.conversationBot.exceptions.HomunculusException;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculus;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;
import org.ar4k.agent.cortex.memory.BotTimeContextConversation;
import org.ar4k.agent.cortex.memory.TimeContextConversation;
import org.joda.time.Instant;
import org.springframework.messaging.MessageHeaders;

public class BotMessage implements CortexMessage {

  private static final long serialVersionUID = -6446411791307044530L;

  private String message = null;
  private String mittente = null;
  private String destinatario = null;
  private TimeContextConversation contesto = new BotTimeContextConversation();
  private Set<OntologyTag> tags = new HashSet<OntologyTag>();
  private boolean godMessage = false;

  private Instant generationTime = new Instant();

  private Map<AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<IntentMatched>> nlpReplies = new HashMap<AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<IntentMatched>>();

  private Map<AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<HomunculusException>> childrenExceptions = new HashMap<AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<HomunculusException>>();

  private Map<AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Double> importanceReplies = new HashMap<AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Double>();

  private Map<AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Double> understandingReplies = new HashMap<AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Double>();

  private Map<AbstractHomunculus<? extends AbstractHomunculusConfiguration>, String> debugData = new HashMap<AbstractHomunculus<? extends AbstractHomunculusConfiguration>, String>();

  private Collection<AbstractHomunculus<?>> answerRequests = new HashSet<AbstractHomunculus<?>>();

  private Double importance = .0;

  private Map<AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<CortexMessage>> childrenReplies = new HashMap<AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<CortexMessage>>();;

  @Override
  public Collection<OntologyTag> getTags() {
    return tags;
  }

  @Override
  public TimeContextConversation getContext() {
    return contesto;
  }

  @Override
  public void setContext(TimeContextConversation contesto) {
    this.contesto = contesto;
    this.contesto.addMemoryFact(this);
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String getSender() {
    return mittente;
  }

  @Override
  public void setSender(String mittente) {
    this.mittente = mittente;
  }

  @Override
  public String getReceiver() {
    return destinatario;
  }

  @Override
  public void setReceiver(String receiver) {
    destinatario = receiver;
  }

  @Override
  public void setTag(OntologyTag tag) {
    tags.add(tag);
  }

  @Override
  public Instant getGenerationTime() {
    return generationTime;
  }

  @Override
  public Object getPayload() {
    // TODO dopo poc
    return null;
  }

  @Override
  public MessageHeaders getHeaders() {
    // TODO dopopoc
    return null;
  }

  @Override
  public void addException(AbstractHomunculus<? extends AbstractHomunculusConfiguration> homunculus,
      HomunculusException exception) {
    if (childrenExceptions.get(homunculus) == null) {
      childrenExceptions.put(homunculus, new HashSet<HomunculusException>());
    }
    childrenExceptions.get(homunculus).add(exception);
  }

  @Override
  public void addNlpReply(AbstractHomunculus<? extends AbstractHomunculusConfiguration> homunculus,
      IntentMatched nlpReply) {
    if (nlpReplies.get(homunculus) == null) {
      nlpReplies.put(homunculus, (Collection<IntentMatched>) new ArrayList<IntentMatched>());
    }
    nlpReplies.get(homunculus).add(nlpReply);
  }

  @Override
  public void setImportance(Double value) {
    this.importance = value;

  }

  @Override
  public void requestAnswer(AbstractHomunculus<? extends AbstractHomunculusConfiguration> homunculus) {
    this.answerRequests.add(homunculus);
  }

  @Override
  public Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<IntentMatched>> nlpReplies() {
    return nlpReplies;
  }

  @Override
  public Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<HomunculusException>> childrenExceptions() {
    return childrenExceptions;
  }

  @Override
  public Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Double> importanceReplies() {
    return importanceReplies;
  }

  @Override
  public Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Double> understandingReplies() {
    return understandingReplies;
  }

  @Override
  public Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, String> debugData() {
    return debugData;
  }

  @Override
  public Collection<AbstractHomunculus<?>> answerRequests() {
    return answerRequests;
  }

  @Override
  public boolean isGodMessage() {
    return godMessage;
  }

  @Override
  public void setGodMessage(boolean godMessage) {
    this.godMessage = godMessage;
  }

  @Override
  public void setMapNlpReplies(
      Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<IntentMatched>> mapNlpReplies) {
    nlpReplies = (Map<AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<IntentMatched>>) mapNlpReplies;
  }

  @Override
  public void setMapChildrenExceptions(
      Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<HomunculusException>> mapChildrenExceptions) {
    childrenExceptions = (Map<AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<HomunculusException>>) mapChildrenExceptions;

  }

  @Override
  public void setMapImportanceReplies(
      Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Double> mapImportanceReplies) {
    importanceReplies = (Map<AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Double>) mapImportanceReplies;
  }

  @Override
  public void setMapUnderstandingReplies(
      Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Double> mapUnderstandingReplies) {
    understandingReplies = (Map<AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Double>) mapUnderstandingReplies;
  }

  @Override
  public void setMapDebugData(
      Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, String> mapDebugData) {
    debugData = (Map<AbstractHomunculus<? extends AbstractHomunculusConfiguration>, String>) mapDebugData;
  }

  @Override
  public void setAnswerRequests(Collection<AbstractHomunculus<?>> answerRequests) {
    this.answerRequests = answerRequests;
  }

  @Override
  public Double getImportance() {
    return importance;
  }

  @Override
  public void addChildrenReply(AbstractHomunculus<? extends AbstractHomunculusConfiguration> homunculus,
      CortexMessage cortexMessage) {
    if (childrenReplies.get(homunculus) == null) {
      childrenReplies.put(homunculus, new HashSet<CortexMessage>());
    }
    childrenReplies.get(homunculus).add(cortexMessage);
  }

  @Override
  public void setMapChildrenReplies(
      Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<CortexMessage>> mapChildrenReplies) {
    childrenReplies = (Map<AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<CortexMessage>>) mapChildrenReplies;
  }

  @Override
  public Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<CortexMessage>> childrenReplies() {
    return childrenReplies;
  }

}
