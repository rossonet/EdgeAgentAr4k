// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http.grpc.beacon;

public interface AddressSpaceOrBuilder extends
    // @@protoc_insertion_point(interface_extends:beacon.AddressSpace)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int64 lastUpdate = 1;</code>
   */
  long getLastUpdate();

  /**
   * <code>repeated .beacon.NodeMetadata nodes = 2;</code>
   */
  java.util.List<org.ar4k.agent.tunnels.http.grpc.beacon.NodeMetadata> 
      getNodesList();
  /**
   * <code>repeated .beacon.NodeMetadata nodes = 2;</code>
   */
  org.ar4k.agent.tunnels.http.grpc.beacon.NodeMetadata getNodes(int index);
  /**
   * <code>repeated .beacon.NodeMetadata nodes = 2;</code>
   */
  int getNodesCount();
  /**
   * <code>repeated .beacon.NodeMetadata nodes = 2;</code>
   */
  java.util.List<? extends org.ar4k.agent.tunnels.http.grpc.beacon.NodeMetadataOrBuilder> 
      getNodesOrBuilderList();
  /**
   * <code>repeated .beacon.NodeMetadata nodes = 2;</code>
   */
  org.ar4k.agent.tunnels.http.grpc.beacon.NodeMetadataOrBuilder getNodesOrBuilder(
      int index);
}
