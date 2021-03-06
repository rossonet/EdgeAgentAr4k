// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http2.grpc.beacon;

/**
 * Protobuf type {@code beacon.RegisterReply}
 */
public  final class RegisterReply extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:beacon.RegisterReply)
    RegisterReplyOrBuilder {
private static final long serialVersionUID = 0L;
  // Use RegisterReply.newBuilder() to construct.
  private RegisterReply(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private RegisterReply() {
    registerCode_ = "";
    cert_ = "";
    ca_ = "";
    otpSeed_ = "";
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private RegisterReply(
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

            registerCode_ = s;
            break;
          }
          case 16: {

            monitoringFrequency_ = input.readInt32();
            break;
          }
          case 26: {
            java.lang.String s = input.readStringRequireUtf8();

            cert_ = s;
            break;
          }
          case 34: {
            java.lang.String s = input.readStringRequireUtf8();

            ca_ = s;
            break;
          }
          case 42: {
            org.ar4k.agent.tunnels.http2.grpc.beacon.Status.Builder subBuilder = null;
            if (statusRegistration_ != null) {
              subBuilder = statusRegistration_.toBuilder();
            }
            statusRegistration_ = input.readMessage(org.ar4k.agent.tunnels.http2.grpc.beacon.Status.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(statusRegistration_);
              statusRegistration_ = subBuilder.buildPartial();
            }

            break;
          }
          case 48: {

            timestampRegistration_ = input.readInt64();
            break;
          }
          case 58: {
            java.lang.String s = input.readStringRequireUtf8();

            otpSeed_ = s;
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
    return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_RegisterReply_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_RegisterReply_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply.class, org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply.Builder.class);
  }

  public static final int REGISTERCODE_FIELD_NUMBER = 1;
  private volatile java.lang.Object registerCode_;
  /**
   * <code>string registerCode = 1;</code>
   */
  public java.lang.String getRegisterCode() {
    java.lang.Object ref = registerCode_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      registerCode_ = s;
      return s;
    }
  }
  /**
   * <code>string registerCode = 1;</code>
   */
  public com.google.protobuf.ByteString
      getRegisterCodeBytes() {
    java.lang.Object ref = registerCode_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      registerCode_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int MONITORINGFREQUENCY_FIELD_NUMBER = 2;
  private int monitoringFrequency_;
  /**
   * <code>int32 monitoringFrequency = 2;</code>
   */
  public int getMonitoringFrequency() {
    return monitoringFrequency_;
  }

  public static final int CERT_FIELD_NUMBER = 3;
  private volatile java.lang.Object cert_;
  /**
   * <code>string cert = 3;</code>
   */
  public java.lang.String getCert() {
    java.lang.Object ref = cert_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      cert_ = s;
      return s;
    }
  }
  /**
   * <code>string cert = 3;</code>
   */
  public com.google.protobuf.ByteString
      getCertBytes() {
    java.lang.Object ref = cert_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      cert_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int CA_FIELD_NUMBER = 4;
  private volatile java.lang.Object ca_;
  /**
   * <code>string ca = 4;</code>
   */
  public java.lang.String getCa() {
    java.lang.Object ref = ca_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      ca_ = s;
      return s;
    }
  }
  /**
   * <code>string ca = 4;</code>
   */
  public com.google.protobuf.ByteString
      getCaBytes() {
    java.lang.Object ref = ca_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      ca_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int STATUSREGISTRATION_FIELD_NUMBER = 5;
  private org.ar4k.agent.tunnels.http2.grpc.beacon.Status statusRegistration_;
  /**
   * <code>.beacon.Status statusRegistration = 5;</code>
   */
  public boolean hasStatusRegistration() {
    return statusRegistration_ != null;
  }
  /**
   * <code>.beacon.Status statusRegistration = 5;</code>
   */
  public org.ar4k.agent.tunnels.http2.grpc.beacon.Status getStatusRegistration() {
    return statusRegistration_ == null ? org.ar4k.agent.tunnels.http2.grpc.beacon.Status.getDefaultInstance() : statusRegistration_;
  }
  /**
   * <code>.beacon.Status statusRegistration = 5;</code>
   */
  public org.ar4k.agent.tunnels.http2.grpc.beacon.StatusOrBuilder getStatusRegistrationOrBuilder() {
    return getStatusRegistration();
  }

  public static final int TIMESTAMPREGISTRATION_FIELD_NUMBER = 6;
  private long timestampRegistration_;
  /**
   * <code>int64 timestampRegistration = 6;</code>
   */
  public long getTimestampRegistration() {
    return timestampRegistration_;
  }

  public static final int OTPSEED_FIELD_NUMBER = 7;
  private volatile java.lang.Object otpSeed_;
  /**
   * <code>string otpSeed = 7;</code>
   */
  public java.lang.String getOtpSeed() {
    java.lang.Object ref = otpSeed_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      otpSeed_ = s;
      return s;
    }
  }
  /**
   * <code>string otpSeed = 7;</code>
   */
  public com.google.protobuf.ByteString
      getOtpSeedBytes() {
    java.lang.Object ref = otpSeed_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      otpSeed_ = b;
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
    if (!getRegisterCodeBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, registerCode_);
    }
    if (monitoringFrequency_ != 0) {
      output.writeInt32(2, monitoringFrequency_);
    }
    if (!getCertBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, cert_);
    }
    if (!getCaBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 4, ca_);
    }
    if (statusRegistration_ != null) {
      output.writeMessage(5, getStatusRegistration());
    }
    if (timestampRegistration_ != 0L) {
      output.writeInt64(6, timestampRegistration_);
    }
    if (!getOtpSeedBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 7, otpSeed_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getRegisterCodeBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, registerCode_);
    }
    if (monitoringFrequency_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, monitoringFrequency_);
    }
    if (!getCertBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, cert_);
    }
    if (!getCaBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, ca_);
    }
    if (statusRegistration_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(5, getStatusRegistration());
    }
    if (timestampRegistration_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(6, timestampRegistration_);
    }
    if (!getOtpSeedBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(7, otpSeed_);
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
    if (!(obj instanceof org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply)) {
      return super.equals(obj);
    }
    org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply other = (org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply) obj;

    if (!getRegisterCode()
        .equals(other.getRegisterCode())) return false;
    if (getMonitoringFrequency()
        != other.getMonitoringFrequency()) return false;
    if (!getCert()
        .equals(other.getCert())) return false;
    if (!getCa()
        .equals(other.getCa())) return false;
    if (hasStatusRegistration() != other.hasStatusRegistration()) return false;
    if (hasStatusRegistration()) {
      if (!getStatusRegistration()
          .equals(other.getStatusRegistration())) return false;
    }
    if (getTimestampRegistration()
        != other.getTimestampRegistration()) return false;
    if (!getOtpSeed()
        .equals(other.getOtpSeed())) return false;
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
    hash = (37 * hash) + REGISTERCODE_FIELD_NUMBER;
    hash = (53 * hash) + getRegisterCode().hashCode();
    hash = (37 * hash) + MONITORINGFREQUENCY_FIELD_NUMBER;
    hash = (53 * hash) + getMonitoringFrequency();
    hash = (37 * hash) + CERT_FIELD_NUMBER;
    hash = (53 * hash) + getCert().hashCode();
    hash = (37 * hash) + CA_FIELD_NUMBER;
    hash = (53 * hash) + getCa().hashCode();
    if (hasStatusRegistration()) {
      hash = (37 * hash) + STATUSREGISTRATION_FIELD_NUMBER;
      hash = (53 * hash) + getStatusRegistration().hashCode();
    }
    hash = (37 * hash) + TIMESTAMPREGISTRATION_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getTimestampRegistration());
    hash = (37 * hash) + OTPSEED_FIELD_NUMBER;
    hash = (53 * hash) + getOtpSeed().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply parseFrom(
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
  public static Builder newBuilder(org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply prototype) {
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
   * Protobuf type {@code beacon.RegisterReply}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:beacon.RegisterReply)
      org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReplyOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_RegisterReply_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_RegisterReply_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply.class, org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply.Builder.class);
    }

    // Construct using org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply.newBuilder()
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
      registerCode_ = "";

      monitoringFrequency_ = 0;

      cert_ = "";

      ca_ = "";

      if (statusRegistrationBuilder_ == null) {
        statusRegistration_ = null;
      } else {
        statusRegistration_ = null;
        statusRegistrationBuilder_ = null;
      }
      timestampRegistration_ = 0L;

      otpSeed_ = "";

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_RegisterReply_descriptor;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply getDefaultInstanceForType() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply.getDefaultInstance();
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply build() {
      org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply buildPartial() {
      org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply result = new org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply(this);
      result.registerCode_ = registerCode_;
      result.monitoringFrequency_ = monitoringFrequency_;
      result.cert_ = cert_;
      result.ca_ = ca_;
      if (statusRegistrationBuilder_ == null) {
        result.statusRegistration_ = statusRegistration_;
      } else {
        result.statusRegistration_ = statusRegistrationBuilder_.build();
      }
      result.timestampRegistration_ = timestampRegistration_;
      result.otpSeed_ = otpSeed_;
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
      if (other instanceof org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply) {
        return mergeFrom((org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply other) {
      if (other == org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply.getDefaultInstance()) return this;
      if (!other.getRegisterCode().isEmpty()) {
        registerCode_ = other.registerCode_;
        onChanged();
      }
      if (other.getMonitoringFrequency() != 0) {
        setMonitoringFrequency(other.getMonitoringFrequency());
      }
      if (!other.getCert().isEmpty()) {
        cert_ = other.cert_;
        onChanged();
      }
      if (!other.getCa().isEmpty()) {
        ca_ = other.ca_;
        onChanged();
      }
      if (other.hasStatusRegistration()) {
        mergeStatusRegistration(other.getStatusRegistration());
      }
      if (other.getTimestampRegistration() != 0L) {
        setTimestampRegistration(other.getTimestampRegistration());
      }
      if (!other.getOtpSeed().isEmpty()) {
        otpSeed_ = other.otpSeed_;
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
      org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object registerCode_ = "";
    /**
     * <code>string registerCode = 1;</code>
     */
    public java.lang.String getRegisterCode() {
      java.lang.Object ref = registerCode_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        registerCode_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string registerCode = 1;</code>
     */
    public com.google.protobuf.ByteString
        getRegisterCodeBytes() {
      java.lang.Object ref = registerCode_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        registerCode_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string registerCode = 1;</code>
     */
    public Builder setRegisterCode(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      registerCode_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string registerCode = 1;</code>
     */
    public Builder clearRegisterCode() {
      
      registerCode_ = getDefaultInstance().getRegisterCode();
      onChanged();
      return this;
    }
    /**
     * <code>string registerCode = 1;</code>
     */
    public Builder setRegisterCodeBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      registerCode_ = value;
      onChanged();
      return this;
    }

    private int monitoringFrequency_ ;
    /**
     * <code>int32 monitoringFrequency = 2;</code>
     */
    public int getMonitoringFrequency() {
      return monitoringFrequency_;
    }
    /**
     * <code>int32 monitoringFrequency = 2;</code>
     */
    public Builder setMonitoringFrequency(int value) {
      
      monitoringFrequency_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 monitoringFrequency = 2;</code>
     */
    public Builder clearMonitoringFrequency() {
      
      monitoringFrequency_ = 0;
      onChanged();
      return this;
    }

    private java.lang.Object cert_ = "";
    /**
     * <code>string cert = 3;</code>
     */
    public java.lang.String getCert() {
      java.lang.Object ref = cert_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        cert_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string cert = 3;</code>
     */
    public com.google.protobuf.ByteString
        getCertBytes() {
      java.lang.Object ref = cert_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        cert_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string cert = 3;</code>
     */
    public Builder setCert(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      cert_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string cert = 3;</code>
     */
    public Builder clearCert() {
      
      cert_ = getDefaultInstance().getCert();
      onChanged();
      return this;
    }
    /**
     * <code>string cert = 3;</code>
     */
    public Builder setCertBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      cert_ = value;
      onChanged();
      return this;
    }

    private java.lang.Object ca_ = "";
    /**
     * <code>string ca = 4;</code>
     */
    public java.lang.String getCa() {
      java.lang.Object ref = ca_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        ca_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string ca = 4;</code>
     */
    public com.google.protobuf.ByteString
        getCaBytes() {
      java.lang.Object ref = ca_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        ca_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string ca = 4;</code>
     */
    public Builder setCa(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      ca_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string ca = 4;</code>
     */
    public Builder clearCa() {
      
      ca_ = getDefaultInstance().getCa();
      onChanged();
      return this;
    }
    /**
     * <code>string ca = 4;</code>
     */
    public Builder setCaBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      ca_ = value;
      onChanged();
      return this;
    }

    private org.ar4k.agent.tunnels.http2.grpc.beacon.Status statusRegistration_;
    private com.google.protobuf.SingleFieldBuilderV3<
        org.ar4k.agent.tunnels.http2.grpc.beacon.Status, org.ar4k.agent.tunnels.http2.grpc.beacon.Status.Builder, org.ar4k.agent.tunnels.http2.grpc.beacon.StatusOrBuilder> statusRegistrationBuilder_;
    /**
     * <code>.beacon.Status statusRegistration = 5;</code>
     */
    public boolean hasStatusRegistration() {
      return statusRegistrationBuilder_ != null || statusRegistration_ != null;
    }
    /**
     * <code>.beacon.Status statusRegistration = 5;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.Status getStatusRegistration() {
      if (statusRegistrationBuilder_ == null) {
        return statusRegistration_ == null ? org.ar4k.agent.tunnels.http2.grpc.beacon.Status.getDefaultInstance() : statusRegistration_;
      } else {
        return statusRegistrationBuilder_.getMessage();
      }
    }
    /**
     * <code>.beacon.Status statusRegistration = 5;</code>
     */
    public Builder setStatusRegistration(org.ar4k.agent.tunnels.http2.grpc.beacon.Status value) {
      if (statusRegistrationBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        statusRegistration_ = value;
        onChanged();
      } else {
        statusRegistrationBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.beacon.Status statusRegistration = 5;</code>
     */
    public Builder setStatusRegistration(
        org.ar4k.agent.tunnels.http2.grpc.beacon.Status.Builder builderForValue) {
      if (statusRegistrationBuilder_ == null) {
        statusRegistration_ = builderForValue.build();
        onChanged();
      } else {
        statusRegistrationBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.beacon.Status statusRegistration = 5;</code>
     */
    public Builder mergeStatusRegistration(org.ar4k.agent.tunnels.http2.grpc.beacon.Status value) {
      if (statusRegistrationBuilder_ == null) {
        if (statusRegistration_ != null) {
          statusRegistration_ =
            org.ar4k.agent.tunnels.http2.grpc.beacon.Status.newBuilder(statusRegistration_).mergeFrom(value).buildPartial();
        } else {
          statusRegistration_ = value;
        }
        onChanged();
      } else {
        statusRegistrationBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.beacon.Status statusRegistration = 5;</code>
     */
    public Builder clearStatusRegistration() {
      if (statusRegistrationBuilder_ == null) {
        statusRegistration_ = null;
        onChanged();
      } else {
        statusRegistration_ = null;
        statusRegistrationBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.beacon.Status statusRegistration = 5;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.Status.Builder getStatusRegistrationBuilder() {
      
      onChanged();
      return getStatusRegistrationFieldBuilder().getBuilder();
    }
    /**
     * <code>.beacon.Status statusRegistration = 5;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.StatusOrBuilder getStatusRegistrationOrBuilder() {
      if (statusRegistrationBuilder_ != null) {
        return statusRegistrationBuilder_.getMessageOrBuilder();
      } else {
        return statusRegistration_ == null ?
            org.ar4k.agent.tunnels.http2.grpc.beacon.Status.getDefaultInstance() : statusRegistration_;
      }
    }
    /**
     * <code>.beacon.Status statusRegistration = 5;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        org.ar4k.agent.tunnels.http2.grpc.beacon.Status, org.ar4k.agent.tunnels.http2.grpc.beacon.Status.Builder, org.ar4k.agent.tunnels.http2.grpc.beacon.StatusOrBuilder> 
        getStatusRegistrationFieldBuilder() {
      if (statusRegistrationBuilder_ == null) {
        statusRegistrationBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            org.ar4k.agent.tunnels.http2.grpc.beacon.Status, org.ar4k.agent.tunnels.http2.grpc.beacon.Status.Builder, org.ar4k.agent.tunnels.http2.grpc.beacon.StatusOrBuilder>(
                getStatusRegistration(),
                getParentForChildren(),
                isClean());
        statusRegistration_ = null;
      }
      return statusRegistrationBuilder_;
    }

    private long timestampRegistration_ ;
    /**
     * <code>int64 timestampRegistration = 6;</code>
     */
    public long getTimestampRegistration() {
      return timestampRegistration_;
    }
    /**
     * <code>int64 timestampRegistration = 6;</code>
     */
    public Builder setTimestampRegistration(long value) {
      
      timestampRegistration_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 timestampRegistration = 6;</code>
     */
    public Builder clearTimestampRegistration() {
      
      timestampRegistration_ = 0L;
      onChanged();
      return this;
    }

    private java.lang.Object otpSeed_ = "";
    /**
     * <code>string otpSeed = 7;</code>
     */
    public java.lang.String getOtpSeed() {
      java.lang.Object ref = otpSeed_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        otpSeed_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string otpSeed = 7;</code>
     */
    public com.google.protobuf.ByteString
        getOtpSeedBytes() {
      java.lang.Object ref = otpSeed_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        otpSeed_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string otpSeed = 7;</code>
     */
    public Builder setOtpSeed(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      otpSeed_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string otpSeed = 7;</code>
     */
    public Builder clearOtpSeed() {
      
      otpSeed_ = getDefaultInstance().getOtpSeed();
      onChanged();
      return this;
    }
    /**
     * <code>string otpSeed = 7;</code>
     */
    public Builder setOtpSeedBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      otpSeed_ = value;
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


    // @@protoc_insertion_point(builder_scope:beacon.RegisterReply)
  }

  // @@protoc_insertion_point(class_scope:beacon.RegisterReply)
  private static final org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply();
  }

  public static org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<RegisterReply>
      PARSER = new com.google.protobuf.AbstractParser<RegisterReply>() {
    @java.lang.Override
    public RegisterReply parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new RegisterReply(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<RegisterReply> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<RegisterReply> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

