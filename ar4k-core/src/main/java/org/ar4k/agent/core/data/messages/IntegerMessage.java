package org.ar4k.agent.core.data.messages;

import org.springframework.messaging.MessageHeaders;

public class IntegerMessage extends InternalMessage<Integer> {

	private static final long serialVersionUID = 4369570405381824237L;
	private Integer rawInteger = null;
	private MessageHeaders header = null;

	@Override
	public Integer getPayload() {
		return rawInteger;
	}

	@Override
	public MessageHeaders getHeaders() {
		return header;
	}

	@Override
	public void setPayload(Integer elaborateMessage) {
		rawInteger = elaborateMessage;
	}

	@Override
	public void close() throws Exception {
		rawInteger = null;
		header = null;
	}

	@Override
	public void setHeaders(MessageHeaders headers) {
		header = headers;

	}

}
