
package org.ar4k.agent.core.data.messages;

public class LongMessage extends InternalMessage<Long> {

	private static final long serialVersionUID = 668852623206868956L;
	private Long rawLong = null;

	@Override
	public Long getPayload() {
		return rawLong;
	}

	@Override
	public void setPayload(Long elaborateMessage) {
		rawLong = elaborateMessage;
	}

	@Override
	public void close() throws Exception {
		rawLong = null;
	}

}
