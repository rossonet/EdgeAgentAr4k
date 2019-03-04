package org.ar4k.agent.core;

import org.ar4k.agent.config.ConfigSeed;

public interface Ar4kComponent {

  public void init();

  public void kill();

  public ConfigSeed getConfiguration();

  void setConfiguration(ConfigSeed configuration);

}
