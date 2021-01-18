package org.ar4k.agent.bootstrap.recipes;

import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;

public class BootstrapViaSshConsole extends BootstrapViaLocalConsole {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BootstrapViaSshConsole.class.toString());
	private String hostname = null;
	private int port = 22;
	private String username = null;
	private String password = null;

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isProviderEndpointAndAuthRequired() {
		return (hostname == null) || (port == 0) || (username == null) || (password == null);
	}

	@Override
	public String descriptionProviderEndpointAndAuthRequired() {
		return "Please provide the ssh endpoint and password";
	}

	@Override
	public void shellProviderEndpointAndAuth() {
		try {
			hostname = AbstractShellHelper.readStringParameterOnTerminal("Remote ssh host");
			port = Integer.valueOf(AbstractShellHelper.readStringParameterOnTerminal("Remote ssh port"));
			username = AbstractShellHelper.readStringParameterOnTerminal("Remote ssh username");
			password = AbstractShellHelper.readStringParameterOnTerminal("Remote ssh password");
			String message = AnsiOutput.toString(AnsiColor.GREEN, username, AnsiColor.BLUE, "@", AnsiColor.GREEN,
					hostname, AnsiColor.BLUE, ":", AnsiColor.GREEN, port);
			AbstractShellHelper.printOnTerminal(AnsiColor.GREEN, "configured remote " + message);
		} catch (Exception a) {
			logger.logException(a);
		}
	}

	@Override
	public void setUp(String serverPort, String keystoreFile, String keystoreCa, String keystoreBeacon,
			String adminPassword, String discoveryPort, String beaconserverPort) {
		super.setUp(serverPort, keystoreFile, keystoreCa, keystoreBeacon, adminPassword, discoveryPort,
				beaconserverPort);
		// TODO pubblicazione ssh
	}

	@Override
	public void start() {
		// TODO remote start
	}

	@Override
	public void stop() {
		// TODO remote stop
	}
}
