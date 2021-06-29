package org.ar4k.agent.tunnels.sshd;

import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.shell.ShellFactory;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.springframework.shell.Shell;

public class HomunculusShellFactory implements ShellFactory {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(HomunculusShellFactory.class);

	private final Homunculus homunculus;
	private final Shell shell;

	public HomunculusShellFactory(Homunculus homunculus, Shell shell) {
		this.homunculus = homunculus;
		this.shell = shell;
		logger.warn("Remote connection from SSH to the agent");
	}

	@Override
	public Command create() {
		SshdHomunculusCommand command = new SshdHomunculusCommand(homunculus, shell);
		return command;
	}

}
