// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http2.grpc.beacon;

/**
 * Protobuf type {@code beacon.ResponseNetworkChannel}
 */
public  final class ResponseNetworkChannel extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:beacon.ResponseNetworkChannel)
    ResponseNetworkChannelOrBuilder {
private static final long serialVersionUID = 0L;
  // Use ResponseNetworkChannel.newBuilder() to construct.
  private ResponseNetworkChannel(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ResponseNetworkChannel() {
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private ResponseNetworkChannel(
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
          case 8: {

            targeId_ = input.readInt64();
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
    return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_ResponseNetworkChannel_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_ResponseNetworkChannel_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel.class, org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel.Builder.class);
  }

  public static final int TARGEID_FIELD_NUMBER = 1;
  private long targeId_;
  /**
   * <code>int64 targeId = 1;</code>
   */
  public long getTargeId() {
    return targeId_;
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
    if (targeId_ != 0L) {
      output.writeInt64(1, targeId_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (targeId_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(1, targeId_);
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
    if (!(obj instanceof org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel)) {
      return super.equals(obj);
    }
    org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel other = (org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel) obj;

    if (getTargeId()
        != other.getTargeId()) return false;
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
    hash = (37 * hash) + TARGEID_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getTargeId());
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel parseFrom(
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
  public static Builder newBuilder(org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel prototype) {
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
   * Protobuf type {@code beacon.ResponseNetworkChannel}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:beacon.ResponseNetworkChannel)
      org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannelOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_ResponseNetworkChannel_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_ResponseNetworkChannel_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel.class, org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel.Builder.class);
    }

    // Construct using org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel.newBuilder()
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
      targeId_ = 0L;

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_ResponseNetworkChannel_descriptor;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel getDefaultInstanceForType() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel.getDefaultInstance();
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel build() {
      org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel buildPartial() {
      org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel result = new org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel(this);
      result.targeId_ = targeId_;
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
      if (other instanceof org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel) {
        return mergeFrom((org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel other) {
      if (other == org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel.getDefaultInstance()) return this;
      if (other.getTargeId() != 0L) {
        setTargeId(other.getTargeId());
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
      org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private long targeId_ ;
    /**
     * <code>int64 targeId = 1;</code>
     */
    public long getTargeId() {
      return targeId_;
    }
    /**
     * <code>int64 targeId = 1;</code>
     */
    public Builder setTargeId(long value) {
      
      targeId_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int64 targeId = 1;</code>
     */
    public Builder clearTargeId() {
      
      targeId_ = 0L;
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


    // @@protoc_insertion_point(builder_scope:beacon.ResponseNetworkChannel)
  }

  // @@protoc_insertion_point(class_scope:beacon.ResponseNetworkChannel)
  private static final org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel();
  }

  public static org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ResponseNetworkChannel>
      PARSER = new com.google.protobuf.AbstractParser<ResponseNetworkChannel>() {
    @java.lang.Override
    public ResponseNetworkChannel parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new ResponseNetworkChannel(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ResponseNetworkChannel> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ResponseNetworkChannel> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.ar4k.agent.tunnels.http2.grpc.beacon.ResponseNetworkChannel getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

