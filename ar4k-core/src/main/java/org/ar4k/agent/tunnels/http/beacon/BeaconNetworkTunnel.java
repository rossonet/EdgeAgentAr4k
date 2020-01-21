package org.ar4k.agent.tunnels.http.beacon;

import java.util.UUID;

import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.network.NetworkConfig;
import org.ar4k.agent.network.NetworkHub;
import org.ar4k.agent.network.NetworkTunnel;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.ResponseNetworkChannel;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelServiceV1Grpc.TunnelServiceV1Stub;

public class BeaconNetworkTunnel implements NetworkTunnel {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(BeaconNetworkTunnel.class.toString());

  private final String uniqueBeaconNetwork = UUID.randomUUID().toString();
  private final NetworkConfig config;
  private final boolean ownerRequest;
  private final NetworkHub hub = new Ar4kNetworkHub();
  private Agent remoteAgent = null;
  private final Agent me;
  private ResponseNetworkChannel responseNetworkChannel = null;
  private long targetId;
  private final TunnelServiceV1Stub asyncStubTunnel;

  public BeaconNetworkTunnel(Agent me, NetworkConfig config, boolean ownerRequest,
      TunnelServiceV1Stub asyncStubTunnel) {
    this.me = me;
    this.config = config;
    this.ownerRequest = ownerRequest;
    targetId = UUID.randomUUID().getLeastSignificantBits();
    this.asyncStubTunnel = asyncStubTunnel;
    logger.debug("created network object " + targetId);
  }

  @Override
  public NetworkConfig getConfig() {
    return config;
  }

  @Override
  public void init() throws ServiceInitException {
    logger.debug("starting network node " + targetId);
    // logger.info("starting BeaconNetworkTunnel");
    hub.setConfig(config);
    hub.setAsyncStubTunnel(asyncStubTunnel);
    hub.setTunnelId(targetId);
    hub.setAgent(me);
    hub.setUniqueNetworkLink(uniqueBeaconNetwork);
    hub.runNetty(ownerRequest);
    logger.debug("started network hub " + targetId);
  }

  @Override
  public void kill() {
    logger.debug("stopping BeaconNetworkTunnel");
    try {
      hub.close();
    } catch (Exception e) {
      logger.logException(e);
    }
  }

  @Override
  public NetworkHub getHub() {
    return hub;
  }

  public void setResponseNetworkChannel(ResponseNetworkChannel response) {
    this.responseNetworkChannel = response;
    this.targetId = response.getTargeId();

  }

  public boolean isOwnerRequest() {
    return ownerRequest;
  }

  @Override
  public void close() throws Exception {
    kill();
  }

  public long getTargetId() {
    return targetId;
  }

  public Agent getRemoteAgent() {
    return remoteAgent;
  }

  public void setRemoteAgent(Agent remoteAgent) {
    this.remoteAgent = remoteAgent;
  }

}
