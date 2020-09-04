package org.ar4k.agent.network;

public interface NetworkReceiver extends AutoCloseable {

	public enum NetworkStatus {
		INIT, ACTIVE, INACTIVE, FAULT
	}

	long getTunnelId();

	NetworkStatus getNetworkStatus();

	void exceptionPacketReceived(long sessionId, long messageUuid);

	void confirmPacketReceived(long sessionId, long ackMessageId, long lastReceivedAckMessageId);

}
