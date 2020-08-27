package org.ar4k.agent.tunnels.sshd.firstCommand;

import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.shell.InvertedShell;
import org.apache.sshd.server.shell.ProcessShellFactory;

/**
 * A Factory of Command that will create a new process and bridge the streams.
 *
 * 
 * 
 * 
 * @author andrea
 *
 */
public class AnimaProcessShellFactory extends ProcessShellFactory {

	public AnimaProcessShellFactory(String[] strings) {
		super(strings);
	}

	@Override
	public Command create() {
		return new AnimaInvertedShellWrapper(createInvertedShell());
	}

	@Override
	protected InvertedShell createInvertedShell() {
		return new AnimaProcessShell(resolveEffectiveCommand(getCommand()));
	}

}
