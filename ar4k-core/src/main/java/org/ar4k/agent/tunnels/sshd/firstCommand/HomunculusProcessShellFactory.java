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
public class HomunculusProcessShellFactory extends ProcessShellFactory {

	public HomunculusProcessShellFactory(String[] strings) {
		super(strings);
	}

	@Override
	public Command create() {
		return new HomunculusInvertedShellWrapper(createInvertedShell());
	}

	@Override
	protected InvertedShell createInvertedShell() {
		return new HomunculusProcessShell(resolveEffectiveCommand(getCommand()));
	}

}
