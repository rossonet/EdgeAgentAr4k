package org.ar4k.agent.tunnels.http2.beacon.server;

import java.util.UUID;

import org.ar4k.agent.tunnels.http2.beacon.BeaconAgent;
import org.ar4k.agent.tunnels.http2.beacon.socket.server.BeaconServerNetworkHub;
import org.ar4k.agent.tunnels.http2.beacon.socket.server.TunnelRunnerBeaconServer;
import org.ar4k.agent.tunnels.http2.grpc.beacon.CommandReplyRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.CommandType;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RequestToAgent;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RequestTunnelMessage;
import org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel;
import org.ar4k.agent.tunnels.http2.grpc.beacon.TunnelMessage;
import org.ar4k.agent.tunnels.http2.grpc.beacon.TunnelServiceV1Grpc;
import org.ar4k.agent.tunnels.http2.grpc.beacon.TunnelType;

import io.grpc.stub.StreamObserver;

class BeaconServerTunnelService extends TunnelServiceV1Grpc.TunnelServiceV1ImplBase {

	/**
	 * 
	 */
	private final BeaconServer beaconServer;

	/**
	 * @param beaconServer
	 */
	BeaconServerTunnelService(BeaconServer beaconServer) {
		this.beaconServer = beaconServer;
	}

	@Override
	public StreamObserver<TunnelMessage> openNetworkChannel(StreamObserver<TunnelMessage> responseObserver) {
		BeaconServer.logger.debug("Received on openNetworkChannel " + responseObserver.hashCode());
		return new BeaconServerNetworkHub(responseObserver, this.beaconServer.tunnels);
	}

	@Override
	public void requestTunnel(RequestTunnelMessage request,
			StreamObserver<ResponseNetworkChannel> responseObserver) {
		BeaconServer.logger.debug("Beacon client require tunnel -> " + request);
		try {
			final long tunnelUniqueId = UUID.randomUUID().getMostSignificantBits();
			TunnelRunnerBeaconServer tunnelRunner = null;
			if (request.getMode().equals(TunnelType.SERVER_TO_BYTES_TCP)
					|| request.getMode().equals(TunnelType.SERVER_TO_BYTES_UDP)) {
				tunnelRunner = new TunnelRunnerBeaconServer(tunnelUniqueId, request.getAgentDestination(),
						request.getAgentSource());
			} else if (request.getMode().equals(TunnelType.BYTES_TO_CLIENT_TCP)
					|| request.getMode().equals(TunnelType.BYTES_TO_CLIENT_UDP)) {
				tunnelRunner = new TunnelRunnerBeaconServer(tunnelUniqueId, request.getAgentSource(),
						request.getAgentDestination());
			}
			this.beaconServer.getTunnels().add(tunnelRunner);
			final ResponseNetworkChannel channelCreated = requestNetworkClientToAgent(request, tunnelUniqueId);
			responseObserver.onNext(channelCreated);
			responseObserver.onCompleted();
		} catch (final Exception e) {
			BeaconServer.logger.logException(e);
		}
	}

	private ResponseNetworkChannel requestNetworkClientToAgent(RequestTunnelMessage request, long idRequest)
			throws InterruptedException {
		BeaconServer.logger.debug("searching in " + this.beaconServer.agents.size() + " agents");
		for (final BeaconAgent at : this.beaconServer.agents) {
			if (at.getAgentUniqueName().equals(request.getAgentDestination().getAgentUniqueName())) {
				final RequestToAgent rta = RequestToAgent.newBuilder().setCaller(request.getAgentSource())
						.setUniqueIdRequest(String.valueOf(idRequest)).setType(CommandType.EXPOSE_PORT)
						.setTunnelRequest(request).build();
				at.addRequestForAgent(rta);
				BeaconServer.logger.debug("Required client tunnel to agent target -> " + rta);
				break;
			}
		}
		final CommandReplyRequest cmdReply = this.beaconServer.waitReply(String.valueOf(idRequest), BeaconServer.DEFAULT_TIMEOUT);
		final ResponseNetworkChannel channelCreated = cmdReply.getTunnelReply();
		BeaconServer.logger.debug("Beacon client tunnel reply -> " + channelCreated);
		return channelCreated;
	}
}