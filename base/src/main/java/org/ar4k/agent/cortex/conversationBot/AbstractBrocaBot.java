package org.ar4k.agent.cortex.conversationBot;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.ar4k.agent.cortex.conversationBot.exceptions.BotBootException;
import org.ar4k.agent.cortex.conversationBot.exceptions.HomunculusBootException;
import org.ar4k.agent.cortex.conversationBot.exceptions.HomunculusException;
import org.ar4k.agent.cortex.conversationBot.exceptions.LockedHomunculusException;
import org.ar4k.agent.cortex.conversationBot.exceptions.TrainingIntentMatcherException;
import org.ar4k.agent.cortex.conversationBot.messages.BotMessage;
import org.ar4k.agent.cortex.conversationBot.messages.CortexMessage;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculus;
import org.ar4k.agent.cortex.conversationBot.nodes.AbstractHomunculusConfiguration;
import org.ar4k.agent.cortex.conversationBot.utils.AbstractHomunculusConfigurationJsonAdapter;
import org.ar4k.agent.cortex.conversationBot.utils.SymbolicLinksTreeHomunculusJsonAdapter;
import org.ar4k.agent.helper.SymbolicLinksTree;
import org.joda.time.Instant;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class AbstractBrocaBot<T extends AbstractBotConfiguration> implements BrocaBot<T> {

  public T configuration = null;
  private transient Map<String, Object> runtimeData = new HashMap<String, Object>();
  private transient Instant lastRun = new Instant();

  private transient AbstractHomunculusConfiguration bootNodeConfig = null;
  private transient AbstractHomunculus<? extends AbstractHomunculusConfiguration> masterNode = null;

  private transient HashMap<AbstractHomunculusConfiguration, AbstractHomunculus<? extends AbstractHomunculusConfiguration>> nodes = new HashMap<AbstractHomunculusConfiguration, AbstractHomunculus<? extends AbstractHomunculusConfiguration>>();
  private transient HashMap<AbstractHomunculusConfiguration, SymbolicLinksTree<? extends AbstractHomunculusConfiguration>> map = new HashMap<AbstractHomunculusConfiguration, SymbolicLinksTree<? extends AbstractHomunculusConfiguration>>();
  private transient SymbolicLinksTree<? extends AbstractHomunculusConfiguration> runtimeSymbolicLinksTree = null;
  private boolean active = false;
  private boolean debug = false;

  public AbstractBrocaBot(T configuration) throws BotBootException {
    super();
    this.configuration = configuration;
    this.setBootNodeConfig(newHomunculusConfiguration("boot-config-node"));
  }

  public AbstractBrocaBot(T configuration, Map<String, Object> bootstrapData) throws BotBootException {
    super();
    this.configuration = configuration;
    this.configuration.bootstrapData = runtimeData;
    this.setBootNodeConfig(newHomunculusConfiguration("boot-config-node"));
    if (runtimeSymbolicLinksTree == null) {
      runtimeSymbolicLinksTree = new SymbolicLinksTree<AbstractHomunculusConfiguration>(bootNodeConfig);
    }
  }

  public void lastRunTrigger() {
    lastRun = new Instant();
  }

  public void createRuntimeTreeFromConfig() throws BotBootException {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(SymbolicLinksTree.class,
            new SymbolicLinksTreeHomunculusJsonAdapter<SymbolicLinksTree<AbstractHomunculusConfiguration>>(this,
                (SymbolicLinksTree<AbstractHomunculusConfiguration>) runtimeSymbolicLinksTree))
        .registerTypeAdapter(AbstractHomunculusConfiguration.class,
            new AbstractHomunculusConfigurationJsonAdapter<AbstractHomunculusConfiguration>(
                (Class<AbstractHomunculusConfiguration>) homunculusConfigurationClass()))
        .setPrettyPrinting().create();
    masterNode = null;
    bootNodeConfig = null;
    runtimeSymbolicLinksTree = gson.fromJson(configuration.symbolicLinksTree, SymbolicLinksTree.class);
    bootNodeConfig = runtimeSymbolicLinksTree.getHead();
  }

  public String getRuntimeSymbolicLinksTreeAsJson() throws BotBootException {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(SymbolicLinksTree.class,
            new SymbolicLinksTreeHomunculusJsonAdapter<SymbolicLinksTree<AbstractHomunculusConfiguration>>(this,
                (SymbolicLinksTree<AbstractHomunculusConfiguration>) runtimeSymbolicLinksTree))
        .registerTypeAdapter(AbstractHomunculusConfiguration.class,
            new AbstractHomunculusConfigurationJsonAdapter<AbstractHomunculusConfiguration>(
                (Class<AbstractHomunculusConfiguration>) homunculusConfigurationClass()))
        .setPrettyPrinting().create();
    return gson.toJson(runtimeSymbolicLinksTree);
  }

  @Override
  public void handleMessage(Message<?> message) throws MessagingException {
    CortexMessage cortexMessage = new BotMessage();
    try {
      submit(cortexMessage);
    } catch (Exception e) {
      throw new MessagingException(e.getMessage());
    }
    lastRunTrigger();
  }

  @Override
  public String getBotName() {
    return configuration.botName;
  }

  @Override
  public String getBotDescription() {
    return configuration.botDescription;
  }

  @Override
  public T getConfig() {
    return configuration;
  }

  @Override
  public void setConfig(T configuration) {
    this.configuration = configuration;
  }

  @Override
  public Map<String, Object> getBotStorageMap() {
    return runtimeData;
  }

  @Override
  public Collection<String> getBotStorageKeys() {
    return runtimeData.keySet();
  }

  @Override
  public Object getBotStorageObject(String key) {
    return runtimeData.get(key);
  }

  @Override
  public Object addBotStorageObject(String key, Object data) {
    runtimeData.put(key, data);
    return getBotStorageObject(key);
  }

  @Override
  public void runtimeDataFromConfig() {
    runtimeData = configuration.bootstrapData;
  }

  @Override
  public void configDataFromRuntime() {
    configuration.bootstrapData = runtimeData;
  }

  @Override
  public Instant getLastRunTime() {
    return lastRun;
  }

  @Override
  public SymbolicLinksTree<? extends AbstractHomunculusConfiguration> getRuntimeSymbolicLinksTree() {
    return runtimeSymbolicLinksTree;
  }

  private void caricaMappa(SymbolicLinksTree<? extends AbstractHomunculusConfiguration> albero)
      throws InstantiationException, IllegalAccessException {
    for (SymbolicLinksTree<? extends AbstractHomunculusConfiguration> albi : albero.getSubTrees()) {
      caricaMappa(albi);
    }
    map.put(albero.getHead(), albero);
    nodes.put(albero.getHead(), albero.getHead().getHomunculus(this));
  }

  @Override
  public void bootstrapBot() throws BotBootException, HomunculusBootException, TrainingIntentMatcherException {
    // createRuntimeTreeFromConfig();
    try {
      // crea i nodi con una funzione annidata e carica map e nodes
      caricaMappa(runtimeSymbolicLinksTree);
      for (SymbolicLinksTree<? extends AbstractHomunculusConfiguration> alb : runtimeSymbolicLinksTree.getSubTrees()) {
        caricaMappa(alb);
      }
      masterNode = (AbstractHomunculus<? extends AbstractHomunculusConfiguration>) nodes
          .get(runtimeSymbolicLinksTree.getHead());
      if (debug == true) {
        System.out.println("masterNode " + runtimeSymbolicLinksTree.getHead());
      }
      // per ogni nodo assegna i parenti e i figli
      for (AbstractHomunculusConfiguration nodo : nodes.keySet()) {
        // TODO: Aggingiugere il caricamento del link simbolico realFather
        AbstractHomunculus<? extends AbstractHomunculusConfiguration> father = nodes.get(map.get(nodo).getHead());
        AbstractHomunculus<? extends AbstractHomunculusConfiguration> me = nodes.get(nodo);
        HashSet<AbstractHomunculus<? extends AbstractHomunculusConfiguration>> figli = new HashSet<AbstractHomunculus<? extends AbstractHomunculusConfiguration>>();
        for (SymbolicLinksTree<? extends AbstractHomunculusConfiguration> albFiglio : map.get(nodo).getSubTrees()) {
          figli.add(nodes.get(albFiglio.getHead()));
        }
        me.setParent(father);
        for (AbstractHomunculus<? extends AbstractHomunculusConfiguration> child : figli) {
          me.addChild(child);
          // System.out.println(me.getChildren());
        }
        // System.out.println("boot node " + nodo);
      }
      // infine chiama il boot su tutti i nodi
      active = true;
      try {
        for (AbstractHomunculusConfiguration nodo : nodes.keySet()) {
          // System.out.println("boot node " + nodo);
          AbstractHomunculus<? extends AbstractHomunculusConfiguration> targetNode = nodes.get(nodo);
          targetNode.bootstrap();
        }
      } catch (Exception e) {
        throw new HomunculusBootException("in bootstrap loop " + e.getMessage());
      }
    } catch (InstantiationException | IllegalAccessException | SecurityException | LockedHomunculusException a) {
      throw new HomunculusBootException(a.getMessage());
    }
  }

  @Override
  public Collection<String> checkConfig(T configuration) {
    return null;
  }

  @Override
  public boolean isOnline() {
    return masterNode != null;
  }

  @Override
  public boolean selfTest() {
    return isOnline();
  }

  @Override
  public boolean isActiveBot() {
    return active;
  }

  @Override
  public CortexMessage consoleGodQuery(CortexMessage adminMessage) throws HomunculusException {
    adminMessage.setGodMessage(true);
    return query(adminMessage);
  }

  @Override
  public String consoleGodQuery(String adminMessage) throws HomunculusException {
    CortexMessage m = new BotMessage();
    m.setMessage(adminMessage);
    CortexMessage r = consoleGodQuery(m);
    return r != null ? r.getMessage() : null;
  }

  @Override
  public String getConfigAsJson() {
    return configuration.toJson();
  }

  @Override
  public void setRuntimeSymbolicLinksTree(SymbolicLinksTree<? extends AbstractHomunculusConfiguration> tree) {
    runtimeSymbolicLinksTree = tree;
  }

  @Override
  public AbstractHomunculusConfiguration getBootNodeConfig() {
    return bootNodeConfig;
  }

  @Override
  public void setBootNodeConfig(AbstractHomunculusConfiguration bootNode) {
    this.bootNodeConfig = bootNode;
  }

  public AbstractHomunculus<? extends AbstractHomunculusConfiguration> getMasterNode() {
    return masterNode;
  }

  @Override
  public CortexMessage query(CortexMessage utterance) throws HomunculusException {
    // System.out.println("passo a parseMessage");
    return (CortexMessage) getMasterNode().parseMessage(utterance);
  }

  @Override
  public void submit(CortexMessage utterance) throws HomunculusException {
    query(utterance);
  }

}
