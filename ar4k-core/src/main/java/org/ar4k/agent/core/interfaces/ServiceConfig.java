package org.ar4k.agent.core.interfaces;

import org.ar4k.agent.config.AbstractServiceConfig;

/**
 * interfaccia da implementare per una configurazione di servizio valida
 *
 * @see org.ar4k.agent.config.AbstractServiceConfig
 *
 * @author andrea
 *
 */
public interface ServiceConfig extends ConfigSeed {

  EdgeComponent instantiate();

  boolean isSpringBean();

  int getPriority();

  int getWatchDogInterval();

  int getMaxRestartRetries();

  int getWatchDogTimeout();

}
