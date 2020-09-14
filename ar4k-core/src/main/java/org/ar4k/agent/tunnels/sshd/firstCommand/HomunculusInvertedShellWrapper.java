package org.ar4k.agent.tunnels.sshd.firstCommand;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;

//import org.apache.sshd.common.util.io.IoUtils;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.shell.InvertedShell;
import org.apache.sshd.server.shell.InvertedShellWrapper;

/**
 *
 * A shell implementation that wraps an instance of InvertedShell as a Command.
 * This is useful when using external processes. When starting the shell, this
 * wrapper will also create a thread used to pump the streams and also to check
 * if the shell is alive.
 *
 * @author andrea
 *
 */
public class HomunculusInvertedShellWrapper extends InvertedShellWrapper {

	private InvertedShell shellMark = null;

	public HomunculusInvertedShellWrapper(InvertedShell shell, Executor executor, boolean shutdownExecutor, int bufferSize) {
		super(shell, executor, shutdownExecutor, bufferSize);
		this.shellMark = shell;
	}

	public HomunculusInvertedShellWrapper(InvertedShell createInvertedShell) {
		super(createInvertedShell);
		this.shellMark = createInvertedShell;
	}

	@Override
	public synchronized void start(Environment env) throws IOException {
		super.start(env);
		OutputStream is = shellMark.getInputStream();
		final String command = " \n";
		is.flush();
		is.write(command.getBytes(StandardCharsets.UTF_8));
		is.flush();
		is = null;
	}
}
