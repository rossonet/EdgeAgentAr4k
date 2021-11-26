package org.ar4k.agent.core.data.messages;

public class LoggerMessage extends InternalMessage<String> {

	private static final long serialVersionUID = -5478252652357782408L;
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
