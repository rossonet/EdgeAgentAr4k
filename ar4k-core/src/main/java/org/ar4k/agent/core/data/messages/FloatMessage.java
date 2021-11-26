package org.ar4k.agent.core.data.messages;

public class FloatMessage extends InternalMessage<Float> {

	private static final long serialVersionUID = 8935409506721578330L;
	private Float rawFloat = null;

	@Override
	public Float getPayload() {
		return rawFloat;
	}

	@Override
	public void setPayload(Float elaborateMessage) {
		rawFloat = elaborateMessage;
	}

	@Override
	public void close() throws Exception {
		rawFloat = null;
	}

}
