package org.ar4k.agent.core;

import java.util.Timer;

import org.ar4k.agent.config.ServiceConfig;

public class AnimaService extends AbstractAr4kService {

  public AnimaService(Anima anima, ServiceConfig serviceConfig, Timer timerScheduler) {
    super(anima, serviceConfig, timerScheduler);
  }

}