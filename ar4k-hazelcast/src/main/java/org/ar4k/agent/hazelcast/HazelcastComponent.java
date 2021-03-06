package org.ar4k.agent.hazelcast;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.springframework.context.ConfigurableApplicationContext;

import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ITopic;
import com.hazelcast.cp.lock.FencedLock;

public class HazelcastComponent implements EdgeComponent {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(HazelcastComponent.class);

	private Homunculus homunculus = null;

	private HazelcastConfig configuration = null;

	private HazelcastInstance hazelcastInstance = null;

	private String beanName = null;

	private final Set<ExternalMessageHandler> subscriberTopicsFromExternal = new HashSet<>();
	private final Set<InternalMessageHandler> subscriberTopicsFromInternal = new HashSet<>();

	// TODO Permettere la creazione di canali brodcast tra i nodi coinvolti nel
	// cluster
	private DataAddress dataspace = null;

	public HazelcastComponent(Homunculus homunculus, HazelcastConfig tribeConfig) {
		this.configuration = tribeConfig;
		this.homunculus = homunculus;
		beanName = this.configuration.getBeanName();
	}

	public HazelcastComponent(HazelcastConfig tribeConfig) {
		homunculus = tribeConfig.homunculus;
		this.configuration = tribeConfig;
		beanName = this.configuration.getBeanName();
	}

	@Override
	public ServiceConfig getConfiguration() {
		return configuration;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		this.configuration = (HazelcastConfig) configuration;
	}

	public String getBeanName() {
		return beanName;
	}

	@Override
	public void close() throws IOException {
		kill();
	}

	@Override
	public void init() {
		createOrGetHazelcastInstance();
		registerBean();
	}

	private void registerBean() {
		((ConfigurableApplicationContext) Homunculus.getApplicationContext()).getBeanFactory()
				.registerSingleton(beanName, this);
	}

	@Override
	public void kill() {
		deregisterBean();
		if (hazelcastInstance != null) {
			hazelcastInstance.shutdown();
			hazelcastInstance = null;
		}
		for (final ExternalMessageHandler subscriber : subscriberTopicsFromExternal) {
			subscriber.getSource().destroy();
			try {
				subscriber.getTarget().close();
			} catch (final IOException e) {
				logger.logException(e);
			}
		}
		subscriberTopicsFromExternal.clear();
		subscriberTopicsFromInternal.clear();
	}

	private void deregisterBean() {
		((ConfigurableApplicationContext) Homunculus.getApplicationContext()).getBeanFactory().destroyBean(this);
	}

	public HazelcastInstance createOrGetHazelcastInstance() {
		if (hazelcastInstance == null) {
			hazelcastInstance = Hazelcast.newHazelcastInstance(generateHazelcastConfig());
		}
		return hazelcastInstance;
	}

	private Config generateHazelcastConfig() {
		final Config config = new Config();
		config.setInstanceName(
				configuration.getUniqueId() != null ? configuration.getUniqueId() : homunculus.getAgentUniqueName());
		final JoinConfig joinConfig = config.getNetworkConfig().getJoin();
		if (configuration.isMultiCastEnabled()) {
			joinConfig.getMulticastConfig().setEnabled(true);
		} else {
			joinConfig.getMulticastConfig().setEnabled(false);
		}
		if (configuration.getMembers() != null && !configuration.getMembers().isEmpty()) {
			joinConfig.getTcpIpConfig().setMembers(configuration.getMembers());
			joinConfig.getTcpIpConfig().setEnabled(true);
		} else {
			joinConfig.getTcpIpConfig().setEnabled(false);
		}
		if (configuration.getGroupName() != null && !configuration.getGroupName().isEmpty()) {
			config.getGroupConfig().setName(configuration.getGroupName());
			if (configuration.getGroupPassword() != null && !configuration.getGroupPassword().isEmpty()) {
				config.setGroupConfig(new GroupConfig(configuration.getGroupName(), configuration.getGroupPassword()));
			} else {
				config.setGroupConfig(new GroupConfig(configuration.getGroupName()));
			}
		}
		if (configuration.isKubernetesEnabled()) {
			joinConfig.getKubernetesConfig().setEnabled(true);
		} else {
			joinConfig.getKubernetesConfig().setEnabled(false);
		}
		if (configuration.getKubernetesNameSpace() != null && !configuration.getKubernetesNameSpace().isEmpty()) {
			logger.info("running on namespace " + configuration.getKubernetesNameSpace());
			joinConfig.getKubernetesConfig().setProperty("namespace", configuration.getKubernetesNameSpace());
		}
		return config;
	}

	IMap<Object, Object> getMap(String mapName) {
		return createOrGetHazelcastInstance().getMap(mapName);
	}

	IList<Object> getList(String listName) {
		return createOrGetHazelcastInstance().getList(listName);
	}

	ITopic<Object> getTopic(String topicName) {
		return createOrGetHazelcastInstance().getTopic(topicName);
	}

	IQueue<Object> getQueue(String queueName) {
		return createOrGetHazelcastInstance().getQueue(queueName);
	}

	FencedLock getLock(String lockName) {
		return createOrGetHazelcastInstance().getCPSubsystem().getLock(lockName);
	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		return ServiceStatus.RUNNING;
	}

	@Override
	public Homunculus getHomunculus() {
		return homunculus;
	}

	@Override
	public DataAddress getDataAddress() {
		return dataspace;
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
	public String getServiceName() {
		return getConfiguration().getName();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HazelcastComponent [");
		if (homunculus != null) {
			builder.append("homunculus=");
			builder.append(homunculus);
			builder.append(", ");
		}
		if (configuration != null) {
			builder.append("configuration=");
			builder.append(configuration);
			builder.append(", ");
		}
		if (hazelcastInstance != null) {
			builder.append("hazelcastInstance=");
			builder.append(hazelcastInstance);
			builder.append(", ");
		}
		if (beanName != null) {
			builder.append("beanName=");
			builder.append(beanName);
			builder.append(", ");
		}
		if (subscriberTopicsFromExternal != null) {
			builder.append("subscriberTopicsFromExternal=");
			builder.append(subscriberTopicsFromExternal);
			builder.append(", ");
		}
		if (subscriberTopicsFromInternal != null) {
			builder.append("subscriberTopicsFromInternal=");
			builder.append(subscriberTopicsFromInternal);
			builder.append(", ");
		}
		if (dataspace != null) {
			builder.append("dataspace=");
			builder.append(dataspace);
		}
		builder.append("]");
		return builder.toString();
	}

}
