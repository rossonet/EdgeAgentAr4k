package org.ar4k.agent.opcua.client;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.json.JSONObject;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio di connessione client OPCUA with Eclipse Milo.
 */
public class OpcUaClientService implements EdgeComponent {

  private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(OpcUaClientService.class.toString());

  @Override
  public void close() throws Exception {
    // TODO OPCUA Auto-generated method stub

  }

  @Override
  public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
    // TODO OPCUA Auto-generated method stub
    return null;
  }

  @Override
  public void init() throws ServiceInitException {
    // TODO OPCUA Auto-generated method stub

  }

  @Override
  public void kill() {
    // TODO OPCUA Auto-generated method stub

  }

  @Override
  public Homunculus getHomunculus() {
    // TODO OPCUA Auto-generated method stub
    return null;
  }

  @Override
  public DataAddress getDataAddress() {
    // TODO OPCUA Auto-generated method stub
    return null;
  }

  @Override
  public void setDataAddress(DataAddress dataAddress) {
    // TODO OPCUA Auto-generated method stub

  }

  @Override
  public void setHomunculus(Homunculus homunculus) {
    // TODO OPCUA Auto-generated method stub

  }

  @Override
  public ServiceConfig getConfiguration() {
    // TODO OPCUA Auto-generated method stub
    return null;
  }

  @Override
  public void setConfiguration(ServiceConfig configuration) {
    // TODO OPCUA Auto-generated method stub

  }

  @Override
  public JSONObject getDescriptionJson() {
    // TODO OPCUA Auto-generated method stub
    return null;
  }

}
