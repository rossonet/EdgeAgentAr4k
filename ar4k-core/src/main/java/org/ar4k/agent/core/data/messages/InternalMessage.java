package org.ar4k.agent.core.data.messages;

import org.springframework.messaging.MessageHeaders;

public abstract class InternalMessage<T> implements EdgeMessage<T> {

	private static final long serialVersionUID = 8423795771428874000L;

	private EdgeMessageHeaders headers = null;

	@Override
	@SuppressWarnings("unchecked")
	public void setObjectPayload(Object object) {
		setPayload((T) object);
	}

	@Override
	public EdgeMessageHeaders getHeaders() {
		return headers;
	}

	@Override
	public void setHeaders(EdgeMessageHeaders headers) {
		this.headers = headers;
	}

	@Override
	public void setHeaders(MessageHeaders originalHeaders) {
		this.headers = new EdgeMessageHeaders(originalHeaders);
	}

}
