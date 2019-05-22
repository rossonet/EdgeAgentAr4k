package org.ar4k.agent.tunnels.http.grpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.RpcServiceV1Grpc;
import org.ar4k.agent.tunnels.http.grpc.beacon.Status;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class BeaconServer implements Runnable {
  private static final Logger logger = Logger.getLogger(BeaconServer.class.getName());

  private final int port;
  private final Server server;
  private int defaultPollTime = 6000;
  private final List<BeaconAgent> agentLabelRegisterReplies = new ArrayList<>();

  private boolean running = false;

  private Thread process = null;

  public BeaconServer(int port) throws IOException {
    this(ServerBuilder.forPort(port), port);
  }

  public BeaconServer(ServerBuilder<?> serverBuilder, int port) {
    this.port = port;
    server = serverBuilder.addService(new RpcService()).build();
  }

  public void start() throws IOException {
    server.start();
    running = true;
    logger.info("Server Beacon started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        // Use stderr here since the logger may have been reset by its JVM shutdown
        // hook.
        System.err.println("*** shutting down Beacon server since JVM is shutting down");
        BeaconServer.this.stop();
      }
    });
    if (process == null) {
      process = new Thread(this);
      process.start();
    }
  }

  public void stop() {
    if (server != null) {
      server.shutdown();
    }
    running = false;
  }

  public void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  private class RpcService extends RpcServiceV1Grpc.RpcServiceV1ImplBase {
    @Override
    public void register(RegisterRequest request, StreamObserver<RegisterReply> responseObserver) {
      RegisterReply reply = RegisterReply.newBuilder().setResult(Status.GOOD).setRegisterCode(request.getName())
          .setMonitoringFrequency(defaultPollTime).build();
      agentLabelRegisterReplies.add(new BeaconAgent(request, reply));
      responseObserver.onNext(reply);
      responseObserver.onCompleted();
    }
  }

  public String getStatus() {
    return server != null ? ("running on " + server.getPort()) : null;
  }

  public boolean isStopped() {
    return server != null ? (server.isShutdown() || server.isTerminated()) : true;
  }

  public int getPort() {
    return port;
  }

  public int getDefaultPollTime() {
    return defaultPollTime;
  }

  public void setDefaultPollTime(int defaultPollTime) {
    this.defaultPollTime = defaultPollTime;
  }

  public List<BeaconAgent> getAgentLabelRegisterReplies() {
    return agentLabelRegisterReplies;
  }

  @Override
  public void run() {
    while (running) {
      try {
        Thread.sleep((long) defaultPollTime);
      } catch (InterruptedException e) {
        logger.info("in Beacon server loop " + e.getMessage());
      }
    }
  }
}
