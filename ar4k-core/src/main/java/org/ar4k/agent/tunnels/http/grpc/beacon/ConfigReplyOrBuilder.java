// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http.grpc.beacon;

public interface ConfigReplyOrBuilder extends
    // @@protoc_insertion_point(interface_extends:beacon.ConfigReply)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string base64Config = 1;</code>
   */
  java.lang.String getBase64Config();
  /**
   * <code>string base64Config = 1;</code>
   */
  com.google.protobuf.ByteString
      getBase64ConfigBytes();

  /**
   * <code>int64 restartAt = 2;</code>
   */
  long getRestartAt();

  /**
   * <code>.beacon.StatusValue status = 3;</code>
   */
  int getStatusValue();
  /**
   * <code>.beacon.StatusValue status = 3;</code>
   */
  org.ar4k.agent.tunnels.http.grpc.beacon.StatusValue getStatus();
}
