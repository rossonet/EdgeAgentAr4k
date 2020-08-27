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
package org.ar4k.agent.opcua.client;

import java.util.Collection;
import java.util.HashSet;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.EdgeComponent;
import org.ar4k.agent.opcua.CryptoModeValidator;
import org.ar4k.agent.opcua.Enumerator.CryptoMode;
import org.ar4k.agent.opcua.Enumerator.SecurityMode;
import org.ar4k.agent.opcua.SecurityModeValidator;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione client OPC UA collegata all'agente.
 */
public class OpcUaClientConfig extends AbstractServiceConfig {

  private static final long serialVersionUID = -864167279161787378L;

  @Parameter(names = "--serverUrl", description = "server url in format opc.tcp://localhost:53530/Server")
  public String serverUrl = "opc.tcp://localhost:53530/Server";

  @Parameter(names = "--forceHostName", description = "force to host to connect after the discovery")
  public Boolean forceHostName = true;

  @Parameter(names = "--securityMode", description = "security mode for the connection", validateWith = SecurityModeValidator.class)
  public SecurityMode securityMode = SecurityMode.none;

  @Parameter(names = "--cryptoMode", description = "crypto mode for the connection", validateWith = CryptoModeValidator.class)
  public CryptoMode cryptoMode = CryptoMode.none;

  @Parameter(names = "--username", description = "username for the connection")
  public String username = null;

  @Parameter(names = "--password", description = "password for the connection")
  public String password = null;

  @Parameter(names = "--aliasCertificateInKeystore", description = "alias for certificate in keystore")
  public String aliasCertificateInKeystore = null;

  @Parameter(names = "--subscriptions", description = "List of node to subscribe", variableArity = true)
  Collection<OpcUaClientNode> subscriptions = new HashSet<>();

  @Override
  public EdgeComponent instantiate() {
    OpcUaClientService ss = new OpcUaClientService();
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
