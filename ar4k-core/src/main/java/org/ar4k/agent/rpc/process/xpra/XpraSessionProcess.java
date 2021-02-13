package org.ar4k.agent.rpc.process.xpra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.ar4k.agent.helper.NetworkHelper;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.rpc.process.AgentProcess;
import org.ar4k.agent.rpc.process.EdgeRpcProcess;

@EdgeRpcProcess
//TODO portare in seed
public class XpraSessionProcess implements AgentProcess {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(XpraSessionProcess.class.toString());

	private Process xpraProcess = null;
	private int tcpPort = 0;
	private String command = "xterm";
	private String label;

	public boolean debug = false;
	public boolean webcam = true;
	public boolean fileTransfer = true;

	private void start(String command) {
		if (command != null && !command.isEmpty()) {
			this.command = command;
		}
		if (tcpPort == 0) {
			tcpPort = NetworkHelper.findAvailablePort(14500);
		}
		String[] commandArray = { "xpra", "start", "--bind-tcp=0.0.0.0:" + String.valueOf(tcpPort),
				debug ? "--daemon=no" : "--daemon=yes", "--system-tray=no", webcam ? "--webcam=yes" : "--webcam=no",
				fileTransfer ? "--file-transfer=on" : "--file-transfer=off", "--terminate-children=yes", "--html=on",
				"--start=" + this.command };
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < commandArray.length; i++) {
			sb.append(commandArray[i]);
			sb.append(" ");
		}
		logger.info("run command " + sb.toString());
		ProcessBuilder builder = new ProcessBuilder(commandArray);
		try {
			xpraProcess = builder.start();
			logger.warn("new Xpra session created on port " + tcpPort);
		} catch (IOException e) {
			logger.logException(e);
		}
	}

	private void stop() {
		if (xpraProcess != null) {
			xpraProcess.destroy();
		}
		final String[] killCommand = { "pkill", "--signal", "9", "-f",
				"--bind-tcp=0.0.0.0:" + String.valueOf(tcpPort) };
		try {
			logger.info("KILL -9 TO " + "--bind-tcp=0.0.0.0:" + String.valueOf(tcpPort));
			StringBuilder output = new StringBuilder();
			AgentProcess.runCommand(killCommand, null, output);
			logger.info("killed xpra service with command:\n" + Arrays.toString(killCommand) + "\n" + output);
		} catch (final IOException e) {
			logger.error("ERROR killing xpra service instance " + "--bind-tcp=0.0.0.0:" + String.valueOf(tcpPort)
					+ " with command  " + Arrays.toString(killCommand) + " -> [" + e.getClass() + "] " + e.toString());
		}
	}

	public int getTcpPort() {
		return tcpPort;
	}

	public void setTcpPort(int tcpPort) {
		this.tcpPort = tcpPort;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	@Override
	public boolean isAlive() {
		return xpraProcess != null && xpraProcess.isAlive();
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String getOutput() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(xpraProcess.getInputStream()));
		String reply = null;
		try {
			if (reader.ready())
				reply = reader.lines().collect(Collectors.joining());
		} catch (IOException e) {
			logger.logException(e);
		}
		return reply;
	}

	@Override
	public String getErrors() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(xpraProcess.getErrorStream()));
		String reply = null;
		try {
			if (reader.ready())
				reply = reader.lines().collect(Collectors.joining());
		} catch (IOException e) {
			logger.logException(e);
		}
		return reply;
	}

	@Override
	public String toString() {
		return isAlive() ? "Xpra server running on port " + String.valueOf(tcpPort) : "Xpra server is dead";
	}

	@Override
	public void close() throws IOException {
		stop();
	}

	@Override
	public void eval(String script) {
		start(script);
	}

}
