package org.ar4k.agent.tunnels.http.beacon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.UnrecoverableKeyException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;

import org.apache.commons.lang.StringUtils;
import org.ar4k.agent.core.Anima;
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
import org.ar4k.agent.tunnels.http.grpc.beacon.StatusValue;
import org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.MethodTarget;
import org.springframework.shell.Shell;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

public class BeaconClient implements Runnable, AutoCloseable {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(BeaconClient.class.toString());
  private static final int INTERVAL_REGISTRATION_TRY = 15000;
  private static final String TMP_BEACON_PATH_DEFAULT = "/tmp/beacon-client-" + UUID.randomUUID().toString();
  public static final CharSequence COMPLETION_CHAR = "?";
  public static final int discoveryPacketMaxSize = 1024;

  private transient long lastRegisterTime = 0;

  private ManagedChannel channel = null;
  private RpcServiceV1BlockingStub blockingStub = null;
  private RpcServiceV1Stub asyncStub = null;
  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  private boolean running = false;

  private Thread process = null;

  private Agent me = null;

  private transient RpcConversation localExecutor;
  private final transient Anima anima;
  private List<RemoteBeaconRpcExecutor> remoteExecutors = new ArrayList<>();
  private int discoveryPort = 0; // se diverso da zero prova la connessione e poi ripiega sul discovery
  private String discoveryFilter = "AR4K";
  private transient DatagramSocket socketDiscovery = null;
  private String reservedUniqueName = null;
  private StatusValue registerStatus = StatusValue.BAD;
  private final int pollingFrequency = 500;

  private String aliasBeaconClientInKeystore = "beacon-client";
  private String hostTarget = null;
  private int port = 0; // se zero esclude la connessione diretta ed eventualmente passa al discovery
  private String certChainFile = TMP_BEACON_PATH_DEFAULT + "-ca.pem";
  private String privateFile = TMP_BEACON_PATH_DEFAULT + ".key";
  private String certFile = TMP_BEACON_PATH_DEFAULT + ".pem";

  public static class Builder {
    private Anima anima = null;
    private RpcConversation rpcConversation = null;
    private String host = null;
    private int port = 0;
    private int discoveryPort = 0;
    private String discoveryFilter = null;
    private String uniqueName = null;
    private String certChainFile = null;
    private String certFile = null;
    private String privateFile = null;
    private String aliasBeaconClientInKeystore = null;
    @SuppressWarnings("unused")
    private String beaconCaChainPem = null;

    public Anima getAnima() {
      return anima;
    }

    public Builder setAnima(Anima anima) {
      this.anima = anima;
      return this;
    }

    public RpcConversation getRpcConversation() {
      return rpcConversation;
    }

    public Builder setRpcConversation(RpcConversation rpcConversation) {
      this.rpcConversation = rpcConversation;
      return this;
    }

    public String getHost() {
      return host;
    }

    public Builder setHost(String host) {
      this.host = host;
      return this;
    }

    public int getPort() {
      return port;
    }

    public Builder setPort(int port) {
      this.port = port;
      return this;
    }

    public int getDiscoveryPort() {
      return discoveryPort;
    }

    public Builder setDiscoveryPort(int discoveryPort) {
      this.discoveryPort = discoveryPort;
      return this;
    }

    public String getDiscoveryFilter() {
      return discoveryFilter;
    }

    public Builder setDiscoveryFilter(String discoveryFilter) {
      this.discoveryFilter = discoveryFilter;
      return this;
    }

    public String getUniqueName() {
      return uniqueName;
    }

    public Builder setUniqueName(String uniqueName) {
      this.uniqueName = uniqueName;
      return this;
    }

    public String getCertChainFile() {
      return certChainFile;
    }

    public Builder setCertChainFile(String certChainFile) {
      this.certChainFile = certChainFile;
      return this;
    }

    public String getCertFile() {
      return certFile;
    }

    public Builder setCertFile(String certFile) {
      this.certFile = certFile;
      return this;
    }

    public String getPrivateFile() {
      return privateFile;
    }

    public Builder setPrivateFile(String privateFile) {
      this.privateFile = privateFile;
      return this;
    }

    public String getAliasBeaconClientInKeystore() {
      return aliasBeaconClientInKeystore;
    }

    public Builder setAliasBeaconClientInKeystore(String aliasBeaconClientInKeystore) {
      this.aliasBeaconClientInKeystore = aliasBeaconClientInKeystore;
      return this;
    }

    public Builder setBeaconCaChainPem(String beaconCaChainPem) {
      this.beaconCaChainPem = beaconCaChainPem;
      return this;
    }

    public BeaconClient build() throws UnrecoverableKeyException {
      return new BeaconClient(anima, rpcConversation, host, port, discoveryPort, discoveryFilter, uniqueName,
          certChainFile, certFile, privateFile, aliasBeaconClientInKeystore);
    }

  }

  private BeaconClient(Anima anima, RpcConversation rpcConversation, String host, int port, int discoveryPort,
      String discoveryFilter, String uniqueName, String certChainFile, String certFile, String privateFile,
      String aliasBeaconClientInKeystore) throws UnrecoverableKeyException {
    this.localExecutor = rpcConversation;
    this.discoveryPort = discoveryPort;
    this.discoveryFilter = discoveryFilter;
    this.anima = anima;
    this.localExecutor = new RpcConversation(Anima.getApplicationContext().getBean(Shell.class));
    this.hostTarget = host;
    this.port = port;
    if (aliasBeaconClientInKeystore != null) {
      this.aliasBeaconClientInKeystore = aliasBeaconClientInKeystore;
    }
    if (certChainFile != null)
      this.certChainFile = certChainFile;
    if (certFile != null)
      this.certFile = certFile;
    if (privateFile != null)
      this.privateFile = privateFile;
    if (uniqueName != null)
      this.reservedUniqueName = uniqueName;
    else
      this.reservedUniqueName = UUID.randomUUID().toString();
    if (anima.getMyIdentityKeystore().listCertificate().contains(this.aliasBeaconClientInKeystore)) {
      logger.info("Certificate for Beacon client is present in keystore");
    } else {
      throw new UnrecoverableKeyException("key " + this.aliasBeaconClientInKeystore + " not found in keystore");
    }
    if (this.port > 0) {
      try {
        startConnection(this.hostTarget, this.port);
      } catch (SSLException e) {
        logger.logException(e);
      }
    }
    runInstance();
  }

  private void startConnection(String host, int port) throws SSLException {
    // TODO SSL facoltativo
    generateCaFile();
    generateCertFile();
    writePrivateKey(aliasBeaconClientInKeystore, anima, privateFile);
    runConnection(NettyChannelBuilder.forAddress(host, port).usePlaintext());// .sslContext(GrpcSslContexts.forClient()
    // .keyManager(new File(certFile), new File(privateFile)).trustManager(new
    // File(certChainFile)).build()));
    // TODO SSL facoltativo
  }

  private static void writePrivateKey(String alias, Anima animaTarget, String privateKey) {
    logger.info("USE KEY FOR CLIENT: " + alias + " target file -> " + privateKey);
    String pk = animaTarget.getMyIdentityKeystore().getPrivateKeyBase64(alias);
    FileWriter writer;
    try {
      writer = new FileWriter(new File(privateKey));
      writer.write("-----BEGIN PRIVATE KEY-----\n");
      writer.write(pk);
      writer.write("\n-----END PRIVATE KEY-----\n");
      writer.close();
    } catch (IOException e) {
      logger.logException(e);
    }

  }

  private void generateCaFile() {
    try {
      FileWriter writer = new FileWriter(new File(certChainFile));
      // if (getCaServer() == null || getCaServer().isEmpty()) {
      String pemTxtClient = anima.getMyIdentityKeystore().getCaPem(aliasBeaconClientInKeystore);
      String pemTxtCa = anima.getMyIdentityKeystore().getCaPem(anima.getMyAliasCertInKeystore());
      writer.write("-----BEGIN CERTIFICATE-----\n");
      writer.write(pemTxtClient);
      writer.write("\n-----END CERTIFICATE-----\n");
      writer.write("-----BEGIN CERTIFICATE-----\n");
      writer.write(pemTxtCa);
      writer.write("\n-----END CERTIFICATE-----\n");
      /*
       * } else { writer.write(getCaServer()); }
       */
      writer.close();
    } catch (IOException e) {
      logger.logException(e);
    }
  }

  private void generateCertFile() {
    try {
      FileWriter writer = new FileWriter(new File(certFile));
      String pemTxt = anima.getMyIdentityKeystore().getCaPem(aliasBeaconClientInKeystore);
      writer.write("-----BEGIN CERTIFICATE-----\n");
      writer.write(pemTxt);
      writer.write("\n-----END CERTIFICATE-----\n");
      writer.close();
    } catch (IOException e) {
      logger.logException(e);
    }
  }

  public void runConnection(ManagedChannelBuilder<?> channelBuilder) {
    channel = channelBuilder.build();
    blockingStub = RpcServiceV1Grpc.newBlockingStub(channel);
    asyncStub = RpcServiceV1Grpc.newStub(channel);
    actionRegister();
  }

  private void actionRegister() {
    if (lastRegisterTime == 0 || (lastRegisterTime + INTERVAL_REGISTRATION_TRY) < (new Date()).getTime()) {
      lastRegisterTime = new Date().getTime();
      try {
        logger.debug("try registration to beacon server.\n-status registration: " + registerStatus
            + "\n-status connection: " + getStateConnection());
        registerStatus = registerToBeacon(reservedUniqueName, getDisplayRequestTxt(), null);
      } catch (Exception e) {
        logger.logException(e);
      }
    }
  }

  private String getDisplayRequestTxt() {
    return Anima.getRegistrationPin();
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

  private StatusValue registerToBeacon(String uniqueName, String displayKey, String csr)
      throws IOException, InterruptedException, ParseException {
    RegisterRequest request;
    StatusValue result = StatusValue.BAD;
    try {
      long timeRequest = new Date().getTime();
      request = RegisterRequest.newBuilder().setJsonHealth(gson.toJson(HardwareHelper.getSystemInfo()))
          .setDisplayKey(displayKey).setName(uniqueName).setTime(Timestamp.newBuilder().setSeconds(timeRequest))
          .build();
      logger.debug("try registration, channel status " + channel.getState(true));
      RegisterReply reply = blockingStub.register(request);
      me = Agent.newBuilder().setAgentUniqueName(reply.getRegisterCode()).build();
      logger.info("connection to beacon ok . I'm " + me.getAgentUniqueName());
      result = reply.getStatusRegistration().getStatus();
    } catch (io.grpc.StatusRuntimeException e) {
      logger.warn("Beacon server connection is " + e.getMessage());
      logger.info(Ar4kLogger.stackTraceToString(e, 6));
      logger.logExceptionDebug(e);
    }
    return result;
  }

  public int getPollingFreq() {
    return pollingFrequency;
  }

  @Override
  public void run() {
    while (running) {
      try {
        // se la registrazione è in attesa di approvazione
        if (getStateConnection().equals(ConnectivityState.READY) && !registerStatus.equals(StatusValue.GOOD)) {
          actionRegister();
        }
        // se sono registrato
        if (me != null && getStateConnection().equals(ConnectivityState.READY)
            && registerStatus.equals(StatusValue.GOOD)) {
          checkPollChannel();
          sendHardwareInfo();
          Thread.sleep(getPollingFreq());
        }
        // se non c'è connessione e discoveryPort è attivo
        if (channel == null && discoveryPort != 0 && !getStateConnection().equals(ConnectivityState.READY)) {
          lookOut();
        }
        // se non c'è connessione e port (server) è attivo
        if (!getStateConnection().equals(ConnectivityState.READY) && channel == null && port != 0 && hostTarget != null
            && !hostTarget.isEmpty()) {
          startConnection(this.hostTarget, this.port);
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
        if (discoveryFilter == null || message.contains(discoveryFilter)) {
          String hostBeacon = packet.getAddress().getHostAddress();
          int portBeacon = Integer.valueOf(message.split(":")[1]);
          logger
              .info("-- Beacon server found on host " + hostBeacon + " port " + String.valueOf(portBeacon + "/TCP --"));
          startConnection(this.hostTarget, this.port);
        }
      }
    }
  }

  private void checkPollChannel() {
    try {
      elaborateRequestFromBus(blockingStub.pollingCmdQueue(me));
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
    try {
      CompletionContext context = new CompletionContext(m.getWordsList(), m.getWordIndex(), m.getPosition());
      List<CompletionProposal> listProposal = localExecutor.complete(context);
      List<String> resultList = new ArrayList<>();
      for (CompletionProposal p : listProposal) {
        resultList.add(p.toString());
      }
      CommandReplyRequest request = CommandReplyRequest.newBuilder().setAgentDestination(m.getCaller())
          .setAgentSender(me).setUniqueIdRequest(m.getUniqueIdRequest()).addAllReplies(resultList).build();
      blockingStub.sendCommandReply(request);
    } catch (Exception a) {
      logger.logException(a);
    }
  }

  private void execCommand(RequestToAgent m) {
    try {
      String reply = localExecutor.elaborateMessage(m.getRequestCommand());
      // System.out.println("generated from client " + reply);
      CommandReplyRequest request = CommandReplyRequest.newBuilder().setAgentDestination(m.getCaller())
          .setAgentSender(me).setUniqueIdRequest(m.getUniqueIdRequest()).addReplies(reply).build();
      blockingStub.sendCommandReply(request);
    } catch (Exception a) {
      logger.logException(a);
    }
  }

  private void notImplemented(RequestToAgent m) {
    try {
      String error = "Type " + m.getType() + " not implemented";
      CommandReplyRequest request = CommandReplyRequest.newBuilder().setAgentDestination(m.getCaller())
          .setAgentSender(me).setUniqueIdRequest(m.getUniqueIdRequest()).addErrors(error).build();
      blockingStub.sendCommandReply(request);
    } catch (Exception a) {
      logger.logException(a);
    }
  }

  private void listCommand(RequestToAgent m) {
    try {
      Map<String, MethodTarget> listCmdMap = localExecutor.listCommands();
      List<String> listReply = new ArrayList<>();
      for (Entry<String, MethodTarget> p : listCmdMap.entrySet()) {
        listReply.add("[" + p.getValue().getGroup() + "] " + p.getKey()
            + (p.getValue().getAvailability().isAvailable() ? " -> " : " [X]-> ") + p.getValue().getHelp());
      }
      CommandReplyRequest request = CommandReplyRequest.newBuilder().setAgentDestination(m.getCaller())
          .setAgentSender(me).setUniqueIdRequest(m.getUniqueIdRequest()).addAllReplies(listReply).build();
      blockingStub.sendCommandReply(request);
    } catch (Exception a) {
      logger.logException(a);
    }
  }

  private void sendHardwareInfo() {
    try {
      HealthRequest hr = HealthRequest.newBuilder().setAgentSender(me)
          .setJsonHardwareInfo(gson.toJson(HardwareHelper.getSystemInfo())).build();
      blockingStub.sendHealth(hr);
    } catch (IOException | InterruptedException | ParseException | io.grpc.StatusRuntimeException e) {
      logger.warn("sendHardwareInfo -> " + e.getMessage());
      logger.logException(e);
    }
  }

  public void sendLoggerLine(String message, String level) {
    try {
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
    } catch (Exception a) {
      logger.logException(a);
    }
  }

  public void sendException(Exception message) {
    try {
      StringBuilder sb = new StringBuilder();
      for (StackTraceElement l : message.getStackTrace()) {
        sb.append(l.toString());
      }
      ExceptionRequest e = ExceptionRequest.newBuilder().setAgentSender(me).setMessageException(message.getMessage())
          .setStackTraceException(sb.toString()).build();
      blockingStub.sendException(e);
    } catch (Exception a) {
      logger.logException(a);
    }
  }

  public String getAgentUniqueName() {
    return me != null ? me.getAgentUniqueName() : null;
  }

  public RpcServiceV1Stub getAsyncStub() {
    return asyncStub;
  }

  public RpcServiceV1BlockingStub getBlockingStub() {
    return blockingStub;
  }

  public ListCommandsReply listCommadsOnAgent(String agentId) {
    try {
      Agent a = Agent.newBuilder().setAgentUniqueName(agentId).build();
      ListCommandsRequest lcr = ListCommandsRequest.newBuilder().setAgentTarget(a).setAgentSender(me).build();
      return blockingStub.listCommands(lcr);
    } catch (Exception a) {
      logger.logException(a);
      return null;
    }
  }

  public ElaborateMessageReply runCommadsOnAgent(String agentId, String command) {
    try {
      Agent a = Agent.newBuilder().setAgentUniqueName(agentId).build();
      ElaborateMessageRequest emr = ElaborateMessageRequest.newBuilder().setAgentTarget(a).setAgentSender(me)
          .setCommandMessage(command).build();
      return blockingStub.elaborateMessage(emr);
    } catch (Exception a) {
      logger.logException(a);
      return null;
    }
  }

  private CompleteCommandReply runCompletitionOnAgent(String agentId, List<String> words, int wordIndex, int position) {
    try {
      Agent a = Agent.newBuilder().setAgentUniqueName(agentId).build();
      CompleteCommandRequest ccr = CompleteCommandRequest.newBuilder().setAgentTarget(a).setAgentSender(me)
          .addAllWords(words).setWordIndex(wordIndex).setPosition(position).build();
      return blockingStub.completeCommand(ccr);
    } catch (Exception a) {
      logger.logException(a);
      return null;
    }
  }

  public CompleteCommandReply runCompletitionOnAgent(String agentUniqueName, String command) {
    try {
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
      return runCompletitionOnAgent(agentUniqueName, clean, word, pos);
    } catch (Exception a) {
      logger.logException(a);
      return null;
    }
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

  public String getReservedUniqueName() {
    return reservedUniqueName;
  }

  public void setReservedUniqueName(String reservedUniqueName) {
    this.reservedUniqueName = reservedUniqueName;
  }

  public StatusValue getRegistrationStatus() {
    return registerStatus;
  }

  @Override
  public void close() {
    asyncStub = null;
    blockingStub = null;
    channel.shutdownNow();
    channel = null;
    logger.info("Client Beacon closed");
  }

}
