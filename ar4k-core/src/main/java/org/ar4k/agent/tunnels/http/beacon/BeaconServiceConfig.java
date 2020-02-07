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
package org.ar4k.agent.tunnels.http.beacon;

import java.util.UUID;

import org.ar4k.agent.config.AbstractServiceConfig;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione Beacon Server.
 */
public class BeaconServiceConfig extends AbstractServiceConfig {

  private static final long serialVersionUID = -8036646566388899515L;

  @Parameter(names = "--port", description = "the port for run the Beacon endpoint")
  public int port = 6599;
  @Parameter(names = "--discoveryPort", description = "the port for the UDP flash -discovery-. if 0 then then flash will be stopped")
  public int discoveryPort = 33666;
  @Parameter(names = "--broadcastAddress", description = "the broadcast address for the flash")
  public String broadcastAddress = "255.255.255.255";
  @Parameter(names = "--acceptAllCerts", description = "in registration phase accept all cetificates or use the auth flow")
  public boolean acceptAllCerts = true;
  @Parameter(names = "--filterBlackListCertRegister", description = "regex to filter the CN of client. If it matches, the cert sign is disabled", required = false)
  public String filterBlackListCertRegister = null;
  @Parameter(names = "--stringDiscovery", description = "the message in the discovery flash")
  public String stringDiscovery = "AR4K-BEACON-" + UUID.randomUUID().toString();
  @Parameter(names = "--certChainFile", description = "file for storing the ca for the server")
  public String certChainFile = "./tmp/beacon-server-" + UUID.randomUUID().toString() + "-ca.pem";
  @Parameter(names = "--certFile", description = "file for storing the cert for the server")
  public String certFile = "./tmp/beacon-server-" + UUID.randomUUID().toString() + ".pem";
  @Parameter(names = "--privateKeyFile", description = "file for storing the key for the server")
  public String privateKeyFile = "./tmp/beacon-server-" + UUID.randomUUID().toString() + ".key";
  @Parameter(names = "--aliasBeaconServerInKeystore", description = "the alias in anima keystore for the cert/key")
  public String aliasBeaconServerInKeystore = "beacon-server";
  @Parameter(names = "--caChainPem", description = "the ca chain for the server in pem format")
  public String caChainPem = null;
  @Parameter(names = "--filterActiveCommand", description = "regex to filter the CN of client. If it matches, the action are enabled", required = false)
  public String filterActiveCommand = ".*";

  @Override
  public BeaconService instantiate() {
    BeaconService ss = new BeaconService();
    ss.setConfiguration(this);
    return ss;
  }

  @Override
  public String toString() {
    return "BeaconServiceConfig [port=" + port + ", discoveryPort=" + discoveryPort + ", broadcastAddress="
        + broadcastAddress + ", acceptAllCerts=" + acceptAllCerts + ", filterBlackListCertRegister="
        + filterBlackListCertRegister + ", stringDiscovery=" + stringDiscovery + ", certChainFile=" + certChainFile
        + ", certFile=" + certFile + ", privateKeyFile=" + privateKeyFile + ", aliasBeaconServerInKeystore="
        + aliasBeaconServerInKeystore + ", caChainPem=" + caChainPem + ", filterActiveCommand=" + filterActiveCommand
        + "]";
  }

  @Override
  public boolean isSpringBean() {
    return false;
  }

}
