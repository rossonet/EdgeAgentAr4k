package org.ar4k.agent.tunnels.http.beacon.socket;

import static org.ar4k.agent.tunnels.http.beacon.socket.BeaconNetworkTunnel.TRACE_LOG_IN_INFO;

import java.util.Date;

import org.ar4k.agent.config.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http.grpc.beacon.MessageStatus;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage;
import org.joda.time.Instant;

import io.grpc.stub.StreamObserver;

public class BeaconConnection implements AutoCloseable {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconConnection.class.toString());

	private enum StatusNow {
		INIT, ONLINE, RECONNECTION, CLOSED
	}

	private final BeaconNetworkTunnel beaconNetworkTunnel;

	private StreamObserver<TunnelMessage> toBeaconServer = null;
	private BeaconEndpointFromObserver fromBeaconServer = null;
	private Instant lastCheck = Instant.now();

	private StatusNow statusNow = StatusNow.INIT;

	BeaconConnection(BeaconNetworkTunnel beaconNetworkTunnel) {
		this.beaconNetworkTunnel = beaconNetworkTunnel;
		statusNow = StatusNow.INIT;
		createNewBeaconConnection();
	}

	private synchronized void createNewBeaconConnection() {
		final StatusNow originalState = statusNow;
		MessageStatus messageType = null;
		if (statusNow.equals(StatusNow.INIT)) {
			messageType = MessageStatus.beaconChannelRequest;
		} else {
			messageType = (beaconNetworkTunnel.getMyRoleMode().equals(NetworkMode.CLIENT)
					? MessageStatus.closeRequestClient
					: MessageStatus.closeRequestServer);
		}
		final TunnelMessage tunnelMessage = TunnelMessage.newBuilder().setAgentSource(beaconNetworkTunnel.getMe())
				.setTunnelId(beaconNetworkTunnel.getTunnelId()).setClassUuid(beaconNetworkTunnel.getUniqueClassId())
				.setMessageStatus(messageType).build();
		fromBeaconServer = new BeaconEndpointFromObserver(this);
		toBeaconServer = beaconNetworkTunnel.getAsyncStubTunnel().openNetworkChannel(fromBeaconServer);
		statusNow = StatusNow.ONLINE;
		if (TRACE_LOG_IN_INFO)
			logger.info("Beacon connection tunnel id {} role {} is now online", beaconNetworkTunnel.getTunnelId(),
					beaconNetworkTunnel);
		toBeaconServerOnNext(tunnelMessage);
		if (originalState.equals(StatusNow.RECONNECTION)) {
			beaconNetworkTunnel.nextActionAllSessions();
		}
	}

	synchronized void toBeaconServerOnNext(TunnelMessage message) {
		checkObserverTimeoutAndActive();
		if (isBeaconToServerOnline()) {
			toBeaconServer.onNext(message);
		} else {
			checkBeaconConnectedOrRetry();
		}
	}

	void selfCheckIfNeeded() {
		checkObserverTimeoutAndActive();
		if (lastCheck.plus(BeaconNetworkTunnel.BEACON_DELAY_RECONNECTION).isBeforeNow()) {
			lastCheck = Instant.now();
			checkBeaconConnectedOrRetry();
		}
	}

	private void checkBeaconConnectedOrRetry() {
		checkObserverTimeoutAndActive();
		if (beaconNetworkTunnel.isActiveTunnel() && !isBeaconToServerOnline()) {
			receivedOnErrorInFromObserver();
		}
	}

	boolean isBeaconToServerOnline() {
		if (!statusNow.equals(StatusNow.ONLINE)) {
			logger.info("beacon connection status is {}", statusNow);
			return false;
		} else {
			return true;
		}

	}

	private void checkObserverTimeoutAndActive() {
		final long time = new Date().getTime();
		if (statusNow.equals(StatusNow.ONLINE) && fromBeaconServer != null && fromBeaconServer.isOnline()
				&& ((fromBeaconServer.getLastMessageReceivedFromServerBeacon()
						+ (BeaconNetworkTunnel.LAST_MESSAGE_FROM_BEACON_SERVER_TIMEOUT
								* BeaconNetworkTunnel.PING_FROM_BEACON_SERVER_CHECK_FACTOR)) < time)) {
			logger.warn("ping from server failed after {}",
					(time - fromBeaconServer.getLastMessageReceivedFromServerBeacon()));
			receivedOnErrorInFromObserver();
		}
	}

	@Override
	public void close() {
		statusNow = StatusNow.CLOSED;
		if (TRACE_LOG_IN_INFO)
			logger.info("Beacon connection tunnel id {} role {} is now closed", beaconNetworkTunnel.getTunnelId(),
					beaconNetworkTunnel);
	}

	BeaconNetworkTunnel getBeaconNetworkTunnel() {
		return beaconNetworkTunnel;
	}

	synchronized void receivedOnErrorInFromObserver() {
		beaconNetworkTunnel.incrementPacketError();
		if (statusNow.equals(StatusNow.ONLINE)) {
			statusNow = StatusNow.RECONNECTION;
			if (TRACE_LOG_IN_INFO)
				logger.info("Beacon connection tunnel id {} role {} is now in recconnection state",
						beaconNetworkTunnel.getTunnelId(), beaconNetworkTunnel);
			createNewBeaconConnection();
		} else {
			if (TRACE_LOG_IN_INFO)
				logger.info("Beacon connection tunnel id {} role {} received onError but it is in state {}",
						beaconNetworkTunnel.getTunnelId(), beaconNetworkTunnel, statusNow);
		}
	}
}
