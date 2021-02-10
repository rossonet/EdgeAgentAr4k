package org.ar4k.agent.tunnels.http2.beacon.server;

import java.security.UnrecoverableKeyException;

import org.ar4k.agent.core.Homunculus;

public class BeaconServerBuilder {
	private Homunculus homunculus = null;
	private int port = 0;
	private int discoveryPort = 0;
	private String broadcastAddress = null;
	private boolean acceptCerts = false;
	private String stringDiscovery = null;
	private String certChainFile = null;
	private String certFile = null;
	private String privateKeyFile = null;
	private String aliasBeaconServerInKeystore = null;
	private String caChainPem = null;
	private String filterActiveCommand = null;
	private String filterBlackListCertRegister = null;

	public Homunculus getHomunculus() {
		return homunculus;
	}

	public BeaconServerBuilder setHomunculus(Homunculus homunculus) {
		this.homunculus = homunculus;
		return this;
	}

	public int getPort() {
		return port;
	}

	public BeaconServerBuilder setPort(int port) {
		this.port = port;
		return this;
	}

	public int getDiscoveryPort() {
		return discoveryPort;
	}

	public BeaconServerBuilder setDiscoveryPort(int discoveryPort) {
		this.discoveryPort = discoveryPort;
		return this;
	}

	public String getBroadcastAddress() {
		return broadcastAddress;
	}

	public BeaconServerBuilder setBroadcastAddress(String broadcastAddress) {
		this.broadcastAddress = broadcastAddress;
		return this;
	}

	public boolean isAcceptCerts() {
		return acceptCerts;
	}

	public BeaconServerBuilder setAcceptCerts(boolean acceptCerts) {
		this.acceptCerts = acceptCerts;
		return this;
	}

	public String getStringDiscovery() {
		return stringDiscovery;
	}

	public BeaconServerBuilder setStringDiscovery(String stringDiscovery) {
		this.stringDiscovery = stringDiscovery;
		return this;
	}

	public String getCertChainFile() {
		return certChainFile;
	}

	public BeaconServerBuilder setCertChainFile(String certChainFile) {
		this.certChainFile = certChainFile;
		return this;
	}

	public String getCertFile() {
		return certFile;
	}

	public BeaconServerBuilder setCertFile(String certFile) {
		this.certFile = certFile;
		return this;
	}

	public String getPrivateKeyFile() {
		return privateKeyFile;
	}

	public BeaconServerBuilder setPrivateKeyFile(String privateKeyFile) {
		this.privateKeyFile = privateKeyFile;
		return this;
	}

	public String getAliasBeaconServerInKeystore() {
		return aliasBeaconServerInKeystore;
	}

	public BeaconServerBuilder setAliasBeaconServerInKeystore(String aliasBeaconServerInKeystore) {
		this.aliasBeaconServerInKeystore = aliasBeaconServerInKeystore;
		return this;
	}

	public String getCaChainPem() {
		return caChainPem;
	}

	public BeaconServerBuilder setCaChainPem(String caChainPem) {
		this.caChainPem = caChainPem;
		return this;
	}

	public String getFilterActiveCommand() {
		return filterActiveCommand;
	}

	public BeaconServerBuilder setFilterActiveCommand(String filterActiveCommand) {
		this.filterActiveCommand = filterActiveCommand;
		return this;
	}

	public String getFilterBlackListCertRegister() {
		return filterBlackListCertRegister;
	}

	public BeaconServerBuilder setFilterBlackListCertRegister(String filterBlackListCertRegister) {
		this.filterBlackListCertRegister = filterBlackListCertRegister;
		return this;
	}

	public BeaconServer build() throws UnrecoverableKeyException {
		return new BeaconServer(homunculus, port, discoveryPort, broadcastAddress, acceptCerts, stringDiscovery,
				certChainFile, certFile, privateKeyFile, aliasBeaconServerInKeystore, caChainPem,
				filterActiveCommand, filterBlackListCertRegister);
	}

}