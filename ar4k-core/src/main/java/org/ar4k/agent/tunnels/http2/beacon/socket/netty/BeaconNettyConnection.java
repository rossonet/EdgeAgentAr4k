package org.ar4k.agent.tunnels.http2.beacon.socket.netty;

import static org.ar4k.agent.tunnels.http2.beacon.socket.netty.BeaconNettyNetworkTunnel.TRACE_LOG_IN_INFO;

import java.util.Date;

import org.ar4k.agent.config.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http2.grpc.beacon.MessageStatus;
import org.ar4k.agent.tunnels.http2.grpc.beacon.TunnelMessage;
import org.joda.time.Instant;

import io.grpc.stub.StreamObserver;

public class BeaconNettyConnection implements AutoCloseable {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(BeaconNettyConnection.class);

	private enum StatusNow {
		INIT, ONLINE, RECONNECTION, CLOSED
	}

	private final BeaconNettyNetworkTunnel beaconNettyNetworkTunnel;

	private StreamObserver<TunnelMessage> toBeaconServer = null;
	private BeaconNettyEndpointFromObserver fromBeaconServer = null;
	private Instant lastCheck = Instant.now();

	private StatusNow statusNow = StatusNow.INIT;

	BeaconNettyConnection(BeaconNettyNetworkTunnel beaconNettyNetworkTunnel) {
		this.beaconNettyNetworkTunnel = beaconNettyNetworkTunnel;
		statusNow = StatusNow.INIT;
		createNewBeaconConnection();
	}

	private synchronized void createNewBeaconConnection() {
		final StatusNow originalState = statusNow;
		MessageStatus messageType = null;
		if (statusNow.equals(StatusNow.INIT)) {
			messageType = MessageStatus.beaconChannelRequest;
		} else {
			messageType = (beaconNettyNetworkTunnel.getMyRoleMode().equals(NetworkMode.CLIENT)
					? MessageStatus.closeRequestClient
					: MessageStatus.closeRequestServer);
		}
		final TunnelMessage tunnelMessage = TunnelMessage.newBuilder().setAgentSource(beaconNettyNetworkTunnel.getMe())
				.setTunnelId(beaconNettyNetworkTunnel.getTunnelId())
				.setClassUuid(beaconNettyNetworkTunnel.getUniqueClassId()).setMessageStatus(messageType).build();
		fromBeaconServer = new BeaconNettyEndpointFromObserver(this);
		toBeaconServer = beaconNettyNetworkTunnel.getAsyncStubTunnel().openNetworkChannel(fromBeaconServer);
		statusNow = StatusNow.ONLINE;
		if (TRACE_LOG_IN_INFO)
			logger.info("Beacon connection tunnel id {} role {} is now online", beaconNettyNetworkTunnel.getTunnelId(),
					beaconNettyNetworkTunnel);
		toBeaconServerOnNext(tunnelMessage);
		if (originalState.equals(StatusNow.RECONNECTION)) {
			beaconNettyNetworkTunnel.nextActionAllSessions();
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
		if (lastCheck.plus(BeaconNettyNetworkTunnel.BEACON_DELAY_RECONNECTION).isBeforeNow()) {
			lastCheck = Instant.now();
			checkBeaconConnectedOrRetry();
		}
	}

	private void checkBeaconConnectedOrRetry() {
		checkObserverTimeoutAndActive();
		if (beaconNettyNetworkTunnel.isActiveTunnel() && !isBeaconToServerOnline()) {
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
						+ (BeaconNettyNetworkTunnel.LAST_MESSAGE_FROM_BEACON_SERVER_TIMEOUT
								* BeaconNettyNetworkTunnel.PING_FROM_BEACON_SERVER_CHECK_FACTOR)) < time)) {
			logger.warn("ping from server failed after {}",
					(time - fromBeaconServer.getLastMessageReceivedFromServerBeacon()));
			receivedOnErrorInFromObserver();
		}
	}

	@Override
	public void close() {
		statusNow = StatusNow.CLOSED;
		if (TRACE_LOG_IN_INFO)
			logger.info("Beacon connection tunnel id {} role {} is now closed", beaconNettyNetworkTunnel.getTunnelId(),
					beaconNettyNetworkTunnel);
	}

	BeaconNettyNetworkTunnel getBeaconNetworkTunnel() {
		return beaconNettyNetworkTunnel;
	}

	synchronized void receivedOnErrorInFromObserver() {
		beaconNettyNetworkTunnel.incrementPacketError();
		if (statusNow.equals(StatusNow.ONLINE)) {
			statusNow = StatusNow.RECONNECTION;
			if (TRACE_LOG_IN_INFO)
				logger.info("Beacon connection tunnel id {} role {} is now in recconnection state",
						beaconNettyNetworkTunnel.getTunnelId(), beaconNettyNetworkTunnel);
			createNewBeaconConnection();
		} else {
			if (TRACE_LOG_IN_INFO)
				logger.info("Beacon connection tunnel id {} role {} received onError but it is in state {}",
						beaconNettyNetworkTunnel.getTunnelId(), beaconNettyNetworkTunnel, statusNow);
		}
	}
}
