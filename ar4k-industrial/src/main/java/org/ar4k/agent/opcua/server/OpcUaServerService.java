package org.ar4k.agent.opcua.server;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.services.EdgeComponent;
import org.ar4k.agent.core.services.ServiceConfig;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio di connessione opcua server.
 */
public class OpcUaServerService implements EdgeComponent {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(OpcUaServerService.class);

	private Ar4kOpcUaServer server = null;

	private OpcUaServerConfig configuration = null;

	private Homunculus homunculus;

	private DataAddress dataAddress;

	@Override
	public void init() {
		initializeServerOpcUa();
	}

	@Override
	public AbstractServiceConfig getConfiguration() {
		return configuration;
	}

	@Override
	public void close() throws Exception {
		kill();
	}

	private void initializeServerOpcUa() {
		if (server == null) {
			try {
				server = new Ar4kOpcUaServer(configuration);
				server.startup();
				dataAddress.addCallbackOnChange(server.getNamespace());
			} catch (final Exception e) {
				logger.logException(e);
			}
		}
	}

	public Ar4kOpcUaServer getServer() {
		return server;
	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		if (server != null) {
			return server.updateAndGetStatus();
		} else {
			return ServiceStatus.INIT;
		}
	}

	@Override
	public void kill() {
		if (server != null) {
			server.shutdown();
			server = null;
		}
	}

	@Override
	public Homunculus getHomunculus() {
		return homunculus;
	}

	@Override
	public DataAddress getDataAddress() {
		return dataAddress;
	}

	@Override
	public void setDataAddress(DataAddress dataAddress) {
		this.dataAddress = dataAddress;
	}

	@Override
	public void setHomunculus(Homunculus homunculus) {
		this.homunculus = homunculus;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		this.configuration = (OpcUaServerConfig) configuration;
	}

	@Override
	public String getServiceName() {
		return getConfiguration().getName();
	}

}
