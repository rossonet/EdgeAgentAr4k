// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http2.grpc.beacon;

public interface ListCommandsReplyOrBuilder extends
    // @@protoc_insertion_point(interface_extends:beacon.ListCommandsReply)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated .beacon.Command commands = 1;</code>
   */
  java.util.List<org.ar4k.agent.tunnels.http2.grpc.beacon.Command> 
      getCommandsList();
  /**
   * <code>repeated .beacon.Command commands = 1;</code>
   */
  org.ar4k.agent.tunnels.http2.grpc.beacon.Command getCommands(int index);
  /**
   * <code>repeated .beacon.Command commands = 1;</code>
   */
  int getCommandsCount();
  /**
   * <code>repeated .beacon.Command commands = 1;</code>
   */
  java.util.List<? extends org.ar4k.agent.tunnels.http2.grpc.beacon.CommandOrBuilder> 
      getCommandsOrBuilderList();
  /**
   * <code>repeated .beacon.Command commands = 1;</code>
   */
  org.ar4k.agent.tunnels.http2.grpc.beacon.CommandOrBuilder getCommandsOrBuilder(
      int index);
}