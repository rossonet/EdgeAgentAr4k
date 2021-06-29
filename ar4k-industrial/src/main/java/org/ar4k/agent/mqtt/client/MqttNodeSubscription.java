package org.ar4k.agent.mqtt.client;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttNodeSubscription {
	
	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(MqttNodeSubscription.class);

	public MqttNodeSubscription(MqttClient mqttClient, MqttTopicConfig t) {
		try {
			mqttClient.subscribe(t.topic);
		} catch (MqttException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
	}

}
