package org.ar4k.agent.core.data.generator;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.channels.IDirectChannel;
import org.ar4k.agent.core.data.channels.IExecutorChannel;
import org.ar4k.agent.core.data.channels.IPriorityChannel;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.channels.IQueueChannel;
import org.ar4k.agent.core.data.channels.IRendezvousChannel;
import org.ar4k.agent.core.data.generator.dataType.BooleanSimulator;
import org.ar4k.agent.core.data.generator.dataType.DoubleSimulator;
import org.ar4k.agent.core.data.generator.dataType.FloatSimulator;
import org.ar4k.agent.core.data.generator.dataType.IntegerSimulator;
import org.ar4k.agent.core.data.generator.dataType.JsonNumericSimulator;
import org.ar4k.agent.core.data.generator.dataType.JsonStringSimulator;
import org.ar4k.agent.core.data.generator.dataType.LoggerSimulator;
import org.ar4k.agent.core.data.generator.dataType.LongSimulator;
import org.ar4k.agent.core.data.generator.dataType.StringSimulator;
import org.ar4k.agent.core.data.generator.simulator.DataBagSimulator;
import org.ar4k.agent.core.data.generator.simulator.EchoIncrementalSimulator;
import org.ar4k.agent.core.data.generator.simulator.IncrementalSimulator;
import org.ar4k.agent.core.data.generator.simulator.RandomSimulator;
import org.ar4k.agent.core.data.generator.simulator.SinusoidSimulator;
import org.ar4k.agent.core.data.generator.simulator.StaticSimulator;
import org.ar4k.agent.core.interfaces.EdgeChannel;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.json.JSONObject;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.SubscribableChannel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Generatore dati casuali e player per databag
 *
 */
public class DataGeneratorService implements EdgeComponent {

	public class FireSimulationTask extends TimerTask {

		private final EdgeChannel dataChannel;
		private final NextGenerator dataNodeSimulator;

		public FireSimulationTask(EdgeChannel dataChannel, NextGenerator dataNodeSimulator) {
			this.dataChannel = dataChannel;
			this.dataNodeSimulator = dataNodeSimulator;
		}

		@Override
		public boolean cancel() {
			logger.info("FireSimulationTask stopping");
			return super.cancel();
		}

		@Override
		public void run() {
			try {
				if (dataNodeSimulator != null) {
					final List<Message<?>> nextValue = dataNodeSimulator.getNextValue();
					if (nextValue != null) {
						for (final Message<?> message : nextValue) {
							if (message != null) {
								try {
									dataChannel.getChannel().send(message);
								} catch (final Exception aa) {
									logger.logException(aa);
								}
							}
						}
					}
				} else {
					logger.info("dataNodeSimulator null");
				}
			} catch (final Exception aa) {
				logger.logException("type: " + dataNodeSimulator.getClass().getName(), aa);
			}
		}

	}

	public class PoolDataTask extends TimerTask {

		private final PollableChannel dataChannel;
		private final NextGenerator dataNodeSimulator;
		private final long delay;

		public PoolDataTask(PollableChannel dataChannel, NextGenerator dataNodeSimulator, long delay) {
			this.dataChannel = dataChannel;
			this.dataNodeSimulator = dataNodeSimulator;
			this.delay = delay / 2;
		}

		@Override
		public void run() {
			try {
				final Message<?> receive = dataChannel.receive(delay);
				if (receive != null) {
					((ReceiverData) dataNodeSimulator).setValue(receive.getPayload());
				}
			} catch (final Exception aa) {
				logger.logException(aa);
			}
		}

	}

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(DataGeneratorService.class.toString());

	// iniettata vedi set/get
	private DataGeneratorConfig configuration = null;

	private DataAddress dataspace = null;
	private Homunculus homunculus = null;
	private ServiceStatus serviceStatus = ServiceStatus.INIT;

	private Map<EdgeChannel, NextGenerator> simulatedDatas = new HashMap<>();

	private Timer timerSimulation = new Timer();

	@Override
	public void close() throws IOException {
		kill();
	}

	@Override
	public DataGeneratorConfig getConfiguration() {
		return configuration;
	}

	@Override
	public DataAddress getDataAddress() {
		return dataspace;
	}

	@Override
	public JSONObject getDescriptionJson() {
		final Gson gson = new GsonBuilder().create();
		JSONObject o = new JSONObject(gson.toJsonTree(configuration).getAsString());
		o.put("status", serviceStatus.toString());
		return o;
	}

	@Override
	public Homunculus getHomunculus() {
		return homunculus;
	}

	@Override
	public String getServiceName() {
		return "data-generator";
	}

	@Override
	public void init() {
		for (final SingleDataGeneratorPointConfig single : configuration.datas) {
			addNodeSimulated(single);
		}
	}

	@Override
	public void kill() {
		logger.info("start killing timer DataGeneratorService");
		if (timerSimulation != null) {
			timerSimulation.cancel();
			timerSimulation.purge();
			timerSimulation = null;
		}
		serviceStatus = ServiceStatus.KILLED;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		this.configuration = ((DataGeneratorConfig) configuration);
	}

	@Override
	public void setDataAddress(DataAddress dataAddress) {
		dataspace = dataAddress;
	}

	@Override
	public void setHomunculus(Homunculus homunculus) {
		this.homunculus = homunculus;
	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		return serviceStatus;
	}

	private void addNodeSimulated(SingleDataGeneratorPointConfig single) {
		Class<? extends EdgeChannel> typeChannel = null;
		switch (single.typeChannel) {
		case DirectChannel:
			typeChannel = IDirectChannel.class;
			break;
		case ExecutorChannel:
			typeChannel = IExecutorChannel.class;
			break;
		case PriorityChannel:
			typeChannel = IPriorityChannel.class;
			break;
		case PublishSubscribe:
			typeChannel = IPublishSubscribeChannel.class;
			break;
		case QueueChannel:
			typeChannel = IQueueChannel.class;
			break;
		case RendezvousChannel:
			typeChannel = IRendezvousChannel.class;
			break;
		default:
			typeChannel = IPublishSubscribeChannel.class;
			break;
		}
		FormatGenerator formatSimulator = null;
		switch (single.typeData) {
		case DOUBLE:
			formatSimulator = new DoubleSimulator();
			break;
		case FLOAT:
			formatSimulator = new FloatSimulator();
			break;
		case INTEGER:
			formatSimulator = new IntegerSimulator();
			break;
		case BOOLEAN:
			formatSimulator = new BooleanSimulator();
			break;
		case JSON_NUMERIC:
			formatSimulator = new JsonNumericSimulator(single.nodeId, single.patternJson);
			break;
		case JSON_STRING:
			formatSimulator = new JsonStringSimulator(single.nodeId, single.patternJson);
			break;
		case LOGGER:
			formatSimulator = new LoggerSimulator();
			break;
		case LONG:
			formatSimulator = new LongSimulator();
			break;
		case STRING:
			formatSimulator = new StringSimulator();
			break;
		default:
			formatSimulator = new JsonStringSimulator(single.nodeId, single.patternJson);
			break;
		}
		NextGenerator dataNodeSimulator = null;
		switch (single.typeSimulator) {
		case DATA_BAG:
			dataNodeSimulator = new DataBagSimulator(single.dataBagFilePath, single.frequency, single.delta);
			break;
		case ECHO_INCREMENTAL:
			dataNodeSimulator = new EchoIncrementalSimulator(formatSimulator, single.rangeLower, single.rangeHi,
					single.delta);
			break;
		case INCREMENTAL:
			dataNodeSimulator = new IncrementalSimulator(formatSimulator, single.rangeLower, single.rangeHi,
					single.delta);
			break;
		case RANDOM:
			dataNodeSimulator = new RandomSimulator(formatSimulator, single.rangeLower, single.rangeHi, single.delta);
			break;
		case SINUSOID:
			dataNodeSimulator = new SinusoidSimulator(formatSimulator, single.rangeLower, single.rangeHi, single.delta,
					single.height);
			break;
		case STATIC:
			dataNodeSimulator = new StaticSimulator(formatSimulator, single.rangeLower, single.rangeHi, single.delta);
			break;
		default:
			dataNodeSimulator = new StaticSimulator(formatSimulator, single.rangeLower, single.rangeHi, single.delta);
			break;

		}
		final EdgeChannel dataChannel = dataspace.createOrGetDataChannel(single.nodeId, typeChannel, single.description,
				(String) null, (String) null, single.tags, this);
		dataChannel.setDomainId(single.domainId);
		dataChannel.setNameSpace(single.namespace);
		simulatedDatas.put(dataChannel, dataNodeSimulator);
		timerSimulation.schedule(new FireSimulationTask(dataChannel, dataNodeSimulator), single.frequency,
				single.frequency);
		if (dataNodeSimulator instanceof ReceiverData) {
			if (dataChannel instanceof SubscribableChannel) {
				((SubscribableChannel) dataChannel).subscribe(((ReceiverData) dataNodeSimulator).getCallBack());
			} else if (dataChannel instanceof PollableChannel) {
				timerSimulation.schedule(
						new PoolDataTask((PollableChannel) dataChannel, dataNodeSimulator, single.frequency),
						single.frequency, single.frequency);
			}
		}
	}

}
