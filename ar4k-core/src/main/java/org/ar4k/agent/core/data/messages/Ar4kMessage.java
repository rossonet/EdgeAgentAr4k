package org.ar4k.agent.core.data.messages;

import org.springframework.messaging.Message;

public interface Ar4kMessage<T> extends Message<T>, AutoCloseable {

}
