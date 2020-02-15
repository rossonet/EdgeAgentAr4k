package org.ar4k.agent.core.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.data.channels.INoDataChannel;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.springframework.messaging.MessageChannel;

public class DataAddress implements AutoCloseable {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(DataAddress.class.toString());

  protected final Anima anima;

  public DataAddress(Anima anima, String dataNamePrefix) {
    dataChannels.clear();
    this.anima = anima;
    this.dataNamePrefix = dataNamePrefix;
  }

  protected final Collection<Ar4kChannel> dataChannels = new HashSet<>();

  protected String defaultScope = "ar4k-ai";
  protected String levelSeparator = "/";
  protected final String dataNamePrefix;

  protected transient Set<DataAddressChange> callbacks = new HashSet<>();

  public Collection<Ar4kChannel> getDataChannels() {
    return dataChannels;
  }

  public Ar4kChannel getChannel(String channelId) {
    Ar4kChannel r = null;
    for (Ar4kChannel c : getDataChannels()) {
      if (c.getNodeId() != null && c.getNodeId().equals(channelId)) {
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

  public Ar4kChannel createOrGetDataChannel(String nodeId, Class<? extends Ar4kChannel> channelType, String description,
      String father, String scope) {
    return createOrGetDataChannel(nodeId, channelType, description, getChannel(father), scope);
  }

  public Ar4kChannel createOrGetDataChannel(String nodeId, Class<? extends Ar4kChannel> channelType, String description,
      Ar4kChannel father, String scope) {
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
        logger.info("create channel " + nodeId + " of type " + channelType);
        returnChannel = (Ar4kChannel) ctor.newInstance();
        returnChannel.setNodeId(nodeId);
        returnChannel.setDescription(description);
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

  public void removeDataChannel(Ar4kChannel dataChannel, boolean clearList) {
    try {
      logger.info("Try to remove dataChannel " + dataChannel.getNodeId());
      dataChannel.close();
    } catch (Exception e) {
      logger.logException(e);
    }
    if (clearList) {
      this.dataChannels.remove(dataChannel);
    }
    for (DataAddressChange target : callbacks) {
      target.onDataAddressDelete(dataChannel.toString());
    }
    logger.info(dataChannel.getNodeId() + " [" + dataChannel.getDescription() + "] removed");
  }

  public void removeDataChannel(String dataChannel, boolean clearList) {
    removeDataChannel(getChannel(dataChannel), clearList);
  }

  public void clearDataChannels() {
    for (Ar4kChannel target : this.dataChannels) {
      removeDataChannel(target, false);
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

  @Override
  public void close() throws Exception {
    clearDataChannels();
    callbacks.clear();
  }

  public String getDataNamePrefix() {
    return dataNamePrefix;
  }

}
