package org.ar4k.agent.core.data.channels;

import org.ar4k.agent.core.data.AbstractChannel;
import org.ar4k.agent.core.interfaces.EdgeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.management.PollableChannelManagement;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;

public class IQueueChannel extends AbstractChannel implements PollableChannel, PollableChannelManagement {

	public IQueueChannel() {
		final QueueChannel c = new QueueChannel(50);
		super.setChannel(c);
		super.setChannelType(Type.QUEUE);
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
	public Class<? extends EdgeChannel> getChannelClass() {
		return this.getClass();
	}

}
