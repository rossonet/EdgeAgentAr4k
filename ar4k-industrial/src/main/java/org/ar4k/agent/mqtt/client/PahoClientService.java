package org.ar4k.agent.mqtt.client;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.core.interfaces.EdgeComponent.ServiceStatus;
import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.opcua.client.OpcUaClientConfig;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio di connessione client Paho.
 */

// TODO completare servizio client Paho
public class PahoClientService implements EdgeComponent {

	private MqttClient mqttClient = null;
	private PahoClientConfig configuration = null;
	private DataAddress dataAddress = null;
	private Homunculus homunculus = null;
	private ServiceStatus serviceStatus = ServiceStatus.INIT;

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(PahoClientService.class.toString());

	@Override
	public void close() throws Exception {
		kill();
	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void init() throws ServiceInitException {
		MqttClientPersistence persistence = null;
		if (configuration.persistenceOnFileSystem != null && !configuration.persistenceOnFileSystem.isEmpty()) {
			persistence = new MqttDefaultFilePersistence(configuration.persistenceOnFileSystem);
		} else {
			persistence = new MemoryPersistence();
		}
		mqttClient = new MqttClient(configuration.broker, configuration.clientId, persistence);

	}

	@Override
	public void kill() {
		if (mqttClient != null) {
			try {
				mqttClient.disconnect();
			} catch (MqttException exception) {
				logger.logException(exception);
			}
		}
		serviceStatus = ServiceStatus.KILLED;
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
	public ServiceConfig getConfiguration() {
		return configuration;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		this.configuration = (PahoClientConfig) configuration;
	}

	@Override
	public String getServiceName() {
		return getConfiguration().getName();
	}

}
