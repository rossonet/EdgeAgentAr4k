package org.ar4k.agent.core.data.messages;

public class BooleanMessage extends InternalMessage<Boolean> {

	private static final long serialVersionUID = -2535147233102026382L;
	private Boolean rawBoolean = null;

	@Override
	public Boolean getPayload() {
		return rawBoolean;
	}

	@Override
	public void setPayload(Boolean elaborateMessage) {
		rawBoolean = elaborateMessage;
	}

	@Override
	public void close() throws Exception {
		rawBoolean = null;
	}

}
