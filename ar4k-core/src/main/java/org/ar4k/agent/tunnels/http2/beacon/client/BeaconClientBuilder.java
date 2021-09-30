package org.ar4k.agent.tunnels.http2.beacon.client;

import java.security.UnrecoverableKeyException;

import org.ar4k.agent.core.EdgeAgentCore;
import org.ar4k.agent.core.RpcConversation;

public class BeaconClientBuilder {
	private EdgeAgentCore edgeAgentCore = null;
	private RpcConversation rpcConversation = null;
	private String host = null;
	private int port = 0;
	private int discoveryPort = 0;
	private String discoveryFilter = null;
	private String uniqueName = null;
	private String certChainFile = null;
	private String certFile = null;
	private String privateFile = null;
	private String aliasBeaconClientInKeystore = null;
	private String beaconCaChainPem = null;

	public BeaconClient build() throws UnrecoverableKeyException {
		return new BeaconClient(edgeAgentCore, rpcConversation, host, port, discoveryPort, discoveryFilter, uniqueName,
				certChainFile, certFile, privateFile, aliasBeaconClientInKeystore, beaconCaChainPem);
	}

	public String getAliasBeaconClientInKeystore() {
		return aliasBeaconClientInKeystore;
	}

	public String getCertChainFile() {
		return certChainFile;
	}

	public String getCertFile() {
		return certFile;
	}

	public String getDiscoveryFilter() {
		return discoveryFilter;
	}

	public int getDiscoveryPort() {
		return discoveryPort;
	}

	public EdgeAgentCore getHomunculus() {
		return edgeAgentCore;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getPrivateFile() {
		return privateFile;
	}

	public RpcConversation getRpcConversation() {
		return rpcConversation;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public BeaconClientBuilder setAliasBeaconClientInKeystore(String aliasBeaconClientInKeystore) {
		this.aliasBeaconClientInKeystore = aliasBeaconClientInKeystore;
		return this;
	}

	public BeaconClientBuilder setBeaconCaChainPem(String beaconCaChainPem) {
		this.beaconCaChainPem = beaconCaChainPem;
		return this;
	}

	public BeaconClientBuilder setCertChainFile(String certChainFile) {
		this.certChainFile = certChainFile;
		return this;
	}

	public BeaconClientBuilder setCertFile(String certFile) {
		this.certFile = certFile;
		return this;
	}

	public BeaconClientBuilder setDiscoveryFilter(String discoveryFilter) {
		this.discoveryFilter = discoveryFilter;
		return this;
	}

	public BeaconClientBuilder setDiscoveryPort(int discoveryPort) {
		this.discoveryPort = discoveryPort;
		return this;
	}

	public BeaconClientBuilder setHomunculus(EdgeAgentCore edgeAgentCore) {
		this.edgeAgentCore = edgeAgentCore;
		return this;
	}

	public BeaconClientBuilder setHost(String host) {
		this.host = host;
		return this;
	}

	public BeaconClientBuilder setPort(int port) {
		this.port = port;
		return this;
	}

	public BeaconClientBuilder setPrivateFile(String privateFile) {
		this.privateFile = privateFile;
		return this;
	}

	public BeaconClientBuilder setRpcConversation(RpcConversation rpcConversation) {
		this.rpcConversation = rpcConversation;
		return this;
	}

	public BeaconClientBuilder setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
		return this;
	}

}