package org.ar4k.agent.core.data.messages;

import org.springframework.messaging.MessageHeaders;

public class LoggerMessage extends InternalMessage<String> {

	private static final long serialVersionUID = -5478252652357782408L;
	private String rawString = null;
	private MessageHeaders header = null;

	@Override
	public String getPayload() {
		return rawString;
	}

	@Override
	public MessageHeaders getHeaders() {
		return header;
	}

	@Override
	public void setPayload(String elaborateMessage) {
		rawString = elaborateMessage;
	}

	@Override
	public void close() throws Exception {
		rawString = null;
		header = null;
	}

	@Override
	public void setHeaders(MessageHeaders headers) {
		header = headers;

	}

}
