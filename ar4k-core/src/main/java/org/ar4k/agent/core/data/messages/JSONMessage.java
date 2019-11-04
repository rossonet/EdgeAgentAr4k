package org.ar4k.agent.core.data.messages;

import org.json.JSONObject;
import org.springframework.messaging.MessageHeaders;

public class JSONMessage implements Ar4kMessage<JSONObject> {

  private JSONObject payload = null;
  private MessageHeaders headers = null;

  public void setPayload(JSONObject payload) {
    this.payload = payload;
  }

  public void setHeaders(MessageHeaders headers) {
    this.headers = headers;
  }

  @Override
  public JSONObject getPayload() {
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
