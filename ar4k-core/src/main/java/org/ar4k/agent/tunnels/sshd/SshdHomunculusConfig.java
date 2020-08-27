package org.ar4k.agent.tunnels.sshd;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.EdgeComponent;

import com.beust.jcommander.Parameter;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione servizio connessione SSHD.
 *
 */
public class SshdHomunculusConfig extends AbstractServiceConfig {

	@Parameter(names = "--port", description = "the port for the SSHD service")
	public int port = 6661;
	@Parameter(names = "--bindHost", description = "bind which interface. All = 0.0.0.0")
	public String broadcastAddress = "0.0.0.0";
	@Parameter(names = "--authorizedKeys", description = "authorized keys file for ssh")
	public String authorizedKeys = "~/.ssh/authorized_keys";

	private static final long serialVersionUID = 6301077946480730173L;

	@Override
	public EdgeComponent instantiate() {
		final SshdHomunculusService ss = new SshdHomunculusService();
		ss.setConfiguration(this);
		return ss;
	}

	@Override
	public int getPriority() {
		return 15;
	}

	@Override
	public boolean isSpringBean() {
		return false;
	}

	@Override
	public String toString() {
		return "SshdHomunculusConfig [port=" + port + ", broadcastAddress=" + broadcastAddress + "]";
	}
}
