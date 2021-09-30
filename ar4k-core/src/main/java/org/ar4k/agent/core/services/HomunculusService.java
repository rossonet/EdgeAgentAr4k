package org.ar4k.agent.core.services;

import java.util.Timer;

import org.ar4k.agent.core.EdgeAgentCore;

public class HomunculusService extends AbstractEdgeService {

  public HomunculusService(EdgeAgentCore edgeAgentCore, ServiceConfig serviceConfig, Timer timerScheduler) {
    super(edgeAgentCore, serviceConfig, timerScheduler);
  }

}
