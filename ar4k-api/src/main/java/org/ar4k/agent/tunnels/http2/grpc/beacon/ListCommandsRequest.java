// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http2.grpc.beacon;

/**
 * Protobuf type {@code beacon.ListCommandsRequest}
 */
public  final class ListCommandsRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:beacon.ListCommandsRequest)
    ListCommandsRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use ListCommandsRequest.newBuilder() to construct.
  private ListCommandsRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ListCommandsRequest() {
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private ListCommandsRequest(
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
            org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder subBuilder = null;
            if (agentTarget_ != null) {
              subBuilder = agentTarget_.toBuilder();
            }
            agentTarget_ = input.readMessage(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(agentTarget_);
              agentTarget_ = subBuilder.buildPartial();
            }

            break;
          }
          case 18: {
            org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder subBuilder = null;
            if (agentSender_ != null) {
              subBuilder = agentSender_.toBuilder();
            }
            agentSender_ = input.readMessage(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(agentSender_);
              agentSender_ = subBuilder.buildPartial();
            }

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
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_ListCommandsRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_ListCommandsRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest.class, org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest.Builder.class);
  }

  public static final int AGENTTARGET_FIELD_NUMBER = 1;
  private org.ar4k.agent.tunnels.http2.grpc.beacon.Agent agentTarget_;
  /**
   * <code>.beacon.Agent agentTarget = 1;</code>
   */
  public boolean hasAgentTarget() {
    return agentTarget_ != null;
  }
  /**
   * <code>.beacon.Agent agentTarget = 1;</code>
   */
  public org.ar4k.agent.tunnels.http2.grpc.beacon.Agent getAgentTarget() {
    return agentTarget_ == null ? org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.getDefaultInstance() : agentTarget_;
  }
  /**
   * <code>.beacon.Agent agentTarget = 1;</code>
   */
  public org.ar4k.agent.tunnels.http2.grpc.beacon.AgentOrBuilder getAgentTargetOrBuilder() {
    return getAgentTarget();
  }

  public static final int AGENTSENDER_FIELD_NUMBER = 2;
  private org.ar4k.agent.tunnels.http2.grpc.beacon.Agent agentSender_;
  /**
   * <code>.beacon.Agent agentSender = 2;</code>
   */
  public boolean hasAgentSender() {
    return agentSender_ != null;
  }
  /**
   * <code>.beacon.Agent agentSender = 2;</code>
   */
  public org.ar4k.agent.tunnels.http2.grpc.beacon.Agent getAgentSender() {
    return agentSender_ == null ? org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.getDefaultInstance() : agentSender_;
  }
  /**
   * <code>.beacon.Agent agentSender = 2;</code>
   */
  public org.ar4k.agent.tunnels.http2.grpc.beacon.AgentOrBuilder getAgentSenderOrBuilder() {
    return getAgentSender();
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
    if (agentTarget_ != null) {
      output.writeMessage(1, getAgentTarget());
    }
    if (agentSender_ != null) {
      output.writeMessage(2, getAgentSender());
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (agentTarget_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getAgentTarget());
    }
    if (agentSender_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getAgentSender());
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
    if (!(obj instanceof org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest)) {
      return super.equals(obj);
    }
    org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest other = (org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest) obj;

    if (hasAgentTarget() != other.hasAgentTarget()) return false;
    if (hasAgentTarget()) {
      if (!getAgentTarget()
          .equals(other.getAgentTarget())) return false;
    }
    if (hasAgentSender() != other.hasAgentSender()) return false;
    if (hasAgentSender()) {
      if (!getAgentSender()
          .equals(other.getAgentSender())) return false;
    }
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
    if (hasAgentTarget()) {
      hash = (37 * hash) + AGENTTARGET_FIELD_NUMBER;
      hash = (53 * hash) + getAgentTarget().hashCode();
    }
    if (hasAgentSender()) {
      hash = (37 * hash) + AGENTSENDER_FIELD_NUMBER;
      hash = (53 * hash) + getAgentSender().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest parseFrom(
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
  public static Builder newBuilder(org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest prototype) {
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
   * Protobuf type {@code beacon.ListCommandsRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:beacon.ListCommandsRequest)
      org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_ListCommandsRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_ListCommandsRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest.class, org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest.Builder.class);
    }

    // Construct using org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest.newBuilder()
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
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (agentTargetBuilder_ == null) {
        agentTarget_ = null;
      } else {
        agentTarget_ = null;
        agentTargetBuilder_ = null;
      }
      if (agentSenderBuilder_ == null) {
        agentSender_ = null;
      } else {
        agentSender_ = null;
        agentSenderBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_ListCommandsRequest_descriptor;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest getDefaultInstanceForType() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest.getDefaultInstance();
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest build() {
      org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest buildPartial() {
      org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest result = new org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest(this);
      if (agentTargetBuilder_ == null) {
        result.agentTarget_ = agentTarget_;
      } else {
        result.agentTarget_ = agentTargetBuilder_.build();
      }
      if (agentSenderBuilder_ == null) {
        result.agentSender_ = agentSender_;
      } else {
        result.agentSender_ = agentSenderBuilder_.build();
      }
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
      if (other instanceof org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest) {
        return mergeFrom((org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest other) {
      if (other == org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest.getDefaultInstance()) return this;
      if (other.hasAgentTarget()) {
        mergeAgentTarget(other.getAgentTarget());
      }
      if (other.hasAgentSender()) {
        mergeAgentSender(other.getAgentSender());
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
      org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private org.ar4k.agent.tunnels.http2.grpc.beacon.Agent agentTarget_;
    private com.google.protobuf.SingleFieldBuilderV3<
        org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder, org.ar4k.agent.tunnels.http2.grpc.beacon.AgentOrBuilder> agentTargetBuilder_;
    /**
     * <code>.beacon.Agent agentTarget = 1;</code>
     */
    public boolean hasAgentTarget() {
      return agentTargetBuilder_ != null || agentTarget_ != null;
    }
    /**
     * <code>.beacon.Agent agentTarget = 1;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.Agent getAgentTarget() {
      if (agentTargetBuilder_ == null) {
        return agentTarget_ == null ? org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.getDefaultInstance() : agentTarget_;
      } else {
        return agentTargetBuilder_.getMessage();
      }
    }
    /**
     * <code>.beacon.Agent agentTarget = 1;</code>
     */
    public Builder setAgentTarget(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent value) {
      if (agentTargetBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        agentTarget_ = value;
        onChanged();
      } else {
        agentTargetBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.beacon.Agent agentTarget = 1;</code>
     */
    public Builder setAgentTarget(
        org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder builderForValue) {
      if (agentTargetBuilder_ == null) {
        agentTarget_ = builderForValue.build();
        onChanged();
      } else {
        agentTargetBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.beacon.Agent agentTarget = 1;</code>
     */
    public Builder mergeAgentTarget(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent value) {
      if (agentTargetBuilder_ == null) {
        if (agentTarget_ != null) {
          agentTarget_ =
            org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.newBuilder(agentTarget_).mergeFrom(value).buildPartial();
        } else {
          agentTarget_ = value;
        }
        onChanged();
      } else {
        agentTargetBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.beacon.Agent agentTarget = 1;</code>
     */
    public Builder clearAgentTarget() {
      if (agentTargetBuilder_ == null) {
        agentTarget_ = null;
        onChanged();
      } else {
        agentTarget_ = null;
        agentTargetBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.beacon.Agent agentTarget = 1;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder getAgentTargetBuilder() {
      
      onChanged();
      return getAgentTargetFieldBuilder().getBuilder();
    }
    /**
     * <code>.beacon.Agent agentTarget = 1;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.AgentOrBuilder getAgentTargetOrBuilder() {
      if (agentTargetBuilder_ != null) {
        return agentTargetBuilder_.getMessageOrBuilder();
      } else {
        return agentTarget_ == null ?
            org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.getDefaultInstance() : agentTarget_;
      }
    }
    /**
     * <code>.beacon.Agent agentTarget = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder, org.ar4k.agent.tunnels.http2.grpc.beacon.AgentOrBuilder> 
        getAgentTargetFieldBuilder() {
      if (agentTargetBuilder_ == null) {
        agentTargetBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder, org.ar4k.agent.tunnels.http2.grpc.beacon.AgentOrBuilder>(
                getAgentTarget(),
                getParentForChildren(),
                isClean());
        agentTarget_ = null;
      }
      return agentTargetBuilder_;
    }

    private org.ar4k.agent.tunnels.http2.grpc.beacon.Agent agentSender_;
    private com.google.protobuf.SingleFieldBuilderV3<
        org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder, org.ar4k.agent.tunnels.http2.grpc.beacon.AgentOrBuilder> agentSenderBuilder_;
    /**
     * <code>.beacon.Agent agentSender = 2;</code>
     */
    public boolean hasAgentSender() {
      return agentSenderBuilder_ != null || agentSender_ != null;
    }
    /**
     * <code>.beacon.Agent agentSender = 2;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.Agent getAgentSender() {
      if (agentSenderBuilder_ == null) {
        return agentSender_ == null ? org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.getDefaultInstance() : agentSender_;
      } else {
        return agentSenderBuilder_.getMessage();
      }
    }
    /**
     * <code>.beacon.Agent agentSender = 2;</code>
     */
    public Builder setAgentSender(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent value) {
      if (agentSenderBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        agentSender_ = value;
        onChanged();
      } else {
        agentSenderBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.beacon.Agent agentSender = 2;</code>
     */
    public Builder setAgentSender(
        org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder builderForValue) {
      if (agentSenderBuilder_ == null) {
        agentSender_ = builderForValue.build();
        onChanged();
      } else {
        agentSenderBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.beacon.Agent agentSender = 2;</code>
     */
    public Builder mergeAgentSender(org.ar4k.agent.tunnels.http2.grpc.beacon.Agent value) {
      if (agentSenderBuilder_ == null) {
        if (agentSender_ != null) {
          agentSender_ =
            org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.newBuilder(agentSender_).mergeFrom(value).buildPartial();
        } else {
          agentSender_ = value;
        }
        onChanged();
      } else {
        agentSenderBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.beacon.Agent agentSender = 2;</code>
     */
    public Builder clearAgentSender() {
      if (agentSenderBuilder_ == null) {
        agentSender_ = null;
        onChanged();
      } else {
        agentSender_ = null;
        agentSenderBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.beacon.Agent agentSender = 2;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder getAgentSenderBuilder() {
      
      onChanged();
      return getAgentSenderFieldBuilder().getBuilder();
    }
    /**
     * <code>.beacon.Agent agentSender = 2;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.AgentOrBuilder getAgentSenderOrBuilder() {
      if (agentSenderBuilder_ != null) {
        return agentSenderBuilder_.getMessageOrBuilder();
      } else {
        return agentSender_ == null ?
            org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.getDefaultInstance() : agentSender_;
      }
    }
    /**
     * <code>.beacon.Agent agentSender = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder, org.ar4k.agent.tunnels.http2.grpc.beacon.AgentOrBuilder> 
        getAgentSenderFieldBuilder() {
      if (agentSenderBuilder_ == null) {
        agentSenderBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            org.ar4k.agent.tunnels.http2.grpc.beacon.Agent, org.ar4k.agent.tunnels.http2.grpc.beacon.Agent.Builder, org.ar4k.agent.tunnels.http2.grpc.beacon.AgentOrBuilder>(
                getAgentSender(),
                getParentForChildren(),
                isClean());
        agentSender_ = null;
      }
      return agentSenderBuilder_;
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


    // @@protoc_insertion_point(builder_scope:beacon.ListCommandsRequest)
  }

  // @@protoc_insertion_point(class_scope:beacon.ListCommandsRequest)
  private static final org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest();
  }

  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ListCommandsRequest>
      PARSER = new com.google.protobuf.AbstractParser<ListCommandsRequest>() {
    @java.lang.Override
    public ListCommandsRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new ListCommandsRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ListCommandsRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ListCommandsRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.ar4k.agent.tunnels.http2.grpc.beacon.ListCommandsRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
