package org.ar4k.agent.tunnels.http2.beacon.socket;

import java.util.List;
import java.util.UUID;

import org.ar4k.agent.config.network.NetworkConfig;
import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RequestToAgent;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RequestTunnelMessage;
import org.joda.time.Instant;

//TODO revisione tunnel on grpc 

// https://github.com/ejona86/grpc-java/blob/29728aeb003ced3c190197c176563643be22bef1/examples/src/main/java/io/grpc/examples/grpcproxy/GrpcProxy.java

// https://github.com/bbottema/java-socks-proxy-server

// https://github.com/CtheSky/JSocks

// https://github.com/hsupu/netty-socks

public abstract class AbstractBeaconNetworkSocketConfig implements NetworkConfig {

	private static final long serialVersionUID = 552608909268206460L;
	private final NetworkMode networkMode;
	private final NetworkProtocol networkProtocol;
	private final int clientPort;
	private final String clientIp;
	private final int serverPort;
	private final String serverBindIp;
	private final String uniqueId;
	private final long lastUpdateData;
	private final long creationDate;
	private final List<String> tags;
	private final String description;
	private final String name;

	public AbstractBeaconNetworkSocketConfig(RequestToAgent m) {
		final RequestTunnelMessage networkRequest = m.getTunnelRequest();
		clientIp = networkRequest.getDestIp();
		clientPort = networkRequest.getDestPort();
		switch (networkRequest.getMode()) {
		case BYTES_TO_CLIENT_TCP:
			networkMode = NetworkMode.CLIENT;
			networkProtocol = NetworkProtocol.TCP;
			break;
		case BYTES_TO_CLIENT_UDP:
			networkMode = NetworkMode.CLIENT;
			networkProtocol = NetworkProtocol.UDP;
			break;
		case SERVER_TO_BYTES_TCP:
			networkMode = NetworkMode.SERVER;
			networkProtocol = NetworkProtocol.TCP;
			break;
		case SERVER_TO_BYTES_UDP:
			networkMode = NetworkMode.SERVER;
			networkProtocol = NetworkProtocol.UDP;
			break;
		default:
			throw new ServiceInitException("in " + m.toString() + " no valid NetworkMode -> " + networkRequest);
		}
		serverPort = networkRequest.getSrcPort();
		serverBindIp = "0.0.0.0";
		uniqueId = String.valueOf(networkRequest.getTargeId());
		lastUpdateData = Instant.now().getMillis();
		creationDate = Instant.now().getMillis();
		tags = null;
		description = "tunnel request from agent " + m.getCaller().getAgentUniqueName();
		name = "tunnel_" + serverPort + "_" + clientPort + "_" + uniqueId;
	}

	// quando chiamato per iniziare un tunnel
	public AbstractBeaconNetworkSocketConfig(String name, String description, NetworkMode remoteNetworkModeRequested,
			NetworkProtocol networkProtocol, String destinationIp, int destinationIpPort, int srcPort) {
		clientIp = destinationIp;
		clientPort = destinationIpPort;
		serverPort = srcPort;
		serverBindIp = "0.0.0.0";
		uniqueId = UUID.randomUUID().toString();
		lastUpdateData = Instant.now().getMillis();
		creationDate = Instant.now().getMillis();
		tags = null;
		this.networkProtocol = networkProtocol;
		this.description = description;
		this.name = name;
		this.networkMode = remoteNetworkModeRequested;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public List<String> getTags() {
		return tags;
	}

	@Override
	public long getCreationDate() {
		return creationDate;
	}

	@Override
	public long getLastUpdate() {
		return lastUpdateData;
	}

	@Override
	public String getUniqueId() {
		return uniqueId;
	}

	@Override
	public String getServerBindIp() {
		return serverBindIp;
	}

	@Override
	public int getServerPort() {
		return serverPort;
	}

	@Override
	public String getClientIp() {
		return clientIp;
	}

	@Override
	public int getClientPort() {
		return clientPort;
	}

	@Override
	public NetworkProtocol getNetworkProtocol() {
		return networkProtocol;
	}

	@Override
	public NetworkMode getNetworkModeRequested() {
		return networkMode;
	}

	@Override
	public String toString() {
		return "BeaconNetworkConfig [networkMode=" + networkMode + ", networkProtocol=" + networkProtocol
				+ ", clientPort=" + clientPort + ", clientIp=" + clientIp + ", serverPort=" + serverPort
				+ ", serverBindIp=" + serverBindIp + ", uniqueId=" + uniqueId + ", lastUpdateData=" + lastUpdateData
				+ ", creationDate=" + creationDate + ", tags=" + tags + ", description=" + description + ", name="
				+ name + "]";
	}

}
