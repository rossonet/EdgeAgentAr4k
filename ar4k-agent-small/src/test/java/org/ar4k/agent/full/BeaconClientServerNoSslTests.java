package org.ar4k.agent.full;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.config.network.NetworkConfig;
import org.ar4k.agent.config.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.config.network.NetworkConfig.NetworkProtocol;
import org.ar4k.agent.config.network.NetworkTunnel;
import org.ar4k.agent.console.Ar4kAgent;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.helper.ContextCreationHelper;
import org.ar4k.agent.helper.KeystoreLoader;
import org.ar4k.agent.helper.NetworkHelper;
import org.ar4k.agent.helper.ReflectionUtils;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.tunnels.http2.beacon.BeaconServiceConfig;
import org.ar4k.agent.tunnels.http2.beacon.client.BeaconClient;
import org.ar4k.agent.tunnels.http2.beacon.socket.classic.BeaconNetworkClassicConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.annotation.DirtiesContext;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/*
import io.grpc.netty.shaded.io.netty.bootstrap.ServerBootstrap;
import io.grpc.netty.shaded.io.netty.buffer.ByteBuf;
import io.grpc.netty.shaded.io.netty.buffer.ByteBufUtil;
import io.grpc.netty.shaded.io.netty.buffer.Unpooled;
import io.grpc.netty.shaded.io.netty.channel.ChannelFuture;
import io.grpc.netty.shaded.io.netty.channel.ChannelHandlerContext;
import io.grpc.netty.shaded.io.netty.channel.ChannelInitializer;
import io.grpc.netty.shaded.io.netty.channel.ChannelOption;
import io.grpc.netty.shaded.io.netty.channel.SimpleChannelInboundHandler;
import io.grpc.netty.shaded.io.netty.channel.nio.NioEventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.socket.SocketChannel;
import io.grpc.netty.shaded.io.netty.channel.socket.nio.NioServerSocketChannel;
*/
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
public class BeaconClientServerNoSslTests {
	private static final String CLIENT1_LABEL = "client1";
	private static final String CLIENT2_LABEL = "client2";
	private static final String SERVER_LABEL = "server";
	private final ExecutorService executor = Executors.newCachedThreadPool();
	private final Map<String, Homunculus> testAnimas = new HashMap<>();
	private final File keyStoreServer = new File("./tmp/server.ks");
	private final File keyStoreClient1 = new File("./tmp/client1.ks");
	private final File keyStoreClient2 = new File("./tmp/client2.ks");
	private final String serverAliasInKeystore = "server";
	private final String client2AliasInKeystore = "client2";
	private final String client1AliasInKeystore = "client1";
	private final String passwordKs = "password";

	private NetworkTunnel networkTunnel = null;
	private Future<Boolean> serverTCP = null;
	private Future<Boolean> clientTCP1 = null;
	private Future<Boolean> clientTCP2 = null;
	private Future<Boolean> clientTCP3 = null;
	private Future<Boolean> clientTCP4 = null;
	private Future<Boolean> clientTCP5 = null;
	private Future<Boolean> clientTCP6 = null;
	private Future<Boolean> clientTCP7 = null;
	private Future<Boolean> clientTCP8 = null;
	private Future<Boolean> clientTCP9 = null;
	private Future<Boolean> clientTCP10 = null;
	private Future<Boolean> clientTCP11 = null;
	private Future<Boolean> clientTCP12 = null;
	private Future<Boolean> clientTCP13 = null;
	private Future<Boolean> clientTCP14 = null;
	private Future<Boolean> clientTCP15 = null;
	private Future<Boolean> clientTCP16 = null;
	private Future<Boolean> clientTCP17 = null;
	private List<String> completed = new ArrayList<>();
	private String basePayload = null;
	private int actualTestSize = 0;

	@Test(timeout = 320000)
	public void ante_a_serverAndClientTestMonoThread() throws Exception {
		serverAndClientTest(false, true, 0, false, 10, false);
	}

	@Test(timeout = 320000)
	public void ante_b_serverAndClientTestMonoThreadAndPayload() throws Exception {
		serverAndClientTest(false, true, 250, false, 10, false);
	}

	@Test(timeout = 320000)
	public void ante_c_serverAndClientTestWithFork() throws Exception {
		serverAndClientTest(false, false, 0, false, 10, false);
	}

	@Test(timeout = 320000)
	public void ante_d_serverAndClientTestWithForkAndPayload() throws Exception {
		serverAndClientTest(false, false, 250, false, 10, false);
	}

	@Test(timeout = 320000)
	public void ante_e_serverAndClientTestWithSshPayload() throws Exception {
		serverAndClientTest(false, false, 0, true, 10, false);
	}

	@Test(timeout = 640000)
	public void short_a_serverAndClientTestMonoThread() throws Exception {
		serverAndClientTest(false, true, 0, false, 80, false);
	}

	@Test(timeout = 640000)
	public void short_b_serverAndClientTestMonoThreadAndPayload() throws Exception {
		serverAndClientTest(false, true, 250, false, 80, false);
	}

	@Test(timeout = 640000)
	public void short_c_serverAndClientTestWithFork() throws Exception {
		serverAndClientTest(false, false, 0, false, 80, false);
	}

	@Test(timeout = 640000)
	public void short_d_serverAndClientTestWithForkAndPayload() throws Exception {
		serverAndClientTest(false, false, 250, false, 80, false);
	}

	@Test(timeout = 2560000)
	public void short_e_serverAndClientTestWithSshPayload() throws Exception {
		serverAndClientTest(false, false, 0, true, 80, false);
	}

	@Test(timeout = 2560000)
	public void verybig_a_serverAndClientTestMonoThread() throws Exception {
		serverAndClientTest(false, true, 0, false, 100, false);
	}

	@Test(timeout = 2560000)
	public void verybig_b_serverAndClientTestMonoThreadAndPayload() throws Exception {
		serverAndClientTest(false, true, 250, false, 100, false);
	}

	@Test(timeout = 2560000)
	public void verybig_c_serverAndClientTestWithFork() throws Exception {
		serverAndClientTest(false, false, 0, false, 100, false);
	}

	@Test(timeout = 2560000)
	public void verybig_d_serverAndClientTestWithForkAndPayload() throws Exception {
		serverAndClientTest(false, false, 250, false, 100, false);
	}

	@Test(timeout = 5120000)
	public void verybig_e_serverAndClientTestWithSshPayload() throws Exception {
		serverAndClientTest(false, false, 0, true, 100, false);
	}

	@Ignore
	@Test(timeout = 500000)
	public void testSshdServer() throws Exception {
		final Callable<Boolean> runner = getNewSshdServer(4444);
		System.out.println("starting sshd server on port " + 4444);
		runner.call();
		Thread.sleep(5000);
		final Callable<Boolean> clientRunner2 = createClientRunnerSsh("127.0.0.1", 4444, "TEST");
		clientTCP2 = executor.submit(clientRunner2);
		while (!completed.contains("TEST")) {
			System.out.println("waiting...");
			System.out.println("TEST -> " + completed.contains("TEST"));
			Thread.sleep(3000);
		}
		assertTrue(completed.contains("TEST"));
	}

	@Before
	public void before() throws Exception {
		// use original netty
		BeaconClient.setUseNettyForTunnel(true);
		actualTestSize = 0;
		deleteTmpDirectories();
		Files.createDirectories(Paths.get("./tmp"));
		KeystoreLoader.create(serverAliasInKeystore, keyStoreServer.getAbsolutePath(), passwordKs);
		KeystoreLoader.create(client2AliasInKeystore, keyStoreClient1.getAbsolutePath(), passwordKs);
		KeystoreLoader.create(client1AliasInKeystore, keyStoreClient2.getAbsolutePath(), passwordKs);
	}

	private void deleteTmpDirectories() {
		deleteDir(new File("./tmp"));
		deleteDir(new File("./tmp1"));
		deleteDir(new File("./tmp2"));
		deleteDir(new File("./tmp3"));
		deleteDir(new File("~/.ar4k"));
	}

	private void deleteDir(File dir) {
		final File[] files = dir.listFiles();
		if (files != null) {
			for (final File file : files) {
				deleteDir(file);
			}
		}
		dir.delete();
	}

	@After
	public void tearDown() throws Exception {
		System.err.println("\n\nEND TESTS\n\n");
		for (final Homunculus a : testAnimas.values()) {
			a.close();
		}
		testAnimas.clear();
		if (serverTCP != null) {
			serverTCP.cancel(true);
		}
		if (clientTCP1 != null) {
			clientTCP1.cancel(false);
		}
		if (clientTCP2 != null) {
			clientTCP2.cancel(false);
		}
		if (clientTCP3 != null) {
			clientTCP3.cancel(false);
		}
		if (clientTCP4 != null) {
			clientTCP4.cancel(false);
		}
		if (clientTCP5 != null) {
			clientTCP5.cancel(false);
		}
		if (clientTCP6 != null) {
			clientTCP6.cancel(false);
		}
		if (clientTCP7 != null) {
			clientTCP7.cancel(false);
		}
		if (clientTCP8 != null) {
			clientTCP8.cancel(false);
		}
		if (clientTCP9 != null) {
			clientTCP9.cancel(false);
		}
		if (clientTCP10 != null) {
			clientTCP10.cancel(false);
		}
		if (clientTCP11 != null) {
			clientTCP11.cancel(false);
		}
		if (clientTCP12 != null) {
			clientTCP12.cancel(false);
		}
		if (clientTCP13 != null) {
			clientTCP13.cancel(false);
		}
		if (clientTCP14 != null) {
			clientTCP14.cancel(false);
		}
		if (clientTCP15 != null) {
			clientTCP15.cancel(false);
		}
		if (clientTCP16 != null) {
			clientTCP16.cancel(false);
		}
		if (clientTCP17 != null) {
			clientTCP17.cancel(false);
		}
		if (networkTunnel != null) {
			networkTunnel.close();
			networkTunnel = null;
		}
		Files.deleteIfExists(Paths.get("./tmp/test.config.base64.ar4k"));
		Files.deleteIfExists(keyStoreServer.toPath());
		Files.deleteIfExists(keyStoreClient1.toPath());
		Files.deleteIfExists(keyStoreClient2.toPath());
		if (executor != null) {
			executor.shutdownNow();
			executor.awaitTermination(3, TimeUnit.MINUTES);
		}
		deleteTmpDirectories();
		System.err.println("\n\n\n\n\n\n\n\n\n\n");
		System.err.println("--------------------------------------------------------------");
		System.err.println("THRED STATUS AFTER A");
		System.err.println(ReflectionUtils.logThreadInfo());
		System.err.println("\n\n--------------------------------------------------------------");
	}

	private void serverAndClientTest(boolean ssl, boolean singleThread, int payloadSize, boolean ssh, int testElements,
			boolean disconnectServer) throws Exception {
		actualTestSize = testElements;
		final List<String> baseArgs = new ArrayList<>();
		basePayload = getAlphaNumericString(payloadSize);
		if (ssl) {
			baseArgs.add("--ar4k.beaconClearText=false");
		}
		baseArgs.add("--spring.shell.command.quit.enabled=false");
		baseArgs.add("--logging.level.root=INFO");
		// baseArgs.add("--ar4k.confPath=./tmp");
		// baseArgs.add("--ar4k.fileConfig=./tmp/test.config.base64.ar4k");
		baseArgs.add("--ar4k.webConfig=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
		baseArgs.add("--ar4k.dnsConfig=demo1.rossonet.name");
//    addArgs.add("--ar4k.baseConfig=");
		baseArgs.add("--ar4k.webKeystore=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
		baseArgs.add("--ar4k.dnsKeystore=ks1.rossonet.name");
		// baseArgs.add("--ar4k.keystoreMainAlias=");
		baseArgs.add("--ar4k.keystorePassword=" + passwordKs);
		baseArgs.add("--ar4k.beaconCaChainPem= ");// not used
		baseArgs.add("--ar4k.adminPassword=password");
//    addArgs.add("--ar4k.webRegistrationEndpoint=");
//    addArgs.add("--ar4k.dnsRegistrationEndpoint=");
		baseArgs.add("--ar4k.beaconDiscoveryFilterString=TEST-REGISTER");
		baseArgs.add("--ar4k.beaconDiscoveryPort=33667");
		baseArgs.add("--ar4k.fileConfigOrder=1");
		baseArgs.add("--ar4k.webConfigOrder=2");
		baseArgs.add("--ar4k.dnsConfigOrder=3");
		baseArgs.add("--ar4k.baseConfigOrder=0");
		baseArgs.add("--ar4k.threadSleep=1000");
		baseArgs.add("--ar4k.logoUrl=/static/img/ar4k.png");
		final List<String> baseArgsServer = new ArrayList<String>(baseArgs);
		baseArgsServer.add("--ar4k.confPath=./tmp1");
		baseArgsServer.add("--ar4k.fileConfig=./tmp1/test.config.base64.ar4k");
		final List<String> baseArgsClient1 = new ArrayList<String>(baseArgs);
		baseArgsClient1.add("--ar4k.confPath=./tmp2");
		baseArgsClient1.add("--ar4k.fileConfig=./tmp2/test.config.base64.ar4k");
		final List<String> baseArgsClient2 = new ArrayList<String>(baseArgs);
		baseArgsClient2.add("--ar4k.confPath=./tmp3");
		baseArgsClient2.add("--ar4k.fileConfig=./tmp3/test.config.base64.ar4k");
		baseArgs.clear();
		final EdgeConfig serverConfig = new EdgeConfig();
		serverConfig.name = "server-beacon";
		serverConfig.beaconServer = null;
		serverConfig.beaconDiscoveryPort = 0;
		final BeaconServiceConfig beaconServiceConfig = new BeaconServiceConfig();
		beaconServiceConfig.discoveryPort = 33667;
		beaconServiceConfig.port = 22116;
		beaconServiceConfig.aliasBeaconServerInKeystore = serverAliasInKeystore;
		beaconServiceConfig.stringDiscovery = "TEST-REGISTER";
		serverConfig.pots.add(beaconServiceConfig);
		final EdgeConfig config2 = null;
		final EdgeConfig config1 = null;

		testAnimas.put(SERVER_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "a.log",
						keyStoreServer.getAbsolutePath(), 1124, baseArgsServer, serverConfig, serverAliasInKeystore,
						serverAliasInKeystore, "https://127.0.0.1:22116")).get());
		testAnimas.put(CLIENT2_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "b.log",
						keyStoreClient2.getAbsolutePath(), 1125, baseArgsClient2, config2, client2AliasInKeystore,
						client2AliasInKeystore, "https://127.0.0.1:22116")).get());
		testAnimas.put(CLIENT1_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "c.log",
						keyStoreClient1.getAbsolutePath(), 1126, baseArgsClient1, config1, client1AliasInKeystore,
						client1AliasInKeystore, "https://127.0.0.1:22116")).get());
		Thread.sleep(5000);
		for (final Homunculus a : testAnimas.values()) {
			final String animaName = a.getRuntimeConfig() != null ? a.getRuntimeConfig().getName() : "no-config";
			if (animaName.equals("server-beacon")) {
				Assert.assertEquals(a.getState(), Homunculus.HomunculusStates.RUNNING);
			} else {
				Assert.assertEquals(a.getState(), Homunculus.HomunculusStates.STAMINAL);
			}
		}
		Thread.sleep(3000);
		final String destinationIp = "127.0.0.1";
		final int destinationPort = NetworkHelper.findAvailablePort(7777);
		final int srcPort = NetworkHelper.findAvailablePort(8888);
		Callable<Boolean> runner = null;
		if (ssh) {
			runner = getNewSshdServer(destinationPort);
		} else {
			if (singleThread) {
				runner = getNewTcpServerSingleThread(destinationPort);
			} else {
				runner = getNewTcpServerMultiThread(destinationPort);
			}
		}

		// codice
		serverTCP = executor.submit(runner);
		final NetworkConfig config = new BeaconNetworkClassicConfig("tunnel-test", "tunnel in fase di test",
				NetworkMode.CLIENT, NetworkProtocol.TCP, destinationIp, destinationPort, srcPort);
		networkTunnel = testAnimas.get(CLIENT1_LABEL).getBeaconClient()
				.getNewNetworkTunnel(testAnimas.get(CLIENT2_LABEL).getAgentUniqueName(), config);
		System.out.println("network tunnel status -> " + networkTunnel.getNetworkReceiver().getNetworkStatus());
		Thread.sleep(5000);
		System.err.println("\n\n\n\n\n\n\n\n\n\n");
		System.err.println("--------------------------------------------------------------");
		System.err.println("STARTING - SEND FIRST PACKAGE");
		System.err.println("--------------------------------------------------------------");
		if (ssh) {
			final Callable<Boolean> clientRunnerSsh = createClientRunnerSsh(destinationIp, srcPort, "A");
			clientTCP1 = executor.submit(clientRunnerSsh);
		} else {
			final Callable<Boolean> clientRunner1 = createClientRunnerTcp(destinationIp, srcPort, "A");
			clientTCP1 = executor.submit(clientRunner1);
			if (!singleThread) {
				Thread.sleep(1800);
				final Callable<Boolean> clientRunner2 = createClientRunnerTcp(destinationIp, srcPort, "B");
				clientTCP2 = executor.submit(clientRunner2);
				Thread.sleep(2000);
				final Callable<Boolean> clientRunner3 = createClientRunnerTcp(destinationIp, srcPort, "C");
				clientTCP3 = executor.submit(clientRunner3);
				Thread.sleep(2200);
				final Callable<Boolean> clientRunner4 = createClientRunnerTcp(destinationIp, srcPort, "D");
				clientTCP4 = executor.submit(clientRunner4);
				Thread.sleep(1200);
				final Callable<Boolean> clientRunner5 = createClientRunnerTcp(destinationIp, srcPort, "E");
				clientTCP5 = executor.submit(clientRunner5);
				Thread.sleep(1800);
				final Callable<Boolean> clientRunner6 = createClientRunnerTcp(destinationIp, srcPort, "F");
				clientTCP6 = executor.submit(clientRunner6);
				Thread.sleep(1900);
				final Callable<Boolean> clientRunner7 = createClientRunnerTcp(destinationIp, srcPort, "G");
				clientTCP7 = executor.submit(clientRunner7);
				Thread.sleep(2200);
				final Callable<Boolean> clientRunner8 = createClientRunnerTcp(destinationIp, srcPort, "H");
				clientTCP8 = executor.submit(clientRunner8);
				Thread.sleep(1200);
				final Callable<Boolean> clientRunner9 = createClientRunnerTcp(destinationIp, srcPort, "I");
				clientTCP9 = executor.submit(clientRunner9);
				Thread.sleep(1800);
				final Callable<Boolean> clientRunner10 = createClientRunnerTcp(destinationIp, srcPort, "L");
				clientTCP10 = executor.submit(clientRunner10);
				Thread.sleep(1300);
				final Callable<Boolean> clientRunner11 = createClientRunnerTcp(destinationIp, srcPort, "M");
				clientTCP11 = executor.submit(clientRunner11);
				Thread.sleep(2200);
				final Callable<Boolean> clientRunner12 = createClientRunnerTcp(destinationIp, srcPort, "N");
				clientTCP12 = executor.submit(clientRunner12);
				Thread.sleep(1200);
				final Callable<Boolean> clientRunner13 = createClientRunnerTcp(destinationIp, srcPort, "O");
				clientTCP13 = executor.submit(clientRunner13);
				Thread.sleep(1800);
				final Callable<Boolean> clientRunner14 = createClientRunnerTcp(destinationIp, srcPort, "P");
				clientTCP14 = executor.submit(clientRunner14);
				Thread.sleep(900);
				final Callable<Boolean> clientRunner15 = createClientRunnerTcp(destinationIp, srcPort, "Q");
				clientTCP15 = executor.submit(clientRunner15);
				Thread.sleep(2200);
				final Callable<Boolean> clientRunner16 = createClientRunnerTcp(destinationIp, srcPort, "R");
				clientTCP16 = executor.submit(clientRunner16);
				Thread.sleep(4000);
				final Callable<Boolean> clientRunner17 = createClientRunnerTcp(destinationIp, srcPort, "S");
				clientTCP17 = executor.submit(clientRunner17);
			}
		}
		if (disconnectServer) {
			Thread.sleep(10000);
			testAnimas.get(SERVER_LABEL).close();
			testAnimas.remove(SERVER_LABEL);
			Thread.sleep(5000);
			testAnimas.put(SERVER_LABEL,
					executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "a.log",
							keyStoreServer.getAbsolutePath(), 1124, baseArgsServer, serverConfig, serverAliasInKeystore,
							serverAliasInKeystore, "https://127.0.0.1:22116")).get());
		}
		while (!completed.contains("A") || ((!ssh && !singleThread) && (!completed.contains("B")
				|| !completed.contains("C") || !completed.contains("D") || !completed.contains("E")
				|| !completed.contains("F") || !completed.contains("G") || !completed.contains("H")
				|| !completed.contains("I") || !completed.contains("L") || !completed.contains("M")
				|| !completed.contains("N") || !completed.contains("O") || !completed.contains("P")
				|| !completed.contains("Q") || !completed.contains("R") || !completed.contains("S")))) {
			System.out.println("waiting...");
			System.out.println("A -> " + completed.contains("A"));
			if ((!ssh && !singleThread)) {
				System.out.println(" [R]:" + networkTunnel.getPacketReceived() + " [S]:" + networkTunnel.getPacketSend()
						+ " [E]:" + networkTunnel.getPacketError() + " [W]:" + networkTunnel.getWaitingPackagesCount());
				System.out.println("B -> " + completed.contains("B"));
				System.out.println("C -> " + completed.contains("C"));
				System.out.println("D -> " + completed.contains("D"));
				System.out.println("E -> " + completed.contains("E"));
				System.out.println("F -> " + completed.contains("F"));
				System.out.println("G -> " + completed.contains("G"));
				System.out.println("H -> " + completed.contains("H"));
				System.out.println("I -> " + completed.contains("I"));
				System.out.println("L -> " + completed.contains("L"));
				System.out.println("M -> " + completed.contains("M"));
				System.out.println("N -> " + completed.contains("N"));
				System.out.println("O -> " + completed.contains("O"));
				System.out.println("P -> " + completed.contains("P"));
				System.out.println("Q -> " + completed.contains("Q"));
				System.out.println("R -> " + completed.contains("R"));
				System.out.println("S -> " + completed.contains("S"));
			}
			Thread.sleep(3000);
		}
		networkTunnel.close();
		networkTunnel = null;
		assertTrue(completed.contains("A"));
		if ((!ssh && !singleThread)) {
			assertTrue(completed.contains("B"));
			assertTrue(completed.contains("C"));
			assertTrue(completed.contains("D"));
			assertTrue(completed.contains("E"));
			assertTrue(completed.contains("F"));
			assertTrue(completed.contains("G"));
			assertTrue(completed.contains("H"));
			assertTrue(completed.contains("I"));
			assertTrue(completed.contains("L"));
			assertTrue(completed.contains("M"));
			assertTrue(completed.contains("N"));
			assertTrue(completed.contains("O"));
			assertTrue(completed.contains("P"));
			assertTrue(completed.contains("Q"));
			assertTrue(completed.contains("R"));
			assertTrue(completed.contains("S"));
		}
		if (serverTCP != null) {
			serverTCP.cancel(true);
		}
	}

	private Callable<Boolean> getNewSshdServer(int destinationPort) {
		return new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				final SshServer sshd = SshServer.setUpDefaultServer();
				sshd.setPort(destinationPort);
				sshd.setShellFactory(new ProcessShellFactory(new String[] { "/bin/bash", "-i", "-l" }));
				final SimpleGeneratorHostKeyProvider hostKeyProvider = new SimpleGeneratorHostKeyProvider();
				hostKeyProvider.setAlgorithm("RSA");
				sshd.setKeyPairProvider(hostKeyProvider);
				sshd.setPasswordAuthenticator(new MyPasswordAuthenticator());
				sshd.start();
				return true;
			}
		};
	}

	private class MyPasswordAuthenticator implements PasswordAuthenticator {

		@Override
		public boolean authenticate(String username, String password, ServerSession session)
				throws PasswordChangeRequiredException, AsyncAuthException {
			return true;
		}

	}

	private Callable<Boolean> getNewTcpServerMultiThread(final int destinationPort) {
		return new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
				final NioEventLoopGroup workerGroup = new NioEventLoopGroup();
				try {
					final ServerTcpInitHandler serverInitHandler = new ServerTcpInitHandler();
					final ServerBootstrap b = new ServerBootstrap().group(bossGroup, workerGroup)
							.channel(NioServerSocketChannel.class).childHandler(serverInitHandler)
							.childOption(ChannelOption.SO_KEEPALIVE, true)// .childOption(ChannelOption.TCP_NODELAY,
																			// true)
							.childOption(ChannelOption.SO_REUSEADDR, true);
					// .childOption(ChannelOption.AUTO_READ, false);
					final ChannelFuture serverHandler = b.bind(destinationPort).await();
					if (serverHandler.isSuccess()) {
						System.out.println("TEST - server Netty on port " + destinationPort);
					} else {
						System.out.println("server Netty on port " + destinationPort);
					}
					System.out.println("started server socket " + destinationPort);
				} catch (final Exception a) {
					System.out.println("-------------------------- fault creating server socket");
					a.printStackTrace();
				}
				return true;
			}
		};
	}

	private Callable<Boolean> getNewTcpServerSingleThread(final int destinationPort) {
		return new Callable<Boolean>() {
			// private int last = 0;

			@Override
			public Boolean call() throws Exception {
				System.out.println("!!! server socket !!!");
				final ServerSocket serverSocket = new ServerSocket(destinationPort);
				serverSocket.setReuseAddress(true);
				final Socket socket = serverSocket.accept();
				final PrintWriter w = new PrintWriter(socket.getOutputStream(), true);
				final InputStreamReader reader = new InputStreamReader(socket.getInputStream());
				try {
					while (!completed.contains("A")) {
						if (reader.ready()) {
							final StringBuilder sb = new StringBuilder();
							while (reader.ready()) {
								sb.append((char) reader.read());
							}
							final String nextLine = sb.toString();
							System.out.println("*********** string received\n" + nextLine);
							int valueNew = 0;
							if (basePayload != null) {
								valueNew = Integer.valueOf(nextLine.replace(basePayload, ""));
							} else {
								valueNew = Integer.valueOf(nextLine);
							}
							System.out.println("--------------------------------------------------------------");
							System.out.println("server test received from beacon client " + valueNew);
							System.out.println("--------------------------------------------------------------");
							Thread.sleep(1000);
							final int reply = valueNew + 1;
							if (reply > (actualTestSize + 2)) {
								socket.close();
								System.out.println("--------------------------------------------------------------");
								System.out.println("server test close on " + reply);
								System.out.println("--------------------------------------------------------------");
							} else {
								if (basePayload != null) {
									final String string = basePayload + String.valueOf(reply);
									w.write(string);
									System.out.println("*********** string sent\n" + string);
								} else {
									w.write(String.valueOf(reply));
								}
								w.flush();
								System.out.println("--------------------------------------------------------------");
								System.out.println("server test sent to beacon client " + reply);
								System.out.println("--------------------------------------------------------------");
							}
						}
					}
				} catch (final InterruptedException f) {
					serverSocket.close();
					System.out.println("-------------------------- server closed " + EdgeLogger.stackTraceToString(f));
				} catch (final Exception a) {
					serverSocket.close();
					System.out.println("-------------------------- server closed " + EdgeLogger.stackTraceToString(a));
					a.printStackTrace();
				}
				serverSocket.close();
				System.out.println("--------------------------------------------------------------");
				System.out.println("server test close on end");
				System.out.println("--------------------------------------------------------------");
				return true;
			}

		};
	}

	public Callable<Boolean> createClientRunnerSsh(String destinationIp, int srcPort, String tag) {
		return new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				final String user = "test-user";
				final String password = "password-user";
				int value = 1;
				try {

					final java.util.Properties config = new java.util.Properties();
					config.put("StrictHostKeyChecking", "no");
					final JSch jsch = new JSch();
					final Session session = jsch.getSession(user, destinationIp, srcPort);
					session.setPassword(password);
					session.setConfig(config);
					session.connect();
					System.err.println("--------------------------------------------------------------");
					System.err.println("client " + tag + " connected");
					System.err.println("--------------------------------------------------------------");
					Thread.sleep(10000);
					while (!completed.contains(tag)) {
						final Channel channel = session.openChannel("shell");
						System.err.println("--------------------------------------------------------------");
						System.err.println("client test " + tag + " sent to beacon server " + value);
						System.err.println("--------------------------------------------------------------");
						final String command1 = "echo DATA:$(( " + value + "+1 ))";
						final byte[] buf = command1.getBytes();
						final InputStream inCommand = new ByteArrayInputStream(buf);
						((ChannelShell) channel).setInputStream(inCommand);
						final InputStream in = channel.getInputStream();
						channel.connect();
						final String result = IOUtils.toString(in, Charset.defaultCharset());
						System.out.println(result);
						for (final String dataTest : result.split("\n")) {
							if (dataTest.contains("DATA:")) {
								value = Integer.valueOf(dataTest.replace("DATA:", ""));
								updateClientCounter(tag, value);
								System.err.println("--------------------------------------------------------------");
								System.err.println("client test " + tag + " received from beacon server " + value);
								System.err.println("--------------------------------------------------------------");
								break;
							}
						}
						channel.disconnect();
					}
					session.disconnect();
				} catch (final Exception e) {
					e.printStackTrace();
				}
				return true;
			}

		};
	}

	public Callable<Boolean> createClientRunnerTcp(String destinationIp, int srcPort, String tag) {
		return new Callable<Boolean>() {
			private int last = 0;

			@Override
			public Boolean call() throws Exception {
				final SocketAddress endpoint = new InetSocketAddress(destinationIp, srcPort);
				final Socket socketClient = new Socket();
				socketClient.connect(endpoint, 60000);
				socketClient.setKeepAlive(true);
				final InputStreamReader reader = new InputStreamReader(socketClient.getInputStream());
				final PrintWriter w = new PrintWriter(socketClient.getOutputStream(), true);
				last = 1;
				if (basePayload != null) {
					final String string = basePayload + String.valueOf(last);
					System.out.println("*********** string sent\n" + string);
					w.write(string);
				} else {
					w.write(String.valueOf(last));
				}
				w.flush();
				System.out.println("--------------------------------------------------------------");
				System.out.println("client test " + tag + " sent to beacon server " + last);
				System.out.println("--------------------------------------------------------------");
				try {
					while (!completed.contains(tag)) {
						if (reader.ready()) {
							final StringBuilder sb = new StringBuilder();
							while (reader.ready()) {
								sb.append((char) reader.read());
							}
							final String nextLine = sb.toString();
							System.out.println("*********** string received\n" + nextLine);
							int valueNew = 0;
							if (basePayload != null) {
								valueNew = Integer.valueOf(nextLine.replace(basePayload, ""));
							} else {
								valueNew = Integer.valueOf(nextLine);
							}
							System.out.println("--------------------------------------------------------------");
							System.out.println("client test " + tag + " received from beacon server " + valueNew);
							System.out.println("--------------------------------------------------------------");
							final boolean toClose = updateClientCounter(tag, valueNew);
							if (!toClose) {
								if (last == 0)
									last = valueNew + 1;
								else {
									if (last + 1 != valueNew) {
										throw new Exception("error in client test " + tag + " last cached:" + last
												+ ", new:" + valueNew);
									} else {
										last = valueNew + 1;
									}
								}
								Thread.sleep(1000);
								if (basePayload != null) {
									final String string = basePayload + String.valueOf(last);
									System.out.println("*********** string sent\n" + string);
									w.write(string);
								} else {
									w.write(String.valueOf(last));
								}
								w.flush();
							} else {
								System.out.println("*********** close Socket beacause toClose " + toClose);
								socketClient.close();
							}
							System.out.println("--------------------------------------------------------------");
							System.out.println("client test " + tag + " sent to server beacon " + last);
							System.out.println("--------------------------------------------------------------");
						}
					}
				} catch (final InterruptedException f) {
					socketClient.close();
					System.out.println("client closed beacause " + EdgeLogger.stackTraceToString(f));
				} catch (final Exception a) {
					socketClient.close();
					System.out.println("client closed " + EdgeLogger.stackTraceToString(a));
					a.printStackTrace();
				}
				// socketClient.close();
				return true;
			}

		};
	}

	protected boolean updateClientCounter(String tag, int valueNew) {
		System.out.println("tag: " + tag + " newValue: " + valueNew + " actualSize: " + actualTestSize);
		try {
			System.out.println("package counter " + tag + " -> " + String.valueOf(valueNew) + " [R]:"
					+ networkTunnel.getPacketReceived() + " [S]:" + networkTunnel.getPacketSend() + " [E]:"
					+ networkTunnel.getPacketError() + " [W]:" + networkTunnel.getWaitingPackagesCount());
		} catch (final Exception a) {
			// fallisce nei test preparatori
			System.out.println("package counter " + EdgeLogger.stackTraceToString(a));
		}
		if (valueNew > actualTestSize) {
			completed.add(tag);
			switch (tag) {
			case "A":
				clientTCP1.cancel(false);
				break;
			case "B":
				clientTCP2.cancel(false);
				break;
			case "C":
				clientTCP3.cancel(false);
				break;

			case "D":
				clientTCP4.cancel(false);
				break;

			case "E":
				clientTCP5.cancel(false);
				break;
			case "F":
				clientTCP6.cancel(false);
				break;
			case "G":
				clientTCP7.cancel(false);
				break;
			case "H":
				clientTCP8.cancel(false);
				break;
			case "I":
				clientTCP9.cancel(false);
				break;
			case "L":
				clientTCP10.cancel(false);
				break;
			case "M":
				clientTCP11.cancel(false);
				break;
			case "N":
				clientTCP12.cancel(false);
				break;
			case "O":
				clientTCP13.cancel(false);
				break;
			case "P":
				clientTCP14.cancel(false);
				break;
			case "Q":
				clientTCP15.cancel(false);
				break;
			case "R":
				clientTCP16.cancel(false);
				break;
			case "S":
				clientTCP17.cancel(false);
				break;
			}
			System.out.println(
					"COMPLETED!!!! - tag: " + tag + " newValue: " + valueNew + " actualSize: " + actualTestSize);
			return true;
		} else
			System.out
					.println("continue with tag: " + tag + " newValue: " + valueNew + " actualSize: " + actualTestSize);
		return false;

	}

	private final class ServerTcpInitHandler extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel serverSocketChannel) throws Exception {
			final ServerTCPHandler serverTCPSocketChannelHandler = new ServerTCPHandler();
			serverSocketChannel.pipeline().addLast(serverTCPSocketChannelHandler);
		}
	}

	private final class ServerTCPHandler extends SimpleChannelInboundHandler<ByteBuf> implements AutoCloseable {

		@Override
		public void close() throws Exception {
			System.out.println("server test close thread");
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, ByteBuf s) throws Exception {
			final String nextLine = new String(ByteBufUtil.getBytes(s));
			System.out.println("*********** string received\n" + nextLine);
			int valueNew = 0;
			if (basePayload != null) {
				valueNew = Integer.valueOf(nextLine.replace(basePayload, ""));
			} else {
				valueNew = Integer.valueOf(nextLine);
			}
			System.out.println("--------------------------------------------------------------");
			System.out.println("server test received from beacon client " + ctx.name() + " -> " + valueNew);
			System.out.println("--------------------------------------------------------------");
			Thread.sleep(1000);
			final int reply = valueNew + 1;
			if (reply > (actualTestSize + 2)) {
				ctx.close();
				System.out.println("--------------------------------------------------------------");
				System.out.println("server test " + ctx.name() + " close on " + reply);
				System.out.println("--------------------------------------------------------------");
			} else {
				if (basePayload != null) {
					final String stringOut = basePayload + String.valueOf(reply);
					System.out.println("*********** string sent\n" + stringOut);
					ctx.writeAndFlush(Unpooled.wrappedBuffer(stringOut.getBytes()));
				} else {
					ctx.writeAndFlush(Unpooled.wrappedBuffer(String.valueOf(reply).getBytes()));
				}
				System.out.println("--------------------------------------------------------------");
				System.out.println("server test sent to beacon client " + ctx.name() + " -> " + reply);
				System.out.println("--------------------------------------------------------------");
			}
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			System.out.println("-------------------------- server test channelActive");
			super.channelActive(ctx);
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			System.out.println("-------------------------- server test channelInactive");
			super.channelInactive(ctx);
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			System.out.println("-------------------------- server test channelReadComplete");
			super.channelReadComplete(ctx);
		}

		@Override
		public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
			System.out.println("-------------------------- server test channelRegistered");
			super.channelRegistered(ctx);
		}

		@Override
		public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
			System.out.println("-------------------------- server test channelUnregistered");
			super.channelUnregistered(ctx);
		}

		@Override
		public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
			System.out.println("-------------------------- server test channelWritabilityChanged");
			super.channelWritabilityChanged(ctx);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			System.out.println("-------------------------- server test exceptionCaught " + cause.getMessage());
			super.exceptionCaught(ctx, cause);
		}

		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			System.out.println("-------------------------- server test userEventTriggered " + evt);
			super.userEventTriggered(ctx, evt);
		}

	}

	private static final String getAlphaNumericString(int n) {
		if (n > 0) {
			final String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
			final StringBuilder sb = new StringBuilder(n);
			for (int i = 0; i < n; i++) {
				final int index = (int) (alphaNumericString.length() * Math.random());

				sb.append(alphaNumericString.charAt(index));
			}
			return sb.toString();
		} else {
			return null;
		}
	}
}
