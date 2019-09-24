package org.ar4k.agent.logger;

import org.ar4k.agent.rpc.RpcMessage;
import org.springframework.messaging.MessageHeaders;

public class LoggerMessage<S> implements RpcMessage<String> {

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

}
