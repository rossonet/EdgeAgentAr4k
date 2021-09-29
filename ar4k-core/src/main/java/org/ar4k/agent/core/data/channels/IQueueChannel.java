package org.ar4k.agent.core.data.channels;

import org.ar4k.agent.core.data.AbstractChannel;
import org.ar4k.agent.core.data.DataServiceOwner;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;

public class IQueueChannel extends AbstractChannel implements PollableChannel {

	public IQueueChannel(DataServiceOwner serviceOwner) {
		super(serviceOwner);
		final QueueChannel c = new QueueChannel(50);
		super.setChannel(c);
		super.setChannelType(Type.QUEUE);
	}

	@Override
	public boolean send(Message<?> message, long timeout) {
		return getChannel().send(message, timeout);
	}

	@Override
	public Message<?> receive() {
		return ((QueueChannel) getChannel()).receive();
	}

	@Override
	public Message<?> receive(long timeout) {
		return ((QueueChannel) getChannel()).receive(timeout);
	}

	@Override
	public Class<? extends EdgeChannel> getChannelClass() {
		return this.getClass();
	}

}
