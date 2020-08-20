package org.ar4k.agent.core.data.messages;

import org.springframework.messaging.MessageHeaders;

public class DoubleMessage extends InternalMessage<Double> {

	private static final long serialVersionUID = -94717163505183160L;
	private Double rawDouble = null;
	private MessageHeaders header = null;

	@Override
	public Double getPayload() {
		return rawDouble;
	}

	@Override
	public MessageHeaders getHeaders() {
		return header;
	}

	@Override
	public void setPayload(Double elaborateMessage) {
		rawDouble = elaborateMessage;
	}

	@Override
	public void close() throws Exception {
		rawDouble = null;
		header = null;
	}

	@Override
	public void setHeaders(MessageHeaders headers) {
		header = headers;

	}

}
