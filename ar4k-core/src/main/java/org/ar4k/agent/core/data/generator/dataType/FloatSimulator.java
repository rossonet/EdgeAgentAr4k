package org.ar4k.agent.core.data.generator.dataType;

import org.ar4k.agent.core.data.generator.FormatGenerator;
import org.ar4k.agent.core.data.messages.EdgeMessage;
import org.ar4k.agent.core.data.messages.FloatMessage;

public class FloatSimulator implements FormatGenerator {

	@Override
	public EdgeMessage<?> format(Object actual) {
		final FloatMessage message = new FloatMessage();
		message.setPayload(((Double) actual).floatValue());
		return message;
	}

	@Override
	public boolean isNumber() {
		return true;
	}

}
