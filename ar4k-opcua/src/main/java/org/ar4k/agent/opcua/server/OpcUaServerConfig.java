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
package org.ar4k.agent.opcua.server;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.opcua.CryptoModeValidator;
import org.ar4k.agent.opcua.Enumerator.CryptoMode;
import org.ar4k.agent.opcua.Enumerator.SecurityMode;
import org.ar4k.agent.opcua.SecurityModeValidator;
import org.ar4k.agent.opcua.client.OpcUaClientConfigJsonAdapter;

import com.beust.jcommander.Parameter;
import com.google.gson.TypeAdapter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione client OPC UA collegata all'agente.
 */
public class OpcUaServerConfig extends AbstractServiceConfig {

  private static final long serialVersionUID = -864167279161787378L;

  @Parameter(names = "--serverPort", description = "port for the OPCUA server binary")
  public Integer serverPort = 45341;

  @Parameter(names = "--serverPortHttps", description = "port for the OPCUA server HTTPS")
  public Integer serverPortHttps = 8443;

  @Parameter(names = "--serverPath", description = "server path")
  public String serverPath = "/agent/discovery";

  @Parameter(names = "--bindAddress", description = "bind address")
  public String bindAddress = "0.0.0.0";

  @Parameter(names = "--serverText", description = "server description text")
  public String serverText = "Ar4k Agent OPC UA Server";

  @Parameter(names = "--productUri", description = "product URI")
  public String productUri = "urn:ar4k:agent:opcua-server";

  @Parameter(names = "--manufacturerName", description = "manufacturer name")
  public String manufacturerName = "Agent xxx";

  @Parameter(names = "--productName", description = "product name")
  public String productName = "Ar4k Agent";

  @Parameter(names = "--securityMode", description = "security mode for the connection", validateWith = SecurityModeValidator.class)
  public SecurityMode securityMode = SecurityMode.none;

  @Parameter(names = "--cryptoMode", description = "crypto mode for the connection", validateWith = CryptoModeValidator.class)
  public CryptoMode cryptoMode = CryptoMode.none;

  @Parameter(names = "--aliasCertificateInKeystore", description = "alias for certificate in keystore")
  public String aliasCertificateInKeystore = null;

  @Parameter(names = "--scope", description = "scope in data address to expose")
  public String scope = "ar4k-ai";

  @Override
  public OpcUaServerService instantiate() {
    OpcUaServerService ss = new OpcUaServerService();
    ss.setConfiguration(this);
    return ss;
  }

  @Override
  public int getPriority() {
    return 8;
  }

  @Override
  public TypeAdapter<? extends ConfigSeed> getJsonTypeAdapter() {
    return new OpcUaClientConfigJsonAdapter();
  }

  @Override
  public boolean isSpringBean() {
    return false;
  }
}
