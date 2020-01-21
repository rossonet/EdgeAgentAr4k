// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http.grpc.beacon;

/**
 * Protobuf enum {@code beacon.TunnelType}
 */
public enum TunnelType
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>SERVER_TO_BYTES_TCP = 0;</code>
   */
  SERVER_TO_BYTES_TCP(0),
  /**
   * <code>BYTES_TO_CLIENT_TCP = 1;</code>
   */
  BYTES_TO_CLIENT_TCP(1),
  /**
   * <code>SERVER_TO_BYTES_UDP = 2;</code>
   */
  SERVER_TO_BYTES_UDP(2),
  /**
   * <code>BYTES_TO_CLIENT_UDP = 3;</code>
   */
  BYTES_TO_CLIENT_UDP(3),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>SERVER_TO_BYTES_TCP = 0;</code>
   */
  public static final int SERVER_TO_BYTES_TCP_VALUE = 0;
  /**
   * <code>BYTES_TO_CLIENT_TCP = 1;</code>
   */
  public static final int BYTES_TO_CLIENT_TCP_VALUE = 1;
  /**
   * <code>SERVER_TO_BYTES_UDP = 2;</code>
   */
  public static final int SERVER_TO_BYTES_UDP_VALUE = 2;
  /**
   * <code>BYTES_TO_CLIENT_UDP = 3;</code>
   */
  public static final int BYTES_TO_CLIENT_UDP_VALUE = 3;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static TunnelType valueOf(int value) {
    return forNumber(value);
  }

  public static TunnelType forNumber(int value) {
    switch (value) {
      case 0: return SERVER_TO_BYTES_TCP;
      case 1: return BYTES_TO_CLIENT_TCP;
      case 2: return SERVER_TO_BYTES_UDP;
      case 3: return BYTES_TO_CLIENT_UDP;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<TunnelType>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      TunnelType> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<TunnelType>() {
          public TunnelType findValueByNumber(int number) {
            return TunnelType.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.getDescriptor().getEnumTypes().get(5);
  }

  private static final TunnelType[] VALUES = values();

  public static TunnelType valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private TunnelType(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:beacon.TunnelType)
}

