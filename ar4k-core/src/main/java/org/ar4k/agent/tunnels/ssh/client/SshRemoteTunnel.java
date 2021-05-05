package org.ar4k.agent.tunnels.ssh.client;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio tunnel SSH.
 *
 */
public class SshRemoteTunnel extends AbstractSshTunnel {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(SshRemoteTunnel.class.toString());

	private boolean tunnelReturn = true;

	private void startTunnel() {
		try {
			connect().setPortForwardingR(((SshRemoteConfig) configuration).bindHost,
					((SshRemoteConfig) configuration).bindPort, ((SshRemoteConfig) configuration).redirectServer,
					((SshRemoteConfig) configuration).redirectPort);
			tunnelReturn = true;
		} catch (final Exception e) {
			logger.logException("ssh tunnel", e);
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
