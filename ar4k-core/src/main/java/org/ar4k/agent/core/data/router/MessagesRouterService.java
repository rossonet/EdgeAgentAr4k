package org.ar4k.agent.core.data.router;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.EdgeAgentCore;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.DataAddressBase;
import org.ar4k.agent.core.services.EdgeComponent;
import org.ar4k.agent.core.services.ServiceConfig;
import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.exception.ServiceWatchDogException;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         router messages service
 *
 */
//TODO completare router messaggi
public class MessagesRouterService implements EdgeComponent {

	@Override
	public DataAddressBase getDataAddress() {
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
	public EdgeAgentCore getHomunculus() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void setDataAddress(DataAddress dataAddressBase) {
		// Auto-generated method stub

	}

	@Override
	public void setHomunculus(Homunculus homunculusBase) {
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

}
