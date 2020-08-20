package org.ar4k.agent.core.data.generator.dataType;

import org.ar4k.agent.core.data.generator.FormatGenerator;
import org.ar4k.agent.core.data.messages.Ar4kMessage;
import org.ar4k.agent.core.data.messages.StringMessage;

public class StringSimulator implements FormatGenerator {

	@Override
	public Ar4kMessage<?> format(Object actual) {
		final StringMessage message = new StringMessage();
		message.setPayload(actual.toString());
		return message;
	}

	@Override
	public boolean isNumber() {
		return false;
	}

}
