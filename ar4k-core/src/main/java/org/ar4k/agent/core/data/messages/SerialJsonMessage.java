package org.ar4k.agent.core.data.messages;

import org.json.JSONObject;
import org.springframework.messaging.MessageHeaders;

public class SerialJsonMessage extends InternalMessage<JSONObject> {

	private static final long serialVersionUID = 7259562624995277392L;
	private JSONObject payload = null;
	private MessageHeaders headers = null;

	@Override
	public void setPayload(JSONObject payload) {
		this.payload = payload;
	}

	@Override
	public void setHeaders(MessageHeaders headers) {
		this.headers = headers;
	}

	@Override
	public JSONObject getPayload() {
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
