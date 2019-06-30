package org.ar4k.agent.config;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import org.joda.time.Instant;

import com.google.gson.TypeAdapter;

public interface ConfigSeed extends Serializable, Cloneable {

  public String getName();

  public String getDescription();

  public Collection<String> getTags();

  //public Ar4kComponent instantiate();

  public Instant getCreationDate();

  public Instant getLastUpdateDate();

  public UUID getUniqueId();

  public int getPriority();

  public TypeAdapter<? extends ConfigSeed> getJsonTypeAdapter();

}
