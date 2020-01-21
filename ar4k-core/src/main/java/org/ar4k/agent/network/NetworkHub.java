package org.ar4k.agent.network;

import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelServiceV1Grpc.TunnelServiceV1Stub;

public interface NetworkHub extends AutoCloseable {
  public static enum NetworkStatus {
    INIT, ACTIVE, INACTIVE, FAULT
  }

  long getTunnelId();

  NetworkStatus getStatus();

  void setAsyncStubTunnel(TunnelServiceV1Stub asyncStubTunnel);

  void setConfig(NetworkConfig config);

  void runNetty(boolean owner);

  void setTunnelId(long targetId);

  void setAgent(Agent me);

  void setUniqueNetworkLink(String uniqueBeaconNetwork);

  long getPacketSend();

  long getPacketReceived();
}
