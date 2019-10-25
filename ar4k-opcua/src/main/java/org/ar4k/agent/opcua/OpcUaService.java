package org.ar4k.agent.opcua;

import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.AbstractAr4kService;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;

import com.google.gson.JsonElement;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio di connessione seriale.
 */
public class OpcUaService extends AbstractAr4kService {

  public enum SecurityMode {
    signAndEncrypt, sign, none
  }

  public enum CryptoMode {
    basic256Sha256, basic256, basic128Rsa15, none
  }

  public enum DeadbandType {
    none, absolute, percent
  }

  public enum DataChangeTrigger {
    status, statusOrValue, statusOrValueOrTimestamp
  }

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(OpcUaService.class.toString());

  @Override
  public void stop() {
    // TODO Auto-generated method stub

  }

  @Override
  public void init() {
    // TODO Auto-generated method stub

  }

  @Override
  public void setConfiguration(ConfigSeed configuration) {
    // TODO Auto-generated method stub

  }

  @Override
  public JsonElement getStatusJson() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void close() throws Exception {
    // TODO Auto-generated method stub

  }

}
