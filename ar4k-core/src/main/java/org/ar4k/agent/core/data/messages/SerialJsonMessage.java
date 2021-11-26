package org.ar4k.agent.core.data.messages;

import org.json.JSONObject;

public class SerialJsonMessage extends InternalMessage<JSONObject> {

	private static final long serialVersionUID = 7259562624995277392L;
	private JSONObject payload = null;

	@Override
	public void setPayload(JSONObject payload) {
		this.payload = payload;
	}

	@Override
	public JSONObject getPayload() {
		return payload;
	}

	@Override
	public void close() throws Exception {
		payload = null;
	}

}
