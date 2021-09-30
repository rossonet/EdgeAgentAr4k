package org.ar4k.agent.tunnels.http2.beacon;

import org.ar4k.agent.core.EdgeAgentCore;
import org.ar4k.agent.core.data.DataAddressBase;
import org.ar4k.agent.tunnels.http2.beacon.client.BeaconClient;

public class BeaconDataAddress extends DataAddressBase {

	private final BeaconClient beaconClient;

	public BeaconDataAddress(BeaconClient beaconClient, EdgeAgentCore edgeAgentCore) {
		super(edgeAgentCore, beaconClient);
		this.beaconClient = beaconClient;
	}

	public BeaconClient getBeaconClient() {
		return beaconClient;
	}

}
