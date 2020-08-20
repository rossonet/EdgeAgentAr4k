// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http.grpc.beacon;

public interface NodeMetadataOrBuilder extends
    // @@protoc_insertion_point(interface_extends:beacon.NodeMetadata)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.beacon.DataNode dataNode = 1;</code>
   */
  boolean hasDataNode();
  /**
   * <code>.beacon.DataNode dataNode = 1;</code>
   */
  org.ar4k.agent.tunnels.http.grpc.beacon.DataNode getDataNode();
  /**
   * <code>.beacon.DataNode dataNode = 1;</code>
   */
  org.ar4k.agent.tunnels.http.grpc.beacon.DataNodeOrBuilder getDataNodeOrBuilder();

  /**
   * <code>string namespace = 2;</code>
   */
  java.lang.String getNamespace();
  /**
   * <code>string namespace = 2;</code>
   */
  com.google.protobuf.ByteString
      getNamespaceBytes();

  /**
   * <code>string domainId = 3;</code>
   */
  java.lang.String getDomainId();
  /**
   * <code>string domainId = 3;</code>
   */
  com.google.protobuf.ByteString
      getDomainIdBytes();

  /**
   * <code>repeated string tags = 4;</code>
   */
  java.util.List<java.lang.String>
      getTagsList();
  /**
   * <code>repeated string tags = 4;</code>
   */
  int getTagsCount();
  /**
   * <code>repeated string tags = 4;</code>
   */
  java.lang.String getTags(int index);
  /**
   * <code>repeated string tags = 4;</code>
   */
  com.google.protobuf.ByteString
      getTagsBytes(int index);

  /**
   * <code>repeated .beacon.ParentScope fatherForScopes = 5;</code>
   */
  java.util.List<org.ar4k.agent.tunnels.http.grpc.beacon.ParentScope> 
      getFatherForScopesList();
  /**
   * <code>repeated .beacon.ParentScope fatherForScopes = 5;</code>
   */
  org.ar4k.agent.tunnels.http.grpc.beacon.ParentScope getFatherForScopes(int index);
  /**
   * <code>repeated .beacon.ParentScope fatherForScopes = 5;</code>
   */
  int getFatherForScopesCount();
  /**
   * <code>repeated .beacon.ParentScope fatherForScopes = 5;</code>
   */
  java.util.List<? extends org.ar4k.agent.tunnels.http.grpc.beacon.ParentScopeOrBuilder> 
      getFatherForScopesOrBuilderList();
  /**
   * <code>repeated .beacon.ParentScope fatherForScopes = 5;</code>
   */
  org.ar4k.agent.tunnels.http.grpc.beacon.ParentScopeOrBuilder getFatherForScopesOrBuilder(
      int index);

  /**
   * <code>repeated string logs = 6;</code>
   */
  java.util.List<java.lang.String>
      getLogsList();
  /**
   * <code>repeated string logs = 6;</code>
   */
  int getLogsCount();
  /**
   * <code>repeated string logs = 6;</code>
   */
  java.lang.String getLogs(int index);
  /**
   * <code>repeated string logs = 6;</code>
   */
  com.google.protobuf.ByteString
      getLogsBytes(int index);
}
