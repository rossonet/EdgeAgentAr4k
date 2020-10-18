package org.ar4k.agent.tunnels.http2.beacon.socket.classic;

import org.ar4k.agent.tunnels.http2.beacon.socket.AbstractBeaconNetworkSocketConfig;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RequestToAgent;

public class BeaconNetworkClassicConfig extends AbstractBeaconNetworkSocketConfig {

	public BeaconNetworkClassicConfig(RequestToAgent m) {
		super(m);
	}

	public BeaconNetworkClassicConfig(String name, String description, NetworkMode remoteNetworkModeRequested,
			NetworkProtocol networkProtocol, String destinationIp, int destinationIpPort, int srcPort) {
		super(name, description, remoteNetworkModeRequested, networkProtocol, destinationIp, destinationIpPort,
				srcPort);
	}

	private static final long serialVersionUID = 552678909268206460L;

}
