// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http.grpc.beacon;

/**
 * Protobuf type {@code beacon.Agent}
 */
public  final class Agent extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:beacon.Agent)
    AgentOrBuilder {
private static final long serialVersionUID = 0L;
  // Use Agent.newBuilder() to construct.
  private Agent(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private Agent() {
    agentUniqueName_ = "";
    shortDescription_ = "";
    jsonHardwareInfo_ = "";
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private Agent(
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
            java.lang.String s = input.readStringRequireUtf8();

            agentUniqueName_ = s;
            break;
          }
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();

            shortDescription_ = s;
            break;
          }
          case 26: {
            org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.Builder subBuilder = null;
            if (registerData_ != null) {
              subBuilder = registerData_.toBuilder();
            }
            registerData_ = input.readMessage(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(registerData_);
              registerData_ = subBuilder.buildPartial();
            }

            break;
          }
          case 34: {
            java.lang.String s = input.readStringRequireUtf8();

            jsonHardwareInfo_ = s;
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
    return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_Agent_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_Agent_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.ar4k.agent.tunnels.http.grpc.beacon.Agent.class, org.ar4k.agent.tunnels.http.grpc.beacon.Agent.Builder.class);
  }

  public static final int AGENTUNIQUENAME_FIELD_NUMBER = 1;
  private volatile java.lang.Object agentUniqueName_;
  /**
   * <code>string agentUniqueName = 1;</code>
   */
  public java.lang.String getAgentUniqueName() {
    java.lang.Object ref = agentUniqueName_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      agentUniqueName_ = s;
      return s;
    }
  }
  /**
   * <code>string agentUniqueName = 1;</code>
   */
  public com.google.protobuf.ByteString
      getAgentUniqueNameBytes() {
    java.lang.Object ref = agentUniqueName_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      agentUniqueName_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int SHORTDESCRIPTION_FIELD_NUMBER = 2;
  private volatile java.lang.Object shortDescription_;
  /**
   * <code>string shortDescription = 2;</code>
   */
  public java.lang.String getShortDescription() {
    java.lang.Object ref = shortDescription_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      shortDescription_ = s;
      return s;
    }
  }
  /**
   * <code>string shortDescription = 2;</code>
   */
  public com.google.protobuf.ByteString
      getShortDescriptionBytes() {
    java.lang.Object ref = shortDescription_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      shortDescription_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int REGISTERDATA_FIELD_NUMBER = 3;
  private org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply registerData_;
  /**
   * <code>.beacon.RegisterReply registerData = 3;</code>
   */
  public boolean hasRegisterData() {
    return registerData_ != null;
  }
  /**
   * <code>.beacon.RegisterReply registerData = 3;</code>
   */
  public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply getRegisterData() {
    return registerData_ == null ? org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance() : registerData_;
  }
  /**
   * <code>.beacon.RegisterReply registerData = 3;</code>
   */
  public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReplyOrBuilder getRegisterDataOrBuilder() {
    return getRegisterData();
  }

  public static final int JSONHARDWAREINFO_FIELD_NUMBER = 4;
  private volatile java.lang.Object jsonHardwareInfo_;
  /**
   * <code>string jsonHardwareInfo = 4;</code>
   */
  public java.lang.String getJsonHardwareInfo() {
    java.lang.Object ref = jsonHardwareInfo_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      jsonHardwareInfo_ = s;
      return s;
    }
  }
  /**
   * <code>string jsonHardwareInfo = 4;</code>
   */
  public com.google.protobuf.ByteString
      getJsonHardwareInfoBytes() {
    java.lang.Object ref = jsonHardwareInfo_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      jsonHardwareInfo_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
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
    if (!getAgentUniqueNameBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, agentUniqueName_);
    }
    if (!getShortDescriptionBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, shortDescription_);
    }
    if (registerData_ != null) {
      output.writeMessage(3, getRegisterData());
    }
    if (!getJsonHardwareInfoBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 4, jsonHardwareInfo_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getAgentUniqueNameBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, agentUniqueName_);
    }
    if (!getShortDescriptionBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, shortDescription_);
    }
    if (registerData_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, getRegisterData());
    }
    if (!getJsonHardwareInfoBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, jsonHardwareInfo_);
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
    if (!(obj instanceof org.ar4k.agent.tunnels.http.grpc.beacon.Agent)) {
      return super.equals(obj);
    }
    org.ar4k.agent.tunnels.http.grpc.beacon.Agent other = (org.ar4k.agent.tunnels.http.grpc.beacon.Agent) obj;

    if (!getAgentUniqueName()
        .equals(other.getAgentUniqueName())) return false;
    if (!getShortDescription()
        .equals(other.getShortDescription())) return false;
    if (hasRegisterData() != other.hasRegisterData()) return false;
    if (hasRegisterData()) {
      if (!getRegisterData()
          .equals(other.getRegisterData())) return false;
    }
    if (!getJsonHardwareInfo()
        .equals(other.getJsonHardwareInfo())) return false;
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
    hash = (37 * hash) + AGENTUNIQUENAME_FIELD_NUMBER;
    hash = (53 * hash) + getAgentUniqueName().hashCode();
    hash = (37 * hash) + SHORTDESCRIPTION_FIELD_NUMBER;
    hash = (53 * hash) + getShortDescription().hashCode();
    if (hasRegisterData()) {
      hash = (37 * hash) + REGISTERDATA_FIELD_NUMBER;
      hash = (53 * hash) + getRegisterData().hashCode();
    }
    hash = (37 * hash) + JSONHARDWAREINFO_FIELD_NUMBER;
    hash = (53 * hash) + getJsonHardwareInfo().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.ar4k.agent.tunnels.http.grpc.beacon.Agent parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.Agent parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.Agent parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.Agent parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.Agent parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.Agent parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.Agent parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.Agent parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.Agent parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.Agent parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.Agent parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.Agent parseFrom(
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
  public static Builder newBuilder(org.ar4k.agent.tunnels.http.grpc.beacon.Agent prototype) {
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
   * Protobuf type {@code beacon.Agent}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:beacon.Agent)
      org.ar4k.agent.tunnels.http.grpc.beacon.AgentOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_Agent_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_Agent_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.ar4k.agent.tunnels.http.grpc.beacon.Agent.class, org.ar4k.agent.tunnels.http.grpc.beacon.Agent.Builder.class);
    }

    // Construct using org.ar4k.agent.tunnels.http.grpc.beacon.Agent.newBuilder()
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
      agentUniqueName_ = "";

      shortDescription_ = "";

      if (registerDataBuilder_ == null) {
        registerData_ = null;
      } else {
        registerData_ = null;
        registerDataBuilder_ = null;
      }
      jsonHardwareInfo_ = "";

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_Agent_descriptor;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http.grpc.beacon.Agent getDefaultInstanceForType() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.Agent.getDefaultInstance();
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http.grpc.beacon.Agent build() {
      org.ar4k.agent.tunnels.http.grpc.beacon.Agent result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http.grpc.beacon.Agent buildPartial() {
      org.ar4k.agent.tunnels.http.grpc.beacon.Agent result = new org.ar4k.agent.tunnels.http.grpc.beacon.Agent(this);
      result.agentUniqueName_ = agentUniqueName_;
      result.shortDescription_ = shortDescription_;
      if (registerDataBuilder_ == null) {
        result.registerData_ = registerData_;
      } else {
        result.registerData_ = registerDataBuilder_.build();
      }
      result.jsonHardwareInfo_ = jsonHardwareInfo_;
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
      if (other instanceof org.ar4k.agent.tunnels.http.grpc.beacon.Agent) {
        return mergeFrom((org.ar4k.agent.tunnels.http.grpc.beacon.Agent)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.ar4k.agent.tunnels.http.grpc.beacon.Agent other) {
      if (other == org.ar4k.agent.tunnels.http.grpc.beacon.Agent.getDefaultInstance()) return this;
      if (!other.getAgentUniqueName().isEmpty()) {
        agentUniqueName_ = other.agentUniqueName_;
        onChanged();
      }
      if (!other.getShortDescription().isEmpty()) {
        shortDescription_ = other.shortDescription_;
        onChanged();
      }
      if (other.hasRegisterData()) {
        mergeRegisterData(other.getRegisterData());
      }
      if (!other.getJsonHardwareInfo().isEmpty()) {
        jsonHardwareInfo_ = other.jsonHardwareInfo_;
        onChanged();
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
      org.ar4k.agent.tunnels.http.grpc.beacon.Agent parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.ar4k.agent.tunnels.http.grpc.beacon.Agent) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object agentUniqueName_ = "";
    /**
     * <code>string agentUniqueName = 1;</code>
     */
    public java.lang.String getAgentUniqueName() {
      java.lang.Object ref = agentUniqueName_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        agentUniqueName_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string agentUniqueName = 1;</code>
     */
    public com.google.protobuf.ByteString
        getAgentUniqueNameBytes() {
      java.lang.Object ref = agentUniqueName_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        agentUniqueName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string agentUniqueName = 1;</code>
     */
    public Builder setAgentUniqueName(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      agentUniqueName_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string agentUniqueName = 1;</code>
     */
    public Builder clearAgentUniqueName() {
      
      agentUniqueName_ = getDefaultInstance().getAgentUniqueName();
      onChanged();
      return this;
    }
    /**
     * <code>string agentUniqueName = 1;</code>
     */
    public Builder setAgentUniqueNameBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      agentUniqueName_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object shortDescription_ = "";
    /**
     * <code>string shortDescription = 2;</code>
     */
    public java.lang.String getShortDescription() {
      java.lang.Object ref = shortDescription_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        shortDescription_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string shortDescription = 2;</code>
     */
    public com.google.protobuf.ByteString
        getShortDescriptionBytes() {
      java.lang.Object ref = shortDescription_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        shortDescription_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string shortDescription = 2;</code>
     */
    public Builder setShortDescription(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      shortDescription_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string shortDescription = 2;</code>
     */
    public Builder clearShortDescription() {
      
      shortDescription_ = getDefaultInstance().getShortDescription();
      onChanged();
      return this;
    }
    /**
     * <code>string shortDescription = 2;</code>
     */
    public Builder setShortDescriptionBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      shortDescription_ = value;
      onChanged();
      return this;
    }

    private org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply registerData_;
    private com.google.protobuf.SingleFieldBuilderV3<
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReplyOrBuilder> registerDataBuilder_;
    /**
     * <code>.beacon.RegisterReply registerData = 3;</code>
     */
    public boolean hasRegisterData() {
      return registerDataBuilder_ != null || registerData_ != null;
    }
    /**
     * <code>.beacon.RegisterReply registerData = 3;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply getRegisterData() {
      if (registerDataBuilder_ == null) {
        return registerData_ == null ? org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance() : registerData_;
      } else {
        return registerDataBuilder_.getMessage();
      }
    }
    /**
     * <code>.beacon.RegisterReply registerData = 3;</code>
     */
    public Builder setRegisterData(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply value) {
      if (registerDataBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        registerData_ = value;
        onChanged();
      } else {
        registerDataBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.beacon.RegisterReply registerData = 3;</code>
     */
    public Builder setRegisterData(
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.Builder builderForValue) {
      if (registerDataBuilder_ == null) {
        registerData_ = builderForValue.build();
        onChanged();
      } else {
        registerDataBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.beacon.RegisterReply registerData = 3;</code>
     */
    public Builder mergeRegisterData(org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply value) {
      if (registerDataBuilder_ == null) {
        if (registerData_ != null) {
          registerData_ =
            org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.newBuilder(registerData_).mergeFrom(value).buildPartial();
        } else {
          registerData_ = value;
        }
        onChanged();
      } else {
        registerDataBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.beacon.RegisterReply registerData = 3;</code>
     */
    public Builder clearRegisterData() {
      if (registerDataBuilder_ == null) {
        registerData_ = null;
        onChanged();
      } else {
        registerData_ = null;
        registerDataBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.beacon.RegisterReply registerData = 3;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.Builder getRegisterDataBuilder() {
      
      onChanged();
      return getRegisterDataFieldBuilder().getBuilder();
    }
    /**
     * <code>.beacon.RegisterReply registerData = 3;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReplyOrBuilder getRegisterDataOrBuilder() {
      if (registerDataBuilder_ != null) {
        return registerDataBuilder_.getMessageOrBuilder();
      } else {
        return registerData_ == null ?
            org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.getDefaultInstance() : registerData_;
      }
    }
    /**
     * <code>.beacon.RegisterReply registerData = 3;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReplyOrBuilder> 
        getRegisterDataFieldBuilder() {
      if (registerDataBuilder_ == null) {
        registerDataBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReplyOrBuilder>(
                getRegisterData(),
                getParentForChildren(),
                isClean());
        registerData_ = null;
      }
      return registerDataBuilder_;
    }

    private java.lang.Object jsonHardwareInfo_ = "";
    /**
     * <code>string jsonHardwareInfo = 4;</code>
     */
    public java.lang.String getJsonHardwareInfo() {
      java.lang.Object ref = jsonHardwareInfo_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        jsonHardwareInfo_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string jsonHardwareInfo = 4;</code>
     */
    public com.google.protobuf.ByteString
        getJsonHardwareInfoBytes() {
      java.lang.Object ref = jsonHardwareInfo_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        jsonHardwareInfo_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string jsonHardwareInfo = 4;</code>
     */
    public Builder setJsonHardwareInfo(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      jsonHardwareInfo_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string jsonHardwareInfo = 4;</code>
     */
    public Builder clearJsonHardwareInfo() {
      
      jsonHardwareInfo_ = getDefaultInstance().getJsonHardwareInfo();
      onChanged();
      return this;
    }
    /**
     * <code>string jsonHardwareInfo = 4;</code>
     */
    public Builder setJsonHardwareInfoBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      jsonHardwareInfo_ = value;
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


    // @@protoc_insertion_point(builder_scope:beacon.Agent)
  }

  // @@protoc_insertion_point(class_scope:beacon.Agent)
  private static final org.ar4k.agent.tunnels.http.grpc.beacon.Agent DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.ar4k.agent.tunnels.http.grpc.beacon.Agent();
  }

  public static org.ar4k.agent.tunnels.http.grpc.beacon.Agent getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Agent>
      PARSER = new com.google.protobuf.AbstractParser<Agent>() {
    @java.lang.Override
    public Agent parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new Agent(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<Agent> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Agent> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.ar4k.agent.tunnels.http.grpc.beacon.Agent getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

