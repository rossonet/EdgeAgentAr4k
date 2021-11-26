package org.ar4k.agent.core.data.messages;

public class IntegerMessage extends InternalMessage<Integer> {

	private static final long serialVersionUID = 4369570405381824237L;
	private Integer rawInteger = null;

	@Override
	public Integer getPayload() {
		return rawInteger;
	}

	@Override
	public void setPayload(Integer elaborateMessage) {
		rawInteger = elaborateMessage;
	}

	@Override
	public void close() throws Exception {
		rawInteger = null;
	}

}
