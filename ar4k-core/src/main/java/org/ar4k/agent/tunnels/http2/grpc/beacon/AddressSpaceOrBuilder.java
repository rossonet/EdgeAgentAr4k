// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http2.grpc.beacon;

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
  java.util.List<org.ar4k.agent.tunnels.http2.grpc.beacon.NodeMetadata> 
      getNodesList();
  /**
   * <code>repeated .beacon.NodeMetadata nodes = 2;</code>
   */
  org.ar4k.agent.tunnels.http2.grpc.beacon.NodeMetadata getNodes(int index);
  /**
   * <code>repeated .beacon.NodeMetadata nodes = 2;</code>
   */
  int getNodesCount();
  /**
   * <code>repeated .beacon.NodeMetadata nodes = 2;</code>
   */
  java.util.List<? extends org.ar4k.agent.tunnels.http2.grpc.beacon.NodeMetadataOrBuilder> 
      getNodesOrBuilderList();
  /**
   * <code>repeated .beacon.NodeMetadata nodes = 2;</code>
   */
  org.ar4k.agent.tunnels.http2.grpc.beacon.NodeMetadataOrBuilder getNodesOrBuilder(
      int index);

  /**
   * <code>repeated .beacon.AddressSpace subAddressSpace = 3;</code>
   */
  java.util.List<org.ar4k.agent.tunnels.http2.grpc.beacon.AddressSpace> 
      getSubAddressSpaceList();
  /**
   * <code>repeated .beacon.AddressSpace subAddressSpace = 3;</code>
   */
  org.ar4k.agent.tunnels.http2.grpc.beacon.AddressSpace getSubAddressSpace(int index);
  /**
   * <code>repeated .beacon.AddressSpace subAddressSpace = 3;</code>
   */
  int getSubAddressSpaceCount();
  /**
   * <code>repeated .beacon.AddressSpace subAddressSpace = 3;</code>
   */
  java.util.List<? extends org.ar4k.agent.tunnels.http2.grpc.beacon.AddressSpaceOrBuilder> 
      getSubAddressSpaceOrBuilderList();
  /**
   * <code>repeated .beacon.AddressSpace subAddressSpace = 3;</code>
   */
  org.ar4k.agent.tunnels.http2.grpc.beacon.AddressSpaceOrBuilder getSubAddressSpaceOrBuilder(
      int index);
}
