package org.ar4k.agent.tunnels.sshd;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.sshd.common.channel.ChannelListener;
import org.apache.sshd.common.forward.DefaultForwarderFactory;
import org.apache.sshd.common.forward.PortForwardingEventListener;
import org.apache.sshd.common.future.CloseFuture;
import org.apache.sshd.common.future.SshFutureListener;
import org.apache.sshd.common.session.Session;
import org.apache.sshd.common.session.SessionListener;
import org.apache.sshd.common.session.helpers.AbstractSession;
import org.apache.sshd.common.util.net.SshdSocketAddress;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.forward.AcceptAllForwardingFilter;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.scp.ScpCommandFactory;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.messages.StringMessage;
import org.ar4k.agent.core.interfaces.EdgeChannel;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.sshd.firstCommand.HomunculusProcessShellFactory;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Gestore servizio per connessioni sshd
 *
 **/
public class SshdSystemService implements EdgeComponent, SshFutureListener<CloseFuture>, SessionListener,
		ChannelListener, PortForwardingEventListener {

	@Override
	public void establishingExplicitTunnel(Session session, SshdSocketAddress local, SshdSocketAddress remote,
			boolean localForwarding) throws IOException {
		if (local != null && remote != null) {
			logger.info("new ssh tunnel from " + local.getHostName() + ":" + local.getPort() + " to "
					+ remote.getHostName() + ":" + remote.getPort());
		}
		PortForwardingEventListener.super.establishingExplicitTunnel(session, local, remote, localForwarding);
	}

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(SshdSystemService.class.toString());

	// iniettata vedi set/get
	private SshdSystemConfig configuration = null;
	private SshServer server = null;
	private final static Gson gson = new GsonBuilder().create();

	private Homunculus homunculus = null;

	private DataAddress dataspace = null;

	private ServiceStatus serviceStatus = ServiceStatus.INIT;

	private EdgeChannel requestCommandChannel = null;

	private EdgeChannel replyCommandChannel = null;

	private EdgeChannel statusChannel = null;

	@Override
	public SshdSystemConfig getConfiguration() {
		return configuration;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		this.configuration = ((SshdSystemConfig) configuration);
	}

	@Override
	public synchronized void init() {
		setDataspace();
		server = SshServer.setUpDefaultServer();
		final PasswordAuthenticator passwordAuthenticator = new HomunculusPasswordAuthenticator(homunculus);
		server.setPasswordAuthenticator(passwordAuthenticator);
		final PublickeyAuthenticator publickeyAuthenticator = new HomunculusPublickeyAuthenticator(
				Paths.get(ConfigHelper.resolveWorkingString(configuration.authorizedKeys, true)));
		server.setPublickeyAuthenticator(publickeyAuthenticator);
		server.setHost(configuration.bindHost);
		server.setPort(configuration.port);
		server.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
		final ProcessShellFactory shellFactory = new HomunculusProcessShellFactory(configuration.cmd.split("\\s+"));
		server.setShellFactory(shellFactory);
		server.setCommandFactory(new ScpCommandFactory());
		server.setSubsystemFactories(Collections.singletonList(new SftpSubsystemFactory()));
		server.addCloseFutureListener(this);
		server.addSessionListener(this);
		server.addChannelListener(this);
		server.setForwardingFilter(AcceptAllForwardingFilter.INSTANCE);
		// ForwardingFilterFactory forwarderFactory = new Ar4kForwardingFilterFactory();
		server.setForwarderFactory(DefaultForwarderFactory.INSTANCE);
		server.addPortForwardingEventListener(this);
		// server.setFileSystemFactory(new VirtualFileSystemFactory());
		try {
			server.start();
			serviceStatus = ServiceStatus.RUNNING;
		} catch (final IOException e) {
			logger.logException(e);
		}
	}

	private void setDataspace() {
		requestCommandChannel = dataspace.createOrGetDataChannel("request", IPublishSubscribeChannel.class,
				"requested command on ssh", (String) null, (String) null, null, this);
		replyCommandChannel = dataspace.createOrGetDataChannel("reply", IPublishSubscribeChannel.class,
				"reply command to ssh", (String) null, (String) null, null, this);
		statusChannel = dataspace.createOrGetDataChannel("status", IPublishSubscribeChannel.class,
				"status of ssh connection", (String) null, (String) null, null, this);
	}

	@Override
	public void close() throws IOException {
		kill();
	}

	@Override
	public void kill() {
		serviceStatus = ServiceStatus.KILLED;
		if (server != null)
			try {
				server.stop();
				server.close();
			} catch (final IOException e) {
				logger.logException(e);
			}
	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		final StringMessage message = new StringMessage();
		message.setPayload(serviceStatus.toString());
		statusChannel.getChannel().send(message);
		return serviceStatus;
	}

	@Override
	public Homunculus getHomunculus() {
		return homunculus;
	}

	@Override
	public DataAddress getDataAddress() {
		return dataspace;
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
	public JSONObject getDescriptionJson() {
		final Map<String, String> data = new HashMap<>();
		if (server != null) {
			int sc = 0;
			for (final SocketAddress ia : server.getBoundAddresses()) {
				if (ia instanceof InetSocketAddress) {
					final InetSocketAddress a = (InetSocketAddress) ia;
					final InetAddress inetAddress = a.getAddress();
					final int inetPort = a.getPort();
					if (inetAddress instanceof Inet4Address)
						data.put("bound-address-ipv4-" + sc, inetAddress.toString() + ":" + inetPort);
					else if (inetAddress instanceof Inet6Address)
						data.put("bound-address-ipv6-" + sc, inetAddress.toString() + ":" + inetPort);
					else
						data.put("bound-address-" + sc, inetAddress.toString());
				} else {
					logger.debug("not an internet protocol socket..");
				}
				sc++;
			}
			int ssc = 0;
			for (final AbstractSession s : server.getActiveSessions()) {
				data.put("active-session-" + ssc, s.toString());
				ssc++;
			}
			data.put("version", server.getVersion());
		}
		data.put("configuration", configuration.toString());
		final String json = gson.toJson(data);
		return new JSONObject(json);
	}

	@Override
	public void operationComplete(CloseFuture future) {
		reloadServer();
	}

	public synchronized void reloadServer() {
		if (serviceStatus.equals(ServiceStatus.RUNNING)) {
			logger.info("server sshd closed in running state, restart after 60 seconds");
			kill();
			try {
				Thread.sleep(5000);
			} catch (final InterruptedException e) {
				logger.logExceptionDebug(e);
			}
			init();
		}
	}

	@Override
	public String getServiceName() {
		return getConfiguration().getName();
	}

}
