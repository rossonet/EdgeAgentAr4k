// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http.grpc.beacon;

public interface ListCommandsRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:beacon.ListCommandsRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.beacon.Agent agentTarget = 1;</code>
   */
  boolean hasAgentTarget();
  /**
   * <code>.beacon.Agent agentTarget = 1;</code>
   */
  org.ar4k.agent.tunnels.http.grpc.beacon.Agent getAgentTarget();
  /**
   * <code>.beacon.Agent agentTarget = 1;</code>
   */
  org.ar4k.agent.tunnels.http.grpc.beacon.AgentOrBuilder getAgentTargetOrBuilder();

  /**
   * <code>.beacon.Agent agentSender = 2;</code>
   */
  boolean hasAgentSender();
  /**
   * <code>.beacon.Agent agentSender = 2;</code>
   */
  org.ar4k.agent.tunnels.http.grpc.beacon.Agent getAgentSender();
  /**
   * <code>.beacon.Agent agentSender = 2;</code>
   */
  org.ar4k.agent.tunnels.http.grpc.beacon.AgentOrBuilder getAgentSenderOrBuilder();
}