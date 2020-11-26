// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http2.grpc.beacon;

/**
 * Protobuf type {@code beacon.FlowMessageData}
 */
public  final class FlowMessageData extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:beacon.FlowMessageData)
    FlowMessageDataOrBuilder {
private static final long serialVersionUID = 0L;
  // Use FlowMessageData.newBuilder() to construct.
  private FlowMessageData(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private FlowMessageData() {
    values_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private FlowMessageData(
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
            if (!((mutable_bitField0_ & 0x00000001) != 0)) {
              values_ = new java.util.ArrayList<org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode>();
              mutable_bitField0_ |= 0x00000001;
            }
            values_.add(
                input.readMessage(org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode.parser(), extensionRegistry));
            break;
          }
          case 16: {

            hiSpeedRoutingTag_ = input.readInt32();
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
      if (((mutable_bitField0_ & 0x00000001) != 0)) {
        values_ = java.util.Collections.unmodifiableList(values_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_FlowMessageData_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_FlowMessageData_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData.class, org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData.Builder.class);
  }

  private int bitField0_;
  public static final int VALUES_FIELD_NUMBER = 1;
  private java.util.List<org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode> values_;
  /**
   * <code>repeated .beacon.DataNode values = 1;</code>
   */
  public java.util.List<org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode> getValuesList() {
    return values_;
  }
  /**
   * <code>repeated .beacon.DataNode values = 1;</code>
   */
  public java.util.List<? extends org.ar4k.agent.tunnels.http2.grpc.beacon.DataNodeOrBuilder> 
      getValuesOrBuilderList() {
    return values_;
  }
  /**
   * <code>repeated .beacon.DataNode values = 1;</code>
   */
  public int getValuesCount() {
    return values_.size();
  }
  /**
   * <code>repeated .beacon.DataNode values = 1;</code>
   */
  public org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode getValues(int index) {
    return values_.get(index);
  }
  /**
   * <code>repeated .beacon.DataNode values = 1;</code>
   */
  public org.ar4k.agent.tunnels.http2.grpc.beacon.DataNodeOrBuilder getValuesOrBuilder(
      int index) {
    return values_.get(index);
  }

  public static final int HISPEEDROUTINGTAG_FIELD_NUMBER = 2;
  private int hiSpeedRoutingTag_;
  /**
   * <code>int32 hiSpeedRoutingTag = 2;</code>
   */
  public int getHiSpeedRoutingTag() {
    return hiSpeedRoutingTag_;
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
    for (int i = 0; i < values_.size(); i++) {
      output.writeMessage(1, values_.get(i));
    }
    if (hiSpeedRoutingTag_ != 0) {
      output.writeInt32(2, hiSpeedRoutingTag_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < values_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, values_.get(i));
    }
    if (hiSpeedRoutingTag_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, hiSpeedRoutingTag_);
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
    if (!(obj instanceof org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData)) {
      return super.equals(obj);
    }
    org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData other = (org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData) obj;

    if (!getValuesList()
        .equals(other.getValuesList())) return false;
    if (getHiSpeedRoutingTag()
        != other.getHiSpeedRoutingTag()) return false;
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
    if (getValuesCount() > 0) {
      hash = (37 * hash) + VALUES_FIELD_NUMBER;
      hash = (53 * hash) + getValuesList().hashCode();
    }
    hash = (37 * hash) + HISPEEDROUTINGTAG_FIELD_NUMBER;
    hash = (53 * hash) + getHiSpeedRoutingTag();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData parseFrom(
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
  public static Builder newBuilder(org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData prototype) {
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
   * Protobuf type {@code beacon.FlowMessageData}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:beacon.FlowMessageData)
      org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageDataOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_FlowMessageData_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_FlowMessageData_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData.class, org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData.Builder.class);
    }

    // Construct using org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData.newBuilder()
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
        getValuesFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (valuesBuilder_ == null) {
        values_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        valuesBuilder_.clear();
      }
      hiSpeedRoutingTag_ = 0;

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.BeaconMirrorService.internal_static_beacon_FlowMessageData_descriptor;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData getDefaultInstanceForType() {
      return org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData.getDefaultInstance();
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData build() {
      org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData buildPartial() {
      org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData result = new org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (valuesBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          values_ = java.util.Collections.unmodifiableList(values_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.values_ = values_;
      } else {
        result.values_ = valuesBuilder_.build();
      }
      result.hiSpeedRoutingTag_ = hiSpeedRoutingTag_;
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
      if (other instanceof org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData) {
        return mergeFrom((org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData other) {
      if (other == org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData.getDefaultInstance()) return this;
      if (valuesBuilder_ == null) {
        if (!other.values_.isEmpty()) {
          if (values_.isEmpty()) {
            values_ = other.values_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureValuesIsMutable();
            values_.addAll(other.values_);
          }
          onChanged();
        }
      } else {
        if (!other.values_.isEmpty()) {
          if (valuesBuilder_.isEmpty()) {
            valuesBuilder_.dispose();
            valuesBuilder_ = null;
            values_ = other.values_;
            bitField0_ = (bitField0_ & ~0x00000001);
            valuesBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getValuesFieldBuilder() : null;
          } else {
            valuesBuilder_.addAllMessages(other.values_);
          }
        }
      }
      if (other.getHiSpeedRoutingTag() != 0) {
        setHiSpeedRoutingTag(other.getHiSpeedRoutingTag());
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
      org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.util.List<org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode> values_ =
      java.util.Collections.emptyList();
    private void ensureValuesIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        values_ = new java.util.ArrayList<org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode>(values_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode, org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode.Builder, org.ar4k.agent.tunnels.http2.grpc.beacon.DataNodeOrBuilder> valuesBuilder_;

    /**
     * <code>repeated .beacon.DataNode values = 1;</code>
     */
    public java.util.List<org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode> getValuesList() {
      if (valuesBuilder_ == null) {
        return java.util.Collections.unmodifiableList(values_);
      } else {
        return valuesBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .beacon.DataNode values = 1;</code>
     */
    public int getValuesCount() {
      if (valuesBuilder_ == null) {
        return values_.size();
      } else {
        return valuesBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .beacon.DataNode values = 1;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode getValues(int index) {
      if (valuesBuilder_ == null) {
        return values_.get(index);
      } else {
        return valuesBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .beacon.DataNode values = 1;</code>
     */
    public Builder setValues(
        int index, org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode value) {
      if (valuesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureValuesIsMutable();
        values_.set(index, value);
        onChanged();
      } else {
        valuesBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .beacon.DataNode values = 1;</code>
     */
    public Builder setValues(
        int index, org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode.Builder builderForValue) {
      if (valuesBuilder_ == null) {
        ensureValuesIsMutable();
        values_.set(index, builderForValue.build());
        onChanged();
      } else {
        valuesBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .beacon.DataNode values = 1;</code>
     */
    public Builder addValues(org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode value) {
      if (valuesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureValuesIsMutable();
        values_.add(value);
        onChanged();
      } else {
        valuesBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .beacon.DataNode values = 1;</code>
     */
    public Builder addValues(
        int index, org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode value) {
      if (valuesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureValuesIsMutable();
        values_.add(index, value);
        onChanged();
      } else {
        valuesBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .beacon.DataNode values = 1;</code>
     */
    public Builder addValues(
        org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode.Builder builderForValue) {
      if (valuesBuilder_ == null) {
        ensureValuesIsMutable();
        values_.add(builderForValue.build());
        onChanged();
      } else {
        valuesBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .beacon.DataNode values = 1;</code>
     */
    public Builder addValues(
        int index, org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode.Builder builderForValue) {
      if (valuesBuilder_ == null) {
        ensureValuesIsMutable();
        values_.add(index, builderForValue.build());
        onChanged();
      } else {
        valuesBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .beacon.DataNode values = 1;</code>
     */
    public Builder addAllValues(
        java.lang.Iterable<? extends org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode> values) {
      if (valuesBuilder_ == null) {
        ensureValuesIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, values_);
        onChanged();
      } else {
        valuesBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .beacon.DataNode values = 1;</code>
     */
    public Builder clearValues() {
      if (valuesBuilder_ == null) {
        values_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        valuesBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .beacon.DataNode values = 1;</code>
     */
    public Builder removeValues(int index) {
      if (valuesBuilder_ == null) {
        ensureValuesIsMutable();
        values_.remove(index);
        onChanged();
      } else {
        valuesBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .beacon.DataNode values = 1;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode.Builder getValuesBuilder(
        int index) {
      return getValuesFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .beacon.DataNode values = 1;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.DataNodeOrBuilder getValuesOrBuilder(
        int index) {
      if (valuesBuilder_ == null) {
        return values_.get(index);  } else {
        return valuesBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .beacon.DataNode values = 1;</code>
     */
    public java.util.List<? extends org.ar4k.agent.tunnels.http2.grpc.beacon.DataNodeOrBuilder> 
         getValuesOrBuilderList() {
      if (valuesBuilder_ != null) {
        return valuesBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(values_);
      }
    }
    /**
     * <code>repeated .beacon.DataNode values = 1;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode.Builder addValuesBuilder() {
      return getValuesFieldBuilder().addBuilder(
          org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode.getDefaultInstance());
    }
    /**
     * <code>repeated .beacon.DataNode values = 1;</code>
     */
    public org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode.Builder addValuesBuilder(
        int index) {
      return getValuesFieldBuilder().addBuilder(
          index, org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode.getDefaultInstance());
    }
    /**
     * <code>repeated .beacon.DataNode values = 1;</code>
     */
    public java.util.List<org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode.Builder> 
         getValuesBuilderList() {
      return getValuesFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode, org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode.Builder, org.ar4k.agent.tunnels.http2.grpc.beacon.DataNodeOrBuilder> 
        getValuesFieldBuilder() {
      if (valuesBuilder_ == null) {
        valuesBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode, org.ar4k.agent.tunnels.http2.grpc.beacon.DataNode.Builder, org.ar4k.agent.tunnels.http2.grpc.beacon.DataNodeOrBuilder>(
                values_,
                ((bitField0_ & 0x00000001) != 0),
                getParentForChildren(),
                isClean());
        values_ = null;
      }
      return valuesBuilder_;
    }

    private int hiSpeedRoutingTag_ ;
    /**
     * <code>int32 hiSpeedRoutingTag = 2;</code>
     */
    public int getHiSpeedRoutingTag() {
      return hiSpeedRoutingTag_;
    }
    /**
     * <code>int32 hiSpeedRoutingTag = 2;</code>
     */
    public Builder setHiSpeedRoutingTag(int value) {
      
      hiSpeedRoutingTag_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 hiSpeedRoutingTag = 2;</code>
     */
    public Builder clearHiSpeedRoutingTag() {
      
      hiSpeedRoutingTag_ = 0;
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


    // @@protoc_insertion_point(builder_scope:beacon.FlowMessageData)
  }

  // @@protoc_insertion_point(class_scope:beacon.FlowMessageData)
  private static final org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData();
  }

  public static org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<FlowMessageData>
      PARSER = new com.google.protobuf.AbstractParser<FlowMessageData>() {
    @java.lang.Override
    public FlowMessageData parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new FlowMessageData(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<FlowMessageData> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<FlowMessageData> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.ar4k.agent.tunnels.http2.grpc.beacon.FlowMessageData getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

