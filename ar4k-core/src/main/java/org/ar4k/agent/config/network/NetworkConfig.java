package org.ar4k.agent.config.network;

import org.ar4k.agent.core.interfaces.ConfigSeed;

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
