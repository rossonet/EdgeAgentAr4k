
package org.ar4k.agent.core.data.messages;

import org.springframework.messaging.MessageHeaders;

public class LongMessage extends InternalMessage<Long> {

	private static final long serialVersionUID = 668852623206868956L;
	private Long rawLong = null;
	private MessageHeaders header = null;

	@Override
	public Long getPayload() {
		return rawLong;
	}

	@Override
	public MessageHeaders getHeaders() {
		return header;
	}

	@Override
	public void setPayload(Long elaborateMessage) {
		rawLong = elaborateMessage;
	}

	@Override
	public void close() throws Exception {
		rawLong = null;
		header = null;
	}

	@Override
	public void setHeaders(MessageHeaders headers) {
		header = headers;

	}

}
