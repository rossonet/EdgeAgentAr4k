package org.ar4k.agent.camera.messages;

import java.awt.image.BufferedImage;

import org.ar4k.agent.core.data.messages.Ar4kMessage;
import org.springframework.messaging.MessageHeaders;

public class VideoMessage implements Ar4kMessage<BufferedImage> {

  private BufferedImage payload = null;
  private MessageHeaders headers = null;

  public void setPayload(BufferedImage payload) {
    this.payload = payload;
  }

  public void setHeaders(MessageHeaders headers) {
    this.headers = headers;
  }

  @Override
  public BufferedImage getPayload() {
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
