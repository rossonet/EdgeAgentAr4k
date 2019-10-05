package org.ar4k.agent.tunnels.http.grpc;

import org.ar4k.agent.rpc.Homunculus;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;

public class RemoteBeaconAgentHomunculus implements Homunculus {

  private Agent remoteAgent = null;

  public Agent getRemoteAgent() {
    return remoteAgent;
  }

  public void setRemoteAgent(Agent remoteAgent) {
    this.remoteAgent = remoteAgent;
  }

}