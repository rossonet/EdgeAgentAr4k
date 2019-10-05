// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ar4k_beacon.proto

package org.ar4k.agent.tunnels.http.grpc.beacon;

/**
 * Protobuf enum {@code beacon.DataType}
 */
public enum DataType
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>STRING = 0;</code>
   */
  STRING(0),
  /**
   * <code>CHAR = 1;</code>
   */
  CHAR(1),
  /**
   * <code>BYTES = 2;</code>
   */
  BYTES(2),
  /**
   * <code>INT32 = 3;</code>
   */
  INT32(3),
  /**
   * <code>INT64 = 4;</code>
   */
  INT64(4),
  /**
   * <code>FLOAT = 5;</code>
   */
  FLOAT(5),
  /**
   * <code>DOUBLE = 6;</code>
   */
  DOUBLE(6),
  /**
   * <code>BOOLEAN = 7;</code>
   */
  BOOLEAN(7),
  /**
   * <code>TIMESTAMP = 8;</code>
   */
  TIMESTAMP(8),
  /**
   * <code>OBJECT = 9;</code>
   */
  OBJECT(9),
  /**
   * <code>UNKNOWN = 10;</code>
   */
  UNKNOWN(10),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>STRING = 0;</code>
   */
  public static final int STRING_VALUE = 0;
  /**
   * <code>CHAR = 1;</code>
   */
  public static final int CHAR_VALUE = 1;
  /**
   * <code>BYTES = 2;</code>
   */
  public static final int BYTES_VALUE = 2;
  /**
   * <code>INT32 = 3;</code>
   */
  public static final int INT32_VALUE = 3;
  /**
   * <code>INT64 = 4;</code>
   */
  public static final int INT64_VALUE = 4;
  /**
   * <code>FLOAT = 5;</code>
   */
  public static final int FLOAT_VALUE = 5;
  /**
   * <code>DOUBLE = 6;</code>
   */
  public static final int DOUBLE_VALUE = 6;
  /**
   * <code>BOOLEAN = 7;</code>
   */
  public static final int BOOLEAN_VALUE = 7;
  /**
   * <code>TIMESTAMP = 8;</code>
   */
  public static final int TIMESTAMP_VALUE = 8;
  /**
   * <code>OBJECT = 9;</code>
   */
  public static final int OBJECT_VALUE = 9;
  /**
   * <code>UNKNOWN = 10;</code>
   */
  public static final int UNKNOWN_VALUE = 10;


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
  public static DataType valueOf(int value) {
    return forNumber(value);
  }

  public static DataType forNumber(int value) {
    switch (value) {
      case 0: return STRING;
      case 1: return CHAR;
      case 2: return BYTES;
      case 3: return INT32;
      case 4: return INT64;
      case 5: return FLOAT;
      case 6: return DOUBLE;
      case 7: return BOOLEAN;
      case 8: return TIMESTAMP;
      case 9: return OBJECT;
      case 10: return UNKNOWN;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<DataType>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      DataType> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<DataType>() {
          public DataType findValueByNumber(int number) {
            return DataType.forNumber(number);
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
    return org.ar4k.agent.tunnels.http.grpc.beacon.BeaconMirrorService.getDescriptor().getEnumTypes().get(4);
  }

  private static final DataType[] VALUES = values();

  public static DataType valueOf(
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

  private DataType(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:beacon.DataType)
}
