/**
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    */
package org.ar4k.agent.iot.serial;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
//import org.ar4k.agent.camel.DynamicRouteBuilder;
import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.AbstractAr4kService;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.data.Ar4kChannel;
import org.ar4k.agent.core.data.channels.INoDataChannel;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.json.JSONObject;
import org.springframework.messaging.MessageHeaders;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio di connessione seriale.
 */
public class SerialService extends AbstractAr4kService implements SerialPortDataListener {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(SerialService.class.toString());

  private SerialPort comPort = null;

  private SerialConfig configuration = null;

  private Anima anima = Anima.getApplicationContext().getBean(Anima.class);

  public static enum BaudRate {
    bs150, bs300, bs600, bs1200, bs1800, bs2400, bs4800, bs7200, bs9600, bs14400, bs19200, bs38400, bs56000, bs57600,
    bs76800, bs115200, bs128000, bs230400, bs250000, auto

  }

  public static enum ConventionalNotation {
    _8N1, _7E1
  }

  private IPublishSubscribeChannel readChannelBytes = null;

  private IPublishSubscribeChannel readChannel = null;

  private IPublishSubscribeChannel writeChannel = null;

  private IPublishSubscribeChannel writeChannelBytes = null;

  private HandlerBytesWriter handlerBytesWriter = null;

  private HandlerStringWriter handlerStringWriter = null;

  @Override
  public synchronized void loop() {
    if (comPort == null) {
      openPort();
    } else {
      checkPort();
    }
  }

  private void checkPort() {
    if (comPort.isOpen() != true) {
      comPort = null;
    }
  }

  private void openPort() {
    try {
      comPort = SerialPort.getCommPort(configuration.serial);
    } catch (Exception ee) {
      logger.logException(ee);
    }
    // configura baudrate e bit controllo
    comPort.setBaudRate(Integer.valueOf(configuration.baudRate.name().replace("bs", "")));
    if (configuration.conventionalNotation.equals(ConventionalNotation._8N1)) {
      comPort.setNumDataBits(8);
      comPort.setNumStopBits(1);
      comPort.setParity(0);
    }
    if (configuration.conventionalNotation.equals(ConventionalNotation._7E1)) {
      comPort.setNumDataBits(7);
      comPort.setNumStopBits(1);
      comPort.setParity(1);
    }
    // apre la porta
    comPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, configuration.clockRunnableClass,
        configuration.clockRunnableClass);
    comPort.openPort();
    comPort.addDataListener(this);
  }

  @Override
  public void kill() {
    if (comPort != null) {
      comPort.closePort();
      comPort = null;
    }
    super.kill();
  }

  @Override
  public SerialConfig getConfiguration() {
    return configuration;
  }

  @Override
  public void setConfiguration(AbstractServiceConfig configuration) {
    super.setConfiguration(configuration);
    this.configuration = ((SerialConfig) configuration);
  }

  @Override
  public Anima getAnima() {
    return anima;
  }

  @Override
  public void setAnima(Anima anima) {
    super.setAnima(anima);
    this.anima = anima;
  }

  // ritorna le seriali disponibili
  public static SerialPort[] getSerialDevice() {
    return SerialPort.getCommPorts();
  }

  @Override
  public void init() {
    popolateDataTopics();
  }

  private void popolateDataTopics() {
    Ar4kChannel channelRoot = anima.getDataAddress().createOrGetDataChannel(configuration.fatherOfChannels,
        INoDataChannel.class, (String) null, null);
    readChannel = (IPublishSubscribeChannel) anima.getDataAddress().createOrGetDataChannel(configuration.endpointRead,
        IPublishSubscribeChannel.class, channelRoot,
        configuration.scopeOfChannels != null ? configuration.scopeOfChannels
            : anima.getDataAddress().getDefaultScope());
    readChannelBytes = (IPublishSubscribeChannel) anima.getDataAddress().createOrGetDataChannel(
        configuration.endpointRead, IPublishSubscribeChannel.class, channelRoot,
        configuration.scopeOfChannels != null ? configuration.scopeOfChannels
            : anima.getDataAddress().getDefaultScope());
    writeChannel = (IPublishSubscribeChannel) anima.getDataAddress().createOrGetDataChannel(configuration.endpointRead,
        IPublishSubscribeChannel.class, channelRoot,
        configuration.scopeOfChannels != null ? configuration.scopeOfChannels
            : anima.getDataAddress().getDefaultScope());
    writeChannelBytes = (IPublishSubscribeChannel) anima.getDataAddress().createOrGetDataChannel(
        configuration.endpointRead, IPublishSubscribeChannel.class, channelRoot,
        configuration.scopeOfChannels != null ? configuration.scopeOfChannels
            : anima.getDataAddress().getDefaultScope());
    readChannel.addTag("serial-read");
    readChannelBytes.addTag("serial-read-bytes");
    writeChannel.addTag("serial-write");
    writeChannelBytes.addTag("serial-write-bytes");
    handlerBytesWriter = new HandlerBytesWriter(this);
    handlerStringWriter = new HandlerStringWriter(this);
    writeChannel.subscribe(handlerStringWriter);
    writeChannelBytes.subscribe(handlerBytesWriter);
  }

  @Override
  public void setConfiguration(ConfigSeed configuration) {
    this.configuration = (SerialConfig) configuration;
  }

  @Override
  public String getStatusString() {
    return comPort != null ? comPort.getPortDescription() : "DISCONNECTED";
  }

  @Override
  public JSONObject getStatusJson() {
    JSONObject end = new JSONObject();
    end.put("status", getStatusString());
    return end;
  }

  @Override
  public void close() throws IOException {
    kill();
  }

  @Override
  public void stop() {
    kill();
  }

  @Override
  public int getListeningEvents() {
    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
  }

  @Override
  public void serialEvent(SerialPortEvent message) {
    callProtectedEvent(message);
    SerialBytesMessage messageToBytes = new SerialBytesMessage();
    SerialStringMessage messageToString = new SerialStringMessage();
    final Map<String, Object> headersMapString = new HashMap<>();
    headersMapString.put("serial-port", message.getSerialPort());
    headersMapString.put("publish-source", message.getSource());
    headersMapString.put("type", "string");
    final MessageHeaders headersString = new MessageHeaders(headersMapString);
    messageToString.setHeaders(headersString);
    final Map<String, Object> headersMapBytes = new HashMap<>();
    headersMapBytes.put("serial-port", message.getSerialPort());
    headersMapBytes.put("publish-source", message.getSource());
    headersMapBytes.put("type", "bytes");
    final MessageHeaders headersBytes = new MessageHeaders(headersMapBytes);
    messageToBytes.setHeaders(headersBytes);
    messageToBytes.setPayload(ArrayUtils.toObject(message.getReceivedData()));
    messageToString.setPayload(new String(message.getReceivedData(), StandardCharsets.UTF_8));
    readChannelBytes.send(messageToBytes);
    readChannel.send(messageToString);
  }

  protected void callProtectedEvent(SerialPortEvent message) {
  }

  public SerialPort getComPort() {
    return comPort;
  }
}
