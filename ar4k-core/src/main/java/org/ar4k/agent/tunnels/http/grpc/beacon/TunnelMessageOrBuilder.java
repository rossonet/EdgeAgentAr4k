// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http.grpc.beacon;

public interface TunnelMessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:beacon.TunnelMessage)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.beacon.Agent agent = 1;</code>
   */
  boolean hasAgent();
  /**
   * <code>.beacon.Agent agent = 1;</code>
   */
  org.ar4k.agent.tunnels.http.grpc.beacon.Agent getAgent();
  /**
   * <code>.beacon.Agent agent = 1;</code>
   */
  org.ar4k.agent.tunnels.http.grpc.beacon.AgentOrBuilder getAgentOrBuilder();

  /**
   * <code>int64 targeId = 2;</code>
   */
  long getTargeId();

  /**
   * <code>bytes payload = 3;</code>
   */
  com.google.protobuf.ByteString getPayload();

  /**
   * <code>repeated string errors = 4;</code>
   */
  java.util.List<java.lang.String>
      getErrorsList();
  /**
   * <code>repeated string errors = 4;</code>
   */
  int getErrorsCount();
  /**
   * <code>repeated string errors = 4;</code>
   */
  java.lang.String getErrors(int index);
  /**
   * <code>repeated string errors = 4;</code>
   */
  com.google.protobuf.ByteString
      getErrorsBytes(int index);

  /**
   * <code>string uniqueId = 5;</code>
   */
  java.lang.String getUniqueId();
  /**
   * <code>string uniqueId = 5;</code>
   */
  com.google.protobuf.ByteString
      getUniqueIdBytes();

  /**
   * <code>int64 sessionId = 6;</code>
   */
  long getSessionId();
}
