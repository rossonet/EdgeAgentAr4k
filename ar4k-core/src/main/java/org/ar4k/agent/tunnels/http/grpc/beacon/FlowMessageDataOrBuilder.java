// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http.grpc.beacon;

public interface FlowMessageDataOrBuilder extends
    // @@protoc_insertion_point(interface_extends:beacon.FlowMessageData)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated .beacon.DataNode values = 1;</code>
   */
  java.util.List<org.ar4k.agent.tunnels.http.grpc.beacon.DataNode> 
      getValuesList();
  /**
   * <code>repeated .beacon.DataNode values = 1;</code>
   */
  org.ar4k.agent.tunnels.http.grpc.beacon.DataNode getValues(int index);
  /**
   * <code>repeated .beacon.DataNode values = 1;</code>
   */
  int getValuesCount();
  /**
   * <code>repeated .beacon.DataNode values = 1;</code>
   */
  java.util.List<? extends org.ar4k.agent.tunnels.http.grpc.beacon.DataNodeOrBuilder> 
      getValuesOrBuilderList();
  /**
   * <code>repeated .beacon.DataNode values = 1;</code>
   */
  org.ar4k.agent.tunnels.http.grpc.beacon.DataNodeOrBuilder getValuesOrBuilder(
      int index);

  /**
   * <pre>
   * sperimentale per indirizzare rapidamente il routing nei grossi volumi
   * </pre>
   *
   * <code>int32 hiSpeedRoutingTag = 2;</code>
   */
  int getHiSpeedRoutingTag();
}