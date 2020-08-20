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
import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Ar4kComponent;
import org.ar4k.agent.core.data.Ar4kChannel;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
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
public class SerialService implements Ar4kComponent, SerialPortDataListener {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(SerialService.class.toString());

	private SerialPort comPort = null;

	private SerialConfig configuration = null;

	private Anima anima = Anima.getApplicationContext().getBean(Anima.class);

	public static enum BaudRate {
		bs150, bs300, bs600, bs1200, bs1800, bs2400, bs4800, bs7200, bs9600, bs14400, bs19200, bs38400, bs56000,
		bs57600, bs76800, bs115200, bs128000, bs230400, bs250000, auto

	}

	public static enum ConventionalNotation {
		_8N1, _7E1
	}

	private Ar4kChannel channelRoot = null;

	private IPublishSubscribeChannel readChannelBytes = null;

	private IPublishSubscribeChannel readChannel = null;

	private IPublishSubscribeChannel writeChannel = null;

	private IPublishSubscribeChannel writeChannelBytes = null;

	private HandlerBytesWriter handlerBytesWriter = null;

	private HandlerStringWriter handlerStringWriter = null;

	// TODO DataAddress
	private DataAddress dataspace = null;

	private void openSerialPort() {
		if (comPort == null) {
			openPort();
		} else {
			checkPort();
		}
	}

	private void checkPort() {
		if (comPort.isOpen()) {
			comPort = null;
			openPort();
		}
	}

	private void openPort() {
		try {
			logger.debug("try to open serial port " + configuration.serial);
			comPort = SerialPort.getCommPort(configuration.serial);
		} catch (final Exception ee) {
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
		logger.debug("Serial port " + comPort.getSystemPortName() + " opened");
	}

	@Override
	public void kill() {
		anima.getDataAddress().removeDataChannel(writeChannelBytes, true);
		anima.getDataAddress().removeDataChannel(writeChannel, true);
		anima.getDataAddress().removeDataChannel(readChannelBytes, true);
		anima.getDataAddress().removeDataChannel(readChannel, true);
		anima.getDataAddress().removeDataChannel(channelRoot, true);
		if (comPort != null) {
			comPort.closePort();
			comPort = null;
		}
	}

	@Override
	public SerialConfig getConfiguration() {
		return configuration;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		this.configuration = ((SerialConfig) configuration);
	}

	@Override
	public Anima getAnima() {
		return anima;
	}

	@Override
	public void setAnima(Anima anima) {
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
		openSerialPort();
		readChannel = (IPublishSubscribeChannel) anima.getDataAddress().createOrGetDataChannel(
				configuration.getEndpointRead(), IPublishSubscribeChannel.class,
				"serial port " + comPort.getSystemPortName() + " read channel", channelRoot,
				anima.getDataAddress().getDefaultScope(), anima.getTags());
		readChannelBytes = (IPublishSubscribeChannel) anima.getDataAddress().createOrGetDataChannel(
				configuration.getEndpointReadByte(), IPublishSubscribeChannel.class,
				"serial port " + comPort.getSystemPortName() + " read bytes node", channelRoot,
				anima.getDataAddress().getDefaultScope(), anima.getTags());
		writeChannel = (IPublishSubscribeChannel) anima.getDataAddress().createOrGetDataChannel(
				configuration.getEndpointWrite(), IPublishSubscribeChannel.class,
				"serial port " + comPort.getSystemPortName() + " write node", channelRoot,
				anima.getDataAddress().getDefaultScope(), anima.getTags());
		writeChannelBytes = (IPublishSubscribeChannel) anima.getDataAddress().createOrGetDataChannel(
				configuration.getEndpointWriteByte(), IPublishSubscribeChannel.class,
				"serial port " + comPort.getSystemPortName() + " write bytes node", channelRoot,
				anima.getDataAddress().getDefaultScope(), anima.getTags());
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
	public String toString() {
		return comPort != null ? comPort.getSystemPortName() + " [" + comPort.isOpen() + "]" : "DISCONNECTED";
	}

	@Override
	public void close() throws IOException {
		kill();
	}

	@Override
	public int getListeningEvents() {
		logger.debug("received listening request from serial port");
		return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
	}

	@Override
	public void serialEvent(SerialPortEvent message) {
		logger.debug("received serialEvent request from serial port");
		logger.debug("received from serial port => " + message != null ? message.toString() : "NaN");
		callProtectedEvent(message);
		final SerialBytesMessage messageToBytes = new SerialBytesMessage();
		final SerialStringMessage messageToString = new SerialStringMessage();
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

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		openSerialPort();
		return ServiceStatus.RUNNING;
	}

	@Override
	public DataAddress getDataAddress() {
		return dataspace;
	}

	@Override
	public void setDataAddress(DataAddress dataAddress) {
		this.dataspace = dataAddress;
	}

	@Override
	public JSONObject getDescriptionJson() {
		final JSONObject end = new JSONObject();
		end.put("status", toString());
		return end;
	}
}
