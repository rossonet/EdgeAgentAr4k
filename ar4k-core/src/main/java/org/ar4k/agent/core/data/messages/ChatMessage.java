package org.ar4k.agent.core.data.messages;

import org.ar4k.agent.mattermost.ChatPayload;

public class ChatMessage extends InternalMessage<ChatPayload> {

	private static final long serialVersionUID = -5476178503924474441L;
	private ChatPayload chatPayload = null;

	@Override
	public ChatPayload getPayload() {
		return chatPayload;
	}

	@Override
	public void setPayload(ChatPayload elaborateMessage) {
		chatPayload = elaborateMessage;
	}

	@Override
	public void close() throws Exception {
		chatPayload = null;
	}

}
