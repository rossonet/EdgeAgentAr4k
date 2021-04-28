package org.ar4k.agent.tunnels.http2.beacon.server.cluster;

import java.io.IOException;
import java.util.List;

import org.ar4k.agent.core.interfaces.IBeaconServer;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http2.beacon.BeaconAgent;
import org.ar4k.agent.tunnels.http2.beacon.socket.server.TunnelRunnerBeaconServer;
import org.ar4k.agent.tunnels.http2.grpc.beacon.AgentRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.CommandReplyRequest;

//TODO sviluppare beacon server cluster basato su hazelcast per grandi volumi di client

public class BeaconServerCluster implements Runnable, AutoCloseable, IBeaconServer {

	static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconServerCluster.class.toString());

	@Override
	public void start() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void blockUntilShutdown() throws InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TunnelRunnerBeaconServer> getTunnels() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommandReplyRequest waitReply(String idRequest, long defaultTimeOut) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isStopped() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDefaultPollTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDefaultPollTime(int defaultPollTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<BeaconAgent> getAgentRegistered() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendFlashUdp() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getDefaultBeaconFlashMoltiplicator() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDefaultBeaconFlashMoltiplicator(int defaultBeaconFlashMoltiplicator) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getDiscoveryPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDiscoveryPort(int discoveryPort) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBroadcastAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBroadcastAddress(String broadcastAddress) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getStringDiscovery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setStringDiscovery(String stringDiscovery) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAcceptAllCerts() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCertChainFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrivateKeyFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearOldData() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<AgentRequest> listAgentRequests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void approveCsrRequest(String csr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
