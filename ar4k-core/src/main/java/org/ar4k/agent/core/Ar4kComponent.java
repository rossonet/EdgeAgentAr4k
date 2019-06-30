package org.ar4k.agent.core;

import java.io.Closeable;

import org.ar4k.agent.config.ConfigSeed;
import org.springframework.beans.factory.BeanNameAware;

import com.google.gson.JsonElement;

// compoenente come bean
public interface Ar4kComponent extends BeanNameAware, Closeable {

  public void init();

  public void kill();

  public ConfigSeed getConfiguration();

  void setConfiguration(ConfigSeed configuration);

  public String getStatusString();

  public JsonElement getStatusJson();

}
