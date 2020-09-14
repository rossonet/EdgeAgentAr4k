package org.ar4k.agent.core.data.channels;

import org.ar4k.agent.core.data.AbstractChannel;
import org.ar4k.agent.core.interfaces.EdgeChannel;
import org.springframework.integration.channel.RendezvousChannel;
import org.springframework.integration.support.management.PollableChannelManagement;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;

public class IRendezvousChannel extends AbstractChannel implements PollableChannel, PollableChannelManagement {

	public IRendezvousChannel() {
		final RendezvousChannel c = new RendezvousChannel();
		super.setChannel(c);
		super.setChannelType(Type.RENDEZVOUS);
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
	public Class<? extends EdgeChannel> getChannelClass() {
		return this.getClass();
	}

}
