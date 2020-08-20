package org.ar4k.agent.web.scada;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.IBeaconClient;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

public class BeaconClientWrapper {

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
			this.beaconClient = Anima.getApplicationContext().getBean(Anima.class).connectToBeaconService(
					"http://" + host + ":" + port, beaconCaChainPem, discoveryPort, discoveryFilter, false);
		}
	}

	public IBeaconClient getBeaconClient() {
		return beaconClient;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
		tryConnection();
	}

	/**
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
		tryConnection();
	}

	/**
	 * @return the discoveryPort
	 */
	public Integer getDiscoveryPort() {
		return discoveryPort;
	}

	/**
	 * @param discoveryPort the discoveryPort to set
	 */
	public void setDiscoveryPort(Integer discoveryPort) {
		this.discoveryPort = discoveryPort;
		tryConnection();
	}

	/**
	 * @return the discoveryFilter
	 */
	public String getDiscoveryFilter() {
		return discoveryFilter;
	}

	/**
	 * @param discoveryFilter the discoveryFilter to set
	 */
	public void setDiscoveryFilter(String discoveryFilter) {
		this.discoveryFilter = discoveryFilter;
		tryConnection();
	}

	/**
	 * @return the rpcConversation
	 */
	public RpcConversation getRpcConversation() {
		return rpcConversation;
	}

	/**
	 * @param rpcConversation the rpcConversation to set
	 */
	public void setRpcConversation(RpcConversation rpcConversation) {
		this.rpcConversation = rpcConversation;
		tryConnection();
	}

	/**
	 * @return the aliasBeaconClientInKeystore
	 */
	public String getAliasBeaconClientInKeystore() {
		return aliasBeaconClientInKeystore;
	}

	/**
	 * @param aliasBeaconClientInKeystore the aliasBeaconClientInKeystore to set
	 */
	public void setAliasBeaconClientInKeystore(String aliasBeaconClientInKeystore) {
		this.aliasBeaconClientInKeystore = aliasBeaconClientInKeystore;
		tryConnection();
	}

	/**
	 * @return the certFile
	 */
	public String getCertFile() {
		return certFile;
	}

	/**
	 * @param certFile the certFile to set
	 */
	public void setCertFile(String certFile) {
		this.certFile = certFile;
		tryConnection();
	}

	/**
	 * @return the certChainFile
	 */
	public String getCertChainFile() {
		return certChainFile;
	}

	/**
	 * @param certChainFile the certChainFile to set
	 */
	public void setCertChainFile(String certChainFile) {
		this.certChainFile = certChainFile;
		tryConnection();
	}

	/**
	 * @return the privateFile
	 */
	public String getPrivateFile() {
		return privateFile;
	}

	/**
	 * @param privateFile the privateFile to set
	 */
	public void setPrivateFile(String privateFile) {
		this.privateFile = privateFile;
		tryConnection();
	}

	/**
	 * @return the beaconCaChainPem
	 */
	public String getBeaconCaChainPem() {
		return beaconCaChainPem;
	}

	/**
	 * @param beaconCaChainPem the beaconCaChainPem to set
	 */
	public void setBeaconCaChainPem(String beaconCaChainPem) {
		this.beaconCaChainPem = beaconCaChainPem;
		tryConnection();
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getAgentUniqueName() {
		return beaconClient.getAgentUniqueName();
	}

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

	public String getId() {
		return id;
	}

	public String getStatus() {
		return beaconClient != null ? beaconClient.getStateConnection().toString() : "DISCONNECTED";

	}

	public String getRegistrationStatus() {
		return beaconClient != null ? beaconClient.getRegistrationStatus().toString() : "DISCONNECTED";

	}

	public Integer getAgentsCount() {
		return beaconClient != null ? beaconClient.listAgentsConnectedToBeacon().size() : 0;

	}

}
