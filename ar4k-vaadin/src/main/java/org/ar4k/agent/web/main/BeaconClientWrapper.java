package org.ar4k.agent.web.main;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.core.interfaces.IBeaconClient;
import org.ar4k.agent.core.interfaces.IBeaconClientScadaWrapper;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

public class BeaconClientWrapper implements IBeaconClientScadaWrapper {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconClientWrapper.class.toString());

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
	// private List<String> tags = new ArrayList<String>();

	private IBeaconClient beaconClient;

	public BeaconClientWrapper() {
		tryConnection();
	}

	private void tryConnection() {
		if (beaconClient != null) {
			try {
				beaconClient.shutdown();
			} catch (final InterruptedException e) {
				logger.logException(e);
			}
		}
		if (host != null && !host.isEmpty() && port != 0) {
			if (Homunculus.getApplicationContext() != null
					&& Homunculus.getApplicationContext().getBean(Homunculus.class) != null) {
				this.beaconClient = Homunculus.getApplicationContext().getBean(Homunculus.class).connectToBeaconService(
						"http://" + host + ":" + port, beaconCaChainPem, discoveryPort, discoveryFilter, false);
			} else {
				logger.warn("no Homunculus found");
			}
		}
	}

	@Override
	public IBeaconClient getBeaconClient() {
		return beaconClient;
	}

	/**
	 * @return the host
	 */
	@Override
	public String getHost() {
		return host;
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
	 * @return the port
	 */
	@Override
	public Integer getPort() {
		return port;
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
	 * @return the discoveryPort
	 */
	@Override
	public Integer getDiscoveryPort() {
		return discoveryPort;
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
	 * @return the discoveryFilter
	 */
	@Override
	public String getDiscoveryFilter() {
		return discoveryFilter;
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
	 * @return the rpcConversation
	 */
	@Override
	public RpcConversation getRpcConversation() {
		return rpcConversation;
	}

	/**
	 * @param rpcConversation the rpcConversation to set
	 */
	@Override
	public void setRpcConversation(RpcConversation rpcConversation) {
		this.rpcConversation = rpcConversation;
		tryConnection();
	}

	/**
	 * @return the aliasBeaconClientInKeystore
	 */
	@Override
	public String getAliasBeaconClientInKeystore() {
		return aliasBeaconClientInKeystore;
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
	 * @return the certFile
	 */
	@Override
	public String getCertFile() {
		return certFile;
	}

	/**
	 * @param certFile the certFile to set
	 */
	@Override
	public void setCertFile(String certFile) {
		this.certFile = certFile;
		tryConnection();
	}

	/**
	 * @return the certChainFile
	 */
	@Override
	public String getCertChainFile() {
		return certChainFile;
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
	 * @return the privateFile
	 */
	@Override
	public String getPrivateFile() {
		return privateFile;
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
	 * @return the beaconCaChainPem
	 */
	@Override
	public String getBeaconCaChainPem() {
		return beaconCaChainPem;
	}

	/**
	 * @param beaconCaChainPem the beaconCaChainPem to set
	 */
	@Override
	public void setBeaconCaChainPem(String beaconCaChainPem) {
		this.beaconCaChainPem = beaconCaChainPem;
		tryConnection();
	}

	@Override
	public String getCompany() {
		return company;
	}

	@Override
	public void setCompany(String company) {
		this.company = company;
	}

	@Override
	public String getContext() {
		return context;
	}

	@Override
	public void setContext(String context) {
		this.context = context;
	}

	@Override
	public String getAgentUniqueName() {
		return beaconClient.getAgentUniqueName();
	}

	@Override
	public boolean isFoundBy(String filter) {
		// TODO Auto-generated method stub
		return true;
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

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getStatus() {
		return beaconClient != null ? beaconClient.getStateConnection().toString() : "DISCONNECTED";

	}

	@Override
	public String getRegistrationStatus() {
		return beaconClient != null ? beaconClient.getRegistrationStatus().toString() : "DISCONNECTED";

	}

	@Override
	public Integer getAgentsCount() {
		return beaconClient != null ? beaconClient.listAgentsConnectedToBeacon().size() : 0;

	}

}
