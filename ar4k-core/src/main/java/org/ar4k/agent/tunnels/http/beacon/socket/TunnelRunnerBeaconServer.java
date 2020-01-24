package org.ar4k.agent.tunnels.http.beacon.socket;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage;

import io.grpc.stub.StreamObserver;

public class TunnelRunnerBeaconServer {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(TunnelRunnerBeaconServer.class.toString());

  private final long targeId;
  private boolean active = false;
  private Agent serverAgent = null;
  private Agent clientAgent = null;
  private final Map<Long, StreamObserver<TunnelMessage>> serverObserver = new ConcurrentHashMap<>();

  private StreamObserver<TunnelMessage> clientObserver = null;

  public TunnelRunnerBeaconServer(long targeId) {
    logger.info("tunnel " + targeId + " created");
    this.targeId = targeId;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public long getTargeId() {
    return targeId;
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
    logger.trace("nextAgent -> " + nextAgent + " " + value);
    if (nextAgent != null) {
      logger.trace("Stream observer found");
      nextAgent.onNext(value);
    } else {
      logger.trace("Stream observer not found for " + value);
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
        if (serverObserver.get(value.getSessionId()) == null) {
          serverObserver.put(value.getSessionId(), responseObserver);
          logger.info("new session " + value.getSessionId() + " in tunnel " + value.getTargeId());
        }
        if (clientObserver != null) {
          nextAgentObserver = clientObserver;
        } else {
          logger.trace("clientObserver not found by agent");
        }
      }
      if (value.getAgent().equals(clientAgent)) {
        logger.trace("request from client by Agent");
        if (clientObserver == null) {
          clientObserver = responseObserver;
          logger.trace("clientObserver registered by Agent");
        }
        if (serverObserver.get(value.getSessionId()) != null) {
          nextAgentObserver = serverObserver.get(value.getSessionId());
        } else {
          logger.trace("serverObserver not found in agent");
        }
      }
    }
    if (nextAgentObserver == null) {
      if (serverAgent.equals(clientAgent)) {
        logger.trace("client and service host uniqueId are equal");
        if (value.getUniqueId().startsWith("S_")) {
          logger.trace("request from server by UUID");
          if (serverObserver.get(value.getSessionId()) == null) {
            serverObserver.put(value.getSessionId(), responseObserver);
            logger.info("new session " + value.getSessionId() + " in tunnel " + value.getTargeId());
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
          if (serverObserver.get(value.getSessionId()) != null) {
            nextAgentObserver = serverObserver.get(value.getSessionId());
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

  public Set<Long> getServerObserver() {
    return serverObserver.keySet();
  }

}
