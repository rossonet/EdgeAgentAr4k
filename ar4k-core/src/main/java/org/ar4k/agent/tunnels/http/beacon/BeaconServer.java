package org.ar4k.agent.tunnels.http.beacon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.IBeaconServer;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.tunnels.http.beacon.socket.TunnelRunnerBeaconServer;
import org.ar4k.agent.tunnels.http.grpc.beacon.AddressSpace;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.AgentRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.ApproveAgentRequestRequest;
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
import org.ar4k.agent.tunnels.http.grpc.beacon.FlowMessageData;
import org.ar4k.agent.tunnels.http.grpc.beacon.HealthRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.ListAgentsReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ListAgentsRequestReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.PollingRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.RequestToAgent;
import org.ar4k.agent.tunnels.http.grpc.beacon.RequestTunnelMessage;
import org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite;
import org.ar4k.agent.tunnels.http.grpc.beacon.ResponseNetworkChannel;
import org.ar4k.agent.tunnels.http.grpc.beacon.RpcServiceV1Grpc;
import org.ar4k.agent.tunnels.http.grpc.beacon.Status;
import org.ar4k.agent.tunnels.http.grpc.beacon.StatusValue;
import org.ar4k.agent.tunnels.http.grpc.beacon.SubscribeRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelServiceV1Grpc;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelType;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import com.google.protobuf.ByteString;

import io.grpc.Grpc;
import io.grpc.Metadata;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.ClientAuth;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslProvider;
import io.grpc.stub.StreamObserver;

public class BeaconServer implements Runnable, AutoCloseable, IBeaconServer {

  // private static final String TMP_BEACON_PATH_DEFAULT = "/tmp/beacon-server-" +
  // UUID.randomUUID().toString();
  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(BeaconServer.class.toString());
  private static final long defaultTimeOut = 10000L;
  private static final long waitReplyLoopWaitTime = 300L;
  private static final long TIMEOUT_AGENT_POOL = 15 * 60 * 1000;

  private int port = 0;
  private Server server = null;
  private int defaultPollTime = 6000;
  private int defaultBeaconFlashMoltiplicator = 10; // ogni quanti cicli di loop in run emette un flash udp
  private final List<BeaconAgent> agents = new ArrayList<>(); // elenco agenti connessi
  private boolean acceptAllCerts = true; // se true firma in automatico altrimenti gestione della coda di autorizzazione
  private boolean running = true;
  private transient Anima anima = null;

  private transient final List<TunnelRunnerBeaconServer> tunnels = new LinkedList<>();

  private Thread process = null;
  private DatagramSocket socketFlashBeacon = null;

  private List<RegistrationRequest> listAgentRequest = new ArrayList<>();

  // coda risposta clients
  private final Map<String, CommandReplyRequest> repliesQueue = new ConcurrentHashMap<>();
  private int discoveryPort = 0;
  private String broadcastAddress = "255.255.255.255";
  private String stringDiscovery = "AR4K";
  private String certChainFileLastPart = "beacon-ca.pem";
  private String certFileLastPart = "beacon-cert.pem";
  private String privateKeyFileLastPart = "beacon.key";
  private String aliasBeaconServerInKeystore = "beacon-server";
  private String caChainPem = null;

  public static class Builder {
    private Anima anima = null;
    private int port = 0;
    private int discoveryPort = 0;
    private String broadcastAddress = null;
    private boolean acceptCerts = false;
    private String stringDiscovery = null;
    private String certChainFile = null;
    private String certFile = null;
    private String privateKeyFile = null;
    private String aliasBeaconServerInKeystore = null;
    private String caChainPem = null;

    public Anima getAnima() {
      return anima;
    }

    public Builder setAnima(Anima anima) {
      this.anima = anima;
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

    public String getBroadcastAddress() {
      return broadcastAddress;
    }

    public Builder setBroadcastAddress(String broadcastAddress) {
      this.broadcastAddress = broadcastAddress;
      return this;
    }

    public boolean isAcceptCerts() {
      return acceptCerts;
    }

    public Builder setAcceptCerts(boolean acceptCerts) {
      this.acceptCerts = acceptCerts;
      return this;
    }

    public String getStringDiscovery() {
      return stringDiscovery;
    }

    public Builder setStringDiscovery(String stringDiscovery) {
      this.stringDiscovery = stringDiscovery;
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

    public String getPrivateKeyFile() {
      return privateKeyFile;
    }

    public Builder setPrivateKeyFile(String privateKeyFile) {
      this.privateKeyFile = privateKeyFile;
      return this;
    }

    public String getAliasBeaconServerInKeystore() {
      return aliasBeaconServerInKeystore;
    }

    public Builder setAliasBeaconServerInKeystore(String aliasBeaconServerInKeystore) {
      this.aliasBeaconServerInKeystore = aliasBeaconServerInKeystore;
      return this;
    }

    public String getCaChainPem() {
      return caChainPem;
    }

    public Builder setCaChainPem(String caChainPem) {
      this.caChainPem = caChainPem;
      return this;
    }

    public BeaconServer build() throws UnrecoverableKeyException {
      return new BeaconServer(anima, port, discoveryPort, broadcastAddress, acceptCerts, stringDiscovery, certChainFile,
          certFile, privateKeyFile, aliasBeaconServerInKeystore, caChainPem);
    }

  }

  private BeaconServer(Anima animaTarget, int port, int discoveryPort, String broadcastAddress, boolean acceptCerts,
      String stringDiscovery, String certChainFile, String certFile, String privateKeyFile,
      String aliasBeaconServerInKeystore, String caChainPem) throws UnrecoverableKeyException {
    this.anima = animaTarget;
    if (aliasBeaconServerInKeystore != null)
      this.aliasBeaconServerInKeystore = aliasBeaconServerInKeystore;
    if (port > 0)
      this.port = port;
    if (certChainFile != null)
      this.certChainFileLastPart = certChainFile;
    if (certFile != null)
      this.certFileLastPart = certFile;
    if (privateKeyFile != null)
      this.privateKeyFileLastPart = privateKeyFile;
    if (caChainPem != null)
      this.caChainPem = caChainPem;
    this.acceptAllCerts = acceptCerts;
    this.discoveryPort = discoveryPort;
    if (broadcastAddress != null)
      this.broadcastAddress = broadcastAddress;
    if (stringDiscovery != null)
      this.stringDiscovery = stringDiscovery;
    if (Boolean.valueOf(anima.getStarterProperties().getBeaconClearText())) {
      logger.info("Starting beacon server txt mode");
      try {
        ServerBuilder<?> serverBuilder = NettyServerBuilder.forPort(port);
        server = serverBuilder.addService(new RpcService()).addService(new TunnelService())
            .addService(new DataService()).build();
      } catch (Exception e) {
        logger.logException(e);
      }
    } else {
      if (animaTarget != null && animaTarget.getMyIdentityKeystore() != null
          && animaTarget.getMyIdentityKeystore().listCertificate() != null
          && animaTarget.getMyIdentityKeystore().listCertificate().contains(this.aliasBeaconServerInKeystore)) {
        logger.info("Certificate with alias '" + this.aliasBeaconServerInKeystore
            + "' for Beacon server is present in keystore");
      } else {
        throw new UnrecoverableKeyException(
            "key " + this.aliasBeaconServerInKeystore + " not found in keystore [" + animaTarget + "]");
      }
      writePemCa(this.certChainFileLastPart);
      writePemCert(this.aliasBeaconServerInKeystore, animaTarget, this.certFileLastPart);
      writePrivateKey(this.aliasBeaconServerInKeystore, animaTarget, this.privateKeyFileLastPart);
      try {
        logger.info("Starting Beacon server");
        SslContextBuilder sslContextBuild = GrpcSslContexts
            .forServer(new File(this.certFileLastPart), new File(this.privateKeyFileLastPart))
            .trustManager(new File(this.certChainFileLastPart)).clientAuth(ClientAuth.OPTIONAL);
        ServerBuilder<?> serverBuilder = NettyServerBuilder.forPort(port)
            .sslContext(GrpcSslContexts.configure(sslContextBuild, SslProvider.OPENSSL).build());
        server = serverBuilder.intercept(new AuthorizationInterceptor()).addService(new RpcService())
            .addService(new DataService()).addService(new TunnelService()).build();
      } catch (Exception e) {
        logger.logException(e);
      }
    }
  }

  private class AuthorizationInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(final ServerCall<ReqT, RespT> serverCall, final Metadata metadata,
        final ServerCallHandler<ReqT, RespT> serverCallHandler) {
      try {
        SSLSession sslSession = serverCall.getAttributes().get(Grpc.TRANSPORT_ATTR_SSL_SESSION);
        for (Certificate i : sslSession.getPeerCertificates()) {
          logger.trace(" - " + i.toString());
        }
        if (sslSession.isValid()) {
          logger.trace("session ok");
        } else {
          if (serverCall.getMethodDescriptor().getFullMethodName().equals("beacon.RpcServiceV1/Register")) {
            logger.debug("session not ok but register call");
          } else {
            logger.info("session not ok ");
            io.grpc.Status status = io.grpc.Status.PERMISSION_DENIED;
            serverCall.close(status, metadata);
          }
        }
      } catch (SSLPeerUnverifiedException e) {
        if (serverCall.getMethodDescriptor().getFullMethodName().equals("beacon.RpcServiceV1/Register")) {
          logger.info("session not ok, but to " + serverCall.getMethodDescriptor().getFullMethodName());
        } else {
          io.grpc.Status status = io.grpc.Status.PERMISSION_DENIED;
          serverCall.close(status, metadata);
        }
      }
      return serverCallHandler.startCall(serverCall, metadata);
    }
  }

  private static void writePrivateKey(String aliasBeaconServer, Anima animaTarget, String privateKey) {
    String pk = animaTarget.getMyIdentityKeystore().getPrivateKeyBase64(aliasBeaconServer);
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

  private void writePemCa(String certChain) {
    try {
      FileWriter writer = new FileWriter(new File(certChain));
      for (String cert : caChainPem.split(",")) {
        writer.write("-----BEGIN CERTIFICATE-----\n");
        writer.write(cert);
        writer.write("\n-----END CERTIFICATE-----\n");
      }
      writer.close();
    } catch (IOException e) {
      logger.logException(e);
    }
  }

  private void writePemCert(String aliasBeaconServer, Anima animaTarget, String certChain) {
    try {
      FileWriter writer = new FileWriter(new File(certChain));
      String pemTxtBase = animaTarget.getMyIdentityKeystore().getCaPem(aliasBeaconServer);
      writer.write("-----BEGIN CERTIFICATE-----\n");
      writer.write(pemTxtBase);
      writer.write("\n-----END CERTIFICATE-----\n");
      writer.close();
    } catch (IOException e) {
      logger.logException(e);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#start()
   */
  @Override
  public void start() throws IOException {
    server.start();
    logger.info("Server Beacon started, listening on " + port);
    running = true;
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        logger.info("Shutting down Beacon server since JVM is shutting down");
        BeaconServer.this.stop();
      }
    });
    if (process == null) {
      process = new Thread(this);
      process.start();
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#stop()
   */
  @Override
  public void stop() {
    running = false;
    if (tunnels != null && !tunnels.isEmpty()) {
      for (TunnelRunnerBeaconServer t : tunnels) {
        try {
          t.close();
        } catch (Exception e) {
          logger.logException(e);
        }
      }
      tunnels.clear();
    }
    if (agents != null && !agents.isEmpty()) {
      for (BeaconAgent a : agents) {
        try {
          a.close();
        } catch (Exception e) {
          logger.logException(e);
        }
      }
      agents.clear();
    }
    if (server != null) {
      server.shutdown();
      server.shutdownNow();
      server = null;
    }
    if (socketFlashBeacon != null) {
      socketFlashBeacon.close();
      socketFlashBeacon = null;
    }
    if (process != null) {
      process = null;
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#blockUntilShutdown()
   */
  @Override
  public void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  private class DataService extends DataServiceV1Grpc.DataServiceV1ImplBase {

    @Override
    public StreamObserver<RequestWrite> writeSubscription(StreamObserver<FlowMessageData> responseObserver) {
      // TODO Auto-generated method stub
      return super.writeSubscription(responseObserver);
    }

    @Override
    public void polling(PollingRequest request, StreamObserver<FlowMessageData> responseObserver) {
      // TODO DATASERVICE Auto-generated method stub
      super.polling(request, responseObserver);
    }

    @Override
    public void subscription(SubscribeRequest request, StreamObserver<FlowMessageData> responseObserver) {
      // TODO DATASERVICE Auto-generated method stub
      super.subscription(request, responseObserver);
    }

    @Override
    public void write(RequestWrite request, StreamObserver<FlowMessageData> responseObserver) {
      // TODO DATASERVICE Auto-generated method stub
      super.write(request, responseObserver);
    }

    @Override
    public void sendAddressSpace(AddressSpace request, StreamObserver<AddressSpace> responseObserver) {
      // TODO DATASERVICE Auto-generated method stub
      super.sendAddressSpace(request, responseObserver);
    }

    @Override
    public void getRemoteAddressSpace(Agent request, StreamObserver<AddressSpace> responseObserver) {
      // TODO DATASERVICE Auto-generated method stub
      super.getRemoteAddressSpace(request, responseObserver);
    }
  }

  private class TunnelService extends TunnelServiceV1Grpc.TunnelServiceV1ImplBase {

    @Override
    public StreamObserver<TunnelMessage> openNetworkChannel(StreamObserver<TunnelMessage> responseObserver) {
      return new StreamObserver<TunnelMessage>() {

        private TunnelRunnerBeaconServer target = null;
        private Queue<TunnelMessage> onNextQueue = new ConcurrentLinkedQueue<>();

        @Override
        public void onNext(TunnelMessage value) {
          if (value.getPayload() == null || value.getPayload().isEmpty()) {
            logger.trace("Received on BeaconServer " + value);
          } else {
            logger.trace("Received on BeaconServer " + value.getPayload().toStringUtf8());
          }
          onNextQueue.add(value);
          logger.trace("added to queue");
          if (target == null) {
            for (TunnelRunnerBeaconServer t : getTunnels()) {
              logger.trace("searching target from: " + t);
              if (value.getTargeId() == t.getTargeId()) {
                target = t;
                break;
              }
            }
          }
          logger.trace("From BeaconServer send to " + target);
          if (target != null) {
            while (!onNextQueue.isEmpty()) {
              logger.trace("send message from queue");
              target.onNext(onNextQueue.poll(), responseObserver);
            }
          } else {
            logger.info(
                "beacon server bytes in cache waiting " + value.getTargeId() + " connection: " + onNextQueue.size());
          }
        }

        @Override
        public void onError(Throwable t) {
          logger.logException("Error on BeaconServer " + target, t);
          if (target != null) {
            target.onError(t, responseObserver);
          }
        }

        @Override
        public void onCompleted() {
          logger.info("Complete stream on BeaconServer " + target);
          if (target != null) {
            target.onCompleted(responseObserver);
          }
        }

      };
    }

    @Override
    public void requestTunnel(RequestTunnelMessage request, StreamObserver<ResponseNetworkChannel> responseObserver) {
      logger.info("Beacon client require tunnel -> " + request);
      try {
        long idRequest = UUID.randomUUID().getMostSignificantBits();
        TunnelRunnerBeaconServer tunnelRunner = new TunnelRunnerBeaconServer(idRequest);
        tunnelRunner.setActive(true);
        if (request.getMode().equals(TunnelType.SERVER_TO_BYTES_TCP)
            || request.getMode().equals(TunnelType.SERVER_TO_BYTES_UDP)) {
          tunnelRunner.setServerAgent(request.getAgentDestination());
          tunnelRunner.setClientAgent(request.getAgentSender());
        } else if (request.getMode().equals(TunnelType.BYTES_TO_CLIENT_TCP)
            || request.getMode().equals(TunnelType.BYTES_TO_CLIENT_UDP)) {
          tunnelRunner.setServerAgent(request.getAgentSender());
          tunnelRunner.setClientAgent(request.getAgentDestination());
        }
        getTunnels().add(tunnelRunner);
        logger.debug("searching in " + agents.size() + " agents");
        for (BeaconAgent at : agents) {
          if (at.getAgentUniqueName().equals(request.getAgentDestination().getAgentUniqueName())) {
            RequestToAgent rta = RequestToAgent.newBuilder().setCaller(request.getAgentSender())
                .setUniqueIdRequest(String.valueOf(idRequest)).setType(CommandType.EXPOSE_PORT)
                .setTunnelRequest(request).build();
            at.addRequestForAgent(rta);
            logger.debug("Required client tunnel to agent target -> " + rta);
            break;
          }
        }
        CommandReplyRequest cmdReply = waitReply(String.valueOf(idRequest), defaultTimeOut);
        ResponseNetworkChannel channelCreated = cmdReply.getTunnelReply();
        logger.debug("Beacon client tunnel reply -> " + channelCreated);
        responseObserver.onNext(channelCreated);
        responseObserver.onCompleted();
      } catch (Exception e) {
        logger.logException(e);
      }
    }
  }

  private class RpcService extends RpcServiceV1Grpc.RpcServiceV1ImplBase {

    private static final int SIGN_TIME = 3650;

    @Override
    public void sendHealth(HealthRequest request, io.grpc.stub.StreamObserver<Status> responseObserver) {
      try {
        responseObserver.onNext(Status.newBuilder().setStatusValue(StatusValue.GOOD.getNumber()).build());
        responseObserver.onCompleted();
      } catch (Exception a) {
        logger.logException(a);
      }
    }

    @Override
    public void sendCommandReply(CommandReplyRequest request, StreamObserver<Status> responseObserver) {
      try {
        repliesQueue.put(request.getUniqueIdRequest(), request);
        responseObserver.onNext(Status.newBuilder().setStatus(StatusValue.GOOD).build());
        responseObserver.onCompleted();
      } catch (Exception a) {
        logger.logException(a);
      }
    }

    @Override
    public void register(RegisterRequest request, StreamObserver<RegisterReply> responseObserver) {
      try {
        String uniqueClientNameForBeacon = request.getName();
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.Builder replyBuilder = RegisterReply.newBuilder()
            .setCa(ByteString.copyFromUtf8(caChainPem));
        RegisterReply reply = null;
        if (!Boolean.valueOf(anima.getStarterProperties().getBeaconClearText()) && request.getRequestCsr() != null
            && !request.getRequestCsr().isEmpty()) {
          if (acceptAllCerts) {
            reply = replyBuilder.setStatusRegistration(Status.newBuilder().setStatus(StatusValue.GOOD))
                .setRegisterCode(uniqueClientNameForBeacon).setMonitoringFrequency(defaultPollTime)
                .setCert(getFirmedCert(request.getRequestCsr())).build();
          } else {
            // TODO inserire il meccanismo per la coda autorizzativa
            RegistrationRequest newRequest = new RegistrationRequest(request);
            listAgentRequest.add(newRequest);
            reply = replyBuilder.setStatusRegistration(Status.newBuilder().setStatus(StatusValue.WAIT_HUMAN))
                .setRegisterCode(uniqueClientNameForBeacon).setMonitoringFrequency(defaultPollTime).build();
          }
        } else {
          reply = replyBuilder.setStatusRegistration(Status.newBuilder().setStatus(StatusValue.GOOD))
              .setRegisterCode(uniqueClientNameForBeacon).setMonitoringFrequency(defaultPollTime).build();
        }
        agents.add(new BeaconAgent(request, reply));
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
      } catch (Exception a) {
        logger.logException(a);
      }
    }

    private String getFirmedCert(String requestCsr) throws IOException {
      logger.debug("SIGN CSR BASE64 " + requestCsr);
      String requestAlias = "beacon-" + UUID.randomUUID().toString().replace("-", "");
      byte[] data = Base64.getDecoder().decode(requestCsr);
      PKCS10CertificationRequest csrDecoded = new PKCS10CertificationRequest(data);
      logger.debug("SIGN CSR " + csrDecoded.getSubject());
      return anima.getMyIdentityKeystore().signCertificateBase64(csrDecoded, requestAlias, SIGN_TIME,
          anima.getMyAliasCertInKeystore());
    }

    @Override
    public void listAgents(Empty request, StreamObserver<ListAgentsReply> responseObserver) {
      try {
        List<Agent> values = new ArrayList<>();
        for (BeaconAgent r : agents) {
          Agent a = Agent.newBuilder().setAgentUniqueName(r.getAgentUniqueName()).build();
          values.add(a);
        }
        ListAgentsReply reply = ListAgentsReply.newBuilder().addAllAgents(values)
            .setResult(Status.newBuilder().setStatus(StatusValue.GOOD)).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
      } catch (Exception a) {
        logger.logException(a);
      }
    }

    @Override
    public void listAgentsRequestComplete(Empty request, StreamObserver<ListAgentsRequestReply> responseObserver) {
      try {
        List<AgentRequest> values = new ArrayList<>();
        for (RegistrationRequest r : listAgentRequest) {
          org.ar4k.agent.tunnels.http.grpc.beacon.AgentRequest.Builder a = AgentRequest.newBuilder()
              .setIdRequest(r.idRequest).setRequest(r.getRegisterRequest());
          if (r.approved && r.approvedDate != null)
            a.setApproved(r.approvedDate);
          if (r.completed != null)
            a.setRegistrationCompleted(r.completed);
          values.add(a.build());
        }
        ListAgentsRequestReply reply = ListAgentsRequestReply.newBuilder().addAllRequests(values)
            .setResult(Status.newBuilder().setStatus(StatusValue.GOOD)).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
      } catch (Exception a) {
        logger.logException(a);
      }
    }

    @Override
    public void approveAgentRequest(ApproveAgentRequestRequest request, StreamObserver<Status> responseObserver) {
      try {
        org.ar4k.agent.tunnels.http.grpc.beacon.Status.Builder status = Status.newBuilder().setStatus(StatusValue.BAD);
        for (RegistrationRequest r : listAgentRequest) {
          if (r.idRequest.equals(request.getIdRequest())) {
            r.approved = true;
            r.approvedDate = Timestamp.newBuilder().setSeconds(new Date().getTime()).build();
            r.pemApproved = request.getCert();
            r.note = request.getNote();
            status = Status.newBuilder().setStatus(StatusValue.GOOD);
          }
        }
        responseObserver.onNext(status.build());
        responseObserver.onCompleted();
      } catch (Exception a) {
        logger.logException(a);
      }
    }

    @Override
    public void pollingCmdQueue(Agent request, StreamObserver<FlowMessage> responseObserver) {
      try {
        List<RequestToAgent> values = new ArrayList<>();
        for (BeaconAgent at : agents) {
          if (at.getAgentUniqueName().equals(request.getAgentUniqueName())) {
            values.addAll(at.getCommandsToBeExecute());
            break;
          }
        }
        FlowMessage fm = FlowMessage.newBuilder().addAllToDoList(values).build();
        responseObserver.onNext(fm);
        responseObserver.onCompleted();
      } catch (Exception a) {
        logger.logException(a);
      }
    }

    @Override
    public void completeCommand(CompleteCommandRequest request, StreamObserver<CompleteCommandReply> responseObserver) {
      try {
        String idRequest = UUID.randomUUID().toString();
        for (BeaconAgent at : agents) {
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
        for (BeaconAgent at : agents) {
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
        for (BeaconAgent at : agents) {
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

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#getStatus()
   */
  @Override
  public String getStatus() {
    return server != null ? ("running on " + server.getPort()) : null;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#getTunnels()
   */
  @Override
  public List<TunnelRunnerBeaconServer> getTunnels() {
    return tunnels;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * org.ar4k.agent.tunnels.http.beacon.IBeaconServer#waitReply(java.lang.String,
   * long)
   */
  @Override
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

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#isStopped()
   */
  @Override
  public boolean isStopped() {
    return server != null ? (server.isShutdown() || server.isTerminated()) : true;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#getPort()
   */
  @Override
  public int getPort() {
    return port;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#getDefaultPollTime()
   */
  @Override
  public int getDefaultPollTime() {
    return defaultPollTime;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#setDefaultPollTime(int)
   */
  @Override
  public void setDefaultPollTime(int defaultPollTime) {
    this.defaultPollTime = defaultPollTime;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * org.ar4k.agent.tunnels.http.beacon.IBeaconServer#getAgentLabelRegisterReplies
   * ()
   */
  @Override
  public List<BeaconAgent> getAgentLabelRegisterReplies() {
    return agents;
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
        Thread.sleep(defaultPollTime);
      } catch (InterruptedException e) {
        logger.info("in Beacon server loop error " + e.getMessage());
        logger.logException(e);
      }
    }
    logger.info("in Beacon server loop terminated ");
  }

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#sendFlashUdp()
   */
  @Override
  public void sendFlashUdp() {
    try {
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

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#
   * getDefaultBeaconFlashMoltiplicator()
   */
  @Override
  public int getDefaultBeaconFlashMoltiplicator() {
    return defaultBeaconFlashMoltiplicator;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#
   * setDefaultBeaconFlashMoltiplicator(int)
   */
  @Override
  public void setDefaultBeaconFlashMoltiplicator(int defaultBeaconFlashMoltiplicator) {
    this.defaultBeaconFlashMoltiplicator = defaultBeaconFlashMoltiplicator;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#getDiscoveryPort()
   */
  @Override
  public int getDiscoveryPort() {
    return discoveryPort;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#setDiscoveryPort(int)
   */
  @Override
  public void setDiscoveryPort(int discoveryPort) {
    this.discoveryPort = discoveryPort;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#getBroadcastAddress()
   */
  @Override
  public String getBroadcastAddress() {
    return broadcastAddress;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * org.ar4k.agent.tunnels.http.beacon.IBeaconServer#setBroadcastAddress(java.
   * lang.String)
   */
  @Override
  public void setBroadcastAddress(String broadcastAddress) {
    this.broadcastAddress = broadcastAddress;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#getStringDiscovery()
   */
  @Override
  public String getStringDiscovery() {
    return stringDiscovery;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * org.ar4k.agent.tunnels.http.beacon.IBeaconServer#setStringDiscovery(java.lang
   * .String)
   */
  @Override
  public void setStringDiscovery(String stringDiscovery) {
    this.stringDiscovery = stringDiscovery;
  }

  public static long getDefaultTimeout() {
    return defaultTimeOut;
  }

  public static long getWaitreplyloopwaittime() {
    return waitReplyLoopWaitTime;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#isAcceptAllCerts()
   */
  @Override
  public boolean isAcceptAllCerts() {
    return acceptAllCerts;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#getCertChainFile()
   */
  @Override
  public String getCertChainFile() {
    return certFileLastPart;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#getPrivateKeyFile()
   */
  @Override
  public String getPrivateKeyFile() {
    return privateKeyFileLastPart;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#close()
   */
  @Override
  public void close() {
    stop();
    if (agents != null) {
      agents.clear();
    }
    if (tunnels != null) {
      tunnels.clear();
    }
    if (listAgentRequest != null) {
      listAgentRequest.clear();
    }
    anima = null;
  }

  @Override
  public String toString() {
    return "BeaconServer [port=" + port + ", server=" + server + ", defaultPollTime=" + defaultPollTime
        + ", defaultBeaconFlashMoltiplicator=" + defaultBeaconFlashMoltiplicator + ", agentLabelRegisterReplies="
        + agents + ", acceptAllCerts=" + acceptAllCerts + ", running=" + running + ", discoveryPort=" + discoveryPort
        + ", broadcastAddress=" + broadcastAddress + ", stringDiscovery=" + stringDiscovery + ", certChainFile="
        + certChainFileLastPart + ", certFile=" + certFileLastPart + ", privateKeyFile=" + privateKeyFileLastPart
        + ", aliasBeaconServerInKeystore=" + aliasBeaconServerInKeystore + ", caChainPem=" + caChainPem + "]";
  }

  /*
   * (non-Javadoc)
   *
   * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#clearOldData()
   */
  @Override
  public void clearOldData() {
    List<BeaconAgent> toDelete = new ArrayList<>();
    for (BeaconAgent a : agents) {
      if (a.getLastCall().plus(TIMEOUT_AGENT_POOL).isBeforeNow()) {
        try {
          logger.info("agent " + a + " doesn't poll data since " + a.getLastCall().toDateTime());
          toDelete.add(a);
          clearTunnelForAgent(a.getAgentUniqueName());
          a.close();
        } catch (Exception e) {
          logger.logException("deleting agent " + a, e);
        }
      }
    }
    for (BeaconAgent atd : toDelete) {
      logger.info("agent will be removed ->\n" + atd);
      agents.remove(atd);
    }
  }

  private void clearTunnelForAgent(String agentUniqueId) {
    List<TunnelRunnerBeaconServer> tunnelToDelete = new ArrayList<>();
    for (TunnelRunnerBeaconServer t : tunnels) {
      if (t.getClientAgent().getAgentUniqueName().equals(agentUniqueId)
          || t.getServerAgent().getAgentUniqueName().equals(agentUniqueId)) {
        tunnelToDelete.add(t);
      }
    }
    for (TunnelRunnerBeaconServer ttd : tunnelToDelete) {
      logger.info("tunnel will be removed ->\n" + ttd);
      tunnels.remove(ttd);
    }
  }

}
