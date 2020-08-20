package org.ar4k.agent.core.data.channels;

import org.ar4k.agent.core.data.AbstractChannel;
import org.ar4k.agent.core.data.Ar4kChannel;
import org.springframework.messaging.Message;

public class INoDataChannel extends AbstractChannel {

	@Override
	public Class<? extends Ar4kChannel> getChannelClass() {
		return this.getClass();
	}

	@Override
	public boolean send(Message<?> message, long timeout) {
		// not used
		return false;
	}

}
