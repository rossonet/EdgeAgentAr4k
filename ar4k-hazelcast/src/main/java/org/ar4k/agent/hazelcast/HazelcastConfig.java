package org.ar4k.agent.hazelcast;

import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Ar4kComponent;
import org.joda.time.Instant;

import com.beust.jcommander.Parameter;
import com.google.gson.TypeAdapter;

public class HazelcastConfig extends AbstractServiceConfig {

  private static final long serialVersionUID = -2924869182396567535L;

  public Instant creationDate = new Instant();
  public Instant lastUpdate = new Instant();

  @Parameter(names = "--uniqueName", description = "the uniqueName of node for the cluster")
  public String uniqueName = Anima.getApplicationContext().getBean(Anima.class).getAgentUniqueName();
  @Parameter(names = "--beanName", description = "the beanName for the Spring registration")
  public String beanName = "hazelcast-instance";
  @Parameter(names = "--groupName", description = "the optional group name")
  public String groupName = null;
  @Parameter(names = "--groupPassword", description = "the password of group if it is needed")
  public String groupPassword = null;
  @Parameter(names = "--multiCastEnable", description = "is multicast plugin enbled?")
  public boolean multiCastEnable = true;
  @Parameter(names = "--members", description = "the members of the cluster", arity = 0)
  public List<String> members = new ArrayList<>();
  @Parameter(names = "--kubernetesEnabled", description = "is kubernetes plugin enbled?")
  public boolean kubernetesEnabled = false;
  @Parameter(names = "--kubernetesNameSpace", description = "the kubernetes name space of the cluster")
  public String kubernetesNameSpace = null;
  @Parameter(names = "--internalDirectoryChannel", description = "internal directory channel for message topics")
  public String bindDirectoryChannel = null;
  @Parameter(names = "--topics", description = "topics to subscribe", arity = 0)
  public List<String> topics = new ArrayList<>();

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Ar4kComponent instantiate() {
    return new HazelcastComponent(this);
  }

  @Override
  public Instant getCreationDate() {
    return creationDate;
  }

  @Override
  public Instant getLastUpdateDate() {
    return lastUpdate;
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
  public TypeAdapter<? extends ConfigSeed> getJsonTypeAdapter() {
    return new HazelcastConfigJsonAdapter();
  }

  @Override
  public boolean isSpringBean() {
    return true;
  }

  public String getKubernetesNameSpace() {
    return kubernetesNameSpace;
  }

  public boolean isKubernetes() {
    return kubernetesEnabled;
  }

  public List<String> getMembers() {
    return members;
  }

  public boolean isMultiCast() {
    return multiCastEnable;
  }

  public String getGroupPassword() {
    return groupPassword;
  }

  public String getGroup() {
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
    this.multiCastEnable = multiCastEnable;
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

  public List<String> getTopics() {
    return topics;
  }

  public void setTopics(List<String> topics) {
    this.topics = topics;
  }

  public Instant getLastUpdate() {
    return lastUpdate;
  }

  public String getUniqueName() {
    return uniqueName;
  }

  public String getGroupName() {
    return groupName;
  }

  public boolean isMultiCastEnable() {
    return multiCastEnable;
  }

  public boolean isKubernetesEnabled() {
    return kubernetesEnabled;
  }

  public String getBindDirectoryChannel() {
    return bindDirectoryChannel;
  }
}
