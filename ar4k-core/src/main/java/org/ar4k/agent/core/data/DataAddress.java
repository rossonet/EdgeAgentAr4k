package org.ar4k.agent.core.data;

import java.util.Collection;
import java.util.HashSet;

public class DataAddress {

  private Collection<DataNode> dataNodes = new HashSet<>();

  public Collection<DataNode> getDataNodes() {
    return dataNodes;
  }

  public void addDataNodes(DataNode dataNodes) {
    this.dataNodes.add(dataNodes);
  }

  public void removeDataNodes(DataNode dataNodes) {
    this.dataNodes.remove(dataNodes);
  }

  public void clearDataNodes(DataNode dataNodes) {
    this.dataNodes.clear();
  }

  public Collection<DataTag> getAllTags() {
    final Collection<DataTag> result = new HashSet<>();
    for (DataNode n : dataNodes) {
      result.addAll(n.getTags());
    }
    return result;
  }

}
