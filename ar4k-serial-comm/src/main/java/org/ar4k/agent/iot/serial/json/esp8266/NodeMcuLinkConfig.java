/**
 *
 */
package org.ar4k.agent.iot.serial.json.esp8266;

import org.ar4k.agent.iot.serial.SerialConfig;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
public class NodeMcuLinkConfig extends SerialConfig {

  private static final long serialVersionUID = 3483449400156969370L;

  @Override
  public NodeMcuLinkService instantiate() {
    NodeMcuLinkService ss = new NodeMcuLinkService();
    ss.setConfiguration(this);
    return ss;
  }

}
