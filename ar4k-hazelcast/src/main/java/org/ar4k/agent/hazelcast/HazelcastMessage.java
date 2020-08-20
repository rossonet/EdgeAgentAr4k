package org.ar4k.agent.hazelcast;

import org.ar4k.agent.core.data.messages.InternalMessage;
import org.springframework.messaging.MessageHeaders;

public class HazelcastMessage extends InternalMessage<Object> {

	private static final long serialVersionUID = 2148550503181500278L;
	private Object payload = null;
	private MessageHeaders headers = null;

	@Override
	public void setPayload(Object payload) {
		this.payload = payload;
	}

	@Override
	public void setHeaders(MessageHeaders headers) {
		this.headers = headers;
	}

	@Override
	public Object getPayload() {
		return payload;
	}

	@Override
	public MessageHeaders getHeaders() {
		return headers;
	}

	@Override
	public void close() throws Exception {
		payload = null;
		headers = null;
	}

}
