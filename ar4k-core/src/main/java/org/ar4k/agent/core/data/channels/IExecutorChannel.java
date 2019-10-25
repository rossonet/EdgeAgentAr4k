package org.ar4k.agent.core.data.channels;

import java.util.concurrent.Executors;

import org.ar4k.agent.core.data.Ar4kChannel;
import org.ar4k.agent.core.data.Channel;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.dispatcher.RoundRobinLoadBalancingStrategy;
import org.springframework.integration.support.management.SubscribableChannelManagement;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

public class IExecutorChannel extends Channel implements SubscribableChannel, SubscribableChannelManagement {

  public IExecutorChannel() {
    ExecutorChannel c = new ExecutorChannel(Executors.newFixedThreadPool(10), new RoundRobinLoadBalancingStrategy());
    super.setChannel(c);
    super.setChannelType(Type.ExecutorChannel);
  }

  @Override
  public boolean send(Message<?> message, long timeout) {
    return getChannel().send(message, timeout);
  }

  @Override
  public int getSubscriberCount() {
    return ((ExecutorChannel) getChannel()).getSubscriberCount();
  }

  @Override
  public boolean subscribe(MessageHandler handler) {
    return ((ExecutorChannel) getChannel()).subscribe(handler);
  }

  @Override
  public boolean unsubscribe(MessageHandler handler) {
    return ((ExecutorChannel) getChannel()).unsubscribe(handler);
  }

  public void setFailover(boolean failover) {
    ((ExecutorChannel) getChannel()).setFailover(failover);
  }

  public void setMaxSubscribers(int maxSubscribers) {
    ((ExecutorChannel) getChannel()).setMaxSubscribers(maxSubscribers);
  }

  @Override
  public Message<?> receive() {
    return null;
  }

  @Override
  public Message<?> receive(long timeout) {
    return null;
  }

  @Override
  public Class<? extends Ar4kChannel> getChannelClass() {
    return this.getClass();
  }

}
