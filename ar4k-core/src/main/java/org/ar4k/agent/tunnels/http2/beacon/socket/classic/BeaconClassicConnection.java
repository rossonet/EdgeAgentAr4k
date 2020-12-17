package org.ar4k.agent.tunnels.http2.beacon.socket.classic;

import java.util.Date;

import org.ar4k.agent.config.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http2.grpc.beacon.MessageStatus;
import org.ar4k.agent.tunnels.http2.grpc.beacon.TunnelMessage;
import org.joda.time.Instant;

import io.grpc.stub.StreamObserver;

public class BeaconClassicConnection implements AutoCloseable {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconClassicConnection.class.toString());

	private enum StatusNow {
		INIT, ONLINE, RECONNECTION, CLOSED
	}

	private final BeaconNetworkClassicTunnel beaconNetworkClassicTunnel;

	private StreamObserver<TunnelMessage> toBeaconServer = null;
	private BeaconClassicEndpointFromObserver fromBeaconServer = null;
	private Instant lastCheck = Instant.now();

	private StatusNow statusNow = StatusNow.INIT;

	BeaconClassicConnection(BeaconNetworkClassicTunnel beaconNetworkClassicTunnel) {
		this.beaconNetworkClassicTunnel = beaconNetworkClassicTunnel;
		statusNow = StatusNow.INIT;
		createNewBeaconConnection();
	}

	private synchronized void createNewBeaconConnection() {
		final StatusNow originalState = statusNow;
		MessageStatus messageType = null;
		if (statusNow.equals(StatusNow.INIT)) {
			messageType = MessageStatus.beaconChannelRequest;
		} else {
			messageType = (beaconNetworkClassicTunnel.getMyRoleMode().equals(NetworkMode.CLIENT)
					? MessageStatus.closeRequestClient
					: MessageStatus.closeRequestServer);
		}
		final TunnelMessage tunnelMessage = TunnelMessage.newBuilder()
				.setAgentSource(beaconNetworkClassicTunnel.getMe())
				.setTunnelId(beaconNetworkClassicTunnel.getTunnelId())
				.setClassUuid(beaconNetworkClassicTunnel.getUniqueClassId()).setMessageStatus(messageType).build();
		fromBeaconServer = new BeaconClassicEndpointFromObserver(this);
		toBeaconServer = beaconNetworkClassicTunnel// .getAsyncStubTunnel()
				.openNetworkChannel(fromBeaconServer);
		statusNow = StatusNow.ONLINE;
		if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO)
			logger.info("Beacon connection classic tunnel id {} role {} is now online",
					beaconNetworkClassicTunnel.getTunnelId(), beaconNetworkClassicTunnel);
		toBeaconServerOnNext(tunnelMessage);
		if (originalState.equals(StatusNow.RECONNECTION)) {
			beaconNetworkClassicTunnel.nextAction();
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
		if (lastCheck.plus(BeaconNetworkClassicTunnel.BEACON_DELAY_RECONNECTION).isBeforeNow()) {
			lastCheck = Instant.now();
			checkBeaconConnectedOrRetry();
		}
	}

	private void checkBeaconConnectedOrRetry() {
		checkObserverTimeoutAndActive();
		if (beaconNetworkClassicTunnel.isActiveTunnel() && !isBeaconToServerOnline()) {
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
						+ (BeaconNetworkClassicTunnel.LAST_MESSAGE_FROM_BEACON_SERVER_TIMEOUT
								* BeaconNetworkClassicTunnel.PING_FROM_BEACON_SERVER_CHECK_FACTOR)) < time)) {
			logger.warn("ping from server failed after {}",
					(time - fromBeaconServer.getLastMessageReceivedFromServerBeacon()));
			receivedOnErrorInFromObserver();
		}
	}

	@Override
	public void close() {
		statusNow = StatusNow.CLOSED;
		if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO)
			logger.info("Beacon connection tunnel id {} role {} is now closed",
					beaconNetworkClassicTunnel.getTunnelId(), beaconNetworkClassicTunnel);
	}

	BeaconNetworkClassicTunnel getBeaconNetworkTunnel() {
		return beaconNetworkClassicTunnel;
	}

	synchronized void receivedOnErrorInFromObserver() {
		beaconNetworkClassicTunnel.incrementPacketError();
		if (statusNow.equals(StatusNow.ONLINE)) {
			statusNow = StatusNow.RECONNECTION;
			if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO)
				logger.info("Beacon connection tunnel id {} role {} is now in recconnection state",
						beaconNetworkClassicTunnel.getTunnelId(), beaconNetworkClassicTunnel);
			createNewBeaconConnection();
		} else {
			if (BeaconNetworkClassicTunnel.TRACE_LOG_IN_INFO)
				logger.info("Beacon connection tunnel id {} role {} received onError but it is in state {}",
						beaconNetworkClassicTunnel.getTunnelId(), beaconNetworkClassicTunnel, statusNow);
		}
	}
}
