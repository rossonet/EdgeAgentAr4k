package org.ar4k.agent.tunnels.http.grpc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.helper.HardwareHelper;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.CommandReplyRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ElaborateMessageRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.Empty;
import org.ar4k.agent.tunnels.http.grpc.beacon.ExceptionRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessage;
import org.ar4k.agent.tunnels.http.grpc.beacon.HealthRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.ListAgentsReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.LogSeverity;
import org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.RequestToAgent;
import org.ar4k.agent.tunnels.http.grpc.beacon.RpcServiceV1Grpc;
import org.ar4k.agent.tunnels.http.grpc.beacon.RpcServiceV1Grpc.RpcServiceV1BlockingStub;
import org.ar4k.agent.tunnels.http.grpc.beacon.RpcServiceV1Grpc.RpcServiceV1Stub;
import org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.MethodTarget;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BeaconClient implements Runnable {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(BeaconClient.class.toString());
  public static final CharSequence COMPLETION_CHAR = "?";
  public static final int discoveryPacketMaxSize = 1024;
  private long defaultPollingFreq = 500L;

  private ManagedChannel channel = null;
  private RpcServiceV1BlockingStub blockingStub = null;
  private RpcServiceV1Stub asyncStub = null;
  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
  private final String secretKey = UUID.randomUUID().toString();
  // private String registerCode = null;

  private boolean running = false;

  private Thread process = null;

  private Agent me = null;

  private RpcConversation localExecutor = new RpcConversation();

  private List<RemoteBeaconRpcExecutor> remoteExecutors = new ArrayList<>();
  private int discoveryPort = 0;
  private String discoveryFilter = "AR4K";
  private DatagramSocket socketDiscovery = null;
  private String reservedUniqueName = null;
  private String registerStatus = "BAD";

  public BeaconClient(RpcConversation rpcConversation, String host, int port, int discoveryPort, String discoveryFilter,
      String uniqueName) {
    this.localExecutor = rpcConversation;
    this.discoveryPort = discoveryPort;
    this.discoveryFilter = discoveryFilter;
    if (uniqueName != null)
      this.reservedUniqueName = uniqueName;
    else
      this.reservedUniqueName = UUID.randomUUID().toString();
    if (port != 0) {
      runConnection(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }
    runInstance();
  }

  public void runConnection(ManagedChannelBuilder<?> channelBuilder) {
    channel = channelBuilder.build();
    blockingStub = RpcServiceV1Grpc.newBlockingStub(channel);
    asyncStub = RpcServiceV1Grpc.newStub(channel);
    try {
      registerStatus = registerToBeacon(reservedUniqueName);
    } catch (IOException | InterruptedException | ParseException | NullPointerException e) {
      logger.logException(e);
    }
  }

  public void runInstance() {
    logger.info("Client Beacon started, connected state "
        + ((channel != null && channel.getState(true) != null) ? channel.getState(true).toString()
            : " WAITING BEACON FLASH"));
    running = true;
    if (process == null) {
      process = new Thread(this);
      process.start();
    }
  }

  public RemoteBeaconRpcExecutor getRemoteExecutor(Agent agent) {
    RemoteBeaconRpcExecutor result = null;
    for (RemoteBeaconRpcExecutor f : remoteExecutors) {
      if (f.getRemoteHomunculus().getRemoteAgent().equals(agent)) {
        result = f;
        break;
      }
    }
    if (result == null) {
      RemoteBeaconAgentHomunculus remoteHomunculus = new RemoteBeaconAgentHomunculus();
      remoteHomunculus.setRemoteAgent(agent);
      result = new RemoteBeaconRpcExecutor(me, remoteHomunculus, blockingStub);
      remoteExecutors.add(result);
    }
    return result;
  }

  public List<Agent> listAgentsConnectedToBeacon() {
    Empty empty = Empty.newBuilder().build();
    ListAgentsReply reply = blockingStub.listAgents(empty);
    return reply.getAgentsList();
  }

  public void shutdown() throws InterruptedException {
    running = false;
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public ConnectivityState getStateConnection() {
    return channel != null ? channel.getState(true) : ConnectivityState.TRANSIENT_FAILURE;
  }

  private String registerToBeacon(String uniqueName) throws IOException, InterruptedException, ParseException {
    RegisterRequest request;
    String result = "BAD";
    try {
      long timeRequest = new Date().getTime();
      request = RegisterRequest.newBuilder().setJsonHealth(gson.toJson(HardwareHelper.getSystemInfo()))
          .setSecretKey(secretKey).setName(uniqueName).setTime(Timestamp.newBuilder().setSeconds(timeRequest)).build();
      RegisterReply reply = blockingStub.register(request);
      me = Agent.newBuilder().setAgentUniqueName(reply.getRegisterCode())
          .setPollingFrequency(reply.getMonitoringFrequency()).setTimestampRegistration(timeRequest).build();
      result = reply.getResult().getStatus().name();
    } catch (io.grpc.StatusRuntimeException e) {
      logger.warn("Beacon server is " + e.getMessage());
      logger.logException(e);
    }
    return result;
  }

  public int getPollingFreq() {
    return me.getPollingFrequency();
  }

  @Override
  public void run() {
    while (running) {
      try {
        if (me != null && getStateConnection().equals(ConnectivityState.READY)) {
          checkPollChannel();
          sendHardwareInfo();
          Thread.sleep((long) getPollingFreq());
        } else {
          Thread.sleep(defaultPollingFreq);
        }
        if (channel == null && discoveryFilter != null && discoveryPort != 0
            && !getStateConnection().equals(ConnectivityState.READY)) {
          lookOut();
        }
      } catch (Exception e) {
        logger.info("in Beacon client loop " + e.getMessage());
        logger.logException(e);
      }
    }
  }

  public void lookOut()
      throws SocketException, UnknownHostException, IOException, InterruptedException, ParseException {
    if (socketDiscovery == null) {
      socketDiscovery = new DatagramSocket(discoveryPort, InetAddress.getByName("0.0.0.0"));
      socketDiscovery.setBroadcast(true);
    } else {
      byte[] recvBuf = new byte[discoveryPacketMaxSize];
      DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
      socketDiscovery.receive(packet);
      if (packet.getData().length > 0) {
        String message = new String(packet.getData()).trim();
        logger.info("DISCOVERY FLASH: " + message);
        if (message.contains(discoveryFilter)) {
          String hostBeacon = packet.getAddress().getHostAddress();
          int portBeacon = Integer.valueOf(message.split(":")[1]);
          logger
              .info("-- Beacon server found on host " + hostBeacon + " port " + String.valueOf(portBeacon + "/TCP --"));
          runConnection(ManagedChannelBuilder.forAddress(hostBeacon, portBeacon).usePlaintext());
          if (!registerToBeacon(reservedUniqueName).equals("BAD")) {
            socketDiscovery.close();
            socketDiscovery = null;
          }
        }
      }
    }
  }

  private void checkPollChannel() {
    try {
      elaborateRequestFromBus(blockingStub.polling(me));
    } catch (io.grpc.StatusRuntimeException e) {
      logger.debug("GRPC POLL FAILED " + e.getMessage());
      logger.logException(e);
    }
  }

  private void elaborateRequestFromBus(FlowMessage messages) {
    for (RequestToAgent m : messages.getToDoListList()) {
      switch (m.getType()) {
      case COMPLETE_COMMAND:
        completeCommand(m);
        break;
      case ELABORATE_MESSAGE_COMMAND:
        execCommand(m);
        break;
      case EXPOSE_PORT:
        notImplemented(m);
        break;
      case LIST_COMMANDS:
        listCommand(m);
        break;
      case OPEN_PROXY_SOCKS:
        notImplemented(m);
        break;
      case UNRECOGNIZED:
        notImplemented(m);
        break;
      default:
        notImplemented(m);
        break;
      }
    }
  }

  private void completeCommand(RequestToAgent m) {
    // System.out.println(
    // "received from client w: " + m.getWordsList() + "\nindex:" + m.getWordIndex()
    // + "\npos:" + m.getPosition());
    CompletionContext context = new CompletionContext(m.getWordsList(), m.getWordIndex(), m.getPosition());
    List<CompletionProposal> listProposal = localExecutor.complete(context);
    List<String> resultList = new ArrayList<>();
    for (CompletionProposal p : listProposal) {
      resultList.add(p.toString());
    }
    CommandReplyRequest request = CommandReplyRequest.newBuilder().setAgentDestination(m.getCaller()).setAgentSender(me)
        .setUniqueIdRequest(m.getUniqueIdRequest()).addAllReplies(resultList).build();
    blockingStub.sendCommandReply(request);
  }

  private void execCommand(RequestToAgent m) {
    String reply = localExecutor.elaborateMessage(m.getRequestCommand());
    // System.out.println("generated from client " + reply);
    CommandReplyRequest request = CommandReplyRequest.newBuilder().setAgentDestination(m.getCaller()).setAgentSender(me)
        .setUniqueIdRequest(m.getUniqueIdRequest()).addReplies(reply).build();
    blockingStub.sendCommandReply(request);
  }

  private void notImplemented(RequestToAgent m) {
    String error = "Type " + m.getType() + " not implemented";
    CommandReplyRequest request = CommandReplyRequest.newBuilder().setAgentDestination(m.getCaller()).setAgentSender(me)
        .setUniqueIdRequest(m.getUniqueIdRequest()).addErrors(error).build();
    blockingStub.sendCommandReply(request);
  }

  private void listCommand(RequestToAgent m) {
    Map<String, MethodTarget> listCmdMap = localExecutor.listCommands();
    List<String> listReply = new ArrayList<>();
    for (Entry<String, MethodTarget> p : listCmdMap.entrySet()) {
      listReply.add("[" + p.getValue().getGroup() + "] " + p.getKey()
          + (p.getValue().getAvailability().isAvailable() ? " -> " : " [X]-> ") + p.getValue().getHelp());
    }
    CommandReplyRequest request = CommandReplyRequest.newBuilder().setAgentDestination(m.getCaller()).setAgentSender(me)
        .setUniqueIdRequest(m.getUniqueIdRequest()).addAllReplies(listReply).build();
    blockingStub.sendCommandReply(request);
  }

  private void sendHardwareInfo() {
    try {
      HealthRequest hr = HealthRequest.newBuilder().setAgentSender(me)
          .setHardwareInfo(gson.toJson(HardwareHelper.getSystemInfo())).build();
      blockingStub.sendHealth(hr);
    } catch (IOException | InterruptedException | ParseException | io.grpc.StatusRuntimeException e) {
      logger.warn("sendHardwareInfo -> " + e.getMessage());
      logger.logException(e);
    }
  }

  public void sendLoggerLine(String message, String level) {
    LogSeverity logSeverity;
    switch (level) {
    case "DEFAULT":
      logSeverity = LogSeverity.DEFAULT;
      break;
    case "DEBUG":
      logSeverity = LogSeverity.DEBUG;
      break;
    case "INFO":
      logSeverity = LogSeverity.INFO;
      break;
    case "NOTICE":
      logSeverity = LogSeverity.NOTICE;
      break;
    case "WARNING":
      logSeverity = LogSeverity.WARNING;
      break;
    case "ERROR":
      logSeverity = LogSeverity.ERROR;
      break;
    case "CRITICAL":
      logSeverity = LogSeverity.CRITICAL;
      break;
    case "ALERT":
      logSeverity = LogSeverity.ALERT;
      break;
    case "EMERGENCY":
      logSeverity = LogSeverity.EMERGENCY;
      break;
    default:
      logSeverity = LogSeverity.DEFAULT;
      break;
    }
    LogRequest lr = LogRequest.newBuilder().setSeverity(logSeverity).setAgentSender(me).setLogLine(message).build();
    blockingStub.sendLog(lr);
  }

  public void sendException(Exception message) {
    StringBuilder sb = new StringBuilder();
    for (StackTraceElement l : message.getStackTrace()) {
      sb.append(l.toString());
    }
    ExceptionRequest e = ExceptionRequest.newBuilder().setAgentSender(me).setMessageException(message.getMessage())
        .setStackTraceException(sb.toString()).build();
    blockingStub.sendException(e);
  }

  public String getAgentUniqueName() {
    return me.getAgentUniqueName();
  }

  public RpcServiceV1Stub getAsyncStub() {
    return asyncStub;
  }

  public RpcServiceV1BlockingStub getBlockingStub() {
    return blockingStub;
  }

  public ListCommandsReply listCommadsOnAgent(String agentId) {
    Agent a = Agent.newBuilder().setAgentUniqueName(agentId).build();
    ListCommandsRequest lcr = ListCommandsRequest.newBuilder().setAgentTarget(a).setAgentSender(me).build();
    return blockingStub.listCommands(lcr);
  }

  public ElaborateMessageReply runCommadsOnAgent(String agentId, String command) {
    Agent a = Agent.newBuilder().setAgentUniqueName(agentId).build();
    ElaborateMessageRequest emr = ElaborateMessageRequest.newBuilder().setAgentTarget(a).setAgentSender(me)
        .setCommandMessage(command).build();
    return blockingStub.elaborateMessage(emr);
  }

  private CompleteCommandReply runCompletitionOnAgent(String agentId, List<String> words, int wordIndex, int position) {
    Agent a = Agent.newBuilder().setAgentUniqueName(agentId).build();
    CompleteCommandRequest ccr = CompleteCommandRequest.newBuilder().setAgentTarget(a).setAgentSender(me)
        .addAllWords(words).setWordIndex(wordIndex).setPosition(position).build();
    return blockingStub.completeCommand(ccr);
  }

  public CompleteCommandReply runCompletitionOnAgent(String agentUniqueName, String command) {
    List<String> m = Arrays.asList(StringUtils.split(command));
    List<String> clean = new ArrayList<>(m.size());
    int pos = 0;
    int word = 0;
    int counter = 0;
    for (String p : m) {
      if (p.contains(COMPLETION_CHAR)) {
        word = counter;
        pos = p.indexOf(COMPLETION_CHAR.toString());
        if (!p.equals(COMPLETION_CHAR.toString())) {
          // System.out.println("add " + p.replace(COMPLETION_CHAR, ""));
          clean.add(p.replace(COMPLETION_CHAR, ""));
        } else {
          // System.out.println("add " + p);
          clean.add(p);
        }
      } else {
        clean.add(p);
      }
      counter++;
    }
    // System.out.println("generated from shell interface w: " + clean + "\nindex:"
    // + word + "\npos:" + pos);
    return runCompletitionOnAgent(agentUniqueName, clean, word, pos);
  }

  public long getDefaultPollingFreq() {
    return defaultPollingFreq;
  }

  public void setDefaultPollingFreq(long defaultPollingFreq) {
    this.defaultPollingFreq = defaultPollingFreq;
  }

  public List<RemoteBeaconRpcExecutor> getRemoteExecutors() {
    return remoteExecutors;
  }

  public void setRemoteExecutors(List<RemoteBeaconRpcExecutor> remoteExecutors) {
    this.remoteExecutors = remoteExecutors;
  }

  public int getDiscoveryPort() {
    return discoveryPort;
  }

  public void setDiscoveryPort(int discoveryPort) {
    this.discoveryPort = discoveryPort;
  }

  public String getDiscoveryFilter() {
    return discoveryFilter;
  }

  public void setDiscoveryFilter(String discoveryFilter) {
    this.discoveryFilter = discoveryFilter;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public String getReservedUniqueName() {
    return reservedUniqueName;
  }

  public void setReservedUniqueName(String reservedUniqueName) {
    this.reservedUniqueName = reservedUniqueName;
  }

  public String getRegistrationStatus() {
    return registerStatus;
  }

}
