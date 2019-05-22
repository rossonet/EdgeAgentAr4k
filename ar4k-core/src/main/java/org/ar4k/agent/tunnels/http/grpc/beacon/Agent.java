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
    secretKey_ = "";
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

            secretKey_ = s;
            break;
          }
          case 24: {

            pollingFrequency_ = input.readInt32();
            break;
          }
          case 32: {

            timestampRegistration_ = input.readInt64();
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

  public static final int SECRETKEY_FIELD_NUMBER = 2;
  private volatile java.lang.Object secretKey_;
  /**
   * <pre>
   *string hardwareInfoAsJson = 2;
   * </pre>
   *
   * <code>string secretKey = 2;</code>
   */
  public java.lang.String getSecretKey() {
    java.lang.Object ref = secretKey_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      secretKey_ = s;
      return s;
    }
  }
  /**
   * <pre>
   *string hardwareInfoAsJson = 2;
   * </pre>
   *
   * <code>string secretKey = 2;</code>
   */
  public com.google.protobuf.ByteString
      getSecretKeyBytes() {
    java.lang.Object ref = secretKey_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      secretKey_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int POLLINGFREQUENCY_FIELD_NUMBER = 3;
  private int pollingFrequency_;
  /**
   * <code>int32 pollingFrequency = 3;</code>
   */
  public int getPollingFrequency() {
    return pollingFrequency_;
  }

  public static final int TIMESTAMPREGISTRATION_FIELD_NUMBER = 4;
  private long timestampRegistration_;
  /**
   * <code>int64 timestampRegistration = 4;</code>
   */
  public long getTimestampRegistration() {
    return timestampRegistration_;
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
    if (!getSecretKeyBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, secretKey_);
    }
    if (pollingFrequency_ != 0) {
      output.writeInt32(3, pollingFrequency_);
    }
    if (timestampRegistration_ != 0L) {
      output.writeInt64(4, timestampRegistration_);
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
    if (!getSecretKeyBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, secretKey_);
    }
    if (pollingFrequency_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(3, pollingFrequency_);
    }
    if (timestampRegistration_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(4, timestampRegistration_);
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
    if (!getSecretKey()
        .equals(other.getSecretKey())) return false;
    if (getPollingFrequency()
        != other.getPollingFrequency()) return false;
    if (getTimestampRegistration()
        != other.getTimestampRegistration()) return false;
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
    hash = (37 * hash) + SECRETKEY_FIELD_NUMBER;
    hash = (53 * hash) + getSecretKey().hashCode();
    hash = (37 * hash) + POLLINGFREQUENCY_FIELD_NUMBER;
    hash = (53 * hash) + getPollingFrequency();
    hash = (37 * hash) + TIMESTAMPREGISTRATION_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getTimestampRegistration());
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

      secretKey_ = "";

      pollingFrequency_ = 0;

      timestampRegistration_ = 0L;

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
      result.secretKey_ = secretKey_;
      result.pollingFrequency_ = pollingFrequency_;
      result.timestampRegistration_ = timestampRegistration_;
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
      if (!other.getSecretKey().isEmpty()) {
        secretKey_ = other.secretKey_;
        onChanged();
      }
      if (other.getPollingFrequency() != 0) {
        setPollingFrequency(other.getPollingFrequency());
      }
      if (other.getTimestampRegistration() != 0L) {
        setTimestampRegistration(other.getTimestampRegistration());
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

    private java.lang.Object secretKey_ = "";
    /**
     * <pre>
     *string hardwareInfoAsJson = 2;
     * </pre>
     *
     * <code>string secretKey = 2;</code>
     */
    public java.lang.String getSecretKey() {
      java.lang.Object ref = secretKey_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        secretKey_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <pre>
     *string hardwareInfoAsJson = 2;
     * </pre>
     *
     * <code>string secretKey = 2;</code>
     */
    public com.google.protobuf.ByteString
        getSecretKeyBytes() {
      java.lang.Object ref = secretKey_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        secretKey_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     *string hardwareInfoAsJson = 2;
     * </pre>
     *
     * <code>string secretKey = 2;</code>
     */
    public Builder setSecretKey(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      secretKey_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     *string hardwareInfoAsJson = 2;
     * </pre>
     *
     * <code>string secretKey = 2;</code>
     */
    public Builder clearSecretKey() {
      
      secretKey_ = getDefaultInstance().getSecretKey();
      onChanged();
      return this;
    }
    /**
     * <pre>
     *string hardwareInfoAsJson = 2;
     * </pre>
     *
     * <code>string secretKey = 2;</code>
     */
    public Builder setSecretKeyBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      secretKey_ = value;
      onChanged();
      return this;
    }

    private int pollingFrequency_ ;
    /**
     * <code>int32 pollingFrequency = 3;</code>
     */
    public int getPollingFrequency() {
      return pollingFrequency_;
    }
    /**
     * <code>int32 pollingFrequency = 3;</code>
     */
    public Builder setPollingFrequency(int value) {
      
      pollingFrequency_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 pollingFrequency = 3;</code>
     */
    public Builder clearPollingFrequency() {
      
      pollingFrequency_ = 0;
      onChanged();
      return this;
    }

    private long timestampRegistration_ ;
    /**
     * <code>int64 timestampRegistration = 4;</code>
     */
    public long getTimestampRegistration() {
      return timestampRegistration_;
    }
    /**
     * <code>int64 timestampRegistration = 4;</code>
     */
    public Builder setTimestampRegistration(long value) {
      
      timestampRegistration_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 timestampRegistration = 4;</code>
     */
    public Builder clearTimestampRegistration() {
      
      timestampRegistration_ = 0L;
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

