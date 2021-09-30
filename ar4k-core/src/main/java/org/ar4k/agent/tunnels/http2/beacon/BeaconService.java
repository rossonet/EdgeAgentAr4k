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
package org.ar4k.agent.tunnels.http2.beacon;

import java.io.IOException;
import java.security.UnrecoverableKeyException;
import java.util.Arrays;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.channels.EdgeChannel;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.messages.StringMessage;
import org.ar4k.agent.core.services.EdgeComponent;
import org.ar4k.agent.core.services.ServiceConfig;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http2.beacon.server.BeaconServer;
import org.ar4k.agent.tunnels.http2.beacon.server.BeaconServerBuilder;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio Beacon per telemetria
 *
 */
public class BeaconService implements EdgeComponent {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(BeaconService.class);

	// iniettata vedi set/get
	private BeaconServiceConfig configuration = null;

	private Homunculus homunculusBase = null;

	private DataAddress dataAddressBase = null;

	private BeaconServer beaconServer = null;

	private EdgeChannel statusChannel = null;

	@Override
	public void close() throws Exception {
		if (beaconServer != null) {
			beaconServer.close();
		}
	}

	public BeaconServer getBeaconServer() {
		return beaconServer;
	}

	@Override
	public ServiceConfig getConfiguration() {
		return configuration;
	}

	@Override
	public DataAddress getDataAddress() {
		return dataAddressBase;
	}

	@Override
	public Homunculus getHomunculus() {
		return homunculusBase;
	}

	@Override
	public synchronized void init() {
		setDataspace();
		try {
			if (beaconServer == null) {
				beaconServer = new BeaconServerBuilder().setHomunculus(homunculusBase).setPort(configuration.port)
						.setDiscoveryPort(configuration.discoveryPort).setCaChainPem(configuration.caChainPem)
						.setAliasBeaconServerInKeystore(configuration.aliasBeaconServerInKeystore)
						.setPrivateKeyFile(configuration.privateKeyFile).setCertFile(configuration.certFile)
						.setCertChainFile(configuration.certChainFile).setStringDiscovery(configuration.stringDiscovery)
						.setBroadcastAddress(configuration.broadcastAddress)
						.setAcceptCerts(configuration.acceptAllCerts)
						.setAliasBeaconServerSignMaster(configuration.aliasBeaconServerSignMaster).build();
				beaconServer.start();
			}
		} catch (final IOException e) {
			logger.logException(e);
		} catch (final UnrecoverableKeyException e) {
			logger.warn(e.getMessage());
		}
	}

	private void setDataspace() {
		statusChannel = dataAddressBase.createOrGetDataChannel("statusChannel", IPublishSubscribeChannel.class,
				"statusChannel of beacon server", homunculusBase.getDataAddress().getSystemChannel(), (String) null,
				ConfigHelper.mergeTags(Arrays.asList("beacon-service", "status"), getConfiguration().getTags()), this);
	}

	@Override
	public void kill() {
		if (beaconServer != null) {
			beaconServer.stop();
			beaconServer = null;
		}
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		this.configuration = (BeaconServiceConfig) configuration;
	}

	@Override
	public void setDataAddress(DataAddress dataAddressBase) {
		this.dataAddressBase = dataAddressBase;
	}

	@Override
	public void setHomunculus(Homunculus homunculusBase) {
		this.homunculusBase = homunculusBase;
	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		if (beaconServer == null) {
			init();
		} else {
			if (beaconServer.isStopped()) {
				beaconServer.stop();
				beaconServer = null;
				init();
			} else {
				beaconServer.clearOldData();
			}
		}
		final StringMessage message = new StringMessage();
		message.setPayload(ServiceStatus.RUNNING.name());
		statusChannel.getChannel().send(message);
		return ServiceStatus.RUNNING;
	}

	@Override
	public String getServiceName() {
		return getConfiguration().getName();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BeaconService [");
		if (beaconServer != null) {
			builder.append("beaconServer=");
			builder.append(beaconServer);
			builder.append(", ");
		}
		if (statusChannel != null) {
			builder.append("statusChannel=");
			builder.append(statusChannel);
		}
		builder.append("]");
		return builder.toString();
	}

}
