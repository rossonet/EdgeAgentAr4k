package org.ar4k.agent.bootstrap.recipes;

import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.ssh.client.SSHUserInfo;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class BootstrapViaSshConsole extends BootstrapViaLocalConsole {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BootstrapViaSshConsole.class.toString());
	private static final int TIMEOUT = 60000;
	private String hostname = null;
	private int port = 22;
	private String username = null;
	private String password = null;
	private String privateKeyFile = null;
	private String remotePath = null;
	private JSch jsch = null;

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isProviderEndpointAndAuthRequired() {
		return (hostname == null) || (port == 0) || (username == null) || (password == null && privateKeyFile == null)
				|| (remotePath == null);
	}

	@Override
	public String descriptionProviderEndpointAndAuthRequired() {
		return "Please provide the ssh endpoint and authentication parameters";
	}

	@Override
	public void shellProviderEndpointAndAuth() {
		try {
			hostname = AbstractShellHelper.readStringParameterOnTerminal("Remote ssh host");
			port = Integer.valueOf(AbstractShellHelper.readStringParameterOnTerminal("Remote ssh port"));
			username = AbstractShellHelper.readStringParameterOnTerminal("Remote ssh username");
			final String authMethod = AbstractShellHelper
					.readStringParameterOnTerminal("Remote authentication with password or key (password|key)");
			if (authMethod.equals("key")) {
				privateKeyFile = AbstractShellHelper.readStringParameterOnTerminal("Remote private key path");
			} else {
				password = AbstractShellHelper.readStringParameterOnTerminal("Remote ssh password");
			}
			remotePath = AbstractShellHelper.readStringParameterOnTerminal("Path to install on remote server");
			String message = AnsiOutput.toString(AnsiColor.GREEN, username, AnsiColor.BLUE, "@", AnsiColor.GREEN,
					hostname, AnsiColor.BLUE, ":", AnsiColor.GREEN, port, remotePath);
			AbstractShellHelper.printOnTerminal(AnsiColor.GREEN, "configured remote " + message);
			if (password != null) {
				AbstractShellHelper.printOnTerminal(AnsiColor.RED, "authentication with password");
			} else {
				AbstractShellHelper.printOnTerminal(AnsiColor.RED, "authentication with private key");
			}
		} catch (Exception a) {
			logger.logException(a);
		}
	}

	@Override
	public void setUp(String serverPort, String keystoreFile, String keystoreCa, String keystoreBeacon,
			String adminPassword, String discoveryPort, String beaconserverPort) {
		super.setUp(serverPort, keystoreFile, keystoreCa, keystoreBeacon, adminPassword, discoveryPort,
				beaconserverPort);
		try {
			Session session = connect();
			if (session != null && session.isConnected()) {
				ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
				channel.connect(TIMEOUT);
				channel.cd(remotePath);
				logger.warn("copy " + shellInterface.getRunningProject().getFileSystemPath().toFile().getAbsolutePath()
						+ "/app.jar to " + remotePath);
				channel.put(
						shellInterface.getRunningProject().getFileSystemPath().toFile().getAbsolutePath() + "/app.jar",
						"app.jar");
				logger.warn("copy " + shellInterface.getRunningProject().getFileSystemPath().toFile().getAbsolutePath()
						+ "/application.properties to " + remotePath);
				channel.put(shellInterface.getRunningProject().getFileSystemPath().toFile().getAbsolutePath()
						+ "/application.properties", "application.properties");
				channel.mkdir("keys");
				channel.cd("keys");
				logger.warn("copy " + shellInterface.getRunningProject().getFileSystemPath().toFile().getAbsolutePath()
						+ "/keys/master.ks to " + remotePath + "/keys");
				channel.put(shellInterface.getRunningProject().getFileSystemPath().toFile().getAbsolutePath()
						+ "/keys/master.ks", "master.ks");

				channel.disconnect();
				session.disconnect();
			} else {
				logger.error("ssh connection doesn't work");
			}
		} catch (Exception e) {
			logger.logException(e);
		}
	}

	@Override
	public void start() {
		// TODO remote start
	}

	@Override
	public void stop() {
		// TODO remote stop
	}

	private synchronized Session connect() {
		Session session = null;
		try {
			jsch = new JSch();
			if (privateKeyFile != null)
				jsch.addIdentity(privateKeyFile);
			session = jsch.getSession(username, hostname, port);
			session.setConfig("StrictHostKeyChecking", "no");
			final SSHUserInfo ui = new SSHUserInfo();
			if (password != null)
				ui.setPassword(password);
			ui.setTrust(true);
			session.setUserInfo(ui);
			session.setDaemonThread(true);
			session.connect(TIMEOUT);
		} catch (final Exception e) {
			logger.logException(e);
		}
		return session;
	}
}
