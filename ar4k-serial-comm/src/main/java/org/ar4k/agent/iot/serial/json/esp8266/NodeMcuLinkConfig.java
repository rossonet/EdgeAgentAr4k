/**
 *
 */
package org.ar4k.agent.iot.serial.json.esp8266;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.config.ConfigSeed;

import com.google.gson.TypeAdapter;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
public class NodeMcuLinkConfig extends AbstractServiceConfig {

  private static final long serialVersionUID = 3483449400156969370L;

  @Override
  public NodeMcuLinkService instantiate() {
    NodeMcuLinkService ss = new NodeMcuLinkService();
    ss.setConfiguration(this);
    return ss;
  }

  @Override
  public boolean isSpringBean() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public TypeAdapter<? extends ConfigSeed> getJsonTypeAdapter() {
    // TODO Auto-generated method stub
    return null;
  }

}
