package org.ar4k.agent.helper;

public final class StringUtils {

	private StringUtils() {
		throw new UnsupportedOperationException("Just for static usage");
	}

	public static String toEmptyStringIfNull(Object value) {
		return value == null ? "" : value.toString();
	}
}
