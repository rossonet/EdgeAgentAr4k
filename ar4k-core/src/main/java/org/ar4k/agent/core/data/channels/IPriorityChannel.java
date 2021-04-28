package org.ar4k.agent.core.data.channels;

import org.ar4k.agent.core.data.AbstractChannel;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.DataServiceOwner;
import org.ar4k.agent.core.interfaces.EdgeChannel;
import org.springframework.integration.channel.PriorityChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;

public class IPriorityChannel extends AbstractChannel implements PollableChannel {

	public IPriorityChannel(DataServiceOwner serviceOwner) {
		super(serviceOwner);
		final PriorityChannel c = new PriorityChannel();
		super.setChannel(c);
		super.setChannelType(Type.PRIORITY);
	}

	@Override
	public boolean send(Message<?> message, long timeout) {
		return getChannel().send(message, timeout);
	}

	@Override
	public Message<?> receive() {
		return ((PriorityChannel) getChannel()).receive();
	}

	@Override
	public Message<?> receive(long timeout) {
		return ((PriorityChannel) getChannel()).receive(timeout);
	}

	@Override
	public Class<? extends EdgeChannel> getChannelClass() {
		return this.getClass();
	}

}
