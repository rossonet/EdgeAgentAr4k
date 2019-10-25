package org.ar4k.agent.core.data.channels;

import org.ar4k.agent.core.data.Ar4kChannel;
import org.ar4k.agent.core.data.Channel;
import org.springframework.integration.channel.RendezvousChannel;
import org.springframework.integration.support.management.PollableChannelManagement;
import org.springframework.integration.support.management.Statistics;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;

public class IRendezvousChannel extends Channel implements PollableChannel, PollableChannelManagement {

  public IRendezvousChannel() {
    RendezvousChannel c = new RendezvousChannel();
    super.setChannel(c);
    super.setChannelType(Type.RendezvousChannel);
  }

  @Override
  public boolean send(Message<?> message, long timeout) {
    return getChannel().send(message, timeout);
  }

  @Override
  public Message<?> receive() {
    return ((RendezvousChannel) getChannel()).receive();
  }

  @Override
  public Message<?> receive(long timeout) {
    return ((RendezvousChannel) getChannel()).receive(timeout);
  }

  @Override
  public int getSendCount() {
    return ((RendezvousChannel) getChannel()).getSendCount();
  }

  @Override
  public long getSendCountLong() {
    return ((RendezvousChannel) getChannel()).getSendCountLong();
  }

  @Override
  public int getSendErrorCount() {
    return ((RendezvousChannel) getChannel()).getSendErrorCount();
  }

  @Override
  public long getSendErrorCountLong() {
    return ((RendezvousChannel) getChannel()).getSendErrorCountLong();
  }

  @Override
  public double getTimeSinceLastSend() {
    return ((RendezvousChannel) getChannel()).getTimeSinceLastSend();
  }

  @Override
  public double getMeanSendRate() {
    return ((RendezvousChannel) getChannel()).getMeanSendRate();
  }

  @Override
  public double getMeanErrorRate() {
    return ((RendezvousChannel) getChannel()).getMeanErrorRate();
  }

  @Override
  public double getMeanErrorRatio() {
    return ((RendezvousChannel) getChannel()).getMeanErrorRatio();
  }

  @Override
  public double getMeanSendDuration() {
    return ((RendezvousChannel) getChannel()).getMeanSendDuration();
  }

  @Override
  public double getMinSendDuration() {
    return ((RendezvousChannel) getChannel()).getMinSendDuration();
  }

  @Override
  public double getMaxSendDuration() {
    return ((RendezvousChannel) getChannel()).getMaxSendDuration();
  }

  @Override
  public double getStandardDeviationSendDuration() {
    return ((RendezvousChannel) getChannel()).getStandardDeviationSendDuration();
  }

  @Override
  public Statistics getSendDuration() {
    return ((RendezvousChannel) getChannel()).getSendDuration();
  }

  @Override
  public Statistics getSendRate() {
    return ((RendezvousChannel) getChannel()).getSendRate();
  }

  @Override
  public Statistics getErrorRate() {
    return ((RendezvousChannel) getChannel()).getErrorRate();
  }

  @Override
  public void setStatsEnabled(boolean statsEnabled) {
    ((RendezvousChannel) getChannel()).setStatsEnabled(statsEnabled);
  }

  @Override
  public boolean isStatsEnabled() {
    return ((RendezvousChannel) getChannel()).isStatsEnabled();
  }

  @Override
  public void setLoggingEnabled(boolean enabled) {
    ((RendezvousChannel) getChannel()).setLoggingEnabled(enabled);
  }

  @Override
  public boolean isLoggingEnabled() {
    return ((RendezvousChannel) getChannel()).isLoggingEnabled();
  }

  @Override
  public void reset() {
    ((RendezvousChannel) getChannel()).reset();
  }

  @Override
  public void setCountsEnabled(boolean countsEnabled) {
    ((RendezvousChannel) getChannel()).setCountsEnabled(countsEnabled);
  }

  @Override
  public boolean isCountsEnabled() {
    return ((RendezvousChannel) getChannel()).isCountsEnabled();
  }

  @Override
  public ManagementOverrides getOverrides() {
    return ((RendezvousChannel) getChannel()).getOverrides();
  }

  @Override
  public void destroy() throws Exception {
    ((RendezvousChannel) getChannel()).destroy();
  }

  @Override
  public int getReceiveCount() {
    return ((RendezvousChannel) getChannel()).getReceiveCount();
  }

  @Override
  public long getReceiveCountLong() {
    return ((RendezvousChannel) getChannel()).getReceiveCountLong();
  }

  @Override
  public int getReceiveErrorCount() {
    return ((RendezvousChannel) getChannel()).getReceiveErrorCount();
  }

  @Override
  public long getReceiveErrorCountLong() {
    return ((RendezvousChannel) getChannel()).getReceiveErrorCountLong();
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
