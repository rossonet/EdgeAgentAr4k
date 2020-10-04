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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.interfaces.ConfigSeed;
import org.ar4k.agent.core.interfaces.IBeaconServer;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http.beacon.socket.server.BeaconServerNetworkHub;
import org.ar4k.agent.tunnels.http.beacon.socket.server.TunnelRunnerBeaconServer;
import org.ar4k.agent.tunnels.http.grpc.beacon.AddressSpace;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.AgentRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.ApproveAgentRequestRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.Command;
import org.ar4k.agent.tunnels.http.grpc.beacon.CommandReplyRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.CommandType;
import org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.CompleteCommandRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.ConfigReport;
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
import org.ar4k.agent.tunnels.http.grpc.beacon.ListStringReply;
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
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.json.JSONObject;

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
import io.grpc.netty.shaded.io.netty.channel.ChannelOption;
import io.grpc.netty.shaded.io.netty.handler.ssl.ClientAuth;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslProvider;
import io.grpc.stub.StreamObserver;

public class BeaconServer implements Runnable, AutoCloseable, IBeaconServer {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconServer.class.toString());

	private static final int SIGN_TIME = 3650;
	private static final long defaultTimeOut = 10000L;
	private static final long waitReplyLoopWaitTime = 300L;
	private static final long TIMEOUT_AGENT_POOL = 15 * 60 * 1000;

	private int port = 0;
	private Server server = null;
	private int defaultPollTime = 6000;
	private int defaultBeaconFlashMoltiplicator = 10; // ogni quanti cicli di loop in run emette un flash udp
	private final List<BeaconAgent> agents = new ArrayList<>(); // elenco agenti connessi
	private boolean acceptAllCerts = true; // se true firma in automatico altrimenti gestione della coda di
											// autorizzazione
	private boolean running = true;
	private transient Homunculus homunculus = null;

	private final List<TunnelRunnerBeaconServer> tunnels = new LinkedList<>();

	private Thread process = null;
	private DatagramSocket socketFlashBeacon = null;

	private final List<RegistrationRequest> listAgentRequest = new ArrayList<>();

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
	private String filterActiveCommand = null;
	private String filterBlackListCertRegister = null;
	private Pattern filterBlackListCertRegisterPattern = null;
	private Pattern filterActiveCommandPattern = null;
	private String markThread;

	public static class Builder {
		private Homunculus homunculus = null;
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
		private String filterActiveCommand = null;
		private String filterBlackListCertRegister = null;

		public Homunculus getHomunculus() {
			return homunculus;
		}

		public Builder setHomunculus(Homunculus homunculus) {
			this.homunculus = homunculus;
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

		public String getFilterActiveCommand() {
			return filterActiveCommand;
		}

		public Builder setFilterActiveCommand(String filterActiveCommand) {
			this.filterActiveCommand = filterActiveCommand;
			return this;
		}

		public String getFilterBlackListCertRegister() {
			return filterBlackListCertRegister;
		}

		public Builder setFilterBlackListCertRegister(String filterBlackListCertRegister) {
			this.filterBlackListCertRegister = filterBlackListCertRegister;
			return this;
		}

		public BeaconServer build() throws UnrecoverableKeyException {
			return new BeaconServer(homunculus, port, discoveryPort, broadcastAddress, acceptCerts, stringDiscovery,
					certChainFile, certFile, privateKeyFile, aliasBeaconServerInKeystore, caChainPem,
					filterActiveCommand, filterBlackListCertRegister);
		}

	}

	private BeaconServer(Homunculus homunculusTarget, int port, int discoveryPort, String broadcastAddress,
			boolean acceptCerts, String stringDiscovery, String certChainFile, String certFile, String privateKeyFile,
			String aliasBeaconServerInKeystore, String caChainPem, String filterActiveCommand,
			String filterBlackListCertRegister) throws UnrecoverableKeyException {
		this.homunculus = homunculusTarget;
		if (aliasBeaconServerInKeystore != null && !aliasBeaconServerInKeystore.isEmpty())
			this.aliasBeaconServerInKeystore = aliasBeaconServerInKeystore;
		this.port = port;
		if (certChainFile != null && !certChainFile.isEmpty())
			this.certChainFileLastPart = certChainFile;
		if (certFile != null && !certFile.isEmpty())
			this.certFileLastPart = certFile;
		if (privateKeyFile != null && !privateKeyFile.isEmpty())
			this.privateKeyFileLastPart = privateKeyFile;
		if (caChainPem != null && !caChainPem.isEmpty())
			this.caChainPem = caChainPem;
		this.acceptAllCerts = acceptCerts;
		this.discoveryPort = discoveryPort;
		if (broadcastAddress != null && !broadcastAddress.isEmpty())
			this.broadcastAddress = broadcastAddress;
		if (stringDiscovery != null && !stringDiscovery.isEmpty())
			this.stringDiscovery = stringDiscovery;
		if (filterActiveCommand != null && !filterActiveCommand.isEmpty())
			this.filterActiveCommand = filterActiveCommand;
		if (filterBlackListCertRegister != null && !filterBlackListCertRegister.isEmpty())
			this.filterBlackListCertRegister = filterBlackListCertRegister;
		getBeaconServer(homunculusTarget, port);
	}

	public synchronized void getBeaconServer(Homunculus homunculusTarget, int port) throws UnrecoverableKeyException {
		if (Boolean.valueOf(homunculus.getStarterProperties().getBeaconClearText())) {
			logger.info("Starting beacon server txt mode");
			try {
				final ServerBuilder<?> serverBuilder = NettyServerBuilder.forPort(port)
						.withChildOption(ChannelOption.SO_REUSEADDR, true);
				server = serverBuilder.addService(new RpcService()).addService(new TunnelService())
						.addService(new DataService()).build();
			} catch (final Exception e) {
				logger.logException(e);
			}
		} else {
			if (homunculusTarget != null && homunculusTarget.getMyIdentityKeystore() != null
					&& homunculusTarget.getMyIdentityKeystore().listCertificate() != null && homunculusTarget
							.getMyIdentityKeystore().listCertificate().contains(this.aliasBeaconServerInKeystore)) {
				logger.info("Certificate with alias '" + this.aliasBeaconServerInKeystore
						+ "' for Beacon server is present in keystore");
			} else {
				throw new UnrecoverableKeyException("key " + this.aliasBeaconServerInKeystore
						+ " not found in keystore [" + homunculusTarget + "]");
			}
			writePemCa(this.certChainFileLastPart);
			writePemCert(this.aliasBeaconServerInKeystore, homunculusTarget, this.certFileLastPart);
			writePrivateKey(this.aliasBeaconServerInKeystore, homunculusTarget, this.privateKeyFileLastPart);
			try {
				logger.info("Starting Beacon server");
				final SslContextBuilder sslContextBuild = GrpcSslContexts
						.forServer(new File(this.certFileLastPart), new File(this.privateKeyFileLastPart))
						.trustManager(new File(this.certChainFileLastPart)).clientAuth(ClientAuth.OPTIONAL);
				final ServerBuilder<?> serverBuilder = NettyServerBuilder.forPort(port)
						.withChildOption(ChannelOption.SO_REUSEADDR, true)
						.sslContext(GrpcSslContexts.configure(sslContextBuild, SslProvider.OPENSSL).build());
				server = serverBuilder.intercept(new AuthorizationInterceptor()).addService(new RpcService())
						.addService(new DataService()).addService(new TunnelService()).build();
			} catch (final Exception e) {
				logger.logException(e);
			}
		}
		markThread = "bs-" + String.valueOf(port) + "-" + Homunculus.THREAD_ID;
		Thread.currentThread().setName(markThread);

	}

	private class AuthorizationInterceptor implements ServerInterceptor {

		@Override
		public <ReqT, RespT> Listener<ReqT> interceptCall(final ServerCall<ReqT, RespT> serverCall,
				final Metadata metadata, final ServerCallHandler<ReqT, RespT> serverCallHandler) {
			try {
				final SSLSession sslSession = serverCall.getAttributes().get(Grpc.TRANSPORT_ATTR_SSL_SESSION);
				for (final Certificate i : sslSession.getPeerCertificates()) {
					logger.debug(" - " + i.toString());
				}
				if (sslSession.isValid()) {
					authSslOk(serverCall, metadata);
				} else {
					authSslNotFound(serverCall, metadata);
				}
			} catch (final SSLPeerUnverifiedException e) {
				authSslNotFound(serverCall, metadata);
			}
			return serverCallHandler.startCall(serverCall, metadata);
		}

		private <ReqT, RespT> void authSslOk(ServerCall<ReqT, RespT> serverCall, Metadata metadata) {
			if (filterActiveCommand != null && !filterActiveCommand.isEmpty()) {
				if (filterActiveCommandPattern == null) {
					filterActiveCommandPattern = Pattern.compile(filterActiveCommand);
				}
				final String metodo = serverCall.getMethodDescriptor().getFullMethodName();
				String name = "";
				try {
					name = serverCall.getAttributes().get(Grpc.TRANSPORT_ATTR_SSL_SESSION).getPeerPrincipal().getName();
				} catch (final SSLPeerUnverifiedException e) {
					logger.logException(e);
				}
				if (metodo.equals("beacon.RpcServiceV1/ListCommands")
						|| metodo.equals("beacon.RpcServiceV1/CompleteCommand")
						|| metodo.equals("beacon.RpcServiceV1/ElaborateMessage")
						|| metodo.equals("beacon.RpcServiceV1/KickAgent")
						|| metodo.equals("beacon.RpcServiceV1/ApproveAgentRequest")
						|| metodo.equals("beacon.RpcServiceV1/ListAgents")
						|| metodo.equals("beacon.RpcServiceV1/ListAgentsRequestComplete")
						|| metodo.equals("beacon.RpcServiceV1/ListAgentsRequestToDo")) {
					if (filterActiveCommandPattern.matcher(name).matches()) {
						logger.debug("session ok");
					} else {
						logger.info("client not authorized." + name + " not matches the regex filter "
								+ filterActiveCommand);
						final io.grpc.Status status = io.grpc.Status.PERMISSION_DENIED;
						serverCall.close(status, metadata);
					}
				}
			}
		}

		private <ReqT, RespT> void authSslNotFound(final ServerCall<ReqT, RespT> serverCall, final Metadata metadata) {
			if (serverCall.getMethodDescriptor().getFullMethodName().equals("beacon.RpcServiceV1/Register")) {
				logger.debug("session not ok but register call");
			} else {
				logger.info("session not ok");
				final io.grpc.Status status = io.grpc.Status.PERMISSION_DENIED;
				serverCall.close(status, metadata);
			}
		}
	}

	private static void writePrivateKey(String aliasBeaconServer, Homunculus homunculusTarget, String privateKey) {
		final String pk = homunculusTarget.getMyIdentityKeystore().getPrivateKeyBase64(aliasBeaconServer);
		FileWriter writer;
		try {
			writer = new FileWriter(new File(privateKey));
			writer.write("-----BEGIN PRIVATE KEY-----\n");
			writer.write(pk);
			writer.write("\n-----END PRIVATE KEY-----\n");
			writer.close();
		} catch (final IOException e) {
			logger.logException(e);
		}
	}

	private void writePemCa(String certChain) {
		try {
			final FileWriter writer = new FileWriter(new File(certChain));
			for (final String cert : caChainPem.split(",")) {
				writer.write("-----BEGIN CERTIFICATE-----\n");
				writer.write(cert);
				writer.write("\n-----END CERTIFICATE-----\n");
			}
			writer.close();
		} catch (final IOException e) {
			logger.logException(e);
		}
	}

	private void writePemCert(String aliasBeaconServer, Homunculus homunculusTarget, String certChain) {
		try {
			final FileWriter writer = new FileWriter(new File(certChain));
			final String pemTxtBase = homunculusTarget.getMyIdentityKeystore().getCaPem(aliasBeaconServer);
			writer.write("-----BEGIN CERTIFICATE-----\n");
			writer.write(pemTxtBase);
			writer.write("\n-----END CERTIFICATE-----\n");
			writer.close();
		} catch (final IOException e) {
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
			process.setName("p-" + markThread);
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
		if (getTunnels() != null && !getTunnels().isEmpty()) {
			for (final TunnelRunnerBeaconServer t : getTunnels()) {
				try {
					t.close();
				} catch (final Exception e) {
					logger.logException(e);
				}
			}
			getTunnels().clear();
		}
		if (agents != null && !agents.isEmpty()) {
			for (final BeaconAgent a : agents) {
				try {
					a.close();
				} catch (final Exception e) {
					logger.logException(e);
				}
			}
			agents.clear();
		}
		if (server != null) {
			server.shutdown();
			server.shutdownNow();
		}
		if (socketFlashBeacon != null) {
			socketFlashBeacon.close();
			socketFlashBeacon = null;
		}
		if (process != null) {
			process = null;
		}
		try {
			blockUntilShutdown();
			server = null;
		} catch (final InterruptedException e) {
			logger.logException(e);
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
			// TODO DATASERVICE Auto-generated method stub
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
			logger.debug("Received on openNetworkChannel " + responseObserver.hashCode());
			return new BeaconServerNetworkHub(responseObserver, tunnels);
		}

		@Override
		public void requestTunnel(RequestTunnelMessage request,
				StreamObserver<ResponseNetworkChannel> responseObserver) {
			logger.debug("Beacon client require tunnel -> " + request);
			try {
				final long tunnelUniqueId = UUID.randomUUID().getMostSignificantBits();
				TunnelRunnerBeaconServer tunnelRunner = null;
				if (request.getMode().equals(TunnelType.SERVER_TO_BYTES_TCP)
						|| request.getMode().equals(TunnelType.SERVER_TO_BYTES_UDP)) {
					tunnelRunner = new TunnelRunnerBeaconServer(tunnelUniqueId, request.getAgentDestination(),
							request.getAgentSource());
				} else if (request.getMode().equals(TunnelType.BYTES_TO_CLIENT_TCP)
						|| request.getMode().equals(TunnelType.BYTES_TO_CLIENT_UDP)) {
					tunnelRunner = new TunnelRunnerBeaconServer(tunnelUniqueId, request.getAgentSource(),
							request.getAgentDestination());
				}
				getTunnels().add(tunnelRunner);
				final ResponseNetworkChannel channelCreated = requestNetworkClientToAgent(request, tunnelUniqueId);
				responseObserver.onNext(channelCreated);
				responseObserver.onCompleted();
			} catch (final Exception e) {
				logger.logException(e);
			}
		}

		private ResponseNetworkChannel requestNetworkClientToAgent(RequestTunnelMessage request, long idRequest)
				throws InterruptedException {
			logger.debug("searching in " + agents.size() + " agents");
			for (final BeaconAgent at : agents) {
				if (at.getAgentUniqueName().equals(request.getAgentDestination().getAgentUniqueName())) {
					final RequestToAgent rta = RequestToAgent.newBuilder().setCaller(request.getAgentSource())
							.setUniqueIdRequest(String.valueOf(idRequest)).setType(CommandType.EXPOSE_PORT)
							.setTunnelRequest(request).build();
					at.addRequestForAgent(rta);
					logger.debug("Required client tunnel to agent target -> " + rta);
					break;
				}
			}
			final CommandReplyRequest cmdReply = waitReply(String.valueOf(idRequest), defaultTimeOut);
			final ResponseNetworkChannel channelCreated = cmdReply.getTunnelReply();
			logger.debug("Beacon client tunnel reply -> " + channelCreated);
			return channelCreated;
		}
	}

	private class RpcService extends RpcServiceV1Grpc.RpcServiceV1ImplBase {

		@Override
		public void getConfigRuntime(Agent agent, StreamObserver<ConfigReply> responseObserver) {
			try {
				final String idRequest = UUID.randomUUID().toString();
				for (final BeaconAgent at : agents) {
					if (at.getAgentUniqueName().equals(agent.getAgentUniqueName())) {
						final RequestToAgent rta = RequestToAgent.newBuilder().setCaller(agent)
								.setUniqueIdRequest(idRequest).setType(CommandType.GET_CONFIGURATION).build();
						at.addRequestForAgent(rta);
						break;
					}
				}
				CommandReplyRequest agentReply = null;
				agentReply = waitReply(idRequest, defaultTimeOut);
				elaborateConfigReply(responseObserver, agentReply);
				responseObserver.onCompleted();
			} catch (final Exception e) {
				logger.logException(e);
			}
		}

		@Override
		public void sendConfigRuntime(ConfigReport request, StreamObserver<ConfigReply> responseObserver) {
			try {
				final String idRequest = UUID.randomUUID().toString();
				for (final BeaconAgent at : agents) {
					if (at.getAgentUniqueName().equals(request.getAgent().getAgentUniqueName())) {
						final RequestToAgent rta = RequestToAgent.newBuilder().setCaller(request.getAgent())
								.setUniqueIdRequest(idRequest).setType(CommandType.SET_CONFIGURATION)
								.setRequestCommand(request.getBase64Config()).build();
						at.addRequestForAgent(rta);
						break;
					}
				}
				CommandReplyRequest agentReply = null;
				agentReply = waitReply(idRequest, defaultTimeOut);
				elaborateConfigReply(responseObserver, agentReply);
				responseObserver.onCompleted();
			} catch (final Exception e) {
				logger.logException(e);
			}
		}

		private void elaborateConfigReply(StreamObserver<ConfigReply> responseObserver, CommandReplyRequest agentReply)
				throws IOException, ClassNotFoundException {
			if (agentReply != null) {
				final String base64Config = agentReply.getBase64Config();
				final ConfigSeed configSeed = ConfigHelper.fromBase64(base64Config);
				final ConfigReply finalReply = ConfigReply.newBuilder().setBase64Config(base64Config)
						.setJsonConfig(ConfigHelper.toJson(configSeed)).setYmlConfig(ConfigHelper.toYaml(configSeed))
						.build();
				responseObserver.onNext(finalReply);
			}
		}

		@Override
		public void sendHealth(HealthRequest request, io.grpc.stub.StreamObserver<Status> responseObserver) {
			try {
				try {
					for (final BeaconAgent at : agents) {
						if (at.getAgentUniqueName().equals(request.getAgentSender().getAgentUniqueName())) {
							at.setHardwareInfo(new JSONObject(request.getJsonHardwareInfo()));
							break;
						}
					}
					responseObserver.onNext(Status.newBuilder().setStatusValue(StatusValue.GOOD.getNumber()).build());
					responseObserver.onCompleted();
				} catch (final Exception e) {
					logger.logException(e);
				}
			} catch (final Exception a) {
				logger.logException(a);
			}
		}

		@Override
		public void sendCommandReply(CommandReplyRequest request, StreamObserver<Status> responseObserver) {
			try {
				repliesQueue.put(request.getUniqueIdRequest(), request);
				responseObserver.onNext(Status.newBuilder().setStatus(StatusValue.GOOD).build());
				responseObserver.onCompleted();
			} catch (final Exception a) {
				logger.logException(a);
			}
		}

		@Override
		public void register(RegisterRequest request, StreamObserver<RegisterReply> responseObserver) {
			try {
				final String uniqueClientNameForBeacon = request.getName();
				final org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.Builder replyBuilder = RegisterReply
						.newBuilder();
				RegisterReply reply = null;
				if (!Boolean.valueOf(homunculus.getStarterProperties().getBeaconClearText())
						&& request.getRequestCsr() != null && !request.getRequestCsr().isEmpty()) {
					if (acceptAllCerts) {
						reply = replyBuilder.setStatusRegistration(Status.newBuilder().setStatus(StatusValue.GOOD))
								.setRegisterCode(uniqueClientNameForBeacon).setMonitoringFrequency(defaultPollTime)
								.setCert(getFirmedCert(request.getRequestCsr())).setCa(caChainPem).build();
						agents.add(new BeaconAgent(request, reply));
					} else {
						// TODO inserire il meccanismo per la coda autorizzativa
						final RegistrationRequest newRequest = new RegistrationRequest(request);
						listAgentRequest.add(newRequest);
						reply = replyBuilder
								.setStatusRegistration(Status.newBuilder().setStatus(StatusValue.WAIT_HUMAN))
								.setRegisterCode(uniqueClientNameForBeacon).setMonitoringFrequency(defaultPollTime)
								.setCa(caChainPem).build();
					}
				} else {
					reply = replyBuilder.setStatusRegistration(Status.newBuilder().setStatus(StatusValue.GOOD))
							.setRegisterCode(uniqueClientNameForBeacon).setMonitoringFrequency(defaultPollTime).build();
					agents.add(new BeaconAgent(request, reply));
				}
				responseObserver.onNext(reply);
				responseObserver.onCompleted();
			} catch (final Exception a) {
				logger.logException(a);
			}
		}

		private String getFirmedCert(String requestCsr) throws IOException {
			logger.debug("SIGN CSR BASE64 " + requestCsr);
			final String requestAlias = "beacon-" + UUID.randomUUID().toString().replace("-", "");
			final byte[] data = Base64.getDecoder().decode(requestCsr);
			final PKCS10CertificationRequest csrDecoded = new PKCS10CertificationRequest(data);
			if (!checkRegexOnX509(csrDecoded.getSubject())) {
				logger.debug("SIGN CSR " + csrDecoded.getSubject());
				return homunculus.getMyIdentityKeystore().signCertificateBase64(csrDecoded, requestAlias, SIGN_TIME,
						homunculus.getMyAliasCertInKeystore());
			} else
				logger.warn("\nNOT SIGN CERT\n" + csrDecoded.getSubject() + "\nbeacause it matches the blacklist ["
						+ filterBlackListCertRegister + "]");
			return null;
		}

		private boolean checkRegexOnX509(X500Name subject) {
			if (filterBlackListCertRegister != null && !filterBlackListCertRegister.isEmpty()) {
				if (filterBlackListCertRegisterPattern == null) {
					filterBlackListCertRegisterPattern = Pattern.compile(filterBlackListCertRegister);
				}
				final RDN cn = subject.getRDNs(BCStyle.CN)[0];
				return filterBlackListCertRegisterPattern.matcher(IETFUtils.valueToString(cn.getFirst().getValue()))
						.matches();
			} else {
				return false;
			}
		}

		@Override
		public void listAgents(Empty request, StreamObserver<ListAgentsReply> responseObserver) {
			try {
				final List<Agent> values = new ArrayList<>();
				for (final BeaconAgent r : agents) {
					final Agent a = Agent.newBuilder().setAgentUniqueName(r.getAgentUniqueName())
							.setShortDescription(r.getShortDescription()).setRegisterData(r.getRegisterReply())
							.setJsonHardwareInfo(r.getHardwareInfoAsJson().toString(2))
							.setLastContact(Timestamp.newBuilder().setSeconds(r.getLastCall().getMillis() / 1000))
							.build();
					values.add(a);
				}
				final ListAgentsReply reply = ListAgentsReply.newBuilder().addAllAgents(values)
						.setResult(Status.newBuilder().setStatus(StatusValue.GOOD)).build();
				responseObserver.onNext(reply);
				responseObserver.onCompleted();
			} catch (final Exception a) {
				logger.logException(a);
			}
		}

		@Override
		public void listAgentsRequestComplete(Empty request, StreamObserver<ListAgentsRequestReply> responseObserver) {
			try {
				final List<AgentRequest> values = listAgentRequests();
				final ListAgentsRequestReply reply = ListAgentsRequestReply.newBuilder().addAllRequests(values)
						.setResult(Status.newBuilder().setStatus(StatusValue.GOOD)).build();
				responseObserver.onNext(reply);
				responseObserver.onCompleted();
			} catch (final Exception a) {
				logger.logException(a);
			}
		}

		@Override
		public void approveAgentRequest(ApproveAgentRequestRequest request, StreamObserver<Status> responseObserver) {
			try {
				org.ar4k.agent.tunnels.http.grpc.beacon.Status.Builder status = Status.newBuilder()
						.setStatus(StatusValue.BAD);
				for (final RegistrationRequest r : listAgentRequest) {
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
			} catch (final Exception a) {
				logger.logException(a);
			}
		}

		@Override
		public void pollingCmdQueue(Agent request, StreamObserver<FlowMessage> responseObserver) {
			try {
				final List<RequestToAgent> values = new ArrayList<>();
				for (final BeaconAgent at : agents) {
					if (at.getAgentUniqueName().equals(request.getAgentUniqueName())) {
						values.addAll(at.getCommandsToBeExecute());
						break;
					}
				}
				final FlowMessage fm = FlowMessage.newBuilder().addAllToDoList(values).build();
				responseObserver.onNext(fm);
				responseObserver.onCompleted();
			} catch (final Exception a) {
				logger.logException(a);
			}
		}

		@Override
		public void completeCommand(CompleteCommandRequest request,
				StreamObserver<CompleteCommandReply> responseObserver) {
			try {
				final String idRequest = UUID.randomUUID().toString();
				for (final BeaconAgent at : agents) {
					if (at.getAgentUniqueName().equals(request.getAgentTarget().getAgentUniqueName())) {
						final RequestToAgent rta = RequestToAgent.newBuilder().setCaller(request.getAgentSender())
								.setUniqueIdRequest(idRequest).setType(CommandType.COMPLETE_COMMAND)
								.addAllWords(request.getWordsList()).setWordIndex(request.getWordIndex())
								.setPosition(request.getPosition()).build();
						at.addRequestForAgent(rta);
						break;
					}
				}
				CommandReplyRequest agentReply = null;
				agentReply = waitReply(idRequest, defaultTimeOut);
				if (agentReply != null) {
					final List<String> sb = new ArrayList<>();
					for (final String cr : agentReply.getRepliesList()) {
						sb.add(cr);
					}
					final CompleteCommandReply finalReply = CompleteCommandReply.newBuilder().addAllReplies(sb).build();
					responseObserver.onNext(finalReply);
				}
				responseObserver.onCompleted();
			} catch (final Exception e) {
				logger.logException(e);
			}
		}

		@Override
		public void elaborateMessage(ElaborateMessageRequest request,
				StreamObserver<ElaborateMessageReply> responseObserver) {
			try {
				final String idRequest = UUID.randomUUID().toString();
				for (final BeaconAgent at : agents) {
					if (at.getAgentUniqueName().equals(request.getAgentTarget().getAgentUniqueName())) {
						final RequestToAgent rta = RequestToAgent.newBuilder().setCaller(request.getAgentSender())
								.setUniqueIdRequest(idRequest).setType(CommandType.ELABORATE_MESSAGE_COMMAND)
								.setRequestCommand(request.getCommandMessage()).build();
						at.addRequestForAgent(rta);
						break;
					}
				}
				CommandReplyRequest agentReply = null;
				agentReply = waitReply(idRequest, defaultTimeOut);
				if (agentReply != null) {
					final StringBuilder sb = new StringBuilder();
					for (final String cr : agentReply.getRepliesList()) {
						sb.append(cr + "\n");
					}
					final ElaborateMessageReply finalReply = ElaborateMessageReply.newBuilder().setReply(sb.toString())
							.build();
					responseObserver.onNext(finalReply);
				}
				responseObserver.onCompleted();
			} catch (final Exception e) {
				logger.logException(e);
			}
		}

		@Override
		public void listCommands(ListCommandsRequest request, StreamObserver<ListCommandsReply> responseObserver) {
			try {
				final String idRequest = UUID.randomUUID().toString();
				for (final BeaconAgent at : agents) {
					if (at.getAgentUniqueName().equals(request.getAgentTarget().getAgentUniqueName())) {
						final RequestToAgent rta = RequestToAgent.newBuilder().setCaller(request.getAgentSender())
								.setUniqueIdRequest(idRequest).setType(CommandType.LIST_COMMANDS).build();
						at.addRequestForAgent(rta);
						break;
					}
				}
				CommandReplyRequest agentReply = null;
				agentReply = waitReply(idRequest, defaultTimeOut);
				if (agentReply != null) {
					final List<Command> listCommands = new ArrayList<>();
					for (final String cr : agentReply.getRepliesList()) {
						final Command c = Command.newBuilder().setAgentSender(agentReply.getAgentSender())
								.setCommand(cr).build();
						listCommands.add(c);
					}
					final ListCommandsReply finalReply = ListCommandsReply.newBuilder().addAllCommands(listCommands)
							.build();
					responseObserver.onNext(finalReply);
				}
				responseObserver.onCompleted();
			} catch (final Exception e) {
				logger.logException(e);
			}
		}

		@Override
		public void getRuntimeProvides(Agent agent, StreamObserver<ListStringReply> responseObserver) {
			try {
				final String idRequest = UUID.randomUUID().toString();
				for (final BeaconAgent at : agents) {
					if (at.getAgentUniqueName().equals(agent.getAgentUniqueName())) {
						final RequestToAgent rta = RequestToAgent.newBuilder().setCaller(agent)
								.setUniqueIdRequest(idRequest).setType(CommandType.GET_PROVIDES).build();
						at.addRequestForAgent(rta);
						break;
					}
				}
				CommandReplyRequest agentReply = null;
				agentReply = waitReply(idRequest, defaultTimeOut);
				elaborateProvidesReply(responseObserver, agentReply);
				responseObserver.onCompleted();
			} catch (final Exception e) {
				logger.logException(e);
			}
		}

		private void elaborateProvidesReply(StreamObserver<ListStringReply> responseObserver,
				CommandReplyRequest agentReply) {
			if (agentReply != null) {
				final List<String> providesList = agentReply.getRepliesList();
				final ListStringReply finalReply = ListStringReply.newBuilder()
						.setAgentSender(agentReply.getAgentSender()).setLinesNumber(providesList.size())
						.addAllListDatas(providesList).build();
				responseObserver.onNext(finalReply);
			}

		}

		@Override
		public void getRuntimeRequired(Agent agent, StreamObserver<ListStringReply> responseObserver) {
			try {
				final String idRequest = UUID.randomUUID().toString();
				for (final BeaconAgent at : agents) {
					if (at.getAgentUniqueName().equals(agent.getAgentUniqueName())) {
						final RequestToAgent rta = RequestToAgent.newBuilder().setCaller(agent)
								.setUniqueIdRequest(idRequest).setType(CommandType.GET_REQUIRED).build();
						at.addRequestForAgent(rta);
						break;
					}
				}
				CommandReplyRequest agentReply = null;
				agentReply = waitReply(idRequest, defaultTimeOut);
				elaborateRequiredReply(responseObserver, agentReply);
				responseObserver.onCompleted();
			} catch (final Exception e) {
				logger.logException(e);
			}
		}

		private void elaborateRequiredReply(StreamObserver<ListStringReply> responseObserver,
				CommandReplyRequest agentReply) {
			if (agentReply != null) {
				final List<String> providesList = agentReply.getRepliesList();
				final ListStringReply finalReply = ListStringReply.newBuilder()
						.setAgentSender(agentReply.getAgentSender()).setLinesNumber(providesList.size())
						.addAllListDatas(providesList).build();
				responseObserver.onNext(finalReply);
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
		final long start = new Date().getTime();
		CommandReplyRequest ret = null;
		try {
			while (new Date().getTime() < (start + defaultTimeOut)) {
				if (repliesQueue.containsKey(idRequest)) {
					ret = repliesQueue.remove(idRequest);
					break;
				}
				Thread.sleep(waitReplyLoopWaitTime);
			}
		} catch (final Exception e) {
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
			} catch (final InterruptedException e) {
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
			final byte[] sendData = (stringDiscovery + ":" + String.valueOf(port)).getBytes();
			try {
				final DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
						InetAddress.getByName(broadcastAddress), discoveryPort);
				socketFlashBeacon.send(sendPacket);
				logger.debug(getClass().getName() + ">>> Request packet sent to: " + broadcastAddress);
			} catch (final Exception e) {
				logger.logException(e);
				logger.warn("Error sending flash beacon " + e.getMessage());
			}
			final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				final NetworkInterface networkInterface = interfaces.nextElement();
				if (networkInterface.isLoopback() || !networkInterface.isUp()) {
					continue; // Don't want to broadcast to the loopback interface
				}
				for (final InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
					final InetAddress broadcast = interfaceAddress.getBroadcast();
					if (broadcast == null) {
						continue;
					}
					try {
						final DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast,
								discoveryPort);
						socketFlashBeacon.send(sendPacket);
					} catch (final Exception e) {
						logger.logException(e);
						logger.warn(
								"Error sending flash beacon on " + broadcast.getHostName() + " -> " + e.getMessage());
					}
					logger.debug(getClass().getName() + ">>> Request packet sent to: " + broadcast.getHostAddress()
							+ "; Interface: " + networkInterface.getDisplayName());
				}
			}
		} catch (final IOException ex) {
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
		if (getTunnels() != null) {
			getTunnels().clear();
		}
		if (listAgentRequest != null) {
			listAgentRequest.clear();
		}
		homunculus = null;
	}

	@Override
	public String toString() {
		return "BeaconServer [port=" + port + ", server=" + server + ", defaultPollTime=" + defaultPollTime
				+ ", defaultBeaconFlashMoltiplicator=" + defaultBeaconFlashMoltiplicator
				+ ", agentLabelRegisterReplies=" + agents + ", acceptAllCerts=" + acceptAllCerts + ", running="
				+ running + ", discoveryPort=" + discoveryPort + ", broadcastAddress=" + broadcastAddress
				+ ", stringDiscovery=" + stringDiscovery + ", certChainFile=" + certChainFileLastPart + ", certFile="
				+ certFileLastPart + ", privateKeyFile=" + privateKeyFileLastPart + ", aliasBeaconServerInKeystore="
				+ aliasBeaconServerInKeystore + ", caChainPem=" + caChainPem + "]";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.ar4k.agent.tunnels.http.beacon.IBeaconServer#clearOldData()
	 */
	@Override
	public void clearOldData() {
		final List<BeaconAgent> toDelete = new ArrayList<>();
		for (final BeaconAgent a : agents) {
			if (a.getLastCall().plus(TIMEOUT_AGENT_POOL).isBeforeNow()) {
				try {
					logger.info("agent " + a + " doesn't poll data since " + a.getLastCall().toDateTime());
					toDelete.add(a);
					clearTunnelForAgent(a.getAgentUniqueName());
					a.close();
				} catch (final Exception e) {
					logger.logException("deleting agent " + a, e);
				}
			}
		}
		for (final BeaconAgent atd : toDelete) {
			logger.info("agent will be removed ->\n" + atd);
			agents.remove(atd);
		}
	}

	private void clearTunnelForAgent(String agentUniqueId) {
		final List<TunnelRunnerBeaconServer> tunnelToDelete = new ArrayList<>();
		for (final TunnelRunnerBeaconServer t : getTunnels()) {
			if (t.getClientAgent().getAgentUniqueName().equals(agentUniqueId)
					|| t.getServerAgent().getAgentUniqueName().equals(agentUniqueId)) {
				tunnelToDelete.add(t);
			}
		}
		for (final TunnelRunnerBeaconServer ttd : tunnelToDelete) {
			logger.info("tunnel will be removed ->\n" + ttd);
			getTunnels().remove(ttd);
		}
	}

	public List<AgentRequest> listAgentRequests() {
		final List<AgentRequest> values = new ArrayList<>();
		for (final RegistrationRequest r : listAgentRequest) {
			final org.ar4k.agent.tunnels.http.grpc.beacon.AgentRequest.Builder a = AgentRequest.newBuilder()
					.setIdRequest(r.idRequest).setRequest(r.getRegisterRequest());
			if (r.approved && r.approvedDate != null)
				a.setApproved(r.approvedDate);
			if (r.completed != null)
				a.setRegistrationCompleted(r.completed);
			values.add(a.build());
		}
		return values;
	}

}
