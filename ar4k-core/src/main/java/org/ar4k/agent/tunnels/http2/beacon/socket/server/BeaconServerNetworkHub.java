package org.ar4k.agent.tunnels.http2.beacon.socket.server;

import static org.ar4k.agent.tunnels.http2.beacon.socket.netty.BeaconNettyNetworkTunnel.TRACE_LOG_IN_INFO;

import java.util.List;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http2.grpc.beacon.TunnelMessage;

import io.grpc.stub.StreamObserver;

public class BeaconServerNetworkHub implements StreamObserver<TunnelMessage>, AutoCloseable {
	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconServerNetworkHub.class.toString());

	private final StreamObserver<TunnelMessage> responseObserver;
	private TunnelRunnerBeaconServer tunnel = null;
	private final List<TunnelRunnerBeaconServer> tunnelsRegister;
	private boolean closed = false;

	public BeaconServerNetworkHub(StreamObserver<TunnelMessage> responseObserver,
			List<TunnelRunnerBeaconServer> tunnels) {
		this.responseObserver = responseObserver;
		this.tunnelsRegister = tunnels;
		if (TRACE_LOG_IN_INFO)
			logger.info("BeaconServerNetworkObserver created");
	}

	@Override
	public void onNext(TunnelMessage value) {
		try {
			if (!closed) {
				if (TRACE_LOG_IN_INFO)
					logger.info("Received on BeaconServer [session:" + value.getSessionId() + ",target:"
							+ value.getTunnelId() + ", data:" + value + "]");
				if (tunnel == null) {
					for (final TunnelRunnerBeaconServer t : tunnelsRegister) {
						if (TRACE_LOG_IN_INFO)
							logger.info("searching target from: {}", t);
						if (value.getTunnelId() == t.getTunnelId()) {
							tunnel = t;
							break;
						}
					}
				}
				if (TRACE_LOG_IN_INFO)
					logger.info("From BeaconServer send to tunnel {}", tunnel);
				if (tunnel != null) {
					if (TRACE_LOG_IN_INFO)
						logger.info("send message {} from queue", value);
					tunnel.onNext(value, responseObserver);
				} else {
					if (TRACE_LOG_IN_INFO)
						logger.info("beacon server bytes in cache waiting {}", value.getTunnelId());
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
		if (TRACE_LOG_IN_INFO)
			logger.info("Error on BeaconServer to tunnel {} -> {}", tunnel, t.getMessage());
		if (tunnel != null) {
			tunnel.onError(t, responseObserver);
		}
	}

	@Override
	public void onCompleted() {
		closed = true;
		if (TRACE_LOG_IN_INFO)
			logger.info("Complete stream on BeaconServer to tunnel {}", tunnel);
		if (tunnel != null) {
			tunnel.onCompleted(responseObserver);
			tunnel = null;
		}
	}

	public boolean isClosed() {
		return closed;
	}

	@Override
	public void close() throws Exception {
		closed = true;
		try {
			onCompleted();
		} catch (final Exception a) {
			logger.info("Exception closing Beacon server hub {}", a.toString());
		}
	}

}
