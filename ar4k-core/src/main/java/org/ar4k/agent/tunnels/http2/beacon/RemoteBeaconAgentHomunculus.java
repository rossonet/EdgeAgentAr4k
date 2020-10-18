package org.ar4k.agent.tunnels.http2.beacon;

import org.ar4k.agent.rpc.IHomunculusRpc;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Agent;

public class RemoteBeaconAgentHomunculus implements IHomunculusRpc {

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
