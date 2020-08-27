package org.ar4k.agent.network;

import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.ResponseNetworkChannel;

public interface NetworkTunnel extends AutoCloseable {
	public static enum NetworkModeRequest {
		SERVER_TO_BYTES_TCP, BYTES_TO_SERVER_TCP, SERVER_TO_BYTES_UDP, BYTES_TO_SERVER_UDP
	}

	NetworkConfig getConfig();

	void kill();

	NetworkReceiver getNetworkReceiver();

	long getPacketSend();

	long getPacketReceived();

	long getPacketControl();

	long getPacketError();

	void init() throws ServiceInitException;

	void beaconObserverComplete(long targetId);

	void setRemoteAgent(Agent remoteAgent);

	void setResponseNetworkChannel(ResponseNetworkChannel response);

	long getTunnelId();

	Agent getRemoteAgent();

	void selfCheckIfNeeded();

}
