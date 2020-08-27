package org.ar4k.agent.tunnels.http.beacon;

import org.ar4k.agent.rpc.IHomunculus;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;

public class RemoteBeaconAgentHomunculus implements IHomunculus {

  private Agent remoteAgent = null;

  public Agent getRemoteAgent() {
    return remoteAgent;
  }

  public void setRemoteAgent(Agent remoteAgent) {
    this.remoteAgent = remoteAgent;
  }

  @Override
  public void close() throws Exception {
    remoteAgent = null;
  }

}
