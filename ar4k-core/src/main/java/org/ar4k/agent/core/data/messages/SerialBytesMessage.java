package org.ar4k.agent.core.data.messages;

public class SerialBytesMessage extends InternalMessage<Byte[]> {

	private static final long serialVersionUID = -8317240852093510543L;
	private Byte[] payload = null;

	@Override
	public void setPayload(Byte[] payload) {
		this.payload = payload;
	}

	@Override
	public Byte[] getPayload() {
		return payload;
	}

	@Override
	public void close() throws Exception {
		payload = null;
	}

}
