package org.ar4k.agent.core.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.data.channels.INoDataChannel;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.channels.IQueueChannel;
import org.ar4k.agent.helper.HardwareHelper;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.spring.HealthMessage;
import org.springframework.messaging.MessageChannel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DataAddress implements AutoCloseable {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(DataAddress.class.toString());

  public DataAddress() {
    dataChannels.clear();
  }

  private Collection<Ar4kChannel> dataChannels = new HashSet<>();

  private String defaultScope = "ar4k-ai";
  private String levelSeparator = "/";

  private long delay = 35000L;
  private long period = 15000L;

  private transient Set<DataAddressChange> callbacks = new HashSet<>();
  private transient Timer timer = new Timer("TimerHealth");

  public Collection<Ar4kChannel> getDataChannels() {
    return dataChannels;
  }

  public Ar4kChannel getChannel(String channelId) {
    Ar4kChannel r = null;
    for (Ar4kChannel c : getDataChannels()) {
      if (c.getNodeId().equals(channelId)) {
        r = c;
        break;
      }
    }
    return r;
  }

  public void callAddressSpaceRefresh(Ar4kChannel nodeUpdated) {
    for (DataAddressChange target : callbacks) {
      target.onDataAddressUpdate(nodeUpdated);
    }
  }

  public void addCallbackOnChange(DataAddressChange callback) {
    callbacks.add(callback);
  }

  public void removeCallbackOnChange(DataAddressChange callback) {
    callbacks.remove(callback);
  }

  public Ar4kChannel createOrGetDataChannel(String nodeId, Class<? extends Ar4kChannel> channelType, String father,
      String scope) {
    return createOrGetDataChannel(nodeId, channelType, getChannel(father), scope);
  }

  public Ar4kChannel createOrGetDataChannel(String nodeId, Class<? extends Ar4kChannel> channelType, Ar4kChannel father,
      String scope) {
    Ar4kChannel returnChannel = null;
    if (getChannel(nodeId) != null) {
      returnChannel = getChannel(nodeId);
      this.dataChannels.add(returnChannel);
      returnChannel.setDataAddress(this);
      if (channelType != null && !channelType.equals(getChannel(nodeId).getClass())) {
        logger.warn("GET A DATA CHANNEL OF DIFFERENT TYPE: " + channelType.getName() + " -> "
            + getChannel(nodeId).getClass().getName());
      }
    } else {
      try {
        if (channelType == null) {
          channelType = INoDataChannel.class;
        }
        Constructor<?> ctor = channelType.getConstructor();
        returnChannel = (Ar4kChannel) ctor.newInstance();
        this.dataChannels.add(returnChannel);
        returnChannel.setDataAddress(this);
        if (father != null) {
          returnChannel.setFatherOfScope(scope, father);
        }
        logger.info(returnChannel.getNodeId() + " [" + returnChannel.getDescription() + "] started");
        for (DataAddressChange target : callbacks) {
          target.onDataAddressCreate(returnChannel);
        }
      } catch (Exception a) {
        logger.logException(a);
      }
    }
    return returnChannel;
  }

  public void removeDataChannel(Ar4kChannel dataChannel) {
    try {
      dataChannel.close();
    } catch (Exception e) {
      logger.logException(e);
    }
    this.dataChannels.remove(dataChannel);
    for (DataAddressChange target : callbacks) {
      target.onDataAddressDelete(dataChannel.toString());
    }
    logger.info(dataChannel.getNodeId() + " [" + dataChannel.getDescription() + "] removed");
  }

  public void removeDataChannel(String dataChannel) {
    removeDataChannel(getChannel(dataChannel));
  }

  public void clearDataChannels() {
    for (Ar4kChannel target : this.dataChannels) {
      removeDataChannel(target);
    }
    this.dataChannels.clear();
  }

  public Collection<String> listChannels() {
    Collection<String> result = new ArrayList<>();
    for (Ar4kChannel c : getDataChannels()) {
      result.add(c.getNodeId() + " [" + c.getDescription() + "] "
          + (c.getChannel() != null ? c.getChannel().getBeanName() : c.getStatus())
          + (c.getChannel() != null ? " -> " + c.getChannel().getFullChannelName() : ""));
    }
    return result;
  }

  public Collection<String> listSpringIntegrationChannels() {
    Collection<String> result = new ArrayList<>();
    for (Entry<String, MessageChannel> s : Anima.getApplicationContext().getBeansOfType(MessageChannel.class)
        .entrySet()) {
      result.add(s.getKey() + " -> " + s.getValue());
    }
    return result;
  }

  public String getDefaultScope() {
    return defaultScope;
  }

  public void setDefaultScope(String defaultScope) {
    this.defaultScope = defaultScope;
  }

  public String getLevelSeparator() {
    return levelSeparator;
  }

  public void setLevelSeparator(String levelSeparator) {
    this.levelSeparator = levelSeparator;
  }

  // task per health
  private TimerTask repeatedTask = new TimerTask() {

    private transient Anima anima = null;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void run() {
      try {
        sendEvent(HardwareHelper.getSystemInfo().getHealthIndicator());
      } catch (Exception e) {
        logger.logException(e);
      }
    }

    private void sendEvent(Map<String, Object> healthMessage) {
      if (anima == null && Anima.getApplicationContext() != null
          && Anima.getApplicationContext().getBean(Anima.class) != null
          && Anima.getApplicationContext().getBean(Anima.class).getDataAddress() != null) {
        anima = Anima.getApplicationContext().getBean(Anima.class);
      }
      HealthMessage<String> messageObject = new HealthMessage<>();
      messageObject.setPayload(gson.toJson(healthMessage));
      if (anima != null && anima.getDataAddress() != null && anima.getDataAddress().getChannel("health") != null)
        ((IPublishSubscribeChannel) anima.getDataAddress().getChannel("health")).send(messageObject);
    }
  };
  // task per health

  public void firstStart() {
    Ar4kChannel systemChannel = createOrGetDataChannel("system", INoDataChannel.class, (String) null, null);
    systemChannel.setDescription("local JVM system");
    Ar4kChannel loggerChannel = createOrGetDataChannel("logger", IPublishSubscribeChannel.class, systemChannel,
        getDefaultScope());
    loggerChannel.setDescription("logger queue");
    Ar4kChannel healthChannel = createOrGetDataChannel("health", IPublishSubscribeChannel.class, systemChannel,
        getDefaultScope());
    healthChannel.setDescription("local machine hardware and software stats");
    Ar4kChannel cmdChannel = createOrGetDataChannel("command", IQueueChannel.class, systemChannel, getDefaultScope());
    cmdChannel.setDescription("RPC interface");
    // start health regular messages
    timer.scheduleAtFixedRate(repeatedTask, delay, period);
  }

  @Override
  public void close() throws Exception {
    clearDataChannels();
    callbacks.clear();
  }

}
