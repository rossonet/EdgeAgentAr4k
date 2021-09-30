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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.core.EdgeAgentCore;
import org.ar4k.agent.core.HomunculusSession;
import org.ar4k.agent.core.HomunculusStateMachineConfig;
import org.ar4k.agent.core.Homunculus.HomunculusEvents;
import org.ar4k.agent.core.Homunculus.HomunculusStates;
import org.ar4k.agent.core.data.DataBag;
import org.ar4k.agent.core.data.DataChannelFilter;
import org.ar4k.agent.core.data.IDataChannelFilter.Label;
import org.ar4k.agent.core.data.IDataChannelFilter.Operator;
import org.ar4k.agent.core.data.FilterLine;
import org.ar4k.agent.core.data.channels.EdgeChannel;
import org.ar4k.agent.core.data.channels.IDirectChannel;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.generator.DataGeneratorConfig;
import org.ar4k.agent.core.data.generator.SingleDataGeneratorPointConfig;
import org.ar4k.agent.core.data.generator.SingleDataGeneratorPointConfig.ChannelType;
import org.ar4k.agent.core.data.generator.SingleDataGeneratorPointConfig.DataGeneratorMode;
import org.ar4k.agent.core.data.generator.SingleDataGeneratorPointConfig.DataType;
import org.ar4k.agent.core.data.messages.BooleanMessage;
import org.ar4k.agent.core.data.messages.FloatMessage;
import org.ar4k.agent.core.data.messages.IntegerMessage;
import org.ar4k.agent.core.data.messages.JSONMessage;
import org.ar4k.agent.core.data.messages.LongMessage;
import org.ar4k.agent.core.data.messages.StringMessage;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.spring.EdgeAuthenticationManager;
import org.ar4k.agent.spring.EdgeUserDetailsService;
import org.assertj.core.util.Lists;
import org.jline.builtins.Commands;
import org.json.JSONObject;
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
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
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
@TestPropertySource(locations = "classpath:application-data-simulator.properties")
@SpringBootConfiguration
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DataSimulatorTests implements MessageHandler {

	@Autowired
	EdgeAgentCore edgeAgentCore;

	final String fileName = "/tmp/test-data-simulator.ar4k";
	final String dataBagFile = "/tmp/test-databag.bin";

	@Before
	public void setUp() throws Exception {
		Files.deleteIfExists(Paths.get(fileName));
		Files.deleteIfExists(Paths.get(dataBagFile));
		messages.clear();
		Thread.sleep(3000L);
		System.out.println(edgeAgentCore.getState());
	}

	@After
	public void tearDownAfterClass() throws Exception {
		Files.deleteIfExists(Paths.get(fileName));
		Files.deleteIfExists(Paths.get(dataBagFile));
	}

	@Rule
	public TestWatcher watcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
		}
	};

	private final List<Message<?>> messages = Collections.synchronizedList(new ArrayList<>());

	@Test
	public void checkOnePointSimulatorLong() throws InterruptedException, IOException {
		final EdgeConfig c = new EdgeConfig();
		final String check = UUID.randomUUID().toString();
		c.name = "test salvataggio conf";
		c.author = check;
		final DataGeneratorConfig s1 = new DataGeneratorConfig();
		s1.name = "data simulator";
		final SingleDataGeneratorPointConfig sp = new SingleDataGeneratorPointConfig();
		sp.delta = 1;
		sp.description = "test point";
		sp.rangeHi = 10;
		sp.rangeLower = 5;
		sp.frequency = 500;
		sp.nodeId = "TestData";
		sp.namespace = "testing";
		sp.tags = Lists.list("test", "prova", "single-point");
		sp.typeChannel = ChannelType.PublishSubscribe;
		sp.typeData = DataType.LONG;
		sp.typeSimulator = DataGeneratorMode.RANDOM;
		s1.datas.add(sp);
		c.pots.add(s1);
		Files.write(Paths.get(fileName), ConfigHelper.toBase64(c).getBytes(), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
		edgeAgentCore.sendEvent(HomunculusEvents.COMPLETE_RELOAD);
		Thread.sleep(3000);
		System.out.println(edgeAgentCore.getState());
		Thread.sleep(3000);
		System.out.println("configured channels -> " + edgeAgentCore.getDataAddress().listChannels());
		((IPublishSubscribeChannel) edgeAgentCore.getDataAddress().getChannel("data-generator/TestData"))
				.subscribe(this);
		assertEquals(edgeAgentCore.getState(), HomunculusStates.RUNNING);
		assertTrue(check.equals(edgeAgentCore.getRuntimeConfig().author));
		Thread.sleep(20000);
		boolean found = false;
		for (final Object single : messages) {
			final Long value = ((LongMessage) single).getPayload();
			if (value == 7) {
				found = true;
				break;
			}
		}
		assertTrue(found);
	}

	@Test
	public void checkOnePointSimulatorBoolean() throws InterruptedException, IOException {
		final EdgeConfig c = new EdgeConfig();
		final String check = UUID.randomUUID().toString();
		c.name = "test salvataggio conf";
		c.author = check;
		final DataGeneratorConfig s1 = new DataGeneratorConfig();
		s1.name = "data simulator";
		final SingleDataGeneratorPointConfig sp = new SingleDataGeneratorPointConfig();
		sp.delta = 1;
		sp.description = "test point";
		sp.rangeHi = 10;
		sp.rangeLower = 5;
		sp.frequency = 1000;
		sp.nodeId = "TestBooleanData";
		sp.namespace = "testing";
		sp.tags = Lists.list("test", "prova", "single-point");
		sp.typeChannel = ChannelType.PublishSubscribe;
		sp.typeData = DataType.BOOLEAN;
		sp.typeSimulator = DataGeneratorMode.INCREMENTAL;
		s1.datas.add(sp);
		c.pots.add(s1);
		Files.write(Paths.get(fileName), ConfigHelper.toBase64(c).getBytes(), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
		edgeAgentCore.sendEvent(HomunculusEvents.COMPLETE_RELOAD);
		Thread.sleep(3000);
		System.out.println(edgeAgentCore.getState());
		Thread.sleep(3000);
		System.out.println("configured channels -> " + edgeAgentCore.getDataAddress().listChannels());
		((IPublishSubscribeChannel) edgeAgentCore.getDataAddress().getChannel("data-generator/TestBooleanData"))
				.subscribe(this);
		assertEquals(edgeAgentCore.getState(), HomunculusStates.RUNNING);
		assertTrue(check.equals(edgeAgentCore.getRuntimeConfig().author));
		Thread.sleep(10000);
		boolean foundTrue = false;
		boolean foundFalse = false;
		for (final Object single : messages) {
			final Boolean value = ((BooleanMessage) single).getPayload();
			if (value) {
				foundTrue = true;
			} else {
				foundFalse = true;
			}
			if (foundTrue && foundFalse) {
				break;
			}
		}
		assertTrue(foundTrue);
		assertTrue(foundFalse);
	}

	@Test
	public void checkOnePointSimulatorString() throws InterruptedException, IOException {
		final EdgeConfig c = new EdgeConfig();
		final String check = UUID.randomUUID().toString();
		c.name = "test salvataggio conf";
		c.author = check;
		final DataGeneratorConfig s1 = new DataGeneratorConfig();
		s1.name = "data simulator";
		final SingleDataGeneratorPointConfig sp = new SingleDataGeneratorPointConfig();
		sp.delta = 1;
		sp.description = "test point";
		sp.rangeHi = 10;
		sp.rangeLower = 5;
		sp.frequency = 500;
		sp.nodeId = "TestData";
		sp.namespace = "testing";
		sp.tags = Lists.list("test", "prova", "single-point");
		sp.typeChannel = ChannelType.PublishSubscribe;
		sp.typeData = DataType.STRING;
		sp.typeSimulator = DataGeneratorMode.RANDOM;
		s1.datas.add(sp);
		c.pots.add(s1);
		Files.write(Paths.get(fileName), ConfigHelper.toBase64(c).getBytes(), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
		edgeAgentCore.sendEvent(HomunculusEvents.COMPLETE_RELOAD);
		Thread.sleep(3000);
		System.out.println(edgeAgentCore.getState());
		Thread.sleep(3000);
		System.out.println("configured channels -> " + edgeAgentCore.getDataAddress().listChannels());
		((IPublishSubscribeChannel) edgeAgentCore.getDataAddress().getChannel("data-generator/TestData"))
				.subscribe(this);
		assertEquals(edgeAgentCore.getState(), HomunculusStates.RUNNING);
		assertTrue(check.equals(edgeAgentCore.getRuntimeConfig().author));
		Thread.sleep(20000);
		boolean found = true;
		for (final Object single : messages) {
			final String value = ((StringMessage) single).getPayload();
			int foundTime = 0;
			for (final Object checkSingle : messages) {
				final String valueCheck = ((StringMessage) checkSingle).getPayload();
				if (valueCheck.equals(value)) {
					foundTime++;
				}
			}
			if ((!value.contains("-")) || foundTime != 1) {
				found = false;
				break;
			}
		}
		assertTrue(found);
		assertTrue(messages.size() > 10);
	}

	@Test
	public void checkOnePointSimulatorJsonNumeric() throws InterruptedException, IOException {
		final EdgeConfig c = new EdgeConfig();
		final String check = UUID.randomUUID().toString();
		c.name = "test salvataggio conf";
		c.author = check;
		final DataGeneratorConfig s1 = new DataGeneratorConfig();
		s1.name = "data simulator";
		final SingleDataGeneratorPointConfig sp = new SingleDataGeneratorPointConfig();
		sp.delta = 1;
		sp.description = "test point";
		sp.rangeHi = 10;
		sp.rangeLower = 5;
		sp.frequency = 50;
		sp.nodeId = "TestData";
		sp.namespace = "testing";
		sp.patternJson = "{" + "\"k\":\"%nodeId%\"," + "\"ts\":%time%," + "\"data\":%value%" + "}";
		sp.tags = Lists.list("test", "prova", "single-point");
		sp.typeChannel = ChannelType.DirectChannel;
		sp.typeData = DataType.JSON_NUMERIC;
		sp.typeSimulator = DataGeneratorMode.RANDOM;
		s1.datas.add(sp);
		c.pots.add(s1);
		Files.write(Paths.get(fileName), ConfigHelper.toBase64(c).getBytes(), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
		edgeAgentCore.sendEvent(HomunculusEvents.COMPLETE_RELOAD);
		Thread.sleep(3000);
		System.out.println(edgeAgentCore.getState());
		Thread.sleep(3000);
		System.out.println("configured channels -> " + edgeAgentCore.getDataAddress().listChannels());
		((IDirectChannel) edgeAgentCore.getDataAddress().getChannel("data-generator/TestData")).subscribe(this);
		assertEquals(edgeAgentCore.getState(), HomunculusStates.RUNNING);
		assertTrue(check.equals(edgeAgentCore.getRuntimeConfig().author));
		Thread.sleep(20000);
		boolean found = false;
		for (final Object single : messages) {
			final JSONObject json = ((JSONMessage) single).getPayload();
			final double value = json.getDouble("data");
			if (value < 8 && value > 7) {
				found = true;
				break;
			}
		}
		assertTrue(found);
		assertTrue(messages.size() > 10);
	}

	@Test
	public void checkOnePointSimulatorJsonString() throws InterruptedException, IOException {
		final EdgeConfig c = new EdgeConfig();
		final String check = UUID.randomUUID().toString();
		c.name = "test salvataggio conf";
		c.author = check;
		final DataGeneratorConfig s1 = new DataGeneratorConfig();
		s1.name = "data simulator";
		final SingleDataGeneratorPointConfig sp = new SingleDataGeneratorPointConfig();
		sp.delta = 1;
		sp.description = "test point";
		sp.rangeHi = 10;
		sp.rangeLower = 5;
		sp.frequency = 500;
		sp.nodeId = "TestData";
		sp.namespace = "testing";
		sp.patternJson = "{" + "\"k\":\"%nodeId%\"," + "\"ts\":%time%," + "\"data\":%value%" + "}";
		sp.tags = Lists.list("test", "prova", "single-point");
		sp.typeChannel = ChannelType.DirectChannel;
		sp.typeData = DataType.JSON_STRING;
		sp.typeSimulator = DataGeneratorMode.RANDOM;
		s1.datas.add(sp);
		c.pots.add(s1);
		Files.write(Paths.get(fileName), ConfigHelper.toBase64(c).getBytes(), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
		edgeAgentCore.sendEvent(HomunculusEvents.COMPLETE_RELOAD);
		Thread.sleep(3000);
		System.out.println(edgeAgentCore.getState());
		Thread.sleep(3000);
		System.out.println("configured channels -> " + edgeAgentCore.getDataAddress().listChannels());
		((IDirectChannel) edgeAgentCore.getDataAddress().getChannel("data-generator/TestData")).subscribe(this);
		assertEquals(edgeAgentCore.getState(), HomunculusStates.RUNNING);
		assertTrue(check.equals(edgeAgentCore.getRuntimeConfig().author));
		Thread.sleep(20000);
		boolean found = true;
		for (final Object single : messages) {
			final JSONObject json = ((JSONMessage) single).getPayload();
			final String value = json.getString("data");
			int foundTime = 0;
			for (final Object checkSingle : messages) {
				final JSONObject jsonCheck = ((JSONMessage) checkSingle).getPayload();
				final String valueCheck = jsonCheck.getString("data");
				if (valueCheck.equals(value)) {
					foundTime++;
				}
			}
			if ((!value.contains("-")) || foundTime != 1) {
				found = false;
				break;
			}
		}
		assertTrue(found);
		assertTrue(messages.size() > 10);
	}

	@Test
	public void checkOnePointSimulatorInteger() throws InterruptedException, IOException {
		final EdgeConfig c = new EdgeConfig();
		final String check = UUID.randomUUID().toString();
		c.name = "test salvataggio conf";
		c.author = check;
		final DataGeneratorConfig s1 = new DataGeneratorConfig();
		s1.name = "data simulator";
		final SingleDataGeneratorPointConfig sp = new SingleDataGeneratorPointConfig();
		sp.delta = 2;
		sp.description = "test point";
		sp.rangeHi = 15;
		sp.rangeLower = 0;
		sp.frequency = 500;
		sp.nodeId = "TestData";
		sp.namespace = "testing";
		sp.tags = Lists.list("test", "prova", "single-point");
		sp.typeChannel = ChannelType.PublishSubscribe;
		sp.typeData = DataType.INTEGER;
		sp.typeSimulator = DataGeneratorMode.INCREMENTAL;
		s1.datas.add(sp);
		c.pots.add(s1);
		Files.write(Paths.get(fileName), ConfigHelper.toBase64(c).getBytes(), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
		edgeAgentCore.sendEvent(HomunculusEvents.COMPLETE_RELOAD);
		Thread.sleep(3000);
		System.out.println(edgeAgentCore.getState());
		Thread.sleep(3000);
		System.out.println("configured channels -> " + edgeAgentCore.getDataAddress().listChannels());
		((IPublishSubscribeChannel) edgeAgentCore.getDataAddress().getChannel("data-generator/TestData"))
				.subscribe(this);
		assertEquals(edgeAgentCore.getState(), HomunculusStates.RUNNING);
		assertTrue(check.equals(edgeAgentCore.getRuntimeConfig().author));
		Thread.sleep(20000);
		boolean found = false;
		for (final Object single : messages) {
			final Integer value = ((IntegerMessage) single).getPayload();
			if (value == 8) {
				found = true;
				break;
			}
		}
		assertTrue(found);
	}

	@Test
	public void checkOnePointSimulatorDataBag() throws InterruptedException, IOException {
		final EdgeConfig c = new EdgeConfig();
		final String check = UUID.randomUUID().toString();
		c.name = "test salvataggio conf";
		c.author = check;
		final DataGeneratorConfig s1 = new DataGeneratorConfig();
		s1.name = "data simulator";
		final SingleDataGeneratorPointConfig sp = new SingleDataGeneratorPointConfig();
		sp.delta = 2;
		sp.description = "test point";
		sp.rangeHi = 6000;
		sp.rangeLower = 5000;
		sp.frequency = 1000;
		sp.nodeId = "TestDataGood";
		sp.namespace = "testing";
		sp.domainId = "mydom";
		sp.tags = Lists.list("test", "prova", "single-point", "good");
		sp.typeChannel = ChannelType.PublishSubscribe;
		sp.typeData = DataType.INTEGER;
		sp.typeSimulator = DataGeneratorMode.INCREMENTAL;
		s1.datas.add(sp);
		c.pots.add(s1);
		final DataGeneratorConfig s2 = new DataGeneratorConfig();
		s2.name = "data not good";
		final SingleDataGeneratorPointConfig sp2 = new SingleDataGeneratorPointConfig();
		sp2.delta = 2;
		sp2.description = "test point";
		sp2.rangeHi = 40000;
		sp2.rangeLower = 6002;
		sp2.frequency = 400;
		sp2.nodeId = "TestDataBad";
		sp2.namespace = "testing";
		sp2.domainId = "mydom";
		sp2.tags = Lists.list("test", "prova", "single-point", "not-good");
		sp2.typeChannel = ChannelType.PublishSubscribe;
		sp2.typeData = DataType.INTEGER;
		sp2.typeSimulator = DataGeneratorMode.INCREMENTAL;
		s2.datas.add(sp2);
		c.pots.add(s2);
		Files.write(Paths.get(fileName), ConfigHelper.toBase64(c).getBytes(), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
		edgeAgentCore.sendEvent(HomunculusEvents.COMPLETE_RELOAD);
		Thread.sleep(3000);
		System.out.println(edgeAgentCore.getState());
		Thread.sleep(3000);
		System.out.println("configured channels -> " + edgeAgentCore.getDataAddress().listChannels());
		((IPublishSubscribeChannel) edgeAgentCore.getDataAddress().getChannel("data-generator/TestDataGood"))
				.subscribe(this);
		((IPublishSubscribeChannel) edgeAgentCore.getDataAddress().getChannel("data-generator/TestDataBad"))
				.subscribe(this);
		final List<FilterLine> filters = new ArrayList<>();
		final FilterLine tagLine = new FilterLine(Operator.AND, Label.TAG, Lists.list("prova", "single-point", "good"),
				Operator.AND);
		filters.add(tagLine);
		final FilterLine domainLine = new FilterLine(Operator.AND, Label.DOMAIN, Lists.list("mydom"), Operator.OR);
		filters.add(domainLine);
		final FilterLine nameSpaceLine = new FilterLine(Operator.AND, Label.NAME_SPACE, Lists.list("testing"),
				Operator.AND);
		filters.add(nameSpaceLine);
		final DataChannelFilter dataChannelFilter = new DataChannelFilter(filters);
		assertEquals(edgeAgentCore.getState(), HomunculusStates.RUNNING);
		assertTrue(check.equals(edgeAgentCore.getRuntimeConfig().author));
		final Collection<EdgeChannel> allChannels = edgeAgentCore.getDataAddress().getDataChannels(dataChannelFilter);
		for (final EdgeChannel channel : allChannels) {
			System.out.println("found -> " + channel);
		}
		assertEquals(1, allChannels.size());
		final DataBag db = new DataBag(new File(dataBagFile), allChannels, 500, 1000);
		Thread.sleep(30000);
		db.close();
		final EdgeConfig c3 = new EdgeConfig();
		final String check3 = UUID.randomUUID().toString();
		c3.name = "test salvataggio conf";
		c3.author = check3;
		final DataGeneratorConfig s3 = new DataGeneratorConfig();
		s3.name = "data simulator";
		final SingleDataGeneratorPointConfig sp3 = new SingleDataGeneratorPointConfig();
		sp3.description = "test point databag";
		sp3.frequency = 500;
		sp3.delta = 5000;
		sp3.nodeId = "TestData";
		sp3.namespace = "testing";
		sp3.domainId = "mydom";
		sp3.tags = Lists.list("test", "prova", "single-point", "good", "data-bag");
		sp3.typeChannel = ChannelType.PublishSubscribe;
		sp3.typeSimulator = DataGeneratorMode.DATA_BAG;
		sp3.dataBagFilePath = dataBagFile;
		s3.datas.add(sp3);
		c3.pots.add(s3);
		Files.write(Paths.get(fileName), ConfigHelper.toBase64(c3).getBytes(), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
		edgeAgentCore.sendEvent(HomunculusEvents.COMPLETE_RELOAD);
		messages.clear();
		Thread.sleep(3000);
		System.out.println(edgeAgentCore.getState());
		((IPublishSubscribeChannel) edgeAgentCore.getDataAddress().getChannel("data-generator/TestData"))
				.subscribe(this);
		Thread.sleep(40000);
		edgeAgentCore.sendEvent(HomunculusEvents.STOP);
		for (final Integer checkValue : Lists.newArrayList(5014, 5016, 5018, 5020, 5022, 5024, 5026, 5028, 5030, 5032,
				5034, 5036, 5038, 5040, 5042, 5044, 5046, 5048, 5050, 5052)) {
			boolean found = false;
			final StringBuilder checkedValues = new StringBuilder();
			for (final Object single : messages) {
				final Integer value = ((IntegerMessage) single).getPayload();
				checkedValues.append(value + ", ");
				assertTrue(value < 6000);
				if (value.equals(checkValue)) {
					found = true;
					break;
				}
			}
			if (!found) {
				System.out.println(checkValue + " not found in " + checkedValues.toString());
			}
			assertTrue(found);
		}
	}

	@Test
	public void checkOnePointSimulatorFloat() throws InterruptedException, IOException {
		final EdgeConfig c = new EdgeConfig();
		final String check = UUID.randomUUID().toString();
		c.name = "test salvataggio conf";
		c.author = check;
		final DataGeneratorConfig s1 = new DataGeneratorConfig();
		s1.name = "data simulator";
		final SingleDataGeneratorPointConfig sp = new SingleDataGeneratorPointConfig();
		sp.delta = 1;
		sp.description = "test point";
		sp.rangeHi = 10000;
		sp.rangeLower = 0;
		sp.height = 1000;
		sp.frequency = 200;
		sp.nodeId = "TestData";
		sp.namespace = "testing";
		sp.tags = Lists.list("test", "prova", "single-point");
		sp.typeChannel = ChannelType.PublishSubscribe;
		sp.typeData = DataType.FLOAT;
		sp.typeSimulator = DataGeneratorMode.SINUSOID;
		s1.datas.add(sp);
		c.pots.add(s1);
		Files.write(Paths.get(fileName), ConfigHelper.toBase64(c).getBytes(), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
		edgeAgentCore.sendEvent(HomunculusEvents.COMPLETE_RELOAD);
		Thread.sleep(3000);
		System.out.println(edgeAgentCore.getState());
		Thread.sleep(3000);
		System.out.println("configured channels -> " + edgeAgentCore.getDataAddress().listChannels());
		((IPublishSubscribeChannel) edgeAgentCore.getDataAddress().getChannel("data-generator/TestData"))
				.subscribe(this);
		assertEquals(edgeAgentCore.getState(), HomunculusStates.RUNNING);
		assertTrue(check.equals(edgeAgentCore.getRuntimeConfig().author));
		Thread.sleep(20000);
		boolean found = false;
		for (final Object single : messages) {
			final Float value = ((FloatMessage) single).getPayload();
			if (value == 1000) {
				found = true;
				break;
			}
		}
		assertTrue(found);
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		messages.add(message);
		System.out.println(message.getPayload().toString());
	}

}
