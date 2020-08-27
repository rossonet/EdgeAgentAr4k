package org.ar4k.agent.activemq;

import org.apache.activemq.artemis.api.core.client.ActiveMQClient;
import org.apache.activemq.artemis.api.core.client.ClientSession;
import org.apache.activemq.artemis.api.core.client.ClientSessionFactory;
import org.apache.activemq.artemis.api.core.client.ServerLocator;
import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.EdgeComponent;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.json.JSONObject;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio di broker MQTT ActiveMQ
 */
public class ActiveMqService implements EdgeComponent {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(ActiveMqService.class.toString());

	private ActiveMqBroker broker = null;

	private Anima anima = null;

	private DataAddress dataAddress;

	private ActiveMqConfig configuration;

	private void registerBean() {
		((ConfigurableApplicationContext) Anima.getApplicationContext()).getBeanFactory()
				.registerSingleton(configuration.beanName, this);
	}

	private void deregisterBean() {
		((ConfigurableApplicationContext) Anima.getApplicationContext()).getBeanFactory().destroyBean(this);
	}

	@Override
	public void close() throws Exception {
		kill();
	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		// TODO ACTIVEMQ verifica stato
		return ServiceStatus.RUNNING;
	}

	@Override
	public void init() throws ServiceInitException {
		try {
			broker = new ActiveMqBroker(configuration.secured ? new ActiveMqSecurityManager() : null,
					configuration.portMqtt, configuration.portMqttSsl, configuration.portWebService,
					anima.getMyIdentityKeystore().filePath(), anima.getMyIdentityKeystore().keystorePassword,
					configuration.discoveryName, configuration.broadcastPeriod, configuration.clusterName,
					configuration.groupAddress, configuration.groupPort, configuration.clusterRetryInterval,
					configuration.trunkPort, configuration.clusterTimeWait, configuration.clusterUnit,
					configuration.clusterIterations, configuration.clusterServers, configuration.maxHops,
					configuration.clusterStaticHosts);

			broker.start();
			registerBean();
		} catch (final Exception e) {
			logger.logException(e);
		}
	}

	@Override
	public void kill() {
		if (broker != null) {
			try {
				broker.stop();
				broker = null;
				deregisterBean();
			} catch (final Exception e) {
				logger.logException(e);
			}

		}
	}

	@Override
	public Anima getAnima() {
		return anima;
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
	public void setAnima(Anima anima) {
		this.anima = anima;
	}

	@Override
	public ServiceConfig getConfiguration() {
		return configuration;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		this.configuration = (ActiveMqConfig) configuration;
	}

	@Override
	public JSONObject getDescriptionJson() {
		// TODO ACTIVEMQ Auto-generated method stub
		return null;
	}

	public ActiveMqBroker getBroker() {
		return broker;
	}

	public ClientSession getClientSession() {
		ClientSession session = null;
		if (getBroker() != null && getBroker().getTransportConfigurations() != null) {
			try {
				final ServerLocator locator = ActiveMQClient.createServerLocator(
						(configuration.clusterName != null && !configuration.clusterName.isEmpty()),
						getBroker().getTransportConfigurations());
				final ClientSessionFactory factory = locator.createSessionFactory();
				session = factory.createSession();
				session.start();
			} catch (final Exception e) {
				logger.logException(e);
			}
		} else {
			if (getBroker() == null)
				logger.warn("broker is null " + getBroker());
			else
				logger.warn("broker transport is null " + getBroker().getTransportConfigurations());
		}
		return session;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ActiveMqService [");
		if (broker != null)
			builder.append("broker=").append(broker).append(", ");
		if (dataAddress != null)
			builder.append("dataAddress=").append(dataAddress).append(", ");
		if (configuration != null)
			builder.append("configuration=").append(configuration);
		builder.append("]");
		return builder.toString();
	}

}