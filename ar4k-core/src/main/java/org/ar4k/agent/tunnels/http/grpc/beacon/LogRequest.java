// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http.grpc.beacon;

/**
 * Protobuf type {@code beacon.LogRequest}
 */
public  final class LogRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:beacon.LogRequest)
    LogRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use LogRequest.newBuilder() to construct.
  private LogRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private LogRequest() {
    severity_ = 0;
    logLine_ = "";
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private LogRequest(
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
            if (agentSender_ != null) {
              subBuilder = agentSender_.toBuilder();
            }
            agentSender_ = input.readMessage(org.ar4k.agent.tunnels.http.grpc.beacon.Agent.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(agentSender_);
              agentSender_ = subBuilder.buildPartial();
            }

            break;
          }
          case 16: {
            int rawValue = input.readEnum();

            severity_ = rawValue;
            break;
          }
          case 26: {
            java.lang.String s = input.readStringRequireUtf8();

            logLine_ = s;
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
    return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_LogRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_LogRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest.class, org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest.Builder.class);
  }

  public static final int AGENTSENDER_FIELD_NUMBER = 1;
  private org.ar4k.agent.tunnels.http.grpc.beacon.Agent agentSender_;
  /**
   * <code>.beacon.Agent agentSender = 1;</code>
   */
  public boolean hasAgentSender() {
    return agentSender_ != null;
  }
  /**
   * <code>.beacon.Agent agentSender = 1;</code>
   */
  public org.ar4k.agent.tunnels.http.grpc.beacon.Agent getAgentSender() {
    return agentSender_ == null ? org.ar4k.agent.tunnels.http.grpc.beacon.Agent.getDefaultInstance() : agentSender_;
  }
  /**
   * <code>.beacon.Agent agentSender = 1;</code>
   */
  public org.ar4k.agent.tunnels.http.grpc.beacon.AgentOrBuilder getAgentSenderOrBuilder() {
    return getAgentSender();
  }

  public static final int SEVERITY_FIELD_NUMBER = 2;
  private int severity_;
  /**
   * <code>.beacon.LogSeverity severity = 2;</code>
   */
  public int getSeverityValue() {
    return severity_;
  }
  /**
   * <code>.beacon.LogSeverity severity = 2;</code>
   */
  public org.ar4k.agent.tunnels.http.grpc.beacon.LogSeverity getSeverity() {
    @SuppressWarnings("deprecation")
    org.ar4k.agent.tunnels.http.grpc.beacon.LogSeverity result = org.ar4k.agent.tunnels.http.grpc.beacon.LogSeverity.valueOf(severity_);
    return result == null ? org.ar4k.agent.tunnels.http.grpc.beacon.LogSeverity.UNRECOGNIZED : result;
  }

  public static final int LOGLINE_FIELD_NUMBER = 3;
  private volatile java.lang.Object logLine_;
  /**
   * <code>string logLine = 3;</code>
   */
  public java.lang.String getLogLine() {
    java.lang.Object ref = logLine_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      logLine_ = s;
      return s;
    }
  }
  /**
   * <code>string logLine = 3;</code>
   */
  public com.google.protobuf.ByteString
      getLogLineBytes() {
    java.lang.Object ref = logLine_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      logLine_ = b;
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
    if (agentSender_ != null) {
      output.writeMessage(1, getAgentSender());
    }
    if (severity_ != org.ar4k.agent.tunnels.http.grpc.beacon.LogSeverity.DEFAULT.getNumber()) {
      output.writeEnum(2, severity_);
    }
    if (!getLogLineBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, logLine_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (agentSender_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getAgentSender());
    }
    if (severity_ != org.ar4k.agent.tunnels.http.grpc.beacon.LogSeverity.DEFAULT.getNumber()) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(2, severity_);
    }
    if (!getLogLineBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, logLine_);
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
    if (!(obj instanceof org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest)) {
      return super.equals(obj);
    }
    org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest other = (org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest) obj;

    if (hasAgentSender() != other.hasAgentSender()) return false;
    if (hasAgentSender()) {
      if (!getAgentSender()
          .equals(other.getAgentSender())) return false;
    }
    if (severity_ != other.severity_) return false;
    if (!getLogLine()
        .equals(other.getLogLine())) return false;
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
    if (hasAgentSender()) {
      hash = (37 * hash) + AGENTSENDER_FIELD_NUMBER;
      hash = (53 * hash) + getAgentSender().hashCode();
    }
    hash = (37 * hash) + SEVERITY_FIELD_NUMBER;
    hash = (53 * hash) + severity_;
    hash = (37 * hash) + LOGLINE_FIELD_NUMBER;
    hash = (53 * hash) + getLogLine().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest parseFrom(
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
  public static Builder newBuilder(org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest prototype) {
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
   * Protobuf type {@code beacon.LogRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:beacon.LogRequest)
      org.ar4k.agent.tunnels.http.grpc.beacon.LogRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_LogRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_LogRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest.class, org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest.Builder.class);
    }

    // Construct using org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest.newBuilder()
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
      if (agentSenderBuilder_ == null) {
        agentSender_ = null;
      } else {
        agentSender_ = null;
        agentSenderBuilder_ = null;
      }
      severity_ = 0;

      logLine_ = "";

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.internal_static_beacon_LogRequest_descriptor;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest getDefaultInstanceForType() {
      return org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest.getDefaultInstance();
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest build() {
      org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest buildPartial() {
      org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest result = new org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest(this);
      if (agentSenderBuilder_ == null) {
        result.agentSender_ = agentSender_;
      } else {
        result.agentSender_ = agentSenderBuilder_.build();
      }
      result.severity_ = severity_;
      result.logLine_ = logLine_;
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
      if (other instanceof org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest) {
        return mergeFrom((org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest other) {
      if (other == org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest.getDefaultInstance()) return this;
      if (other.hasAgentSender()) {
        mergeAgentSender(other.getAgentSender());
      }
      if (other.severity_ != 0) {
        setSeverityValue(other.getSeverityValue());
      }
      if (!other.getLogLine().isEmpty()) {
        logLine_ = other.logLine_;
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
      org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private org.ar4k.agent.tunnels.http.grpc.beacon.Agent agentSender_;
    private com.google.protobuf.SingleFieldBuilderV3<
        org.ar4k.agent.tunnels.http.grpc.beacon.Agent, org.ar4k.agent.tunnels.http.grpc.beacon.Agent.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.AgentOrBuilder> agentSenderBuilder_;
    /**
     * <code>.beacon.Agent agentSender = 1;</code>
     */
    public boolean hasAgentSender() {
      return agentSenderBuilder_ != null || agentSender_ != null;
    }
    /**
     * <code>.beacon.Agent agentSender = 1;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Agent getAgentSender() {
      if (agentSenderBuilder_ == null) {
        return agentSender_ == null ? org.ar4k.agent.tunnels.http.grpc.beacon.Agent.getDefaultInstance() : agentSender_;
      } else {
        return agentSenderBuilder_.getMessage();
      }
    }
    /**
     * <code>.beacon.Agent agentSender = 1;</code>
     */
    public Builder setAgentSender(org.ar4k.agent.tunnels.http.grpc.beacon.Agent value) {
      if (agentSenderBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        agentSender_ = value;
        onChanged();
      } else {
        agentSenderBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.beacon.Agent agentSender = 1;</code>
     */
    public Builder setAgentSender(
        org.ar4k.agent.tunnels.http.grpc.beacon.Agent.Builder builderForValue) {
      if (agentSenderBuilder_ == null) {
        agentSender_ = builderForValue.build();
        onChanged();
      } else {
        agentSenderBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.beacon.Agent agentSender = 1;</code>
     */
    public Builder mergeAgentSender(org.ar4k.agent.tunnels.http.grpc.beacon.Agent value) {
      if (agentSenderBuilder_ == null) {
        if (agentSender_ != null) {
          agentSender_ =
            org.ar4k.agent.tunnels.http.grpc.beacon.Agent.newBuilder(agentSender_).mergeFrom(value).buildPartial();
        } else {
          agentSender_ = value;
        }
        onChanged();
      } else {
        agentSenderBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.beacon.Agent agentSender = 1;</code>
     */
    public Builder clearAgentSender() {
      if (agentSenderBuilder_ == null) {
        agentSender_ = null;
        onChanged();
      } else {
        agentSender_ = null;
        agentSenderBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.beacon.Agent agentSender = 1;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.Agent.Builder getAgentSenderBuilder() {
      
      onChanged();
      return getAgentSenderFieldBuilder().getBuilder();
    }
    /**
     * <code>.beacon.Agent agentSender = 1;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.AgentOrBuilder getAgentSenderOrBuilder() {
      if (agentSenderBuilder_ != null) {
        return agentSenderBuilder_.getMessageOrBuilder();
      } else {
        return agentSender_ == null ?
            org.ar4k.agent.tunnels.http.grpc.beacon.Agent.getDefaultInstance() : agentSender_;
      }
    }
    /**
     * <code>.beacon.Agent agentSender = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        org.ar4k.agent.tunnels.http.grpc.beacon.Agent, org.ar4k.agent.tunnels.http.grpc.beacon.Agent.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.AgentOrBuilder> 
        getAgentSenderFieldBuilder() {
      if (agentSenderBuilder_ == null) {
        agentSenderBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            org.ar4k.agent.tunnels.http.grpc.beacon.Agent, org.ar4k.agent.tunnels.http.grpc.beacon.Agent.Builder, org.ar4k.agent.tunnels.http.grpc.beacon.AgentOrBuilder>(
                getAgentSender(),
                getParentForChildren(),
                isClean());
        agentSender_ = null;
      }
      return agentSenderBuilder_;
    }

    private int severity_ = 0;
    /**
     * <code>.beacon.LogSeverity severity = 2;</code>
     */
    public int getSeverityValue() {
      return severity_;
    }
    /**
     * <code>.beacon.LogSeverity severity = 2;</code>
     */
    public Builder setSeverityValue(int value) {
      severity_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>.beacon.LogSeverity severity = 2;</code>
     */
    public org.ar4k.agent.tunnels.http.grpc.beacon.LogSeverity getSeverity() {
      @SuppressWarnings("deprecation")
      org.ar4k.agent.tunnels.http.grpc.beacon.LogSeverity result = org.ar4k.agent.tunnels.http.grpc.beacon.LogSeverity.valueOf(severity_);
      return result == null ? org.ar4k.agent.tunnels.http.grpc.beacon.LogSeverity.UNRECOGNIZED : result;
    }
    /**
     * <code>.beacon.LogSeverity severity = 2;</code>
     */
    public Builder setSeverity(org.ar4k.agent.tunnels.http.grpc.beacon.LogSeverity value) {
      if (value == null) {
        throw new NullPointerException();
      }
      
      severity_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <code>.beacon.LogSeverity severity = 2;</code>
     */
    public Builder clearSeverity() {
      
      severity_ = 0;
      onChanged();
      return this;
    }

    private java.lang.Object logLine_ = "";
    /**
     * <code>string logLine = 3;</code>
     */
    public java.lang.String getLogLine() {
      java.lang.Object ref = logLine_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        logLine_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string logLine = 3;</code>
     */
    public com.google.protobuf.ByteString
        getLogLineBytes() {
      java.lang.Object ref = logLine_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        logLine_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string logLine = 3;</code>
     */
    public Builder setLogLine(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      logLine_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string logLine = 3;</code>
     */
    public Builder clearLogLine() {
      
      logLine_ = getDefaultInstance().getLogLine();
      onChanged();
      return this;
    }
    /**
     * <code>string logLine = 3;</code>
     */
    public Builder setLogLineBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      logLine_ = value;
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


    // @@protoc_insertion_point(builder_scope:beacon.LogRequest)
  }

  // @@protoc_insertion_point(class_scope:beacon.LogRequest)
  private static final org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest();
  }

  public static org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<LogRequest>
      PARSER = new com.google.protobuf.AbstractParser<LogRequest>() {
    @java.lang.Override
    public LogRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new LogRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<LogRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<LogRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public org.ar4k.agent.tunnels.http.grpc.beacon.LogRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

