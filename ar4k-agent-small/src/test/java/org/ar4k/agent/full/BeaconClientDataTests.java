package org.ar4k.agent.full;

import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
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

import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.console.Ar4kAgent;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.data.DataAddressAnima;
import org.ar4k.agent.helper.ContextCreationHelper;
import org.ar4k.agent.keystore.KeystoreLoader;
import org.ar4k.agent.network.NetworkConfig;
import org.ar4k.agent.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.network.NetworkConfig.NetworkProtocol;
import org.ar4k.agent.network.NetworkTunnel;
import org.ar4k.agent.tunnels.http.beacon.BeaconServiceConfig;
import org.ar4k.agent.tunnels.http.beacon.socket.BeaconNetworkConfig;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.Command;
import org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class BeaconClientDataTests {

	private static final String CLIENT1_LABEL = "client1";
	private static final String CLIENT2_LABEL = "client2";
	private static final String SERVER_LABEL = "server";
	private final ExecutorService executor = Executors.newCachedThreadPool();
	private final Map<String, Anima> testAnimas = new HashMap<>();
	private final File keyStoreServer = new File("./tmp/server.ks");
	private final File keyStoreClient1 = new File("./tmp/client1.ks");
	private final File keyStoreClient2 = new File("./tmp/client2.ks");
	private final String serverAliasInKeystore = "server";
	private final String client2AliasInKeystore = "client2";
	private final String client1AliasInKeystore = "client1";
	private final String passwordKs = "password";

	@Before
	public void before() throws Exception {
		deleteDir(new File("./tmp"));
		deleteDir(new File("./tmp1"));
		deleteDir(new File("./tmp2"));
		deleteDir(new File("./tmp3"));
		deleteDir(new File("~/.ar4k"));

		Files.createDirectories(Paths.get("./tmp"));
		KeystoreLoader.create(serverAliasInKeystore, keyStoreServer.getAbsolutePath(), passwordKs);
		KeystoreLoader.create(client2AliasInKeystore, keyStoreClient1.getAbsolutePath(), passwordKs);
		KeystoreLoader.create(client1AliasInKeystore, keyStoreClient2.getAbsolutePath(), passwordKs);
		/*
		 * try { File keyStoreMaster =
		 * ResourceUtils.getFile("classpath:test-beacon.tkeystore");
		 * FileUtils.copyFile(keyStoreMaster, keyStoreServer);
		 * FileUtils.copyFile(keyStoreMaster, keyStoreClient1);
		 * FileUtils.copyFile(keyStoreMaster, keyStoreClient2); } catch
		 * (FileNotFoundException e) { e.printStackTrace(); }
		 */
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
		for (final Anima a : testAnimas.values()) {
			a.close();
		}
		Files.deleteIfExists(Paths.get("./tmp/test.config.base64.ar4k"));
		Files.deleteIfExists(keyStoreServer.toPath());
		Files.deleteIfExists(keyStoreClient1.toPath());
		Files.deleteIfExists(keyStoreClient2.toPath());
		if (executor != null) {
			executor.shutdownNow();
			executor.awaitTermination(1, TimeUnit.MINUTES);
		}
		deleteDir(new File("./tmp"));
		deleteDir(new File("./tmp1"));
		deleteDir(new File("./tmp2"));
		deleteDir(new File("./tmp3"));
		deleteDir(new File("~/.ar4k"));
	}

	@Test
	@Ignore
	public void base3AnimasWithDiscovery() throws Exception {
		final List<String> baseArgs = new ArrayList<>();
		baseArgs.add("--spring.shell.command.quit.enabled=false");
		baseArgs.add("--logging.level.root=INFO");
		baseArgs.add("--ar4k.confPath=./tmp");
		baseArgs.add("--ar4k.fileConfig=./tmp/test.config.base64.ar4k");
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
		final Ar4kConfig serverConfig = new Ar4kConfig();
		serverConfig.name = "server-beacon";
		serverConfig.beaconServer = null;
		serverConfig.beaconDiscoveryPort = 0;
		final BeaconServiceConfig beaconServiceConfig = new BeaconServiceConfig();
		beaconServiceConfig.discoveryPort = 33667;
		beaconServiceConfig.port = 22116;
		beaconServiceConfig.aliasBeaconServerInKeystore = serverAliasInKeystore;
		beaconServiceConfig.stringDiscovery = "TEST-REGISTER";
		serverConfig.pots.add(beaconServiceConfig);
		final Ar4kConfig config2 = null;
		final Ar4kConfig config1 = null;

		testAnimas.put(SERVER_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "a.log",
						keyStoreServer.getAbsolutePath(), 1124, baseArgs, serverConfig, serverAliasInKeystore,
						serverAliasInKeystore, "https://127.0.0.1:22116")).get());
		testAnimas.put(CLIENT2_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "b.log",
						keyStoreClient2.getAbsolutePath(), 1125, baseArgs, config2, client2AliasInKeystore,
						client2AliasInKeystore, "https://127.0.0.1:22116")).get());
		testAnimas.put(CLIENT1_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "c.log",
						keyStoreClient1.getAbsolutePath(), 1126, baseArgs, config1, client1AliasInKeystore,
						client1AliasInKeystore, "https://127.0.0.1:22116")).get());
		Thread.sleep(20000);
		for (final Anima a : testAnimas.values()) {
			final String animaName = a.getRuntimeConfig() != null ? a.getRuntimeConfig().getName() : "no-config";
			if (animaName.equals("server-beacon")) {
				Assert.assertEquals(a.getState(), Anima.AnimaStates.RUNNING);
			} else {
				Assert.assertEquals(a.getState(), Anima.AnimaStates.STAMINAL);
			}
		}
		Thread.sleep(1000 * 60 * 1);
	}

	@Test
	@Ignore
	public void oneServerAsClient() throws Exception {
		final List<String> baseArgs = new ArrayList<>();
		baseArgs.add("--spring.shell.command.quit.enabled=false");
		baseArgs.add("--logging.level.root=INFO");
		baseArgs.add("--ar4k.confPath=./tmp");
		baseArgs.add("--ar4k.fileConfig=./tmp/test.config.base64.ar4k");
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
		final Ar4kConfig serverConfig = new Ar4kConfig();
		serverConfig.name = "server-beacon";
		serverConfig.beaconServer = null;
		serverConfig.beaconDiscoveryPort = 0;
		final BeaconServiceConfig beaconServiceConfig = new BeaconServiceConfig();
		beaconServiceConfig.discoveryPort = 33667;
		beaconServiceConfig.port = 22116;
		beaconServiceConfig.aliasBeaconServerInKeystore = serverAliasInKeystore;
		beaconServiceConfig.stringDiscovery = "TEST-REGISTER";
		serverConfig.pots.add(beaconServiceConfig);

		testAnimas.put(SERVER_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "a.log",
						keyStoreServer.getAbsolutePath(), 1124, baseArgs, serverConfig, serverAliasInKeystore,
						serverAliasInKeystore, "https://127.0.0.1:22116")).get());
		Thread.sleep(20000);
		for (final Anima a : testAnimas.values()) {
			final String animaName = a.getRuntimeConfig() != null ? a.getRuntimeConfig().getName() : "no-config";
			if (animaName.equals("server-beacon")) {
				Assert.assertEquals(a.getState(), Anima.AnimaStates.RUNNING);
			} else {
				Assert.assertEquals(a.getState(), Anima.AnimaStates.STAMINAL);
			}
		}
		Thread.sleep(3000);
		final List<Agent> agents = testAnimas.get(SERVER_LABEL).getBeaconClient().listAgentsConnectedToBeacon();
		String agentToQuery = null;
		for (final Agent a : agents) {
			System.out.println("agent found by test -> " + a.getAgentUniqueName());
			agentToQuery = a.getAgentUniqueName();
		}
		final ListCommandsReply commands = testAnimas.get(SERVER_LABEL).getBeaconClient()
				.listCommadsOnAgent(agentToQuery);
		for (final Command c : commands.getCommandsList()) {
			System.out.println(c.getCommand());
		}
		System.out.println(testAnimas.get(SERVER_LABEL).getBeaconClient().runCommadsOnAgent(agentToQuery, "help"));
		System.out.println(
				testAnimas.get(SERVER_LABEL).getBeaconClient().runCommadsOnAgent(agentToQuery, "get-threads-info"));
		Thread.sleep(1000 * 60 * 1);
	}

	@Test
	public void oneServerAsClientSocketTestLeftNoSsl() throws Exception {
		oneServerAsClientSocketTestLeft(false);
	}

	@Test
	@Ignore
	public void oneServerAsClientSocketTestLeftSsl() throws Exception {
		oneServerAsClientSocketTestLeft(true);
	}

	private void oneServerAsClientSocketTestLeft(boolean ssl) throws Exception {
		final List<String> baseArgs = new ArrayList<>();
		if (ssl) {
			baseArgs.add("--ar4k.beaconClearText=false");
		}
		baseArgs.add("--spring.shell.command.quit.enabled=false");
		baseArgs.add("--logging.level.root=INFO");
		baseArgs.add("--ar4k.confPath=./tmp");
		baseArgs.add("--ar4k.fileConfig=./tmp/test.config.base64.ar4k");
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
		final Ar4kConfig serverConfig = new Ar4kConfig();
		serverConfig.name = "server-beacon";
		serverConfig.beaconServer = null;
		serverConfig.beaconDiscoveryPort = 0;
		final BeaconServiceConfig beaconServiceConfig = new BeaconServiceConfig();
		beaconServiceConfig.discoveryPort = 33667;
		beaconServiceConfig.port = 22116;
		beaconServiceConfig.aliasBeaconServerInKeystore = serverAliasInKeystore;
		beaconServiceConfig.stringDiscovery = "TEST-REGISTER";
		serverConfig.pots.add(beaconServiceConfig);

		testAnimas.put(SERVER_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "a.log",
						keyStoreServer.getAbsolutePath(), 1124, baseArgs, serverConfig, serverAliasInKeystore,
						serverAliasInKeystore, "https://127.0.0.1:22116")).get());
		Thread.sleep(15000);
		for (final Anima a : testAnimas.values()) {
			final String animaName = a.getRuntimeConfig() != null ? a.getRuntimeConfig().getName() : "no-config";
			if (animaName.equals("server-beacon")) {
				Assert.assertEquals(a.getState(), Anima.AnimaStates.RUNNING);
			} else {
				Assert.assertEquals(a.getState(), Anima.AnimaStates.STAMINAL);
			}
		}
		Thread.sleep(5000);
		final List<Agent> agents = testAnimas.get(SERVER_LABEL).getBeaconClient().listAgentsConnectedToBeacon();
		String agentToQuery = null;
		for (final Agent a : agents) {
			System.out.println("agent found by test -> " + a.getAgentUniqueName());
			agentToQuery = a.getAgentUniqueName();
		}
		final DataAddressAnima targetDataAddress = testAnimas.get(SERVER_LABEL).getDataAddress();
		System.out.println("OBJECT: " + targetDataAddress);
		System.out.println("listChannels: " + targetDataAddress.listChannels());
		System.out.println("getDataChannels: " + targetDataAddress.getDataChannels());
		System.out.println("getDefaultScope: " + targetDataAddress.getDefaultScope());
		System.out.println("getKnowTags: " + targetDataAddress.getKnowTags());
		Thread.sleep(20000);
	}

	public Callable<Boolean> createClientRunner(String destinationIp, int srcPort) {
		return new Callable<Boolean>() {
			private int last = 0;

			@Override
			public Boolean call() throws Exception {
				final SocketAddress endpoint = new InetSocketAddress(destinationIp, srcPort);
				@SuppressWarnings("resource")
				final Socket socketClient = new Socket();
				socketClient.connect(endpoint, 60000);
				socketClient.setKeepAlive(true);
				final InputStreamReader reader = new InputStreamReader(socketClient.getInputStream());
				final PrintWriter w = new PrintWriter(socketClient.getOutputStream(), true);
				w.write(1);
				w.flush();
				last = 1;
				System.out.println("client test sent to beacon server " + last);
				try {
					while (!completed) {
						while (reader.ready()) {
							final int valueNew = reader.read();
							System.out.println("client test received from beacon server " + valueNew);
							updateClientCounter(valueNew);
							if (last == 0)
								last = valueNew + 1;
							else {
								if (last + 1 != valueNew) {
									throw new Exception(
											"error in client test last cached:" + last + ",new:" + valueNew);
								} else {
									last = valueNew + 1;
								}
							}
							Thread.sleep(1000);
							w.write(last);
							w.flush();
							System.out.println("client test sent to server beacon " + last);
						}
					}
				} catch (final InterruptedException f) {
					socketClient.close();
					System.out.println("client closed");
				} catch (final Exception a) {
					socketClient.close();
					System.out.println("client closed");
					a.printStackTrace();
				}
				socketClient.close();
				return true;
			}
		};
	}

	protected void updateClientCounter(int valueNew) {
		// System.out.println("counter: " + valueNew);
		if (valueNew > 40) {
			completed = true;
			clientTCP.cancel(true);
			serverTCP.cancel(true);
			System.out.println("package counter [R]:" + networkTunnel.getNetworkReceiver().getPacketReceived() + " [S]:"
					+ networkTunnel.getNetworkReceiver().getPacketSend());
		}
	}

	NetworkTunnel networkTunnel = null;
	Future<Boolean> serverTCP = null;
	Future<Boolean> clientTCP = null;
	boolean completed = false;

	@Test
	@Ignore
	public void oneServerAsClientTestLocalSsh() throws Exception {
		final List<String> baseArgs = new ArrayList<>();
		baseArgs.add("--spring.shell.command.quit.enabled=false");
		baseArgs.add("--logging.level.root=INFO");
		baseArgs.add("--ar4k.confPath=./tmp");
		baseArgs.add("--ar4k.fileConfig=./tmp/test.config.base64.ar4k");
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
		final Ar4kConfig serverConfig = new Ar4kConfig();
		serverConfig.name = "server-beacon";
		serverConfig.beaconServer = null;
		serverConfig.beaconDiscoveryPort = 0;
		final BeaconServiceConfig beaconServiceConfig = new BeaconServiceConfig();
		beaconServiceConfig.discoveryPort = 33667;
		beaconServiceConfig.port = 22116;
		beaconServiceConfig.aliasBeaconServerInKeystore = serverAliasInKeystore;
		beaconServiceConfig.stringDiscovery = "TEST-REGISTER";
		serverConfig.pots.add(beaconServiceConfig);

		testAnimas.put(SERVER_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "a.log",
						keyStoreServer.getAbsolutePath(), 1124, baseArgs, serverConfig, serverAliasInKeystore,
						serverAliasInKeystore, "https://127.0.0.1:22116")).get());
		Thread.sleep(15000);
		for (final Anima a : testAnimas.values()) {
			final String animaName = a.getRuntimeConfig() != null ? a.getRuntimeConfig().getName() : "no-config";
			if (animaName.equals("server-beacon")) {
				Assert.assertEquals(a.getState(), Anima.AnimaStates.RUNNING);
			} else {
				Assert.assertEquals(a.getState(), Anima.AnimaStates.STAMINAL);
			}
		}
		Thread.sleep(5000);
		final List<Agent> agents = testAnimas.get(SERVER_LABEL).getBeaconClient().listAgentsConnectedToBeacon();
		String agentToQuery = null;
		for (final Agent a : agents) {
			System.out.println("agent found by test -> " + a.getAgentUniqueName());
			agentToQuery = a.getAgentUniqueName();
		}
		final String destinationIp = "127.0.0.1";
		final int destinationPort = 22;
		final int srcPort = 8822;
		// codice
		final NetworkConfig config = new BeaconNetworkConfig("tunnel-test", "tunnel in fase di test",
				NetworkMode.CLIENT, NetworkProtocol.TCP, destinationIp, destinationPort, srcPort);
		networkTunnel = testAnimas.get(SERVER_LABEL).getBeaconClient().getNetworkTunnel(agentToQuery, config);
		System.out.println("network tunnel status -> " + networkTunnel.getNetworkReceiver().getStatus());
		System.out.println("Try to connect to:\nssh localhost -p " + srcPort);
		Thread.sleep(120000);
		System.out.println("package counter [R]:" + networkTunnel.getNetworkReceiver().getPacketReceived() + " [S]:"
				+ networkTunnel.getNetworkReceiver().getPacketSend());
	}
}
