package org.ar4k.agent.tunnels.http2.beacon;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.tunnels.http2.beacon.client.BeaconClient;

public class BeaconDataAddress extends DataAddress {

	private final BeaconClient beaconClient;

	public BeaconDataAddress(BeaconClient beaconClient, Homunculus homunculus) {
		super(homunculus);
		this.beaconClient = beaconClient;
	}

	public BeaconClient getBeaconClient() {
		return beaconClient;
	}

}
