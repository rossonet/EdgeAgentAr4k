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
package org.ar4k.agent.iot.serial.cnc;

import java.util.HashMap;
import java.util.Map;

import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.iot.serial.SerialService;
import org.ar4k.agent.iot.serial.SerialStringMessage;
import org.springframework.messaging.MessageHeaders;

import com.fazecast.jSerialComm.SerialPortEvent;
import com.google.common.base.Charsets;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio collegamento macchine CNC via seriale/telnet
 *
 */
public class CncService extends SerialService {

	long conteggioLoop = 0;

	private CncConfig configuration = null;

	private Anima anima = Anima.getApplicationContext().getBean(Anima.class);

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		super.updateAndGetStatus();
		conteggioLoop++;
		for (final TriggerCommand c : configuration.cronCommands) {
			if (conteggioLoop % c.timer == 0) {
				this.getComPort().writeBytes(c.getBytesCommand(), c.getSizeBytesCommand());
			}
		}
		return ServiceStatus.RUNNING;
	}

	@Override
	public CncConfig getConfiguration() {
		return configuration;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		super.setConfiguration(configuration);
		this.configuration = ((CncConfig) configuration);
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

	@Override
	protected void callProtectedEvent(SerialPortEvent message) {
		for (final RouterMessagesCnc testRoute : configuration.repliesAnalizer) {
			final String messageTxt = new String(message.getReceivedData(), Charsets.UTF_8);
			if (testRoute.matches(messageTxt)) {
				final SerialStringMessage messageToString = new SerialStringMessage();
				final Map<String, Object> headersMapString = new HashMap<>();
				headersMapString.put("serial-port", message.getSerialPort());
				headersMapString.put("publish-source", message.getSource());
				headersMapString.put("type", "string");
				final MessageHeaders headersStringMessage = new MessageHeaders(headersMapString);
				messageToString.setHeaders(headersStringMessage);
				messageToString.setPayload(messageTxt);
				// TODO DataAddress
				// IPublishSubscribeChannel channel =
				// testRoute.getAr4kChannel(configuration.getFatherOfChannels(),
				// configuration.scopeOfChannels);
				// channel.send(messageToString);
			}
		}
	}

}
