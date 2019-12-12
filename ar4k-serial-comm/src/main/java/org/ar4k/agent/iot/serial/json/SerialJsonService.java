package org.ar4k.agent.iot.serial.json;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.data.Ar4kChannel;
import org.ar4k.agent.core.data.channels.INoDataChannel;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.iot.serial.SerialConfig;
import org.ar4k.agent.iot.serial.SerialService;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.json.JSONObject;
import org.springframework.messaging.MessageHeaders;

import com.fazecast.jSerialComm.SerialPortEvent;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
public class SerialJsonService extends SerialService {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(SerialJsonService.class.toString());

  private Anima anima = Anima.getApplicationContext().getBean(Anima.class);

  private Ar4kChannel channelRoot = null;
  private IPublishSubscribeChannel exceptionChannel = null;
  private IPublishSubscribeChannel elseChannel = null;
  private SerialJsonConfig configuration = null;

  private Map<String, IPublishSubscribeChannel> channelMap = new HashMap<>();

  private String sb = "";

  @Override
  public void loop() {
    super.loop();
  }

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
  public void setConfiguration(AbstractServiceConfig configuration) {
    super.setConfiguration(configuration);
    this.configuration = ((SerialJsonConfig) configuration);
  }

  @Override
  protected void callProtectedEvent(SerialPortEvent message) {
    try {
      sb += new String(message.getReceivedData(), StandardCharsets.UTF_8);
      if (sb.contains("\n")) {
        String[] datasToWork = sb.split("\n");
        if (datasToWork.length > 1) {
          sb = datasToWork[datasToWork.length - 1];
        }
        for (int i = 0; (datasToWork.length < 2 && i < datasToWork.length) || (i < datasToWork.length - 1); i++) {
          try {
            JSONObject jsonMessage = new JSONObject(datasToWork[i]);
            if (jsonMessage.has(configuration.getTagLabel())) {
              SerialJsonMessage messageToSend = new SerialJsonMessage();
              final Map<String, Object> headersMap = new HashMap<>();
              headersMap.put("serial-port", message.getSerialPort());
              headersMap.put("publish-source", message.getSource());
              headersMap.put("type", "json");
              final MessageHeaders headers = new MessageHeaders(headersMap);
              messageToSend.setHeaders(headers);
              JSONObject jsonMessageData = new JSONObject();
              // jsonMessageData.put("data", jsonMessage.getJSONObject("data"));
              jsonMessageData.put("message", jsonMessage);
              messageToSend.setPayload(jsonMessageData);
              getChannelByLabel(jsonMessage.getString(configuration.getTagLabel())).send(messageToSend);
            } else {
              SerialJsonMessage messageElse = new SerialJsonMessage();
              final Map<String, Object> headersMapElse = new HashMap<>();
              headersMapElse.put("serial-port", message.getSerialPort());
              headersMapElse.put("publish-source", message.getSource());
              headersMapElse.put("type", "json");
              final MessageHeaders headersException = new MessageHeaders(headersMapElse);
              messageElse.setHeaders(headersException);
              messageElse.setPayload(jsonMessage);
              elseChannel.send(messageElse);
            }
          } catch (Exception f) {
            try {
              SerialJsonMessage messageException = new SerialJsonMessage();
              final Map<String, Object> headersMap = new HashMap<>();
              headersMap.put("serial-port", message.getSerialPort());
              headersMap.put("publish-source", message.getSource());
              headersMap.put("type", "json");
              final MessageHeaders headersException = new MessageHeaders(headersMap);
              messageException.setHeaders(headersException);
              JSONObject jsonExceptionMessage = new JSONObject();
              jsonExceptionMessage.put("exception", Ar4kLogger.stackTraceToString(f));
              jsonExceptionMessage.put("message", datasToWork[i]);
              messageException.setPayload(jsonExceptionMessage);
              exceptionChannel.send(messageException);
            } catch (Exception e) {
              logger.logException(e);
            }
          }
        }
      }
    } catch (Exception a) {
      try {
        SerialJsonMessage messageException = new SerialJsonMessage();
        final Map<String, Object> headersMap = new HashMap<>();
        headersMap.put("serial-port", message.getSerialPort());
        headersMap.put("publish-source", message.getSource());
        headersMap.put("type", "json");
        final MessageHeaders headersException = new MessageHeaders(headersMap);
        messageException.setHeaders(headersException);
        JSONObject jsonExceptionMessage = new JSONObject();
        jsonExceptionMessage.put("exception", Ar4kLogger.stackTraceToString(a));
        jsonExceptionMessage.put("message-buffer", sb);
        messageException.setPayload(jsonExceptionMessage);
        exceptionChannel.send(messageException);
      } catch (Exception e) {
        logger.logException(e);
      }
    }
  }

  private IPublishSubscribeChannel getChannelByLabel(String tagLabel) {
    if (!channelMap.containsKey(tagLabel)) {
      IPublishSubscribeChannel newChannel = (IPublishSubscribeChannel) anima.getDataAddress().createOrGetDataChannel(
          configuration.getPreChannelName() + tagLabel, IPublishSubscribeChannel.class,
          "serial port json tag " + tagLabel, channelRoot,
          configuration.scopeOfChannels != null ? configuration.scopeOfChannels
              : anima.getDataAddress().getDefaultScope());
      newChannel.addTag(tagLabel);
      channelMap.put(tagLabel, newChannel);
    }
    return channelMap.get(tagLabel);
  }

  private void popolateDataTopics() {
    channelRoot = anima.getDataAddress().createOrGetDataChannel(configuration.getFatherOfChannels(),
        INoDataChannel.class, "serial port json root node", (String) null, null);
    exceptionChannel = (IPublishSubscribeChannel) anima.getDataAddress().createOrGetDataChannel(
        configuration.getBrokenMessage(), IPublishSubscribeChannel.class, "serial port json exception channel",
        channelRoot, configuration.scopeOfChannels != null ? configuration.scopeOfChannels
            : anima.getDataAddress().getDefaultScope());
    elseChannel = (IPublishSubscribeChannel) anima.getDataAddress().createOrGetDataChannel(
        configuration.getDefaultChannel(), IPublishSubscribeChannel.class, "serial port json message not routed",
        channelRoot, configuration.scopeOfChannels != null ? configuration.scopeOfChannels
            : anima.getDataAddress().getDefaultScope());
    exceptionChannel.addTag("exception");
    elseChannel.addTag("not-routed");
  }

}
