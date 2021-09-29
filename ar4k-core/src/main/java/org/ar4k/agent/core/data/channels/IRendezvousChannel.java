package org.ar4k.agent.core.data.channels;

import org.ar4k.agent.core.data.AbstractChannel;
import org.ar4k.agent.core.data.DataServiceOwner;
import org.springframework.integration.channel.RendezvousChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;

public class IRendezvousChannel extends AbstractChannel implements PollableChannel {

	public IRendezvousChannel(DataServiceOwner serviceOwner) {
		super(serviceOwner);
		final RendezvousChannel c = new RendezvousChannel();
		super.setChannel(c);
		super.setChannelType(Type.RENDEZVOUS);
	}

	@Override
	public boolean send(Message<?> message, long timeout) {
		return getChannel().send(message, timeout);
	}

	@Override
	public Message<?> receive() {
		return ((RendezvousChannel) getChannel()).receive();
	}

	@Override
	public Message<?> receive(long timeout) {
		return ((RendezvousChannel) getChannel()).receive(timeout);
	}

	@Override
	public Class<? extends EdgeChannel> getChannelClass() {
		return this.getClass();
	}

}
