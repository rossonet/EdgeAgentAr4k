package org.ar4k.agent.core.data.messages;

import org.json.JSONObject;

public class JSONMessage extends InternalMessage<JSONObject> {

	private static final long serialVersionUID = 115570475166086278L;
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
