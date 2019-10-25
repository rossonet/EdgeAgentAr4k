package org.ar4k.agent.core.data.channels;

import org.ar4k.agent.core.data.Ar4kChannel;
import org.ar4k.agent.core.data.Channel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.management.PollableChannelManagement;
import org.springframework.integration.support.management.Statistics;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;

public class IQueueChannel extends Channel implements PollableChannel, PollableChannelManagement {

  public IQueueChannel() {
    QueueChannel c = new QueueChannel(50);
    super.setChannel(c);
    super.setChannelType(Type.QueueChannel);
  }

  @Override
  public boolean send(Message<?> message, long timeout) {
    return getChannel().send(message, timeout);
  }

  @Override
  public Message<?> receive() {
    return ((QueueChannel) getChannel()).receive();
  }

  @Override
  public Message<?> receive(long timeout) {
    return ((QueueChannel) getChannel()).receive(timeout);
  }

  @Override
  public int getSendCount() {
    return ((QueueChannel) getChannel()).getSendCount();
  }

  @Override
  public long getSendCountLong() {
    return ((QueueChannel) getChannel()).getSendCountLong();
  }

  @Override
  public int getSendErrorCount() {
    return ((QueueChannel) getChannel()).getSendErrorCount();
  }

  @Override
  public long getSendErrorCountLong() {
    return ((QueueChannel) getChannel()).getSendErrorCountLong();
  }

  @Override
  public double getTimeSinceLastSend() {
    return ((QueueChannel) getChannel()).getTimeSinceLastSend();
  }

  @Override
  public double getMeanSendRate() {
    return ((QueueChannel) getChannel()).getMeanSendRate();
  }

  @Override
  public double getMeanErrorRate() {
    return ((QueueChannel) getChannel()).getMeanErrorRate();
  }

  @Override
  public double getMeanErrorRatio() {
    return ((QueueChannel) getChannel()).getMeanErrorRatio();
  }

  @Override
  public double getMeanSendDuration() {
    return ((QueueChannel) getChannel()).getMeanSendDuration();
  }

  @Override
  public double getMinSendDuration() {
    return ((QueueChannel) getChannel()).getMinSendDuration();
  }

  @Override
  public double getMaxSendDuration() {
    return ((QueueChannel) getChannel()).getMaxSendDuration();
  }

  @Override
  public double getStandardDeviationSendDuration() {
    return ((QueueChannel) getChannel()).getStandardDeviationSendDuration();
  }

  @Override
  public Statistics getSendDuration() {
    return ((QueueChannel) getChannel()).getSendDuration();
  }

  @Override
  public Statistics getSendRate() {
    return ((QueueChannel) getChannel()).getSendRate();
  }

  @Override
  public Statistics getErrorRate() {
    return ((QueueChannel) getChannel()).getErrorRate();
  }

  @Override
  public void setStatsEnabled(boolean statsEnabled) {
    ((QueueChannel) getChannel()).setStatsEnabled(statsEnabled);
  }

  @Override
  public boolean isStatsEnabled() {
    return ((QueueChannel) getChannel()).isStatsEnabled();
  }

  @Override
  public void setLoggingEnabled(boolean enabled) {
    ((QueueChannel) getChannel()).setLoggingEnabled(enabled);
  }

  @Override
  public boolean isLoggingEnabled() {
    return ((QueueChannel) getChannel()).isLoggingEnabled();
  }

  @Override
  public void reset() {
    ((QueueChannel) getChannel()).reset();
  }

  @Override
  public void setCountsEnabled(boolean countsEnabled) {
    ((QueueChannel) getChannel()).setCountsEnabled(countsEnabled);
  }

  @Override
  public boolean isCountsEnabled() {
    return ((QueueChannel) getChannel()).isCountsEnabled();
  }

  @Override
  public ManagementOverrides getOverrides() {
    return ((QueueChannel) getChannel()).getOverrides();
  }

  @Override
  public void destroy() throws Exception {
    ((QueueChannel) getChannel()).destroy();
  }

  @Override
  public int getReceiveCount() {
    return ((QueueChannel) getChannel()).getReceiveCount();
  }

  @Override
  public long getReceiveCountLong() {
    return ((QueueChannel) getChannel()).getReceiveCountLong();
  }

  @Override
  public int getReceiveErrorCount() {
    return ((QueueChannel) getChannel()).getReceiveErrorCount();
  }

  @Override
  public long getReceiveErrorCountLong() {
    return ((QueueChannel) getChannel()).getReceiveErrorCountLong();
  }

  @Override
  public boolean subscribe(MessageHandler handler) {
    return false;
  }

  @Override
  public boolean unsubscribe(MessageHandler handler) {
    return false;
  }

  @Override
  public Class<? extends Ar4kChannel> getChannelClass() {
    return this.getClass();
  }

}
