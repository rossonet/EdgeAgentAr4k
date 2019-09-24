package org.ar4k.agent.core.data.channels;

import org.ar4k.agent.core.data.Channel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

public class INoDataChannel extends Channel {

  @Override
  public Message<?> receive() {
    return null;
  }

  @Override
  public Message<?> receive(long timeout) {
    return null;
  }

  @Override
  public boolean send(Message<?> message, long timeout) {
    return false;
  }

  @Override
  public boolean subscribe(MessageHandler handler) {
    return false;
  }

  @Override
  public boolean unsubscribe(MessageHandler handler) {
    return false;
  }

}
