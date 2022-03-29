package org.ar4k.agent.tunnels.http2.beacon.socket.classic;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.ar4k.agent.config.network.NetworkConfig;
import org.ar4k.agent.config.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.config.network.NetworkReceiver;
import org.ar4k.agent.config.network.NetworkTunnel;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel;
import org.ar4k.agent.tunnels.http2.grpc.beacon.TunnelMessage;
import org.ar4k.agent.tunnels.http2.grpc.beacon.TunnelServiceV1Grpc.TunnelServiceV1Stub;

import io.grpc.stub.StreamObserver;

public class BeaconNetworkClassicTunnel implements NetworkTunnel {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(BeaconNetworkClassicTunnel.class);

	public static final long BEACON_DELAY_RECONNECTION = 0;
	public static final int LAST_MESSAGE_FROM_BEACON_SERVER_TIMEOUT = 0;
	public static final int PING_FROM_BEACON_SERVER_CHECK_FACTOR = 0;
	public static final long DELAY_SOFT_CHECK = 0;

	public static final boolean TRACE_LOG_IN_INFO = true;

	private final AtomicReference<Socket> socketClient = new AtomicReference<>();

	private final AtomicReference<ServerSocket> socketServer = new AtomicReference<>();
	private final NetworkMode myRoleMode;
	private final long tunnelId;
	private final Agent me;
	private final long uniqueClassId;
	private int packetError = 0;
	private final NetworkConfig networkConfig;
	private long packetReceived = 0;
	private long packetControl = 0;
	private Agent remoteAgent = null;
	private final boolean isStartingFromMe;
	private final TunnelServiceV1Stub asyncStubTunnel;

	public BeaconNetworkClassicTunnel(Agent me, NetworkConfig config, boolean ownerRequest,
			TunnelServiceV1Stub asyncStubTunnel, String tunnelId) {
		this.uniqueClassId = UUID.randomUUID().getMostSignificantBits();
		this.networkConfig = config;
		this.me = me;
		this.isStartingFromMe = ownerRequest;
		this.tunnelId = Long.valueOf(tunnelId);
		this.asyncStubTunnel = asyncStubTunnel;
		if ((config.getNetworkModeRequested().equals(NetworkMode.CLIENT) && ownerRequest)
				|| config.getNetworkModeRequested().equals(NetworkMode.SERVER) && !ownerRequest) {
			myRoleMode = NetworkMode.CLIENT;
		} else {
			myRoleMode = NetworkMode.SERVER;
		}
		if (TRACE_LOG_IN_INFO)
			logger.info(me.getAgentUniqueName() + " created BeaconNetworkTunnel tunnel id {} role {}", tunnelId,
					myRoleMode);
		// TO______DO Completare e rivedere classe BeaconNetworkClassicTunnel
	}

	public void nextAction() {
		// Auto-generated method stub

	}

	public boolean isActiveTunnel() {
		// Auto-generated method stub
		return false;
	}

	@Override
	public int getWaitingPackagesCount() {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public void init() {
		// Auto-generated method stub

	}

	@Override
	public void beaconObserverComplete(long targetId) {
		// Auto-generated method stub

	}

	public StreamObserver<TunnelMessage> openNetworkChannel(BeaconClassicEndpointFromObserver fromBeaconServer) {
		// Auto-generated method stub
		return null;
	}

	public Map<Long, CachedChunkClassic> getOutputCachedDataBase64ByMessageId() {
		// Auto-generated method stub
		return null;
	}

	public String reportDetails() {
		// Auto-generated method stub
		return null;
	}

	public long getLastAckSent(long sessionId) {
		// Auto-generated method stub
		return 0;
	}

	public Map<Long, CachedChunkClassic> getOutputCachedMessages(long sessionId) {
		// Auto-generated method stub
		return null;
	}

	public void sendExceptionMessage(long sessionId, long tunnelId, long messageId, Exception clientEx) {
		// Auto-generated method stub

	}

	public void confirmPacketReceived(long sessionId, long messageAckId, long messageAckReceivedId) {
		// Auto-generated method stub

	}

	public void exceptionPacketReceived(long sessionId, long messageAckId) {
		// Auto-generated method stub

	}

	public boolean isActive(long sessionId) {
		// Auto-generated method stub
		return false;
	}

	public ServerSocket getOrCreateServerSocketChannel(long sessionId) {
		// Auto-generated method stub
		return null;
	}

	public Socket getOrCreateClientHandler(long sessionId) {
		return null;
		// Auto-generated method stub

	}

	@Override
	public void close() throws Exception {
		// Auto-generated method stub

	}

	@Override
	public void kill() {
		// Auto-generated method stub

	}

	@Override
	public NetworkReceiver getNetworkReceiver() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public long getPacketSend() {
		// Auto-generated method stub
		return 0;
	}

	@Override
	public void selfCheckIfNeeded() {
		// Auto-generated method stub
	}

	@Override
	public void setResponseNetworkChannel(ResponseNetworkChannel response) {
		// Auto-generated method stub
	}

	public void incrementPacketError() {
		packetError++;
	}

	public NetworkMode getMyRoleMode() {
		return myRoleMode;
	}

	@Override
	public long getTunnelId() {
		return tunnelId;
	}

	public Agent getMe() {
		return me;
	}

	@Override
	public long getUniqueClassId() {
		return uniqueClassId;
	}

	@Override
	public long getPacketReceived() {
		return packetReceived;
	}

	@Override
	public long getPacketControl() {
		return packetControl;
	}

	@Override
	public long getPacketError() {
		return packetError;
	}

	@Override
	public NetworkConfig getConfig() {
		return networkConfig;
	}

	@Override
	public void setRemoteAgent(Agent remoteAgent) {
		this.remoteAgent = remoteAgent;
	}

	@Override
	public Agent getRemoteAgent() {
		return remoteAgent;
	}

}
