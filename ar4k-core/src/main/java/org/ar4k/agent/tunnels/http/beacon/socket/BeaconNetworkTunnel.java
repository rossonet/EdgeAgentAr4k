package org.ar4k.agent.tunnels.http.beacon.socket;

import java.util.UUID;

import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.network.NetworkConfig;
import org.ar4k.agent.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.network.NetworkTunnel;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.ResponseNetworkChannel;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelServiceV1Grpc.TunnelServiceV1Stub;

import io.netty.channel.ChannelHandlerContext;

public class BeaconNetworkTunnel implements NetworkTunnel {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconNetworkTunnel.class.toString());
	private static final int PACKET_CHUNK_LIMIT = 192;
	static final long LAST_MESSAGE_FROM_BEACON_SERVER_TIMEOUT = 3500;
	static final int BEACON_DELAY_RECONNECTION = 1500;
	static final int RETRY_LIMIT = 15;
	static final int WAIT_WHILE_DELAY = 1200;
	static final long SYNC_TIME_OUT = 20000;
	static final int DELAY_SOFT_CHECK = 1500;
	static final long PING_FROM_BEACON_SERVER_CHECK_FACTOR = 2;
	// static final int DELAY_RESEND_MULTI = 10;

	public static boolean trace = false;

	private final TunnelServiceV1Stub asyncStubTunnel;

	private long tunnelUniqueId;
	private long uniqueClassId = 0;
	private final NetworkConfig config;
	private final boolean isStartingFromMe;
	private final Agent meAgent;
	private Agent remoteAgent = null;
	private NetworkMode myRoleMode = null;
	private BeaconConnection beaconConnection = null;
	private BeaconNetworkReceiver networkReceiver = null;

	private boolean activeTunnel = true;

	public BeaconNetworkTunnel(Agent me, NetworkConfig config, boolean ownerRequest,
			TunnelServiceV1Stub asyncStubTunnel, String tunnelId) {
		this.meAgent = me;
		this.config = config;
		this.isStartingFromMe = ownerRequest;
		tunnelUniqueId = Long.valueOf(tunnelId);
		this.asyncStubTunnel = asyncStubTunnel;
		if ((config.getNetworkModeRequested().equals(NetworkMode.CLIENT) && ownerRequest)
				|| config.getNetworkModeRequested().equals(NetworkMode.SERVER) && !ownerRequest) {
			myRoleMode = NetworkMode.CLIENT;
		} else {
			myRoleMode = NetworkMode.SERVER;
		}
		if (trace)
			logger.info(me.getAgentUniqueName() + " created BeaconNetworkTunnel tunnel id " + tunnelUniqueId + "/"
					+ myRoleMode);
	}

	@Override
	public void init() throws ServiceInitException {
		uniqueClassId = UUID.randomUUID().getMostSignificantBits();
		networkReceiver = new BeaconNetworkReceiver(this, uniqueClassId, PACKET_CHUNK_LIMIT);
		if (myRoleMode.equals(NetworkMode.SERVER)) {
			try {
				networkReceiver.getOrCreateServerSocketChannel(uniqueClassId);
			} catch (final Exception e) {
				logger.logException(e);
			}
			if (trace)
				logger.info("tunnel server TCP id " + tunnelUniqueId + " created");
		}
		if (trace)
			logger.info(meAgent.getAgentUniqueName() + " started NetworkHub tunnel id " + tunnelUniqueId + "/"
					+ myRoleMode);
		beaconConnection = new BeaconConnection(this);
	}

	@Override
	public void setResponseNetworkChannel(ResponseNetworkChannel response) {
		this.tunnelUniqueId = response.getTargeId();
	}

	@Override
	public void setRemoteAgent(Agent remoteAgent) {
		this.remoteAgent = remoteAgent;
	}

	@Override
	public NetworkConfig getConfig() {
		return config;
	}

	@Override
	public BeaconNetworkReceiver getNetworkReceiver() {
		return networkReceiver;
	}

	void callChannelClientComplete(long clientSerialId) {
		if (trace) {
			logger.info("callChannelClientComplete " + clientSerialId);
		}
		// TODO Auto-generated method stub
	}

	void callChannelClientException(long clientSerialId) {
		if (trace) {
			logger.info("callChannelClientException " + clientSerialId);
		}
		// TODO Auto-generated method stub
	}

	void callChannelServerComplete(long serverSessionId) {
		if (trace) {
			logger.info("callChannelServerComplete " + serverSessionId);
		}
		// TODO Auto-generated method stub
	}

	void callChannelServerException(long serverSessionId) {
		if (trace) {
			logger.info("callChannelServerException " + serverSessionId);
		}
		// TODO Auto-generated method stub
	}

	long getUniqueClassId() {
		return uniqueClassId;
	}

	@Override
	public long getTunnelId() {
		return tunnelUniqueId;
	}

	@Override
	public Agent getRemoteAgent() {
		return remoteAgent;
	}

	@Override
	public long getPacketSend() {
		return networkReceiver.getPacketSend();
	}

	@Override
	public long getPacketReceived() {
		return networkReceiver.getPacketReceived();
	}

	@Override
	public long getPacketControl() {
		return networkReceiver.getPacketControl();
	}

	@Override
	public long getPacketError() {
		return networkReceiver.getPacketError();
	}

	Agent getMe() {
		return meAgent;
	}

	boolean isActiveTunnel() {
		return activeTunnel;
	}

	private boolean getOwner() {
		return isStartingFromMe;
	}

	TunnelServiceV1Stub getAsyncStubTunnel() {
		return asyncStubTunnel;
	}

	boolean imTheClient() {
		return (getConfig().getNetworkModeRequested().equals(NetworkMode.CLIENT) && getOwner())
				|| getConfig().getNetworkModeRequested().equals(NetworkMode.SERVER) && !getOwner();
	}

	@Override
	public void beaconObserverComplete(long targetId) {
		activeTunnel = false;
		kill();
		if (trace) {
			logger.info("BeaconObserver completed on " + meAgent.getAgentUniqueName() + " tunnelId: " + targetId);
		}
	}

	@Override
	public void close() throws Exception {
		kill();
		if (trace)
			logger.info("closed BeaconNetworkTunnel on " + meAgent.getAgentUniqueName() + " id: " + tunnelUniqueId
					+ " role:" + myRoleMode);
	}

	@Override
	public void kill() {
		activeTunnel = false;
		beaconConnection.close();
		if (networkReceiver != null) {
			try {
				networkReceiver.close();
			} catch (final Exception e) {
				logger.logException("EXCEPTION killing BeaconNetworkTunnel on " + meAgent.getAgentUniqueName() + " id: "
						+ tunnelUniqueId + " role: " + myRoleMode, e);
			}
		}
		networkReceiver = null;
		if (trace)
			logger.info("killed BeaconNetworkTunnel on " + meAgent.getAgentUniqueName() + " id: " + tunnelUniqueId
					+ " role: " + myRoleMode);
	}

	boolean isBeaconConnectionOk(BeaconNetworkTunnel beaconNetworkTunnel) {
		try {
			return (beaconConnection != null && beaconConnection.isBeaconToServerOnline());
		} catch (final Exception e) {
			logger.logException(e);
			return false;
		}
	}

	void sendMessageToBeaconTunnel(TunnelMessage tunnelMessage) {
		beaconConnection.toBeaconServerOnNext(tunnelMessage);
	}

	@Override
	public void selfCheckIfNeeded() {
		if (beaconConnection != null) {
			beaconConnection.selfCheckIfNeeded();
		}
		nextAction(null);
	}

	NetworkMode getMyRoleMode() {
		return myRoleMode;
	}

	void nextAction(ChannelHandlerContext channelHandlerContext) {
		if (networkReceiver != null) {
			networkReceiver.nextAction(channelHandlerContext);
		}

	}

}
