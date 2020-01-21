package org.ar4k.agent.tunnels.http.beacon.socket;

import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.RequestTunnelMessage;
import org.ar4k.agent.tunnels.http.grpc.beacon.ResponseNetworkChannel;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage;

import io.grpc.stub.StreamObserver;

public class TunnelRunnerBeaconServer {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(TunnelRunnerBeaconServer.class.toString());

  private final long targeId;
  private final RequestTunnelMessage request;
  private final ResponseNetworkChannel channelCreated;
  private boolean active = false;
  private Agent serverAgent = null;
  private Agent clientAgent = null;
  private StreamObserver<TunnelMessage> serverObserver = null;
  private StreamObserver<TunnelMessage> clientObserver = null;

  public TunnelRunnerBeaconServer(long targeId, RequestTunnelMessage request, ResponseNetworkChannel channelCreated) {
    logger.debug("tunnel " + targeId + " created");
    this.targeId = targeId;
    this.request = request;
    this.channelCreated = channelCreated;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public long getTargeId() {
    return targeId;
  }

  public RequestTunnelMessage getRequest() {
    return request;
  }

  public ResponseNetworkChannel getChannelCreated() {
    return channelCreated;
  }

  public boolean isActive() {
    return active;
  }

  public void setServerAgent(Agent agent) {
    this.serverAgent = agent;

  }

  public Agent getServerAgent() {
    return serverAgent;
  }

  public void setClientAgent(Agent agent) {
    this.clientAgent = agent;
  }

  public void onNext(TunnelMessage value, StreamObserver<TunnelMessage> responseObserver) {
    StreamObserver<TunnelMessage> nextAgent = getNextAgentObserver(value, responseObserver);
    logger.trace("nextAgent -> " + nextAgent);
    if (nextAgent != null) {
      logger.trace("Stream observer found");
      nextAgent.onNext(value);
    } else {
      logger.debug("Stream observer not found for " + value);
    }
  }

  // metodo per routing principale
  private StreamObserver<TunnelMessage> getNextAgentObserver(TunnelMessage value,
      StreamObserver<TunnelMessage> responseObserver) {
    StreamObserver<TunnelMessage> nextAgentObserver = null;
    if (!serverAgent.equals(clientAgent)) {
      logger.trace("client and service host are different");
      if (value.getAgent().equals(serverAgent)) {
        logger.trace("request from server by Agent");
        if (serverObserver == null) {
          serverObserver = responseObserver;
          logger.debug("serverObserver registered by Agent");
        }
        if (clientObserver != null) {
          nextAgentObserver = clientObserver;
        } else {
          logger.info("clientObserver not found by agent");
        }
      }
      if (value.getAgent().equals(clientAgent)) {
        logger.trace("request from client by Agent");
        if (clientObserver == null) {
          clientObserver = responseObserver;
          logger.debug("clientObserver registered by Agent");
        }
        if (serverObserver != null) {
          nextAgentObserver = serverObserver;
        } else {
          logger.info("serverObserver not found in agent");
        }
      }
    }
    if (nextAgentObserver == null) {
      if (serverAgent.equals(clientAgent)) {
        logger.trace("client and service host uniqueId are equal");
        if (value.getUniqueId().startsWith("S_")) {
          logger.trace("request from server by UUID");
          if (serverObserver == null) {
            serverObserver = responseObserver;
            logger.trace("serverObserver registered in uuid");
          }
          if (clientObserver != null) {
            nextAgentObserver = clientObserver;
          } else {
            logger.trace("clientObserver not found by uuid");
          }
        } else if (value.getUniqueId().startsWith("C_")) {
          logger.trace("request from client by UUID");
          if (clientObserver == null) {
            clientObserver = responseObserver;
            logger.trace("clientObserver registered in uuid");
          }
          if (serverObserver != null) {
            nextAgentObserver = serverObserver;
          } else {
            logger.trace("serverObserver not found in uuid");
          }
        } else {
          logger.info("prefix of " + value.getUniqueId() + " is not valid");
        }
      }
    }
    return nextAgentObserver;
  }

  @Override
  public String toString() {
    return "TunnelRunnerBeaconServer [targeId=" + targeId + ", active=" + active + ", serverAgent=" + serverAgent
        + ", clientAgent=" + clientAgent + "]";
  }

  public Agent getClientAgent() {
    return clientAgent;
  }

  public void onError(Throwable t, StreamObserver<TunnelMessage> responseObserver) {
    logger.logException("Error on TunnelRunnerBeaconServer " + targeId, t);

  }

  public void onCompleted(StreamObserver<TunnelMessage> responseObserver) {
    logger.debug("Complete on TunnelRunnerBeaconServer " + targeId);

  }

}
