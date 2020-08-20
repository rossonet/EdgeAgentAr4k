package org.ar4k.agent.network;

import org.ar4k.agent.config.ConfigSeed;

public interface NetworkConfig extends ConfigSeed {
	public static enum NetworkProtocol {
		UDP, TCP
	}

	public static enum NetworkMode {
		SERVER, CLIENT
	}

	String getServerBindIp();

	int getServerPort();

	String getClientIp();

	int getClientPort();

	NetworkProtocol getNetworkProtocol();

	NetworkMode getNetworkModeRequested();

}
