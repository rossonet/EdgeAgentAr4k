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

import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.industrial.Enumerator.CryptoMode;
import org.ar4k.agent.industrial.Enumerator.SecurityMode;
import org.ar4k.agent.industrial.validators.AuthModeValidator;
import org.ar4k.agent.industrial.validators.CryptoModeValidator;
import org.ar4k.agent.industrial.validators.SecurityModeValidator;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione client OPC UA.
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

	@Parameter(names = "--acknowledgeTimeout", description = "the timeout, in milliseconds, to wait for an Acknowledge message in response to the client's Hello message.")
	private String acknowledgeTimeout = "5000";

	@Parameter(names = "--channelLifetime", description = "the secure channel lifetime to request, in milliseconds")
	private String channelLifetime = "60000";

	@Parameter(names = "--connectTimeout", description = "the timeout, in milliseconds, when opening a socket connection to a remote host")
	private String connectTimeout = "5000";

	@Parameter(names = "--keepAliveTimeout", description = "the timeout for the keep alive message in milliseconds")
	private String keepAliveTimeout = "5000";

	@Parameter(names = "--maxChunkCount", description = "the maximum number of chunks in any request Message. A value of zero indicates that the server has no limit")
	private String maxChunkCount = "0";

	@Parameter(names = "--maxMessageSize", description = "the maximum size for any request message. The maximum size for any request Message. The client shall abort the message with a Bad_RequestTooLarge StatusCode if a request message exceeds this value. The message size is calculated using the unencrypted message body. A value of zero indicates that the Server has no limit")
	private String maxMessageSize = "0";

	@Parameter(names = "--requestTimeout", description = "the custom request timeout to use in milliseconds")
	private String requestTimeout = "60000";

	@Parameter(names = "--maxChunkSize", description = "the max size for a single chunk trasmitted from the server. Must be greater than or equal to 8196")
	private String maxChunkSize = "8196";

	@Parameter(names = "--sessionTimeout", description = "the timeout for the session in milliseconds")
	private String sessionTimeout = "120000";

	@Parameter(names = "--authMode", description = "authentication method used to connect to the server. Must be none, password or certificate", validateWith = AuthModeValidator.class)
	private String authMode = "none";

	@Parameter(names = "--username", description = "username for the connection")
	public String username = null;

	@Parameter(names = "--password", description = "password for the connection")
	public String password = null;

	@Parameter(names = "--aliasCryptoCertificateInKeystore", description = "alias for certificate in keystore for the crypto channel")
	public String aliasCryptoCertificateInKeystore = null;

	@Parameter(names = "--aliasAuthCertificateInKeystore", description = "alias for certificate in keystore for authentication")
	public String aliasAuthCertificateInKeystore = null;

	@Parameter(names = "--subscriptions", description = "List of node to subscribe", variableArity = true)
	public List<OpcUaClientNode> subscriptions = new ArrayList<>();

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OpcUaClientConfig [");
		if (serverUrl != null) {
			builder.append("serverUrl=");
			builder.append(serverUrl);
			builder.append(", ");
		}
		if (forceHostName != null) {
			builder.append("forceHostName=");
			builder.append(forceHostName);
			builder.append(", ");
		}
		if (securityMode != null) {
			builder.append("securityMode=");
			builder.append(securityMode);
			builder.append(", ");
		}
		if (cryptoMode != null) {
			builder.append("cryptoMode=");
			builder.append(cryptoMode);
			builder.append(", ");
		}
		if (acknowledgeTimeout != null) {
			builder.append("acknowledgeTimeout=");
			builder.append(acknowledgeTimeout);
			builder.append(", ");
		}
		if (channelLifetime != null) {
			builder.append("channelLifetime=");
			builder.append(channelLifetime);
			builder.append(", ");
		}
		if (connectTimeout != null) {
			builder.append("connectTimeout=");
			builder.append(connectTimeout);
			builder.append(", ");
		}
		if (keepAliveTimeout != null) {
			builder.append("keepAliveTimeout=");
			builder.append(keepAliveTimeout);
			builder.append(", ");
		}
		if (maxChunkCount != null) {
			builder.append("maxChunkCount=");
			builder.append(maxChunkCount);
			builder.append(", ");
		}
		if (maxMessageSize != null) {
			builder.append("maxMessageSize=");
			builder.append(maxMessageSize);
			builder.append(", ");
		}
		if (requestTimeout != null) {
			builder.append("requestTimeout=");
			builder.append(requestTimeout);
			builder.append(", ");
		}
		if (maxChunkSize != null) {
			builder.append("maxChunkSize=");
			builder.append(maxChunkSize);
			builder.append(", ");
		}
		if (sessionTimeout != null) {
			builder.append("sessionTimeout=");
			builder.append(sessionTimeout);
			builder.append(", ");
		}
		if (authMode != null) {
			builder.append("authMode=");
			builder.append(authMode);
			builder.append(", ");
		}
		if (username != null) {
			builder.append("username=");
			builder.append(username);
			builder.append(", ");
		}
		if (password != null) {
			builder.append("password=");
			builder.append(password);
			builder.append(", ");
		}
		if (aliasCryptoCertificateInKeystore != null) {
			builder.append("aliasCryptoCertificateInKeystore=");
			builder.append(aliasCryptoCertificateInKeystore);
			builder.append(", ");
		}
		if (aliasAuthCertificateInKeystore != null) {
			builder.append("aliasAuthCertificateInKeystore=");
			builder.append(aliasAuthCertificateInKeystore);
			builder.append(", ");
		}
		if (subscriptions != null) {
			builder.append("subscriptions=");
			builder.append(subscriptions);
		}
		builder.append("]");
		return builder.toString();
	}
}
