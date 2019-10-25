package org.ar4k.agent.config;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import org.joda.time.Instant;

import com.google.gson.TypeAdapter;

public interface ConfigSeed extends Serializable, Cloneable {

  String getName();

  String getDescription();

  Collection<String> getTags();

  Instant getCreationDate();

  Instant getLastUpdateDate();

  UUID getUniqueId();

  int getPriority();

  TypeAdapter<? extends ConfigSeed> getJsonTypeAdapter();

}
