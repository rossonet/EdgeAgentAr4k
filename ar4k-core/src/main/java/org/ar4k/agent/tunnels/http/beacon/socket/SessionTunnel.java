package org.ar4k.agent.tunnels.http.beacon.socket;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import io.netty.channel.ChannelHandlerContext;

class SessionTunnel implements AutoCloseable, Serializable {

	private static final long serialVersionUID = 5332515255490196858L;
	private final long sessionId;
	private final AtomicLong progressiveNetworkToBeacon = new AtomicLong(0);
	private final AtomicLong lastAckSent = new AtomicLong(0);
	private final AtomicLong lastAckReceived = new AtomicLong(0);
	private final Map<Long, MessageCached> inputCachedMessages = new ConcurrentHashMap<>();
	private final Map<Long, MessageCached> outputCachedMessages = new ConcurrentHashMap<>();
	private boolean callChannelClientComplete = false;
	private boolean callChannelClientException = false;
	private boolean callChannelServerComplete = false;
	private boolean callChannelServerException = false;
	private transient ChannelHandlerContext lastNettyContext = null;

	SessionTunnel(long sessionId) {
		this.sessionId = sessionId;
	}

	@Override
	public void close() throws Exception {
		for (final MessageCached m : inputCachedMessages.values()) {
			m.end();
		}
		inputCachedMessages.clear();
		for (final MessageCached m : outputCachedMessages.values()) {
			m.end();
		}
		outputCachedMessages.clear();

	}

	long getNextProgressiveNetworkToBeacon() {
		return progressiveNetworkToBeacon.incrementAndGet();
	}

	long getLastAckSent() {
		return lastAckSent.get();
	}

	long getLastAckReceived() {
		return lastAckReceived.get();
	}

	long getSessionId() {
		return sessionId;
	}

	@Override
	public int hashCode() {
		return Long.hashCode(sessionId);
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof SessionTunnel && ((SessionTunnel) obj).getSessionId() == getSessionId());
	}

	int countInputCachedMessages() {
		return inputCachedMessages.size();
	}

	int countOutputCachedMessages() {
		return outputCachedMessages.size();
	}

	Map<Long, MessageCached> getInputCachedMessagesMap() {
		return inputCachedMessages;
	}

	Map<Long, MessageCached> getOutputCachedMessagesMap() {
		return outputCachedMessages;
	}

	ChannelHandlerContext getLastNettyContext() {
		return lastNettyContext;
	}

	void addInputCachedMessage(long messageId, MessageCached messageCached) {
		inputCachedMessages.put(messageId, messageCached);

	}

	void addOutputCachedMessage(long messageId, MessageCached messageCached) {
		outputCachedMessages.put(messageId, messageCached);

	}

	void removeIputCachedMessage(long messageId) {
		inputCachedMessages.remove(messageId);
	}

	void removeOutputCachedMessage(long messageId) {
		outputCachedMessages.remove(messageId);

	}

	private void setLastAckReceived(long messageUuid) {
		lastAckReceived.set(messageUuid);
	}

	private void setLastAckSent(long messageUuid) {
		lastAckSent.set(messageUuid);
	}

	synchronized void updateLastAckReceivedIfNeed(long messageUuid) {
		if (messageUuid > getLastAckReceived()) {
			setLastAckReceived(messageUuid);
		}
	}

	synchronized void updateLastAckSentIfNeed(long messageUuid) {
		if (messageUuid > getLastAckSent()) {
			setLastAckSent(messageUuid);
		}
	}

	void callChannelClientComplete() {
		callChannelClientComplete = true;
	}

	void callChannelClientException() {
		callChannelClientException = true;
	}

	void callChannelServerComplete() {
		callChannelServerComplete = true;
	}

	void callChannelServerException() {
		callChannelServerException = true;
	}

	boolean isCallChannelClientComplete() {
		return callChannelClientComplete;
	}

	boolean isCallChannelClientException() {
		return callChannelClientException;
	}

	boolean isCallChannelServerComplete() {
		return callChannelServerComplete;
	}

	boolean isCallChannelServerException() {
		return callChannelServerException;
	}

	public void setLastNettyContext(ChannelHandlerContext ctx) {
		this.lastNettyContext = ctx;

	}

}
