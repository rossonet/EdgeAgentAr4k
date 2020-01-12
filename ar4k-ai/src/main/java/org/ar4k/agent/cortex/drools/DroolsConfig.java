package org.ar4k.agent.cortex.drools;

import java.util.Collection;
import java.util.Map;

import org.ar4k.agent.config.AbstractServiceConfig;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione Drools collegata all'agente.
 */
public class DroolsConfig extends AbstractServiceConfig {

  private static final long serialVersionUID = -5184060109145099045L;
  @Parameter(names = "--aiName", description = "unique name for the AI")
  public String aiName = "beelzebub";

  @Override
  public DroolsService instantiate() {
    DroolsService ss = new DroolsService();
    ss.setConfiguration(this);
    return ss;
  }

  @Override
  public int getPriority() {
    return 7;
  }

  @Override
  public boolean isSpringBean() {
    return false;
  }

  public boolean isStateless() {
    // TODO Auto-generated method stub
    return false;
  }

  public String getSessionName() {
    // TODO Auto-generated method stub
    return null;
  }

  public Collection<String> getUrlModules() {
    // TODO Auto-generated method stub
    return null;
  }

  public Collection<String> getFileModules() {
    // TODO Auto-generated method stub
    return null;
  }

  public Collection<String> getStringModules() {
    // TODO Auto-generated method stub
    return null;
  }

  public Map<String, String> getGlobalData() {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean insertAnima() {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean insertDataAddress() {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean insertDataStore() {
    // TODO Auto-generated method stub
    return false;
  }

  public String getBasePath() {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean isOpenNlpEnable() {
    // TODO Auto-generated method stub
    return false;
  }
}
