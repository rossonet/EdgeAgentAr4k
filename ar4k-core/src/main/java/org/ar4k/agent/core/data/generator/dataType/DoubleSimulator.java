package org.ar4k.agent.core.data.generator.dataType;

import org.ar4k.agent.core.data.generator.FormatGenerator;
import org.ar4k.agent.core.data.messages.Ar4kMessage;
import org.ar4k.agent.core.data.messages.DoubleMessage;

public class DoubleSimulator implements FormatGenerator {

	@Override
	public Ar4kMessage<?> format(Object actual) {
		final DoubleMessage message = new DoubleMessage();
		message.setPayload((Double) actual);
		return message;
	}

	@Override
	public boolean isNumber() {
		return true;
	}

}
