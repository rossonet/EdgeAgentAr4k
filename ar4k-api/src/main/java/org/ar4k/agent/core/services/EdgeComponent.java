package org.ar4k.agent.core.services;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.DataServiceOwner;
import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.exception.ServiceWatchDogException;

/**
 * interfaccia da implementare per un servizio gestito
 *
 * @author andrea
 *
 */
public interface EdgeComponent extends DataServiceOwner, AutoCloseable {

	public static enum ServiceStatus {
		INIT, STARTING, STAMINAL, RUNNING, STOPPED, KILLED, FAULT
	}

	ServiceStatus updateAndGetStatus() throws ServiceWatchDogException;

	void init() throws ServiceInitException;

	void kill();

	Homunculus getHomunculus();

	void setDataAddress(DataAddress dataAddressBase);

	void setHomunculus(Homunculus homunculusBase);

	ServiceConfig getConfiguration();

	void setConfiguration(ServiceConfig configuration);

}
