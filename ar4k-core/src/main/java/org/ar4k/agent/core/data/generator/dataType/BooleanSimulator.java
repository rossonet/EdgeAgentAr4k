package org.ar4k.agent.core.data.generator.dataType;

import org.ar4k.agent.core.data.generator.FormatGenerator;
import org.ar4k.agent.core.data.messages.EdgeMessage;
import org.ar4k.agent.core.data.messages.BooleanMessage;

public class BooleanSimulator implements FormatGenerator {

	@Override
	public EdgeMessage<?> format(Object actual) {
		final BooleanMessage message = new BooleanMessage();
		message.setPayload(Math.round((Double) actual) % 2 == 0);
		return message;
	}

	@Override
	public boolean isNumber() {
		return true;
	}

}
