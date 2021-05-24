package org.ar4k.agent.opcua.client;

import java.util.Collection;
import java.util.HashSet;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

public class OpcUaTreeDataNode {
  private String browseName = null;
  private String description = null;
  private String nodeClass = null;
  private UInteger writeMask = null;
  private UInteger userWriteMask = null;
  private String readMask = null;
  private String userReadMask = null;
  private Node node = null;
  ExpandedNodeId id = null;
  Collection<OpcUaTreeDataNode> childs = null;

  public String getBrowseName() {
    return browseName;
  }

  public void setBrowseName(String browseName) {
    this.browseName = browseName;
  }

  public String getNodeClass() {
    return nodeClass;
  }

  public void setNodeClass(String nodeClass) {
    this.nodeClass = nodeClass;
  }

  public String getId() {
    return toParseableString(id);
  }

  public ExpandedNodeId getExpandedNodeId() {
    return id;
  }

  @Override
  public String toString() {
    return getId();
  }

  public void setId(ExpandedNodeId expandedNodeId) {
    this.id = expandedNodeId;
  }

  public Collection<OpcUaTreeDataNode> getChilds() {
    return childs;
  }

  public Collection<String> getChildren() {
    Collection<String> listA = new HashSet<>();
    if (childs != null)
      for (OpcUaTreeDataNode n : childs) {
        listA.add(n.getId());
      }
    return listA;

  }

  public Boolean getWriteMask() {
    return filterMask(writeMask);
  }

  public void setWriteMask(UInteger uInteger) {
    this.writeMask = uInteger;
  }

  public Boolean getUserWriteMask() {
    return filterMask(userWriteMask);
  }

  public void setUserWriteMask(UInteger uInteger) {
    this.userWriteMask = uInteger;
  }

  private Boolean filterMask(UInteger userMask) {
    if (userMask != null) {
      if (userMask.equals(UInteger.valueOf(0))) {
        return false;
      } else if (userMask.equals(UInteger.valueOf(1))) {
        return true;
      }
    } else {
      return (Boolean) null;
    }
    return (Boolean) null;
  }

  private String toParseableString(ExpandedNodeId node) {
    StringBuilder sb = new StringBuilder();
    sb.append("ns=").append(node.getNamespaceIndex()).append(";");
    switch (node.getType()) {
    case Numeric:
      sb.append("i=").append(node.getIdentifier());
      break;
    case String:
      sb.append("s=").append(node.getIdentifier());
      break;
    case Guid:
      sb.append("g=").append(node.getIdentifier());
      break;
    case Opaque:
      ByteString bs = (ByteString) node.getIdentifier();
      if (bs.isNull())
        sb.append("b=");
      else
        sb.append("b=").append(DatatypeConverter.printBase64Binary(bs.bytes()));
      break;
    default:
      throw new IllegalStateException("unknown IdType: " + node.getType());
    }
    return sb.toString();
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getReadMask() {
    return readMask;
  }

  public void setReadMask(String readMask) {
    this.readMask = readMask;
  }

  public String getUserReadMask() {
    return userReadMask;
  }

  public void setUserReadMask(String userReadMask) {
    this.userReadMask = userReadMask;
  }

  public void setNode(Node node) {
    this.node = node;
  }

  public Node getNode() {
    return node;
  }

  public void setChilds(Collection<OpcUaTreeDataNode> childs) {
    this.childs = childs;
  }
}
