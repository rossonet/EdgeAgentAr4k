
package org.ar4k.agent.tunnels.ssh.client;

import java.util.Arrays;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.channels.EdgeChannel;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.messages.StringMessage;
import org.ar4k.agent.core.services.EdgeComponent;
import org.ar4k.agent.core.services.ServiceConfig;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio tunnel SSH astratto.
 *
 */
public abstract class AbstractSshTunnel implements EdgeComponent {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(AbstractSshTunnel.class);

	protected AbstractSshConfig configuration = null;

	protected DataAddress dataspace;

	protected Homunculus homunculus;

	private JSch jsch = null;

	private Session session = null;

	private EdgeChannel statusChannel = null;

	@Override
	public void init() {
		statusChannel = dataspace.createOrGetDataChannel("status", IPublishSubscribeChannel.class,
				"status of ssh connection", homunculus.getDataAddress().getSystemChannel(), (String) null,
				ConfigHelper.mergeTags(Arrays.asList("ssh-tunnel", "status"), getConfiguration().getTags()), this);
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AbstractSshTunnel [");
		if (jsch != null) {
			builder.append("jsch=");
			builder.append(jsch);
			builder.append(", ");
		}
		if (session != null) {
			builder.append("session=");
			builder.append(session);
			builder.append(", ");
		}
		if (statusChannel != null) {
			builder.append("statusChannel=");
			builder.append(statusChannel);
		}
		builder.append("]");
		return builder.toString();
	}

}
