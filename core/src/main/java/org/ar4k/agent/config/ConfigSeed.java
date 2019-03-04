package org.ar4k.agent.config;

import java.io.Serializable;
import java.util.UUID;

import org.ar4k.agent.core.Ar4kComponent;
import org.joda.time.Instant;

public interface ConfigSeed extends Serializable, Cloneable {

  public String getName();

  public Ar4kComponent instanziate();

  public Instant getCreationDate();

  public Instant getLastUpdateDate();

  public UUID getUniqueId();

}
