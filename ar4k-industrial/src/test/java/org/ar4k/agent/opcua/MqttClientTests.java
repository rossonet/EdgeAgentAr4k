package org.ar4k.agent.opcua;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.HomunculusSession;
import org.ar4k.agent.core.HomunculusStateMachineConfig;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.DataAddressHomunculus;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.industrial.IndustrialShellInterface;
import org.ar4k.agent.opcua.client.OpcUaClientConfig;
import org.ar4k.agent.opcua.client.OpcUaClientNodeConfig;
import org.ar4k.agent.opcua.client.OpcUaClientService;
import org.ar4k.agent.spring.EdgeAuthenticationManager;
import org.ar4k.agent.spring.EdgeUserDetailsService;
import org.jline.builtins.Commands;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.shell.Shell;
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
		EdgeUserDetailsService.class, EdgeAuthenticationManager.class, BCryptPasswordEncoder.class,
		IndustrialShellInterface.class })
@TestPropertySource(locations = "classpath:application-opc-ua.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class MqttClientTests implements MessageHandler {

	@Autowired
	Homunculus homunculus;

	@Autowired
	Shell shell;

	@Before
	public void setUp() throws Exception {
		System.out.println(homunculus.getState());
	}

	@After
	public void tearDownAfterClass() throws Exception {
		homunculus.close();
	}

	@Rule
	public TestWatcher watcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
		}
	};

	@Test
	@Ignore
	public void connectOpcUaClientToLocalProsys() throws Exception {
		OpcUaClientConfig opcUaClientConfig = new OpcUaClientConfig();
		opcUaClientConfig.setName("test-opc");
		OpcUaClientNodeConfig nodeConfig1 = new OpcUaClientNodeConfig();
		nodeConfig1.nodeId = "ns=5;s=Counter1";
		nodeConfig1.readChannel = "counter";
		OpcUaClientNodeConfig nodeConfig2 = new OpcUaClientNodeConfig();
		nodeConfig2.nodeId = "ns=5;s=Expression1";
		nodeConfig2.readChannel = "expression";
		OpcUaClientNodeConfig nodeConfig3 = new OpcUaClientNodeConfig();
		nodeConfig3.nodeId = "ns=5;s=Random1";
		nodeConfig3.readChannel = "random";
		OpcUaClientNodeConfig nodeConfig4 = new OpcUaClientNodeConfig();
		nodeConfig4.nodeId = "ns=5;s=Sawtooth1";
		nodeConfig4.readChannel = "sawtooth";
		OpcUaClientNodeConfig nodeConfig5 = new OpcUaClientNodeConfig();
		nodeConfig5.nodeId = "ns=5;s=Sinusoid1";
		nodeConfig5.readChannel = "sinusoid";
		OpcUaClientNodeConfig nodeConfig6 = new OpcUaClientNodeConfig();
		nodeConfig6.nodeId = "ns=5;s=Square1";
		nodeConfig6.readChannel = "square";
		OpcUaClientNodeConfig nodeConfig7 = new OpcUaClientNodeConfig();
		nodeConfig7.nodeId = "ns=5;s=Triangle1";
		nodeConfig7.readChannel = "triangle";
		opcUaClientConfig.subscriptions.add(nodeConfig1);
		opcUaClientConfig.subscriptions.add(nodeConfig2);
		opcUaClientConfig.subscriptions.add(nodeConfig3);
		opcUaClientConfig.subscriptions.add(nodeConfig4);
		opcUaClientConfig.subscriptions.add(nodeConfig5);
		opcUaClientConfig.subscriptions.add(nodeConfig6);
		opcUaClientConfig.subscriptions.add(nodeConfig7);
		OpcUaClientService service = new OpcUaClientService();
		service.setConfiguration(opcUaClientConfig);
		service.setHomunculus(homunculus);
		final DataAddress dataAddressService = new DataAddress(homunculus, service);
		service.setDataAddress(dataAddressService);
		homunculus.getDataAddress().registerSlave(service);
		service.init();
		Thread.sleep(10000);
		final DataAddressHomunculus dataAddress = homunculus.getDataAddress();
		System.out.println("channels -> " + dataAddress.listChannels());
		((IPublishSubscribeChannel) dataAddress.getChannel("test-opc/counter")).subscribe(this);
		((IPublishSubscribeChannel) dataAddress.getChannel("test-opc/expression")).subscribe(this);
		((IPublishSubscribeChannel) dataAddress.getChannel("test-opc/random")).subscribe(this);
		((IPublishSubscribeChannel) dataAddress.getChannel("test-opc/sawtooth")).subscribe(this);
		((IPublishSubscribeChannel) dataAddress.getChannel("test-opc/sinusoid")).subscribe(this);
		((IPublishSubscribeChannel) dataAddress.getChannel("test-opc/square")).subscribe(this);
		((IPublishSubscribeChannel) dataAddress.getChannel("test-opc/triangle")).subscribe(this);
		Thread.sleep(40000);
		service.close();
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		System.out.println(message.getPayload().toString());
	}

}
