// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http2.grpc.beacon;

/**
 * Protobuf type {@code beacon.ConfigReply}
 */
public  final class ConfigReply extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:beacon.ConfigReply)
    ConfigReplyOrBuilder {
private static final long serialVersionUID = 0L;
  // Use ConfigReply.newBuilder() to construct.
  private ConfigReply(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ConfigReply() {
    base64Config_ = "";
    jsonConfig_ = "";
    ymlConfig_ = "";
    status_ = 0;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private ConfigReply(
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

            base64Config_ = s;
            break;
          }
          case 18: {
            java.lang.String s = input.readStringRequireUtf8();

            jsonConfig_ = s;
            break;
          }
          case 26: {
            java.lang.String s = input.readStringRequireUtf8();

            ymlConfig_ = s;
            break;
          }
          case 32: {

            restartAt_ = input.readInt64();
            break;
          }
          case 40: {
            int rawValue = input.readEnum();

            status_ = rawValue;
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
    return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_ConfigReply_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_ConfigReply_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply.class, org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply.Builder.class);
  }

  public static final int BASE64CONFIG_FIELD_NUMBER = 1;
  private volatile java.lang.Object base64Config_;
  /**
   * <code>string base64Config = 1;</code>
   */
  public java.lang.String getBase64Config() {
    java.lang.Object ref = base64Config_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      base64Config_ = s;
      return s;
    }
  }
  /**
   * <code>string base64Config = 1;</code>
   */
  public com.google.protobuf.ByteString
      getBase64ConfigBytes() {
    java.lang.Object ref = base64Config_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      base64Config_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int JSONCONFIG_FIELD_NUMBER = 2;
  private volatile java.lang.Object jsonConfig_;
  /**
   * <code>string jsonConfig = 2;</code>
   */
  public java.lang.String getJsonConfig() {
    java.lang.Object ref = jsonConfig_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      jsonConfig_ = s;
      return s;
    }
  }
  /**
   * <code>string jsonConfig = 2;</code>
   */
  public com.google.protobuf.ByteString
      getJsonConfigBytes() {
    java.lang.Object ref = jsonConfig_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      jsonConfig_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int YMLCONFIG_FIELD_NUMBER = 3;
  private volatile java.lang.Object ymlConfig_;
  /**
   * <code>string ymlConfig = 3;</code>
   */
  public java.lang.String getYmlConfig() {
    java.lang.Object ref = ymlConfig_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      ymlConfig_ = s;
      return s;
    }
  }
  /**
   * <code>string ymlConfig = 3;</code>
   */
  public com.google.protobuf.ByteString
      getYmlConfigBytes() {
    java.lang.Object ref = ymlConfig_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      ymlConfig_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int RESTARTAT_FIELD_NUMBER = 4;
  private long restartAt_;
  /**
   * <code>int64 restartAt = 4;</code>
   */
  public long getRestartAt() {
    return restartAt_;
  }

  public static final int STATUS_FIELD_NUMBER = 5;
  private int status_;
  /**
   * <code>.beacon.StatusValue status = 5;</code>
   */
  public int getStatusValue() {
    return status_;
  }
  /**
   * <code>.beacon.StatusValue status = 5;</code>
   */
  public org.ar4k.agent.tunnels.http2.grpc.beacon.StatusValue getStatus() {
    @SuppressWarnings("deprecation")
    org.ar4k.agent.tunnels.http2.grpc.beacon.StatusValue result = org.ar4k.agent.tunnels.http2.grpc.beacon.StatusValue.valueOf(status_);
    return result == null ? org.ar4k.agent.tunnels.http2.grpc.beacon.StatusValue.UNRECOGNIZED : result;
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
    if (!getBase64ConfigBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, base64Config_);
    }
    if (!getJsonConfigBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, jsonConfig_);
    }
    if (!getYmlConfigBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, ymlConfig_);
    }
    if (restartAt_ != 0L) {
      output.writeInt64(4, restartAt_);
    }
    if (status_ != org.ar4k.agent.tunnels.http2.grpc.beacon.StatusValue.GOOD.getNumber()) {
      output.writeEnum(5, status_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getBase64ConfigBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, base64Config_);
    }
    if (!getJsonConfigBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, jsonConfig_);
    }
    if (!getYmlConfigBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, ymlConfig_);
    }
    if (restartAt_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(4, restartAt_);
    }
    if (status_ != org.ar4k.agent.tunnels.http2.grpc.beacon.StatusValue.GOOD.getNumber()) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(5, status_);
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
    if (!(obj instanceof org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply)) {
      return super.equals(obj);
    }
    org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply other = (org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply) obj;

    if (!getBase64Config()
        .equals(other.getBase64Config())) return false;
    if (!getJsonConfig()
        .equals(other.getJsonConfig())) return false;
    if (!getYmlConfig()
        .equals(other.getYmlConfig())) return false;
    if (getRestartAt()
        != other.getRestartAt()) return false;
    if (status_ != other.status_) return false;
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
    hash = (37 * hash) + BASE64CONFIG_FIELD_NUMBER;
    hash = (53 * hash) + getBase64Config().hashCode();
    hash = (37 * hash) + JSONCONFIG_FIELD_NUMBER;
    hash = (53 * hash) + getJsonConfig().hashCode();
    hash = (37 * hash) + YMLCONFIG_FIELD_NUMBER;
    hash = (53 * hash) + getYmlConfig().hashCode();
    hash = (37 * hash) + RESTARTAT_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getRestartAt());
    hash = (37 * hash) + STATUS_FIELD_NUMBER;
    hash = (53 * hash) + status_;
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply parseFrom(
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
  public static Builder newBuilder(org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply prototype) {
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
   * Protobuf type {@code beacon.ConfigReply}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:beacon.ConfigReply)
      org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReplyOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_ConfigReply_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_ConfigReply_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply.class, org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply.Builder.class);
    }

    // Construct using org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply.newBuilder()
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
      base64Config_ = "";

      jsonConfig_ = "";

      ymlConfig_ = "";

      restartAt_ = 0L;

      status_ = 0;

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_ConfigReply_descriptor;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply getDefaultInstanceForType() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply.getDefaultInstance();
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply build() {
      org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply buildPartial() {
      org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply result = new org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply(this);
      result.base64Config_ = base64Config_;
      result.jsonConfig_ = jsonConfig_;
      result.ymlConfig_ = ymlConfig_;
      result.restartAt_ = restartAt_;
      result.status_ = status_;
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
      if (other instanceof org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply) {
        return mergeFrom((org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply other) {
      if (other == org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply.getDefaultInstance()) return this;
      if (!other.getBase64Config().isEmpty()) {
        base64Config_ = other.base64Config_;
        onChanged();
      }
      if (!other.getJsonConfig().isEmpty()) {
        jsonConfig_ = other.jsonConfig_;
        onChanged();
      }
      if (!other.getYmlConfig().isEmpty()) {
        ymlConfig_ = other.ymlConfig_;
        onChanged();
      }
      if (other.getRestartAt() != 0L) {
        setRestartAt(other.getRestartAt());
      }
      if (other.status_ != 0) {
        setStatusValue(other.getStatusValue());
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
      org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object base64Config_ = "";
    /**
     * <code>string base64Config = 1;</code>
     */
    public java.lang.String getBase64Config() {
      java.lang.Object ref = base64Config_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        base64Config_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string base64Config = 1;</code>
     */
    public com.google.protobuf.ByteString
        getBase64ConfigBytes() {
      java.lang.Object ref = base64Config_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        base64Config_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string base64Config = 1;</code>
     */
    public Builder setBase64Config(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      base64Config_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string base64Config = 1;</code>
     */
    public Builder clearBase64Config() {
      
      base64Config_ = getDefaultInstance().getBase64Config();
      onChanged();
      return this;
    }
    /**
     * <code>string base64Config = 1;</code>
     */
    public Builder setBase64ConfigBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      base64Config_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object jsonConfig_ = "";
    /**
     * <code>string jsonConfig = 2;</code>
     */
    public java.lang.String getJsonConfig() {
      java.lang.Object ref = jsonConfig_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        jsonConfig_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string jsonConfig = 2;</code>
     */
    public com.google.protobuf.ByteString
        getJsonConfigBytes() {
      java.lang.Object ref = jsonConfig_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        jsonConfig_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string jsonConfig = 2;</code>
     */
    public Builder setJsonConfig(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      jsonConfig_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string jsonConfig = 2;</code>
     */
    public Builder clearJsonConfig() {
      
      jsonConfig_ = getDefaultInstance().getJsonConfig();
      onChanged();
      return this;
    }
    /**
     * <code>string jsonConfig = 2;</code>
     */
    public Builder setJsonConfigBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      jsonConfig_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object ymlConfig_ = "";
    /**
     * <code>string ymlConfig = 3;</code>
     */
    public java.lang.String getYmlConfig() {
      java.lang.Object ref = ymlConfig_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        ymlConfig_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string ymlConfig = 3;</code>
     */
    public com.google.protobuf.ByteString
        getYmlConfigBytes() {
      java.lang.Object ref = ymlConfig_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        ymlConfig_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string ymlConfig = 3;</code>
     */
    public Builder setYmlConfig(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      ymlConfig_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string ymlConfig = 3;</code>
     */
    public Builder clearYmlConfig() {
      
      ymlConfig_ = getDefaultInstance().getYmlConfig();
      onChanged();
      return this;
    }
    /**
     * <code>string ymlConfig = 3;</code>
     */
    public Builder setYmlConfigBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      ymlConfig_ = value;
      onChanged();
      return this;
    }

    private long restartAt_ ;
    /**
     * <code>int64 restartAt = 4;</code>
     */
    public long getRestartAt() {
      return restartAt_;
    }
    /**
     * <code>int64 restartAt = 4;</code>
     */
    public Builder setRestartAt(long value) {
      
      restartAt_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 restartAt = 4;</code>
     */
    public Builder clearRestartAt() {
      
      restartAt_ = 0L;
      onChanged();
      return this;
    }

    private int status_ = 0;
    /**
     * <code>.beacon.StatusValue status = 5;</code>
     */
    public int getStatusValue() {
      return status_;
    }
    /**
     * <code>.beacon.StatusValue status = 5;</code>
     */
    public Builder setStatusValue(int value) {
      status_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>.beacon.StatusValue status = 5;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.StatusValue getStatus() {
      @SuppressWarnings("deprecation")
      org.ar4k.agent.tunnels.http2.grpc.beacon.StatusValue result = org.ar4k.agent.tunnels.http2.grpc.beacon.StatusValue.valueOf(status_);
      return result == null ? org.ar4k.agent.tunnels.http2.grpc.beacon.StatusValue.UNRECOGNIZED : result;
    }
    /**
     * <code>.beacon.StatusValue status = 5;</code>
     */
    public Builder setStatus(org.ar4k.agent.tunnels.http2.grpc.beacon.StatusValue value) {
      if (value == null) {
        throw new NullPointerException();
      }
      
      status_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <code>.beacon.StatusValue status = 5;</code>
     */
    public Builder clearStatus() {
      
      status_ = 0;
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


    // @@protoc_insertion_point(builder_scope:beacon.ConfigReply)
  }

  // @@protoc_insertion_point(class_scope:beacon.ConfigReply)
  private static final org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply();
  }

  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ConfigReply>
      PARSER = new com.google.protobuf.AbstractParser<ConfigReply>() {
    @java.lang.Override
    public ConfigReply parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new ConfigReply(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ConfigReply> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ConfigReply> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.ar4k.agent.tunnels.http2.grpc.beacon.ConfigReply getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

