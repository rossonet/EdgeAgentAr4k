package org.ar4k.agent.core.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.springframework.messaging.MessageChannel;

public class DataAddress {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(DataAddress.class.toString());

  public DataAddress() {
    dataChannels.clear();
  }

  private Collection<AbstractChannel> dataChannels = new HashSet<>();

  public Collection<AbstractChannel> getDataChannels() {
    return dataChannels;
  }

  public AbstractChannel getChannel(String channelId) {
    AbstractChannel r = null;
    for (AbstractChannel c : getDataChannels()) {
      if (c.getNodeId().equals(channelId)) {
        r = c;
        break;
      }
    }
    return r;
  }

  public void addDataChannel(AbstractChannel dataChannel) {
    dataChannel.startDataChannel();
    this.dataChannels.add(dataChannel);
    logger.info(dataChannel.getNodeId() + " [" + dataChannel.getDescription() + "] started");
  }

  public void removeDataChannel(AbstractChannel dataChannel) {
    dataChannel.stopDataChannel();
    this.dataChannels.remove(dataChannel);
    logger.info(dataChannel.getNodeId() + " [" + dataChannel.getDescription() + "] removed");
  }

  public void removeDataChannel(String dataChannel) {
    removeDataChannel(getChannel(dataChannel));
  }

  public void clearDataChannels() {
    for (AbstractChannel target : this.dataChannels) {
      removeDataChannel(target);
    }
  }

  public Collection<String> listChannels() {
    Collection<String> result = new ArrayList<>();
    for (AbstractChannel c : getDataChannels()) {
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

}
