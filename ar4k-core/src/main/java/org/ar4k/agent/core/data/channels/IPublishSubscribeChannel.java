package org.ar4k.agent.core.data.channels;

import org.ar4k.agent.core.data.AbstractChannel;
import org.ar4k.agent.core.interfaces.DataServiceOwner;
import org.ar4k.agent.core.interfaces.EdgeChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.support.management.SubscribableChannelManagement;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

public class IPublishSubscribeChannel extends AbstractChannel
		implements SubscribableChannel, SubscribableChannelManagement {

	public IPublishSubscribeChannel(DataServiceOwner serviceOwner) {
		super(serviceOwner);
		final PublishSubscribeChannel c = new PublishSubscribeChannel();
		super.setChannel(c);
		super.setChannelType(Type.PUBLISH_SUBSCRIBE);
	}

	@Override
	public boolean send(Message<?> message, long timeout) {
		return getChannel().send(message, timeout);
	}

	@Override
	public int getSubscriberCount() {
		return ((PublishSubscribeChannel) getChannel()).getSubscriberCount();
	}

	@Override
	public boolean subscribe(MessageHandler handler) {
		return ((PublishSubscribeChannel) getChannel()).subscribe(handler);
	}

	@Override
	public boolean unsubscribe(MessageHandler handler) {
		return ((PublishSubscribeChannel) getChannel()).unsubscribe(handler);
	}

	public void setMaxSubscribers(int maxSubscribers) {
		((PublishSubscribeChannel) getChannel()).setMaxSubscribers(maxSubscribers);
	}

	@Override
	public Class<? extends EdgeChannel> getChannelClass() {
		return this.getClass();
	}

}
