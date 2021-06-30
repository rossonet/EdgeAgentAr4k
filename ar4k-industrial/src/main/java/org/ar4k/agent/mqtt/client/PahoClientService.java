package org.ar4k.agent.mqtt.client;

import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio di connessione client Paho.
 */

public class PahoClientService implements EdgeComponent {

	private MqttClient mqttClient = null;
	private PahoClientConfig configuration = null;
	private DataAddress dataAddress = null;
	private Homunculus homunculus = null;
	private ServiceStatus serviceStatus = ServiceStatus.INIT;
	private List<MqttTopicSubscription> subscriptions = new ArrayList<>();

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(PahoClientService.class);

	@Override
	public void close() throws Exception {
		kill();
	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		return serviceStatus;
	}

	@Override
	public void init() throws ServiceInitException {
		try {
			MqttClientPersistence persistence = null;
			if (configuration.persistenceOnFileSystem != null && !configuration.persistenceOnFileSystem.isEmpty()) {
				persistence = new MqttDefaultFilePersistence(configuration.persistenceOnFileSystem);
			} else {
				persistence = new MemoryPersistence();
			}
			mqttClient = new MqttClient(configuration.broker, configuration.clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(configuration.cleanSession);
			connOpts.setAutomaticReconnect(true);
			connOpts.setConnectionTimeout(configuration.connectionTimeout);
			connOpts.setKeepAliveInterval(configuration.keepAliveInterval);
			connOpts.setMaxInflight(configuration.maxInflight);
			connOpts.setMaxReconnectDelay(configuration.maxReconnectDelay);
			if (configuration.userName != null && configuration.password != null && !configuration.password.isEmpty()
					&& !configuration.userName.isEmpty()) {
				connOpts.setUserName(configuration.userName);
				connOpts.setPassword(configuration.password.toCharArray());
			}
			mqttClient.connect(connOpts);
			subscribeTopics();
			serviceStatus = ServiceStatus.RUNNING;
		} catch (MqttException exception) {
			logger.logException(exception);
		}
	}

	private void subscribeTopics() {
		for (MqttTopicConfig t : configuration.subscriptions) {
			subscriptions.add(new MqttTopicSubscription(this, mqttClient, t));
		}

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
