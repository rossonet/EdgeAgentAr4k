package org.ar4k.agent.config;

import org.ar4k.agent.core.Ar4kComponent;

public interface ServiceConfig extends PotConfig {

  public Ar4kComponent instantiate();

}
