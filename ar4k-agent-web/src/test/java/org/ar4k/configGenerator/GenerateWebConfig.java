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
package org.ar4k.configGenerator;

import java.io.IOException;

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.core.EdgeAgentCore;
import org.ar4k.agent.core.HomunculusSession;
import org.ar4k.agent.core.HomunculusStateMachineConfig;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.spring.EdgeAuthenticationManager;
import org.ar4k.agent.spring.EdgeUserDetailsService;
import org.ar4k.agent.tunnels.http2.beacon.BeaconServiceConfig;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@Import({ SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class, EdgeAgentCore.class,
		JCommanderParameterResolverAutoConfiguration.class, LegacyAdapterAutoConfiguration.class,
		StandardAPIAutoConfiguration.class, StandardCommandsAutoConfiguration.class, Commands.class,
		FileValueProvider.class, HomunculusStateMachineConfig.class, HomunculusSession.class,
		EdgeUserDetailsService.class, EdgeAuthenticationManager.class, BCryptPasswordEncoder.class })
//@TestPropertySource(locations = "classpath:application.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class GenerateWebConfig {

	@Autowired
	EdgeAgentCore homunculus;

	@Before
	public void setUp() throws Exception {
		Thread.sleep(3000L);
		System.out.println(homunculus.getState());
	}

	@After
	public void tearDownAfterClass() throws Exception {

	}

	@Rule
	public TestWatcher watcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
		}
	};

	@Test
	public void createConfigWeb() throws IOException {
		EdgeConfig config = new EdgeConfig();
		config.author = "Andrea Ambrosini";
		config.name = "config-web-test";
		BeaconServiceConfig s0 = new BeaconServiceConfig();
		s0.acceptAllCerts = false;
		s0.port = 11231;
		s0.name = "socket-0";
		s0.aliasBeaconServerSignMaster = "ca";
		s0.caChainPem = "MIIETjCCAzagAwIBAgIGAXeNmgovMA0GCSqGSIb3DQEBCwUAMIGXMUQwQgYDVQQDDDtsZWdpb24tcm9zc29uZXQtY29tX2E0ZjhjODY0M2ZiYTQ2M2E5ZmJiZjg1YzE1NDRlMTZiLW1hc3RlcjERMA8GA1UECgwIUm9zc29uZXQxDTALBgNVBAsMBEFyNGsxDjAMBgNVBAcMBUltb2xhMRAwDgYDVQQIDAdCb2xvZ25hMQswCQYDVQQGEwJJVDAeFw0yMTAyMDkyMzAwMDBaFw0yNDAyMDkyMzAwMDBaMIGXMUQwQgYDVQQDDDtsZWdpb24tcm9zc29uZXQtY29tX2E0ZjhjODY0M2ZiYTQ2M2E5ZmJiZjg1YzE1NDRlMTZiLW1hc3RlcjERMA8GA1UECgwIUm9zc29uZXQxDTALBgNVBAsMBEFyNGsxDjAMBgNVBAcMBUltb2xhMRAwDgYDVQQIDAdCb2xvZ25hMQswCQYDVQQGEwJJVDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAL4eqTC+6LtEk+WtEA/SwSFPg1qnpiBYPpcwlUpEocFkTMYZ7VSoC5XVNiXfhcDz3DV+QZceQSrgJiNmFegooYfnD9DxpABbnztCwSw/zr81h6s2GmiXcugTHc4+RuCOwAhWteQlas3OB+JB1dGRwfk64A/IH3fjmb3Rped6TdTsGb9Yh6Ah/c2+yATHDNMaTuyu5A+8epcEP5P8qZKaATG4W5jUHszqYR0axQC10oOzZORVCcImb3KOylYMte36HTVA+NFXmpZ8v18YobMoK4X+znyC41aSg6dVZ9rINtCLs4a5+p7d3sMixuO0JRHpjzcvZcWOKIw8t0y5wOIWCDECAwEAAaOBnTCBmjAPBgNVHRMBAf8EBTADAQH/MAsGA1UdDwQEAwIB/jBbBgNVHREEVDBShhhodHRwczovL3d3dy5yb3Nzb25ldC5uZXSCE2xlZ2lvbi1yb3Nzb25ldC1jb22CCWxvY2FsaG9zdIcEfwAAAYcEwKgAdocEwKh6AYcErBEAATAdBgNVHQ4EFgQUf3mDCX5j5jhciXJobq30BOm0soEwDQYJKoZIhvcNAQELBQADggEBAEWt/I9/QUN/PExrx8UuxiX5lvg5HS68Ipl0YPcV0nqNf+3hCPCpiyhlf3F+g+83BvWUei1VBRucHkFfiy5qcCu1c+ODt7qBTPEeN27DYIfLCQGizas0U2gShAB56rbQKwsbTuZFyyT5SF1eZBxdOL2y7adH/RN/PswbR/dfw/lWr+jCbPHLX4HCyozqc+2IMNMRpeUR6EUlvuh70sD1wF4cLAns1UzQw35cfuiG+jWztLfviGnQ+NSbvgoHpT0sItq+6nHvprxl9ub2at980V04vSxZfh3DOj/PR8BzHogJK2cvKpUMedkDpVs32wG3DpPHdptGca7HKT4IJHiWbqA=";
		config.pots.add(s0);
		System.out.println(ConfigHelper.toBase64(config));
	}

}
