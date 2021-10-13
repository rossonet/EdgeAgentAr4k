package org.ar4k.agent.farm.kubernetes.openshift;

import org.ar4k.agent.farm.kubernetes.openshift.OpenShiftClusterTwin.NodeType;

public class ClusterNode {

	private final NodeType nodeType;
	private final String mac;
	private final String ip;

	private final String name;

	public ClusterNode(NodeType nodeType, String mac, String ip, String name) {
		this.nodeType = nodeType;
		this.mac = mac;
		this.ip = ip;
		this.name = name;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public String getMac() {
		return mac;
	}

	public String getIp() {
		return ip;
	}

	public String getName() {
		return name;
	}

	public CharSequence getType() {
		return nodeType.getIpxeFileName();
	}

}
