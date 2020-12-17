package org.ar4k.agent.core.interfaces;

import java.util.List;

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.config.network.NetworkConfig;
import org.ar4k.agent.config.network.NetworkTunnel;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.rpc.process.xpra.XpraSessionProcess;
import org.ar4k.agent.tunnels.http2.beacon.RemoteBeaconRpcExecutor;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http2.grpc.beacon.CompleteCommandReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ElaborateMessageReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ListStringReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.StatusValue;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RpcServiceV1Grpc.RpcServiceV1BlockingStub;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RpcServiceV1Grpc.RpcServiceV1Stub;

import io.grpc.ConnectivityState;

public interface IBeaconClient {

	RemoteBeaconRpcExecutor getRemoteExecutor(Agent agent);

	XpraSessionProcess startXpraService(String executorLabel, int port, String cmd);

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

	ConfigReply sendConfigToAgent(String agentId, EdgeConfig newConfig);

	ConfigReply getConfigFromAgent(String agentId);

	List<NetworkTunnel> getTunnels();

	void removeTunnel(NetworkTunnel toRemove);

	NetworkTunnel getNewNetworkTunnel(String agentId, NetworkConfig config);

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

	RpcConversation getLocalExecutor();

	void closeNetworkTunnel(long targetId);

	ListStringReply getRuntimeProvides(String agentId);

	ListStringReply getRuntimeRequired(String agentId);

}