package org.ar4k.agent.tunnels.http.beacon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.UnrecoverableKeyException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.IBeaconClient;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.helper.HardwareHelper;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.network.NetworkConfig;
import org.ar4k.agent.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.network.NetworkConfig.NetworkProtocol;
import org.ar4k.agent.network.NetworkTunnel;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.CommandReplyRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReport;
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
import org.ar4k.agent.tunnels.http.grpc.beacon.RequestTunnelMessage;
import org.ar4k.agent.tunnels.http.grpc.beacon.ResponseNetworkChannel;
import org.ar4k.agent.tunnels.http.grpc.beacon.RpcServiceV1Grpc;
import org.ar4k.agent.tunnels.http.grpc.beacon.RpcServiceV1Grpc.RpcServiceV1BlockingStub;
import org.ar4k.agent.tunnels.http.grpc.beacon.RpcServiceV1Grpc.RpcServiceV1Stub;
import org.ar4k.agent.tunnels.http.grpc.beacon.StatusValue;
import org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelServiceV1Grpc;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelServiceV1Grpc.TunnelServiceV1BlockingStub;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelServiceV1Grpc.TunnelServiceV1Stub;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelType;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.MethodTarget;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslProvider;

public class BeaconClient implements AutoCloseable, IBeaconClient {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(BeaconClient.class.toString());
  private static final int INTERVAL_HEALTH = 60000;
  public static final CharSequence COMPLETION_CHAR = "?";
  public static final int discoveryPacketMaxSize = 1024;

  private final String TMP_BEACON_PATH_DEFAULT = Anima.getApplicationContext().getBean(Anima.class)
      .getStarterProperties().getConfPath() + "/beacon-client-" + UUID.randomUUID().toString();
  private transient final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  private final transient Anima anima;

  private StatusBeaconClient status = StatusBeaconClient.IDLE;
  private StatusValue registerStatus = StatusValue.BAD;
  private String reservedUniqueName = null;
  private Agent me = null;

  private transient final ScheduledExecutorService timerExecutor = Executors
      .newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

  private enum StatusBeaconClient {
    RUNNING, CONNECTED, REGISTERED, IDLE, KILLED
  }

  private ManagedChannel channel = null;
  private RpcServiceV1BlockingStub blockingStub = null;
  private TunnelServiceV1BlockingStub blockingStubTunnel;
  private RpcServiceV1Stub asyncStub = null;
  private TunnelServiceV1Stub asyncStubTunnel = null;

  private transient RpcConversation localExecutor = null;
  private transient List<RemoteBeaconRpcExecutor> remoteExecutors = new ArrayList<>();
  private int discoveryPort = 0; // se diverso da zero prova la connessione e poi ripiega sul discovery
  private String discoveryFilter = "AR4K";
  private transient DatagramSocket socketDiscovery = null;
  private final int pollingFrequency = 800;

  private String aliasBeaconClientInKeystore = "beacon-client";
  private String hostTarget = null;
  private int port = 0; // se zero esclude la connessione diretta ed eventualmente passa al discovery
  private String certChainFile = TMP_BEACON_PATH_DEFAULT + "-ca.pem";
  private String privateFile = TMP_BEACON_PATH_DEFAULT + ".key";
  private String certFile = TMP_BEACON_PATH_DEFAULT + ".pem";
  private transient List<BeaconNetworkTunnel> tunnels = new LinkedList<>();
  private String certChain = null;
  private transient String csrRequest = null;

  private void checkProcedure() {
    if (status != StatusBeaconClient.KILLED) {
      // se non c'è connessione e port (server) è attivo
      if (channel == null && port != 0 && hostTarget != null && !hostTarget.isEmpty()) {
        startConnection(this.hostTarget, this.port, true);
      }
      // se non c'è connessione e discoveryPort è attivo
      if (channel == null && discoveryPort != 0) {
        lookAround();
      }
      // se la registrazione è in attesa di approvazione
      if (channel != null
          && (getStateConnection().equals(ConnectivityState.READY)
              || getStateConnection().equals(ConnectivityState.IDLE))
          && (registerStatus.equals(StatusValue.WAIT_HUMAN) || registerStatus.equals(StatusValue.BAD)
              || registerStatus.equals(StatusValue.UNRECOGNIZED) || registerStatus.equals(StatusValue.FAULT))) {
        actionRegister();
      }
      // se sono registrato
      if (isConnectionReady()) {
        checkPollChannel();
      }
    }
  }

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
          certChainFile, certFile, privateFile, aliasBeaconClientInKeystore, beaconCaChainPem);
    }

  }

  private BeaconClient(Anima anima, RpcConversation rpcConversation, String host, int port, int discoveryPort,
      String discoveryFilter, String uniqueName, String certChainFile, String certFile, String privateFile,
      String aliasBeaconClientInKeystore, String certChain) throws UnrecoverableKeyException {
    this.localExecutor = rpcConversation;
    this.discoveryPort = discoveryPort;
    this.discoveryFilter = discoveryFilter;
    this.anima = anima;
    // this.localExecutor = new
    // RpcConversation(Anima.getApplicationContext().getBean(Shell.class));
    this.hostTarget = host;
    this.port = port;
    if (aliasBeaconClientInKeystore != null) {
      this.aliasBeaconClientInKeystore = aliasBeaconClientInKeystore;
    }
    if (certChain != null && !certChain.isEmpty())
      this.certChain = certChain;
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
    // solo ssl
    if (!Boolean.valueOf(anima.getStarterProperties().getBeaconClearText())) {
      if (anima.getMyIdentityKeystore().listCertificate().contains(this.aliasBeaconClientInKeystore)) {
        logger.info("Certificate with alias '" + this.aliasBeaconClientInKeystore
            + "' for Beacon client is present in keystore");
      } else {
        csrRequest = anima.getMyIdentityKeystore()
            .getPKCS10CertificationRequestBase64(anima.getMyAliasCertInKeystore());
        logger.info("Certificate with alias '" + this.aliasBeaconClientInKeystore
            + "' for Beacon client is not present in keystore, use " + anima.getMyAliasCertInKeystore());
      }
    }
    if (this.port > 0) {
      startConnection(this.hostTarget, this.port, true);
    }
    runInstance();
  }

  private synchronized void startConnection(String host, int port, boolean register) {
    logger.debug("Starting Beacon client");
    if (Boolean.valueOf(anima.getStarterProperties().getBeaconClearText())) {
      runConnection(NettyChannelBuilder.forAddress(host, port).usePlaintext(), register);
    } else {
      try {
        generateFilesCert();
        SslContextBuilder sslBuilder = GrpcSslContexts.forClient().keyManager(new File(certFile), new File(privateFile))
            .trustManager(new File(this.certChainFile));
        runConnection(NettyChannelBuilder.forAddress(host, port)
            .sslContext(GrpcSslContexts.configure(sslBuilder, SslProvider.OPENSSL).build()), register);
      } catch (Exception e) {
        logger.logException(e);
      }
    }
  }

  private void generateFilesCert() {
    generateCaFile();
    if (anima.getMyIdentityKeystore().listCertificate().contains(this.aliasBeaconClientInKeystore)) {
      generateCertFile(this.aliasBeaconClientInKeystore);
      writePrivateKey(aliasBeaconClientInKeystore, anima, privateFile);
    } else {
      generateCertFile(anima.getMyAliasCertInKeystore());
      writePrivateKey(anima.getMyAliasCertInKeystore(), anima, privateFile);
    }
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
      for (String cert : certChain.split(",")) {
        writer.write("-----BEGIN CERTIFICATE-----\n");
        writer.write(cert);
        writer.write("\n-----END CERTIFICATE-----\n");
      }
      writer.close();
    } catch (IOException e) {
      logger.logException(e);
    }
  }

  private void generateCertFile(String aliasBeaconClient) {
    try {
      FileWriter writer = new FileWriter(new File(certFile));
      String pemTxt = anima.getMyIdentityKeystore().getCaPem(aliasBeaconClient);
      logger.info("SubjectDN\n"
          + anima.getMyIdentityKeystore().getClientCertificate(aliasBeaconClient).getSubjectDN().getName());
      writer.write("-----BEGIN CERTIFICATE-----\n");
      writer.write(pemTxt);
      writer.write("\n-----END CERTIFICATE-----\n");
      writer.close();
    } catch (IOException e) {
      logger.logException(e);
    }
  }

  public synchronized void runConnection(ManagedChannelBuilder<?> channelBuilder, boolean register) {
    cleanChannel("before new connection");
    channel = channelBuilder.build();
    blockingStub = RpcServiceV1Grpc.newBlockingStub(channel);
    blockingStubTunnel = TunnelServiceV1Grpc.newBlockingStub(channel);
    asyncStub = RpcServiceV1Grpc.newStub(channel);
    asyncStubTunnel = TunnelServiceV1Grpc.newStub(channel);
    if (register)
      actionRegister();
  }

  private synchronized void actionRegister() {
    try {
      logger.info("Try registration to beacon server. BEFORE[" + registerStatus + "/" + getStateConnection() + "]");
      registerStatus = registerToBeacon(reservedUniqueName, getDisplayRequestTxt(), csrRequest);
    } catch (Exception e) {
      logger.logException("in beacon registration", e);
    }
  }

  private String getDisplayRequestTxt() {
    return Anima.getRegistrationPin();
  }

  public void runInstance() {
    logger.info("Client Beacon started, connected state "
        + ((channel != null && channel.getState(true) != null) ? channel.getState(true).toString()
            : "WAITING BEACON CONNECTION"));
    // gestione
    timerExecutor.scheduleAtFixedRate(new Runnable() {
      @Override
      public void run() {
        try {
          checkProcedure();
        } catch (Exception e) {
          logger.logException("in beacon client " + this.toString() + " update", e);
        }
      }
    }, 10, getPollingFreq(), TimeUnit.MILLISECONDS);
    // health
    timerExecutor.scheduleAtFixedRate(new Runnable() {
      @Override
      public void run() {
        try {
          sendHardwareInfo();
        } catch (Exception e) {
          logger.logException("in beacon client " + this.toString() + " health", e);
        }
      }
    }, INTERVAL_HEALTH, INTERVAL_HEALTH, TimeUnit.MILLISECONDS);

  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#getRemoteExecutor(org.ar4k.agent.tunnels.http.grpc.beacon.Agent)
   */
  @Override
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

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#listAgentsConnectedToBeacon()
   */
  @Override
  public List<Agent> listAgentsConnectedToBeacon() {
    Empty empty = Empty.newBuilder().build();
    ListAgentsReply reply = blockingStub.listAgents(empty);
    return reply.getAgentsList();
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#shutdown()
   */
  @Override
  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#getStateConnection()
   */
  @Override
  public ConnectivityState getStateConnection() {
    return channel != null ? channel.getState(true) : ConnectivityState.SHUTDOWN;
  }

  private synchronized StatusValue registerToBeacon(String uniqueName, String displayKey, String csr)
      throws IOException, InterruptedException, ParseException {
    RegisterRequest request;
    StatusValue result = StatusValue.BAD;
    try {
      long timeRequest = new Date().getTime();
      org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest.Builder requestBuilder = RegisterRequest.newBuilder()
          .setJsonHealth(gson.toJson(HardwareHelper.getSystemInfo())).setDisplayKey(displayKey).setName(uniqueName)
          .setTime(Timestamp.newBuilder().setSeconds(timeRequest));
      if (csr != null && !csr.isEmpty()) {
        requestBuilder.setRequestCsr(csr);
        logger.debug("SENDING CSR: " + csr);
      }
      request = requestBuilder.build();
      logger.debug("try registration, channel status " + (channel != null ? channel.getState(true) : "null channel"));
      RegisterReply reply = blockingStub.register(request);
      result = reply.getStatusRegistration().getStatus();
      status = StatusBeaconClient.CONNECTED;
      // se funziona
      if (result.equals(StatusValue.GOOD) && (reply.getCert() == null || reply.getCert().isEmpty())) {
        me = Agent.newBuilder().setAgentUniqueName(reply.getRegisterCode()).build();
        logger.info("connection to beacon ok . I'm " + me.getAgentUniqueName());
        status = StatusBeaconClient.REGISTERED;
      }
      // se nuovo certificato
      if (result.equals(StatusValue.GOOD)
          && !anima.getMyIdentityKeystore().listCertificate().contains(this.aliasBeaconClientInKeystore)
          && reply.getCert() != null && !reply.getCert().isEmpty()) {
        try {
          logger.info("received signed cert and ca chain");
          anima.getMyIdentityKeystore().setClientKeyPair(
              anima.getMyIdentityKeystore().getPrivateKeyBase64(anima.getMyAliasCertInKeystore()), reply.getCert(),
              this.aliasBeaconClientInKeystore);
          certChain = reply.getCa().toStringUtf8();
          status = StatusBeaconClient.IDLE;
          startConnection(this.hostTarget, this.port, false);
          registerToBeacon(reservedUniqueName, getDisplayRequestTxt(), null);
        } catch (Exception e) {
          logger.logException(e);
          result = StatusValue.FAULT;
          status = StatusBeaconClient.IDLE;
        }
      }
    } catch (Exception e) {
      status = StatusBeaconClient.IDLE;
      result = StatusValue.FAULT;
      logger.warn("Beacon server connection is " + e.getMessage());
      logger.info(Ar4kLogger.stackTraceToString(e, 4));
    }
    return result;
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#getPollingFreq()
   */
  @Override
  public int getPollingFreq() {
    return pollingFrequency;
  }

  private boolean isConnectionReady() {
    return me != null && getStateConnection().equals(ConnectivityState.READY)
        && registerStatus.equals(StatusValue.GOOD);
  }

  private synchronized void cleanChannel(String description) {
    logger.info("Reset beacon client because " + description);
    registerStatus = StatusValue.BAD;
    status = StatusBeaconClient.IDLE;
    if (channel != null) {
      channel.shutdownNow();
      channel = null;
    }
    me = null;
    blockingStub = null;
    blockingStubTunnel = null;
    asyncStub = null;
    asyncStubTunnel = null;
  }

  public synchronized void lookAround() {
    if (status == StatusBeaconClient.IDLE) {
      try {
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
              logger.info(
                  "-- Beacon server found on host " + hostBeacon + " port " + String.valueOf(portBeacon + "/TCP --"));
              startConnection(this.hostTarget, this.port, true);
            }
          }
        }
      } catch (Exception e) {
        logger.logException("in beacon discovery", e);
      }
    }
  }

  private void checkPollChannel() {
    if (status == StatusBeaconClient.REGISTERED) {
      try {
        elaborateRequestFromBus(blockingStub.pollingCmdQueue(me));
      } catch (Exception e) {
        logger.logException("GRPC POLL FAILED ", e);
      }
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
        exposePort(m);
        break;
      case LIST_COMMANDS:
        listCommand(m);
        break;
      case OPEN_PROXY_SOCKS:
        notImplemented(m);
        break;
      case SET_CONFIGURATION:
        setConfiguration(m);
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

  private void setConfiguration(RequestToAgent m) {
    try {
      logger.warn("received config from beacon client. RequestId:" + m.getUniqueIdRequest());
      ConfigSeed newConfig = ConfigHelper.fromBase64(m.getRequestCommand());
      CommandReplyRequest reply = CommandReplyRequest.newBuilder().setAgentDestination(m.getCaller())
          .setUniqueIdRequest(m.getUniqueIdRequest()).setBase64Config(m.getRequestCommand()).build();
      blockingStub.sendCommandReply(reply);
      anima.elaborateNewConfig((Ar4kConfig) newConfig);
    } catch (Exception a) {
      logger.logException(a);
    }
  }

  private void exposePort(RequestToAgent m) {
    try {
      logger.info("network port required from beacon " + m.getUniqueIdRequest());
      NetworkConfig config = new BeaconNetworkConfig(m);
      BeaconNetworkTunnel tunnel = new BeaconNetworkTunnel(me, config, false, asyncStubTunnel, m.getUniqueIdRequest());
      tunnels.add(tunnel);
      ResponseNetworkChannel value = ResponseNetworkChannel.newBuilder().setTargeId(tunnel.getTargetId())
          .setUniqueIdRequest(m.getUniqueIdRequest()).build();
      CommandReplyRequest request = CommandReplyRequest.newBuilder().setAgentDestination(m.getCaller())
          .setAgentSender(me).setUniqueIdRequest(m.getUniqueIdRequest()).setTunnelReply(value).build();
      blockingStub.sendCommandReply(request);
      tunnel.init();
    } catch (Exception a) {
      logger.logException(a);
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
    if (status == StatusBeaconClient.REGISTERED) {
      try {
        HealthRequest hr = HealthRequest.newBuilder().setAgentSender(me)
            .setJsonHardwareInfo(gson.toJson(HardwareHelper.getSystemInfo())).build();
        blockingStub.sendHealth(hr);
      } catch (Exception e) {
        logger.logException("error sendHardwareInfo", e);
      }
    }
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#sendLoggerLine(java.lang.String, java.lang.String)
   */
  @Override
  public void sendLoggerLine(String message, String level) {
    if (status == StatusBeaconClient.REGISTERED) {
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
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#sendException(java.lang.Exception)
   */
  @Override
  public void sendException(Exception message) {
    if (status == StatusBeaconClient.REGISTERED) {
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
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#getAgentUniqueName()
   */
  @Override
  public String getAgentUniqueName() {
    return me != null ? me.getAgentUniqueName() : null;
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#getAsyncStub()
   */
  @Override
  public RpcServiceV1Stub getAsyncStub() {
    return asyncStub;
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#getBlockingStub()
   */
  @Override
  public RpcServiceV1BlockingStub getBlockingStub() {
    return blockingStub;
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#listCommadsOnAgent(java.lang.String)
   */
  @Override
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

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#sendConfigToAgent(java.lang.String, org.ar4k.agent.config.Ar4kConfig)
   */
  @Override
  public ConfigReply sendConfigToAgent(String agentId, Ar4kConfig newConfig) {
    try {
      Agent a = Agent.newBuilder().setAgentUniqueName(agentId).build();
      ConfigReport req = ConfigReport.newBuilder().setAgent(a).setBase64Config(ConfigHelper.toBase64(newConfig))
          .build();
      return blockingStub.sendConfigRuntime(req);
    } catch (Exception a) {
      logger.logException(a);
      return null;
    }
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#getTunnels()
   */
  @Override
  public List<BeaconNetworkTunnel> getTunnels() {
    return tunnels;
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#removeTunnel(org.ar4k.agent.tunnels.http.beacon.BeaconNetworkTunnel)
   */
  @Override
  public void removeTunnel(BeaconNetworkTunnel toRemove) {
    tunnels.remove(toRemove);
    try {
      toRemove.close();
    } catch (Exception e) {
      logger.logException(e);
    }
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#getNetworkTunnel(java.lang.String, org.ar4k.agent.network.NetworkConfig)
   */
  @Override
  public NetworkTunnel getNetworkTunnel(String agentId, NetworkConfig config) {
    BeaconNetworkTunnel tunnel = new BeaconNetworkTunnel(me, config, true, asyncStubTunnel, "0");
    try {
      Agent a = Agent.newBuilder().setAgentUniqueName(agentId).build();
      tunnel.setRemoteAgent(a);
      org.ar4k.agent.tunnels.http.grpc.beacon.RequestTunnelMessage.Builder request = RequestTunnelMessage.newBuilder()
          .setAgentSender(me).setAgentDestination(a).setDestIp(config.getClientIp()).setDestPort(config.getClientPort())
          .setSrcPort(config.getServerPort());
      if (config.getNetworkProtocol().equals(NetworkProtocol.TCP)) {
        if (config.getNetworkModeRequested().equals(NetworkMode.CLIENT))
          request.setMode(TunnelType.BYTES_TO_CLIENT_TCP);
        else if (config.getNetworkModeRequested().equals(NetworkMode.SERVER)) {
          request.setMode(TunnelType.SERVER_TO_BYTES_TCP);
        }
      } else if (config.getNetworkProtocol().equals(NetworkProtocol.UDP)) {
        if (config.getNetworkModeRequested().equals(NetworkMode.CLIENT))
          request.setMode(TunnelType.BYTES_TO_CLIENT_UDP);
        else if (config.getNetworkModeRequested().equals(NetworkMode.SERVER)) {
          request.setMode(TunnelType.SERVER_TO_BYTES_UDP);
        }
      }
      logger.debug("request beacon tunnel -> " + request.build().getUniqueIdRequest());
      ResponseNetworkChannel response = blockingStubTunnel.requestTunnel(request.build());
      tunnel.setResponseNetworkChannel(response);
      tunnels.add(tunnel);
      logger.debug("request beacon tunnel id_target from response of other agent -> " + response.getTargeId());
      tunnel.init();
      // logger.info("INIT DONE");
      return tunnel;
    } catch (Exception a) {
      logger.logException(a);
      return null;
    }
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#runCommadsOnAgent(java.lang.String, java.lang.String)
   */
  @Override
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

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#runCompletitionOnAgent(java.lang.String, java.lang.String)
   */
  @Override
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

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#getRemoteExecutors()
   */
  @Override
  public List<RemoteBeaconRpcExecutor> getRemoteExecutors() {
    return remoteExecutors;
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#setRemoteExecutors(java.util.List)
   */
  @Override
  public void setRemoteExecutors(List<RemoteBeaconRpcExecutor> remoteExecutors) {
    this.remoteExecutors = remoteExecutors;
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#getDiscoveryPort()
   */
  @Override
  public int getDiscoveryPort() {
    return discoveryPort;
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#setDiscoveryPort(int)
   */
  @Override
  public void setDiscoveryPort(int discoveryPort) {
    this.discoveryPort = discoveryPort;
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#getDiscoveryFilter()
   */
  @Override
  public String getDiscoveryFilter() {
    return discoveryFilter;
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#setDiscoveryFilter(java.lang.String)
   */
  @Override
  public void setDiscoveryFilter(String discoveryFilter) {
    this.discoveryFilter = discoveryFilter;
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#getReservedUniqueName()
   */
  @Override
  public String getReservedUniqueName() {
    return reservedUniqueName;
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#setReservedUniqueName(java.lang.String)
   */
  @Override
  public void setReservedUniqueName(String reservedUniqueName) {
    this.reservedUniqueName = reservedUniqueName;
  }

  /* (non-Javadoc)
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconClient#getRegistrationStatus()
   */
  @Override
  public StatusValue getRegistrationStatus() {
    return registerStatus;
  }

  @Override
  public void close() {
    status = StatusBeaconClient.KILLED;
    timerExecutor.shutdown();
    cleanChannel("close request");
    if (remoteExecutors != null && remoteExecutors.isEmpty()) {
      for (RemoteBeaconRpcExecutor e : remoteExecutors) {
        try {
          e.close();
        } catch (Exception e1) {
          logger.logException(e1);
        }
      }
      remoteExecutors.clear();
    }
    if (localExecutor != null) {
      try {
        localExecutor.close();
      } catch (Exception e) {
        logger.logException(e);
      }
    }
    if (tunnels != null && !tunnels.isEmpty()) {
      for (BeaconNetworkTunnel t : tunnels) {
        try {
          t.close();
        } catch (Exception e) {
          logger.logException(e);
        }
      }
      tunnels.clear();
    }
    logger.info("Client Beacon closed");
  }

  @Override
  public String toString() {
    return "BeaconClient [me=" + me + ", discoveryPort=" + discoveryPort + ", discoveryFilter=" + discoveryFilter
        + ", reservedUniqueName=" + reservedUniqueName + ", registerStatus=" + registerStatus + ", pollingFrequency="
        + pollingFrequency + ", aliasBeaconClientInKeystore=" + aliasBeaconClientInKeystore + ", hostTarget="
        + hostTarget + ", port=" + port + "]";
  }

}
