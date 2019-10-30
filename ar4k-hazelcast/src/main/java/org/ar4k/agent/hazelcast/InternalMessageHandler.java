package org.ar4k.agent.hazelcast;

import org.ar4k.agent.core.data.AbstractChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.hazelcast.core.ITopic;

public class InternalMessageHandler implements MessageHandler {

  private ITopic<Object> target = null;
  private AbstractChannel source = null;

  public InternalMessageHandler(AbstractChannel source, ITopic<Object> target) {
    this.target = target;
    this.source = source;
  }

  @Override
  public void handleMessage(Message<?> message) throws MessagingException {
    // TODO Auto-generated method stub

  }

  public ITopic<Object> getTarget() {
    return target;
  }

  public void setTarget(ITopic<Object> target) {
    this.target = target;
  }

  public AbstractChannel getSource() {
    return source;
  }

  public void setSource(AbstractChannel source) {
    this.source = source;
  }

}
