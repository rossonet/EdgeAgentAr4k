package org.ar4k.agent.tunnels.http.beacon.socket;

import java.util.List;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage;

import io.grpc.stub.StreamObserver;

public class BeaconServerNetworkHub implements StreamObserver<TunnelMessage> {
	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconServerNetworkHub.class.toString());

	private final boolean trace = false;

	private final StreamObserver<TunnelMessage> responseObserver;
	private TunnelRunnerBeaconServer tunnel = null;
	private final List<TunnelRunnerBeaconServer> tunnelsRegister;
	private boolean closed = false;

	public BeaconServerNetworkHub(StreamObserver<TunnelMessage> responseObserver,
			List<TunnelRunnerBeaconServer> tunnels) {
		this.responseObserver = responseObserver;
		this.tunnelsRegister = tunnels;
		if (trace)
			logger.info("BeaconServerNetworkObserver created");
	}

	@Override
	public void onNext(TunnelMessage value) {
		try {
			if (!closed) {
				if (trace)
					logger.info("Received on BeaconServer [session:" + value.getSessionId() + ",target:"
							+ value.getTargeId() + ", data:" + value.getPayload() + "]");
				if (tunnel == null) {
					for (final TunnelRunnerBeaconServer t : tunnelsRegister) {
						if (trace)
							logger.info("searching target from: " + t);
						if (value.getTargeId() == t.getTunnelId()) {
							tunnel = t;
							break;
						}
					}
				}
				if (trace)
					logger.info("From BeaconServer send to tunnel " + tunnel);
				if (tunnel != null) {
					if (trace)
						logger.info("send message from queue");
					tunnel.onNext(value, responseObserver);
				} else {
					if (trace)
						logger.info("beacon server bytes in cache waiting " + value.getTargeId());
				}
			} else {
				logger.warn("received data on closed NetworkChannelStreamObserver");
			}
		} catch (final Exception a) {
			logger.logException("EXCEPTION IN MESSAGE ON BEACON SERVER", a);
		}
	}

	@Override
	public void onError(Throwable t) {
		closed = true;
		if (trace)
			logger.info("Error on BeaconServer to tunnel" + tunnel + " -> " + t.getMessage());
		if (tunnel != null) {
			tunnel.onError(t, responseObserver);
		}
	}

	@Override
	public void onCompleted() {
		closed = true;
		if (trace)
			logger.info("Complete stream on BeaconServer to tunnel " + tunnel);
		if (tunnel != null) {
			tunnel.onCompleted(responseObserver);
			tunnel = null;
		}
	}

	public boolean isClosed() {
		return closed;
	}

}
