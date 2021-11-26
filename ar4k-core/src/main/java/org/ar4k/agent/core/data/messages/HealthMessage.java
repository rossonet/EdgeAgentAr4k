package org.ar4k.agent.core.data.messages;

public class HealthMessage extends InternalMessage<String> {

	private static final long serialVersionUID = -1177546110648083596L;
	private String rawString = null;

	@Override
	public String getPayload() {
		return rawString;
	}

	@Override
	public void setPayload(String elaborateMessage) {
		rawString = elaborateMessage;
	}

	@Override
	public void close() throws Exception {
		rawString = null;
	}

}
