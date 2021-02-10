package org.ar4k.agent.tunnels.http2.beacon.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.interfaces.IBeaconServer;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http2.beacon.BeaconAgent;
import org.ar4k.agent.tunnels.http2.beacon.RegistrationRequest;
import org.ar4k.agent.tunnels.http2.beacon.socket.server.TunnelRunnerBeaconServer;
import org.ar4k.agent.tunnels.http2.grpc.beacon.AgentRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.CommandReplyRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Timestamp;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.channel.ChannelOption;
import io.grpc.netty.shaded.io.netty.handler.ssl.ClientAuth;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslProvider;

public class BeaconServer implements Runnable, AutoCloseable, IBeaconServer {

	static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconServer.class.toString());

	static final int SIGN_TIME = 3650;
	static final long DEFAULT_TIMEOUT = 10000L;
	private static final long WAIT_REPLY_LOOP = 300L;
	private static final long TIMEOUT_AGENT_POOL = (15 * 60 * 1000);
	private int port = 0;
	private Server server = null;
	int defaultPollTime = 6000;
	private int defaultBeaconFlashMoltiplicator = 10; // ogni quanti cicli di loop in run emette un flash udp

	final List<BeaconAgent> agents = new ArrayList<>(); // elenco agenti connessi

	boolean acceptAllCerts = true; // se true firma in automatico altrimenti gestione della coda di
	// autorizzazione
	private boolean running = true;

	Homunculus homunculus = null;

	final List<TunnelRunnerBeaconServer> tunnels = new LinkedList<>();
	private Thread process = null;
	private DatagramSocket socketFlashBeacon = null;
	final List<RegistrationRequest> listAgentRequest = new ArrayList<>();
	// coda risposta clients
	final Map<String, CommandReplyRequest> repliesQueue = new ConcurrentHashMap<>();
	private int discoveryPort = 0;
	private String broadcastAddress = "255.255.255.255";
	private String stringDiscovery = "AR4K";
	private String certChainFileLastPart = "beacon-ca.pem";
	private String certFileLastPart = "beacon-cert.pem";
	private String privateKeyFileLastPart = "beacon.key";
	private String aliasBeaconServerInKeystore = "beacon-server";
	String caChainPem = null;
	String filterActiveCommand = null;

	String filterBlackListCertRegister = null;

	Pattern filterBlackListCertRegisterPattern = null;

	Pattern filterActiveCommandPattern = null;

	private String markThread;

	BeaconServer(Homunculus homunculusTarget, int port, int discoveryPort, String broadcastAddress, boolean acceptCerts,
			String stringDiscovery, String certChainFile, String certFile, String privateKeyFile,
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

	@Override
	public void approveCsrRequest(String csr) {
		for (final RegistrationRequest r : listAgentRequest) {
			if (r.getRegisterRequest().getRequestCsr().equals(csr)) {
				r.approved = true;
				r.approvedDate = Timestamp.newBuilder().setSeconds(new Date().getTime()).build();
				break;
			}
		}
	}

	@Override
	public void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

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

	@Override
	public void close() {
		stop();
		agents.clear();
		if (getTunnels() != null) {
			getTunnels().clear();
		}
		listAgentRequest.clear();
		homunculus = null;
	}

	@Override
	public List<BeaconAgent> getAgentRegistered() {
		return agents;
	}

	@Override
	public String getBroadcastAddress() {
		return broadcastAddress;
	}

	@Override
	public String getCertChainFile() {
		return certFileLastPart;
	}

	@Override
	public int getDefaultBeaconFlashMoltiplicator() {
		return defaultBeaconFlashMoltiplicator;
	}

	@Override
	public int getDefaultPollTime() {
		return defaultPollTime;
	}

	@Override
	public int getDiscoveryPort() {
		return discoveryPort;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public String getPrivateKeyFile() {
		return privateKeyFileLastPart;
	}

	@Override
	public String getStatus() {
		return server != null ? ("running on " + server.getPort()) : null;
	}

	@Override
	public String getStringDiscovery() {
		return stringDiscovery;
	}

	@Override
	public List<TunnelRunnerBeaconServer> getTunnels() {
		return tunnels;
	}

	@Override
	public boolean isAcceptAllCerts() {
		return acceptAllCerts;
	}

	@Override
	public boolean isStopped() {
		return server != null && (server.isShutdown() || server.isTerminated());
	}

	@Override
	public List<AgentRequest> listAgentRequests() {
		final List<AgentRequest> values = new ArrayList<>();
		for (final RegistrationRequest r : listAgentRequest) {
			final org.ar4k.agent.tunnels.http2.grpc.beacon.AgentRequest.Builder a = AgentRequest.newBuilder()
					.setIdRequest(r.idRequest).setRequest(r.getRegisterRequest());
			if (r.approved && r.approvedDate != null)
				a.setApproved(r.approvedDate);
			if (r.completed != null)
				a.setRegistrationCompleted(r.completed);
			values.add(a.build());
		}
		return values;
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
			} catch (final Exception e) {
				logger.info("in Beacon server loop error " + e.getMessage());
				logger.logException(e);
			}
		}
		logger.info("in Beacon server loop terminated ");
	}

	@Override
	public void sendFlashUdp() {
		try {
			if (socketFlashBeacon == null) {
				socketFlashBeacon = new DatagramSocket();
				socketFlashBeacon.setBroadcast(true);
			}
			final byte[] sendData = (stringDiscovery + ":" + String.valueOf(port)).getBytes();
			sendDiscoveryPacket(sendData);
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

	@Override
	public void setBroadcastAddress(String broadcastAddress) {
		this.broadcastAddress = broadcastAddress;
	}

	@Override
	public void setDefaultBeaconFlashMoltiplicator(int defaultBeaconFlashMoltiplicator) {
		this.defaultBeaconFlashMoltiplicator = defaultBeaconFlashMoltiplicator;
	}

	@Override
	public void setDefaultPollTime(int defaultPollTime) {
		this.defaultPollTime = defaultPollTime;
	}

	@Override
	public void setDiscoveryPort(int discoveryPort) {
		this.discoveryPort = discoveryPort;
	}

	@Override
	public void setStringDiscovery(String stringDiscovery) {
		this.stringDiscovery = stringDiscovery;
	}

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
		if (!agents.isEmpty()) {
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

	@Override
	public String toString() {
		StringBuilder builder2 = new StringBuilder();
		builder2.append("BeaconServer [port=");
		builder2.append(port);
		builder2.append(", server=");
		builder2.append(server);
		builder2.append(", defaultPollTime=");
		builder2.append(defaultPollTime);
		builder2.append(", defaultBeaconFlashMoltiplicator=");
		builder2.append(defaultBeaconFlashMoltiplicator);
		builder2.append(", agents=");
		builder2.append(agents);
		builder2.append(", acceptAllCerts=");
		builder2.append(acceptAllCerts);
		builder2.append(", running=");
		builder2.append(running);
		builder2.append(", tunnels=");
		builder2.append(tunnels);
		builder2.append(", process=");
		builder2.append(process);
		builder2.append(", socketFlashBeacon=");
		builder2.append(socketFlashBeacon);
		builder2.append(", listAgentRequest=");
		builder2.append(listAgentRequest);
		builder2.append(", repliesQueue=");
		builder2.append(repliesQueue);
		builder2.append(", discoveryPort=");
		builder2.append(discoveryPort);
		builder2.append(", broadcastAddress=");
		builder2.append(broadcastAddress);
		builder2.append(", stringDiscovery=");
		builder2.append(stringDiscovery);
		builder2.append(", certChainFileLastPart=");
		builder2.append(certChainFileLastPart);
		builder2.append(", certFileLastPart=");
		builder2.append(certFileLastPart);
		builder2.append(", privateKeyFileLastPart=");
		builder2.append(privateKeyFileLastPart);
		builder2.append(", aliasBeaconServerInKeystore=");
		builder2.append(aliasBeaconServerInKeystore);
		builder2.append(", caChainPem=");
		builder2.append(caChainPem);
		builder2.append(", filterActiveCommand=");
		builder2.append(filterActiveCommand);
		builder2.append(", filterBlackListCertRegister=");
		builder2.append(filterBlackListCertRegister);
		builder2.append(", filterBlackListCertRegisterPattern=");
		builder2.append(filterBlackListCertRegisterPattern);
		builder2.append(", filterActiveCommandPattern=");
		builder2.append(filterActiveCommandPattern);
		builder2.append(", markThread=");
		builder2.append(markThread);
		builder2.append("]");
		return builder2.toString();
	}

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
				Thread.sleep(WAIT_REPLY_LOOP);
			}
		} catch (final Exception e) {
			logger.logException(e);
		}
		return ret;
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

	private synchronized void getBeaconServer(Homunculus homunculusTarget, int port) throws UnrecoverableKeyException {
		if (Boolean.valueOf(homunculus.getStarterProperties().getBeaconClearText())) {
			logger.info("Starting beacon server txt mode");
			try {
				final ServerBuilder<?> serverBuilder = NettyServerBuilder.forPort(port)
						.withChildOption(ChannelOption.SO_REUSEADDR, true);
				server = serverBuilder.addService(new BeaconServerRpcService(this))
						.addService(new BeaconServerTunnelService(this)).addService(new BeaconServerDataService(this))
						.build();
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
				server = serverBuilder.intercept(new BeaconServerAuthorizationInterceptor(this))
						.addService(new BeaconServerRpcService(this)).addService(new BeaconServerDataService(this))
						.addService(new BeaconServerTunnelService(this)).build();
			} catch (final Exception e) {
				logger.logException(e);
			}
		}
		markThread = "bs-" + String.valueOf(port) + "-" + Homunculus.THREAD_ID;
		Thread.currentThread().setName(markThread);

	}

	private void sendDiscoveryPacket(final byte[] sendData) {
		try {
			final DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
					InetAddress.getByName(broadcastAddress), discoveryPort);
			socketFlashBeacon.send(sendPacket);
			logger.debug(getClass().getName() + ">>> Request packet sent to: " + broadcastAddress);
		} catch (final Exception e) {
			logger.logException(e);
			logger.warn("Error sending flash beacon " + e.getMessage());
		}
	}

	private void writePemCa(String certChain) {
		try (final FileWriter writer = new FileWriter(new File(certChain))) {
			logger.info("path of Beacon trust certificate -> " + new File(certChain).getAbsolutePath());
			for (final String cert : caChainPem.split(",")) {
				writer.write("-----BEGIN CERTIFICATE-----\n");
				writer.write(cert);
				writer.write("\n-----END CERTIFICATE-----\n");
			}
		} catch (final IOException e) {
			logger.logException(e);
		}
	}

	private void writePemCert(String aliasBeaconServer, Homunculus homunculusTarget, String certChain) {
		try (final FileWriter writer = new FileWriter(new File(certChain))) {
			final String pemTxtBase = homunculusTarget.getMyIdentityKeystore().getCaPem(aliasBeaconServer);
			writer.write("-----BEGIN CERTIFICATE-----\n");
			writer.write(pemTxtBase);
			writer.write("\n-----END CERTIFICATE-----\n");
		} catch (final IOException e) {
			logger.logException(e);
		}
	}

	public static long getDefaultTimeout() {
		return DEFAULT_TIMEOUT;
	}

	public static long getWaitreplyloopwaittime() {
		return WAIT_REPLY_LOOP;
	}

	private static void writePrivateKey(String aliasBeaconServer, Homunculus homunculusTarget, String privateKey) {
		final String pk = homunculusTarget.getMyIdentityKeystore().getPrivateKeyBase64(aliasBeaconServer);
		try (final FileWriter writer = new FileWriter(new File(privateKey))) {
			writer.write("-----BEGIN PRIVATE KEY-----\n");
			writer.write(pk);
			writer.write("\n-----END PRIVATE KEY-----\n");
			writer.close();
		} catch (final IOException e) {
			logger.logException(e);
		}
	}

}
