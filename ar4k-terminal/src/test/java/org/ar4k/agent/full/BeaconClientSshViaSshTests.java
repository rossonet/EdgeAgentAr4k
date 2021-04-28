package org.ar4k.agent.full;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.console.BeaconShellInterface;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.helper.ContextCreationHelper;
import org.ar4k.agent.helper.KeystoreLoader;
import org.ar4k.agent.tunnels.http2.beacon.BeaconServiceConfig;
import org.ar4k.agent.tunnels.ssh.client.SshLocalConfig;
import org.ar4k.agent.tunnels.ssh.client.SshRemoteConfig;
import org.ar4k.agent.tunnels.sshd.SshdSystemConfig;
import org.ar4k.agent.tunnels.sshd.SshdSystemService;
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
public class BeaconClientSshViaSshTests {

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

	private void deleteDir(File dir) {
		File[] files = dir.listFiles();
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
		PKCS10CertificationRequest csrServer = KeystoreLoader.getPKCS10CertificationRequest(serverAliasInKeystore,
				keyStoreServer.getAbsolutePath(), passwordKs);
		PKCS10CertificationRequest csrClient2 = KeystoreLoader.getPKCS10CertificationRequest(client2AliasInKeystore,
				keyStoreClient2.getAbsolutePath(), passwordKs);
		PKCS10CertificationRequest csrClient1 = KeystoreLoader.getPKCS10CertificationRequest(client1AliasInKeystore,
				keyStoreClient1.getAbsolutePath(), passwordKs);
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
		PublicKey pubKeyServer = converter.getPublicKey(csrServer.getSubjectPublicKeyInfo());
		writeCSr("./tmp/csr-server.pem", Base64.getEncoder().encodeToString(csrServer.getEncoded()));
		System.out.println("\nCSR SERVER\n" + pubKeyServer);
		PublicKey pubKeyClient1 = converter.getPublicKey(csrClient1.getSubjectPublicKeyInfo());
		writeCSr("./tmp/csr-client1.pem", Base64.getEncoder().encodeToString(csrClient1.getEncoded()));
		System.out.println("\nCSR CLIENT1\n" + pubKeyClient1);
		PublicKey pubKeyClient2 = converter.getPublicKey(csrClient2.getSubjectPublicKeyInfo());
		writeCSr("./tmp/csr-client2.pem", Base64.getEncoder().encodeToString(csrClient2.getEncoded()));
		System.out.println("\nCSR CLIENT2\n" + pubKeyClient2);
		KeystoreLoader.signCertificate(csrServer, signServerAliasInKeystore, 100, masterAliasInKeystore,
				keyStoreMaster.getAbsolutePath(), passwordKs);
		KeystoreLoader.signCertificate(csrClient2, signClient2AliasInKeystore, 100, masterAliasInKeystore,
				keyStoreMaster.getAbsolutePath(), passwordKs);
		KeystoreLoader.signCertificate(csrClient1, signClient1AliasInKeystore, 100, masterAliasInKeystore,
				keyStoreMaster.getAbsolutePath(), passwordKs);

		String crtServer = KeystoreLoader.getCertCaAsPem(signServerAliasInKeystore, keyStoreMaster.getAbsolutePath(),
				passwordKs);
		String keyServer = KeystoreLoader.getPrivateKeyBase64(serverAliasInKeystore, keyStoreServer.getAbsolutePath(),
				passwordKs);
		KeystoreLoader.setClientKeyPair(keyServer, crtServer, signServerAliasInKeystore,
				keyStoreServer.getAbsolutePath(), passwordKs);
		String crtClient1 = KeystoreLoader.getCertCaAsPem(signClient1AliasInKeystore, keyStoreMaster.getAbsolutePath(),
				passwordKs);
		String keyClient1 = KeystoreLoader.getPrivateKeyBase64(client1AliasInKeystore,
				keyStoreClient1.getAbsolutePath(), passwordKs);
		KeystoreLoader.setClientKeyPair(keyClient1, crtClient1, signClient1AliasInKeystore,
				keyStoreClient1.getAbsolutePath(), passwordKs);

		String crtClient2 = KeystoreLoader.getCertCaAsPem(signClient2AliasInKeystore, keyStoreMaster.getAbsolutePath(),
				passwordKs);
		String keyClient2 = KeystoreLoader.getPrivateKeyBase64(client2AliasInKeystore,
				keyStoreClient2.getAbsolutePath(), passwordKs);
		KeystoreLoader.setClientKeyPair(keyClient2, crtClient2, signClient2AliasInKeystore,
				keyStoreClient2.getAbsolutePath(), passwordKs);
		System.out.println(
				"\n\nLIST MASTER " + KeystoreLoader.listCertificate(keyStoreMaster.getAbsolutePath(), passwordKs));
		System.out.println(
				"\n\nLIST SERVER " + KeystoreLoader.listCertificate(keyStoreServer.getAbsolutePath(), passwordKs));
		System.out.println(
				"\n\nLIST CLIENT 1 " + KeystoreLoader.listCertificate(keyStoreClient1.getAbsolutePath(), passwordKs));
	}

	@After
	public void tearDown() throws Exception {
		System.err.println("\n\nEND TESTS\n\n");
		for (Homunculus a : testAnimas.values()) {
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

	@Test
	@Ignore
	public void simpleSShServerWithSystemShell() throws InterruptedException {
		SshdSystemConfig testServerConfig = new SshdSystemConfig();
		SshdSystemService server = (SshdSystemService) testServerConfig.instantiate();
		server.init();
		for (int i = 0; i < 20; i++) {
			System.out.println(server.getDescriptionJson());
			Thread.sleep(5000);
		}
		server.kill();
	}

	private void writeCSr(String path, String cert) throws UnrecoverableKeyException, NoSuchAlgorithmException,
			CertificateException, KeyStoreException, IOException {
		FileWriter writer = new FileWriter(new File(path));
		writer.write("-----BEGIN CERTIFICATE REQUEST-----\n");
		writer.write(cert);
		writer.write("\n-----END CERTIFICATE REQUEST-----\n");
		writer.close();
	}

	@Test
	@Ignore
	public void allNodeSimulatedSshTunnel() throws Exception {
		allNodeSimulatedWithTunnel(false);
	}

	private void allNodeSimulatedWithTunnel(boolean ssl) throws Exception {
		List<String> baseArgs = new ArrayList<>();
		List<String> baseArgsClientOne = new ArrayList<>();
		List<String> baseArgsClientTwo = new ArrayList<>();
		String certCaAsPem = "";
		if (ssl) {
			baseArgs.add("--ar4k.beaconClearText=false");
			baseArgsClientOne.add("--ar4k.beaconClearText=false");
			baseArgsClientTwo.add("--ar4k.beaconClearText=false");
			certCaAsPem = KeystoreLoader.getCertCaAsPem(masterAliasInKeystore, keyStoreMaster.getAbsolutePath(),
					passwordKs);
			byte[] decodedCrt = Base64.getDecoder().decode(certCaAsPem);
			X509Certificate clientCertificate = (X509Certificate) CertificateFactory.getInstance("X.509")
					.generateCertificate(new ByteArrayInputStream(decodedCrt));
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
		if (certCaAsPem != null && !certCaAsPem.isEmpty())
			baseArgs.add("--ar4k.beaconCaChainPem=" + certCaAsPem);
		baseArgs.add("--ar4k.adminPassword=password");

		baseArgsClientOne.add("--ar4k.keystorePassword=" + passwordKs);
		if (certCaAsPem != null && !certCaAsPem.isEmpty())
			baseArgsClientOne.add("--ar4k.beaconCaChainPem=" + certCaAsPem);
		baseArgsClientOne.add("--ar4k.adminPassword=password");

		baseArgsClientTwo.add("--ar4k.keystorePassword=" + passwordKs);
		if (certCaAsPem != null && !certCaAsPem.isEmpty())
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

		EdgeConfig clientOneConfig = new EdgeConfig();
		EdgeConfig clientTwoConfig = new EdgeConfig();
		EdgeConfig serverConfig = new EdgeConfig();
		serverConfig.name = "server-beacon";
		clientOneConfig.name = "client1-beacon";
		clientTwoConfig.name = "client2-beacon";
		// serverConfig.beaconServer = null;
		serverConfig.beaconDiscoveryPort = 0;
		serverConfig.beaconServerCertChain = certCaAsPem;
		clientOneConfig.beaconServerCertChain = certCaAsPem;
		clientTwoConfig.beaconServerCertChain = certCaAsPem;
		String destinationIp = "127.0.0.1";
		int destinationPort = 7777;
		int srcPort = 8888;
		SshdSystemConfig sshdConfig = new SshdSystemConfig();
		sshdConfig.setName("sshd mina server");
		sshdConfig.port = 10000;
		serverConfig.pots.add(sshdConfig);
		BeaconServiceConfig beaconServiceConfig = new BeaconServiceConfig();
		beaconServiceConfig.discoveryPort = 33667;
		beaconServiceConfig.port = 33666;
		beaconServiceConfig.caChainPem = certCaAsPem;
		beaconServiceConfig.aliasBeaconServerInKeystore = serverAliasInKeystore;
		beaconServiceConfig.stringDiscovery = "TEST-REGISTER";
		serverConfig.pots.add(beaconServiceConfig);

		SshRemoteConfig sshRight = new SshRemoteConfig();
		sshRight.setName("ssh client 2");
		sshRight.redirectServer = destinationIp;
		sshRight.redirectPort = destinationPort;
		sshRight.bindPort = 10008;
		sshRight.bindHost = "0.0.0.0";
		sshRight.host = destinationIp;
		sshRight.port = 10000;
		// sshRight.authkey = "~/.ssh/id_rsa";
		sshRight.username = "admin";
		sshRight.password = "password";
		clientTwoConfig.pots.add(sshRight);

		SshLocalConfig sshLeft = new SshLocalConfig();
		sshLeft.setName("ssh client 1");
		sshLeft.redirectServer = destinationIp;
		sshLeft.redirectPort = 10008;
		sshLeft.bindPort = srcPort;
		sshLeft.bindHost = "0.0.0.0";
		sshLeft.host = destinationIp;
		sshLeft.port = 10000;
		// sshLeft.authkey = "~/.ssh/id_rsa";
		sshLeft.username = "admin";
		sshLeft.password = "password";
		clientOneConfig.pots.add(sshLeft);

		testAnimas.put(SERVER_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgentRunForTest.class, executor, "a.log",
						keyStoreServer.getAbsolutePath(), 1124, baseArgs, serverConfig, serverAliasInKeystore,
						signServerAliasInKeystore, null)).get());
		testAnimas.put(CLIENT2_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgentRunForTest.class, executor, "b.log",
						keyStoreClient2.getAbsolutePath(), 1125, baseArgsClientTwo, clientTwoConfig,
						client2AliasInKeystore, signClient2AliasInKeystore, "https://127.0.0.1:33666")).get());
		testAnimas.put(CLIENT1_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgentRunForTest.class, executor, "c.log",
						keyStoreClient1.getAbsolutePath(), 1126, baseArgsClientOne, clientOneConfig,
						client1AliasInKeystore, signClient1AliasInKeystore, "https://127.0.0.1:33666")).get());
		Thread.sleep(15000);
		for (Homunculus a : testAnimas.values()) {
			// String animaName = a.getRuntimeConfig() != null ?
			// a.getRuntimeConfig().getName() : "no-config";
			Assert.assertEquals(a.getState(), Homunculus.HomunculusStates.RUNNING);
		}
		Thread.sleep(5000);
		BeaconShellInterface beanBeaconShellInterface = testAnimas.get(CLIENT1_LABEL).getApplicationContext()
				.getBean(BeaconShellInterface.class);
		// String commandLogin = "login --username admin --password password";
		String targetAgent = testAnimas.get(CLIENT2_LABEL).getAgentUniqueName();
		// System.out.println("LOGIN: " +
		// beanBeaconShellInterface.runCommandOnRemoteAgent(targetAgent, commandLogin));
		/*
		 * System.out.println("LIST AGENTS BASE: " +
		 * beanBeaconShellInterface.listBeaconAgentsHumanReadable(false, false, "and",
		 * ".*", ".*", ".*", 80));
		 *
		 * System.out.println("LIST AGENTS WITH HEALTH: " +
		 * beanBeaconShellInterface.listBeaconAgentsHumanReadable(true, false, "and",
		 * ".*", ".*", ".*", 80));
		 *
		 * System.out.println("LIST AGENTS CSV: " +
		 * beanBeaconShellInterface.listBeaconAgentsHumanReadable(false, true, "and",
		 * ".*", ".*", ".*", 80));
		 *
		 * System.out.println("LIST AGENTS CSV WITH HEALTH: " +
		 * beanBeaconShellInterface.listBeaconAgentsHumanReadable(true, true, "and",
		 * ".*", ".*", ".*", 80));
		 *
		 * System.out.println("TRY SSH XPRA: " +
		 * beanBeaconShellInterface.listBeaconAgentsHumanReadable(true, true, "and",
		 * ".*", ".*", ".*", 80));
		 */
		// test accesso xpra via beacon
		System.out.println("\ntest xpra\n".toUpperCase());
		System.out.println("run xpra and ssh tunnel command: ".toUpperCase()
				+ beanBeaconShellInterface.createSshMirrorTunnel(targetAgent, 22, "harari.rossonet.net", 22, "rossonet",
						null, "/home/andrea/.ssh/id_rsa", "admin", "password"));
		// System.out.println("REMOTE XPRA RETURN: "
		// +
		// beanBeaconShellInterface.runXpraServerOnAgentAndConnectToViaBeacon(agentToQuery,
		// "test-xpra", "xterm"));// ,
		// "admin",
		// "a4c8ff551a"));
		// test accesso xpra via ssh mirror tunnel

		// test accesso ssh console via beacon

		// test accesso ssh console via ssh mirror tunnel

		// test install xpra with multi-platform (debian / redhat)
		Thread.sleep(15 * 60 * 1000);
	}
}
