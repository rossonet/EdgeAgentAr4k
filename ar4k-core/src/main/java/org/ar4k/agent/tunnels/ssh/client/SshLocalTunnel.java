package org.ar4k.agent.tunnels.ssh.client;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio tunnel SSH.
 *
 */
public class SshLocalTunnel extends AbstractSshTunnel {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(SshLocalTunnel.class.toString());

	private boolean tunnelReturn = true;

	private void startTunnel() {
		try {
			connect().setPortForwardingL(((SshLocalConfig) configuration).bindHost,
					((SshLocalConfig) configuration).bindPort, ((SshLocalConfig) configuration).redirectServer,
					((SshLocalConfig) configuration).redirectPort);
			tunnelReturn = true;
		} catch (final Exception e) {
			logger.logException(e);
			tunnelReturn = false;
		}
	}

	@Override
	public void init() {
		super.init();
		startTunnel();
	}

	@Override
	protected boolean isTunnelOk() {
		return tunnelReturn;
	}

}
