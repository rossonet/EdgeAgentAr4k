package org.ar4k.agent.farm.openshift;

import org.ar4k.agent.farm.openshift.OpenShiftClusterTwin.NodeType;

public class ClusterNode {

	private final NodeType nodeType;
	private final String mac;
	private final String ip;

	public ClusterNode(NodeType nodeType, String mac, String ip) {
		this.nodeType = nodeType;
		this.mac = mac;
		this.ip = ip;
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

}
