package org.ar4k.agent.core;

import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.json.JSONObject;

/**
 * interfaccia da implementare per un servizio gestito
 * 
 * @author andrea
 *
 */
public interface Ar4kComponent extends AutoCloseable {

  public static enum ServiceStatus {
    INIT, STARTING, STAMINAL, RUNNING, STOPPED, KILLED, FAULT
  }

  ServiceStatus updateAndGetStatus() throws ServiceWatchDogException;

  void init() throws ServiceInitException;

  void kill();

  Anima getAnima();

  DataAddress getDataAddress();

  void setDataAddress(DataAddress dataAddress);

  void setAnima(Anima anima);

  ServiceConfig getConfiguration();

  void setConfiguration(ServiceConfig configuration);

  JSONObject getDescriptionJson();

}
