package org.ar4k.agent.core.data.messages;

import org.springframework.messaging.MessageHeaders;

public class PcapMessage extends InternalMessage<PcapPayload> {

	private static final long serialVersionUID = -1639142951156315942L;
	private PcapPayload payload = null;
	private MessageHeaders headers = null;

	@Override
	public void setPayload(PcapPayload payload) {
		this.payload = payload;
	}

	public void setHeaders(MessageHeaders headers) {
		this.headers = headers;
	}

	@Override
	public PcapPayload getPayload() {
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
