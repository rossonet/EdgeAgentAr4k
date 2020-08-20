package org.ar4k.agent.activemq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.activemq.artemis.api.core.BroadcastGroupConfiguration;
import org.apache.activemq.artemis.api.core.DiscoveryGroupConfiguration;
import org.apache.activemq.artemis.api.core.SimpleString;
import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.api.core.UDPBroadcastEndpointFactory;
import org.apache.activemq.artemis.core.config.ClusterConnectionConfiguration;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory;
import org.apache.activemq.artemis.core.remoting.impl.netty.TransportConstants;
import org.apache.activemq.artemis.core.security.CheckType;
import org.apache.activemq.artemis.core.security.Role;
import org.apache.activemq.artemis.core.server.cluster.impl.MessageLoadBalancingType;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.apache.activemq.artemis.core.server.metrics.MetricsManager;
import org.apache.activemq.artemis.core.settings.impl.AddressSettings;
import org.apache.activemq.artemis.spi.core.protocol.RemotingConnection;
import org.apache.activemq.artemis.spi.core.security.ActiveMQSecurityManager;
import org.apache.activemq.artemis.spi.core.security.ActiveMQSecurityManager3;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

public class ActiveMqBroker implements AutoCloseable {

	private static final String GUEST_MQTT_USER = "guest";
	private static final String NETTY_CONNECTOR = "netty-connector";
	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(ActiveMqBroker.class.toString());

	public ActiveMqBroker(ActiveMQSecurityManager securityManager, String portMqtt, String portMqtts,
			String portWebService, String keystoreActiveMq, String keystoreActiceMqPassword, String discoveryName,
			long broadcastPeriod, String clusterName, String groupAddress, int groupPort, long clusterRetryInterval,
			int trunkPort, long timeWait, TimeUnit unit, int iterations, int servers, int maxHops,
			List<String> staticConnectors) {
		if (staticConnectors != null)
			this.staticConnectors.addAll(staticConnectors);
		this.securityManager = securityManager;
		this.portMqtt = portMqtt;
		this.keystoreActiceMqPassword = keystoreActiceMqPassword;
		this.keystoreActiveMq = keystoreActiveMq;
		this.portMqtts = portMqtts;
		this.portWebService = portWebService;
		this.discoveryName = discoveryName;
		this.clusterName = clusterName;
		this.groupAddress = groupAddress;
		this.groupPort = groupPort;
		this.clusterRetryInterval = clusterRetryInterval;
		this.broadcastPeriod = broadcastPeriod;
		this.trunkPort = trunkPort;
		this.clusterActive = (clusterName != null && !clusterName.isEmpty());
		this.clusterTimeWait = timeWait;
		this.clusterUnit = unit;
		this.clusterIterations = iterations;
		this.clusterServers = servers;
		this.maxHops = maxHops;
	}

	private final String portMqtt;
	private final String keystoreActiceMqPassword;
	private final String keystoreActiveMq;
	private final String portMqtts;
	private final String portWebService;
	private final int trunkPort;

	private final EmbeddedActiveMQ embedded = new EmbeddedActiveMQ();
	private final Configuration config = new ConfigurationImpl();
	private final Map<String, Set<Role>> roles = new HashMap<>();

	private final ActiveMQSecurityManager securityManager;
	private final String discoveryName;
	private final String clusterName;
	private final String groupAddress;
	private final int groupPort;
	private final long clusterRetryInterval;
	private final boolean clusterActive;
	private final long broadcastPeriod;
	private TransportConfiguration connectorTransportConfig = null;
	private final long clusterTimeWait;
	private final TimeUnit clusterUnit;
	private final int clusterIterations;
	private final int clusterServers;
	private final int maxHops;
	private final List<String> staticConnectors = new ArrayList<>();

	public void start() throws Exception {
		final AddressSettings adr = new AddressSettings();
		adr.setDeadLetterAddress(new SimpleString("DLQ"));
		adr.setExpiryAddress(new SimpleString("ExpiryQueue"));
		adr.setAutoCreateAddresses(true);
		config.addAddressesSetting("#", adr);
		config.addAcceptorConfiguration("mqtt", "tcp://0.0.0.0:" + portMqtt + "?protocols=MQTT");
		config.addAcceptorConfiguration("mqtts",
				"tcp://0.0.0.0:" + portMqtts + "?protocols=MQTT&sslEnabled=true&keyStorePath=" + keystoreActiveMq
						+ "&keyStorePassword=" + keystoreActiceMqPassword);
		config.addAcceptorConfiguration("ws-mqtt", "tcp://0.0.0.0:" + portWebService + "?protocols=STOMP");
		config.addAcceptorConfiguration("netty-acceptor", "tcp://0.0.0.0:" + trunkPort + "?protocols=CORE");
		if (clusterActive) {
			final Map<String, Object> p = new HashMap<>();
			p.put(TransportConstants.PORT_PROP_NAME, trunkPort);
			connectorTransportConfig = new TransportConfiguration(NettyConnectorFactory.class.getName(), p);
			if (discoveryName != null) {
				final List<String> connectorInfos = new ArrayList<>();
				connectorInfos.add(NETTY_CONNECTOR);
				final UDPBroadcastEndpointFactory udpBrodcast = new UDPBroadcastEndpointFactory()
						.setGroupAddress(groupAddress).setGroupPort(groupPort);
				final BroadcastGroupConfiguration broadcastGroupConfig = new BroadcastGroupConfiguration()
						.setName(discoveryName).setBroadcastPeriod(broadcastPeriod).setConnectorInfos(connectorInfos)
						.setEndpointFactory(udpBrodcast);
				final DiscoveryGroupConfiguration discoveryGroupConfig = new DiscoveryGroupConfiguration()
						.setName(discoveryName).setRefreshTimeout(10000).setBroadcastEndpointFactory(udpBrodcast);
				config.addDiscoveryGroupConfiguration(discoveryName, discoveryGroupConfig);
				config.addBroadcastGroupConfiguration(broadcastGroupConfig);
			}
			final ClusterConnectionConfiguration clusterConnectionConfig = new ClusterConnectionConfiguration()
					.setName(clusterName).setConnectorName(NETTY_CONNECTOR).setRetryInterval(clusterRetryInterval)
					.setDuplicateDetection(true).setMessageLoadBalancingType(MessageLoadBalancingType.STRICT)
					.setMaxHops(maxHops);
			if (discoveryName != null)
				clusterConnectionConfig.setDiscoveryGroupName(discoveryName);
			if (!staticConnectors.isEmpty())
				clusterConnectionConfig.setStaticConnectors(staticConnectors);
			config.addClusterConfiguration(clusterConnectionConfig);
			config.addConnectorConfiguration(NETTY_CONNECTOR, connectorTransportConfig);

		}
		if (securityManager != null) {
			// TODO completare gestion password integrata
			config.setSecurityEnabled(true);
			config.setSecurityRoles(roles);
			embedded.setSecurityManager(securityManager);
		} else {
			config.setSecurityEnabled(true); // autenticare comunque
			final ActiveMQSecurityManager3 securityManagerGuest = new ActiveMQSecurityManager3() {
				@Override
				public boolean validateUser(String user, String password) {
					logger.info("user " + user + " authenticated");
					return true;
				}

				@Override
				public boolean validateUserAndRole(String user, String password, Set<Role> roles, CheckType checkType) {
					logger.info("user/role " + user + " authenticated");
					return true;
				}

				@Override
				public String validateUser(String user, String password, RemotingConnection remotingConnection) {
					logger.info("user " + user + " authenticated");
					return user;
				}

				@Override
				public String validateUserAndRole(String user, String password, Set<Role> roles, CheckType checkType,
						String address, RemotingConnection remotingConnection) {
					logger.info("user/role " + user + " authenticated");
					return user;
				}
			};
			final Set<Role> guestRoles = new HashSet<>();
			guestRoles.add(new Role(GUEST_MQTT_USER, true, true, true, true, true, true, true, true, true, true));
			final Map<String, Set<Role>> securityConfig = new HashMap<>();
			securityConfig.put(GUEST_MQTT_USER, guestRoles);
			config.setSecurityRoles(securityConfig);
			embedded.setSecurityManager(securityManagerGuest);
		}
		config.setPersistenceEnabled(false);
		embedded.setConfiguration(config);
		embedded.start();
		if (clusterActive) {
			embedded.waitClusterForming(clusterTimeWait, clusterUnit, clusterIterations, clusterServers);
		}
		logger.info("broker ActiveMQ started");
	}

	public void stop() throws Exception {
		logger.info("stopping broker ActiveMQ");
		embedded.stop();
	}

	public boolean isRunning() {
		return embedded.getActiveMQServer() != null ? embedded.getActiveMQServer().isActive() : false;
	}

	@Override
	public void close() throws Exception {
		stop();
	}

	public String getBrokerName() {
		return embedded != null ? embedded.getActiveMQServer().describe() : null;
	}

	public String getUptime() {
		return embedded != null ? embedded.getActiveMQServer().getUptime() : null;
	}

	public long getCurrentConnections() {
		return embedded != null ? embedded.getActiveMQServer().getTotalConnectionCount() : 0;
	}

	public MetricsManager getSystemUsage() {
		return embedded != null ? embedded.getActiveMQServer().getMetricsManager() : null;
	}

	public TransportConfiguration getTransportConfigurations() {
		return connectorTransportConfig;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ActiveMqBroker [");
		if (portMqtt != null)
			builder.append("portMqtt=").append(portMqtt).append(", ");
		if (keystoreActiceMqPassword != null)
			builder.append("keystoreActiceMqPassword=").append(keystoreActiceMqPassword).append(", ");
		if (keystoreActiveMq != null)
			builder.append("keystoreActiveMq=").append(keystoreActiveMq).append(", ");
		if (portMqtts != null)
			builder.append("portMqtts=").append(portMqtts).append(", ");
		if (portWebService != null)
			builder.append("portWebService=").append(portWebService).append(", ");
		builder.append("trunkPort=").append(trunkPort).append(", ");
		if (embedded != null)
			builder.append("embedded=").append(embedded).append(", ");
		if (config != null)
			builder.append("config=").append(config).append(", ");
		if (roles != null)
			builder.append("roles=").append(roles).append(", ");
		if (securityManager != null)
			builder.append("securityManager=").append(securityManager).append(", ");
		if (discoveryName != null)
			builder.append("discoveryName=").append(discoveryName).append(", ");
		if (clusterName != null)
			builder.append("clusterName=").append(clusterName).append(", ");
		if (groupAddress != null)
			builder.append("groupAddress=").append(groupAddress).append(", ");
		builder.append("groupPort=").append(groupPort).append(", clusterRetryInterval=").append(clusterRetryInterval)
				.append(", clusterActive=").append(clusterActive).append(", broadcastPeriod=").append(broadcastPeriod)
				.append(", ");
		if (connectorTransportConfig != null)
			builder.append("connectorTransportConfig=").append(connectorTransportConfig).append(", ");
		builder.append("clusterTimeWait=").append(clusterTimeWait).append(", ");
		if (clusterUnit != null)
			builder.append("clusterUnit=").append(clusterUnit).append(", ");
		builder.append("clusterIterations=").append(clusterIterations).append(", clusterServers=")
				.append(clusterServers).append(", maxHops=").append(maxHops).append(", ");
		if (staticConnectors != null)
			builder.append("staticConnectors=").append(staticConnectors);
		builder.append("]");
		return builder.toString();
	}
}
