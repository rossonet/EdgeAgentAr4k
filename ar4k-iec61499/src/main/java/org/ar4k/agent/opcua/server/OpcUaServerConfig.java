package org.ar4k.agent.opcua.server;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Ar4kComponent;
import org.ar4k.agent.opcua.CryptoModeValidator;
import org.ar4k.agent.opcua.Enumerator.CryptoMode;
import org.ar4k.agent.opcua.Enumerator.SecurityMode;
import org.ar4k.agent.opcua.SecurityModeValidator;

import com.beust.jcommander.Parameter;

public class OpcUaServerConfig extends AbstractServiceConfig {

	private static final long serialVersionUID = -864167279161787378L;

	@Parameter(names = "--serverPort", description = "port for the OPCUA server binary. 0 disabled")
	public Integer serverPort = 45341;

	@Parameter(names = "--serverPortHttps", description = "port for the OPCUA server HTTPS. 0 disabled")
	public Integer serverPortHttps = 8443;

	@Parameter(names = "--serverPath", description = "server path")
	public String serverPath = "/agent";

	@Parameter(names = "--bindAddress", description = "bind address")
	public String bindAddress = "0.0.0.0";

	@Parameter(names = "--serverText", description = "server description text")
	public String serverText = "Ar4k Agent OPC UA Server";

	@Parameter(names = "--productUri", description = "product URI")
	public String productUri = "urn:ar4k:agent:opcua-server";

	@Parameter(names = "--namespaceUri", description = "namespace URI")
	public String namespaceUri = "urn:ar4k:agent:namespace";

	@Parameter(names = "--manufacturerName", description = "manufacturer name")
	public String manufacturerName = "Agent OPC";

	@Parameter(names = "--productName", description = "product name")
	public String productName = "Ar4k Edge Agent";

	@Parameter(names = "--securityMode", description = "security mode for the connection", validateWith = SecurityModeValidator.class)
	public SecurityMode securityMode = SecurityMode.none;

	@Parameter(names = "--cryptoMode", description = "crypto mode for the connection", validateWith = CryptoModeValidator.class)
	public CryptoMode cryptoMode = CryptoMode.none;

	@Parameter(names = "--aliasCertificateInKeystore", description = "alias for certificate in keystore")
	public String aliasCertificateInKeystore = "master";

	@Parameter(names = "--applicationName", description = "application name")
	public String applicationName = "Agent Rossonet";

	@Parameter(names = "--baseFolderName", description = "base folder name for the opc objects")
	public String baseFolderName = Anima.getApplicationContext().getBean(Anima.class).getAgentUniqueName();

	@Override
	public Ar4kComponent instantiate() {
		final OpcUaServerService ss = new OpcUaServerService();
		ss.setConfiguration(this);
		return ss;
	}

	@Override
	public int getPriority() {
		return 1100;
	}

	@Override
	public boolean isSpringBean() {
		return false;
	}
}
