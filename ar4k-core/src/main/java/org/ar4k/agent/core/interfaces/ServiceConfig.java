package org.ar4k.agent.core.interfaces;

import java.util.List;

/**
 * interfaccia da implementare per una configurazione di servizio valida
 *
 * @see org.ar4k.agent.config.AbstractServiceConfig
 *
 * @author andrea
 *
 */
public interface ServiceConfig extends ConfigSeed {

	int getMaxRestartRetries();
	
	boolean startOnInit();

	int getPriority();

	List<String> getProvides();

	List<String> getRequired();

	int getWatchDogInterval();

	int getWatchDogTimeout();

	EdgeComponent instantiate();

	boolean isSpringBean();

}
