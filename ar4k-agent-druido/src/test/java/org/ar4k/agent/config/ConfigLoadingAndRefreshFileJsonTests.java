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
package org.ar4k.agent.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import org.ar4k.agent.activemq.ActiveMqConfig;
import org.ar4k.agent.camera.usb.StreamCameraConfig;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.Homunculus.HomunculusEvents;
import org.ar4k.agent.core.Homunculus.HomunculusStates;
import org.ar4k.agent.core.HomunculusSession;
import org.ar4k.agent.core.HomunculusStateMachineConfig;
import org.ar4k.agent.core.data.generator.DataGeneratorConfig;
import org.ar4k.agent.cortex.drools.DroolsConfig;
import org.ar4k.agent.hazelcast.HazelcastConfig;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.iot.serial.SerialConfig;
import org.ar4k.agent.mattermost.service.RossonetChatConfig;
import org.ar4k.agent.modbus.master.ModbusMasterConfig;
import org.ar4k.agent.modbus.slave.ModbusSlaveConfig;
import org.ar4k.agent.mqtt.client.PahoClientConfig;
import org.ar4k.agent.opcua.client.OpcUaClientConfig;
import org.ar4k.agent.opcua.server.OpcUaServerConfig;
import org.ar4k.agent.pcap.service.PcapSnifferConfig;
import org.ar4k.agent.spring.EdgeAuthenticationManager;
import org.ar4k.agent.spring.EdgeUserDetailsService;
import org.ar4k.agent.tunnels.http2.beacon.BeaconServiceConfig;
import org.ar4k.agent.tunnels.sshd.SshdHomunculusConfig;
import org.ar4k.agent.tunnels.sshd.SshdSystemConfig;
import org.ar4k.agent.watson.WatsonConfig;
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
@Import({ SpringShellAutoConfiguration.class, JLineShellAutoConfiguration.class, Homunculus.class,
		JCommanderParameterResolverAutoConfiguration.class, LegacyAdapterAutoConfiguration.class,
		StandardAPIAutoConfiguration.class, StandardCommandsAutoConfiguration.class, Commands.class,
		FileValueProvider.class, HomunculusStateMachineConfig.class, HomunculusSession.class,
		EdgeUserDetailsService.class, EdgeAuthenticationManager.class, BCryptPasswordEncoder.class })
@TestPropertySource(locations = "classpath:application-file.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ConfigLoadingAndRefreshFileJsonTests {

	@Autowired
	Homunculus homunculus;

	final String fileName = "/tmp/test-config.ar4k";

	@Before
	public void setUp() throws Exception {
		Thread.sleep(3000L);
		System.out.println(homunculus.getState());
	}

	@After
	public void tearDownAfterClass() throws Exception {
		Files.deleteIfExists(Paths.get(fileName));
	}

	@Rule
	public TestWatcher watcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
		}
	};

	@Test
	public void checkConfigFileWithReload() throws InterruptedException, IOException {
		EdgeConfig c = new EdgeConfig();
		String check = UUID.randomUUID().toString();
		c.name = "test salvataggio";
		c.author = check;
		BeaconServiceConfig bs = new BeaconServiceConfig();
		bs.name = "active mq";
		bs.note = check;
		c.pots.add(bs);
		ActiveMqConfig amq = new ActiveMqConfig();
		amq.name = "active mq";
		amq.note = check;
		c.pots.add(amq);
		DataGeneratorConfig dg = new DataGeneratorConfig();
		dg.name = "data generator";
		dg.note = check;
		c.pots.add(dg);
		DroolsConfig d = new DroolsConfig();
		d.name = "drools";
		d.note = check;
		c.pots.add(d);
		HazelcastConfig h = new HazelcastConfig();
		h.name = "hazelcast";
		h.note = check;
		c.pots.add(h);
		ModbusMasterConfig mbm = new ModbusMasterConfig();
		mbm.name = "modbus master";
		mbm.note = check;
		c.pots.add(mbm);
		ModbusSlaveConfig mbs = new ModbusSlaveConfig();
		mbs.name = "modbus slave";
		mbs.note = check;
		c.pots.add(mbs);
		OpcUaClientConfig opcc = new OpcUaClientConfig();
		opcc.name = "opc ua client";
		opcc.note = check;
		c.pots.add(opcc);
		OpcUaServerConfig opcs = new OpcUaServerConfig();
		opcs.name = "opc ua server";
		opcs.note = check;
		c.pots.add(opcs);
		PahoClientConfig p = new PahoClientConfig();
		p.name = "paho";
		p.note = check;
		c.pots.add(p);
		PcapSnifferConfig psc = new PcapSnifferConfig();
		psc.name = "pcap";
		psc.note = check;
		c.pots.add(psc);
		RossonetChatConfig mm = new RossonetChatConfig();
		mm.name = "matermost";
		mm.note = check;
		c.pots.add(mm);
		SerialConfig sc = new SerialConfig();
		sc.name = "serial";
		sc.note = check;
		c.pots.add(sc);
		SshdHomunculusConfig sshh = new SshdHomunculusConfig();
		sshh.name = "ssh homunculus";
		sshh.note = check;
		c.pots.add(sshh);
		SshdSystemConfig sshs = new SshdSystemConfig();
		sshs.name = "ssh system";
		sshs.note = check;
		c.pots.add(sshs);
		StreamCameraConfig cam = new StreamCameraConfig();
		cam.name = "camera";
		cam.note = check;
		c.pots.add(cam);
		WatsonConfig watson = new WatsonConfig();
		watson.name = "watson";
		watson.note = check;
		c.pots.add(watson);
		System.out.println("CONFIGURATION\n" + c);
		Files.write(Paths.get(fileName), ConfigHelper.toJson(c).getBytes(), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
		assertEquals(homunculus.getState(), HomunculusStates.STAMINAL);
		homunculus.sendEvent(HomunculusEvents.COMPLETE_RELOAD);
		Thread.sleep(3000);
		System.out.println(homunculus.getState());
		Thread.sleep(3000);
		assertEquals(homunculus.getState(), HomunculusStates.RUNNING);
		assertTrue(check.equals(homunculus.getRuntimeConfig().author));
		assertEquals(17, homunculus.getRuntimeConfig().pots.size());
	}

}
