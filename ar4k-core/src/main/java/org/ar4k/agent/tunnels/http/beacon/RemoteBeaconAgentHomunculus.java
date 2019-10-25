package org.ar4k.agent.tunnels.http.beacon;

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

  @Override
  public void close() throws Exception {
    // TODO Auto-generated method stub
    
  }

}
