package org.ar4k.agent.core.data;

import java.util.Collection;
import java.util.HashSet;

import org.ar4k.agent.tunnels.http.grpc.beacon.DataType;
import org.joda.time.Instant;

public class DataNode {

  private String nodeId = null;

  private String description = null;

  private Instant createData = null;

  private DataType dataType = null;

  private Collection<DataTag> tags = new HashSet<>();

  public Collection<DataTag> getTags() {
    return tags;
  }

  public void addDataTag(DataTag tag) {
    this.tags.add(tag);
  }

  public void removeDataTag(DataTag tag) {
    this.tags.remove(tag);
  }

  public void clearDataTag(DataTag tag) {
    this.tags.clear();
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Instant getCreateData() {
    return createData;
  }

  public void setCreateData(Instant createData) {
    this.createData = createData;
  }

  public DataType getDataType() {
    return dataType;
  }

  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }

}
