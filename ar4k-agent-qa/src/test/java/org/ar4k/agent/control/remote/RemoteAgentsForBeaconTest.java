package org.ar4k.agent.control.remote;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.config.network.NetworkConfig;
import org.ar4k.agent.config.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.config.network.NetworkConfig.NetworkProtocol;
import org.ar4k.agent.config.network.NetworkTunnel;
import org.ar4k.agent.console.Ar4kAgent;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.helper.ContextCreationHelper;
import org.ar4k.agent.keystore.KeystoreLoader;
import org.ar4k.agent.tunnels.http2.beacon.BeaconServiceConfig;
import org.ar4k.agent.tunnels.http2.beacon.socket.netty.BeaconNettyNetworkConfig;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http2.grpc.beacon.AgentRequest;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class RemoteAgentsForBeaconTest {
	private static final int VALIDITY_CERT_DAYS = 365 * 3;
	private static final String CLIENT1_LABEL = "client1";
	private static final String CLIENT2_LABEL = "client2";
	private static final String SERVER_LABEL = "server";
	private final ExecutorService executor = Executors.newCachedThreadPool();
	private final Map<String, Homunculus> testAnimas = new HashMap<>();
	private final File keyStoreMaster = new File("./tmp/master.ks");
	private final File keyStoreServer = new File("./tmp/server.ks");
	private final File keyStoreClient1 = new File("./tmp/client1.ks");
	private final File keyStoreClient2 = new File("./tmp/client2.ks");
	private final String masterAliasInKeystore = "master";
	private final String serverAliasInKeystore = "server";
	private final String client2AliasInKeystore = "client2";
	private final String client1AliasInKeystore = "client1";
	private final String signServerAliasInKeystore = "server-sign";
	private final String signClient2AliasInKeystore = "client2-sign";
	private final String signClient1AliasInKeystore = "client1-sign";
	private final String passwordKs = "password";

	private NetworkTunnel networkTunnel = null;
	private Future<Boolean> serverTCP = null;
	private Future<Boolean> clientTCP = null;
	private boolean completed = false;

	private void deleteDir(File dir) {
		final File[] files = dir.listFiles();
		if (files != null) {
			for (final File file : files) {
				deleteDir(file);
			}
		}
		dir.delete();
	}

	@Before
	public void before() throws Exception {
		deleteDir(new File("./tmp"));
		deleteDir(new File("./tmp1"));
		deleteDir(new File("./tmp2"));
		deleteDir(new File("./tmp3"));
		deleteDir(new File("~/.ar4k"));
		Files.createDirectories(Paths.get("./tmp"));
		KeystoreLoader.createSelfSignedCert("ca", "Rossonet", "TEST UNIT", "IMOLA", "BOLOGNA", "IT",
				"urn:org.ar4k.agent:ca", "*.ar4k.net", "127.0.0.1", masterAliasInKeystore,
				keyStoreMaster.getAbsolutePath(), passwordKs, true, VALIDITY_CERT_DAYS);
		KeystoreLoader.create("server", "Rossonet", "TEST UNIT S", "IMOLA", "BOLOGNA", "IT",
				"urn:org.ar4k.agent:server-test-agent", "*.ar4k.net", "127.0.0.1", serverAliasInKeystore,
				keyStoreServer.getAbsolutePath(), passwordKs, false, VALIDITY_CERT_DAYS);
		KeystoreLoader.create("client2", "Rossonet", "TEST UNIT C2", "IMOLA", "BOLOGNA", "IT",
				"urn:org.ar4k.agent:client2-test-agent", "*.ar4k.net", "127.0.0.1", client2AliasInKeystore,
				keyStoreClient2.getAbsolutePath(), passwordKs, false, VALIDITY_CERT_DAYS);
		KeystoreLoader.create("client1", "Rossonet", "TEST UNIT C1", "IMOLA", "BOLOGNA", "IT",
				"urn:org.ar4k.agent:client1-test-agent", "*.ar4k.net", "127.0.0.1", client1AliasInKeystore,
				keyStoreClient1.getAbsolutePath(), passwordKs, false, VALIDITY_CERT_DAYS);
		final PKCS10CertificationRequest csrServer = KeystoreLoader.getPKCS10CertificationRequest(serverAliasInKeystore,
				keyStoreServer.getAbsolutePath(), passwordKs);
		final JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
		final PublicKey pubKeyServer = converter.getPublicKey(csrServer.getSubjectPublicKeyInfo());
		writeCSr("./tmp/csr-server.pem", Base64.getEncoder().encodeToString(csrServer.getEncoded()));
		System.out.println("\nCSR SERVER\n" + pubKeyServer);
		KeystoreLoader.signCertificate(csrServer, signServerAliasInKeystore, 100, masterAliasInKeystore,
				keyStoreMaster.getAbsolutePath(), passwordKs);
		final String crtServer = KeystoreLoader.getCertCaAsPem(signServerAliasInKeystore,
				keyStoreMaster.getAbsolutePath(), passwordKs);
		final String keyServer = KeystoreLoader.getPrivateKeyBase64(serverAliasInKeystore,
				keyStoreServer.getAbsolutePath(), passwordKs);
		KeystoreLoader.setClientKeyPair(keyServer, crtServer, signServerAliasInKeystore,
				keyStoreServer.getAbsolutePath(), passwordKs);
		System.out.println(
				"\n\nLIST MASTER " + KeystoreLoader.listCertificate(keyStoreMaster.getAbsolutePath(), passwordKs));
		System.out.println(
				"\n\nLIST SERVER " + KeystoreLoader.listCertificate(keyStoreServer.getAbsolutePath(), passwordKs));
		System.out.println(
				"\n\nLIST CLIENT 1 " + KeystoreLoader.listCertificate(keyStoreClient1.getAbsolutePath(), passwordKs));
		System.out.println(
				"\n\nLIST CLIENT 2 " + KeystoreLoader.listCertificate(keyStoreClient2.getAbsolutePath(), passwordKs));
	}

	@After
	public void tearDown() throws Exception {
		System.err.println("\n\nEND TESTS\n\n");
		for (final Homunculus a : testAnimas.values()) {
			a.close();
		}
		Files.deleteIfExists(Paths.get("./tmp/test-server.config.base64.ar4k"));
		Files.deleteIfExists(Paths.get("./tmp/test-client1.config.base64.ar4k"));
		Files.deleteIfExists(Paths.get("./tmp/test-client2.config.base64.ar4k"));
		Files.deleteIfExists(keyStoreMaster.toPath());
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

	private void writeCSr(String path, String cert) throws UnrecoverableKeyException, NoSuchAlgorithmException,
			CertificateException, KeyStoreException, IOException {
		final FileWriter writer = new FileWriter(new File(path));
		writer.write("-----BEGIN CERTIFICATE REQUEST-----\n");
		writer.write(cert);
		writer.write("\n-----END CERTIFICATE REQUEST-----\n");
		writer.close();
	}

	@Test
	@Ignore
	public void clientsForBeaconTests() throws Exception {
		allNodeSimulatedWithTunnel(true);
	}

	private void allNodeSimulatedWithTunnel(boolean ssl) throws Exception {
		final List<String> baseArgs = new ArrayList<>();
		final List<String> baseArgsClientOne = new ArrayList<>();
		final List<String> baseArgsClientTwo = new ArrayList<>();
		String certCaAsPem = "";
		if (ssl) {
			baseArgs.add("--ar4k.beaconClearText=false");
			baseArgsClientOne.add("--ar4k.beaconClearText=false");
			baseArgsClientTwo.add("--ar4k.beaconClearText=false");
			certCaAsPem = KeystoreLoader.getCertCaAsPem(signServerAliasInKeystore, keyStoreServer.getAbsolutePath(),
					passwordKs);
			final byte[] decodedCrt = Base64.getDecoder().decode(certCaAsPem);
			final X509Certificate clientCertificate = (X509Certificate) CertificateFactory.getInstance("X.509")
					.generateCertificate(new ByteArrayInputStream(decodedCrt));
			certCaAsPem = KeystoreLoader.getCertCaAsPem(masterAliasInKeystore, keyStoreMaster.getAbsolutePath(),
					passwordKs) + "," + certCaAsPem;
			System.out.println("\n\nCA Master\n" + certCaAsPem);
			System.out.println(clientCertificate);
		}

		baseArgs.add("--ar4k.consoleOnly=false");
		baseArgs.add("--spring.shell.command.quit.enabled=false");
		baseArgs.add("--logging.level.root=INFO");
		baseArgs.add("--ar4k.confPath=./tmp1");
		baseArgs.add("--ar4k.fileConfig=./tmp1/test-server.config.base64.ar4k");
		baseArgs.add("--ar4k.webConfig=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
		baseArgs.add("--ar4k.dnsConfig=demo1.rossonet.name");

		baseArgsClientOne.add("--ar4k.consoleOnly=false");
		baseArgsClientOne.add("--spring.shell.command.quit.enabled=false");
		baseArgsClientOne.add("--logging.level.root=INFO");
		baseArgsClientOne.add("--ar4k.confPath=./tmp2");
		baseArgsClientOne.add("--ar4k.fileConfig=./tmp2/test-client1.config.base64.ar4k");
		baseArgsClientOne
				.add("--ar4k.webConfig=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
		baseArgsClientOne.add("--ar4k.dnsConfig=demo1.rossonet.name");

		baseArgsClientTwo.add("--ar4k.consoleOnly=false");
		baseArgsClientTwo.add("--spring.shell.command.quit.enabled=false");
		baseArgsClientTwo.add("--logging.level.root=INFO");
		baseArgsClientTwo.add("--ar4k.confPath=./tmp3");
		baseArgsClientTwo.add("--ar4k.fileConfig=./tmp3/test-client2.config.base64.ar4k");
		baseArgsClientTwo
				.add("--ar4k.webConfig=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
		baseArgsClientTwo.add("--ar4k.dnsConfig=demo1.rossonet.name");
//    addArgs.add("--ar4k.baseConfig=");
		baseArgs.add("--ar4k.webKeystore=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
		baseArgs.add("--ar4k.dnsKeystore=ks1.rossonet.name");

		baseArgsClientOne
				.add("--ar4k.webKeystore=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
		baseArgsClientOne.add("--ar4k.dnsKeystore=ks1.rossonet.name");

		baseArgsClientTwo
				.add("--ar4k.webKeystore=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
		baseArgsClientTwo.add("--ar4k.dnsKeystore=ks1.rossonet.name");

		// baseArgs.add("--ar4k.keystoreMainAlias=");
		baseArgs.add("--ar4k.keystorePassword=" + passwordKs);
		baseArgs.add("--ar4k.beaconCaChainPem=" + certCaAsPem);
		baseArgs.add("--ar4k.adminPassword=password");

		baseArgsClientOne.add("--ar4k.keystorePassword=" + passwordKs);
		baseArgsClientOne.add("--ar4k.beaconCaChainPem=" + certCaAsPem);
		baseArgsClientOne.add("--ar4k.adminPassword=password");

		baseArgsClientTwo.add("--ar4k.keystorePassword=" + passwordKs);
		baseArgsClientTwo.add("--ar4k.beaconCaChainPem=" + certCaAsPem);
		baseArgsClientTwo.add("--ar4k.adminPassword=password");
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

		baseArgsClientOne.add("--ar4k.beaconDiscoveryFilterString=TEST-REGISTER");
		baseArgsClientOne.add("--ar4k.beaconDiscoveryPort=33667");
		baseArgsClientOne.add("--ar4k.fileConfigOrder=1");
		baseArgsClientOne.add("--ar4k.webConfigOrder=2");
		baseArgsClientOne.add("--ar4k.dnsConfigOrder=3");
		baseArgsClientOne.add("--ar4k.baseConfigOrder=0");
		baseArgsClientOne.add("--ar4k.threadSleep=1000");
		baseArgsClientOne.add("--ar4k.logoUrl=/static/img/ar4k.png");

		baseArgsClientTwo.add("--ar4k.beaconDiscoveryFilterString=TEST-REGISTER");
		baseArgsClientTwo.add("--ar4k.beaconDiscoveryPort=33667");
		baseArgsClientTwo.add("--ar4k.fileConfigOrder=1");
		baseArgsClientTwo.add("--ar4k.webConfigOrder=2");
		baseArgsClientTwo.add("--ar4k.dnsConfigOrder=3");
		baseArgsClientTwo.add("--ar4k.baseConfigOrder=0");
		baseArgsClientTwo.add("--ar4k.threadSleep=1000");
		baseArgsClientTwo.add("--ar4k.logoUrl=/static/img/ar4k.png");

		final EdgeConfig clientOneConfig = new EdgeConfig();
		final EdgeConfig clientTwoConfig = new EdgeConfig();
		final EdgeConfig serverConfig = new EdgeConfig();
		serverConfig.name = "server-beacon";
		serverConfig.beaconDiscoveryPort = 0;
		serverConfig.beaconServerCertChain = certCaAsPem;
		clientOneConfig.beaconServerCertChain = certCaAsPem;
		clientTwoConfig.beaconServerCertChain = certCaAsPem;
		final BeaconServiceConfig beaconServiceConfig = new BeaconServiceConfig();
		beaconServiceConfig.discoveryPort = 33667;
		beaconServiceConfig.port = 32676;
		beaconServiceConfig.aliasBeaconServerInKeystore = masterAliasInKeystore;
		beaconServiceConfig.caChainPem = certCaAsPem;
		beaconServiceConfig.acceptAllCerts = false;
		beaconServiceConfig.stringDiscovery = "TEST-REGISTER";
		serverConfig.pots.add(beaconServiceConfig);

		testAnimas.put(SERVER_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "a.log",
						keyStoreMaster.getAbsolutePath(), 1124, baseArgs, serverConfig, masterAliasInKeystore,
						masterAliasInKeystore, "https://localhost:32676")).get());
		testAnimas.put(CLIENT2_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "b.log",
						keyStoreClient2.getAbsolutePath(), 1125, baseArgsClientTwo, clientTwoConfig,
						client2AliasInKeystore, signClient2AliasInKeystore, "https://localhost:32676")).get());
		testAnimas.put(CLIENT1_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "c.log",
						keyStoreClient1.getAbsolutePath(), 1126, baseArgsClientOne, clientOneConfig,
						client1AliasInKeystore, signClient1AliasInKeystore, "https://localhost:32676")).get());
		for (final Homunculus a : testAnimas.values()) {
			Assert.assertEquals(a.getState(), Homunculus.HomunculusStates.RUNNING);
		}
		Thread.sleep(40000);
		System.out.println("server provides " + testAnimas.get(SERVER_LABEL).getRuntimeProvides());
		System.out.println("\n\nAPPROVE ALL REQUESTS\n");
		for (AgentRequest s : testAnimas.get(SERVER_LABEL).getBeaconServerIfExists().listAgentRequests()) {
			final String requestCsr = s.getRequest().getRequestCsr();
			System.out.println("approve csr\n" + requestCsr);
			testAnimas.get(SERVER_LABEL).getBeaconServerIfExists().approveCsrRequest(requestCsr);
		}

		Thread.sleep(40000);
		final List<Agent> agents = testAnimas.get(CLIENT1_LABEL).getBeaconClient().listAgentsConnectedToBeacon();
		String agentToQuery = null;
		for (final Agent a : agents) {
			if (testAnimas.get(CLIENT2_LABEL).getAgentUniqueName().equals(a.getAgentUniqueName())) {
				agentToQuery = a.getAgentUniqueName();
				System.out.println("agent client 1 found -> " + a.getAgentUniqueName());
			}
		}
		final String destinationIp = "localhost";
		final int destinationPort = 7777;
		final int srcPort = 8888;
		final Callable<Boolean> runner = new Callable<Boolean>() {
			private int last = 0;

			@Override
			public Boolean call() throws Exception {
				@SuppressWarnings("resource")
				final ServerSocket serverSocket = new ServerSocket(destinationPort);
				serverSocket.setReuseAddress(true);
				final Socket socket = serverSocket.accept();
				final PrintWriter w = new PrintWriter(socket.getOutputStream(), true);
				final InputStreamReader reader = new InputStreamReader(socket.getInputStream());
				try {
					while (!completed) {
						while (reader.ready()) {
							final int valueNew = reader.read();
							System.out.println("server test received from beacon client " + valueNew);
							if (last == 0)
								last = valueNew + 1;
							else {
								if (last + 1 != valueNew) {
									throw new Exception(
											"error in server test last cached:" + last + ",new:" + valueNew);
								} else {
									last = valueNew + 1;
								}
							}
							Thread.sleep(1000);
							w.write(last);
							w.flush();
							System.out.println("server test sent to beacon client " + last);
						}
					}
				} catch (final InterruptedException f) {
					serverSocket.close();
					System.out.println("server closed");
				} catch (final Exception a) {
					serverSocket.close();
					System.out.println("server closed");
					a.printStackTrace();
				}
				serverSocket.close();
				return true;
			}
		};

		final Callable<Boolean> clientRunner = new Callable<Boolean>() {
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
		// codice
		serverTCP = executor.submit(runner);
		final NetworkConfig config = new BeaconNettyNetworkConfig("tunnel-test", "tunnel in fase di test",
				NetworkMode.CLIENT, NetworkProtocol.TCP, destinationIp, destinationPort, srcPort);
		networkTunnel = testAnimas.get(CLIENT2_LABEL).getBeaconClient().getNewNetworkTunnel(agentToQuery, config);
		Thread.sleep(20000);

		System.out.println("network tunnel status -> " + networkTunnel.getNetworkReceiver().getNetworkStatus());
		System.out.println("try to send package");
		clientTCP = executor.submit(clientRunner);
		Thread.sleep(60000);
		assertTrue(completed);
	}

	protected void updateClientCounter(int valueNew) {
		// System.out.println("counter: " + valueNew);
		if (valueNew > 56) {
			completed = true;
			clientTCP.cancel(true);
			serverTCP.cancel(true);
			System.out.println("package counter [R]:" + networkTunnel.getPacketReceived() + " [S]:"
					+ networkTunnel.getPacketSend());
		}
	}

}
