package org.ar4k.agent.core.data.channels;

import org.ar4k.agent.core.data.AbstractChannel;
import org.ar4k.agent.core.data.EdgeChannel;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dispatcher.RoundRobinLoadBalancingStrategy;
import org.springframework.integration.support.management.SubscribableChannelManagement;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

public class IDirectChannel extends AbstractChannel implements SubscribableChannel, SubscribableChannelManagement {

	public IDirectChannel() {
		final DirectChannel c = new DirectChannel(new RoundRobinLoadBalancingStrategy());
		super.setChannel(c);
		super.setChannelType(Type.DIRECT);
	}

	@Override
	public boolean send(Message<?> message, long timeout) {
		return getChannel().send(message, timeout);
	}

	@Override
	public int getSubscriberCount() {
		return ((DirectChannel) getChannel()).getSubscriberCount();
	}

	@Override
	public boolean subscribe(MessageHandler handler) {
		return ((DirectChannel) getChannel()).subscribe(handler);
	}

	@Override
	public boolean unsubscribe(MessageHandler handler) {
		return ((DirectChannel) getChannel()).unsubscribe(handler);
	}

	public void setFailover(boolean failover) {
		((DirectChannel) getChannel()).setFailover(failover);
	}

	public void setMaxSubscribers(int maxSubscribers) {
		((DirectChannel) getChannel()).setMaxSubscribers(maxSubscribers);
	}

	@Override
	public Class<? extends EdgeChannel> getChannelClass() {
		return this.getClass();
	}

}
