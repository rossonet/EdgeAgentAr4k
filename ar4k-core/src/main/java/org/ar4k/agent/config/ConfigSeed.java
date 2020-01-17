package org.ar4k.agent.config;

import java.io.Serializable;
import java.util.Collection;

import org.joda.time.Instant;

public interface ConfigSeed extends Serializable, Cloneable {

  String getName();

  String getDescription();

  Collection<String> getTags();

  Instant getCreationDate();

  Instant getLastUpdateDate();

  String getUniqueId();

}
