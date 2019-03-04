package org.ar4k.agent.cortex.actions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Ar4kStaticLoggerBinder;
import org.ar4k.agent.cortex.OntologyTag;
import org.ar4k.agent.cortex.conversationBot.ai.IntentMatched;
import org.ar4k.agent.cortex.conversationBot.exceptions.DisabledReplyException;
import org.ar4k.agent.cortex.conversationBot.exceptions.IncompleteDataException;
import org.ar4k.agent.cortex.conversationBot.exceptions.NoHaveReplyException;
import org.ar4k.agent.cortex.conversationBot.exceptions.UnderstandingException;
import org.ar4k.agent.cortex.conversationBot.messages.CortexMessage;
import org.ar4k.agent.cortex.conversationBot.messages.Probe;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculus;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;
import org.ar4k.agent.cortex.memory.TimeContextConversation;
import org.ar4k.agent.cortex.memory.TimeContextConversation.Focus;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.springframework.messaging.MessageHeaders;

public class DefaultActions<C extends CortexMessage, T extends TimeContextConversation, H extends AbstractHomunculus<? extends AbstractHomunculusConfiguration>>
    implements Actions<C, T, H> {

  private static final Logger logger = Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(Anima.class.toString());

  private C message = null;
  private T context = null;
  private H homunculus = null;
  private FatherReplyTemplate fatherReplyTemplate = FatherReplyTemplate.ALL; // FatherReplyTemplate.RULES_DEFINED_AND_BEST_IMPORTANCE_LIMIT_ANSWER_AND_BEST_UNDERSTAND_LIMIT_ANSWER;
  private Double importanceLimit = 0.5;
  private Double understandLimit = 0.5;
  private WorkingSteps workingSteps = WorkingSteps.PRE_NLP;
  private ChildrenRequestTemplate childrenRequestTemplate = ChildrenRequestTemplate.OVER_UNDERSTAND_LIMIT_AND_ANSWER;
  private boolean end = false;
  private int timeOutClidren = 1000;

  public DefaultActions(C message, H runnerHomunculus) {
    this.message = message;
    this.context = (T) message.getContext();
    this.homunculus = runnerHomunculus;
  }

  @Override
  public String getMessage() {
    return message.getMessage();
  }

  @Override
  public String getSender() {
    return message.getSender();
  }

  @Override
  public String getReceiver() {
    return message.getReceiver();
  }

  @Override
  public Instant getArriveTime() {
    return message.getGenerationTime();
  }

  @Override
  public boolean changeFocus(String newFocus) {
    return context.changeFocus(Focus.valueOf(newFocus));
  }

  @Override
  public String getLocale() {
    return context.getLocale().toLanguageTag();
  }

  @Override
  public Object getAttribute(String index) {
    return context.getAttribute(index);
  }

  @Override
  public void setAttribute(String index, Object value) {
    context.addAttribute(index, value);
  }

  @Override
  public void deleteAttribute(String index) {
    context.removeAttribute(index);
  }

  @Override
  public Object getLongTermAttribute(String index) {
    return context.getLongTermAttribute(index);
  }

  @Override
  public void setLongTermAttribute(String index, Object value) {
    context.addLongTermAttribute(index, value);
  }

  @Override
  public void deleteLongTermAttribute(String index) {
    context.removeLongTermAttribute(index);
  }

  @Override
  public Object getPayload() {
    return message.getPayload();
  }

  @Override
  public MessageHeaders getHeaders() {
    return message.getHeaders();
  }

  @Override
  public Collection<OntologyTag> getTags() {
    return message.getTags();
  }

  @Override
  public T getContext() {
    return context;
  }

  @Override
  public void logMessage(String message) {
    System.out.println(message);
    logger.info(message);
  }

  @Override
  public C getWorkMessage() {
    return message;
  }

  @Override
  public void setWorkMessage(C cortexMessage) {
    message = cortexMessage;
  }

  @Override
  public H getWorkerHomunculus() {
    return homunculus;
  }

  @Override
  public void setWorkerHomunculus(H homunculus) {
    this.homunculus = homunculus;
  }

  @Override
  public Double getImportanceLimit() {
    return importanceLimit;
  }

  @Override
  public void setImportanceLimit(Double limit) {
    importanceLimit = limit;
  }

  @Override
  public Double getUnderstandLimit() {
    return understandLimit;
  }

  @Override
  public void seUnderstandLimit(Double limit) {
    understandLimit = limit;
  }

  @Override
  public WorkingSteps getWorkingStep() {
    return workingSteps;
  }

  @Override
  public void setWorkingStep(WorkingSteps step) {
    workingSteps = step;
  }

  @Override
  public ChildrenRequestTemplate getChildrenDispatcherTemplate() {
    return childrenRequestTemplate;
  }

  @Override
  public void setChildrenDispatcherTemplate(ChildrenRequestTemplate template) {
    childrenRequestTemplate = template;
  }

  @Override
  public IntentMatched getFatherNlpReply() {
    IntentMatched r = null;
    Collection<? extends IntentMatched> fatherRepliesAll = message.nlpReplies().get(homunculus.getParent());
    for (IntentMatched reply : fatherRepliesAll) {
      if (reply.getChild().equals(homunculus)) {
        r = reply;
        break;
      }
    }
    return r;
  }

  @Override
  public IntentMatched getBestNlpReply() {
    IntentMatched r = null;
    Collection<? extends IntentMatched> myRepliesAll = message.nlpReplies().get(homunculus);
    for (IntentMatched reply : myRepliesAll) {
      if (r == null || r.getScore() < reply.getScore()) {
        r = reply;
      }
    }
    return r;
  }

  @Override
  public IntentMatched getWorseNlpReply() {
    IntentMatched r = null;
    Collection<? extends IntentMatched> myRepliesAll = message.nlpReplies().get(homunculus);
    for (IntentMatched reply : myRepliesAll) {
      if (r == null || r.getScore() > reply.getScore()) {
        r = reply;
      }
    }
    return r;
  }

  @Override
  public Collection<? extends IntentMatched> getNlpReplies() {
    return message.nlpReplies().get(homunculus);
  }

  @Override
  public Map<H, C> getChildrenReplies() {
    Map<H, C> r = new HashMap<H, C>();
    for (AbstractHomunculus<? extends AbstractHomunculusConfiguration> h : homunculus.getChildren()) {
      r.put((H) h, (C) message.childrenReplies().get(h));
    }
    return r;
  }

  @Override
  public C getMostImportantChildrenReply() {
    C messRep = null;
    Double last = .0;
    Map<H, C> mapReplies = getChildrenReplies();
    for (H r : mapReplies.keySet()) {
      if (messRep == null || message.importanceReplies().get(r) > last) {
        last = message.importanceReplies().get(r);
        messRep = mapReplies.get(r);
      }
    }
    return messRep;
  }

  @Override
  public C getBestUnderstandingChildrenReply() {
    C messRep = null;
    Double last = .0;
    Map<H, C> mapReplies = getChildrenReplies();
    for (H r : mapReplies.keySet()) {
      if (messRep == null || message.understandingReplies().get(r) > last) {
        last = message.understandingReplies().get(r);
        messRep = mapReplies.get(r);
      }
    }
    return messRep;
  }

  @Override
  public void disableChildrenReply(H children) {
    message.addException(children, new DisabledReplyException("disabled by " + homunculus));
  }

  @Override
  public boolean isNlpFired() {
    boolean r = true;
    if (getWorkingStep().equals(WorkingSteps.PRE_NLP)) {
      r = false;
    }
    return r;
  }

  @Override
  public boolean areChildrenFired() {
    boolean r = false;
    if (getWorkingStep().equals(WorkingSteps.POST_CHILDREN)) {
      r = true;
    }
    return r;
  }

  @Override
  public void setContext(T context) {
    this.context = context;
  }

  @Override
  public void fireUnderstandingException(String message) throws UnderstandingException {
    throw new UnderstandingException(message);

  }

  @Override
  public void fireIncompleteDataException(String message) throws IncompleteDataException {
    throw new IncompleteDataException(message);
  }

  @Override
  public void fireNoHaveReplyException(String message) throws NoHaveReplyException {
    throw new NoHaveReplyException(message);
  }

  @Override
  public Object runExternalMethod(String methodName, Object data) {
    // TODO da fare dopo poc
    return null;
  }

  @Override
  public String getAuthenticatedTokenPrimaryName() {
    // TODO da fare dopo poc
    return null;
  }

  @Override
  public Collection<String> getAuthenticatedTokenGroups() {
    // TODO da fare dopo poc
    return null;
  }

  @Override
  public void logoutSession() {
    // TODO da fare dopo poc

  }

  @Override
  public void addProbe(Probe probe) {
    // TODO da fare dopo poc
  }

  @Override
  public Object getGlobalData(String index) {
    return homunculus.bot.getBotStorageObject(index);
  }

  @Override
  public FatherReplyTemplate getFatherReplyTemplate() {
    return fatherReplyTemplate;
  }

  @Override
  public void setFatherReplyTemplate(FatherReplyTemplate replyTemplate) {
    fatherReplyTemplate = replyTemplate;
  }

  @Override
  public void workEnded() {
    end = true;
  }

  @Override
  public boolean isWorkEnded() {
    return end;
  }

  @Override
  public int getChildrenTimeout() {
    return timeOutClidren;
  }

  @Override
  public void setChildrenTimeout(int timeout) {
    timeOutClidren = timeout;

  }

  @Override
  public void setMessageReply(String txtMessage) {
    message.setMessage(txtMessage);
  }

}
