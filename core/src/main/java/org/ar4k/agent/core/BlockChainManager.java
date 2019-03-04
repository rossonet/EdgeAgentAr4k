package org.ar4k.agent.core;

import org.ar4k.agent.config.ConfigSeed;

public abstract class BlockChainManager implements Ar4kComponent {

  public static enum Governance {
    MONARCHY, DEMOCRACY, JUNGLE, NONE
  }

  @Override
  public void init() {
    // TODO Auto-generated method stub

  }

  @Override
  public void kill() {
    // TODO Auto-generated method stub

  }

  @Override
  public ConfigSeed getConfiguration() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setConfiguration(ConfigSeed configuration) {
    // TODO Auto-generated method stub

  }
}
