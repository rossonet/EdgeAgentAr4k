package org.ar4k.agent.mqtt.client;

import org.ar4k.agent.core.data.channels.EdgeChannel;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttTopicSubscription implements IMqttMessageListener {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(MqttTopicSubscription.class);
	private final PahoClientService pahoClientService;

	private MqttWriter writeChannel = null;
	private EdgeChannel topicCallback = null;
	private final MqttTopicConfig mqttTopicConfig;

	public MqttTopicSubscription(PahoClientService pahoClientService, MqttClient mqttClient, MqttTopicConfig t) {
		this.mqttTopicConfig = t;
		this.pahoClientService = pahoClientService;
		if (t.readChannel != null && !t.readChannel.isEmpty()) {
			try {
				mqttClient.subscribe(t.topic, t.qosRead, this);
			} catch (MqttException exception) {
				logger.logException(exception);
			}
		}
		if (t.writeChannel != null && !t.writeChannel.isEmpty()) {
			addWriteChannel();
		}
	}

	private void addWriteChannel() {
		final EdgeChannel channelWrite = pahoClientService.getDataAddress().createOrGetDataChannel(
				mqttTopicConfig.writeChannel, IPublishSubscribeChannel.class,
				"mqtt write channel to " + mqttTopicConfig.topic, mqttTopicConfig.fatherOfChannels,
				mqttTopicConfig.scopeOfChannels,
				ConfigHelper.mergeTags(mqttTopicConfig.tags, pahoClientService.getConfiguration().getTags()),
				pahoClientService);
		writeChannel = new MqttWriter(pahoClientService, mqttTopicConfig);
		((IPublishSubscribeChannel) channelWrite).subscribe(writeChannel);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		if (topicCallback != null) {
			final EdgeChannel channel = pahoClientService
					.getDataAddress().createOrGetDataChannel(mqttTopicConfig.readChannel,
							IPublishSubscribeChannel.class, "mqtt data from " + mqttTopicConfig.topic,
							mqttTopicConfig.fatherOfChannels, mqttTopicConfig.scopeOfChannels, ConfigHelper
									.mergeTags(mqttTopicConfig.tags, pahoClientService.getConfiguration().getTags()),
							pahoClientService);
			topicCallback = channel;
			// TO______DO completare invio messaggi verso core
		}

	}

}
