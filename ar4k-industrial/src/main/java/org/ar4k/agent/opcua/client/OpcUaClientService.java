package org.ar4k.agent.opcua.client;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio di connessione client OPCUA with Eclipse Milo.
 */

// TODO completare client opcua
public class OpcUaClientService implements EdgeComponent {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(OpcUaClientService.class.toString());

	@Override
	public void close() throws Exception {
		//  OPCUA Auto-generated method stub

	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		//  OPCUA Auto-generated method stub
		return null;
	}

	@Override
	public void init() throws ServiceInitException {
		//  OPCUA Auto-generated method stub

	}

	@Override
	public void kill() {
		//  OPCUA Auto-generated method stub

	}

	@Override
	public Homunculus getHomunculus() {
		//  OPCUA Auto-generated method stub
		return null;
	}

	@Override
	public DataAddress getDataAddress() {
		//  OPCUA Auto-generated method stub
		return null;
	}

	@Override
	public void setDataAddress(DataAddress dataAddress) {
		//  OPCUA Auto-generated method stub

	}

	@Override
	public void setHomunculus(Homunculus homunculus) {
		//  OPCUA Auto-generated method stub

	}

	@Override
	public ServiceConfig getConfiguration() {
		//  OPCUA Auto-generated method stub
		return null;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		//  OPCUA Auto-generated method stub

	}

	@Override
	public String getServiceName() {
		return getConfiguration().getName();
	}

}
