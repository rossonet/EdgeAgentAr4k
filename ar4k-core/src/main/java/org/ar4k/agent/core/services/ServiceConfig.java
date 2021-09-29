package org.ar4k.agent.core.services;

import java.util.List;

import org.ar4k.agent.core.ConfigSeed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * interfaccia da implementare per una configurazione di servizio valida
 *
 * @see org.ar4k.agent.config.AbstractServiceConfig
 *
 * @author andrea
 *
 */
@JsonTypeInfo(use = Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class-type")
public interface ServiceConfig extends ConfigSeed {

	int getMaxRestartRetries();

	boolean startOnInit();

	int getPriority();

	List<String> getProvides();

	List<String> getRequired();

	int getWatchDogInterval();

	int getWatchDogTimeout();

	@JsonIgnore
	EdgeComponent instantiate();

	@JsonIgnore
	boolean isSpringBean();

}
