package org.ar4k.agent.control.remote;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.console.Ar4kAgent;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.helper.ContextCreationHelper;
import org.ar4k.agent.keystore.KeystoreLoader;
import org.ar4k.agent.tunnels.http2.beacon.BeaconServiceConfig;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RpcServiceV1Grpc;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RpcServiceV1Grpc.RpcServiceV1BlockingStub;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Status;
import org.ar4k.agent.tunnels.http2.grpc.beacon.StatusValue;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.ClientAuth;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslProvider;
import io.grpc.stub.StreamObserver;

@RunWith(SpringJUnit4ClassRunner.class)
//TODO Completare test beacon
@Ignore
public class RemoteControlOverBeaconRpcConfig {

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
				keyStoreMaster.getAbsolutePath(), passwordKs, true);
		KeystoreLoader.create("server", "Rossonet", "TEST UNIT S", "IMOLA", "BOLOGNA", "IT",
				"urn:org.ar4k.agent:server-test-agent", "*.ar4k.net", "127.0.0.1", serverAliasInKeystore,
				keyStoreServer.getAbsolutePath(), passwordKs, false);
		KeystoreLoader.create("client2", "Rossonet", "TEST UNIT C2", "IMOLA", "BOLOGNA", "IT",
				"urn:org.ar4k.agent:client2-test-agent", "*.ar4k.net", "127.0.0.1", client2AliasInKeystore,
				keyStoreClient2.getAbsolutePath(), passwordKs, false);
		KeystoreLoader.create("client1", "Rossonet", "TEST UNIT C1", "IMOLA", "BOLOGNA", "IT",
				"urn:org.ar4k.agent:client1-test-agent", "*.ar4k.net", "127.0.0.1", client1AliasInKeystore,
				keyStoreClient1.getAbsolutePath(), passwordKs, false);
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
	public void simpleGrpcConnectionSsl() {
		Server server = null;
		try {
			writeServerPrivateKey();
			writeServerCert();
			writeClientPrivateKey();
			writeClientCert();
			writeServerPrivateKeyOriginal();
			writeServerCertOriginal();
			writeClientPrivateKeyOriginal();
			writeClientCertOriginal();
			writeCa();
			// server
			SslContextBuilder sslContextBuild = GrpcSslContexts
					.forServer(new File("./tmp/server-cert.crt"), new File("./tmp/server-private.key"))
					.sslProvider(SslProvider.OPENSSL).trustManager(new File("./tmp/ca.pem"))
					.clientAuth(ClientAuth.REQUIRE);
			ServerBuilder<?> serverBuilder = NettyServerBuilder.forPort(11111)
					.sslContext(GrpcSslContexts.configure(sslContextBuild, SslProvider.OPENSSL).build());
			server = serverBuilder.addService(new RpcService()).build();
			server.start();
			// client
			SslContextBuilder sslBuilder = GrpcSslContexts.forClient()
					.keyManager(new File("./tmp/client-cert.crt"), new File("./tmp/client-private.key"))
					.trustManager(new File("./tmp/ca.pem"));
			ManagedChannel channel = NettyChannelBuilder.forAddress("localhost", 11111)
					.sslContext(GrpcSslContexts.configure(sslBuilder, SslProvider.OPENSSL).build()).build();
			System.out.println("CHANNEL: " + channel.toString());
			System.out.println("AUTHORITY: " + channel.authority());
			RpcServiceV1BlockingStub blockingStub = RpcServiceV1Grpc.newBlockingStub(channel);
			RegisterRequest request = RegisterRequest.newBuilder().setName("test").build();
			Thread.sleep(5000L);
			blockingStub.register(request);
			channel.shutdownNow();
			server.shutdownNow();
		} catch (UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | KeyStoreException
				| IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private class RpcService extends RpcServiceV1Grpc.RpcServiceV1ImplBase {

		@Override
		public void register(RegisterRequest request, StreamObserver<RegisterReply> responseObserver) {
			System.out.println("NAME " + request.getName());
			Status status = Status.newBuilder().setStatus(StatusValue.GOOD).build();
			RegisterReply testReply = RegisterReply.newBuilder().setStatusRegistration(status).build();
			responseObserver.onNext(testReply);
			responseObserver.onCompleted();
		}

	}

	private void writeServerPrivateKey() throws UnrecoverableKeyException, NoSuchAlgorithmException,
			CertificateException, KeyStoreException, IOException {
		FileWriter writer;
		writer = new FileWriter(new File("./tmp/server-private.key"));
		writer.write("-----BEGIN PRIVATE KEY-----\n");
		writer.write(KeystoreLoader.getPrivateKeyBase64(signServerAliasInKeystore, keyStoreServer.getAbsolutePath(),
				passwordKs));
		writer.write("\n-----END PRIVATE KEY-----\n");
		writer.close();

	}

	private void writeServerCert() throws UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException,
			KeyStoreException, IOException {
		FileWriter writer = new FileWriter(new File("./tmp/server-cert.crt"));
		writer.write("-----BEGIN CERTIFICATE-----\n");
		writer.write(
				KeystoreLoader.getCertCaAsPem(signServerAliasInKeystore, keyStoreServer.getAbsolutePath(), passwordKs));
		writer.write("\n-----END CERTIFICATE-----\n");
		writer.close();
	}

	private void writeCSr(String path, String cert) throws UnrecoverableKeyException, NoSuchAlgorithmException,
			CertificateException, KeyStoreException, IOException {
		FileWriter writer = new FileWriter(new File(path));
		writer.write("-----BEGIN CERTIFICATE REQUEST-----\n");
		writer.write(cert);
		writer.write("\n-----END CERTIFICATE REQUEST-----\n");
		writer.close();
	}

	private void writeServerPrivateKeyOriginal() throws UnrecoverableKeyException, NoSuchAlgorithmException,
			CertificateException, KeyStoreException, IOException {
		FileWriter writer;
		writer = new FileWriter(new File("./tmp/server-private-original.key"));
		writer.write("-----BEGIN PRIVATE KEY-----\n");
		writer.write(KeystoreLoader.getPrivateKeyBase64(serverAliasInKeystore, keyStoreServer.getAbsolutePath(),
				passwordKs));
		writer.write("\n-----END PRIVATE KEY-----\n");
		writer.close();

	}

	private void writeServerCertOriginal() throws UnrecoverableKeyException, NoSuchAlgorithmException,
			CertificateException, KeyStoreException, IOException {
		FileWriter writer = new FileWriter(new File("./tmp/server-cert-original.crt"));
		writer.write("-----BEGIN CERTIFICATE-----\n");
		writer.write(
				KeystoreLoader.getCertCaAsPem(serverAliasInKeystore, keyStoreServer.getAbsolutePath(), passwordKs));
		writer.write("\n-----END CERTIFICATE-----\n");
		writer.close();
	}

	private void writeClientPrivateKey() throws UnrecoverableKeyException, NoSuchAlgorithmException,
			CertificateException, KeyStoreException, IOException {
		FileWriter writer;
		writer = new FileWriter(new File("./tmp/client-private.key"));
		writer.write("-----BEGIN PRIVATE KEY-----\n");
		writer.write(KeystoreLoader.getPrivateKeyBase64(signClient1AliasInKeystore, keyStoreClient1.getAbsolutePath(),
				passwordKs));
		writer.write("\n-----END PRIVATE KEY-----\n");
		writer.close();

	}

	private void writeClientCert() throws UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException,
			KeyStoreException, IOException {
		FileWriter writer = new FileWriter(new File("./tmp/client-cert.crt"));
		writer.write("-----BEGIN CERTIFICATE-----\n");
		writer.write(KeystoreLoader.getCertCaAsPem(signClient1AliasInKeystore, keyStoreClient1.getAbsolutePath(),
				passwordKs));
		writer.write("\n-----END CERTIFICATE-----\n");
		writer.close();
	}

	private void writeClientPrivateKeyOriginal() throws UnrecoverableKeyException, NoSuchAlgorithmException,
			CertificateException, KeyStoreException, IOException {
		FileWriter writer;
		writer = new FileWriter(new File("./tmp/client-private-original.key"));
		writer.write("-----BEGIN PRIVATE KEY-----\n");
		writer.write(KeystoreLoader.getPrivateKeyBase64(client1AliasInKeystore, keyStoreClient1.getAbsolutePath(),
				passwordKs));
		writer.write("\n-----END PRIVATE KEY-----\n");
		writer.close();

	}

	private void writeClientCertOriginal() throws UnrecoverableKeyException, NoSuchAlgorithmException,
			CertificateException, KeyStoreException, IOException {
		FileWriter writer = new FileWriter(new File("./tmp/client-cert-original.crt"));
		writer.write("-----BEGIN CERTIFICATE-----\n");
		writer.write(
				KeystoreLoader.getCertCaAsPem(client1AliasInKeystore, keyStoreClient1.getAbsolutePath(), passwordKs));
		writer.write("\n-----END CERTIFICATE-----\n");
		writer.close();
	}

	private void writeCa() throws UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException,
			KeyStoreException, IOException {
		FileWriter writer = new FileWriter(new File("./tmp/ca.pem"));
		writer.write("-----BEGIN CERTIFICATE-----\n");
		writer.write(
				KeystoreLoader.getCertCaAsPem(masterAliasInKeystore, keyStoreMaster.getAbsolutePath(), passwordKs));
		writer.write("\n-----END CERTIFICATE-----\n");
		writer.close();
	}

	@Test
	public void allNodeSimulatedWithTunnelSsl() throws Exception {
		allNodeSimulatedWithTunnel(true);
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

		EdgeConfig clientOneConfig = new EdgeConfig();
		EdgeConfig clientTwoConfig = new EdgeConfig();
		EdgeConfig serverConfig = new EdgeConfig();
		serverConfig.name = "server-beacon";
		// serverConfig.beaconServer = null;
		serverConfig.beaconDiscoveryPort = 0;
		serverConfig.beaconServerCertChain = certCaAsPem;
		clientOneConfig.beaconServerCertChain = certCaAsPem;
		clientTwoConfig.beaconServerCertChain = certCaAsPem;
		BeaconServiceConfig beaconServiceConfig = new BeaconServiceConfig();
		beaconServiceConfig.discoveryPort = 33667;
		beaconServiceConfig.port = 31226;
		beaconServiceConfig.aliasBeaconServerInKeystore = signServerAliasInKeystore;
		beaconServiceConfig.caChainPem = certCaAsPem;
		beaconServiceConfig.stringDiscovery = "TEST-REGISTER";
		serverConfig.pots.add(beaconServiceConfig);

		testAnimas.put(SERVER_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "a.log",
						keyStoreServer.getAbsolutePath(), 1124, baseArgs, serverConfig, serverAliasInKeystore,
						signServerAliasInKeystore, "https://localhost:31226")).get());
		testAnimas.put(CLIENT2_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "b.log",
						keyStoreClient2.getAbsolutePath(), 1125, baseArgsClientTwo, clientTwoConfig,
						client2AliasInKeystore, signClient2AliasInKeystore, "https://localhost:31226")).get());
		testAnimas.put(CLIENT1_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "c.log",
						keyStoreClient1.getAbsolutePath(), 1126, baseArgsClientOne, clientOneConfig,
						client1AliasInKeystore, signClient1AliasInKeystore, "https://localhost:31226")).get());
		Thread.sleep(15000);
		for (Homunculus a : testAnimas.values()) {
			// String animaName = a.getRuntimeConfig() != null ?
			// a.getRuntimeConfig().getName() : "no-config";
			Assert.assertEquals(a.getState(), Homunculus.HomunculusStates.RUNNING);
		}
		Thread.sleep(25000);
		List<Agent> agents = testAnimas.get(CLIENT2_LABEL).getBeaconClient().listAgentsConnectedToBeacon();
		String agentToQuery = null;
		for (Agent a : agents) {
			if (testAnimas.get(CLIENT1_LABEL).getAgentUniqueName().equals(a.getAgentUniqueName())) {
				agentToQuery = a.getAgentUniqueName();
				System.out.println("agent client 1 found -> " + a.getAgentUniqueName());
			}
		}

		// codice
		EdgeConfig assertConfig = new EdgeConfig();
		assertConfig.name = "test-send-config";
		String testValue = UUID.randomUUID().toString();
		assertConfig.description = testValue;
		testAnimas.get(CLIENT2_LABEL).getBeaconClient().sendConfigToAgent(agentToQuery, assertConfig);
		Thread.sleep(30000);
		assertEquals(testAnimas.get(CLIENT1_LABEL).getRuntimeConfig().getDescription(), testValue);
	}

	private void deleteDir(File dir) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (final File file : files) {
				deleteDir(file);
			}
		}
		dir.delete();
	}

}
