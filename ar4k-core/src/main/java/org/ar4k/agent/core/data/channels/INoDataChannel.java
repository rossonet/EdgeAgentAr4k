package org.ar4k.agent.core.data.channels;

import org.ar4k.agent.core.data.Ar4kChannel;
import org.ar4k.agent.core.data.AbstractChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

public class INoDataChannel extends AbstractChannel {

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

  @Override
  public Class<? extends Ar4kChannel> getChannelClass() {
    return this.getClass();
  }

}
