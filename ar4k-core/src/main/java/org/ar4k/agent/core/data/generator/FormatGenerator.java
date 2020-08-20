package org.ar4k.agent.core.data.generator;

import org.ar4k.agent.core.data.messages.Ar4kMessage;

public interface FormatGenerator {

	Ar4kMessage<?> format(Object actual);

	boolean isNumber();

}
