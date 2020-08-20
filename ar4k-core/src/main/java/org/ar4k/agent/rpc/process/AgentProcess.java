package org.ar4k.agent.rpc.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

public interface AgentProcess extends AutoCloseable {

	static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(AgentProcess.class.toString());

	boolean isAlive();

	String getLabel();

	void setLabel(String label);

	String getOutput();

	String getErrors();

	void eval(String script);

	static Process runCommand(String[] startCommand, String directory, StringBuilder resultCommand) throws IOException {
		Process process = directory != null ? Runtime.getRuntime().exec(startCommand, null, new File(directory))
				: Runtime.getRuntime().exec(startCommand);
		new Thread(new Runnable() {
			@Override
			public void run() {
				final BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
				final BufferedReader inputError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				String line = null;
				String lineError = null;
				try {
					while ((line = input.readLine()) != null)
						resultCommand.append(line + "\n");
					while ((lineError = inputError.readLine()) != null)
						resultCommand.append(lineError + "\n");
				} catch (final IOException e) {
					logger.warn("exec: " + Arrays.toString(startCommand) + "\n" + EdgeLogger.stackTraceToString(e, 4));
				}
			}
		}).start();
		return process;
	}

}
