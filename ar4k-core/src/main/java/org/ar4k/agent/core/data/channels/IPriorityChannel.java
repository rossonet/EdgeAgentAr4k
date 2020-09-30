package org.ar4k.agent.core.data.channels;

import org.ar4k.agent.core.data.AbstractChannel;
import org.ar4k.agent.core.interfaces.EdgeChannel;
import org.springframework.integration.channel.PriorityChannel;
import org.springframework.integration.support.management.PollableChannelManagement;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;

public class IPriorityChannel extends AbstractChannel implements PollableChannel, PollableChannelManagement {

	public IPriorityChannel() {
		final PriorityChannel c = new PriorityChannel();
		super.setChannel(c);
		super.setChannelType(Type.PRIORITY);
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
	public Class<? extends EdgeChannel> getChannelClass() {
		return this.getClass();
	}

}
