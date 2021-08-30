package org.ar4k.agent.farm.openshift;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import org.ar4k.agent.helper.StringUtils;

public class OpenShiftClusterTwin {

	public enum NodeType {
		CONTROL_NODE, WORKER_NODE, BOOTSTRAP_NODE
	}

	public enum PhisicalType {
		BARE_METAL, AWS, AZURE, JUST_CONNECTION
	}

	private final PhisicalType phisicalType;
	private final String uniqueName;
	private final Map<String, ClusterNode> nodes = new HashMap<>();
	private String publicKey = null;
	private String baseDomain = null;
	private String domain = null;
	private String consoleIp = null;
	private String network = null;
	private String mask = null;
	private String dns1 = null;
	private String dns2 = null;

	public OpenShiftClusterTwin(PhisicalType phisicalType, String uniqueName) {
		this.phisicalType = phisicalType;
		this.uniqueName = uniqueName;
	}

	public String getHumanDescription() {
		return uniqueName + " -> " + phisicalType.toString();
	}

	public void prepareEnviroment() {
		// TODO Auto-generated method stub

	}

	public void setSshPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public void setBaseDomain(String baseDomain) {
		this.baseDomain = baseDomain;

	}

	public void setDomain(String domain) {
		this.domain = domain;

	}

	public void setClusterNetworkDetails(String consoleIp, String network, String mask, String dns1, String dns2,
			String consolePublicIp) {
		if (!StringUtils.isValidIPAddress(consoleIp)) {
			throw new InvalidParameterException(consoleIp + " is not a valid ip");
		}
		this.consoleIp = consoleIp;
		if (!StringUtils.isValidIPAddress(network)) {
			throw new InvalidParameterException(network + " is not a valid ip");
		}
		this.network = network;
		if (!StringUtils.isValidSubnetMask(mask)) {
			throw new InvalidParameterException(mask + " is not a valid network mask");
		}
		this.mask = mask;
		if (!StringUtils.isValidIPAddress(dns1)) {
			throw new InvalidParameterException(dns1 + " is not a valid ip");
		}
		this.dns1 = dns1;
		if (!StringUtils.isValidIPAddress(dns2)) {
			throw new InvalidParameterException(dns2 + " is not a valid ip");
		}
		this.dns2 = dns2;
	}

	public void addMacAddressAndIp(NodeType nodeType, String macAddress, String ip) {
		if (!StringUtils.isValidIPAddress(ip)) {
			throw new InvalidParameterException(ip + " is not a valid ip");
		}
		if (!StringUtils.isValidMacAddress(macAddress)) {
			throw new InvalidParameterException(macAddress + " is not a valid mac address");
		}
		nodes.put(macAddress, new ClusterNode(nodeType, macAddress, ip));
	}

	public boolean isConfigured() {
		return (checkBaseParameters() && checkOneBootstrapNode() && checkControllerNodes());
	}

	private boolean checkControllerNodes() {
		int total = 0;
		for (ClusterNode n : nodes.values()) {
			if (n.getNodeType().equals(NodeType.CONTROL_NODE)) {
				total++;
			}
		}
		return total >= 3;
	}

	private boolean checkOneBootstrapNode() {
		for (ClusterNode n : nodes.values()) {
			if (n.getNodeType().equals(NodeType.BOOTSTRAP_NODE)) {
				return true;
			}
		}
		return false;
	}

	private boolean checkBaseParameters() {
		return publicKey != null && baseDomain != null && domain != null && consoleIp != null && network != null
				&& mask != null && dns1 != null && dns2 != null;
	}

	public String getConfigureWarning() {
		// TODO Auto-generated method stub
		return "complete the class first";
	}

	public String getPublicDnsZone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OpenShiftClusterTwin [");
		if (phisicalType != null) {
			builder.append("phisicalType=");
			builder.append(phisicalType);
			builder.append(", ");
		}
		if (uniqueName != null) {
			builder.append("uniqueName=");
			builder.append(uniqueName);
		}
		builder.append("configured=");
		builder.append(isConfigured());
		builder.append("]");
		return builder.toString();
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getConsoleIp() {
		return consoleIp;
	}

	public void setConsoleIp(String consoleIp) {
		this.consoleIp = consoleIp;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getDns1() {
		return dns1;
	}

	public void setDns1(String dns1) {
		this.dns1 = dns1;
	}

	public String getDns2() {
		return dns2;
	}

	public void setDns2(String dns2) {
		this.dns2 = dns2;
	}

	public PhisicalType getPhisicalType() {
		return phisicalType;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public Map<String, ClusterNode> getNodes() {
		return nodes;
	}

	public String getBaseDomain() {
		return baseDomain;
	}

	public String getDomain() {
		return domain;
	}

}
