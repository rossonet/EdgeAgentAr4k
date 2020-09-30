package org.ar4k.agent.rpc.process.bash.cmd;

import java.io.File;

import javax.script.ScriptEngineFactory;

import org.ar4k.agent.rpc.process.bash.NativeShell;

public class Cmd implements NativeShell {

  public ProcessBuilder createProcess(File commandAsFile) {
    return new ProcessBuilder("cmd", "/q", "/c", commandAsFile.getAbsolutePath());
  }

  public ProcessBuilder createProcess(String command) {
    return new ProcessBuilder("cmd", "/c", command);
  }

  @Override
  public String getInstalledVersionCommand() {
    return "echo|set /p=%CmdExtVersion%";
  }

  @Override
  public String getMajorVersionCommand() {
    return getInstalledVersionCommand();
  }

  @Override
  public ScriptEngineFactory getScriptEngineFactory() {
    return new UserSpaceCmdScriptEngineFactory();
  }

  @Override
  public String getFileExtension() {
    return ".bat";
  }

}
