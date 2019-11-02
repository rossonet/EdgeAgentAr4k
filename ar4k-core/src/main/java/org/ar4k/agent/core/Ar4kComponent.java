package org.ar4k.agent.core;

import org.ar4k.agent.config.ConfigSeed;
import org.json.JSONObject;
import org.springframework.beans.factory.BeanNameAware;

// compoenente come bean
public interface Ar4kComponent extends BeanNameAware, AutoCloseable {

  void init();

  void kill();

  ConfigSeed getConfiguration();

  void setConfiguration(ConfigSeed configuration);

  String getStatusString();

  JSONObject getStatusJson();

}
