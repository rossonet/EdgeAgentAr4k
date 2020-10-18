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

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio Beacon per telemetria
 *
 */
public class BeaconService implements EdgeComponent {

  private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(Homunculus.class.toString());

  // iniettata vedi set/get
  private BeaconServiceConfig configuration = null;

  private Homunculus homunculus = null;

  private DataAddress dataAddress = null;

  private BeaconServer beaconServer = null;

  @Override
  public Homunculus getHomunculus() {
    return homunculus;
  }

  @Override
  public void setHomunculus(Homunculus homunculus) {
    this.homunculus = homunculus;
  }

  @Override
  public synchronized void init() {
    try {
      if (beaconServer == null) {
        beaconServer = new BeaconServer.Builder().setHomunculus(homunculus).setPort(configuration.port)
            .setDiscoveryPort(configuration.discoveryPort).setCaChainPem(configuration.caChainPem)
            .setAliasBeaconServerInKeystore(configuration.aliasBeaconServerInKeystore)
            .setPrivateKeyFile(configuration.privateKeyFile).setCertFile(configuration.certFile)
            .setCertChainFile(configuration.certChainFile).setStringDiscovery(configuration.stringDiscovery)
            .setBroadcastAddress(configuration.broadcastAddress).setAcceptCerts(configuration.acceptAllCerts).build();
        beaconServer.start();
      }
    } catch (IOException e) {
      logger.logException(e);
    } catch (UnrecoverableKeyException e) {
      logger.warn(e.getMessage());
    }
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
    return ServiceStatus.RUNNING;
  }

  @Override
  public void kill() {
    if (beaconServer != null) {
      beaconServer.stop();
      beaconServer = null;
    }
  }

  @Override
  public DataAddress getDataAddress() {
    return dataAddress;
  }

  @Override
  public void setDataAddress(DataAddress dataAddress) {
    this.dataAddress = dataAddress;
  }

  @Override
  public void setConfiguration(ServiceConfig configuration) {
    this.configuration = (BeaconServiceConfig) configuration;
  }

  @Override
  public JSONObject getDescriptionJson() {
    Gson gson = new GsonBuilder().create();
    return new JSONObject(gson.toJsonTree(configuration).getAsString());
  }

  @Override
  public void close() throws Exception {
    if (beaconServer != null) {
      beaconServer.close();
    }
  }

  @Override
  public ServiceConfig getConfiguration() {
    return configuration;
  }

}
