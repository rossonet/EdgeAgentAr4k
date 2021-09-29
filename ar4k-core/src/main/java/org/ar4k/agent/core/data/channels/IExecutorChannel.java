package org.ar4k.agent.core.data.channels;

import java.util.concurrent.Executors;

import org.ar4k.agent.core.data.AbstractChannel;
import org.ar4k.agent.core.data.DataServiceOwner;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.dispatcher.RoundRobinLoadBalancingStrategy;
import org.springframework.integration.support.management.SubscribableChannelManagement;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

public class IExecutorChannel extends AbstractChannel implements SubscribableChannel, SubscribableChannelManagement {

	public IExecutorChannel(DataServiceOwner serviceOwner) {
		super(serviceOwner);
		final ExecutorChannel c = new ExecutorChannel(Executors.newFixedThreadPool(10),
				new RoundRobinLoadBalancingStrategy());
		super.setChannel(c);
		super.setChannelType(Type.EXECUTOR);
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
	public Class<? extends EdgeChannel> getChannelClass() {
		return this.getClass();
	}

}
