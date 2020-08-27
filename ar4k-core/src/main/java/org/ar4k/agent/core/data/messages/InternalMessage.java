package org.ar4k.agent.core.data.messages;

import org.springframework.messaging.MessageHeaders;

public abstract class InternalMessage<T> implements EdgeMessage<T> {

	private static final long serialVersionUID = 8423795771428874000L;

	public abstract void setPayload(T elaborateMessage);

	@SuppressWarnings("unchecked")
	public void setObjectPayload(Object object) {
		setPayload((T) object);
	}

	public abstract void setHeaders(MessageHeaders headers);

}
