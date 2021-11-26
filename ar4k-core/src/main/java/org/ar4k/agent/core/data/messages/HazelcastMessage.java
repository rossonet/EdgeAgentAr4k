package org.ar4k.agent.core.data.messages;

public class HazelcastMessage extends InternalMessage<Object> {

	private static final long serialVersionUID = 2148550503181500278L;
	private Object payload = null;

	@Override
	public void setPayload(Object payload) {
		this.payload = payload;
	}

	@Override
	public Object getPayload() {
		return payload;
	}

	@Override
	public void close() throws Exception {
		payload = null;
	}

}
