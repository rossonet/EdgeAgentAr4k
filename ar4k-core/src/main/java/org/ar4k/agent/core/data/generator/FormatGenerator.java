package org.ar4k.agent.core.data.generator;

import org.ar4k.agent.core.data.messages.EdgeMessage;

public interface FormatGenerator {

	EdgeMessage<?> format(Object actual);

	boolean isNumber();

}
