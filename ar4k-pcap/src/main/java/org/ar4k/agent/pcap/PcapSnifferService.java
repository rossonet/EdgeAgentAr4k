package org.ar4k.agent.pcap;

import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Ar4kComponent;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.json.JSONObject;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
public class PcapSnifferService implements Ar4kComponent {

  @Override
  public void init() {
    // TODO Auto-generated method stub

  }

  @Override
  public void setConfiguration(ServiceConfig configuration) {
    // TODO Auto-generated method stub

  }

  @Override
  public void close() throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public ServiceStates updateAndGetStatus() throws ServiceWatchDogException {
    // TODO Auto-generated method stub
    return null;
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
  public JSONObject getDescriptionJson() {
    // TODO Auto-generated method stub
    return null;
  }

}
