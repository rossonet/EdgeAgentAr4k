package org.ar4k.agent.tunnels.ssh.client;

import com.beust.jcommander.Parameter;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione servizio tunnel SSH
 */

public class SshLocalConfig extends AbstractSshConfig {

	@Override
	public String toString() {
		return "SshLocalConfig [redirectServer=" + redirectServer + ", redirectPort=" + redirectPort + ", bindHost="
				+ bindHost + ", bindPort=" + bindPort + "] " + super.toString();
	}

	private static final long serialVersionUID = 6322932417278452420L;

	@Parameter(names = "--redirectServer", description = "server to forward the connection")
	public String redirectServer = null;

	@Parameter(names = "--redirectPort", description = "port to forward the connection")
	public int redirectPort = 2200;

	@Parameter(names = "--bindHost", description = "If bindHost is an empty string or \"*\", the port should be available from all interfaces. If bind_address is \"localhost\" or null, the listening port will be bound for local use only.")
	public String bindHost = null;

	@Parameter(names = "--bindPort", description = "local port to bind for the connection, should be 0 for auto discovery")
	public int bindPort = 0;

	@Override
	public SshLocalTunnel instantiate() {
		final SshLocalTunnel ss = new SshLocalTunnel();
		ss.setConfiguration(this);
		return ss;
	}

	@Override
	public boolean isSpringBean() {
		return false;
	}

}
