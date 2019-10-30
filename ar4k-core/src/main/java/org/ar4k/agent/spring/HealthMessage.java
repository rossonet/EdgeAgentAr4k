package org.ar4k.agent.spring;

import org.ar4k.agent.rpc.RpcMessage;
import org.springframework.messaging.MessageHeaders;

public class HealthMessage<S> implements RpcMessage<String> {

  private String rawString = null;
  private MessageHeaders header = null;

  @Override
  public String getPayload() {
    return rawString;
  }

  @Override
  public MessageHeaders getHeaders() {
    return header;
  }

  public void setPayload(String elaborateMessage) {
    rawString = elaborateMessage;
  }

  @Override
  public void close() throws Exception {
    rawString = null;
    header = null;
  }

}
