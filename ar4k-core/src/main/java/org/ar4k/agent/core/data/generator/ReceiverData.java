package org.ar4k.agent.core.data.generator;

import org.springframework.messaging.MessageHandler;

public interface ReceiverData {

	MessageHandler getCallBack();

	void setValue(Object payload);

}
