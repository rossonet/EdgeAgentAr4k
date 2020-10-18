package org.ar4k.agent.tunnels.http2.beacon.socket.classic;

import java.net.Socket;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.ar4k.agent.config.network.NetworkConfig;
import org.ar4k.agent.config.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.config.network.NetworkReceiver;
import org.ar4k.agent.config.network.NetworkTunnel;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel;
import org.ar4k.agent.tunnels.http2.grpc.beacon.TunnelMessage;
import org.ar4k.agent.tunnels.http2.grpc.beacon.TunnelServiceV1Grpc.TunnelServiceV1Stub;

import io.grpc.stub.StreamObserver;

public class BeaconNetworkClassicTunnel implements NetworkTunnel {

	public static final long BEACON_DELAY_RECONNECTION = 0;
	public static final int LAST_MESSAGE_FROM_BEACON_SERVER_TIMEOUT = 0;
	public static final int PING_FROM_BEACON_SERVER_CHECK_FACTOR = 0;
	public static final long DELAY_SOFT_CHECK = 0;

	public static final boolean TRACE_LOG_IN_INFO = true;

	private final AtomicReference<Socket> socket = new AtomicReference<>();

	public BeaconNetworkClassicTunnel(Agent me, NetworkConfig config, boolean b, TunnelServiceV1Stub asyncStubTunnel,
			String uniqueIdRequest) {
		// TODO Auto-generated constructor stub
	}

	public NetworkMode getMyRoleMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getTunnelId() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Agent getMe() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getUniqueClassId() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void nextActionAllSessions() {
		// TODO Auto-generated method stub

	}

	public boolean isActiveTunnel() {
		// TODO Auto-generated method stub
		return false;
	}

	public void incrementPacketError() {
		// TODO Auto-generated method stub

	}

	public StreamObserver<TunnelMessage> openNetworkChannel(BeaconClassicEndpointFromObserver fromBeaconServer) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<Long, CachedChunkClassic> getOutputCachedDataBase64ByMessageId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String reportDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getLastAckSent(long sessionId) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Map<Long, CachedChunkClassic> getOutputCachedMessages(long sessionId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void sendExceptionMessage(long sessionId, long tunnelId, long messageId, Exception clientEx) {
		// TODO Auto-generated method stub

	}

	public void confirmPacketReceived(long sessionId, long messageAckId, long messageAckReceivedId) {
		// TODO Auto-generated method stub

	}

	public void exceptionPacketReceived(long sessionId, long messageAckId) {
		// TODO Auto-generated method stub

	}

	public boolean isActive(long sessionId) {
		// TODO Auto-generated method stub
		return false;
	}

	public Socket getOrCreateServerSocketChannel(long sessionId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Socket getOrCreateClientHandler(long sessionId) {
		return null;
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public NetworkConfig getConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub

	}

	@Override
	public NetworkReceiver getNetworkReceiver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getPacketSend() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getPacketReceived() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getPacketControl() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getPacketError() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWaitingPackagesCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void beaconObserverComplete(long targetId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRemoteAgent(Agent remoteAgent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setResponseNetworkChannel(ResponseNetworkChannel response) {
		// TODO Auto-generated method stub

	}

	@Override
	public Agent getRemoteAgent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void selfCheckIfNeeded() {
		// TODO Auto-generated method stub

	}

}
