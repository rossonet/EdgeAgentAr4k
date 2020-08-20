package org.ar4k.agent.core.data.generator.dataType;

import org.ar4k.agent.core.data.generator.FormatGenerator;
import org.ar4k.agent.core.data.messages.Ar4kMessage;
import org.ar4k.agent.core.data.messages.IntegerMessage;

public class IntegerSimulator implements FormatGenerator {

	@Override
	public Ar4kMessage<?> format(Object actual) {
		final IntegerMessage message = new IntegerMessage();
		message.setPayload(((Double) actual).intValue());
		return message;
	}

	@Override
	public boolean isNumber() {
		return true;
	}

}
