// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http.grpc.beacon;

/**
 * Protobuf type {@code beacon.RequestWrite}
 */
public  final class RequestWrite extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:beacon.RequestWrite)
    RequestWriteOrBuilder {
private static final long serialVersionUID = 0L;
  // Use RequestWrite.newBuilder() to construct.
  private RequestWrite(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private RequestWrite() {
    nodeId_ = "";
    type_ = 0;
    data_ = com.google.protobuf.ByteString.EMPTY;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private RequestWrite(
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

            nodeId_ = s;
            break;
          }
          case 18: {
            org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp.Builder subBuilder = null;
            if (time_ != null) {
              subBuilder = time_.toBuilder();
            }
            time_ = input.readMessage(org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(time_);
              time_ = subBuilder.buildPartial();
            }

            break;
          }
          case 26: {
            org.ar4k.agent.tunnels.http.grpc.beacon.Status.Builder subBuilder = null;
            if (quality_ != null) {
              subBuilder = quality_.toBuilder();
            }
            quality_ = input.readMessage(org.ar4k.agent.tunnels.http.grpc.beacon.Status.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(quality_);
              quality_ = subBuilder.buildPartial();
            }

            break;
          }
          case 32: {
            int rawValue = input.readEnum();

            type_ = rawValue;
            break;
          }
          case 42: {

            data_ = input.readBytes();
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
    return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_RequestWrite_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_RequestWrite_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite.class, org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite.Builder.class);
  }

  public static final int NODEID_FIELD_NUMBER = 1;
  private volatile java.lang.Object nodeId_;
  /**
   * <code>string nodeId = 1;</code>
   */
  public java.lang.String getNodeId() {
    java.lang.Object ref = nodeId_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      nodeId_ = s;
      return s;
    }
  }
  /**
   * <code>string nodeId = 1;</code>
   */
  public com.google.protobuf.ByteString
      getNodeIdBytes() {
    java.lang.Object ref = nodeId_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      nodeId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int TIME_FIELD_NUMBER = 2;
  private org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp time_;
  /**
   * <code>.beacon.Timestamp time = 2;</code>
   */
  public boolean hasTime() {
    return time_ != null;
  }
  /**
   * <code>.beacon.Timestamp time = 2;</code>
   */
  public org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp getTime() {
    return time_ == null ? org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp.getDefaultInstance() : time_;
  }
  /**
   * <code>.beacon.Timestamp time = 2;</code>
   */
  public org.ar4k.agent.tunnels.http.grpc.beacon.TimestampOrBuilder getTimeOrBuilder() {
    return getTime();
  }

  public static final int QUALITY_FIELD_NUMBER = 3;
  private org.ar4k.agent.tunnels.http.grpc.beacon.Status quality_;
  /**
   * <code>.beacon.Status quality = 3;</code>
   */
  public boolean hasQuality() {
    return quality_ != null;
  }
  /**
   * <code>.beacon.Status quality = 3;</code>
   */
  public org.ar4k.agent.tunnels.http.grpc.beacon.Status getQuality() {
    return quality_ == null ? org.ar4k.agent.tunnels.http.grpc.beacon.Status.getDefaultInstance() : quality_;
  }
  /**
   * <code>.beacon.Status quality = 3;</code>
   */
  public org.ar4k.agent.tunnels.http.grpc.beacon.StatusOrBuilder getQualityOrBuilder() {
    return getQuality();
  }

  public static final int TYPE_FIELD_NUMBER = 4;
  private int type_;
  /**
   * <code>.beacon.DataType type = 4;</code>
   */
  public int getTypeValue() {
    return type_;
  }
  /**
   * <code>.beacon.DataType type = 4;</code>
   */
  public org.ar4k.agent.tunnels.http.grpc.beacon.DataType getType() {
    @SuppressWarnings("deprecation")
    org.ar4k.agent.tunnels.http.grpc.beacon.DataType result = org.ar4k.agent.tunnels.http.grpc.beacon.DataType.valueOf(type_);
    return result == null ? org.ar4k.agent.tunnels.http.grpc.beacon.DataType.UNRECOGNIZED : result;
  }

  public static final int DATA_FIELD_NUMBER = 5;
  private com.google.protobuf.ByteString data_;
  /**
   * <code>bytes data = 5;</code>
   */
  public com.google.protobuf.ByteString getData() {
    return data_;
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
    if (!getNodeIdBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, nodeId_);
    }
    if (time_ != null) {
      output.writeMessage(2, getTime());
    }
    if (quality_ != null) {
      output.writeMessage(3, getQuality());
    }
    if (type_ != org.ar4k.agent.tunnels.http.grpc.beacon.DataType.STRING.getNumber()) {
      output.writeEnum(4, type_);
    }
    if (!data_.isEmpty()) {
      output.writeBytes(5, data_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getNodeIdBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, nodeId_);
    }
    if (time_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getTime());
    }
    if (quality_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, getQuality());
    }
    if (type_ != org.ar4k.agent.tunnels.http.grpc.beacon.DataType.STRING.getNumber()) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(4, type_);
    }
    if (!data_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(5, data_);
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
    if (!(obj instanceof org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite)) {
      return super.equals(obj);
    }
    org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite other = (org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite) obj;

    if (!getNodeId()
        .equals(other.getNodeId())) return false;
    if (hasTime() != other.hasTime()) return false;
    if (hasTime()) {
      if (!getTime()
          .equals(other.getTime())) return false;
    }
    if (hasQuality() != other.hasQuality()) return false;
    if (hasQuality()) {
      if (!getQuality()
          .equals(other.getQuality())) return false;
    }
    if (type_ != other.type_) return false;
    if (!getData()
        .equals(other.getData())) return false;
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
    hash = (37 * hash) + NODEID_FIELD_NUMBER;
    hash = (53 * hash) + getNodeId().hashCode();
    if (hasTime()) {
      hash = (37 * hash) + TIME_FIELD_NUMBER;
      hash = (53 * hash) + getTime().hashCode();
    }
    if (hasQuality()) {
      hash = (37 * hash) + QUALITY_FIELD_NUMBER;
      hash = (53 * hash) + getQuality().hashCode();
    }
    hash = (37 * hash) + TYPE_FIELD_NUMBER;
    hash = (53 * hash) + type_;
    hash = (37 * hash) + DATA_FIELD_NUMBER;
    hash = (53 * hash) + getData().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite parseFrom(
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
  public static Builder newBuilder(org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite prototype) {
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
   * Protobuf type {@code beacon.RequestWrite}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:beacon.RequestWrite)
      org.ar4k.agent.tunnels.http.grpc.beacon.RequestWriteOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_RequestWrite_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_RequestWrite_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite.class, org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite.Builder.class);
    }

    // Construct using org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite.newBuilder()
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
      nodeId_ = "";

      if (timeBuilder_ == null) {
        time_ = null;
      } else {
        time_ = null;
        timeBuilder_ = null;
      }
      if (qualityBuilder_ == null) {
        quality_ = null;
      } else {
        quality_ = null;
        qualityBuilder_ = null;
      }
      type_ = 0;

      data_ = com.google.protobuf.ByteString.EMPTY;

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_RequestWrite_descriptor;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite getDefaultInstanceForType() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite.getDefaultInstance();
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite build() {
      org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite buildPartial() {
      org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite result = new org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite(this);
      result.nodeId_ = nodeId_;
      if (timeBuilder_ == null) {
        result.time_ = time_;
      } else {
        result.time_ = timeBuilder_.build();
      }
      if (qualityBuilder_ == null) {
        result.quality_ = quality_;
      } else {
        result.quality_ = qualityBuilder_.build();
      }
      result.type_ = type_;
      result.data_ = data_;
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
      if (other instanceof org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite) {
        return mergeFrom((org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite other) {
      if (other == org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite.getDefaultInstance()) return this;
      if (!other.getNodeId().isEmpty()) {
        nodeId_ = other.nodeId_;
        onChanged();
      }
      if (other.hasTime()) {
        mergeTime(other.getTime());
      }
      if (other.hasQuality()) {
        mergeQuality(other.getQuality());
      }
      if (other.type_ != 0) {
        setTypeValue(other.getTypeValue());
      }
      if (other.getData() != com.google.protobuf.ByteString.EMPTY) {
        setData(other.getData());
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
      org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object nodeId_ = "";
    /**
     * <code>string nodeId = 1;</code>
     */
    public java.lang.String getNodeId() {
      java.lang.Object ref = nodeId_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        nodeId_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string nodeId = 1;</code>
     */
    public com.google.protobuf.ByteString
        getNodeIdBytes() {
      java.lang.Object ref = nodeId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        nodeId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string nodeId = 1;</code>
     */
    public Builder setNodeId(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      nodeId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string nodeId = 1;</code>
     */
    public Builder clearNodeId() {
      
      nodeId_ = getDefaultInstance().getNodeId();
      onChanged();
      return this;
    }
    /**
     * <code>string nodeId = 1;</code>
     */
    public Builder setNodeIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      nodeId_ = value;
      onChanged();
      return this;
    }

    private org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp time_;
    private com.google.protobuf.SingleFieldBuilderV3<
        org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp, org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.TimestampOrBuilder> timeBuilder_;
    /**
     * <code>.beacon.Timestamp time = 2;</code>
     */
    public boolean hasTime() {
      return timeBuilder_ != null || time_ != null;
    }
    /**
     * <code>.beacon.Timestamp time = 2;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp getTime() {
      if (timeBuilder_ == null) {
        return time_ == null ? org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp.getDefaultInstance() : time_;
      } else {
        return timeBuilder_.getMessage();
      }
    }
    /**
     * <code>.beacon.Timestamp time = 2;</code>
     */
    public Builder setTime(org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp value) {
      if (timeBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        time_ = value;
        onChanged();
      } else {
        timeBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.beacon.Timestamp time = 2;</code>
     */
    public Builder setTime(
        org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp.Builder builderForValue) {
      if (timeBuilder_ == null) {
        time_ = builderForValue.build();
        onChanged();
      } else {
        timeBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.beacon.Timestamp time = 2;</code>
     */
    public Builder mergeTime(org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp value) {
      if (timeBuilder_ == null) {
        if (time_ != null) {
          time_ =
            org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp.newBuilder(time_).mergeFrom(value).buildPartial();
        } else {
          time_ = value;
        }
        onChanged();
      } else {
        timeBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.beacon.Timestamp time = 2;</code>
     */
    public Builder clearTime() {
      if (timeBuilder_ == null) {
        time_ = null;
        onChanged();
      } else {
        time_ = null;
        timeBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.beacon.Timestamp time = 2;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp.Builder getTimeBuilder() {
      
      onChanged();
      return getTimeFieldBuilder().getBuilder();
    }
    /**
     * <code>.beacon.Timestamp time = 2;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.TimestampOrBuilder getTimeOrBuilder() {
      if (timeBuilder_ != null) {
        return timeBuilder_.getMessageOrBuilder();
      } else {
        return time_ == null ?
            org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp.getDefaultInstance() : time_;
      }
    }
    /**
     * <code>.beacon.Timestamp time = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp, org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.TimestampOrBuilder> 
        getTimeFieldBuilder() {
      if (timeBuilder_ == null) {
        timeBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp, org.ar4k.agent.tunnels.http.grpc.beacon.Timestamp.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.TimestampOrBuilder>(
                getTime(),
                getParentForChildren(),
                isClean());
        time_ = null;
      }
      return timeBuilder_;
    }

    private org.ar4k.agent.tunnels.http.grpc.beacon.Status quality_;
    private com.google.protobuf.SingleFieldBuilderV3<
        org.ar4k.agent.tunnels.http.grpc.beacon.Status, org.ar4k.agent.tunnels.http.grpc.beacon.Status.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.StatusOrBuilder> qualityBuilder_;
    /**
     * <code>.beacon.Status quality = 3;</code>
     */
    public boolean hasQuality() {
      return qualityBuilder_ != null || quality_ != null;
    }
    /**
     * <code>.beacon.Status quality = 3;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Status getQuality() {
      if (qualityBuilder_ == null) {
        return quality_ == null ? org.ar4k.agent.tunnels.http.grpc.beacon.Status.getDefaultInstance() : quality_;
      } else {
        return qualityBuilder_.getMessage();
      }
    }
    /**
     * <code>.beacon.Status quality = 3;</code>
     */
    public Builder setQuality(org.ar4k.agent.tunnels.http.grpc.beacon.Status value) {
      if (qualityBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        quality_ = value;
        onChanged();
      } else {
        qualityBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.beacon.Status quality = 3;</code>
     */
    public Builder setQuality(
        org.ar4k.agent.tunnels.http.grpc.beacon.Status.Builder builderForValue) {
      if (qualityBuilder_ == null) {
        quality_ = builderForValue.build();
        onChanged();
      } else {
        qualityBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.beacon.Status quality = 3;</code>
     */
    public Builder mergeQuality(org.ar4k.agent.tunnels.http.grpc.beacon.Status value) {
      if (qualityBuilder_ == null) {
        if (quality_ != null) {
          quality_ =
            org.ar4k.agent.tunnels.http.grpc.beacon.Status.newBuilder(quality_).mergeFrom(value).buildPartial();
        } else {
          quality_ = value;
        }
        onChanged();
      } else {
        qualityBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.beacon.Status quality = 3;</code>
     */
    public Builder clearQuality() {
      if (qualityBuilder_ == null) {
        quality_ = null;
        onChanged();
      } else {
        quality_ = null;
        qualityBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.beacon.Status quality = 3;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Status.Builder getQualityBuilder() {
      
      onChanged();
      return getQualityFieldBuilder().getBuilder();
    }
    /**
     * <code>.beacon.Status quality = 3;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.StatusOrBuilder getQualityOrBuilder() {
      if (qualityBuilder_ != null) {
        return qualityBuilder_.getMessageOrBuilder();
      } else {
        return quality_ == null ?
            org.ar4k.agent.tunnels.http.grpc.beacon.Status.getDefaultInstance() : quality_;
      }
    }
    /**
     * <code>.beacon.Status quality = 3;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        org.ar4k.agent.tunnels.http.grpc.beacon.Status, org.ar4k.agent.tunnels.http.grpc.beacon.Status.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.StatusOrBuilder> 
        getQualityFieldBuilder() {
      if (qualityBuilder_ == null) {
        qualityBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            org.ar4k.agent.tunnels.http.grpc.beacon.Status, org.ar4k.agent.tunnels.http.grpc.beacon.Status.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.StatusOrBuilder>(
                getQuality(),
                getParentForChildren(),
                isClean());
        quality_ = null;
      }
      return qualityBuilder_;
    }

    private int type_ = 0;
    /**
     * <code>.beacon.DataType type = 4;</code>
     */
    public int getTypeValue() {
      return type_;
    }
    /**
     * <code>.beacon.DataType type = 4;</code>
     */
    public Builder setTypeValue(int value) {
      type_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>.beacon.DataType type = 4;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.DataType getType() {
      @SuppressWarnings("deprecation")
      org.ar4k.agent.tunnels.http.grpc.beacon.DataType result = org.ar4k.agent.tunnels.http.grpc.beacon.DataType.valueOf(type_);
      return result == null ? org.ar4k.agent.tunnels.http.grpc.beacon.DataType.UNRECOGNIZED : result;
    }
    /**
     * <code>.beacon.DataType type = 4;</code>
     */
    public Builder setType(org.ar4k.agent.tunnels.http.grpc.beacon.DataType value) {
      if (value == null) {
        throw new NullPointerException();
      }
      
      type_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <code>.beacon.DataType type = 4;</code>
     */
    public Builder clearType() {
      
      type_ = 0;
      onChanged();
      return this;
    }

    private com.google.protobuf.ByteString data_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <code>bytes data = 5;</code>
     */
    public com.google.protobuf.ByteString getData() {
      return data_;
    }
    /**
     * <code>bytes data = 5;</code>
     */
    public Builder setData(com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      data_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>bytes data = 5;</code>
     */
    public Builder clearData() {
      
      data_ = getDefaultInstance().getData();
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


    // @@protoc_insertion_point(builder_scope:beacon.RequestWrite)
  }

  // @@protoc_insertion_point(class_scope:beacon.RequestWrite)
  private static final org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite();
  }

  public static org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<RequestWrite>
      PARSER = new com.google.protobuf.AbstractParser<RequestWrite>() {
    @java.lang.Override
    public RequestWrite parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new RequestWrite(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<RequestWrite> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<RequestWrite> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.ar4k.agent.tunnels.http.grpc.beacon.RequestWrite getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

