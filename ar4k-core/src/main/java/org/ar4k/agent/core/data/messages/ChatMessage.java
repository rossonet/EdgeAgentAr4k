package org.ar4k.agent.core.data.messages;

import org.ar4k.agent.mattermost.ChatPayload;
import org.springframework.messaging.MessageHeaders;

public class ChatMessage extends InternalMessage<ChatPayload> {

	private static final long serialVersionUID = -5476178503924474441L;
	private ChatPayload chatPayload = null;
	private MessageHeaders header = null;

	@Override
	public ChatPayload getPayload() {
		return chatPayload;
	}

	@Override
	public MessageHeaders getHeaders() {
		return header;
	}

	@Override
	public void setPayload(ChatPayload elaborateMessage) {
		chatPayload = elaborateMessage;
	}

	@Override
	public void close() throws Exception {
		chatPayload = null;
		header = null;
	}

	@Override
	public void setHeaders(MessageHeaders headers) {
		header = headers;

	}

}
