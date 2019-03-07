package org.ar4k.agent.core;

import org.ar4k.agent.config.ConfigSeed;
import org.json.JSONObject;

public interface Ar4kComponent {

  public void init();

  public void kill();

  public ConfigSeed getConfiguration();

  void setConfiguration(ConfigSeed configuration);

  public String getStatusString();

  public JSONObject getStatusJson();

}
