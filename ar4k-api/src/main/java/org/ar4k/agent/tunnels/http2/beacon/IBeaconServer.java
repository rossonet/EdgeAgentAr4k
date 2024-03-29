package org.ar4k.agent.tunnels.http2.beacon;

import java.io.IOException;
import java.util.List;

import org.ar4k.agent.tunnels.http2.beacon.socket.server.ITunnelRunnerBeaconServer;
import org.ar4k.agent.tunnels.http2.grpc.beacon.AgentRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.CommandReplyRequest;

public interface IBeaconServer {

	void start() throws IOException;

	void stop();

	void blockUntilShutdown() throws InterruptedException;

	String getStatus();

	List<ITunnelRunnerBeaconServer> getTunnels();

	CommandReplyRequest waitReply(String idRequest, long defaultTimeOut) throws InterruptedException;

	boolean isStopped();

	int getPort();

	int getDefaultPollTime();

	void setDefaultPollTime(int defaultPollTime);

	List<IBeaconAgent> getAgentRegistered();

	void sendFlashUdp();

	int getDefaultBeaconFlashMoltiplicator();

	void setDefaultBeaconFlashMoltiplicator(int defaultBeaconFlashMoltiplicator);

	int getDiscoveryPort();

	void setDiscoveryPort(int discoveryPort);

	String getBroadcastAddress();

	void setBroadcastAddress(String broadcastAddress);

	String getStringDiscovery();

	void setStringDiscovery(String stringDiscovery);

	boolean isAcceptAllCerts();

	String getCertChainFile();

	String getPrivateKeyFile();

	void clearOldData();

	List<AgentRequest> listAgentRequests();

	void approveCsrRequest(String csr);

}