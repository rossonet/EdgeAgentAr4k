package org.ar4k.agent.hazelcast;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.EdgeComponent;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.json.JSONObject;
import org.springframework.context.ConfigurableApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(Anima.class.toString());

	private Anima anima = null;

	private HazelcastConfig configuration = null;

	private HazelcastInstance hazelcastInstance = null;

	private String beanName = null;

	private final Set<ExternalMessageHandler> subscriberTopicsFromExternal = new HashSet<>();
	private final Set<InternalMessageHandler> subscriberTopicsFromInternal = new HashSet<>();

	// TODO DataAddress
	private DataAddress dataspace = null;

	public HazelcastComponent(Anima anima, HazelcastConfig tribeConfig) {
		this.configuration = tribeConfig;
		this.anima = anima;
		beanName = this.configuration.getBeanName();
	}

	public HazelcastComponent(HazelcastConfig tribeConfig) {
		anima = tribeConfig.anima;
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
		((ConfigurableApplicationContext) Anima.getApplicationContext()).getBeanFactory().registerSingleton(beanName,
				this);
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
		((ConfigurableApplicationContext) Anima.getApplicationContext()).getBeanFactory().destroyBean(this);
	}

	@Override
	public String toString() {
		return hazelcastInstance != null ? hazelcastInstance.getCluster().getClusterState().toString()
				: "hazelcastInstance null";
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
				configuration.getUniqueId() != null ? configuration.getUniqueId() : anima.getAgentUniqueName());
		final JoinConfig joinConfig = config.getNetworkConfig().getJoin();
		if (configuration.isMultiCast()) {
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
		if (configuration.getGroup() != null && !configuration.getGroup().isEmpty()) {
			config.getGroupConfig().setName(configuration.getGroup());
			if (configuration.getGroupPassword() != null && !configuration.getGroupPassword().isEmpty()) {
				config.setGroupConfig(new GroupConfig(configuration.getGroup(), configuration.getGroupPassword()));
			} else {
				config.setGroupConfig(new GroupConfig(configuration.getGroup()));
			}
		}
		if (configuration.isKubernetes()) {
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
	public Anima getAnima() {
		return anima;
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
	public void setAnima(Anima anima) {
		this.anima = anima;
	}

	@Override
	public JSONObject getDescriptionJson() {
		return new JSONObject(gson.toJsonTree(configuration).getAsString());
	}

}
