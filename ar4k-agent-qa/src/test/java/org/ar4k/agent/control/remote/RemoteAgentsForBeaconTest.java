package org.ar4k.agent.control.remote;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.console.Ar4kAgent;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.helper.ContextCreationHelper;
import org.ar4k.agent.helper.KeystoreLoader;
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
	private static final String CLIENT3_LABEL = "client3";
	private final ExecutorService executor = Executors.newCachedThreadPool();
	private final Map<String, Homunculus> testAnimas = new HashMap<>();
	private final File keyStoreClient3 = new File("./tmp/client3.ks");
	private final File keyStoreClient1 = new File("./tmp/client1.ks");
	private final File keyStoreClient2 = new File("./tmp/client2.ks");
	private final String client3AliasInKeystore = "client3";
	private final String client2AliasInKeystore = "client2";
	private final String client1AliasInKeystore = "client1";
	private final String signClient3AliasInKeystore = "client3-sign";
	private final String signClient2AliasInKeystore = "client2-sign";
	private final String signClient1AliasInKeystore = "client1-sign";
	private final String passwordKs = "password";

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
		KeystoreLoader.create("client3", "Rossonet", "TEST UNIT C3", "IMOLA", "BOLOGNA", "IT",
				"urn:org.ar4k.agent:client3-test-agent", "*.ar4k.net", "127.0.0.1", client3AliasInKeystore,
				keyStoreClient3.getAbsolutePath(), passwordKs, false, VALIDITY_CERT_DAYS);
		KeystoreLoader.create("client2", "Rossonet", "TEST UNIT C2", "IMOLA", "BOLOGNA", "IT",
				"urn:org.ar4k.agent:client2-test-agent", "*.ar4k.net", "127.0.0.1", client2AliasInKeystore,
				keyStoreClient2.getAbsolutePath(), passwordKs, false, VALIDITY_CERT_DAYS);
		KeystoreLoader.create("client1", "Rossonet", "TEST UNIT C1", "IMOLA", "BOLOGNA", "IT",
				"urn:org.ar4k.agent:client1-test-agent", "*.ar4k.net", "127.0.0.1", client1AliasInKeystore,
				keyStoreClient1.getAbsolutePath(), passwordKs, false, VALIDITY_CERT_DAYS);
		System.out.println(
				"\n\nLIST CLIENT 1 " + KeystoreLoader.listCertificate(keyStoreClient1.getAbsolutePath(), passwordKs));
		System.out.println(
				"\n\nLIST CLIENT 2 " + KeystoreLoader.listCertificate(keyStoreClient2.getAbsolutePath(), passwordKs));
		System.out.println(
				"\n\nLIST CLIENT 3 " + KeystoreLoader.listCertificate(keyStoreClient3.getAbsolutePath(), passwordKs));
	}

	@After
	public void tearDown() throws Exception {
		System.err.println("\n\nEND TESTS\n\n");
		for (final Homunculus a : testAnimas.values()) {
			a.close();
		}
		Files.deleteIfExists(Paths.get("./tmp/test-client3.config.base64.ar4k"));
		Files.deleteIfExists(Paths.get("./tmp/test-client1.config.base64.ar4k"));
		Files.deleteIfExists(Paths.get("./tmp/test-client2.config.base64.ar4k"));
		Files.deleteIfExists(keyStoreClient3.toPath());
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
	public void clientsForBeaconTests() throws Exception {
		allNodeSimulatedWithTunnel(true);
	}

	private void allNodeSimulatedWithTunnel(boolean ssl) throws Exception {
		final List<String> baseArgsClientThree = new ArrayList<>();
		final List<String> baseArgsClientOne = new ArrayList<>();
		final List<String> baseArgsClientTwo = new ArrayList<>();
		if (ssl) {
			baseArgsClientThree.add("--ar4k.beaconClearText=false");
			baseArgsClientOne.add("--ar4k.beaconClearText=false");
			baseArgsClientTwo.add("--ar4k.beaconClearText=false");
		}
		baseArgsClientThree.add("--ar4k.consoleOnly=false");
		baseArgsClientThree.add("--spring.shell.command.quit.enabled=false");
		baseArgsClientThree.add("--logging.level.root=INFO");
		baseArgsClientThree.add("--ar4k.confPath=./tmp1");
		baseArgsClientThree.add("--ar4k.fileConfig=./tmp1/test-client3.config.base64.ar4k");
		baseArgsClientThree
				.add("--ar4k.webConfig=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
		baseArgsClientThree.add("--ar4k.dnsConfig=demo1.rossonet.name");
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
		baseArgsClientThree
				.add("--ar4k.webKeystore=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
		baseArgsClientThree.add("--ar4k.dnsKeystore=ks1.rossonet.name");
		baseArgsClientOne
				.add("--ar4k.webKeystore=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
		baseArgsClientOne.add("--ar4k.dnsKeystore=ks1.rossonet.name");
		baseArgsClientTwo
				.add("--ar4k.webKeystore=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
		baseArgsClientTwo.add("--ar4k.dnsKeystore=ks1.rossonet.name");
		baseArgsClientThree.add("--ar4k.keystorePassword=" + passwordKs);
		baseArgsClientThree.add("--ar4k.adminPassword=password");
		baseArgsClientOne.add("--ar4k.keystorePassword=" + passwordKs);
		baseArgsClientOne.add("--ar4k.adminPassword=password");
		baseArgsClientTwo.add("--ar4k.keystorePassword=" + passwordKs);
		baseArgsClientTwo.add("--ar4k.adminPassword=password");
		baseArgsClientThree.add("--ar4k.beaconDiscoveryFilterString=TEST-REGISTER");
		baseArgsClientThree.add("--ar4k.beaconDiscoveryPort=33668");
		baseArgsClientThree.add("--ar4k.fileConfigOrder=1");
		baseArgsClientThree.add("--ar4k.webConfigOrder=2");
		baseArgsClientThree.add("--ar4k.dnsConfigOrder=3");
		baseArgsClientThree.add("--ar4k.baseConfigOrder=0");
		baseArgsClientThree.add("--ar4k.threadSleep=1000");
		baseArgsClientThree.add("--ar4k.logoUrl=/static/img/ar4k.png");
		baseArgsClientOne.add("--ar4k.beaconDiscoveryFilterString=TEST-REGISTER");
		baseArgsClientOne.add("--ar4k.beaconDiscoveryPort=33669");
		baseArgsClientOne.add("--ar4k.fileConfigOrder=1");
		baseArgsClientOne.add("--ar4k.webConfigOrder=2");
		baseArgsClientOne.add("--ar4k.dnsConfigOrder=3");
		baseArgsClientOne.add("--ar4k.baseConfigOrder=0");
		baseArgsClientOne.add("--ar4k.threadSleep=1000");
		baseArgsClientOne.add("--ar4k.logoUrl=/static/img/ar4k.png");
		baseArgsClientTwo.add("--ar4k.beaconDiscoveryFilterString=TEST-REGISTER");
		baseArgsClientTwo.add("--ar4k.beaconDiscoveryPort=33666");
		baseArgsClientTwo.add("--ar4k.fileConfigOrder=1");
		baseArgsClientTwo.add("--ar4k.webConfigOrder=2");
		baseArgsClientTwo.add("--ar4k.dnsConfigOrder=3");
		baseArgsClientTwo.add("--ar4k.baseConfigOrder=0");
		baseArgsClientTwo.add("--ar4k.threadSleep=1000");
		baseArgsClientTwo.add("--ar4k.logoUrl=/static/img/ar4k.png");
		String certCaAsPem = "";
		baseArgsClientOne.add("--ar4k.beaconCaChainPem=" + certCaAsPem);
		baseArgsClientTwo.add("--ar4k.beaconCaChainPem=" + certCaAsPem);
		baseArgsClientThree.add("--ar4k.beaconCaChainPem=" + certCaAsPem);
		final EdgeConfig clientOneConfig = new EdgeConfig();
		final EdgeConfig clientTwoConfig = new EdgeConfig();
		final EdgeConfig clientThreeConfig = new EdgeConfig();
		testAnimas.put(CLIENT3_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "a.log",
						keyStoreClient3.getAbsolutePath(), 1124, baseArgsClientThree, clientThreeConfig,
						client3AliasInKeystore, signClient3AliasInKeystore, "https://localhost:11231")).get());
		testAnimas.put(CLIENT2_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "b.log",
						keyStoreClient2.getAbsolutePath(), 1125, baseArgsClientTwo, clientTwoConfig,
						client2AliasInKeystore, signClient2AliasInKeystore, "https://localhost:11231")).get());
		testAnimas.put(CLIENT1_LABEL,
				executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "c.log",
						keyStoreClient1.getAbsolutePath(), 1126, baseArgsClientOne, clientOneConfig,
						client1AliasInKeystore, signClient1AliasInKeystore, "https://localhost:11231")).get());
		for (final Homunculus a : testAnimas.values()) {
			Assert.assertEquals(a.getState(), Homunculus.HomunculusStates.RUNNING);
		}
		Thread.sleep(400000);
	}

}
