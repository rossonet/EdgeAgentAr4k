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
package org.ar4k.agent.mqtt.client;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.interfaces.EdgeComponent;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione client Paho.
 */
public class PahoClientConfig extends AbstractServiceConfig {

	private static final long serialVersionUID = -1385133280351173640L;
	@Parameter(names = "--persistenceOnFileSystem", description = "storage messages on filesystem directory. If null the persistance will be on ram")
	public String persistenceOnFileSystem = null;
	@Parameter(names = "--broker", description = "mqtt broker url")
	public String broker = null;
	@Parameter(names = "--clientId", description = "clientId for mqtt connection")
	public String clientId = "ar4k-" + UUID.randomUUID().toString();
	@Parameter(names = "--cleanSession", description = "if true, clean session when reconnect")
	public boolean cleanSession = true;
	@Parameter(names = "--connectionTimeout", description = "connection timeout in millisenconds")
	public int connectionTimeout = 5000;
	@Parameter(names = "--keepAliveInterval", description = "keep alive interval in seconds")
	public int keepAliveInterval = 20;
	@Parameter(names = "--maxInflight", description = "max inflight messages")
	public int maxInflight = 10;
	@Parameter(names = "--maxReconnectDelay", description = "max reconnection delay in milliseconds")
	public int maxReconnectDelay = 2000;
	@Parameter(names = "--userName", description = "username, if null the connecction will be anonymous")
	public String userName = null;
	@Parameter(names = "--password", description = "username, if null the connecction will be anonymous")
	public String password = null;
	@Parameter(names = "--subscriptions", description = "List of topics to subscribe", variableArity = true)
	public List<MqttTopicConfig> subscriptions = new ArrayList<>();

	@Override
	public EdgeComponent instantiate() {
		PahoClientService ss = new PahoClientService();
		ss.setConfiguration(this);
		return ss;
	}

	@Override
	public int getPriority() {
		return 6;
	}

	@Override
	public boolean isSpringBean() {
		return false;
	}
}
