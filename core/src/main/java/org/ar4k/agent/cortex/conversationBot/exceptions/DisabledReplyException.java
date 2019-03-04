package org.ar4k.agent.cortex.conversationBot.exceptions;

public class DisabledReplyException extends HomunculusException {

  private static final long serialVersionUID = 8312041662739592149L;

  public DisabledReplyException(String message) {
    super(message);
  }
}