package org.ar4k.agent.core.data.messages;

public class IndustrialMessage extends InternalMessage<IndustrialPayload> {

	private static final long serialVersionUID = 8188263696273811135L;
	private IndustrialPayload data = null;

	public IndustrialMessage(IndustrialPayload industrialPayload) {
		this.data = industrialPayload;
	}

	@Override
	public IndustrialPayload getPayload() {
		return data;
	}

	@Override
	public void setPayload(IndustrialPayload data) {
		this.data = data;
	}

	@Override
	public void close() throws Exception {
		data = null;
	}

}
