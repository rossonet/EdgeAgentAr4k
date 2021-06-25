package org.ar4k.agent.iot.serial.json;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.messages.SerialJsonMessage;
import org.ar4k.agent.core.interfaces.EdgeChannel;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.iot.serial.SerialConfig;
import org.ar4k.agent.iot.serial.SerialService;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.json.JSONObject;
import org.springframework.messaging.MessageHeaders;

import com.fazecast.jSerialComm.SerialPortEvent;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
public class SerialJsonService extends SerialService {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(SerialJsonService.class.toString());

	private Homunculus homunculus = Homunculus.getApplicationContext().getBean(Homunculus.class);

	private EdgeChannel channelRoot = null;
	private IPublishSubscribeChannel exceptionChannel = null;
	private IPublishSubscribeChannel elseChannel = null;
	private SerialJsonConfig configuration = null;

	private Map<String, IPublishSubscribeChannel> channelMap = new HashMap<>();

	private String sb = "";

	@Override
	public void init() {
		super.init();
		popolateDataTopics();
	}

	@Override
	public SerialConfig getConfiguration() {
		return configuration;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		super.setConfiguration(configuration);
		this.configuration = ((SerialJsonConfig) configuration);
	}

	@Override
	protected void callProtectedEvent(SerialPortEvent message) {
		try {
			sb += new String(message.getReceivedData(), StandardCharsets.UTF_8);
			if (sb.contains("\n")) {
				final String[] datasToWork = sb.split("\n");
				if (datasToWork.length > 1) {
					sb = datasToWork[datasToWork.length - 1];
				}
				for (int i = 0; (datasToWork.length < 2 && i < datasToWork.length)
						|| (i < datasToWork.length - 1); i++) {
					try {
						final JSONObject jsonMessage = new JSONObject(datasToWork[i]);
						if (jsonMessage.has(configuration.getTagLabel())) {
							final SerialJsonMessage messageToSend = new SerialJsonMessage();
							final Map<String, Object> headersMap = new HashMap<>();
							headersMap.put("serial-port", message.getSerialPort());
							headersMap.put("publish-source", message.getSource());
							headersMap.put("type", "json");
							final MessageHeaders headers = new MessageHeaders(headersMap);
							messageToSend.setHeaders(headers);
							final JSONObject jsonMessageData = new JSONObject();
							// jsonMessageData.put("data", jsonMessage.getJSONObject("data"));
							jsonMessageData.put("message", jsonMessage);
							messageToSend.setPayload(jsonMessageData);
							getChannelByLabel(jsonMessage.getString(configuration.getTagLabel())).send(messageToSend);
						} else {
							final SerialJsonMessage messageElse = new SerialJsonMessage();
							final Map<String, Object> headersMapElse = new HashMap<>();
							headersMapElse.put("serial-port", message.getSerialPort());
							headersMapElse.put("publish-source", message.getSource());
							headersMapElse.put("type", "json");
							final MessageHeaders headersException = new MessageHeaders(headersMapElse);
							messageElse.setHeaders(headersException);
							messageElse.setPayload(jsonMessage);
							elseChannel.send(messageElse);
						}
					} catch (final Exception f) {
						try {
							final SerialJsonMessage messageException = new SerialJsonMessage();
							final Map<String, Object> headersMap = new HashMap<>();
							headersMap.put("serial-port", message.getSerialPort());
							headersMap.put("publish-source", message.getSource());
							headersMap.put("type", "json");
							final MessageHeaders headersException = new MessageHeaders(headersMap);
							messageException.setHeaders(headersException);
							final JSONObject jsonExceptionMessage = new JSONObject();
							jsonExceptionMessage.put("exception", EdgeLogger.stackTraceToString(f));
							jsonExceptionMessage.put("message", datasToWork[i]);
							messageException.setPayload(jsonExceptionMessage);
							exceptionChannel.send(messageException);
						} catch (final Exception e) {
							logger.logException(e);
						}
					}
				}
			}
		} catch (final Exception a) {
			try {
				final SerialJsonMessage messageException = new SerialJsonMessage();
				final Map<String, Object> headersMap = new HashMap<>();
				headersMap.put("serial-port", message.getSerialPort());
				headersMap.put("publish-source", message.getSource());
				headersMap.put("type", "json");
				final MessageHeaders headersException = new MessageHeaders(headersMap);
				messageException.setHeaders(headersException);
				final JSONObject jsonExceptionMessage = new JSONObject();
				jsonExceptionMessage.put("exception", EdgeLogger.stackTraceToString(a));
				jsonExceptionMessage.put("message-buffer", sb);
				messageException.setPayload(jsonExceptionMessage);
				exceptionChannel.send(messageException);
			} catch (final Exception e) {
				logger.logException(e);
			}
		}
	}

	private IPublishSubscribeChannel getChannelByLabel(String tagLabel) {
		if (!channelMap.containsKey(tagLabel)) {
			final IPublishSubscribeChannel newChannel = (IPublishSubscribeChannel) getDataAddress()
					.createOrGetDataChannel(configuration.getPreChannelName() + tagLabel,
							IPublishSubscribeChannel.class, "serial port json tag " + tagLabel, channelRoot,
							homunculus.getDataAddress().getDefaultScope(), homunculus.getTags(), this);
			newChannel.addTag(tagLabel);
			channelMap.put(tagLabel, newChannel);
		}
		return channelMap.get(tagLabel);
	}

	private void popolateDataTopics() {
		exceptionChannel = (IPublishSubscribeChannel) getDataAddress().createOrGetDataChannel(
				configuration.getBrokenMessage(), IPublishSubscribeChannel.class, "serial port json exception channel",
				channelRoot, homunculus.getDataAddress().getDefaultScope(), homunculus.getTags(), this);
		elseChannel = (IPublishSubscribeChannel) getDataAddress().createOrGetDataChannel(
				configuration.getDefaultChannel(), IPublishSubscribeChannel.class,
				"serial port json message not routed", channelRoot, homunculus.getDataAddress().getDefaultScope(),
				homunculus.getTags(), this);
		exceptionChannel.addTag("exception");
		elseChannel.addTag("not-routed");
	}

}
