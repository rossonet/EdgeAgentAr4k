package org.ar4k.agent.activemq;

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
 *         Servizio di broker MQTT ActiveMQ
 */
public class ActiveMqService implements Ar4kComponent {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(ActiveMqService.class.toString());

  private ActiveMqBroker broker = null;

  private Anima anima = null;

  private DataAddress dataAddress;

  private ActiveMqConfig configuration;

  @Override
  public void close() throws Exception {
    kill();
  }

  @Override
  public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
    // TODO ACTIVEMQ Auto-generated method stub
    return ServiceStatus.RUNNING;
  }

  @Override
  public void init() throws ServiceInitException {
    broker = new ActiveMqBroker(new ActiveMqSecurityManager(), configuration.portMqtt, configuration.portMqttSsl,
        configuration.portWebService, anima.getMyIdentityKeystore().filePath(),
        anima.getMyIdentityKeystore().keystorePassword);
  }

  @Override
  public void kill() {
    if (broker != null) {
      try {
        broker.stop();
        broker = null;
      } catch (Exception e) {
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

}
