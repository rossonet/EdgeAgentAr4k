package org.ar4k.agent.iot.serial;

import org.ar4k.agent.core.data.messages.InternalMessage;
import org.springframework.messaging.MessageHeaders;

public class SerialBytesMessage extends InternalMessage<Byte[]> {

	private static final long serialVersionUID = -8317240852093510543L;
	private Byte[] payload = null;
	private MessageHeaders headers = null;

	@Override
	public void setPayload(Byte[] payload) {
		this.payload = payload;
	}

	@Override
	public void setHeaders(MessageHeaders headers) {
		this.headers = headers;
	}

	@Override
	public Byte[] getPayload() {
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
