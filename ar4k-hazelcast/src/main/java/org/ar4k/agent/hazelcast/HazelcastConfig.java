package org.ar4k.agent.hazelcast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.services.EdgeComponent;

import com.beust.jcommander.Parameter;

public class HazelcastConfig extends AbstractServiceConfig {

	private static final long serialVersionUID = -2924869182396567535L;

	@Parameter(names = "--uniqueName", description = "the uniqueName of node for the cluster")
	public String uniqueName = UUID.randomUUID().toString();
	@Parameter(names = "--beanName", description = "the beanName for the Spring registration")
	public String beanName = "hazelcast-instance";
	@Parameter(names = "--groupName", description = "the optional group name")
	public String groupName = null;
	@Parameter(names = "--groupPassword", description = "the password of group if it is needed")
	public String groupPassword = null;
	@Parameter(names = "--multiCastEnabled", description = "is multicast plugin enbled?")
	public boolean multiCastEnabled = true;
	@Parameter(names = "--members", description = "the member nodes of the cluster", arity = 0)
	public List<String> members = new ArrayList<>();
	@Parameter(names = "--kubernetesEnabled", description = "is kubernetes plugin enabled?")
	public boolean kubernetesEnabled = false;
	@Parameter(names = "--kubernetesNameSpace", description = "the kubernetes name space of the cluster")
	public String kubernetesNameSpace = null;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public EdgeComponent instantiate() {
		return new HazelcastComponent(this);
	}

	@Override
	public String getUniqueId() {
		return uniqueName;
	}

	@Override
	public int getPriority() {
		return 2;
	}

	@Override
	public boolean isSpringBean() {
		return true;
	}

	public String getKubernetesNameSpace() {
		return kubernetesNameSpace;
	}

	public List<String> getMembers() {
		return members;
	}

	public String getGroupPassword() {
		return groupPassword;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setGroupPassword(String groupPassword) {
		this.groupPassword = groupPassword;
	}

	public void setMultiCastEnable(boolean multiCastEnable) {
		this.multiCastEnabled = multiCastEnable;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

	public void setKubernetesEnabled(boolean kubernetesEnabled) {
		this.kubernetesEnabled = kubernetesEnabled;
	}

	public void setKubernetesNameSpace(String kubernetesNameSpace) {
		this.kubernetesNameSpace = kubernetesNameSpace;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public boolean isMultiCastEnabled() {
		return multiCastEnabled;
	}

	public boolean isKubernetesEnabled() {
		return kubernetesEnabled;
	}

	@Override
	public String toString() {
		return "HazelcastConfig [creationDate=" + getCreationDate() + ", lastUpdate=" + getLastUpdate()
				+ ", uniqueName=" + uniqueName + ", beanName=" + beanName + ", groupName=" + groupName
				+ ", groupPassword=" + groupPassword + ", multiCastEnabled=" + multiCastEnabled + ", members=" + members
				+ ", kubernetesEnabled=" + kubernetesEnabled + ", kubernetesNameSpace=" + kubernetesNameSpace + "]";
	}

}
