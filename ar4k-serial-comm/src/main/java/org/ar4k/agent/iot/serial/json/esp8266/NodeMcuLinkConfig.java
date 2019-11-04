/**
 *
 */
package org.ar4k.agent.iot.serial.json.esp8266;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.config.ConfigSeed;

import com.beust.jcommander.Parameter;
import com.google.gson.TypeAdapter;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
public class NodeMcuLinkConfig extends AbstractServiceConfig {

  private static final long serialVersionUID = 674536602166202714L;

  @Parameter(names = "--serverPort", description = "port for the NODEMCU collector server")
  public Integer serverPort = 45347;
  @Parameter(names = "--bindAddress", description = "bind address for the server")
  public String bindAddress = "0.0.0.0";
  @Parameter(names = "--discoveryPort", description = "the port for the UDP flash -discovery-. if 0 then then flash will be stopped", required = true)
  public int discoveryPort = 37466;
  @Parameter(names = "--broadcastAddress", description = "the broadcast address for the discovery UDP packet", required = true)
  public String broadcastAddress = "255.255.255.255";
  @Parameter(names = "--defaultChannel", description = "default channel for message without tag for destination")
  public String dafaultChannel = "nodemcu-json-default";
  @Parameter(names = "--optStringRoute", description = "string variable name in json for the route of messages")
  public String optStringRoute = "channel-route";
  @Parameter(names = "--fatherOfChannels", description = "directory channel for message topics")
  public String fatherOfChannels = null;
  @Parameter(names = "--scopeOfChannels", description = "scope for the parent channel. If null take the default of the address space")
  public String scopeOfChannels = null;
  @Parameter(names = "--insertDeviceInIdChannel", description = "if true the device id is inserted in channel name")
  public Boolean insertDeviceInIdChannel = false;

  @Override
  public NodeMcuLinkService instantiate() {
    NodeMcuLinkService ss = new NodeMcuLinkService();
    ss.setConfiguration(this);
    return ss;
  }

  @Override
  public boolean isSpringBean() {
    return false;
  }

  @Override
  public TypeAdapter<? extends ConfigSeed> getJsonTypeAdapter() {
    // TODO Auto-generated method stub
    return null;
  }

}
