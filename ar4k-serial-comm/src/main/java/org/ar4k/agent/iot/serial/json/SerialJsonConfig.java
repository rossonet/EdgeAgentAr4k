/**
 *
 */
package org.ar4k.agent.iot.serial.json;

import org.ar4k.agent.iot.serial.SerialConfig;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
public class SerialJsonConfig extends SerialConfig {

  private static final long serialVersionUID = 5360711461610245833L;

  @Override
  public SerialJsonService instantiate() {
    SerialJsonService ss = new SerialJsonService();
    ss.setConfiguration(this);
    return ss;
  }

}
