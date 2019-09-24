package org.ar4k.agent.core.data.channels;

import org.ar4k.agent.core.data.Channel;
import org.springframework.integration.channel.PriorityChannel;
import org.springframework.integration.support.management.PollableChannelManagement;
import org.springframework.integration.support.management.Statistics;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;

public class IPriorityChannel extends Channel implements PollableChannel, PollableChannelManagement {

  public IPriorityChannel() {
    PriorityChannel c = new PriorityChannel();
    super.setChannel(c);
    super.setChannelType(Type.PriorityChannel);
  }

  @Override
  public boolean send(Message<?> message, long timeout) {
    return getChannel().send(message, timeout);
  }

  @Override
  public Message<?> receive() {
    return ((PriorityChannel) getChannel()).receive();
  }

  @Override
  public Message<?> receive(long timeout) {
    return ((PriorityChannel) getChannel()).receive(timeout);
  }

  @Override
  public int getSendCount() {
    return ((PriorityChannel) getChannel()).getSendCount();
  }

  @Override
  public long getSendCountLong() {
    return ((PriorityChannel) getChannel()).getSendCountLong();
  }

  @Override
  public int getSendErrorCount() {
    return ((PriorityChannel) getChannel()).getSendErrorCount();
  }

  @Override
  public long getSendErrorCountLong() {
    return ((PriorityChannel) getChannel()).getSendErrorCountLong();
  }

  @Override
  public double getTimeSinceLastSend() {
    return ((PriorityChannel) getChannel()).getTimeSinceLastSend();
  }

  @Override
  public double getMeanSendRate() {
    return ((PriorityChannel) getChannel()).getMeanSendRate();
  }

  @Override
  public double getMeanErrorRate() {
    return ((PriorityChannel) getChannel()).getMeanErrorRate();
  }

  @Override
  public double getMeanErrorRatio() {
    return ((PriorityChannel) getChannel()).getMeanErrorRatio();
  }

  @Override
  public double getMeanSendDuration() {
    return ((PriorityChannel) getChannel()).getMeanSendDuration();
  }

  @Override
  public double getMinSendDuration() {
    return ((PriorityChannel) getChannel()).getMinSendDuration();
  }

  @Override
  public double getMaxSendDuration() {
    return ((PriorityChannel) getChannel()).getMaxSendDuration();
  }

  @Override
  public double getStandardDeviationSendDuration() {
    return ((PriorityChannel) getChannel()).getStandardDeviationSendDuration();
  }

  @Override
  public Statistics getSendDuration() {
    return ((PriorityChannel) getChannel()).getSendDuration();
  }

  @Override
  public Statistics getSendRate() {
    return ((PriorityChannel) getChannel()).getSendRate();
  }

  @Override
  public Statistics getErrorRate() {
    return ((PriorityChannel) getChannel()).getErrorRate();
  }

  @Override
  public void setStatsEnabled(boolean statsEnabled) {
    ((PriorityChannel) getChannel()).setStatsEnabled(statsEnabled);
  }

  @Override
  public boolean isStatsEnabled() {
    return ((PriorityChannel) getChannel()).isStatsEnabled();
  }

  @Override
  public void setLoggingEnabled(boolean enabled) {
    ((PriorityChannel) getChannel()).setLoggingEnabled(enabled);
  }

  @Override
  public boolean isLoggingEnabled() {
    return ((PriorityChannel) getChannel()).isLoggingEnabled();
  }

  @Override
  public void reset() {
    ((PriorityChannel) getChannel()).reset();
  }

  @Override
  public void setCountsEnabled(boolean countsEnabled) {
    ((PriorityChannel) getChannel()).setCountsEnabled(countsEnabled);
  }

  @Override
  public boolean isCountsEnabled() {
    return ((PriorityChannel) getChannel()).isCountsEnabled();
  }

  @Override
  public ManagementOverrides getOverrides() {
    return ((PriorityChannel) getChannel()).getOverrides();
  }

  @Override
  public void destroy() throws Exception {
    ((PriorityChannel) getChannel()).destroy();
  }

  @Override
  public int getReceiveCount() {
    return ((PriorityChannel) getChannel()).getReceiveCount();
  }

  @Override
  public long getReceiveCountLong() {
    return ((PriorityChannel) getChannel()).getReceiveCountLong();
  }

  @Override
  public int getReceiveErrorCount() {
    return ((PriorityChannel) getChannel()).getReceiveErrorCount();
  }

  @Override
  public long getReceiveErrorCountLong() {
    return ((PriorityChannel) getChannel()).getReceiveErrorCountLong();
  }

  @Override
  public boolean subscribe(MessageHandler handler) {
    return false;
  }

  @Override
  public boolean unsubscribe(MessageHandler handler) {
    return false;
  }

}
