package org.ar4k.agent.core.data.channels;

import org.ar4k.agent.core.data.AbstractChannel;
import org.ar4k.agent.core.interfaces.EdgeChannel;
import org.springframework.messaging.Message;

public class INoDataChannel extends AbstractChannel {

	@Override
	public Class<? extends EdgeChannel> getChannelClass() {
		return this.getClass();
	}

	@Override
	public boolean send(Message<?> message, long timeout) {
		// not used
		return false;
	}

}
