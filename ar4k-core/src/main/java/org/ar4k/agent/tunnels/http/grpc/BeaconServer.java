package org.ar4k.agent.tunnels.http.grpc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.Command;
import org.ar4k.agent.tunnels.http.grpc.beacon.CommandReplyRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.CommandType;
import org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.DataServiceV1Grpc;
import org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.Empty;
import org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage;
import org.ar4k.agent.tunnels.http.grpc.beacon.HealthRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.ListAgentsReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.RequestToAgent;
import org.ar4k.agent.tunnels.http.grpc.beacon.RpcServiceV1Grpc;
import org.ar4k.agent.tunnels.http.grpc.beacon.Status;
import org.ar4k.agent.tunnels.http.grpc.beacon.StatusValue;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class BeaconServer implements Runnable {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(BeaconServer.class.toString());
  private static final long defaultTimeOut = 10000L;
  private static final long waitReplyLoopWaitTime = 300L;

  private int port = 0;
  private Server server = null;
  private int defaultPollTime = 6000;
  private int defaultBeaconFlashMoltiplicator = 1;
  private final List<BeaconAgent> agentLabelRegisterReplies = new ArrayList<>();
  private boolean acceptAllCerts = true;
  private boolean running = false;
  private transient Anima anima = null;

  private Thread process = null;
  private DatagramSocket socketFlashBeacon = null;

  private final Map<String, CommandReplyRequest> repliesQueue = new ConcurrentHashMap<>();
  private int discoveryPort = 0;
  private String broadcastAddress = "255.255.255.255";
  private String stringDiscovery = "AR4K-BEACON-" + UUID.randomUUID().toString() + ":";

  private final static String aliasBeaconServerInKeystore = "beacon-server";

  public BeaconServer(Anima anima, int port, int discoveryPort, String broadcastAddress, boolean acceptCerts,
      String stringDiscovery) throws IOException {
    this(ServerBuilder.forPort(port), port);
    this.anima = anima;
    this.acceptAllCerts = acceptCerts;
    this.discoveryPort = discoveryPort;
    this.broadcastAddress = broadcastAddress;
    this.stringDiscovery = stringDiscovery;
    if (anima.getMyIdentityKeystore().listCertificate().contains(aliasBeaconServerInKeystore)) {
      logger.info("Certificate for Beacon server is present in keystore");
    } else {
      logger.info("Create certificate for beacon server");
      anima.getMyIdentityKeystore().createSelfSignedCert(
          aliasBeaconServerInKeystore + "-" + UUID.randomUUID().toString(), ConfigHelper.organization,
          ConfigHelper.unit, ConfigHelper.locality, ConfigHelper.state, ConfigHelper.country, ConfigHelper.uri,
          ConfigHelper.dns, ConfigHelper.ip, aliasBeaconServerInKeystore);
      logger.info("Certificate for beacon server: " + anima.getMyIdentityKeystore()
          .getClientCertificate(aliasBeaconServerInKeystore).getSubjectX500Principal().toString() + " - alias "
          + aliasBeaconServerInKeystore);
    }
  }

  private BeaconServer(ServerBuilder<?> serverBuilder, int port) {
    this.port = port;
    server = serverBuilder.addService(new RpcService()).addService(new DataService()).build();
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
    if (socketFlashBeacon != null)
      socketFlashBeacon.close();
  }

  public void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  private class DataService extends DataServiceV1Grpc.DataServiceV1ImplBase {

  }

  private class RpcService extends RpcServiceV1Grpc.RpcServiceV1ImplBase {

    @Override
    public void sendHealth(HealthRequest request, io.grpc.stub.StreamObserver<Status> responseObserver) {
      responseObserver.onNext(Status.newBuilder().setStatusValue(StatusValue.GOOD.getNumber()).build());
      responseObserver.onCompleted();
    }

    @Override
    public void sendCommandReply(CommandReplyRequest request, StreamObserver<Status> responseObserver) {
      repliesQueue.put(request.getUniqueIdRequest(), request);
      responseObserver.onNext(Status.newBuilder().setStatus(StatusValue.GOOD).build());
      responseObserver.onCompleted();
    }

    @Override
    public void register(RegisterRequest request, StreamObserver<RegisterReply> responseObserver) {
      String uniqueClientNameForBeacon = UUID.randomUUID().toString();
      String certFromCsr = getCertForRegistration(aliasBeaconServerInKeystore, request.getRequestCsr(),
          request.getJsonHealth(), request.getDisplayKey(), uniqueClientNameForBeacon);
      logger.info("Certificate for beacon client (signed): "
          + anima.getMyIdentityKeystore().getClientCertificate(uniqueClientNameForBeacon) + " - alias "
          + uniqueClientNameForBeacon);
      logger.info("Certificate for beacon client (CA): " + anima.getMyIdentityKeystore()
          .getClientCertificate(aliasBeaconServerInKeystore).getSubjectX500Principal().toString() + " - alias "
          + aliasBeaconServerInKeystore);
      RegisterReply reply = RegisterReply.newBuilder().setCa(getBeaconCa()).setCert(certFromCsr)
          .setStatusRegistration(
              Status.newBuilder().setStatus(certFromCsr != null ? StatusValue.GOOD : StatusValue.WAIT_HUMAN))
          .setRegisterCode(uniqueClientNameForBeacon).setMonitoringFrequency(defaultPollTime).build();
      agentLabelRegisterReplies.add(new BeaconAgent(request, reply));
      responseObserver.onNext(reply);
      responseObserver.onCompleted();
    }

    private String getBeaconCa() {
      return anima.getMyIdentityKeystore().getClientCertificateBase64(aliasBeaconServerInKeystore);
    }

    private String getCertForRegistration(String idAuth, String requestCsr, String jsonHealth, String consoleKey,
        String targetAlias) {
      String result = null;
      if (acceptAllCerts) {
        result = anima.getMyIdentityKeystore().signCertificateBase64(requestCsr, targetAlias,
            ConfigHelper.defaulBeaconSignvalidity, idAuth);
      } else {
        // TODO filtrare per CA con console manuale e coda
        result = anima.getMyIdentityKeystore().signCertificateBase64(requestCsr, targetAlias,
            ConfigHelper.defaulBeaconSignvalidity, idAuth);
      }
      return result;
    }

    @Override
    public void listAgents(Empty request, StreamObserver<ListAgentsReply> responseObserver) {
      List<Agent> values = new ArrayList<>();
      for (BeaconAgent r : agentLabelRegisterReplies) {
        Agent a = Agent.newBuilder().setAgentUniqueName(r.getAgentUniqueName()).build();
        values.add(a);
      }
      ListAgentsReply reply = ListAgentsReply.newBuilder().addAllAgents(values)
          .setResult(Status.newBuilder().setStatus(StatusValue.GOOD)).build();
      responseObserver.onNext(reply);
      responseObserver.onCompleted();
    }

    @Override
    public void pollingCmdQueue(Agent request, StreamObserver<FlowMessage> responseObserver) {
      List<RequestToAgent> values = new ArrayList<>();
      for (BeaconAgent at : agentLabelRegisterReplies) {
        if (at.getAgentUniqueName().equals(request.getAgentUniqueName())) {
          values.addAll(at.getCommandsToBeExecute());
          break;
        }
      }
      FlowMessage fm = FlowMessage.newBuilder().addAllToDoList(values).build();
      responseObserver.onNext(fm);
      responseObserver.onCompleted();
    }

    @Override
    public void completeCommand(CompleteCommandRequest request, StreamObserver<CompleteCommandReply> responseObserver) {
      try {
        String idRequest = UUID.randomUUID().toString();
        for (BeaconAgent at : agentLabelRegisterReplies) {
          if (at.getAgentUniqueName().equals(request.getAgentTarget().getAgentUniqueName())) {
            RequestToAgent rta = RequestToAgent.newBuilder().setCaller(request.getAgentSender())
                .setUniqueIdRequest(idRequest).setType(CommandType.COMPLETE_COMMAND).addAllWords(request.getWordsList())
                .setWordIndex(request.getWordIndex()).setPosition(request.getPosition()).build();
            at.addRequestForAgent(rta);
            break;
          }
        }
        CommandReplyRequest agentReply = null;
        agentReply = waitReply(idRequest, defaultTimeOut);
        if (agentReply != null) {
          List<String> sb = new ArrayList<>();
          for (String cr : agentReply.getRepliesList()) {
            sb.add(cr);
          }
          CompleteCommandReply finalReply = CompleteCommandReply.newBuilder().addAllReplies(sb).build();
          responseObserver.onNext(finalReply);
        }
        responseObserver.onCompleted();
      } catch (Exception e) {
        logger.logException(e);
      }
    }

    @Override
    public void elaborateMessage(ElaborateMessageRequest request,
        StreamObserver<ElaborateMessageReply> responseObserver) {
      try {
        String idRequest = UUID.randomUUID().toString();
        for (BeaconAgent at : agentLabelRegisterReplies) {
          if (at.getAgentUniqueName().equals(request.getAgentTarget().getAgentUniqueName())) {
            RequestToAgent rta = RequestToAgent.newBuilder().setCaller(request.getAgentSender())
                .setUniqueIdRequest(idRequest).setType(CommandType.ELABORATE_MESSAGE_COMMAND)
                .setRequestCommand(request.getCommandMessage()).build();
            at.addRequestForAgent(rta);
            break;
          }
        }
        CommandReplyRequest agentReply = null;
        agentReply = waitReply(idRequest, defaultTimeOut);
        if (agentReply != null) {
          StringBuilder sb = new StringBuilder();
          for (String cr : agentReply.getRepliesList()) {
            sb.append(cr + "\n");
          }
          ElaborateMessageReply finalReply = ElaborateMessageReply.newBuilder().setReply(sb.toString()).build();
          responseObserver.onNext(finalReply);
        }
        responseObserver.onCompleted();
      } catch (Exception e) {
        logger.logException(e);
      }
    }

    @Override
    public void listCommands(ListCommandsRequest request, StreamObserver<ListCommandsReply> responseObserver) {
      try {
        String idRequest = UUID.randomUUID().toString();
        for (BeaconAgent at : agentLabelRegisterReplies) {
          if (at.getAgentUniqueName().equals(request.getAgentTarget().getAgentUniqueName())) {
            RequestToAgent rta = RequestToAgent.newBuilder().setCaller(request.getAgentSender())
                .setUniqueIdRequest(idRequest).setType(CommandType.LIST_COMMANDS).build();
            at.addRequestForAgent(rta);
            break;
          }
        }
        CommandReplyRequest agentReply = null;
        agentReply = waitReply(idRequest, defaultTimeOut);
        if (agentReply != null) {
          List<Command> listCommands = new ArrayList<>();
          for (String cr : agentReply.getRepliesList()) {
            Command c = Command.newBuilder().setAgentSender(agentReply.getAgentSender()).setCommand(cr).build();
            listCommands.add(c);
          }
          ListCommandsReply finalReply = ListCommandsReply.newBuilder().addAllCommands(listCommands).build();
          responseObserver.onNext(finalReply);
        }
        responseObserver.onCompleted();
      } catch (Exception e) {
        logger.logException(e);
      }
    }
  }

  public String getStatus() {
    return server != null ? ("running on " + server.getPort()) : null;
  }

  public CommandReplyRequest waitReply(String idRequest, long defaultTimeOut) throws InterruptedException {
    long start = new Date().getTime();
    CommandReplyRequest ret = null;
    try {
      while (new Date().getTime() < (start + defaultTimeOut)) {
        if (repliesQueue.containsKey(idRequest)) {
          ret = repliesQueue.remove(idRequest);
          break;
        }
        Thread.sleep(waitReplyLoopWaitTime);
      }
    } catch (Exception e) {
      logger.logException(e);
    }
    return ret;
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
    int counter = 1;
    while (running) {
      if (counter++ > defaultBeaconFlashMoltiplicator) {
        counter = 1;
        if (discoveryPort != 0)
          sendFlashUdp();
      }
      try {
        Thread.sleep((long) defaultPollTime);
      } catch (InterruptedException e) {
        logger.info("in Beacon server loop error " + e.getMessage());
        logger.logException(e);
      }
    }
  }

  public void sendFlashUdp() {
    try {
      // Open a random port to send the package
      if (socketFlashBeacon == null) {
        socketFlashBeacon = new DatagramSocket();
        socketFlashBeacon.setBroadcast(true);
      }
      byte[] sendData = (stringDiscovery + ":" + String.valueOf(port)).getBytes();
      try {
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
            InetAddress.getByName(broadcastAddress), discoveryPort);
        socketFlashBeacon.send(sendPacket);
        logger.debug(getClass().getName() + ">>> Request packet sent to: " + broadcastAddress);
      } catch (Exception e) {
        logger.logException(e);
        logger.warn("Error sending flash beacon " + e.getMessage());
      }
      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
      while (interfaces.hasMoreElements()) {
        NetworkInterface networkInterface = interfaces.nextElement();
        if (networkInterface.isLoopback() || !networkInterface.isUp()) {
          continue; // Don't want to broadcast to the loopback interface
        }
        for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
          InetAddress broadcast = interfaceAddress.getBroadcast();
          if (broadcast == null) {
            continue;
          }
          try {
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, discoveryPort);
            socketFlashBeacon.send(sendPacket);
          } catch (Exception e) {
            logger.logException(e);
            logger.warn("Error sending flash beacon on " + broadcast.getHostName() + " -> " + e.getMessage());
          }
          logger.debug(getClass().getName() + ">>> Request packet sent to: " + broadcast.getHostAddress()
              + "; Interface: " + networkInterface.getDisplayName());
        }
      }
    } catch (IOException ex) {
      logger.logException(ex);
      logger.warn("Exception in Beacon flash " + ex.getMessage());
    }
  }

  public int getDefaultBeaconFlashMoltiplicator() {
    return defaultBeaconFlashMoltiplicator;
  }

  public void setDefaultBeaconFlashMoltiplicator(int defaultBeaconFlashMoltiplicator) {
    this.defaultBeaconFlashMoltiplicator = defaultBeaconFlashMoltiplicator;
  }

  public int getDiscoveryPort() {
    return discoveryPort;
  }

  public void setDiscoveryPort(int discoveryPort) {
    this.discoveryPort = discoveryPort;
  }

  public String getBroadcastAddress() {
    return broadcastAddress;
  }

  public void setBroadcastAddress(String broadcastAddress) {
    this.broadcastAddress = broadcastAddress;
  }

  public String getStringDiscovery() {
    return stringDiscovery;
  }

  public void setStringDiscovery(String stringDiscovery) {
    this.stringDiscovery = stringDiscovery;
  }

  public static long getDefaulttimeout() {
    return defaultTimeOut;
  }

  public static long getWaitreplyloopwaittime() {
    return waitReplyLoopWaitTime;
  }
}
