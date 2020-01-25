package org.ar4k.agent.helper;

public final class StringUtils {
  public static String toEmptyStringIfNull(Object value) {
    return value == null ? "" : value.toString();
  }
}
