package org.ar4k.agent.modbus.slave;

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
 *         Servizio di connessione modbus slave
 */

// TODO completare servizio modbus slave
public class ModbusSlaveService implements EdgeComponent {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(ModbusSlaveService.class);

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
		// TODO OPCUA Auto-generated method stub
		return null;
	}

	@Override
	public DataAddress getDataAddress() {
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

	@Override
	public String getServiceName() {
		return getConfiguration().getName();
	}

}
