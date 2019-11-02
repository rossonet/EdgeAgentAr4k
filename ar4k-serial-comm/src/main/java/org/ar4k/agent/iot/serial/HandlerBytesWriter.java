package org.ar4k.agent.iot.serial;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

public class HandlerBytesWriter implements MessageHandler {

  private final SerialService serialService;

  public HandlerBytesWriter(SerialService serialService) {
    this.serialService = serialService;
  }

  @Override
  public void handleMessage(Message<?> message) throws MessagingException {
    final byte[] btw = ((byte[]) message.getPayload());
    serialService.getComPort().writeBytes(btw, btw.length);
  }

}
