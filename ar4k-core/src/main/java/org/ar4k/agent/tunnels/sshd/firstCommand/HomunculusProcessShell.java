package org.ar4k.agent.tunnels.sshd.firstCommand;

import java.io.IOException;
import java.util.List;

import org.apache.sshd.common.channel.PtyMode;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.shell.ProcessShell;

/**
 * Bridges the I/O streams between the SSH command and the process that executes
 * it
 * 
 * @author andrea
 *
 */

public class HomunculusProcessShell extends ProcessShell {

	public HomunculusProcessShell(List<String> resolveEffectiveCommand) {
		super(resolveEffectiveCommand);
	}

	@Override
	public void start(Environment env) throws IOException {
		super.start(workEnv(env));
	}

	private Environment workEnv(Environment env) {
		env.getPtyModes().put(PtyMode.ECHO, 0);
		return env;
	}

}
