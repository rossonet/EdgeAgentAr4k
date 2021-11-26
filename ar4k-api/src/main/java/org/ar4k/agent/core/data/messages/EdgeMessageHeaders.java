package org.ar4k.agent.core.data.messages;

import java.util.Date;
import java.util.Map;

import org.springframework.lang.Nullable;
import org.springframework.messaging.MessageHeaders;

public class EdgeMessageHeaders extends MessageHeaders {

	private static final long serialVersionUID = -5157230625667116897L;

	/**
	 * The key for the message expires.
	 */
	public static final String EXPIRES = "expires";

	/**
	 * The key for the message duration.
	 */
	public static final String DURATION = "duration";

	public EdgeMessageHeaders(Map<String, Object> headers) {
		super(headers);
	}

	@Nullable
	public Long getExpires() {
		return get(EXPIRES, Long.class);
	}

	@Nullable
	public Long getDuration() {
		return get(DURATION, Long.class);
	}

	@Nullable
	public Date getTime() {
		return new Date(getTimestamp());
	}

}
