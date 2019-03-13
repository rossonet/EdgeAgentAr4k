package org.ar4k.agent.spring.autoconfig.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "ar4k.web", havingValue = "true")
public class Ar4kWebServerFactoryCustomizer implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

  @Value("${server.port}")
  private int targetPort;

  @Override
  public void customize(NettyReactiveWebServerFactory factory) {
    factory.setPort(targetPort);
  }
}
