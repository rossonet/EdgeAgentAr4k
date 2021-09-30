/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    */
package org.ar4k.qa.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.cert.CertificateEncodingException;

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.core.EdgeAgentCore;
import org.ar4k.agent.core.HomunculusSession;
import org.ar4k.agent.core.HomunculusStateMachineConfig;
import org.ar4k.agent.core.Homunculus.HomunculusStates;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.keystore.KeystoreConfig;
import org.ar4k.agent.spring.EdgeAuthenticationManager;
import org.ar4k.agent.spring.EdgeUserDetailsService;
import org.ar4k.agent.tunnels.http2.beacon.BeaconServiceConfig;
import org.bouncycastle.cms.CMSException;
import org.jline.builtins.Commands;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.shell.SpringShellAutoConfiguration;
import org.springframework.shell.jcommander.JCommanderParameterResolverAutoConfiguration;
import org.springframework.shell.jline.JLineShellAutoConfiguration;
import org.springframework.shell.legacy.LegacyAdapterAutoConfiguration;
import org.springframework.shell.standard.FileValueProvider;
import org.springframework.shell.standard.StandardAPIAutoConfiguration;
import org.springframework.shell.standard.commands.StandardCommandsAutoConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({ SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class, EdgeAgentCore.class,
		JCommanderParameterResolverAutoConfiguration.class, LegacyAdapterAutoConfiguration.class,
		StandardAPIAutoConfiguration.class, StandardCommandsAutoConfiguration.class, Commands.class,
		FileValueProvider.class, HomunculusStateMachineConfig.class, HomunculusSession.class,
		EdgeUserDetailsService.class, EdgeAuthenticationManager.class, BCryptPasswordEncoder.class })
@TestPropertySource(locations = "classpath:application-kstore-web.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class KeystoreLoadingWebTests {

	@Autowired
	EdgeAgentCore edgeAgentCore;

	@Before
	public void setUp() throws Exception {
		Thread.sleep(3000L);
		System.out.println(edgeAgentCore.getState());
	}

	@After
	public void tearDownAfterClass() throws Exception {
		Files.deleteIfExists(Paths.get("removed-keystore.ks"));
	}

	@Rule
	public TestWatcher watcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
		}
	};

	@Test
	public void downloadKeystoreWeb() throws InterruptedException {
		Thread.sleep(5000L);
		assertTrue(edgeAgentCore.getMyIdentityKeystore().check());
		System.out.println(
				edgeAgentCore.getMyIdentityKeystore().getClientCertificate("ca").getSubjectX500Principal().getName());
		assertEquals(
				edgeAgentCore.getMyIdentityKeystore().getClientCertificate("ca").getSubjectX500Principal().getName(),
				"C=IT,ST=Bologna,L=Imola,OU=Ar4k,O=Rossonet,CN=ciospo.rossonet.net_a58fdf077b864f2bafc3b9b83f2d5143-master");
		assertEquals(HomunculusStates.RUNNING, edgeAgentCore.getState());
		assertTrue("pcryptoAA".equals(edgeAgentCore.getRuntimeConfig().author));
		assertTrue("webconfig".equals(edgeAgentCore.getRuntimeConfig().name));
		assertTrue("AFYU8K".equals(edgeAgentCore.getRuntimeConfig().tagVersion));
		System.out.println(
				"NOTE 0 -> " + ((BeaconServiceConfig) edgeAgentCore.getRuntimeConfig().pots.toArray()[0]).note);
		assertTrue("345F".equals(((BeaconServiceConfig) edgeAgentCore.getRuntimeConfig().pots.toArray()[0]).note));
		System.out.println(
				"NOTE 1 -> " + ((BeaconServiceConfig) edgeAgentCore.getRuntimeConfig().pots.toArray()[1]).note);
		assertTrue("345F".equals(((BeaconServiceConfig) edgeAgentCore.getRuntimeConfig().pots.toArray()[1]).note));
	}

	@Test
	public void createConfigWeb() throws IOException, CertificateEncodingException, CMSException {
		EdgeConfig config = new EdgeConfig();
		config.author = "pcryptoAA";
		config.name = "webconfig";
		config.tagVersion = "AFYU8K";
		BeaconServiceConfig s0 = new BeaconServiceConfig();
		s0.setNote("345F");
		s0.name = "socket-0";
		BeaconServiceConfig s1 = new BeaconServiceConfig();
		s1.setNote("345F");
		s1.name = "socket-1";
		config.pots.add(s0);
		config.pots.add(s1);
		Files.write(Paths.get("crypto.test-config.a.ar4k"), ConfigHelper.toBase64Crypto(config, "ca").getBytes(),
				StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
	}

	private void createKeystore() throws IOException, CertificateEncodingException, CMSException {
		KeystoreConfig keyStore = new KeystoreConfig();
		keyStore.keystorePassword = "secA4.rk!8";
		keyStore.keyStoreAlias = "new-keystore";
		keyStore.filePathPre = "default-test-config.keystore";
		// "C=IT,ST=Bologna,L=Imola,OU=Ar4k,O=Rossonet,CN=ciospo.rossonet.net_a58fdf077b864f2bafc3b9b83f2d5143-master"
		keyStore.create("ciospo.rossonet.net_a58fdf077b864f2bafc3b9b83f2d5143-master", "Rossonet", "Ar4k", "Imola",
				"Bologna", "IT", "urn:org.ar4k.agent:ca_rossonet-key-agent", "test.data.com", "0.0.0.0", "ca", true,
				300 * 10);
	}
}
