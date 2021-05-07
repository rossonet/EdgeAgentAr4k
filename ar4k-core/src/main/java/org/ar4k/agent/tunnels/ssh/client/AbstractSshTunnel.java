
package org.ar4k.agent.tunnels.ssh.client;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.messages.StringMessage;
import org.ar4k.agent.core.interfaces.EdgeChannel;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.core.interfaces.ServiceConfig;
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
public abstract class AbstractSshTunnel implements EdgeComponent {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(AbstractSshTunnel.class.toString());

	protected AbstractSshConfig configuration = null;

	protected DataAddress dataspace;

	protected Homunculus homunculus;

	private JSch jsch = null;

	private Session session = null;

	private EdgeChannel statusChannel = null;

	@Override
	public void init() {
		statusChannel = dataspace.createOrGetDataChannel("status", IPublishSubscribeChannel.class,
				"status of ssh connection", (String) null, (String) null, null, this);
	}

	@Override
	public void close() {
		kill();
	}

	@Override
	public AbstractSshConfig getConfiguration() {
		return configuration;
	}

	@Override
	public DataAddress getDataAddress() {
		return dataspace;
	}

	@Override
	public JSONObject getDescriptionJson() {
		final Gson gson = new GsonBuilder().create();
		return new JSONObject(gson.toJsonTree(configuration).getAsString());
	}

	@Override
	public Homunculus getHomunculus() {
		return homunculus;
	}

	public JSch getJsch() {
		return jsch;
	}

	@Override
	public String getServiceName() {
		return getConfiguration().getName();
	}

	public Session getSession() {
		return session;
	}

	@Override
	public void kill() {
		if (session != null)
			session.disconnect();
		session = null;
		jsch = null;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		this.configuration = ((AbstractSshConfig) configuration);
	}

	@Override
	public void setDataAddress(DataAddress dataAddress) {
		dataspace = dataAddress;
	}

	@Override
	public void setHomunculus(Homunculus homunculus) {
		this.homunculus = homunculus;
	}

	@Override
	public String toString() {
		return configuration.getClass().getName() + " " + configuration;
	}

	@Override
	public synchronized ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		final StringMessage message = new StringMessage();
		if (isTunnelOk() && session != null && session.isConnected()) {
			message.setPayload(ServiceStatus.RUNNING.toString());
			statusChannel.getChannel().send(message);
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
			message.setPayload(ServiceStatus.STARTING.toString());
			statusChannel.getChannel().send(message);
			return ServiceStatus.STARTING;
		}
	}

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

	protected abstract boolean isTunnelOk();

}
