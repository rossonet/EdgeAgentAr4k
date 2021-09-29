package org.ar4k.agent.core.services;

import java.util.Timer;

import org.ar4k.agent.core.Homunculus;

public class HomunculusService extends AbstractEdgeService {

  public HomunculusService(Homunculus homunculus, ServiceConfig serviceConfig, Timer timerScheduler) {
    super(homunculus, serviceConfig, timerScheduler);
  }

}
