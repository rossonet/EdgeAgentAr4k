// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http.grpc.beacon;

/**
 * Protobuf type {@code beacon.ListCommandsReply}
 */
public  final class ListCommandsReply extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:beacon.ListCommandsReply)
    ListCommandsReplyOrBuilder {
private static final long serialVersionUID = 0L;
  // Use ListCommandsReply.newBuilder() to construct.
  private ListCommandsReply(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ListCommandsReply() {
    commands_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private ListCommandsReply(
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
              commands_ = new java.util.ArrayList<org.ar4k.agent.tunnels.http.grpc.beacon.Command>();
              mutable_bitField0_ |= 0x00000001;
            }
            commands_.add(
                input.readMessage(org.ar4k.agent.tunnels.http.grpc.beacon.Command.parser(), extensionRegistry));
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
        commands_ = java.util.Collections.unmodifiableList(commands_);
      }
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_ListCommandsReply_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_ListCommandsReply_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply.class, org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply.Builder.class);
  }

  public static final int COMMANDS_FIELD_NUMBER = 1;
  private java.util.List<org.ar4k.agent.tunnels.http.grpc.beacon.Command> commands_;
  /**
   * <code>repeated .beacon.Command commands = 1;</code>
   */
  public java.util.List<org.ar4k.agent.tunnels.http.grpc.beacon.Command> getCommandsList() {
    return commands_;
  }
  /**
   * <code>repeated .beacon.Command commands = 1;</code>
   */
  public java.util.List<? extends org.ar4k.agent.tunnels.http.grpc.beacon.CommandOrBuilder> 
      getCommandsOrBuilderList() {
    return commands_;
  }
  /**
   * <code>repeated .beacon.Command commands = 1;</code>
   */
  public int getCommandsCount() {
    return commands_.size();
  }
  /**
   * <code>repeated .beacon.Command commands = 1;</code>
   */
  public org.ar4k.agent.tunnels.http.grpc.beacon.Command getCommands(int index) {
    return commands_.get(index);
  }
  /**
   * <code>repeated .beacon.Command commands = 1;</code>
   */
  public org.ar4k.agent.tunnels.http.grpc.beacon.CommandOrBuilder getCommandsOrBuilder(
      int index) {
    return commands_.get(index);
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
    for (int i = 0; i < commands_.size(); i++) {
      output.writeMessage(1, commands_.get(i));
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < commands_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, commands_.get(i));
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
    if (!(obj instanceof org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply)) {
      return super.equals(obj);
    }
    org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply other = (org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply) obj;

    if (!getCommandsList()
        .equals(other.getCommandsList())) return false;
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
    if (getCommandsCount() > 0) {
      hash = (37 * hash) + COMMANDS_FIELD_NUMBER;
      hash = (53 * hash) + getCommandsList().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply parseFrom(
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
  public static Builder newBuilder(org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply prototype) {
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
   * Protobuf type {@code beacon.ListCommandsReply}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:beacon.ListCommandsReply)
      org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReplyOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_ListCommandsReply_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_ListCommandsReply_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply.class, org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply.Builder.class);
    }

    // Construct using org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply.newBuilder()
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
        getCommandsFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (commandsBuilder_ == null) {
        commands_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
      } else {
        commandsBuilder_.clear();
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_ListCommandsReply_descriptor;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply getDefaultInstanceForType() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply.getDefaultInstance();
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply build() {
      org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply buildPartial() {
      org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply result = new org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply(this);
      int from_bitField0_ = bitField0_;
      if (commandsBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          commands_ = java.util.Collections.unmodifiableList(commands_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.commands_ = commands_;
      } else {
        result.commands_ = commandsBuilder_.build();
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
      if (other instanceof org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply) {
        return mergeFrom((org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply other) {
      if (other == org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply.getDefaultInstance()) return this;
      if (commandsBuilder_ == null) {
        if (!other.commands_.isEmpty()) {
          if (commands_.isEmpty()) {
            commands_ = other.commands_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureCommandsIsMutable();
            commands_.addAll(other.commands_);
          }
          onChanged();
        }
      } else {
        if (!other.commands_.isEmpty()) {
          if (commandsBuilder_.isEmpty()) {
            commandsBuilder_.dispose();
            commandsBuilder_ = null;
            commands_ = other.commands_;
            bitField0_ = (bitField0_ & ~0x00000001);
            commandsBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getCommandsFieldBuilder() : null;
          } else {
            commandsBuilder_.addAllMessages(other.commands_);
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
      org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.util.List<org.ar4k.agent.tunnels.http.grpc.beacon.Command> commands_ =
      java.util.Collections.emptyList();
    private void ensureCommandsIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        commands_ = new java.util.ArrayList<org.ar4k.agent.tunnels.http.grpc.beacon.Command>(commands_);
        bitField0_ |= 0x00000001;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        org.ar4k.agent.tunnels.http.grpc.beacon.Command, org.ar4k.agent.tunnels.http.grpc.beacon.Command.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.CommandOrBuilder> commandsBuilder_;

    /**
     * <code>repeated .beacon.Command commands = 1;</code>
     */
    public java.util.List<org.ar4k.agent.tunnels.http.grpc.beacon.Command> getCommandsList() {
      if (commandsBuilder_ == null) {
        return java.util.Collections.unmodifiableList(commands_);
      } else {
        return commandsBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .beacon.Command commands = 1;</code>
     */
    public int getCommandsCount() {
      if (commandsBuilder_ == null) {
        return commands_.size();
      } else {
        return commandsBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .beacon.Command commands = 1;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Command getCommands(int index) {
      if (commandsBuilder_ == null) {
        return commands_.get(index);
      } else {
        return commandsBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .beacon.Command commands = 1;</code>
     */
    public Builder setCommands(
        int index, org.ar4k.agent.tunnels.http.grpc.beacon.Command value) {
      if (commandsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureCommandsIsMutable();
        commands_.set(index, value);
        onChanged();
      } else {
        commandsBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .beacon.Command commands = 1;</code>
     */
    public Builder setCommands(
        int index, org.ar4k.agent.tunnels.http.grpc.beacon.Command.Builder builderForValue) {
      if (commandsBuilder_ == null) {
        ensureCommandsIsMutable();
        commands_.set(index, builderForValue.build());
        onChanged();
      } else {
        commandsBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .beacon.Command commands = 1;</code>
     */
    public Builder addCommands(org.ar4k.agent.tunnels.http.grpc.beacon.Command value) {
      if (commandsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureCommandsIsMutable();
        commands_.add(value);
        onChanged();
      } else {
        commandsBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .beacon.Command commands = 1;</code>
     */
    public Builder addCommands(
        int index, org.ar4k.agent.tunnels.http.grpc.beacon.Command value) {
      if (commandsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureCommandsIsMutable();
        commands_.add(index, value);
        onChanged();
      } else {
        commandsBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .beacon.Command commands = 1;</code>
     */
    public Builder addCommands(
        org.ar4k.agent.tunnels.http.grpc.beacon.Command.Builder builderForValue) {
      if (commandsBuilder_ == null) {
        ensureCommandsIsMutable();
        commands_.add(builderForValue.build());
        onChanged();
      } else {
        commandsBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .beacon.Command commands = 1;</code>
     */
    public Builder addCommands(
        int index, org.ar4k.agent.tunnels.http.grpc.beacon.Command.Builder builderForValue) {
      if (commandsBuilder_ == null) {
        ensureCommandsIsMutable();
        commands_.add(index, builderForValue.build());
        onChanged();
      } else {
        commandsBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .beacon.Command commands = 1;</code>
     */
    public Builder addAllCommands(
        java.lang.Iterable<? extends org.ar4k.agent.tunnels.http.grpc.beacon.Command> values) {
      if (commandsBuilder_ == null) {
        ensureCommandsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, commands_);
        onChanged();
      } else {
        commandsBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .beacon.Command commands = 1;</code>
     */
    public Builder clearCommands() {
      if (commandsBuilder_ == null) {
        commands_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        commandsBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .beacon.Command commands = 1;</code>
     */
    public Builder removeCommands(int index) {
      if (commandsBuilder_ == null) {
        ensureCommandsIsMutable();
        commands_.remove(index);
        onChanged();
      } else {
        commandsBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .beacon.Command commands = 1;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Command.Builder getCommandsBuilder(
        int index) {
      return getCommandsFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .beacon.Command commands = 1;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.CommandOrBuilder getCommandsOrBuilder(
        int index) {
      if (commandsBuilder_ == null) {
        return commands_.get(index);  } else {
        return commandsBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .beacon.Command commands = 1;</code>
     */
    public java.util.List<? extends org.ar4k.agent.tunnels.http.grpc.beacon.CommandOrBuilder> 
         getCommandsOrBuilderList() {
      if (commandsBuilder_ != null) {
        return commandsBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(commands_);
      }
    }
    /**
     * <code>repeated .beacon.Command commands = 1;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Command.Builder addCommandsBuilder() {
      return getCommandsFieldBuilder().addBuilder(
          org.ar4k.agent.tunnels.http.grpc.beacon.Command.getDefaultInstance());
    }
    /**
     * <code>repeated .beacon.Command commands = 1;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Command.Builder addCommandsBuilder(
        int index) {
      return getCommandsFieldBuilder().addBuilder(
          index, org.ar4k.agent.tunnels.http.grpc.beacon.Command.getDefaultInstance());
    }
    /**
     * <code>repeated .beacon.Command commands = 1;</code>
     */
    public java.util.List<org.ar4k.agent.tunnels.http.grpc.beacon.Command.Builder> 
         getCommandsBuilderList() {
      return getCommandsFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        org.ar4k.agent.tunnels.http.grpc.beacon.Command, org.ar4k.agent.tunnels.http.grpc.beacon.Command.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.CommandOrBuilder> 
        getCommandsFieldBuilder() {
      if (commandsBuilder_ == null) {
        commandsBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            org.ar4k.agent.tunnels.http.grpc.beacon.Command, org.ar4k.agent.tunnels.http.grpc.beacon.Command.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.CommandOrBuilder>(
                commands_,
                ((bitField0_ & 0x00000001) != 0),
                getParentForChildren(),
                isClean());
        commands_ = null;
      }
      return commandsBuilder_;
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


    // @@protoc_insertion_point(builder_scope:beacon.ListCommandsReply)
  }

  // @@protoc_insertion_point(class_scope:beacon.ListCommandsReply)
  private static final org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply();
  }

  public static org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ListCommandsReply>
      PARSER = new com.google.protobuf.AbstractParser<ListCommandsReply>() {
    @java.lang.Override
    public ListCommandsReply parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new ListCommandsReply(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<ListCommandsReply> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ListCommandsReply> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.ar4k.agent.tunnels.http.grpc.beacon.ListCommandsReply getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
