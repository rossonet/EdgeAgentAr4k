package org.ar4k.agent.hazelcast;

import java.util.HashMap;
import java.util.Map;

import org.ar4k.agent.core.data.AbstractChannel;
import org.springframework.messaging.MessageHeaders;

import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

public class ExternalMessageHandler implements MessageListener<Object> {

	private ITopic<Object> source = null;
	private AbstractChannel target = null;

	public ExternalMessageHandler(ITopic<Object> source, AbstractChannel target) {
		this.source = source;
		this.target = target;
	}

	@Override
	public void onMessage(Message<Object> message) {
		final HazelcastMessage messageTo = new HazelcastMessage();
		final Map<String, Object> headersMap = new HashMap<>();
		headersMap.put("publishing-member", message.getPublishingMember());
		headersMap.put("publish-time", message.getPublishTime());
		headersMap.put("publish-source", message.getSource());
		final MessageHeaders headers = new MessageHeaders(headersMap);
		messageTo.setHeaders(headers);
		messageTo.setPayload(message.getMessageObject());
		target.getChannel().send(messageTo);
	}

	public ITopic<Object> getSource() {
		return source;
	}

	public void setSource(ITopic<Object> source) {
		this.source = source;
	}

	public AbstractChannel getTarget() {
		return target;
	}

	public void setTarget(AbstractChannel target) {
		this.target = target;
	}

}
