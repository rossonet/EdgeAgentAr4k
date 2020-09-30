package org.ar4k.agent.iot.serial;

import org.ar4k.agent.core.data.messages.InternalMessage;
import org.springframework.messaging.MessageHeaders;

public class SerialStringMessage extends InternalMessage<String> {

	private static final long serialVersionUID = 577397693578436293L;
	private String payload = null;
	private MessageHeaders headers = null;

	@Override
	public void setPayload(String payload) {
		this.payload = payload;
	}

	@Override
	public void setHeaders(MessageHeaders headers) {
		this.headers = headers;
	}

	@Override
	public String getPayload() {
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
