/**
 *
 */
package org.ar4k.agent.pcap.service;

import java.util.Collection;
import java.util.HashSet;

import org.ar4k.agent.config.AbstractServiceConfig;

import com.beust.jcommander.Parameter;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
public class PcapSnifferConfig extends AbstractServiceConfig {

	private static final long serialVersionUID = 5360711461610245833L;

	@Parameter(names = "--readers", description = "List of pcap acquisition points", variableArity = true)
	public Collection<PcapReader> readers = new HashSet<>();

	@Parameter(names = "--writers", description = "List of pcap writer points", variableArity = true)
	public Collection<PcapWriter> writers = new HashSet<>();

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

}
