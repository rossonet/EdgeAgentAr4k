package org.ar4k.agent.hazelcast;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Ar4kComponent;
import org.ar4k.agent.core.data.Ar4kChannel;
import org.ar4k.agent.core.data.channels.INoDataChannel;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
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

public class HazelcastComponent implements Ar4kComponent {

  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(Anima.class.toString());

  private Anima anima = null;

  private HazelcastConfig configuration = null;

  private HazelcastInstance hazelcastInstance = null;

  private String beanName = null;

  private Set<ExternalMessageHandler> subscriberTopicsFromExternal = new HashSet<>();
  private Set<InternalMessageHandler> subscriberTopicsFromInternal = new HashSet<>();

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
  public ConfigSeed getConfiguration() {
    return configuration;
  }

  @Override
  public void setConfiguration(ConfigSeed configuration) {
    this.configuration = (HazelcastConfig) configuration;
  }

  @Override
  public JSONObject getStatusJson() {
    return new JSONObject(gson.toJsonTree(configuration).getAsString());
  }

  @Override
  public void setBeanName(String name) {
    beanName = name;
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
    popolateDataTopics();
  }

  private void popolateDataTopics() {
    Ar4kChannel channelRoot = anima.getDataAddress().createOrGetDataChannel(configuration.fatherOfChannels,
        INoDataChannel.class, "Hazelcast root component", (String) null, null);
    for (String singleTopicLabel : configuration.getTopics()) {
      ITopic<Object> sigleTopic = getTopic(singleTopicLabel);
      IPublishSubscribeChannel singleAr4kchannelRead = (IPublishSubscribeChannel) anima.getDataAddress()
          .createOrGetDataChannel(singleTopicLabel, IPublishSubscribeChannel.class, singleTopicLabel + " read channel",
              channelRoot, configuration.scopeOfChannels != null ? configuration.scopeOfChannels
                  : anima.getDataAddress().getDefaultScope());
      IPublishSubscribeChannel singleAr4kchannelWrite = (IPublishSubscribeChannel) anima.getDataAddress()
          .createOrGetDataChannel(configuration.getWriteTopic(singleTopicLabel), IPublishSubscribeChannel.class,
              singleTopicLabel + " write channel", channelRoot,
              configuration.scopeOfChannels != null ? configuration.scopeOfChannels
                  : anima.getDataAddress().getDefaultScope());
      singleAr4kchannelRead.addTag("hazelcast-read");
      singleAr4kchannelRead.addTag(singleTopicLabel);
      singleAr4kchannelWrite.addTag("hazelcast-write");
      singleAr4kchannelWrite.addTag(singleTopicLabel);
      ExternalMessageHandler externalHandlerSubscriber = new ExternalMessageHandler(sigleTopic, singleAr4kchannelRead);
      InternalMessageHandler internalHandlerSubscribe = new InternalMessageHandler(singleAr4kchannelWrite, sigleTopic);
      singleAr4kchannelWrite.subscribe(internalHandlerSubscribe);
      sigleTopic.addMessageListener(externalHandlerSubscriber);
      subscriberTopicsFromExternal.add(externalHandlerSubscriber);
      subscriberTopicsFromInternal.add(internalHandlerSubscribe);
    }
  }

  private void registerBean() {
    ((ConfigurableApplicationContext) Anima.getApplicationContext()).getBeanFactory().registerSingleton(beanName, this);
  }

  @Override
  public void kill() {
    deregisterBean();
    if (hazelcastInstance != null) {
      hazelcastInstance.shutdown();
      hazelcastInstance = null;
    }
    for (ExternalMessageHandler subscriber : subscriberTopicsFromExternal) {
      subscriber.getSource().destroy();
      try {
        subscriber.getTarget().close();
      } catch (IOException e) {
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
  public String getStatusString() {
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
    Config config = new Config();
    config.setInstanceName(
        configuration.getUniqueId() != null ? configuration.getUniqueId() : anima.getAgentUniqueName());
    JoinConfig joinConfig = config.getNetworkConfig().getJoin();
    if (configuration.isMultiCast()) {
      joinConfig.getMulticastConfig().setEnabled(false);
    }
    if (configuration.getMembers() != null && !configuration.getMembers().isEmpty()) {
      joinConfig.getTcpIpConfig().setMembers(configuration.getMembers());
      joinConfig.getTcpIpConfig().setEnabled(true);
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

}
