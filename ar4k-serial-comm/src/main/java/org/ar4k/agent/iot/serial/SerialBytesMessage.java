package org.ar4k.agent.iot.serial;

import org.ar4k.agent.core.data.messages.Ar4kMessage;
import org.springframework.messaging.MessageHeaders;

public class SerialBytesMessage implements Ar4kMessage<Byte[]> {

  private Byte[] payload = null;
  private MessageHeaders headers = null;

  public void setPayload(Byte[] payload) {
    this.payload = payload;
  }

  public void setHeaders(MessageHeaders headers) {
    this.headers = headers;
  }

  @Override
  public Byte[] getPayload() {
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
