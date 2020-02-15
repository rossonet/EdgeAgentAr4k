package org.ar4k.agent.core;

import java.util.List;

import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.network.NetworkConfig;
import org.ar4k.agent.network.NetworkTunnel;
import org.ar4k.agent.tunnels.http.beacon.BeaconNetworkTunnel;
import org.ar4k.agent.tunnels.http.beacon.RemoteBeaconRpcExecutor;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.StatusValue;
import org.ar4k.agent.tunnels.http.grpc.beacon.RpcServiceV1Grpc.RpcServiceV1BlockingStub;
import org.ar4k.agent.tunnels.http.grpc.beacon.RpcServiceV1Grpc.RpcServiceV1Stub;

import io.grpc.ConnectivityState;

public interface IBeaconClient {

  RemoteBeaconRpcExecutor getRemoteExecutor(Agent agent);

  List<Agent> listAgentsConnectedToBeacon();

  void shutdown() throws InterruptedException;

  ConnectivityState getStateConnection();

  int getPollingFreq();

  void sendLoggerLine(String message, String level);

  void sendException(Exception message);

  String getAgentUniqueName();

  RpcServiceV1Stub getAsyncStub();

  RpcServiceV1BlockingStub getBlockingStub();

  ListCommandsReply listCommadsOnAgent(String agentId);

  ConfigReply sendConfigToAgent(String agentId, Ar4kConfig newConfig);

  List<BeaconNetworkTunnel> getTunnels();

  void removeTunnel(BeaconNetworkTunnel toRemove);

  NetworkTunnel getNetworkTunnel(String agentId, NetworkConfig config);

  ElaborateMessageReply runCommadsOnAgent(String agentId, String command);

  CompleteCommandReply runCompletitionOnAgent(String agentUniqueName, String command);

  List<RemoteBeaconRpcExecutor> getRemoteExecutors();

  void setRemoteExecutors(List<RemoteBeaconRpcExecutor> remoteExecutors);

  int getDiscoveryPort();

  void setDiscoveryPort(int discoveryPort);

  String getDiscoveryFilter();

  void setDiscoveryFilter(String discoveryFilter);

  String getReservedUniqueName();

  void setReservedUniqueName(String reservedUniqueName);

  StatusValue getRegistrationStatus();

}