package org.ar4k.agent.tunnels.http.beacon.socket;

import java.io.Serializable;
import java.util.Date;

import org.ar4k.agent.exception.ExceptionNetworkEvent;
import org.ar4k.agent.exception.TimeoutNetworkEvent;

public final class LockUntilAck implements Serializable {

	private static final long serialVersionUID = 3207807089900926005L;
	private boolean ackReceived = false;
	private boolean exception = false;
	private boolean blocked = true;
	private final long timeRequest = new Date().getTime();
	private long messageId = 0;

	synchronized void waitAck(long messageId) throws InterruptedException, TimeoutNetworkEvent {
		this.messageId = messageId;
		this.wait(BeaconNetworkTunnel.SYNC_TIME_OUT);
		blocked = false;
		if (!ackReceived) {
			throw new TimeoutNetworkEvent("timeout event");
		}
		if (exception) {
			throw new ExceptionNetworkEvent("exception event");
		}
	}

	synchronized void ackReceived() {
		ackReceived = true;
		exception = false;
		this.notifyAll();
	}

	synchronized void resetReceived() {
		ackReceived = true;
		exception = true;
		this.notifyAll();
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("LockUntileAck [ackReceived=").append(ackReceived).append(", exception=").append(exception)
				.append(", blocked=").append(blocked).append(", timeRequest=").append(timeRequest)
				.append(", messageId=").append(messageId).append("]");
		return builder.toString();
	}

}
