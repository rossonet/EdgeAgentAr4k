package org.ar4k.agent.messages;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway(name = "animaGateway", defaultRequestChannel = "mainAnimaChannel")
public interface IAnimaGateway {

  @Gateway
  void processAnimaRequest(Message<Object> message);
}
