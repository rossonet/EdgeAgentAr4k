// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http2.grpc.beacon;

public interface CommandOrBuilder extends
    // @@protoc_insertion_point(interface_extends:beacon.Command)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.beacon.Agent agentSender = 1;</code>
   */
  boolean hasAgentSender();
  /**
   * <code>.beacon.Agent agentSender = 1;</code>
   */
  org.ar4k.agent.tunnels.http2.grpc.beacon.Agent getAgentSender();
  /**
   * <code>.beacon.Agent agentSender = 1;</code>
   */
  org.ar4k.agent.tunnels.http2.grpc.beacon.AgentOrBuilder getAgentSenderOrBuilder();

  /**
   * <code>string command = 2;</code>
   */
  java.lang.String getCommand();
  /**
   * <code>string command = 2;</code>
   */
  com.google.protobuf.ByteString
      getCommandBytes();
}
