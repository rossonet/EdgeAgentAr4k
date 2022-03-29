package org.ar4k.agent.service.commandManaged;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.services.EdgeComponent;
import org.ar4k.agent.core.services.ServiceConfig;
import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.exception.ServiceWatchDogException;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         command managed service
 *
 */
//TO______DO completare command managed service
public class CommandManagedService implements EdgeComponent {

	@Override
	public DataAddress getDataAddress() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public String getServiceName() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws Exception {
		// Auto-generated method stub
		
	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void init() throws ServiceInitException {
		// Auto-generated method stub
		
	}

	@Override
	public void kill() {
		// Auto-generated method stub
		
	}

	@Override
	public Homunculus getHomunculus() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void setDataAddress(DataAddress dataAddress) {
		// Auto-generated method stub
		
	}

	@Override
	public void setHomunculus(Homunculus homunculus) {
		// Auto-generated method stub
		
	}

	@Override
	public ServiceConfig getConfiguration() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		// Auto-generated method stub
		
	}

	public void pulse() {
		// TO______DO Auto-generated method stub
		
	}

	public String getDescriptionStatus() {
		// TO______DO Auto-generated method stub
		return null;
	}

	public void saveStatus() {
		// TO______DO Auto-generated method stub
		
	}

	public void loadStatus() {
		// TO______DO Auto-generated method stub
		
	}


}
