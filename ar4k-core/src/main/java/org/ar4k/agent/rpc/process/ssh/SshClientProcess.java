package org.ar4k.agent.rpc.process.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.ar4k.agent.helper.NetworkHelper;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.rpc.process.AgentProcess;
import org.ar4k.agent.rpc.process.Ar4kRpcProcess;

@Ar4kRpcProcess
public class SshClientProcess implements AgentProcess {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(SshClientProcess.class.toString());

  private Process xpraProcess = null;
  private int tcpPort = 0;
  private String command = "xterm";
  private String label;

  private void start(String command) {
    if (command != null && !command.isEmpty()) {
      this.command = command;
    }
    if (tcpPort == 0) {
      tcpPort = NetworkHelper.findAvailablePort(14500);
    }
    ProcessBuilder builder = new ProcessBuilder("xpra", "start", "--bind-tcp=0.0.0.0:" + String.valueOf(tcpPort),
        "--daemon=no", "--html=on", "--start=" + this.command);
    try {
      xpraProcess = builder.start();
    } catch (IOException e) {
      logger.logException(e);
    }
  }

  private void stop() {
    if (xpraProcess != null) {
      xpraProcess.destroy();
      if (xpraProcess.isAlive()) {
        xpraProcess.destroyForcibly();
      }
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
