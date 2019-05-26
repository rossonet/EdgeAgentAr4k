// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http.grpc.beacon;

/**
 * Protobuf type {@code beacon.ListSslAuthoritiesReply}
 */
public  final class ListSslAuthoritiesReply extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:beacon.ListSslAuthoritiesReply)
    ListSslAuthoritiesReplyOrBuilder {
private static final long serialVersionUID = 0L;
  // Use ListSslAuthoritiesReply.newBuilder() to construct.
  private ListSslAuthoritiesReply(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ListSslAuthoritiesReply() {
    authorities_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private ListSslAuthoritiesReply(
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
            org.ar4k.agent.tunnels.http.grpc.beacon.Status.Builder subBuilder = null;
            if (result_ != null) {
              subBuilder = result_.toBuilder();
            }
            result_ = input.readMessage(org.ar4k.agent.tunnels.http.grpc.beacon.Status.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(result_);
              result_ = subBuilder.buildPartial();
            }

            break;
          }
          case 18: {
            if (!((mutable_bitField0_ & 0x00000002) != 0)) {
              authorities_ = new java.util.ArrayList<org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority>();
              mutable_bitField0_ |= 0x00000002;
            }
            authorities_.add(
                input.readMessage(org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority.parser(), extensionRegistry));
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
        authorities_ = java.util.Collections.unmodifiableList(authorities_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_ListSslAuthoritiesReply_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_ListSslAuthoritiesReply_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply.class, org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply.Builder.class);
  }

  private int bitField0_;
  public static final int RESULT_FIELD_NUMBER = 1;
  private org.ar4k.agent.tunnels.http.grpc.beacon.Status result_;
  /**
   * <code>.beacon.Status result = 1;</code>
   */
  public boolean hasResult() {
    return result_ != null;
  }
  /**
   * <code>.beacon.Status result = 1;</code>
   */
  public org.ar4k.agent.tunnels.http.grpc.beacon.Status getResult() {
    return result_ == null ? org.ar4k.agent.tunnels.http.grpc.beacon.Status.getDefaultInstance() : result_;
  }
  /**
   * <code>.beacon.Status result = 1;</code>
   */
  public org.ar4k.agent.tunnels.http.grpc.beacon.StatusOrBuilder getResultOrBuilder() {
    return getResult();
  }

  public static final int AUTHORITIES_FIELD_NUMBER = 2;
  private java.util.List<org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority> authorities_;
  /**
   * <code>repeated .beacon.SslAuthority authorities = 2;</code>
   */
  public java.util.List<org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority> getAuthoritiesList() {
    return authorities_;
  }
  /**
   * <code>repeated .beacon.SslAuthority authorities = 2;</code>
   */
  public java.util.List<? extends org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthorityOrBuilder> 
      getAuthoritiesOrBuilderList() {
    return authorities_;
  }
  /**
   * <code>repeated .beacon.SslAuthority authorities = 2;</code>
   */
  public int getAuthoritiesCount() {
    return authorities_.size();
  }
  /**
   * <code>repeated .beacon.SslAuthority authorities = 2;</code>
   */
  public org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority getAuthorities(int index) {
    return authorities_.get(index);
  }
  /**
   * <code>repeated .beacon.SslAuthority authorities = 2;</code>
   */
  public org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthorityOrBuilder getAuthoritiesOrBuilder(
      int index) {
    return authorities_.get(index);
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
    for (int i = 0; i < authorities_.size(); i++) {
      output.writeMessage(2, authorities_.get(i));
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
    for (int i = 0; i < authorities_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, authorities_.get(i));
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
    if (!(obj instanceof org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply)) {
      return super.equals(obj);
    }
    org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply other = (org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply) obj;

    if (hasResult() != other.hasResult()) return false;
    if (hasResult()) {
      if (!getResult()
          .equals(other.getResult())) return false;
    }
    if (!getAuthoritiesList()
        .equals(other.getAuthoritiesList())) return false;
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
    if (getAuthoritiesCount() > 0) {
      hash = (37 * hash) + AUTHORITIES_FIELD_NUMBER;
      hash = (53 * hash) + getAuthoritiesList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply parseFrom(
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
  public static Builder newBuilder(org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply prototype) {
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
   * Protobuf type {@code beacon.ListSslAuthoritiesReply}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:beacon.ListSslAuthoritiesReply)
      org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReplyOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_ListSslAuthoritiesReply_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_ListSslAuthoritiesReply_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply.class, org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply.Builder.class);
    }

    // Construct using org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply.newBuilder()
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
        getAuthoritiesFieldBuilder();
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
      if (authoritiesBuilder_ == null) {
        authorities_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
      } else {
        authoritiesBuilder_.clear();
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_ListSslAuthoritiesReply_descriptor;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply getDefaultInstanceForType() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply.getDefaultInstance();
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply build() {
      org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply buildPartial() {
      org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply result = new org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (resultBuilder_ == null) {
        result.result_ = result_;
      } else {
        result.result_ = resultBuilder_.build();
      }
      if (authoritiesBuilder_ == null) {
        if (((bitField0_ & 0x00000002) != 0)) {
          authorities_ = java.util.Collections.unmodifiableList(authorities_);
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.authorities_ = authorities_;
      } else {
        result.authorities_ = authoritiesBuilder_.build();
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
      if (other instanceof org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply) {
        return mergeFrom((org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply other) {
      if (other == org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply.getDefaultInstance()) return this;
      if (other.hasResult()) {
        mergeResult(other.getResult());
      }
      if (authoritiesBuilder_ == null) {
        if (!other.authorities_.isEmpty()) {
          if (authorities_.isEmpty()) {
            authorities_ = other.authorities_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensureAuthoritiesIsMutable();
            authorities_.addAll(other.authorities_);
          }
          onChanged();
        }
      } else {
        if (!other.authorities_.isEmpty()) {
          if (authoritiesBuilder_.isEmpty()) {
            authoritiesBuilder_.dispose();
            authoritiesBuilder_ = null;
            authorities_ = other.authorities_;
            bitField0_ = (bitField0_ & ~0x00000002);
            authoritiesBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getAuthoritiesFieldBuilder() : null;
          } else {
            authoritiesBuilder_.addAllMessages(other.authorities_);
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
      org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private org.ar4k.agent.tunnels.http.grpc.beacon.Status result_;
    private com.google.protobuf.SingleFieldBuilderV3<
        org.ar4k.agent.tunnels.http.grpc.beacon.Status, org.ar4k.agent.tunnels.http.grpc.beacon.Status.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.StatusOrBuilder> resultBuilder_;
    /**
     * <code>.beacon.Status result = 1;</code>
     */
    public boolean hasResult() {
      return resultBuilder_ != null || result_ != null;
    }
    /**
     * <code>.beacon.Status result = 1;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Status getResult() {
      if (resultBuilder_ == null) {
        return result_ == null ? org.ar4k.agent.tunnels.http.grpc.beacon.Status.getDefaultInstance() : result_;
      } else {
        return resultBuilder_.getMessage();
      }
    }
    /**
     * <code>.beacon.Status result = 1;</code>
     */
    public Builder setResult(org.ar4k.agent.tunnels.http.grpc.beacon.Status value) {
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
        org.ar4k.agent.tunnels.http.grpc.beacon.Status.Builder builderForValue) {
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
    public Builder mergeResult(org.ar4k.agent.tunnels.http.grpc.beacon.Status value) {
      if (resultBuilder_ == null) {
        if (result_ != null) {
          result_ =
            org.ar4k.agent.tunnels.http.grpc.beacon.Status.newBuilder(result_).mergeFrom(value).buildPartial();
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
    public org.ar4k.agent.tunnels.http.grpc.beacon.Status.Builder getResultBuilder() {
      
      onChanged();
      return getResultFieldBuilder().getBuilder();
    }
    /**
     * <code>.beacon.Status result = 1;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.StatusOrBuilder getResultOrBuilder() {
      if (resultBuilder_ != null) {
        return resultBuilder_.getMessageOrBuilder();
      } else {
        return result_ == null ?
            org.ar4k.agent.tunnels.http.grpc.beacon.Status.getDefaultInstance() : result_;
      }
    }
    /**
     * <code>.beacon.Status result = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        org.ar4k.agent.tunnels.http.grpc.beacon.Status, org.ar4k.agent.tunnels.http.grpc.beacon.Status.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.StatusOrBuilder> 
        getResultFieldBuilder() {
      if (resultBuilder_ == null) {
        resultBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            org.ar4k.agent.tunnels.http.grpc.beacon.Status, org.ar4k.agent.tunnels.http.grpc.beacon.Status.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.StatusOrBuilder>(
                getResult(),
                getParentForChildren(),
                isClean());
        result_ = null;
      }
      return resultBuilder_;
    }

    private java.util.List<org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority> authorities_ =
      java.util.Collections.emptyList();
    private void ensureAuthoritiesIsMutable() {
      if (!((bitField0_ & 0x00000002) != 0)) {
        authorities_ = new java.util.ArrayList<org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority>(authorities_);
        bitField0_ |= 0x00000002;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority, org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthorityOrBuilder> authoritiesBuilder_;

    /**
     * <code>repeated .beacon.SslAuthority authorities = 2;</code>
     */
    public java.util.List<org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority> getAuthoritiesList() {
      if (authoritiesBuilder_ == null) {
        return java.util.Collections.unmodifiableList(authorities_);
      } else {
        return authoritiesBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .beacon.SslAuthority authorities = 2;</code>
     */
    public int getAuthoritiesCount() {
      if (authoritiesBuilder_ == null) {
        return authorities_.size();
      } else {
        return authoritiesBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .beacon.SslAuthority authorities = 2;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority getAuthorities(int index) {
      if (authoritiesBuilder_ == null) {
        return authorities_.get(index);
      } else {
        return authoritiesBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .beacon.SslAuthority authorities = 2;</code>
     */
    public Builder setAuthorities(
        int index, org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority value) {
      if (authoritiesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureAuthoritiesIsMutable();
        authorities_.set(index, value);
        onChanged();
      } else {
        authoritiesBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .beacon.SslAuthority authorities = 2;</code>
     */
    public Builder setAuthorities(
        int index, org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority.Builder builderForValue) {
      if (authoritiesBuilder_ == null) {
        ensureAuthoritiesIsMutable();
        authorities_.set(index, builderForValue.build());
        onChanged();
      } else {
        authoritiesBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .beacon.SslAuthority authorities = 2;</code>
     */
    public Builder addAuthorities(org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority value) {
      if (authoritiesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureAuthoritiesIsMutable();
        authorities_.add(value);
        onChanged();
      } else {
        authoritiesBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .beacon.SslAuthority authorities = 2;</code>
     */
    public Builder addAuthorities(
        int index, org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority value) {
      if (authoritiesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureAuthoritiesIsMutable();
        authorities_.add(index, value);
        onChanged();
      } else {
        authoritiesBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .beacon.SslAuthority authorities = 2;</code>
     */
    public Builder addAuthorities(
        org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority.Builder builderForValue) {
      if (authoritiesBuilder_ == null) {
        ensureAuthoritiesIsMutable();
        authorities_.add(builderForValue.build());
        onChanged();
      } else {
        authoritiesBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .beacon.SslAuthority authorities = 2;</code>
     */
    public Builder addAuthorities(
        int index, org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority.Builder builderForValue) {
      if (authoritiesBuilder_ == null) {
        ensureAuthoritiesIsMutable();
        authorities_.add(index, builderForValue.build());
        onChanged();
      } else {
        authoritiesBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .beacon.SslAuthority authorities = 2;</code>
     */
    public Builder addAllAuthorities(
        java.lang.Iterable<? extends org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority> values) {
      if (authoritiesBuilder_ == null) {
        ensureAuthoritiesIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, authorities_);
        onChanged();
      } else {
        authoritiesBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .beacon.SslAuthority authorities = 2;</code>
     */
    public Builder clearAuthorities() {
      if (authoritiesBuilder_ == null) {
        authorities_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
      } else {
        authoritiesBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .beacon.SslAuthority authorities = 2;</code>
     */
    public Builder removeAuthorities(int index) {
      if (authoritiesBuilder_ == null) {
        ensureAuthoritiesIsMutable();
        authorities_.remove(index);
        onChanged();
      } else {
        authoritiesBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .beacon.SslAuthority authorities = 2;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority.Builder getAuthoritiesBuilder(
        int index) {
      return getAuthoritiesFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .beacon.SslAuthority authorities = 2;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthorityOrBuilder getAuthoritiesOrBuilder(
        int index) {
      if (authoritiesBuilder_ == null) {
        return authorities_.get(index);  } else {
        return authoritiesBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .beacon.SslAuthority authorities = 2;</code>
     */
    public java.util.List<? extends org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthorityOrBuilder> 
         getAuthoritiesOrBuilderList() {
      if (authoritiesBuilder_ != null) {
        return authoritiesBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(authorities_);
      }
    }
    /**
     * <code>repeated .beacon.SslAuthority authorities = 2;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority.Builder addAuthoritiesBuilder() {
      return getAuthoritiesFieldBuilder().addBuilder(
          org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority.getDefaultInstance());
    }
    /**
     * <code>repeated .beacon.SslAuthority authorities = 2;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority.Builder addAuthoritiesBuilder(
        int index) {
      return getAuthoritiesFieldBuilder().addBuilder(
          index, org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority.getDefaultInstance());
    }
    /**
     * <code>repeated .beacon.SslAuthority authorities = 2;</code>
     */
    public java.util.List<org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority.Builder> 
         getAuthoritiesBuilderList() {
      return getAuthoritiesFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority, org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthorityOrBuilder> 
        getAuthoritiesFieldBuilder() {
      if (authoritiesBuilder_ == null) {
        authoritiesBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority, org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthority.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.SslAuthorityOrBuilder>(
                authorities_,
                ((bitField0_ & 0x00000002) != 0),
                getParentForChildren(),
                isClean());
        authorities_ = null;
      }
      return authoritiesBuilder_;
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


    // @@protoc_insertion_point(builder_scope:beacon.ListSslAuthoritiesReply)
  }

  // @@protoc_insertion_point(class_scope:beacon.ListSslAuthoritiesReply)
  private static final org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply();
  }

  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ListSslAuthoritiesReply>
      PARSER = new com.google.protobuf.AbstractParser<ListSslAuthoritiesReply>() {
    @java.lang.Override
    public ListSslAuthoritiesReply parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new ListSslAuthoritiesReply(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ListSslAuthoritiesReply> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ListSslAuthoritiesReply> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.ar4k.agent.tunnels.http.grpc.beacon.ListSslAuthoritiesReply getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

