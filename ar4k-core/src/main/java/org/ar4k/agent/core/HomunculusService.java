package org.ar4k.agent.core;

import java.util.Timer;

import org.ar4k.agent.core.interfaces.ServiceConfig;

public class HomunculusService extends AbstractEdgeService {

  public HomunculusService(Homunculus homunculus, ServiceConfig serviceConfig, Timer timerScheduler) {
    super(homunculus, serviceConfig, timerScheduler);
  }

}
