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
package org.ar4k.agent.tunnels.http.grpc;

import java.io.IOException;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.AbstractAr4kService;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio Beacon per telemetria
 *
 */
public class BeaconService extends AbstractAr4kService {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(Anima.class.toString());

  // iniettata vedi set/get
  private BeaconServiceConfig configuration = null;

  // iniettata vedi set/get
  private Anima anima = null;

  private BeaconServer beaconServer = null;

  @Override
  public synchronized void loop() {
    if (beaconServer == null) {
      init();
    } else {
      if (beaconServer.isStopped()) {
        beaconServer.stop();
        beaconServer = null;
        init();
      }
    }
  }

  @Override
  public BeaconServiceConfig getConfiguration() {
    return configuration;
  }

  @Override
  public void setConfiguration(AbstractServiceConfig configuration) {
    super.setConfiguration(configuration);
    this.configuration = ((BeaconServiceConfig) configuration);
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
  public void init() {
    try {
      beaconServer = new BeaconServer(anima, configuration.port, configuration.discoveryPort,
          configuration.broadcastAddress, configuration.acceptAllCerts, configuration.stringDiscovery,
          configuration.certChainFile, configuration.certFile, configuration.privateKeyFile,
          configuration.aliasBeaconServerInKeystore, configuration.aliasBeaconServerRequestCertInKeystore,
          configuration.caChainPem);
      beaconServer.start();
    } catch (IOException e) {
      logger.logException(e);
    }
  }

  @Override
  public void setConfiguration(ConfigSeed configuration) {
    this.configuration = (BeaconServiceConfig) configuration;
  }

  @Override
  public String getStatusString() {
    return beaconServer != null ? beaconServer.getStatus() : "NaN";
  }

  @Override
  public JsonElement getStatusJson() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJsonTree(getStatusString());
  }

  @Override
  public void close() throws IOException {
    if (beaconServer != null)
      beaconServer.stop();
  }

  @Override
  public void stop() {
    // TODO Auto-generated method stub

  }

}
