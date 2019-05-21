package org.ar4k.agent.rpc.xpra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.ar4k.agent.helper.NetworkHelper;
import org.ar4k.agent.rpc.process.AgentProcess;

public class XpraSessionProcess implements AgentProcess {

  private Process xpraProcess = null;
  private int tcpPort = 0;
  private String command = "xterm";
  private String label;

  public void start() {
    if (tcpPort == 0) {
      tcpPort = NetworkHelper.findAvailablePort(14500);
    }
    ProcessBuilder builder = new ProcessBuilder("xpra", "start", "--bind-tcp=0.0.0.0:" + String.valueOf(tcpPort),
        "--daemon=no", "--html=on", "--start=" + command);
    try {
      xpraProcess = builder.start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void stop() {
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
      e.printStackTrace();
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
      e.printStackTrace();
    }
    return reply;
  }

  public String toString() {
    return isAlive() ? "Xpra server running on port " + String.valueOf(tcpPort) : "Xpra server is dead";
  }

  @Override
  public void close() throws IOException {
    stop();
  }

}
