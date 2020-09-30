package org.ar4k.agent.core.data.messages;

import org.springframework.messaging.MessageHeaders;

public class FloatMessage extends InternalMessage<Float> {

	private static final long serialVersionUID = 8935409506721578330L;
	private Float rawFloat = null;
	private MessageHeaders header = null;

	@Override
	public Float getPayload() {
		return rawFloat;
	}

	@Override
	public MessageHeaders getHeaders() {
		return header;
	}

	@Override
	public void setPayload(Float elaborateMessage) {
		rawFloat = elaborateMessage;
	}

	@Override
	public void close() throws Exception {
		rawFloat = null;
		header = null;
	}

	@Override
	public void setHeaders(MessageHeaders headers) {
		header = headers;

	}

}
