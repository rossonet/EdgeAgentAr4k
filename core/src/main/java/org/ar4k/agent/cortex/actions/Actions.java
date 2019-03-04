package org.ar4k.agent.cortex.actions;

import java.util.Collection;
import java.util.Map;

import org.ar4k.agent.cortex.OntologyTag;
import org.ar4k.agent.cortex.conversationBot.ai.IntentMatched;
import org.ar4k.agent.cortex.conversationBot.exceptions.IncompleteDataException;
import org.ar4k.agent.cortex.conversationBot.exceptions.NoHaveReplyException;
import org.ar4k.agent.cortex.conversationBot.exceptions.UnderstandingException;
import org.ar4k.agent.cortex.conversationBot.messages.CortexMessage;
import org.ar4k.agent.cortex.conversationBot.messages.Probe;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculus;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;
import org.ar4k.agent.cortex.memory.TimeContextConversation;
import org.joda.time.Instant;
import org.springframework.messaging.MessageHeaders;

/**
 * rappresenta l'interfaccia minima per collegare callback a drools
 * 
 * @author andrea
 *
 */
public interface Actions<C extends CortexMessage, T extends TimeContextConversation, H extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>> {

  public static enum WorkingSteps {
    PRE_NLP, POST_NLP, POST_CHILDREN
  }

  public static enum ChildrenRequestTemplate {
    ALL_CHILDREN, BETTER_UNDERSTAND, BETTER_UNDERSTAND_AND_ANSWER, ALL_WITH_FOUND_ENTITY,
    ALL_WITH_FOUND_ENTITY_AND_ANSWER, OVER_UNDERSTAND_LIMIT, OVER_UNDERSTAND_LIMIT_AND_ANSWER
  }

  public static enum FatherReplyTemplate {
    ALL, BEST_IMPORTANCE_LIMIT_ANSWER, BEST_UNDERSTAND_LIMIT_ANSWER, RULES_DEFINED, RULES_DEFINED_AND_ALL,
    RULES_DEFINED_AND_BEST_UNDERSTAND_LIMIT_ANSWER, RULES_DEFINED_AND_BEST_IMPORTANCE_LIMIT_ANSWER,
    BEST_IMPORTANCE_LIMIT_ANSWER_AND_BEST_UNDERSTAND_LIMIT_ANSWER,
    RULES_DEFINED_AND_BEST_IMPORTANCE_LIMIT_ANSWER_AND_BEST_UNDERSTAND_LIMIT_ANSWER
  }

  public C getWorkMessage();

  public void setWorkMessage(C cortexMessage);

  public H getWorkerHomunculus();

  public void setWorkerHomunculus(H homunculus);

  public Double getImportanceLimit();

  public void setImportanceLimit(Double limit);

  public Double getUnderstandLimit();

  public void seUnderstandLimit(Double limit);

  public String getMessage();

  public String getSender();

  public String getReceiver();

  public Instant getArriveTime();

  public WorkingSteps getWorkingStep();

  public void setWorkingStep(WorkingSteps step);

  public ChildrenRequestTemplate getChildrenDispatcherTemplate();

  public void setChildrenDispatcherTemplate(ChildrenRequestTemplate childrenTemplate);

  public FatherReplyTemplate getFatherReplyTemplate();

  public void setFatherReplyTemplate(FatherReplyTemplate replyTemplate);

  public boolean changeFocus(String newFocus);

  public String getLocale();

  public Object getAttribute(String index);

  public void setAttribute(String index, Object value);

  public void deleteAttribute(String index);

  public Object getLongTermAttribute(String index);

  public void setLongTermAttribute(String index, Object value);

  public void deleteLongTermAttribute(String index);

  public IntentMatched getFatherNlpReply();

  public Collection<? extends IntentMatched> getNlpReplies();

  public IntentMatched getBestNlpReply();

  public IntentMatched getWorseNlpReply();

  public C getMostImportantChildrenReply();

  public C getBestUnderstandingChildrenReply();

  public void disableChildrenReply(H children);

  public Object getPayload();

  public MessageHeaders getHeaders();

  public boolean isNlpFired();

  public boolean areChildrenFired();

  public Collection<? extends OntologyTag> getTags();

  public T getContext();

  public void setContext(T context);

  public void logMessage(String message);

  public void fireUnderstandingException(String message) throws UnderstandingException;

  public void fireIncompleteDataException(String message) throws IncompleteDataException;

  public void fireNoHaveReplyException(String message) throws NoHaveReplyException;

  public void workEnded();

  public Object runExternalMethod(String methodName, Object data);

  public String getAuthenticatedTokenPrimaryName();

  public Collection<String> getAuthenticatedTokenGroups();

  public void logoutSession();

  public void addProbe(Probe probe);

  public Object getGlobalData(String index);

  public boolean isWorkEnded();

  public int getChildrenTimeout();

  public void setChildrenTimeout(int timeout);

  Map<H, C> getChildrenReplies();

  void setMessageReply(String txtMessage);

}
