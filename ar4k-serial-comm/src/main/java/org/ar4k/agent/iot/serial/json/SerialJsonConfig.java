/**
 *
 */
package org.ar4k.agent.iot.serial.json;

import org.ar4k.agent.iot.serial.SerialConfig;

import com.beust.jcommander.Parameter;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
public class SerialJsonConfig extends SerialConfig {

  private static final long serialVersionUID = 5360712941610245833L;

  @Parameter(names = "--preChannelName", description = "string before channel name")
  public String preChannelName = "serial-json_";
  @Parameter(names = "--defaultChannel", description = "default channel for message without tag for destination")
  public String dafaultChannel = "serial-json-default";
  @Parameter(names = "--optStringRoute", description = "string variable name in json for the route of messages")
  public String optStringRoute = "channel-route";
  @Parameter(names = "--fatherOfChannels", description = "directory channel for message topics")
  public String fatherOfChannels = null;
  @Parameter(names = "--scopeOfChannels", description = "scope for the parent channel. If null take the default of the address space")
  public String scopeOfChannels = null;

  @Override
  public SerialJsonService instantiate() {
    SerialJsonService ss = new SerialJsonService();
    ss.setConfiguration(this);
    return ss;
  }

}
