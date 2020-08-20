package org.ar4k.agent.core.data.messages;

import java.io.Serializable;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

public interface Ar4kMessage<T> extends Message<T>, AutoCloseable, Serializable, Cloneable {

	public void setPayload(T elaborateMessage);

	public void setObjectPayload(Object object);

	public void setHeaders(MessageHeaders headers);
}
