package org.ar4k.agent.tunnels.http2.beacon.socket.netty;

import org.ar4k.agent.tunnels.http2.beacon.socket.AbstractBeaconNetworkSocketConfig;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RequestToAgent;

public class BeaconNettyNetworkConfig extends AbstractBeaconNetworkSocketConfig {

	public BeaconNettyNetworkConfig(RequestToAgent m) {
		super(m);
	}

	public BeaconNettyNetworkConfig(String name, String description, NetworkMode role, NetworkProtocol tcp,
			String destinationIp, int destinationIpPort, int sourceServerPort) {
		super(name, description, role, NetworkProtocol.TCP, destinationIp, destinationIpPort, sourceServerPort);
	}

	private static final long serialVersionUID = 552678909268206460L;

}
