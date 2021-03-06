// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http2.grpc.beacon;

public interface SubscribeRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:beacon.SubscribeRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.beacon.Agent agent = 1;</code>
   */
  boolean hasAgent();
  /**
   * <code>.beacon.Agent agent = 1;</code>
   */
  org.ar4k.agent.tunnels.http2.grpc.beacon.Agent getAgent();
  /**
   * <code>.beacon.Agent agent = 1;</code>
   */
  org.ar4k.agent.tunnels.http2.grpc.beacon.AgentOrBuilder getAgentOrBuilder();

  /**
   * <code>repeated .beacon.DataNode dataNodes = 2;</code>
   */
  java.util.List<org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode> 
      getDataNodesList();
  /**
   * <code>repeated .beacon.DataNode dataNodes = 2;</code>
   */
  org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode getDataNodes(int index);
  /**
   * <code>repeated .beacon.DataNode dataNodes = 2;</code>
   */
  int getDataNodesCount();
  /**
   * <code>repeated .beacon.DataNode dataNodes = 2;</code>
   */
  java.util.List<? extends org.ar4k.agent.tunnels.http2.grpc.beacon.DataNodeOrBuilder> 
      getDataNodesOrBuilderList();
  /**
   * <code>repeated .beacon.DataNode dataNodes = 2;</code>
   */
  org.ar4k.agent.tunnels.http2.grpc.beacon.DataNodeOrBuilder getDataNodesOrBuilder(
      int index);

  /**
   * <code>int32 samplingRate = 3;</code>
   */
  int getSamplingRate();
}
