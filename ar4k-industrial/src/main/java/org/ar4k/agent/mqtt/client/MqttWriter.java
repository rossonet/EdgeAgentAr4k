package org.ar4k.agent.mqtt.client;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

public class MqttWriter implements MessageHandler {

	private PahoClientService pahoClientService;
	private MqttTopicConfig singleNode;

	public MqttWriter(PahoClientService pahoClientService, MqttTopicConfig singleNode) {
		this.pahoClientService = pahoClientService;
		this.singleNode = singleNode;
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		// TO______DO completare scrittura messaggi su coda mqtt

	}

}
