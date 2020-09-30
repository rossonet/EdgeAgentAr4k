package org.ar4k.agent.core.data.generator.dataType;

import org.ar4k.agent.core.data.generator.FormatGenerator;
import org.ar4k.agent.core.data.messages.EdgeMessage;
import org.ar4k.agent.core.data.messages.LongMessage;

public class LongSimulator implements FormatGenerator {

	@Override
	public EdgeMessage<?> format(Object actual) {
		final LongMessage message = new LongMessage();
		message.setPayload(((Double) actual).longValue());
		return message;
	}

	@Override
	public boolean isNumber() {
		return true;
	}
}
