
package org.ar4k.agent.tunnels.ssh.client;

import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Ar4kComponent;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio tunnel SSH astratto.
 *
 */
public abstract class AbstractSshTunnel implements Ar4kComponent {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(AbstractSshTunnel.class.toString());

	protected AbstractSshConfig configuration = null;

	protected DataAddress dataSpace;

	protected Anima anima;

	private JSch jsch = null;

	private Session session = null;

	protected synchronized Session connect() {
		try {
			jsch = new JSch();
			if (configuration.authkey != null)
				jsch.addIdentity(configuration.authkey);
			session = jsch.getSession(configuration.username, configuration.host, configuration.port);
			final SSHUserInfo ui = new SSHUserInfo();
			if (configuration.password != null)
				ui.setPassword(configuration.password);
			ui.setTrust(configuration.trustAllCert);
			session.setUserInfo(ui);
			session.setDaemonThread(true);
			session.connect();
		} catch (final Exception e) {
			logger.logException(e);
		}
		return session;
	}

	@Override
	public synchronized ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		if (isTunnelOk() && session != null && session.isConnected()) {
			return ServiceStatus.RUNNING;
		} else {
			if (session != null) {
				session.disconnect();
				session = null;
			}
			if (jsch != null) {
				jsch = null;
			}
			this.init();
			return ServiceStatus.STARTING;
		}
	}

	protected abstract boolean isTunnelOk();

	@Override
	public AbstractSshConfig getConfiguration() {
		return configuration;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		this.configuration = ((AbstractSshConfig) configuration);
	}

	@Override
	public void close() {
		kill();
	}

	@Override
	public void kill() {
		if (session != null)
			session.disconnect();
		session = null;
		jsch = null;
	}

	public JSch getJsch() {
		return jsch;
	}

	public Session getSession() {
		return session;
	}

	@Override
	public Anima getAnima() {
		return anima;
	}

	@Override
	public DataAddress getDataAddress() {
		return dataSpace;
	}

	@Override
	public void setDataAddress(DataAddress dataAddress) {
		dataSpace = dataAddress;
	}

	@Override
	public void setAnima(Anima anima) {
		this.anima = anima;
	}

	@Override
	public String toString() {
		return configuration.getClass().getName() + " " + configuration;
	}

	@Override
	public JSONObject getDescriptionJson() {
		final Gson gson = new GsonBuilder().create();
		return new JSONObject(gson.toJsonTree(configuration).getAsString());
	}

}
