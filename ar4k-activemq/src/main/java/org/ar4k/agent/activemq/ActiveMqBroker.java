package org.ar4k.agent.activemq;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.security.Role;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.apache.activemq.artemis.core.server.metrics.MetricsManager;
import org.apache.activemq.artemis.spi.core.security.ActiveMQSecurityManager;
import org.apache.activemq.artemis.utils.critical.CriticalAnalyzerPolicy;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;

public class ActiveMqBroker implements AutoCloseable {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(ActiveMqBroker.class.toString());

  public ActiveMqBroker(ActiveMQSecurityManager securityManager, String portMqtt, String portMqtts,
      String portWebService, String keystoreActiceMqPassword, String keystoreActiveMq) {
    super();
    this.securityManager = securityManager;
    this.portMqtt = portMqtt;
    this.keystoreActiceMqPassword = keystoreActiceMqPassword;
    this.keystoreActiveMq = keystoreActiveMq;
    this.portMqtts = portMqtts;
    this.portWebService = portWebService;
  }

  private final String portMqtt;
  private final String keystoreActiceMqPassword;
  private final String keystoreActiveMq;
  private final String portMqtts;
  private final String portWebService;

  private final EmbeddedActiveMQ embedded = new EmbeddedActiveMQ();
  private final Configuration config = new ConfigurationImpl();
  private final Map<String, Set<Role>> roles = new HashMap<>();

  private ActiveMQSecurityManager securityManager = null;

  public void start() throws Exception {
    config.addAcceptorConfiguration("in-vm", "vm://0");
    config.addAcceptorConfiguration("mqtt", "tcp://0.0.0.0:" + portMqtt + "?protocols=MQTT");
    config.addAcceptorConfiguration("mqtts",
        "tcp://0.0.0.0:" + portMqtts + "?protocols=MQTT&sslEnabled=true&keyStorePath=" + keystoreActiveMq
            + "&keyStorePassword=" + keystoreActiceMqPassword);
    // web lo usa
    config.addAcceptorConfiguration("ws-mqtt", "tcp://0.0.0.0:" + portWebService + "?protocols=STOMP");
    config.setPersistenceEnabled(false);
    config.setJournalDatasync(false);
    config.setSecurityEnabled(true);
    config.setMessageCounterEnabled(false);
    config.setJMXManagementEnabled(false);
    config.setPersistIDCache(false);
    config.setJournalSyncTransactional(false);
    config.setJournalSyncNonTransactional(false);
    // web lo usa
    config.setWildcardRoutingEnabled(true);
    config.setCriticalAnalyzerPolicy(CriticalAnalyzerPolicy.SHUTDOWN);
    config.setSecurityRoles(roles);
    // TODO di slow-consumer-policy
    // configQueue.setAddress("broker-rpc");
    // config.addQueueConfiguration(configQueue);
    embedded.setSecurityManager(securityManager);
    embedded.setConfiguration(config);
    embedded.start();
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

}
