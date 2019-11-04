package org.ar4k.agent.opcua.server;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.AbstractAr4kService;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.json.JSONObject;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio di connessione opcua server.
 */
public class OpcUaServerService extends AbstractAr4kService {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(OpcUaServerService.class.toString());

  private Ar4kOpcUaServer server = null;

  private OpcUaServerConfig configuration = null;

  @Override
  public void stop() {
    if (server != null) {
      server.shutdown();
      server = null;
    }
  }

  @Override
  public void init() {
    initializeServerOpcUa();
  }

  @Override
  public AbstractServiceConfig getConfiguration() {
    return configuration;
  }

  @Override
  public void setConfiguration(ConfigSeed configuration) {
    this.configuration = (OpcUaServerConfig) configuration;
  }

  @Override
  public JSONObject getStatusJson() {
    // TODO: da completare
    return null;
  }

  @Override
  public void close() throws Exception {
    stop();
  }

  @Override
  public void loop() {
    initializeServerOpcUa();
  }

  private void initializeServerOpcUa() {
    if (server == null) {
      try {
        server = new Ar4kOpcUaServer(configuration);
        server.startup();
      } catch (Exception e) {
        logger.logException(e);
      }
    }
  }

  public Ar4kOpcUaServer getServer() {
    return server;
  }

}
