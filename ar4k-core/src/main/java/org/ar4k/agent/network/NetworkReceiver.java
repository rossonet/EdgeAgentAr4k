package org.ar4k.agent.network;

public interface NetworkReceiver extends AutoCloseable {

	public static enum NetworkStatus {
		INIT, ACTIVE, INACTIVE, FAULT
	}

	long getTunnelId();

	NetworkStatus getNetworkStatus();

	long getPacketSend();

	long getPacketReceived();

	long getPacketError();

	long getPacketControl();

	int getWaitingPackagesCount();

	void incrementPacketSend();

	void incrementPacketReceived();

	void incrementPacketError();

	void incrementPacketControl();

	void exceptionPacketReceived(long messageUuid);

	void confirmPacketReceived(long messageUuid, long lastReceived);

}
