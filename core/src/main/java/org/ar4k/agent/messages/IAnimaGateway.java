package org.ar4k.agent.messages;

import java.util.List;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway(name = "animaGateway", defaultRequestChannel = "mainAnimaChannel")
public interface IAnimaGateway {

  @Gateway
  void processCargoRequest(Message<List<Ar4kMessage>> message);
}
