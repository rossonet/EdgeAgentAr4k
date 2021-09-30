package org.ar4k.agent.tunnels.http2.beacon.client;

import org.ar4k.agent.tunnels.http2.grpc.beacon.Agent;

public class RemoteBeaconAgentHomunculus implements AutoCloseable {

	private Agent remoteAgent = null;

	@Override
	public void close() throws Exception {
		remoteAgent = null;
	}

	public Agent getRemoteAgent() {
		return remoteAgent;
	}

	public void setRemoteAgent(Agent remoteAgent) {
		this.remoteAgent = remoteAgent;
	}

}
