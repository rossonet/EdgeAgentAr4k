package org.ar4k.agent.core.data.generator;

import java.util.List;

import org.springframework.messaging.Message;

public interface NextGenerator {

	List<Message<?>> getNextValue();

}
