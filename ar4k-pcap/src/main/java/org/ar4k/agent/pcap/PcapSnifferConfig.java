/**
 *
 */
package org.ar4k.agent.pcap;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.config.ConfigSeed;

import com.google.gson.TypeAdapter;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
public class PcapSnifferConfig extends AbstractServiceConfig {

  private static final long serialVersionUID = 5360711461610245833L;

  @Override
  public PcapSnifferService instantiate() {
    PcapSnifferService ss = new PcapSnifferService();
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
