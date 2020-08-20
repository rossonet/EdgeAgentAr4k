package org.ar4k.agent.core.data.generator.dataType;

import org.ar4k.agent.core.data.generator.FormatGenerator;
import org.ar4k.agent.core.data.messages.Ar4kMessage;
import org.ar4k.agent.core.data.messages.LongMessage;

public class LongSimulator implements FormatGenerator {

	@Override
	public Ar4kMessage<?> format(Object actual) {
		final LongMessage message = new LongMessage();
		message.setPayload(((Double) actual).longValue());
		return message;
	}

	@Override
	public boolean isNumber() {
		return true;
	}
}
