package org.ar4k.agent.opcua.client;

import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Ar4kComponent;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.json.JSONObject;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio di connessione client OPCUA with Eclipse Milo.
 */
public class OpcUaClientService implements Ar4kComponent {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(OpcUaClientService.class.toString());

  @Override
  public void close() throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void init() throws ServiceInitException {
    // TODO Auto-generated method stub

  }

  @Override
  public void kill() {
    // TODO Auto-generated method stub

  }

  @Override
  public Anima getAnima() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DataAddress getDataAddress() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setDataAddress(DataAddress dataAddress) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setAnima(Anima anima) {
    // TODO Auto-generated method stub

  }

  @Override
  public ServiceConfig getConfiguration() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setConfiguration(ServiceConfig configuration) {
    // TODO Auto-generated method stub

  }

  @Override
  public JSONObject getDescriptionJson() {
    // TODO Auto-generated method stub
    return null;
  }

}
