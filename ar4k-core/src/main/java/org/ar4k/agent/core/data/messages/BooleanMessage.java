package org.ar4k.agent.core.data.messages;

import org.springframework.messaging.MessageHeaders;

public class BooleanMessage extends InternalMessage<Boolean> {

	private static final long serialVersionUID = -2535147233102026382L;
	private Boolean rawBoolean = null;
	private MessageHeaders header = null;

	@Override
	public Boolean getPayload() {
		return rawBoolean;
	}

	@Override
	public MessageHeaders getHeaders() {
		return header;
	}

	@Override
	public void setPayload(Boolean elaborateMessage) {
		rawBoolean = elaborateMessage;
	}

	@Override
	public void close() throws Exception {
		rawBoolean = null;
		header = null;
	}

	@Override
	public void setHeaders(MessageHeaders headers) {
		header = headers;

	}

}
