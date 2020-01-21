package org.ar4k.agent.tunnels.http.beacon;

import java.util.Collection;
import java.util.UUID;

import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.network.NetworkConfig;
import org.ar4k.agent.tunnels.http.grpc.beacon.RequestToAgent;
import org.ar4k.agent.tunnels.http.grpc.beacon.RequestTunnelMessage;
import org.joda.time.Instant;

public class BeaconNetworkConfig implements NetworkConfig {

  private static final long serialVersionUID = 552608909268206460L;
  private final NetworkMode networkMode;
  private final NetworkProtocol networkProtocol;
  private final int clientPort;
  private final String clientIp;
  private final int serverPort;
  private final String serverBindIp;
  private final String uniqueId;
  private final Instant lastUpdateData;
  private final Instant creationDate;
  private final Collection<String> tags;
  private final String description;
  private final String name;

  // quando chiamato da un altro agente
  public BeaconNetworkConfig(RequestToAgent m) {
    RequestTunnelMessage networkRequest = m.getTunnelRequest();
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
    uniqueId = networkRequest.getUniqueIdRequest();
    lastUpdateData = Instant.now();
    creationDate = Instant.now();
    tags = null;
    description = "tunnel request from agent " + m.getCaller().getAgentUniqueName();
    name = "tunnel_" + serverPort + "_" + clientPort + "_" + uniqueId;
  }

  // quando chiamato per iniziare un tunnel
  public BeaconNetworkConfig(String name, String description, NetworkMode remoteNetworkModeRequested,
      NetworkProtocol networkProtocol, String destinationIp, int destinationIpPort, int srcPort) {
    clientIp = destinationIp;
    clientPort = destinationIpPort;
    serverPort = srcPort;
    serverBindIp = "0.0.0.0";
    uniqueId = UUID.randomUUID().toString().replace("-", ".");
    lastUpdateData = Instant.now();
    creationDate = Instant.now();
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
  public Collection<String> getTags() {
    return tags;
  }

  @Override
  public Instant getCreationDate() {
    return creationDate;
  }

  @Override
  public Instant getLastUpdateDate() {
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
    return "BeaconNetworkConfig [networkMode=" + networkMode + ", networkProtocol=" + networkProtocol + ", clientPort="
        + clientPort + ", clientIp=" + clientIp + ", serverPort=" + serverPort + ", serverBindIp=" + serverBindIp
        + ", uniqueId=" + uniqueId + ", lastUpdateData=" + lastUpdateData + ", creationDate=" + creationDate + ", tags="
        + tags + ", description=" + description + ", name=" + name + "]";
  }

}
