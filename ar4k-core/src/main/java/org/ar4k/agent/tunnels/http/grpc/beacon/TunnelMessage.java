// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http.grpc.beacon;

/**
 * Protobuf type {@code beacon.TunnelMessage}
 */
public  final class TunnelMessage extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:beacon.TunnelMessage)
    TunnelMessageOrBuilder {
private static final long serialVersionUID = 0L;
  // Use TunnelMessage.newBuilder() to construct.
  private TunnelMessage(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private TunnelMessage() {
    payload_ = com.google.protobuf.ByteString.EMPTY;
    errors_ = com.google.protobuf.LazyStringArrayList.EMPTY;
    uniqueId_ = "";
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private TunnelMessage(
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
            org.ar4k.agent.tunnels.http.grpc.beacon.Agent.Builder subBuilder = null;
            if (agent_ != null) {
              subBuilder = agent_.toBuilder();
            }
            agent_ = input.readMessage(org.ar4k.agent.tunnels.http.grpc.beacon.Agent.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(agent_);
              agent_ = subBuilder.buildPartial();
            }

            break;
          }
          case 16: {

            targeId_ = input.readInt64();
            break;
          }
          case 26: {

            payload_ = input.readBytes();
            break;
          }
          case 34: {
            java.lang.String s = input.readStringRequireUtf8();
            if (!((mutable_bitField0_ & 0x00000008) != 0)) {
              errors_ = new com.google.protobuf.LazyStringArrayList();
              mutable_bitField0_ |= 0x00000008;
            }
            errors_.add(s);
            break;
          }
          case 42: {
            java.lang.String s = input.readStringRequireUtf8();

            uniqueId_ = s;
            break;
          }
          case 48: {

            sessionId_ = input.readInt64();
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
      if (((mutable_bitField0_ & 0x00000008) != 0)) {
        errors_ = errors_.getUnmodifiableView();
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_TunnelMessage_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_TunnelMessage_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage.class, org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage.Builder.class);
  }

  private int bitField0_;
  public static final int AGENT_FIELD_NUMBER = 1;
  private org.ar4k.agent.tunnels.http.grpc.beacon.Agent agent_;
  /**
   * <code>.beacon.Agent agent = 1;</code>
   */
  public boolean hasAgent() {
    return agent_ != null;
  }
  /**
   * <code>.beacon.Agent agent = 1;</code>
   */
  public org.ar4k.agent.tunnels.http.grpc.beacon.Agent getAgent() {
    return agent_ == null ? org.ar4k.agent.tunnels.http.grpc.beacon.Agent.getDefaultInstance() : agent_;
  }
  /**
   * <code>.beacon.Agent agent = 1;</code>
   */
  public org.ar4k.agent.tunnels.http.grpc.beacon.AgentOrBuilder getAgentOrBuilder() {
    return getAgent();
  }

  public static final int TARGEID_FIELD_NUMBER = 2;
  private long targeId_;
  /**
   * <code>int64 targeId = 2;</code>
   */
  public long getTargeId() {
    return targeId_;
  }

  public static final int PAYLOAD_FIELD_NUMBER = 3;
  private com.google.protobuf.ByteString payload_;
  /**
   * <code>bytes payload = 3;</code>
   */
  public com.google.protobuf.ByteString getPayload() {
    return payload_;
  }

  public static final int ERRORS_FIELD_NUMBER = 4;
  private com.google.protobuf.LazyStringList errors_;
  /**
   * <code>repeated string errors = 4;</code>
   */
  public com.google.protobuf.ProtocolStringList
      getErrorsList() {
    return errors_;
  }
  /**
   * <code>repeated string errors = 4;</code>
   */
  public int getErrorsCount() {
    return errors_.size();
  }
  /**
   * <code>repeated string errors = 4;</code>
   */
  public java.lang.String getErrors(int index) {
    return errors_.get(index);
  }
  /**
   * <code>repeated string errors = 4;</code>
   */
  public com.google.protobuf.ByteString
      getErrorsBytes(int index) {
    return errors_.getByteString(index);
  }

  public static final int UNIQUEID_FIELD_NUMBER = 5;
  private volatile java.lang.Object uniqueId_;
  /**
   * <code>string uniqueId = 5;</code>
   */
  public java.lang.String getUniqueId() {
    java.lang.Object ref = uniqueId_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      uniqueId_ = s;
      return s;
    }
  }
  /**
   * <code>string uniqueId = 5;</code>
   */
  public com.google.protobuf.ByteString
      getUniqueIdBytes() {
    java.lang.Object ref = uniqueId_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      uniqueId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int SESSIONID_FIELD_NUMBER = 6;
  private long sessionId_;
  /**
   * <code>int64 sessionId = 6;</code>
   */
  public long getSessionId() {
    return sessionId_;
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
    if (agent_ != null) {
      output.writeMessage(1, getAgent());
    }
    if (targeId_ != 0L) {
      output.writeInt64(2, targeId_);
    }
    if (!payload_.isEmpty()) {
      output.writeBytes(3, payload_);
    }
    for (int i = 0; i < errors_.size(); i++) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 4, errors_.getRaw(i));
    }
    if (!getUniqueIdBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 5, uniqueId_);
    }
    if (sessionId_ != 0L) {
      output.writeInt64(6, sessionId_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (agent_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getAgent());
    }
    if (targeId_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(2, targeId_);
    }
    if (!payload_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(3, payload_);
    }
    {
      int dataSize = 0;
      for (int i = 0; i < errors_.size(); i++) {
        dataSize += computeStringSizeNoTag(errors_.getRaw(i));
      }
      size += dataSize;
      size += 1 * getErrorsList().size();
    }
    if (!getUniqueIdBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(5, uniqueId_);
    }
    if (sessionId_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(6, sessionId_);
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
    if (!(obj instanceof org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage)) {
      return super.equals(obj);
    }
    org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage other = (org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage) obj;

    if (hasAgent() != other.hasAgent()) return false;
    if (hasAgent()) {
      if (!getAgent()
          .equals(other.getAgent())) return false;
    }
    if (getTargeId()
        != other.getTargeId()) return false;
    if (!getPayload()
        .equals(other.getPayload())) return false;
    if (!getErrorsList()
        .equals(other.getErrorsList())) return false;
    if (!getUniqueId()
        .equals(other.getUniqueId())) return false;
    if (getSessionId()
        != other.getSessionId()) return false;
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
    if (hasAgent()) {
      hash = (37 * hash) + AGENT_FIELD_NUMBER;
      hash = (53 * hash) + getAgent().hashCode();
    }
    hash = (37 * hash) + TARGEID_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getTargeId());
    hash = (37 * hash) + PAYLOAD_FIELD_NUMBER;
    hash = (53 * hash) + getPayload().hashCode();
    if (getErrorsCount() > 0) {
      hash = (37 * hash) + ERRORS_FIELD_NUMBER;
      hash = (53 * hash) + getErrorsList().hashCode();
    }
    hash = (37 * hash) + UNIQUEID_FIELD_NUMBER;
    hash = (53 * hash) + getUniqueId().hashCode();
    hash = (37 * hash) + SESSIONID_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getSessionId());
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage parseFrom(
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
  public static Builder newBuilder(org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage prototype) {
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
   * Protobuf type {@code beacon.TunnelMessage}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:beacon.TunnelMessage)
      org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessageOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_TunnelMessage_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_TunnelMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage.class, org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage.Builder.class);
    }

    // Construct using org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage.newBuilder()
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
      if (agentBuilder_ == null) {
        agent_ = null;
      } else {
        agent_ = null;
        agentBuilder_ = null;
      }
      targeId_ = 0L;

      payload_ = com.google.protobuf.ByteString.EMPTY;

      errors_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      bitField0_ = (bitField0_ & ~0x00000008);
      uniqueId_ = "";

      sessionId_ = 0L;

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_TunnelMessage_descriptor;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage getDefaultInstanceForType() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage.getDefaultInstance();
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage build() {
      org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage buildPartial() {
      org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage result = new org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (agentBuilder_ == null) {
        result.agent_ = agent_;
      } else {
        result.agent_ = agentBuilder_.build();
      }
      result.targeId_ = targeId_;
      result.payload_ = payload_;
      if (((bitField0_ & 0x00000008) != 0)) {
        errors_ = errors_.getUnmodifiableView();
        bitField0_ = (bitField0_ & ~0x00000008);
      }
      result.errors_ = errors_;
      result.uniqueId_ = uniqueId_;
      result.sessionId_ = sessionId_;
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
      if (other instanceof org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage) {
        return mergeFrom((org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage other) {
      if (other == org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage.getDefaultInstance()) return this;
      if (other.hasAgent()) {
        mergeAgent(other.getAgent());
      }
      if (other.getTargeId() != 0L) {
        setTargeId(other.getTargeId());
      }
      if (other.getPayload() != com.google.protobuf.ByteString.EMPTY) {
        setPayload(other.getPayload());
      }
      if (!other.errors_.isEmpty()) {
        if (errors_.isEmpty()) {
          errors_ = other.errors_;
          bitField0_ = (bitField0_ & ~0x00000008);
        } else {
          ensureErrorsIsMutable();
          errors_.addAll(other.errors_);
        }
        onChanged();
      }
      if (!other.getUniqueId().isEmpty()) {
        uniqueId_ = other.uniqueId_;
        onChanged();
      }
      if (other.getSessionId() != 0L) {
        setSessionId(other.getSessionId());
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
      org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private org.ar4k.agent.tunnels.http.grpc.beacon.Agent agent_;
    private com.google.protobuf.SingleFieldBuilderV3<
        org.ar4k.agent.tunnels.http.grpc.beacon.Agent, org.ar4k.agent.tunnels.http.grpc.beacon.Agent.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.AgentOrBuilder> agentBuilder_;
    /**
     * <code>.beacon.Agent agent = 1;</code>
     */
    public boolean hasAgent() {
      return agentBuilder_ != null || agent_ != null;
    }
    /**
     * <code>.beacon.Agent agent = 1;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Agent getAgent() {
      if (agentBuilder_ == null) {
        return agent_ == null ? org.ar4k.agent.tunnels.http.grpc.beacon.Agent.getDefaultInstance() : agent_;
      } else {
        return agentBuilder_.getMessage();
      }
    }
    /**
     * <code>.beacon.Agent agent = 1;</code>
     */
    public Builder setAgent(org.ar4k.agent.tunnels.http.grpc.beacon.Agent value) {
      if (agentBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        agent_ = value;
        onChanged();
      } else {
        agentBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.beacon.Agent agent = 1;</code>
     */
    public Builder setAgent(
        org.ar4k.agent.tunnels.http.grpc.beacon.Agent.Builder builderForValue) {
      if (agentBuilder_ == null) {
        agent_ = builderForValue.build();
        onChanged();
      } else {
        agentBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.beacon.Agent agent = 1;</code>
     */
    public Builder mergeAgent(org.ar4k.agent.tunnels.http.grpc.beacon.Agent value) {
      if (agentBuilder_ == null) {
        if (agent_ != null) {
          agent_ =
            org.ar4k.agent.tunnels.http.grpc.beacon.Agent.newBuilder(agent_).mergeFrom(value).buildPartial();
        } else {
          agent_ = value;
        }
        onChanged();
      } else {
        agentBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.beacon.Agent agent = 1;</code>
     */
    public Builder clearAgent() {
      if (agentBuilder_ == null) {
        agent_ = null;
        onChanged();
      } else {
        agent_ = null;
        agentBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.beacon.Agent agent = 1;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Agent.Builder getAgentBuilder() {
      
      onChanged();
      return getAgentFieldBuilder().getBuilder();
    }
    /**
     * <code>.beacon.Agent agent = 1;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.AgentOrBuilder getAgentOrBuilder() {
      if (agentBuilder_ != null) {
        return agentBuilder_.getMessageOrBuilder();
      } else {
        return agent_ == null ?
            org.ar4k.agent.tunnels.http.grpc.beacon.Agent.getDefaultInstance() : agent_;
      }
    }
    /**
     * <code>.beacon.Agent agent = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        org.ar4k.agent.tunnels.http.grpc.beacon.Agent, org.ar4k.agent.tunnels.http.grpc.beacon.Agent.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.AgentOrBuilder> 
        getAgentFieldBuilder() {
      if (agentBuilder_ == null) {
        agentBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            org.ar4k.agent.tunnels.http.grpc.beacon.Agent, org.ar4k.agent.tunnels.http.grpc.beacon.Agent.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.AgentOrBuilder>(
                getAgent(),
                getParentForChildren(),
                isClean());
        agent_ = null;
      }
      return agentBuilder_;
    }

    private long targeId_ ;
    /**
     * <code>int64 targeId = 2;</code>
     */
    public long getTargeId() {
      return targeId_;
    }
    /**
     * <code>int64 targeId = 2;</code>
     */
    public Builder setTargeId(long value) {
      
      targeId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 targeId = 2;</code>
     */
    public Builder clearTargeId() {
      
      targeId_ = 0L;
      onChanged();
      return this;
    }

    private com.google.protobuf.ByteString payload_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <code>bytes payload = 3;</code>
     */
    public com.google.protobuf.ByteString getPayload() {
      return payload_;
    }
    /**
     * <code>bytes payload = 3;</code>
     */
    public Builder setPayload(com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      payload_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>bytes payload = 3;</code>
     */
    public Builder clearPayload() {
      
      payload_ = getDefaultInstance().getPayload();
      onChanged();
      return this;
    }

    private com.google.protobuf.LazyStringList errors_ = com.google.protobuf.LazyStringArrayList.EMPTY;
    private void ensureErrorsIsMutable() {
      if (!((bitField0_ & 0x00000008) != 0)) {
        errors_ = new com.google.protobuf.LazyStringArrayList(errors_);
        bitField0_ |= 0x00000008;
       }
    }
    /**
     * <code>repeated string errors = 4;</code>
     */
    public com.google.protobuf.ProtocolStringList
        getErrorsList() {
      return errors_.getUnmodifiableView();
    }
    /**
     * <code>repeated string errors = 4;</code>
     */
    public int getErrorsCount() {
      return errors_.size();
    }
    /**
     * <code>repeated string errors = 4;</code>
     */
    public java.lang.String getErrors(int index) {
      return errors_.get(index);
    }
    /**
     * <code>repeated string errors = 4;</code>
     */
    public com.google.protobuf.ByteString
        getErrorsBytes(int index) {
      return errors_.getByteString(index);
    }
    /**
     * <code>repeated string errors = 4;</code>
     */
    public Builder setErrors(
        int index, java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  ensureErrorsIsMutable();
      errors_.set(index, value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated string errors = 4;</code>
     */
    public Builder addErrors(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  ensureErrorsIsMutable();
      errors_.add(value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated string errors = 4;</code>
     */
    public Builder addAllErrors(
        java.lang.Iterable<java.lang.String> values) {
      ensureErrorsIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(
          values, errors_);
      onChanged();
      return this;
    }
    /**
     * <code>repeated string errors = 4;</code>
     */
    public Builder clearErrors() {
      errors_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      bitField0_ = (bitField0_ & ~0x00000008);
      onChanged();
      return this;
    }
    /**
     * <code>repeated string errors = 4;</code>
     */
    public Builder addErrorsBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      ensureErrorsIsMutable();
      errors_.add(value);
      onChanged();
      return this;
    }

    private java.lang.Object uniqueId_ = "";
    /**
     * <code>string uniqueId = 5;</code>
     */
    public java.lang.String getUniqueId() {
      java.lang.Object ref = uniqueId_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        uniqueId_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string uniqueId = 5;</code>
     */
    public com.google.protobuf.ByteString
        getUniqueIdBytes() {
      java.lang.Object ref = uniqueId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        uniqueId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string uniqueId = 5;</code>
     */
    public Builder setUniqueId(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      uniqueId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string uniqueId = 5;</code>
     */
    public Builder clearUniqueId() {
      
      uniqueId_ = getDefaultInstance().getUniqueId();
      onChanged();
      return this;
    }
    /**
     * <code>string uniqueId = 5;</code>
     */
    public Builder setUniqueIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      uniqueId_ = value;
      onChanged();
      return this;
    }

    private long sessionId_ ;
    /**
     * <code>int64 sessionId = 6;</code>
     */
    public long getSessionId() {
      return sessionId_;
    }
    /**
     * <code>int64 sessionId = 6;</code>
     */
    public Builder setSessionId(long value) {
      
      sessionId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 sessionId = 6;</code>
     */
    public Builder clearSessionId() {
      
      sessionId_ = 0L;
      onChanged();
      return this;
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


    // @@protoc_insertion_point(builder_scope:beacon.TunnelMessage)
  }

  // @@protoc_insertion_point(class_scope:beacon.TunnelMessage)
  private static final org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage();
  }

  public static org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<TunnelMessage>
      PARSER = new com.google.protobuf.AbstractParser<TunnelMessage>() {
    @java.lang.Override
    public TunnelMessage parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new TunnelMessage(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<TunnelMessage> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<TunnelMessage> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
