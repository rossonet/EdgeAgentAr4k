package org.ar4k.agent.web.main;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.ar4k.agent.core.EdgeAgentCore;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http2.beacon.IBeaconClient;
import org.ar4k.agent.tunnels.http2.beacon.IBeaconClientScadaWrapper;

public class BeaconClientWrapper implements IBeaconClientScadaWrapper {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(BeaconClientWrapper.class);

	@NotNull
	@NotEmpty
	private final String id = UUID.randomUUID().toString().replace("-", ".");
	private String host;
	private Integer port = 0;
	private Integer discoveryPort;
	private String discoveryFilter;
	private RpcConversation rpcConversation;
	private String aliasBeaconClientInKeystore;
	private String certFile;
	private String certChainFile;
	private String privateFile;
	private String beaconCaChainPem;
	private String company;
	private String context;

	private boolean homunculusClient = false;

	private IBeaconClient beaconClient;

	public BeaconClientWrapper() {
		tryConnection();
	}

	@Override
	public Integer getAgentsCount() {
		return beaconClient != null ? beaconClient.listAgentsConnectedToBeacon().size() : 0;

	}

	@Override
	public String getAgentUniqueName() {
		return beaconClient.getAgentUniqueName();
	}

	/**
	 * @return the aliasBeaconClientInKeystore
	 */
	@Override
	public String getAliasBeaconClientInKeystore() {
		return aliasBeaconClientInKeystore;
	}

	/**
	 * @return the beaconCaChainPem
	 */
	@Override
	public String getBeaconCaChainPem() {
		return beaconCaChainPem;
	}

	@Override
	public IBeaconClient getBeaconClient() {
		if (homunculusClient && beaconClient == null) {
			tryConnection();
		}
		return beaconClient;
	}

	/**
	 * @return the certChainFile
	 */
	@Override
	public String getCertChainFile() {
		return certChainFile;
	}

	/**
	 * @return the certFile
	 */
	@Override
	public String getCertFile() {
		return certFile;
	}

	@Override
	public String getCompany() {
		return company;
	}

	@Override
	public String getContext() {
		return context;
	}

	/**
	 * @return the discoveryFilter
	 */
	@Override
	public String getDiscoveryFilter() {
		return discoveryFilter;
	}

	/**
	 * @return the discoveryPort
	 */
	@Override
	public Integer getDiscoveryPort() {
		return discoveryPort;
	}

	/**
	 * @return the host
	 */
	@Override
	public String getHost() {
		return host;
	}

	@Override
	public String getId() {
		return id;
	}

	/**
	 * @return the port
	 */
	@Override
	public Integer getPort() {
		return port;
	}

	/**
	 * @return the privateFile
	 */
	@Override
	public String getPrivateFile() {
		return privateFile;
	}

	@Override
	public String getRegistrationStatus() {
		return beaconClient != null ? beaconClient.getRegistrationStatus().toString() : "DISCONNECTED";

	}

	/**
	 * @return the rpcConversation
	 */
	@Override
	public RpcConversation getRpcConversation() {
		return rpcConversation;
	}

	@Override
	public String getStatus() {
		return beaconClient != null ? beaconClient.getStateConnection().toString() : "DISCONNECTED";

	}

	@Override
	public boolean isFoundBy(String filter) {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * @param aliasBeaconClientInKeystore the aliasBeaconClientInKeystore to set
	 */
	@Override
	public void setAliasBeaconClientInKeystore(String aliasBeaconClientInKeystore) {
		this.aliasBeaconClientInKeystore = aliasBeaconClientInKeystore;
		tryConnection();
	}

	/**
	 * @param beaconCaChainPem the beaconCaChainPem to set
	 */
	@Override
	public void setBeaconCaChainPem(String beaconCaChainPem) {
		this.beaconCaChainPem = beaconCaChainPem;
		tryConnection();
	}

	/**
	 * @param certChainFile the certChainFile to set
	 */
	@Override
	public void setCertChainFile(String certChainFile) {
		this.certChainFile = certChainFile;
		tryConnection();
	}

	/**
	 * @param certFile the certFile to set
	 */
	@Override
	public void setCertFile(String certFile) {
		this.certFile = certFile;
		tryConnection();
	}

	@Override
	public void setCompany(String company) {
		this.company = company;
	}

	@Override
	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * @param discoveryFilter the discoveryFilter to set
	 */
	@Override
	public void setDiscoveryFilter(String discoveryFilter) {
		this.discoveryFilter = discoveryFilter;
		tryConnection();
	}

	/**
	 * @param discoveryPort the discoveryPort to set
	 */
	@Override
	public void setDiscoveryPort(Integer discoveryPort) {
		this.discoveryPort = discoveryPort;
		tryConnection();
	}

	/**
	 * @param host the host to set
	 */
	@Override
	public void setHost(String host) {
		this.host = host;
		tryConnection();
	}

	/**
	 * @param port the port to set
	 */
	@Override
	public void setPort(Integer port) {
		this.port = port;
		tryConnection();
	}

	/**
	 * @param privateFile the privateFile to set
	 */
	@Override
	public void setPrivateFile(String privateFile) {
		this.privateFile = privateFile;
		tryConnection();
	}

	/**
	 * @param rpcConversation the rpcConversation to set
	 */
	@Override
	public void setRpcConversation(RpcConversation rpcConversation) {
		this.rpcConversation = rpcConversation;
		tryConnection();
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("BeaconClientWrapper [");
		if (host != null)
			builder.append("host=").append(host).append(", ");
		if (port != null)
			builder.append("port=").append(port).append(", ");
		if (discoveryPort != null)
			builder.append("discoveryPort=").append(discoveryPort).append(", ");
		if (discoveryFilter != null)
			builder.append("discoveryFilter=").append(discoveryFilter).append(", ");
		if (rpcConversation != null)
			builder.append("rpcConversation=").append(rpcConversation).append(", ");
		if (aliasBeaconClientInKeystore != null)
			builder.append("aliasBeaconClientInKeystore=").append(aliasBeaconClientInKeystore).append(", ");
		if (certFile != null)
			builder.append("certFile=").append(certFile).append(", ");
		if (certChainFile != null)
			builder.append("certChainFile=").append(certChainFile).append(", ");
		if (privateFile != null)
			builder.append("privateFile=").append(privateFile).append(", ");
		if (beaconCaChainPem != null)
			builder.append("beaconCaChainPem=").append(beaconCaChainPem).append(", ");
		if (company != null)
			builder.append("company=").append(company).append(", ");
		if (context != null)
			builder.append("context=").append(context).append(", ");
		if (beaconClient != null)
			builder.append("beaconClient=").append(beaconClient);
		builder.append("]");
		return builder.toString();
	}

	private void tryConnection() {
		if (!homunculusClient) {
			if (beaconClient != null) {
				try {
					beaconClient.shutdown();
				} catch (final InterruptedException e) {
					logger.logException(e);
				}
			}
			if (host != null && !host.isEmpty() && port != 0) {
				if (EdgeAgentCore.getApplicationContextStatic() != null
						&& EdgeAgentCore.getApplicationContextStatic().getBean(EdgeAgentCore.class) != null) {
					this.beaconClient = EdgeAgentCore.getApplicationContextStatic().getBean(EdgeAgentCore.class)
							.connectToBeaconService("http://" + host + ":" + port, beaconCaChainPem, discoveryPort,
									discoveryFilter, false);
				} else {
					logger.warn("no Homunculus found");
				}
			}
		} else {
			if (EdgeAgentCore.getApplicationContextStatic().getBean(EdgeAgentCore.class) != null) {
				this.beaconClient = EdgeAgentCore.getApplicationContextStatic().getBean(EdgeAgentCore.class)
						.getBeaconClient();
			}
		}
	}

	@Override
	public boolean isHomunculusClient() {
		return homunculusClient;
	}

	@Override
	public void setHomunculusClient(boolean homunculusClient) {
		this.homunculusClient = homunculusClient;
	}

}
