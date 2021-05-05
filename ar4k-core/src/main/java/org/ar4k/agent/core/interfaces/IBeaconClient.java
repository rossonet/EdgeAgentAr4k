package org.ar4k.agent.core.interfaces;

import java.util.List;

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.config.network.NetworkConfig;
import org.ar4k.agent.config.network.NetworkTunnel;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.core.data.DataServiceOwner;
import org.ar4k.agent.tunnels.http2.beacon.client.RemoteBeaconRpcExecutor;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http2.grpc.beacon.AgentRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RpcServiceV1Grpc.RpcServiceV1BlockingStub;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RpcServiceV1Grpc.RpcServiceV1Stub;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Status;
import org.ar4k.agent.tunnels.http2.grpc.beacon.StatusValue;

import io.grpc.ConnectivityState;

public interface IBeaconClient extends DataServiceOwner {

	void closeNetworkTunnel(long targetId);

	String getAgentUniqueName();

	String getAliasBeaconClientInKeystore();

	RpcServiceV1Stub getAsyncStub();

	RpcServiceV1BlockingStub getBlockingStub();

	String getCertChainAuthority();

	String getCertChainFile();

	String getCertFile();

	ConfigReply getConfigFromAgent(String agentId);

	String getCsrRequest();

	String getDiscoveryFilter();

	int getDiscoveryPort();

	String getHostTarget();

	RpcConversation getLocalExecutor();

	NetworkTunnel getNewNetworkTunnel(String agentId, NetworkConfig config);

	int getPollingFrequency();

	int getPort();

	String getPrivateFile();

	StatusValue getRegistrationStatus();

	RemoteBeaconRpcExecutor getRemoteExecutor(Agent agent);

	List<RemoteBeaconRpcExecutor> getRemoteExecutors();

	String getReservedUniqueName();

	ListStringReply getRuntimeProvides(String agentId);

	ListStringReply getRuntimeRequired(String agentId);

	ConnectivityState getStateConnection();

	List<NetworkTunnel> getTunnels();

	List<Agent> listAgentsConnectedToBeacon();

	ListCommandsReply listCommadsOnAgent(String agentId);

	List<AgentRequest> listProvisioningRequests();

	void removeTunnel(NetworkTunnel toRemove);

	ElaborateMessageReply runCommadsOnAgent(String agentId, String command);

	CompleteCommandReply runCompletitionOnAgent(String agentUniqueName, String command);

	ConfigReply sendConfigToAgent(String agentId, EdgeConfig newConfig);

	void sendException(Exception message);

	void sendLoggerLine(String message, String level);

	void setDiscoveryFilter(String discoveryFilter);

	void setDiscoveryPort(int discoveryPort);

	void setRemoteExecutors(List<RemoteBeaconRpcExecutor> remoteExecutors);

	void setReservedUniqueName(String reservedUniqueName);

	void shutdown() throws InterruptedException;

	Status approveRemoteAgent(String requestId, String cert, String note);

}