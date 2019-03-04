package org.ar4k.agent.cortex.conversationBot.messages;

import java.util.Collection;
import java.util.Map;

import org.ar4k.agent.cortex.conversationBot.ai.IntentMatched;
import org.ar4k.agent.cortex.conversationBot.exceptions.HomunculusException;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculus;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;
import org.springframework.messaging.Message;

public interface CortexMessage extends Message<Object>, ConversationFact {

  public String getMessage();

  public void setMessage(String message);

  public String getSender();

  public void setSender(String sender);

  public String getReceiver();

  public void setReceiver(String receiver);

  // public void addMessageReply(AbstractHomunculus<? extends
  // AbstractHomunculusConfiguration> homunculus, String reply);

  public void addException(AbstractHomunculus<? extends AbstractHomunculusConfiguration> homunculus,
      HomunculusException exception);

  public void addNlpReply(AbstractHomunculus<? extends AbstractHomunculusConfiguration> homunculus,
      IntentMatched nlpReply);

  public void setImportance(Double value);

  public Double getImportance();

  public void addChildrenReply(AbstractHomunculus<? extends AbstractHomunculusConfiguration> homunculus,
      CortexMessage cortexMessage);

  public void setMapChildrenReplies(
      Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<CortexMessage>> mapChildrenReplies);

  public Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<CortexMessage>> childrenReplies();

  public void requestAnswer(AbstractHomunculus<? extends AbstractHomunculusConfiguration> homunculus);

  public Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<IntentMatched>> nlpReplies();

  public Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<HomunculusException>> childrenExceptions();

  public Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Double> importanceReplies();

  public Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Double> understandingReplies();

  public Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, String> debugData();

  public void setMapNlpReplies(
      Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<IntentMatched>> mapNlpReplies);

  public void setMapChildrenExceptions(
      Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Collection<HomunculusException>> mapChildrenExceptions);

  public void setMapImportanceReplies(
      Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Double> mapImportanceReplies);

  public void setMapUnderstandingReplies(
      Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, Double> mapUnderstandingReplies);

  public void setMapDebugData(
      Map<? extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>, String> mapDebugData);

  public Collection<AbstractHomunculus<?>> answerRequests();

  public void setAnswerRequests(Collection<AbstractHomunculus<?>> answerRequests);

  public boolean isGodMessage();

  public void setGodMessage(boolean godMessage);

}
