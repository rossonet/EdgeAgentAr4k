package org.ar4k.agent.opcua.server;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Ar4kComponent;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.json.JSONObject;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio di connessione opcua server.
 */
public class OpcUaServerService implements Ar4kComponent {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(OpcUaServerService.class.toString());

  private Ar4kOpcUaServer server = null;

  private OpcUaServerConfig configuration = null;

  private Anima anima;

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
      } catch (Exception e) {
        logger.logException(e);
      }
    }
  }

  public Ar4kOpcUaServer getServer() {
    return server;
  }

  @Override
  public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void kill() {
    if (server != null) {
      server.shutdown();
      server = null;
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
  public void setConfiguration(ServiceConfig configuration) {
    this.configuration = (OpcUaServerConfig) configuration;
  }

  @Override
  public JSONObject getDescriptionJson() {
    // TODO Auto-generated method stub
    return null;
  }

}