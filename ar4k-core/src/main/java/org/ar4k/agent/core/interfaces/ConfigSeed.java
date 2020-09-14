package org.ar4k.agent.core.interfaces;

import java.io.Serializable;
import java.util.List;

import org.ar4k.agent.config.EdgeConfig;
import org.joda.time.Instant;

/**
 * interfaccia da implementare per una configurazione nel sistema
 *
 * @see org.ar4k.agent.config.EdgeConfig
 * @see org.ar4k.agent.core.interfaces.ServiceConfig
 *
 * @author andrea
 *
 */
public interface ConfigSeed extends Serializable, Cloneable {

  String getName();

  String getDescription();

  List<String> getTags();

  Instant getCreationDate();

  Instant getLastUpdateDate();

  String getUniqueId();

}
