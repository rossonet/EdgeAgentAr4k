package org.ar4k.agent.core.interfaces;

import org.ar4k.agent.core.RpcConversation;

public interface IBeaconClientScadaWrapper {

	IBeaconClient getBeaconClient();

	/**
	 * @return the host
	 */
	String getHost();

	/**
	 * @param host the host to set
	 */
	void setHost(String host);

	/**
	 * @return the port
	 */
	Integer getPort();

	/**
	 * @param port the port to set
	 */
	void setPort(Integer port);

	/**
	 * @return the discoveryPort
	 */
	Integer getDiscoveryPort();

	/**
	 * @param discoveryPort the discoveryPort to set
	 */
	void setDiscoveryPort(Integer discoveryPort);

	/**
	 * @return the discoveryFilter
	 */
	String getDiscoveryFilter();

	/**
	 * @param discoveryFilter the discoveryFilter to set
	 */
	void setDiscoveryFilter(String discoveryFilter);

	/**
	 * @return the rpcConversation
	 */
	RpcConversation getRpcConversation();

	/**
	 * @param rpcConversation the rpcConversation to set
	 */
	void setRpcConversation(RpcConversation rpcConversation);

	/**
	 * @return the aliasBeaconClientInKeystore
	 */
	String getAliasBeaconClientInKeystore();

	/**
	 * @param aliasBeaconClientInKeystore the aliasBeaconClientInKeystore to set
	 */
	void setAliasBeaconClientInKeystore(String aliasBeaconClientInKeystore);

	/**
	 * @return the certFile
	 */
	String getCertFile();

	/**
	 * @param certFile the certFile to set
	 */
	void setCertFile(String certFile);

	/**
	 * @return the certChainFile
	 */
	String getCertChainFile();

	/**
	 * @param certChainFile the certChainFile to set
	 */
	void setCertChainFile(String certChainFile);

	/**
	 * @return the privateFile
	 */
	String getPrivateFile();

	/**
	 * @param privateFile the privateFile to set
	 */
	void setPrivateFile(String privateFile);

	/**
	 * @return the beaconCaChainPem
	 */
	String getBeaconCaChainPem();

	/**
	 * @param beaconCaChainPem the beaconCaChainPem to set
	 */
	void setBeaconCaChainPem(String beaconCaChainPem);

	String getCompany();

	void setCompany(String company);

	String getContext();

	void setContext(String context);

	String getAgentUniqueName();

	boolean isFoundBy(String filter);

	@Override
	String toString();

	String getId();

	String getStatus();

	String getRegistrationStatus();

	Integer getAgentsCount();

	void setHomunculusClient(boolean b);

	boolean isHomunculusClient();

}