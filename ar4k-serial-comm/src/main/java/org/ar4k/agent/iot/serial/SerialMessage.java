package org.ar4k.agent.iot.serial;

import org.ar4k.agent.core.data.messages.Ar4kMessage;
import org.springframework.messaging.MessageHeaders;

public class SerialMessage implements Ar4kMessage<Object> {

  private Object payload = null;
  private MessageHeaders headers = null;

  public void setPayload(Object payload) {
    this.payload = payload;
  }

  public void setHeaders(MessageHeaders headers) {
    this.headers = headers;
  }

  @Override
  public Object getPayload() {
    return payload;
  }

  @Override
  public MessageHeaders getHeaders() {
    return headers;
  }

  @Override
  public void close() throws Exception {
    payload = null;
    headers = null;
  }

}
