package org.ar4k.agent.tunnels.http.beacon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
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
import org.ar4k.agent.helper.ConfigHelper;
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
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.MethodTarget;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.protobuf.ByteString;

import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

// TODO: Dopo la registrazione usare i certificati ottenuti per riconnettersi
public class BeaconClient implements Runnable, AutoCloseable {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(BeaconClient.class.toString());
  public static final CharSequence COMPLETION_CHAR = "?";
  public static final int discoveryPacketMaxSize = 1024;
  private long defaultPollingFreq = 500L;

  private String connectionUuid = UUID.randomUUID().toString();

  private String beaconClientAlias = "beacon-" + connectionUuid + "-client";

  private ManagedChannel channel = null;
  private RpcServiceV1BlockingStub blockingStub = null;
  private RpcServiceV1Stub asyncStub = null;
  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  private boolean running = false;

  private Thread process = null;

  private Agent me = null;

  private RpcConversation localExecutor = new RpcConversation();
  private transient Anima anima = null;
  private List<RemoteBeaconRpcExecutor> remoteExecutors = new ArrayList<>();
  private int discoveryPort = 0; // se diverso da zero prova la connessione e poi ripiega sul discovery
  private String discoveryFilter = "AR4K";
  private DatagramSocket socketDiscovery = null;
  private String reservedUniqueName = null;
  private StatusValue registerStatus = StatusValue.BAD;
  private int pollingFrequency = 1000;
  private String certChainFile = "/tmp/beacon-client-" + UUID.randomUUID().toString() + "-ca.pem";
  private String aliasBeaconClientInKeystore = "beacon-client";
  private String aliasBeaconClientRequestCertInKeystore = "beacon-client-crt-request";
  private ByteString caAfterRegistration = null;
  private String caServer = null;
  private String hostTarget = null;
  private String hostTargetEnforced = null;
  private int port = 0; // se zero esclude la connessione diretta ed eventualmente passa al discovery
  private String privateFile = "/tmp/beacon-client-" + UUID.randomUUID().toString() + ".key";
  private String certFile = "/tmp/beacon-client-" + UUID.randomUUID().toString() + ".pem";

  public static class Builder {
    private Anima anima = null;
    private RpcConversation rpcConversation = null;
    private String host = null;
    private String hostTargetEnforced = null;
    private int port = 0;
    private int discoveryPort = 0;
    private String discoveryFilter = null;
    private String uniqueName = null;
    private String certChainFile = null;
    private String certFile = null;
    private String privateFile = null;
    private String aliasBeaconClientInKeystore = null;
    private String aliasBeaconClientRequestCertInKeystore = null;
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

    public String getAliasBeaconClientRequestCertInKeystore() {
      return aliasBeaconClientRequestCertInKeystore;
    }

    public Builder setAliasBeaconClientRequestCertInKeystore(String aliasBeaconClientRequestCertInKeystore) {
      this.aliasBeaconClientRequestCertInKeystore = aliasBeaconClientRequestCertInKeystore;
      return this;
    }

    public String getBeaconCaChainPem() {
      return beaconCaChainPem;
    }

    public Builder setBeaconCaChainPem(String beaconCaChainPem) {
      this.beaconCaChainPem = beaconCaChainPem;
      return this;
    }

    public BeaconClient build() {
      return new BeaconClient(anima, rpcConversation, host, hostTargetEnforced, port, discoveryPort, discoveryFilter,
          uniqueName, certChainFile, certFile, privateFile, aliasBeaconClientInKeystore,
          aliasBeaconClientRequestCertInKeystore, beaconCaChainPem);
    }

    public String getHostTargetEnforced() {
      return hostTargetEnforced;
    }

    public Builder setHostTargetEnforced(String hostTargetEnforced) {
      this.hostTargetEnforced = hostTargetEnforced;
      return this;
    }
  }

  private BeaconClient(Anima anima, RpcConversation rpcConversation, String host, String hostTargetEnforced, int port,
      int discoveryPort, String discoveryFilter, String uniqueName, String certChainFile, String certFile,
      String privateFile, String aliasBeaconClientInKeystore, String aliasBeaconClientRequestCertInKeystore,
      String beaconCaChainPem) {
    this.localExecutor = rpcConversation;
    this.discoveryPort = discoveryPort;
    this.discoveryFilter = discoveryFilter;
    this.anima = anima;
    this.hostTarget = host;
    if (hostTargetEnforced == null && this.hostTarget != null) {
      this.hostTargetEnforced = this.hostTarget;
    } else {
      this.hostTargetEnforced = hostTargetEnforced;
    }
    this.port = port;
    if (aliasBeaconClientInKeystore != null) {
      this.aliasBeaconClientInKeystore = aliasBeaconClientInKeystore;
    }
    if (aliasBeaconClientRequestCertInKeystore != null) {
      this.aliasBeaconClientRequestCertInKeystore = aliasBeaconClientRequestCertInKeystore;
    }
    if (beaconCaChainPem != null)
      this.caServer = beaconCaChainPem;
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
      generateCertificate();
    }
    if (this.port > 0) {
      try {
        // TODO Verificare perché il certificato usa la firma del master (in sign o
        // generando il csr?)
        // writePrivateKey(this.aliasBeaconClientRequestCertInKeystore, anima,
        // this.privateFile);
        startConnection(this.hostTarget, this.port);
        actionRegister();
      } catch (SSLException e) {
        logger.logException(e);
      }
    }
    runInstance();
  }

  private void startConnection(String host, int port) throws SSLException {
    generateCaFile();
    generateCertFile();
    writePrivateKey(anima.getMyAliasCertInKeystore(), anima, privateFile);
    runConnection(NettyChannelBuilder.forAddress(host, port).sslContext(GrpcSslContexts.forClient()
        .keyManager(new File(certFile), new File(privateFile)).trustManager(new File(certChainFile)).build()));
  }

  private void generateCertificate() {
    logger.debug("Create certificate for beacon client");
    anima.getMyIdentityKeystore().create(anima.getAgentUniqueName() + "-bc", ConfigHelper.organization,
        ConfigHelper.unit, ConfigHelper.locality, ConfigHelper.state, ConfigHelper.country, ConfigHelper.uri,
        ConfigHelper.dns, ConfigHelper.ip, aliasBeaconClientRequestCertInKeystore, false);
    PKCS10CertificationRequest csr = anima.getMyIdentityKeystore()
        .getPKCS10CertificationRequest(aliasBeaconClientRequestCertInKeystore);
    anima.getMyIdentityKeystore().signCertificateBase64(csr, aliasBeaconClientInKeystore, 400,
        anima.getMyAliasCertInKeystore());
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
      if (getCaServer() == null || getCaServer().isEmpty()) {
        String pemTxtClient = anima.getMyIdentityKeystore().getCaPem(aliasBeaconClientInKeystore);
        String pemTxtServer = anima.getMyIdentityKeystore().getCaPem("beacon-server");
        String pemTxtCa = anima.getMyIdentityKeystore().getCaPem(anima.getMyAliasCertInKeystore());
        writer.write("-----BEGIN CERTIFICATE-----\n");
        writer.write(pemTxtClient);
        writer.write("\n-----END CERTIFICATE-----\n");
        writer.write("-----BEGIN CERTIFICATE-----\n");
        writer.write(pemTxtServer);
        writer.write("\n-----END CERTIFICATE-----\n");
        writer.write("-----BEGIN CERTIFICATE-----\n");
        writer.write(pemTxtCa);
        writer.write("\n-----END CERTIFICATE-----\n");
      } else {
        writer.write(getCaServer());
      }
      writer.close();
    } catch (IOException e) {
      logger.logException(e);
    }
  }

  private void generateCertFile() {
    try {
      FileWriter writer = new FileWriter(new File(certFile));
      String pemTxt = anima.getMyIdentityKeystore()
          .getCaPem(me != null ? beaconClientAlias : aliasBeaconClientInKeystore);
      // String pemTxtCa =
      // anima.getMyIdentityKeystore().getCaPem(anima.getMyAliasCertInKeystore());
      writer.write("-----BEGIN CERTIFICATE-----\n");
      writer.write(pemTxt);
      writer.write("\n-----END CERTIFICATE-----\n");
      // writer.write("-----BEGIN CERTIFICATE-----\n");
      // writer.write(pemTxtCa);
      // writer.write("\n-----END CERTIFICATE-----\n");
      writer.close();
    } catch (IOException e) {
      logger.logException(e);
    }
  }

  public void runConnection(ManagedChannelBuilder<?> channelBuilder) {
    channel = channelBuilder.build();
    blockingStub = RpcServiceV1Grpc.newBlockingStub(channel);
    asyncStub = RpcServiceV1Grpc.newStub(channel);
  }

  private void actionRegister() {
    try {
      registerStatus = registerToBeacon(reservedUniqueName, getDisplayRequestTxt(), getCsrRequestBase64());
    } catch (Exception e) {
      logger.logException(e);
    }
  }

  private String getCsrRequestBase64() {
    logger.info("Certificate for registration to the beacon server (SOURCE OF CSR): " + anima.getMyIdentityKeystore()
        .getClientCertificate(aliasBeaconClientRequestCertInKeystore).getSubjectX500Principal().toString() + " - alias "
        + aliasBeaconClientRequestCertInKeystore);
    return anima.getMyIdentityKeystore().getPKCS10CertificationRequestBase64(aliasBeaconClientRequestCertInKeystore);
  }

  private String getDisplayRequestTxt() {
    return Anima.getRegistrationPin();
  }

  private void registerCertificateFromBeacon(String cert, ByteString ca) {
    try {
      anima.getMyIdentityKeystore().setClientKeyPair(
          anima.getMyIdentityKeystore().getPrivateKeyBase64(anima.getMyAliasCertInKeystore()), cert, beaconClientAlias);
      logger.debug("Certificate for future access to Beacon server: "
          + anima.getMyIdentityKeystore().getClientCertificate(beaconClientAlias).getSubjectX500Principal().toString()
          + " - alias " + beaconClientAlias);
      // anima.getMyIdentityKeystore().setCa(byteString, beaconServerCaAlias);
      logger.info("Certificate for CA " + ca.toString(Charset.defaultCharset()));
      caAfterRegistration = ca;
    } catch (NoSuchAlgorithmException e) {
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

  private StatusValue registerToBeacon(String uniqueName, String displayKey, String csr)
      throws IOException, InterruptedException, ParseException {
    RegisterRequest request;
    StatusValue result = StatusValue.BAD;
    try {
      long timeRequest = new Date().getTime();
      request = RegisterRequest.newBuilder().setJsonHealth(gson.toJson(HardwareHelper.getSystemInfo()))
          .setDisplayKey(displayKey).setRequestCsr(csr).setName(uniqueName)
          .setTime(Timestamp.newBuilder().setSeconds(timeRequest)).build();
      logger.debug("try registration, channel status " + channel.getState(true));
      RegisterReply reply = blockingStub.register(request);
      if (reply.getStatusRegistration().getStatus().equals(StatusValue.GOOD)) {
        if (reply.getCa() != null && reply.getCert() != null) {
          registerCertificateFromBeacon(reply.getCert(), reply.getCa());
        }
        if (caAfterRegistration != null) {
          me = Agent.newBuilder().setAgentUniqueName(reply.getRegisterCode()).build();
        }
      }
      result = reply.getStatusRegistration().getStatus();
    } catch (io.grpc.StatusRuntimeException e) {
      logger.warn("Beacon server is " + e.getMessage());
      logger.logException(e);
    }
    if (result.equals(StatusValue.GOOD)) {
      asyncStub = null;
      blockingStub = null;
      // channel.shutdown().awaitTermination(15, TimeUnit.SECONDS);
      channel.shutdownNow();
      channel = null;
      logger.warn("Client Beacon registered. reconnect to beacon with cert");
      startConnection(hostTargetEnforced, port);
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
        if (getStateConnection().equals(ConnectivityState.READY) && registerStatus.equals(StatusValue.WAIT_HUMAN)) {
          actionRegister();
        }
        // se sono registrato
        if (me != null && getStateConnection().equals(ConnectivityState.READY)
            && registerStatus.equals(StatusValue.GOOD)) {
          checkPollChannel();
          sendHardwareInfo();
          Thread.sleep(getPollingFreq());
        } else {
          Thread.sleep(defaultPollingFreq);
        }
        // se non c'è connessione e discoveryPort è attivo
        if (channel == null && discoveryPort != 0 && !getStateConnection().equals(ConnectivityState.READY)) {
          lookOut();
        }
        // se non c'è connessione e port (server) è attivo
        if (channel == null && port != 0 && !getStateConnection().equals(ConnectivityState.READY)) {
          startConnection(this.hostTarget, this.port);
          actionRegister();
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
          // runConnection(ManagedChannelBuilder.forAddress(hostBeacon,
          // portBeacon).usePlaintext());
          startConnection(this.hostTarget, this.port);
          actionRegister();
          if (!registerStatus.equals(StatusValue.BAD)) {
            // se la registrazione va a buon fine chiude il socket
            socketDiscovery.close();
            socketDiscovery = null;
          }
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
          .setJsonHardwareInfo(gson.toJson(HardwareHelper.getSystemInfo())).build();
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

  public String getReservedUniqueName() {
    return reservedUniqueName;
  }

  public void setReservedUniqueName(String reservedUniqueName) {
    this.reservedUniqueName = reservedUniqueName;
  }

  public StatusValue getRegistrationStatus() {
    return registerStatus;
  }

  public String getCaAfterRegistration() {
    return caAfterRegistration.toString(Charsets.UTF_8);
  }

  public String getCaServer() {
    return caAfterRegistration != null ? caAfterRegistration.toString(Charsets.UTF_8) : caServer;
  }

  public void setCaServer(String caServer) {
    this.caServer = caServer;
  }

  @Override
  public void close() {
    // TODO Auto-generated method stub

  }

}
