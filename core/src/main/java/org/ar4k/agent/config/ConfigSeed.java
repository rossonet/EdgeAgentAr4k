package org.ar4k.agent.config;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import org.ar4k.agent.core.Ar4kComponent;
import org.joda.time.Instant;

public interface ConfigSeed extends Serializable, Cloneable {

  public String getName();
  
  public String getDescription();

  public Collection<String> getTags();

  public Ar4kComponent instantiate();

  public Instant getCreationDate();

  public Instant getLastUpdateDate();

  public UUID getUniqueId();
  
  public int getPriority();

}
