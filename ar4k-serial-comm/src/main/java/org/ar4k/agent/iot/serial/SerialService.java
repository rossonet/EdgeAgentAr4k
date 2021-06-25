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
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.messages.SerialBytesMessage;
import org.ar4k.agent.core.data.messages.SerialStringMessage;
import org.ar4k.agent.core.interfaces.EdgeChannel;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.springframework.messaging.MessageHeaders;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio di connessione seriale.
 */
public class SerialService implements EdgeComponent, SerialPortDataListener {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(SerialService.class.toString());

	private SerialPort comPort = null;

	private SerialConfig configuration = null;

	private Homunculus homunculus = Homunculus.getApplicationContext().getBean(Homunculus.class);

	public static enum BaudRate {
		bs150, bs300, bs600, bs1200, bs1800, bs2400, bs4800, bs7200, bs9600, bs14400, bs19200, bs38400, bs56000,
		bs57600, bs76800, bs115200, bs128000, bs230400, bs250000, auto

	}

	public static enum ConventionalNotation {
		_8N1, _7E1
	}

	private EdgeChannel channelRoot = null;

	private IPublishSubscribeChannel readChannelBytes = null;

	private IPublishSubscribeChannel readChannel = null;

	private IPublishSubscribeChannel writeChannel = null;

	private IPublishSubscribeChannel writeChannelBytes = null;

	private HandlerBytesWriter handlerBytesWriter = null;

	private HandlerStringWriter handlerStringWriter = null;

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
		dataspace.removeDataChannel(writeChannelBytes, true);
		dataspace.removeDataChannel(writeChannel, true);
		dataspace.removeDataChannel(readChannelBytes, true);
		dataspace.removeDataChannel(readChannel, true);
		dataspace.removeDataChannel(channelRoot, true);
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
	public Homunculus getHomunculus() {
		return homunculus;
	}

	@Override
	public void setHomunculus(Homunculus homunculus) {
		this.homunculus = homunculus;
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
		readChannel = (IPublishSubscribeChannel) dataspace.createOrGetDataChannel(configuration.getEndpointRead(),
				IPublishSubscribeChannel.class, "serial port " + comPort.getSystemPortName() + " read channel",
				channelRoot, homunculus.getDataAddress().getDefaultScope(), getConfiguration().getTags(), this);
		readChannelBytes = (IPublishSubscribeChannel) dataspace.createOrGetDataChannel(
				configuration.getEndpointReadByte(), IPublishSubscribeChannel.class,
				"serial port " + comPort.getSystemPortName() + " read bytes node", channelRoot,
				homunculus.getDataAddress().getDefaultScope(), getConfiguration().getTags(), this);
		writeChannel = (IPublishSubscribeChannel) dataspace.createOrGetDataChannel(configuration.getEndpointWrite(),
				IPublishSubscribeChannel.class, "serial port " + comPort.getSystemPortName() + " write node",
				channelRoot, homunculus.getDataAddress().getDefaultScope(), getConfiguration().getTags(), this);
		writeChannelBytes = (IPublishSubscribeChannel) dataspace.createOrGetDataChannel(
				configuration.getEndpointWriteByte(), IPublishSubscribeChannel.class,
				"serial port " + comPort.getSystemPortName() + " write bytes node", channelRoot,
				homunculus.getDataAddress().getDefaultScope(), getConfiguration().getTags(), this);
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
	public String getServiceName() {
		return getConfiguration().getName();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SerialService [");
		if (comPort != null) {
			builder.append("comPort=");
			builder.append(comPort);
			builder.append(", ");
		}
		if (configuration != null) {
			builder.append("configuration=");
			builder.append(configuration);
			builder.append(", ");
		}
		if (homunculus != null) {
			builder.append("homunculus=");
			builder.append(homunculus);
			builder.append(", ");
		}
		if (channelRoot != null) {
			builder.append("channelRoot=");
			builder.append(channelRoot);
			builder.append(", ");
		}
		if (readChannelBytes != null) {
			builder.append("readChannelBytes=");
			builder.append(readChannelBytes);
			builder.append(", ");
		}
		if (readChannel != null) {
			builder.append("readChannel=");
			builder.append(readChannel);
			builder.append(", ");
		}
		if (writeChannel != null) {
			builder.append("writeChannel=");
			builder.append(writeChannel);
			builder.append(", ");
		}
		if (writeChannelBytes != null) {
			builder.append("writeChannelBytes=");
			builder.append(writeChannelBytes);
			builder.append(", ");
		}
		if (handlerBytesWriter != null) {
			builder.append("handlerBytesWriter=");
			builder.append(handlerBytesWriter);
			builder.append(", ");
		}
		if (handlerStringWriter != null) {
			builder.append("handlerStringWriter=");
			builder.append(handlerStringWriter);
			builder.append(", ");
		}
		if (dataspace != null) {
			builder.append("dataspace=");
			builder.append(dataspace);
		}
		builder.append("]");
		return builder.toString();
	}
}
