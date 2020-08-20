package org.ar4k.agent.tunnels.http.beacon.socket;

import java.util.Date;
import java.util.UUID;

import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.network.NetworkConfig;
import org.ar4k.agent.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.network.NetworkReceiver;
import org.ar4k.agent.network.NetworkTunnel;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.MessageStatus;
import org.ar4k.agent.tunnels.http.grpc.beacon.ResponseNetworkChannel;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelServiceV1Grpc.TunnelServiceV1Stub;
import org.joda.time.Instant;

import io.grpc.stub.StreamObserver;

/**
 * gestito da beacon client come endpoint di un tunnel
 **/
public class BeaconNetworkTunnel implements NetworkTunnel {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconNetworkTunnel.class.toString());
	private static final int PACKET_CHUNK_LIMIT = 192;
	public static final int RETRY_LIMIT = 5;
	public static final int WAIT_WHILE_DELAY = 250;
	public static final long SYNC_TIME_OUT = 20000;
	private static final boolean RESET_GRPC_ON_FAULT = true;
	public static boolean trace = true;

	private long uniqueClassId = 0;
	private final NetworkConfig config;
	private final boolean isStartingFromMe;
	private NetworkReceiver networkReceiver = null;
	private Agent remoteAgent = null;
	private final Agent me;
	private long tunnelUniqueId;
	private final TunnelServiceV1Stub asyncStubTunnel;
	private transient Instant lastCheck = Instant.now();
	private static final int rateCheck = 10000;

	private StreamObserver<TunnelMessage> toBeaconServer = null;
	private BeaconEndpointObserver fromBeaconServer = null;
	private NetworkMode myRoleMode = null;
	private boolean activeTunnel = true;

	public BeaconNetworkTunnel(Agent me, NetworkConfig config, boolean ownerRequest,
			TunnelServiceV1Stub asyncStubTunnel, String tunnelId) {
		this.me = me;
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
	public NetworkConfig getConfig() {
		return config;
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
			logger.info(me.getAgentUniqueName() + " started NetworkHub tunnel id " + tunnelUniqueId + "/" + myRoleMode);
		createNewBeaconConnection();
	}

	private synchronized void createNewBeaconConnection() {
		final TunnelMessage tunnelMessage = TunnelMessage.newBuilder().setAgentSource(me).setTargeId(tunnelUniqueId)
				.setUuid(uniqueClassId).setMessageStatus(MessageStatus.beaconChannelRequest).build();
		fromBeaconServer = new BeaconEndpointObserver(this, tunnelUniqueId, networkReceiver, myRoleMode, uniqueClassId);
		toBeaconServer = asyncStubTunnel.openNetworkChannel(fromBeaconServer);
		toBeaconServer.onNext(tunnelMessage);
		if (trace)
			logger.info(
					me.getAgentUniqueName() + " createBeaconConnection tunnel id " + tunnelUniqueId + "/" + myRoleMode);
	}

	@Override
	public NetworkReceiver getNetworkReceiver() {
		return networkReceiver;
	}

	public void setResponseNetworkChannel(ResponseNetworkChannel response) {
		this.tunnelUniqueId = response.getTargeId();
	}

	@Override
	public void close() throws Exception {
		kill();
		if (trace)
			logger.info(
					me.getAgentUniqueName() + " closed BeaconNetworkTunnel id " + tunnelUniqueId + "/" + myRoleMode);
	}

	public long getTunnelId() {
		return tunnelUniqueId;
	}

	public Agent getRemoteAgent() {
		return remoteAgent;
	}

	public void setRemoteAgent(Agent remoteAgent) {
		this.remoteAgent = remoteAgent;
	}

	public StreamObserver<TunnelMessage> getToBeaconServer() {
		verify();
		return toBeaconServer;
	}

	public void setToBeaconServer(BeaconEndpointObserver toBeaconServer) {
		this.toBeaconServer = toBeaconServer;
	}

	public BeaconEndpointObserver getFromBeaconServer() {
		verify();
		return fromBeaconServer;
	}

	@Override
	public long getPacketSend() {
		return networkReceiver.getPacketSend();
	}

	@Override
	public long getPacketReceived() {
		return networkReceiver.getPacketReceived();
	}

	public Agent getMe() {
		return me;
	}

	public boolean getOwner() {
		return isStartingFromMe;
	}

	public void selfCheck() {
		if (lastCheck.plus(rateCheck).isBeforeNow()) {
			lastCheck = Instant.now();
			verify();
		}
	}

	private synchronized void verify() {
		if (activeTunnel) {
			if (isConnectedToBeacon()) {
				actionOnObserverFaultActive(tunnelUniqueId);
			}
		}
	}

	private boolean isConnectedToBeacon() {
		return toBeaconServer == null || fromBeaconServer == null || !fromBeaconServer.isOnline();
	}

	@Override
	public void beaconObserverComplete(long targetId) {
		if (trace) {
			logger.info("#### beaconObserverComplete " + targetId);
			logger.info(me.getAgentUniqueName() + " BeaconNetworkTunnel " + targetId + " completed");
		}
		activeTunnel = false;
		kill();
	}

	@Override
	public void beaconObserverFault(long targetId) {
		if (activeTunnel) {
			actionOnObserverFaultClean(targetId);
			final long startCheck = new Date().getTime();
			while (!isConnectedToBeacon() && (startCheck + BeaconNetworkTunnel.SYNC_TIME_OUT) > new Date().getTime()) {
				try {
					Thread.sleep(BeaconNetworkTunnel.WAIT_WHILE_DELAY);
					verify();
				} catch (final InterruptedException e) {
					logger.logException(e);
				}
				if (trace)
					logger.info("in cicle restore beacon connection " + fromBeaconServer);
			}
			if (trace)
				logger.info("connection tunnel ok " + isConnectedToBeacon());
		}
	}

	private void actionOnObserverFaultClean(long targetId) {
		if (RESET_GRPC_ON_FAULT) {
			try {
				toBeaconServer = null;
				fromBeaconServer = null;
			} catch (final Exception e) {
				logger.logException(me.getAgentUniqueName(), e);
			}
		}
	}

	private void actionOnObserverFaultActive(long tunnelId) {
		if (RESET_GRPC_ON_FAULT) {
			try {
				if (activeTunnel) {
					final TunnelMessage tunnelMessage = TunnelMessage.newBuilder().setAgentSource(me)
							.setTargeId(tunnelId).setUuid(uniqueClassId)
							.setMessageStatus(myRoleMode.equals(NetworkMode.CLIENT) ? MessageStatus.closeRequestClient
									: MessageStatus.closeRequestServer)
							.build();
					fromBeaconServer = new BeaconEndpointObserver(this, tunnelId, networkReceiver, myRoleMode,
							uniqueClassId);
					toBeaconServer = asyncStubTunnel.openNetworkChannel(fromBeaconServer);
					toBeaconServer.onNext(tunnelMessage);
					logger.info(me.getAgentUniqueName() + " BeaconNetworkTunnel tunnel id " + tunnelId
							+ " fault procedure completed\nfromBeaconServer: " + fromBeaconServer + "\ntoBeaconServer: "
							+ toBeaconServer);
				}
			} catch (final Exception e) {
				logger.logException(me.getAgentUniqueName(), e);
			}
			if (trace)
				logger.info(me.getAgentUniqueName() + " callFault BeaconNetworkTunnel tunnel id " + tunnelId + "/"
						+ myRoleMode);
		}
	}

	public void callChannelClientComplete(long clientSerialId) {
		if (trace) {
			logger.info("callChannelClientComplete " + clientSerialId);
		}
		// TODO Auto-generated method stub
	}

	public void callChannelClientException(long clientSerialId) {
		if (trace) {
			logger.info("callChannelClientException " + clientSerialId);
		}
		// TODO Auto-generated method stub
	}

	public void callChannelServerComplete(long serverSessionId) {
		if (trace) {
			logger.info("callChannelServerComplete " + serverSessionId);
		}
		// TODO Auto-generated method stub
	}

	public void callChannelServerException(long serverSessionId) {
		if (trace) {
			logger.info("callChannelServerException " + serverSessionId);
		}
		// TODO Auto-generated method stub
	}

	@Override
	public void kill() {
		activeTunnel = false;
		if (toBeaconServer != null) {
			try {
				toBeaconServer.onCompleted();
			} catch (final Exception a) {
				logger.info(a.getMessage());
			}
		}
		toBeaconServer = null;
		try {
			networkReceiver = null;
			if (trace)
				logger.info(me.getAgentUniqueName() + " BeaconNetworkTunnel id " + tunnelUniqueId + "/" + myRoleMode
						+ " closed");
		} catch (final Exception e) {
			logger.logException(me.getAgentUniqueName(), e);
		}
	}

}
