package org.ar4k.agent.config;

import org.ar4k.agent.core.Ar4kComponent;

public interface ServiceConfig extends ConfigSeed {

  Ar4kComponent instantiate();

  boolean isSpringBean();

  int getPriority();

  int getWatchDogInterval();

  int getMaxRestartRetries();

  int getWatchDogTimeout();

  String getDataNamePrefix();

}
