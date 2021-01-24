package org.ar4k.agent.activemq;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.helper.ContextCreationHelper;
import org.ar4k.agent.keystore.KeystoreLoader;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.tunnels.http2.beacon.BeaconServiceConfig;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Agent;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class ActiveMqClusterTest implements MqttCallback {

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
		final PKCS10CertificationRequest csrClient2 = KeystoreLoader
				.getPKCS10CertificationRequest(client2AliasInKeystore, keyStoreClient2.getAbsolutePath(), passwordKs);
		final PKCS10CertificationRequest csrClient1 = KeystoreLoader
				.getPKCS10CertificationRequest(client1AliasInKeystore, keyStoreClient1.getAbsolutePath(), passwordKs);
		final JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
		final PublicKey pubKeyServer = converter.getPublicKey(csrServer.getSubjectPublicKeyInfo());
		// writeCSr("./tmp/csr-server.pem",
		// Base64.getEncoder().encodeToString(csrServer.getEncoded()));
		System.out.println("\nCSR SERVER\n" + pubKeyServer);
		final PublicKey pubKeyClient1 = converter.getPublicKey(csrClient1.getSubjectPublicKeyInfo());
		// writeCSr("./tmp/csr-client1.pem",
		// Base64.getEncoder().encodeToString(csrClient1.getEncoded()));
		System.out.println("\nCSR CLIENT1\n" + pubKeyClient1);
		final PublicKey pubKeyClient2 = converter.getPublicKey(csrClient2.getSubjectPublicKeyInfo());
		// writeCSr("./tmp/csr-client2.pem",
		// Base64.getEncoder().encodeToString(csrClient2.getEncoded()));
		System.out.println("\nCSR CLIENT2\n" + pubKeyClient2);
		KeystoreLoader.signCertificate(csrServer, signServerAliasInKeystore, 100, masterAliasInKeystore,
				keyStoreMaster.getAbsolutePath(), passwordKs);
		KeystoreLoader.signCertificate(csrClient2, signClient2AliasInKeystore, 100, masterAliasInKeystore,
				keyStoreMaster.getAbsolutePath(), passwordKs);
		KeystoreLoader.signCertificate(csrClient1, signClient1AliasInKeystore, 100, masterAliasInKeystore,
				keyStoreMaster.getAbsolutePath(), passwordKs);

		final String crtServer = KeystoreLoader.getCertCaAsPem(signServerAliasInKeystore,
				keyStoreMaster.getAbsolutePath(), passwordKs);
		final String keyServer = KeystoreLoader.getPrivateKeyBase64(serverAliasInKeystore,
				keyStoreServer.getAbsolutePath(), passwordKs);
		KeystoreLoader.setClientKeyPair(keyServer, crtServer, signServerAliasInKeystore,
				keyStoreServer.getAbsolutePath(), passwordKs);
		final String crtClient1 = KeystoreLoader.getCertCaAsPem(signClient1AliasInKeystore,
				keyStoreMaster.getAbsolutePath(), passwordKs);
		final String keyClient1 = KeystoreLoader.getPrivateKeyBase64(client1AliasInKeystore,
				keyStoreClient1.getAbsolutePath(), passwordKs);
		KeystoreLoader.setClientKeyPair(keyClient1, crtClient1, signClient1AliasInKeystore,
				keyStoreClient1.getAbsolutePath(), passwordKs);

		final String crtClient2 = KeystoreLoader.getCertCaAsPem(signClient2AliasInKeystore,
				keyStoreMaster.getAbsolutePath(), passwordKs);
		final String keyClient2 = KeystoreLoader.getPrivateKeyBase64(client2AliasInKeystore,
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

	@Test
	public void multiNodeSimulationActiveMQ() throws Exception {
		allNodeSimulated(true);
	}

	private void allNodeSimulated(boolean ssl) throws Exception {
		final List<String> baseArgs = new ArrayList<>();
		final List<String> baseArgsClientOne = new ArrayList<>();
		final List<String> baseArgsClientTwo = new ArrayList<>();
		String certCaAsPem = "";
		if (ssl) {
			baseArgs.add("--ar4k.beaconClearText=false");
			baseArgsClientOne.add("--ar4k.beaconClearText=false");
			baseArgsClientTwo.add("--ar4k.beaconClearText=false");
			certCaAsPem = KeystoreLoader.getCertCaAsPem(masterAliasInKeystore, keyStoreMaster.getAbsolutePath(),
					passwordKs);
			final byte[] decodedCrt = Base64.getDecoder().decode(certCaAsPem);
			final X509Certificate clientCertificate = (X509Certificate) CertificateFactory.getInstance("X.509")
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
		// serverConfig.beaconServer = null;
		serverConfig.beaconDiscoveryPort = 0;
		serverConfig.beaconServerCertChain = certCaAsPem;
		clientOneConfig.beaconServerCertChain = certCaAsPem;
		clientTwoConfig.beaconServerCertChain = certCaAsPem;
		final BeaconServiceConfig beaconServiceConfig = new BeaconServiceConfig();
		beaconServiceConfig.discoveryPort = 33667;
		beaconServiceConfig.port = 31226;
		beaconServiceConfig.aliasBeaconServerInKeystore = signServerAliasInKeystore;
		beaconServiceConfig.caChainPem = certCaAsPem;
		beaconServiceConfig.stringDiscovery = "TEST-REGISTER";
		serverConfig.pots.add(beaconServiceConfig);
		final ActiveMqConfig hconfig1 = new ActiveMqConfig();
		final ActiveMqConfig hconfig2 = new ActiveMqConfig();
		hconfig1.setName("hcast1");
		hconfig1.portMqtt = "1880";
		hconfig1.portMqttSsl = "1890";
		hconfig1.portWebService = "1990";
		hconfig1.clusterName = "cluster";
		hconfig1.trunkPort = 1206;
		hconfig2.setName("hcast2");
		hconfig2.portMqtt = "1881";
		hconfig2.portMqttSsl = "1891";
		hconfig2.portWebService = "1991";
		hconfig2.clusterName = "cluster";
		hconfig2.trunkPort = 1207;
		hconfig1.clusterServers = 2;
		clientOneConfig.pots.add(hconfig1);
		clientTwoConfig.pots.add(hconfig2);

		testAnimas.put(SERVER_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgentActiveMqRunForTest.class, executor, "a.log",
						keyStoreServer.getAbsolutePath(), 1124, baseArgs, serverConfig, serverAliasInKeystore,
						signServerAliasInKeystore, "https://localhost:31226")).get());
		testAnimas.put(CLIENT2_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgentActiveMqRunForTest.class, executor, "b.log",
						keyStoreClient2.getAbsolutePath(), 1125, baseArgsClientTwo, clientTwoConfig,
						client2AliasInKeystore, signClient2AliasInKeystore, "https://localhost:31226")).get());
		testAnimas.put(CLIENT1_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgentActiveMqRunForTest.class, executor, "c.log",
						keyStoreClient1.getAbsolutePath(), 1126, baseArgsClientOne, clientOneConfig,
						client1AliasInKeystore, signClient1AliasInKeystore, "https://localhost:31226")).get());
		Thread.sleep(6000);
		for (final Homunculus a : testAnimas.values()) {
			// String animaName = a.getRuntimeConfig() != null ?
			// a.getRuntimeConfig().getName() : "no-config";
			Assert.assertEquals(a.getState(), Homunculus.HomunculusStates.RUNNING);
		}
		Thread.sleep(2000);
		final List<Agent> agents = testAnimas.get(CLIENT2_LABEL).getBeaconClient().listAgentsConnectedToBeacon();
		for (final Agent a : agents) {
			if (testAnimas.get(CLIENT1_LABEL).getAgentUniqueName().equals(a.getAgentUniqueName())) {
				System.out.println("agent client 1 found -> " + a.getAgentUniqueName());
			}
		}

		// session1.createQueue("example", RoutingType.MULTICAST, "example", false);
		final String topic = "test/go";
		final int qos = 1;
		final String brokerProducer = "tcp://localhost:1880";
		final String brokerConsumer = "tcp://localhost:1881";
		final String clientIdProducer = "ar4k-client-producer";
		final String clientIdConsumer = "ar4k-client-consumer";
		final MemoryPersistence persistenceProducer = new MemoryPersistence();
		final MemoryPersistence persistenceConsumer = new MemoryPersistence();
		final MqttClient producerClient = new MqttClient(brokerProducer, clientIdProducer, persistenceProducer);
		final MqttClient consumerClient = new MqttClient(brokerConsumer, clientIdConsumer, persistenceConsumer);
		final MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		connOpts.setAutomaticReconnect(true);
		connOpts.setUserName("guest");
		connOpts.setPassword("anonymous".toCharArray());
		System.out.println("Connecting to broker: " + brokerProducer);
		producerClient.connect(connOpts);
		System.out.println("Connected");
		System.out.println("Connecting to broker: " + brokerConsumer);
		consumerClient.connect(connOpts);
		System.out.println("Connected");
		consumerClient.subscribe(topic, qos);
		consumerClient.setCallback(this);
		for (int i = 1; i < 100; i++) {
			final String testString = UUID.randomUUID().toString();
			final JSONObject json = new JSONObject();
			json.put("data", testString);
			System.out.println("Publishing message: " + json.toString());
			queue.offer(json.toString());
			final MqttMessage messageMqtt = new MqttMessage(json.toString().getBytes());
			messageMqtt.setQos(qos);
			producerClient.publish(topic, messageMqtt);
			System.out.println("Message published");
			Thread.sleep(200L);
		}
		Thread.sleep(10000L);
		assertTrue(queue.isEmpty());
		producerClient.disconnect();
		producerClient.close();
		consumerClient.disconnect();
		consumerClient.close();
		System.out.println("Disconnected");
	}

	Queue<String> queue = new LinkedBlockingDeque<String>();

	private void deleteDir(File dir) {
		final File[] files = dir.listFiles();
		if (files != null) {
			for (final File file : files) {
				deleteDir(file);
			}
		}
		dir.delete();
	}

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println(EdgeLogger.stackTraceToString(cause));
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("received " + topic + " -> " + new String(message.getPayload()));
		assertEquals(new String(message.getPayload()), queue.poll());
		assertTrue(queue.isEmpty());
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		System.out.println("delivery " + token.getMessageId());
	}

}
