package org.ar4k.agent.core.data.messages;

public class DoubleMessage extends InternalMessage<Double> {

	private static final long serialVersionUID = -94717163505183160L;
	private Double rawDouble = null;

	@Override
	public Double getPayload() {
		return rawDouble;
	}

	@Override
	public void setPayload(Double elaborateMessage) {
		rawDouble = elaborateMessage;
	}

	@Override
	public void close() throws Exception {
		rawDouble = null;
	}

}
