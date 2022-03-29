package org.ar4k.agent.tunnels.http2.beacon.server.cluster;

import java.io.IOException;
import java.util.List;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http2.beacon.IBeaconAgent;
import org.ar4k.agent.tunnels.http2.beacon.IBeaconServer;
import org.ar4k.agent.tunnels.http2.beacon.socket.server.ITunnelRunnerBeaconServer;
import org.ar4k.agent.tunnels.http2.grpc.beacon.AgentRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.CommandReplyRequest;

//TO______DO sviluppare beacon server cluster basato su hazelcast per grandi volumi di client

public class BeaconServerCluster implements Runnable, AutoCloseable, IBeaconServer {

	static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(BeaconServerCluster.class);

	@Override
	public void start() throws IOException {
		// Auto-generated method stub

	}

	@Override
	public void stop() {
		// Auto-generated method stub

	}

	@Override
	public void blockUntilShutdown() throws InterruptedException {
		// Auto-generated method stub

	}

	@Override
	public String getStatus() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public List<ITunnelRunnerBeaconServer> getTunnels() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public CommandReplyRequest waitReply(String idRequest, long defaultTimeOut) throws InterruptedException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public boolean isStopped() {
		// Auto-generated method stub
		return false;
	}

	@Override
	public int getPort() {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public int getDefaultPollTime() {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public void setDefaultPollTime(int defaultPollTime) {
		// Auto-generated method stub

	}

	@Override
	public List<IBeaconAgent> getAgentRegistered() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void sendFlashUdp() {
		// Auto-generated method stub

	}

	@Override
	public int getDefaultBeaconFlashMoltiplicator() {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public void setDefaultBeaconFlashMoltiplicator(int defaultBeaconFlashMoltiplicator) {
		// Auto-generated method stub

	}

	@Override
	public int getDiscoveryPort() {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public void setDiscoveryPort(int discoveryPort) {
		// Auto-generated method stub

	}

	@Override
	public String getBroadcastAddress() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void setBroadcastAddress(String broadcastAddress) {
		// Auto-generated method stub

	}

	@Override
	public String getStringDiscovery() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void setStringDiscovery(String stringDiscovery) {
		// Auto-generated method stub

	}

	@Override
	public boolean isAcceptAllCerts() {
		// Auto-generated method stub
		return false;
	}

	@Override
	public String getCertChainFile() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public String getPrivateKeyFile() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void clearOldData() {
		// Auto-generated method stub

	}

	@Override
	public List<AgentRequest> listAgentRequests() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void approveCsrRequest(String csr) {
		// Auto-generated method stub

	}

	@Override
	public void close() throws Exception {
		// Auto-generated method stub

	}

	@Override
	public void run() {
		// Auto-generated method stub

	}

}
