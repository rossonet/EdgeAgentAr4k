package org.ar4k.agent.core.data.messages;

public class SerialStringMessage extends InternalMessage<String> {

	private static final long serialVersionUID = 577397693578436293L;
	private String payload = null;

	@Override
	public void setPayload(String payload) {
		this.payload = payload;
	}

	@Override
	public String getPayload() {
		return payload;
	}

	@Override
	public void close() throws Exception {
		payload = null;
	}

}
