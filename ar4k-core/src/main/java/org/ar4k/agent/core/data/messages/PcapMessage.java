package org.ar4k.agent.core.data.messages;

public class PcapMessage extends InternalMessage<PcapPayload> {

	private static final long serialVersionUID = -1639142951156315942L;
	private PcapPayload payload = null;

	@Override
	public void setPayload(PcapPayload payload) {
		this.payload = payload;
	}

	@Override
	public PcapPayload getPayload() {
		return payload;
	}

	@Override
	public void close() throws Exception {
		payload = null;
	}

}
