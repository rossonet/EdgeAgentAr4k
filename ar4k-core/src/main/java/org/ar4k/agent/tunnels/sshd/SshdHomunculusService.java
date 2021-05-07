package org.ar4k.agent.tunnels.sshd;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.sshd.common.channel.ChannelListener;
import org.apache.sshd.common.forward.PortForwardingEventListener;
import org.apache.sshd.common.future.CloseFuture;
import org.apache.sshd.common.future.SshFutureListener;
import org.apache.sshd.common.session.SessionListener;
import org.apache.sshd.common.session.helpers.AbstractSession;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
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
import org.json.JSONObject;
import org.springframework.shell.Shell;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Gestore servizio per connessioni sshd.
 *
 */
public class SshdHomunculusService implements EdgeComponent, SshFutureListener<CloseFuture>, SessionListener,
		ChannelListener, PortForwardingEventListener {

	private final static Gson gson = new GsonBuilder().create();

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(SshdHomunculusService.class.toString());
	// iniettata vedi set/get
	private SshdHomunculusConfig configuration = null;
	private DataAddress dataspace = null;

	private Homunculus homunculus = null;

	private SshServer server = null;

	private ServiceStatus serviceStatus = ServiceStatus.INIT;

	private EdgeChannel requestCommandChannel = null;

	private EdgeChannel replyCommandChannel = null;

	private EdgeChannel statusChannel = null;

	@Override
	public void close() throws IOException {
		kill();
	}

	@Override
	public SshdHomunculusConfig getConfiguration() {
		return configuration;
	}

	@Override
	public DataAddress getDataAddress() {
		return dataspace;
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
	public Homunculus getHomunculus() {
		return homunculus;
	}

	@Override
	public String getServiceName() {
		return getConfiguration().getName();
	}

	@Override
	public void init() {
		setDataspace();
		server = SshServer.setUpDefaultServer();
		final PasswordAuthenticator passwordAuthenticator = new HomunculusPasswordAuthenticator(homunculus);
		server.setPasswordAuthenticator(passwordAuthenticator);
		final PublickeyAuthenticator publickeyAuthenticator = new HomunculusPublickeyAuthenticator(
				Paths.get(ConfigHelper.resolveWorkingString(configuration.authorizedKeys, true)));
		server.setPublickeyAuthenticator(publickeyAuthenticator);
		server.setHost(configuration.broadcastAddress);
		server.setPort(configuration.port);
		server.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
		logger.warn("keys for sshd server generated");
		final HomunculusShellFactory shellFactory = new HomunculusShellFactory(
				Homunculus.getApplicationContext().getBean(Homunculus.class),
				Homunculus.getApplicationContext().getBean(Shell.class));
		server.setShellFactory(shellFactory);
		server.addCloseFutureListener(this);
		server.addSessionListener(this);
		server.addChannelListener(this);
		server.addPortForwardingEventListener(this);
		try {
			server.start();
			serviceStatus = ServiceStatus.RUNNING;
		} catch (final IOException e) {
			logger.logException(e);
		}

	}

	private void setDataspace() {
		requestCommandChannel = dataspace.createOrGetDataChannel("request", IPublishSubscribeChannel.class,
				"requested command on ssh", homunculus.getDataAddress().getSystemChannel(), (String) null,
				ConfigHelper.mergeTags(Arrays.asList("sshd-homunculus", "request"), getConfiguration().getTags()),
				this);
		replyCommandChannel = dataspace.createOrGetDataChannel("reply", IPublishSubscribeChannel.class,
				"reply command to ssh", homunculus.getDataAddress().getSystemChannel(), (String) null,
				ConfigHelper.mergeTags(Arrays.asList("sshd-homunculus", "reply"), getConfiguration().getTags()), this);
		statusChannel = dataspace.createOrGetDataChannel("status", IPublishSubscribeChannel.class,
				"status of ssh connection", homunculus.getDataAddress().getSystemChannel(), (String) null,
				ConfigHelper.mergeTags(Arrays.asList("sshd-homunculus", "status"), getConfiguration().getTags()), this);
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
	public void operationComplete(CloseFuture future) {
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
	public void setConfiguration(ServiceConfig configuration) {
		this.configuration = ((SshdHomunculusConfig) configuration);
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
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		final StringMessage message = new StringMessage();
		message.setPayload(serviceStatus.toString());
		statusChannel.getChannel().send(message);
		return serviceStatus;
	}

}
