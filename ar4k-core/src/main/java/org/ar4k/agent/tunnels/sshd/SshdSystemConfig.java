package org.ar4k.agent.tunnels.sshd;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.EdgeComponent;

import com.beust.jcommander.Parameter;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione servizio connessione sshd.
 *
 **/
public class SshdSystemConfig extends AbstractServiceConfig {

	private static final long serialVersionUID = -1567771487925577804L;

	@Parameter(names = "--port", description = "the port for the SSHD service")
	public int port = 6661;
	@Parameter(names = "--bindHost", description = "bind which interface. All = 0.0.0.0")
	public String bindHost = "0.0.0.0";
	@Parameter(names = "--cmd", description = "cmd to run when start a new session")
	public String cmd = "/bin/bash -i -l";
	@Parameter(names = "--authorizedKeys", description = "authorized keys file for ssh")
	public String authorizedKeys = "~/.ssh/authorized_keys";

	@Override
	public EdgeComponent instantiate() {
		final SshdSystemService ss = new SshdSystemService();
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
		return "SshdSystemConfig [port=" + port + ", bindHost=" + bindHost + ", cmd=" + cmd + "]";
	}
}
