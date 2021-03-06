// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http2.grpc.beacon;

/**
 * Protobuf type {@code beacon.ListAgentsReply}
 */
public  final class ListAgentsReply extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:beacon.ListAgentsReply)
    ListAgentsReplyOrBuilder {
private static final long serialVersionUID = 0L;
  // Use ListAgentsReply.newBuilder() to construct.
  private ListAgentsReply(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ListAgentsReply() {
    agents_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private ListAgentsReply(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            org.ar4k.agent.tunnels.http2.grpc.beacon.Status.Builder subBuilder = null;
            if (result_ != null) {
              subBuilder = result_.toBuilder();
            }
            result_ = input.readMessage(org.ar4k.agent.tunnels.http2.grpc.beacon.Status.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(result_);
              result_ = subBuilder.buildPartial();
            }

            break;
          }
          case 18: {
            if (!((mutable_bitField0_ & 0x00000002) != 0)) {
              agents_ = new java.util.ArrayList<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent>();
              mutable_bitField0_ |= 0x00000002;
            }
            agents_.add(
                input.readMessage(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.parser(), extensionRegistry));
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      if (((mutable_bitField0_ & 0x00000002) != 0)) {
        agents_ = java.util.Collections.unmodifiableList(agents_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_ListAgentsReply_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_ListAgentsReply_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply.class, org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply.Builder.class);
  }

  private int bitField0_;
  public static final int RESULT_FIELD_NUMBER = 1;
  private org.ar4k.agent.tunnels.http2.grpc.beacon.Status result_;
  /**
   * <code>.beacon.Status result = 1;</code>
   */
  public boolean hasResult() {
    return result_ != null;
  }
  /**
   * <code>.beacon.Status result = 1;</code>
   */
  public org.ar4k.agent.tunnels.http2.grpc.beacon.Status getResult() {
    return result_ == null ? org.ar4k.agent.tunnels.http2.grpc.beacon.Status.getDefaultInstance() : result_;
  }
  /**
   * <code>.beacon.Status result = 1;</code>
   */
  public org.ar4k.agent.tunnels.http2.grpc.beacon.StatusOrBuilder getResultOrBuilder() {
    return getResult();
  }

  public static final int AGENTS_FIELD_NUMBER = 2;
  private java.util.List<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent> agents_;
  /**
   * <code>repeated .beacon.Agent agents = 2;</code>
   */
  public java.util.List<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent> getAgentsList() {
    return agents_;
  }
  /**
   * <code>repeated .beacon.Agent agents = 2;</code>
   */
  public java.util.List<? extends org.ar4k.agent.tunnels.http2.grpc.beacon.AgentOrBuilder> 
      getAgentsOrBuilderList() {
    return agents_;
  }
  /**
   * <code>repeated .beacon.Agent agents = 2;</code>
   */
  public int getAgentsCount() {
    return agents_.size();
  }
  /**
   * <code>repeated .beacon.Agent agents = 2;</code>
   */
  public org.ar4k.agent.tunnels.http2.grpc.beacon.Agent getAgents(int index) {
    return agents_.get(index);
  }
  /**
   * <code>repeated .beacon.Agent agents = 2;</code>
   */
  public org.ar4k.agent.tunnels.http2.grpc.beacon.AgentOrBuilder getAgentsOrBuilder(
      int index) {
    return agents_.get(index);
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (result_ != null) {
      output.writeMessage(1, getResult());
    }
    for (int i = 0; i < agents_.size(); i++) {
      output.writeMessage(2, agents_.get(i));
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (result_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getResult());
    }
    for (int i = 0; i < agents_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, agents_.get(i));
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply)) {
      return super.equals(obj);
    }
    org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply other = (org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply) obj;

    if (hasResult() != other.hasResult()) return false;
    if (hasResult()) {
      if (!getResult()
          .equals(other.getResult())) return false;
    }
    if (!getAgentsList()
        .equals(other.getAgentsList())) return false;
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasResult()) {
      hash = (37 * hash) + RESULT_FIELD_NUMBER;
      hash = (53 * hash) + getResult().hashCode();
    }
    if (getAgentsCount() > 0) {
      hash = (37 * hash) + AGENTS_FIELD_NUMBER;
      hash = (53 * hash) + getAgentsList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code beacon.ListAgentsReply}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:beacon.ListAgentsReply)
      org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReplyOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_ListAgentsReply_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_ListAgentsReply_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply.class, org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply.Builder.class);
    }

    // Construct using org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
        getAgentsFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (resultBuilder_ == null) {
        result_ = null;
      } else {
        result_ = null;
        resultBuilder_ = null;
      }
      if (agentsBuilder_ == null) {
        agents_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
      } else {
        agentsBuilder_.clear();
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_ListAgentsReply_descriptor;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply getDefaultInstanceForType() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply.getDefaultInstance();
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply build() {
      org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply buildPartial() {
      org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply result = new org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (resultBuilder_ == null) {
        result.result_ = result_;
      } else {
        result.result_ = resultBuilder_.build();
      }
      if (agentsBuilder_ == null) {
        if (((bitField0_ & 0x00000002) != 0)) {
          agents_ = java.util.Collections.unmodifiableList(agents_);
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.agents_ = agents_;
      } else {
        result.agents_ = agentsBuilder_.build();
      }
      result.bitField0_ = to_bitField0_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply) {
        return mergeFrom((org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply other) {
      if (other == org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply.getDefaultInstance()) return this;
      if (other.hasResult()) {
        mergeResult(other.getResult());
      }
      if (agentsBuilder_ == null) {
        if (!other.agents_.isEmpty()) {
          if (agents_.isEmpty()) {
            agents_ = other.agents_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensureAgentsIsMutable();
            agents_.addAll(other.agents_);
          }
          onChanged();
        }
      } else {
        if (!other.agents_.isEmpty()) {
          if (agentsBuilder_.isEmpty()) {
            agentsBuilder_.dispose();
            agentsBuilder_ = null;
            agents_ = other.agents_;
            bitField0_ = (bitField0_ & ~0x00000002);
            agentsBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getAgentsFieldBuilder() : null;
          } else {
            agentsBuilder_.addAllMessages(other.agents_);
          }
        }
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private org.ar4k.agent.tunnels.http2.grpc.beacon.Status result_;
    private com.google.protobuf.SingleFieldBuilderV3<
        org.ar4k.agent.tunnels.http2.grpc.beacon.Status, org.ar4k.agent.tunnels.http2.grpc.beacon.Status.Builder, org.ar4k.agent.tunnels.http2.grpc.beacon.StatusOrBuilder> resultBuilder_;
    /**
     * <code>.beacon.Status result = 1;</code>
     */
    public boolean hasResult() {
      return resultBuilder_ != null || result_ != null;
    }
    /**
     * <code>.beacon.Status result = 1;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.Status getResult() {
      if (resultBuilder_ == null) {
        return result_ == null ? org.ar4k.agent.tunnels.http2.grpc.beacon.Status.getDefaultInstance() : result_;
      } else {
        return resultBuilder_.getMessage();
      }
    }
    /**
     * <code>.beacon.Status result = 1;</code>
     */
    public Builder setResult(org.ar4k.agent.tunnels.http2.grpc.beacon.Status value) {
      if (resultBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        result_ = value;
        onChanged();
      } else {
        resultBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.beacon.Status result = 1;</code>
     */
    public Builder setResult(
        org.ar4k.agent.tunnels.http2.grpc.beacon.Status.Builder builderForValue) {
      if (resultBuilder_ == null) {
        result_ = builderForValue.build();
        onChanged();
      } else {
        resultBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.beacon.Status result = 1;</code>
     */
    public Builder mergeResult(org.ar4k.agent.tunnels.http2.grpc.beacon.Status value) {
      if (resultBuilder_ == null) {
        if (result_ != null) {
          result_ =
            org.ar4k.agent.tunnels.http2.grpc.beacon.Status.newBuilder(result_).mergeFrom(value).buildPartial();
        } else {
          result_ = value;
        }
        onChanged();
      } else {
        resultBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.beacon.Status result = 1;</code>
     */
    public Builder clearResult() {
      if (resultBuilder_ == null) {
        result_ = null;
        onChanged();
      } else {
        result_ = null;
        resultBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.beacon.Status result = 1;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.Status.Builder getResultBuilder() {
      
      onChanged();
      return getResultFieldBuilder().getBuilder();
    }
    /**
     * <code>.beacon.Status result = 1;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.StatusOrBuilder getResultOrBuilder() {
      if (resultBuilder_ != null) {
        return resultBuilder_.getMessageOrBuilder();
      } else {
        return result_ == null ?
            org.ar4k.agent.tunnels.http2.grpc.beacon.Status.getDefaultInstance() : result_;
      }
    }
    /**
     * <code>.beacon.Status result = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        org.ar4k.agent.tunnels.http2.grpc.beacon.Status, org.ar4k.agent.tunnels.http2.grpc.beacon.Status.Builder, org.ar4k.agent.tunnels.http2.grpc.beacon.StatusOrBuilder> 
        getResultFieldBuilder() {
      if (resultBuilder_ == null) {
        resultBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            org.ar4k.agent.tunnels.http2.grpc.beacon.Status, org.ar4k.agent.tunnels.http2.grpc.beacon.Status.Builder, org.ar4k.agent.tunnels.http2.grpc.beacon.StatusOrBuilder>(
                getResult(),
                getParentForChildren(),
                isClean());
        result_ = null;
      }
      return resultBuilder_;
    }

    private java.util.List<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent> agents_ =
      java.util.Collections.emptyList();
    private void ensureAgentsIsMutable() {
      if (!((bitField0_ & 0x00000002) != 0)) {
        agents_ = new java.util.ArrayList<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent>(agents_);
        bitField0_ |= 0x00000002;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder, org.ar4k.agent.tunnels.http2.grpc.beacon.AgentOrBuilder> agentsBuilder_;

    /**
     * <code>repeated .beacon.Agent agents = 2;</code>
     */
    public java.util.List<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent> getAgentsList() {
      if (agentsBuilder_ == null) {
        return java.util.Collections.unmodifiableList(agents_);
      } else {
        return agentsBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .beacon.Agent agents = 2;</code>
     */
    public int getAgentsCount() {
      if (agentsBuilder_ == null) {
        return agents_.size();
      } else {
        return agentsBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .beacon.Agent agents = 2;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.Agent getAgents(int index) {
      if (agentsBuilder_ == null) {
        return agents_.get(index);
      } else {
        return agentsBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .beacon.Agent agents = 2;</code>
     */
    public Builder setAgents(
        int index, org.ar4k.agent.tunnels.http2.grpc.beacon.Agent value) {
      if (agentsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureAgentsIsMutable();
        agents_.set(index, value);
        onChanged();
      } else {
        agentsBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .beacon.Agent agents = 2;</code>
     */
    public Builder setAgents(
        int index, org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder builderForValue) {
      if (agentsBuilder_ == null) {
        ensureAgentsIsMutable();
        agents_.set(index, builderForValue.build());
        onChanged();
      } else {
        agentsBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .beacon.Agent agents = 2;</code>
     */
    public Builder addAgents(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent value) {
      if (agentsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureAgentsIsMutable();
        agents_.add(value);
        onChanged();
      } else {
        agentsBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .beacon.Agent agents = 2;</code>
     */
    public Builder addAgents(
        int index, org.ar4k.agent.tunnels.http2.grpc.beacon.Agent value) {
      if (agentsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureAgentsIsMutable();
        agents_.add(index, value);
        onChanged();
      } else {
        agentsBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .beacon.Agent agents = 2;</code>
     */
    public Builder addAgents(
        org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder builderForValue) {
      if (agentsBuilder_ == null) {
        ensureAgentsIsMutable();
        agents_.add(builderForValue.build());
        onChanged();
      } else {
        agentsBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .beacon.Agent agents = 2;</code>
     */
    public Builder addAgents(
        int index, org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder builderForValue) {
      if (agentsBuilder_ == null) {
        ensureAgentsIsMutable();
        agents_.add(index, builderForValue.build());
        onChanged();
      } else {
        agentsBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .beacon.Agent agents = 2;</code>
     */
    public Builder addAllAgents(
        java.lang.Iterable<? extends org.ar4k.agent.tunnels.http2.grpc.beacon.Agent> values) {
      if (agentsBuilder_ == null) {
        ensureAgentsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, agents_);
        onChanged();
      } else {
        agentsBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .beacon.Agent agents = 2;</code>
     */
    public Builder clearAgents() {
      if (agentsBuilder_ == null) {
        agents_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
      } else {
        agentsBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .beacon.Agent agents = 2;</code>
     */
    public Builder removeAgents(int index) {
      if (agentsBuilder_ == null) {
        ensureAgentsIsMutable();
        agents_.remove(index);
        onChanged();
      } else {
        agentsBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .beacon.Agent agents = 2;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder getAgentsBuilder(
        int index) {
      return getAgentsFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .beacon.Agent agents = 2;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.AgentOrBuilder getAgentsOrBuilder(
        int index) {
      if (agentsBuilder_ == null) {
        return agents_.get(index);  } else {
        return agentsBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .beacon.Agent agents = 2;</code>
     */
    public java.util.List<? extends org.ar4k.agent.tunnels.http2.grpc.beacon.AgentOrBuilder> 
         getAgentsOrBuilderList() {
      if (agentsBuilder_ != null) {
        return agentsBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(agents_);
      }
    }
    /**
     * <code>repeated .beacon.Agent agents = 2;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder addAgentsBuilder() {
      return getAgentsFieldBuilder().addBuilder(
          org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.getDefaultInstance());
    }
    /**
     * <code>repeated .beacon.Agent agents = 2;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder addAgentsBuilder(
        int index) {
      return getAgentsFieldBuilder().addBuilder(
          index, org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.getDefaultInstance());
    }
    /**
     * <code>repeated .beacon.Agent agents = 2;</code>
     */
    public java.util.List<org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder> 
         getAgentsBuilderList() {
      return getAgentsFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder, org.ar4k.agent.tunnels.http2.grpc.beacon.AgentOrBuilder> 
        getAgentsFieldBuilder() {
      if (agentsBuilder_ == null) {
        agentsBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder, org.ar4k.agent.tunnels.http2.grpc.beacon.AgentOrBuilder>(
                agents_,
                ((bitField0_ & 0x00000002) != 0),
                getParentForChildren(),
                isClean());
        agents_ = null;
      }
      return agentsBuilder_;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:beacon.ListAgentsReply)
  }

  // @@protoc_insertion_point(class_scope:beacon.ListAgentsReply)
  private static final org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply();
  }

  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ListAgentsReply>
      PARSER = new com.google.protobuf.AbstractParser<ListAgentsReply>() {
    @java.lang.Override
    public ListAgentsReply parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new ListAgentsReply(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ListAgentsReply> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ListAgentsReply> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.ar4k.agent.tunnels.http2.grpc.beacon.ListAgentsReply getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

