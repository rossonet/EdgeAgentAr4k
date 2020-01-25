package org.ar4k.agent.rpc.process.bash;

import java.io.File;

import javax.script.ScriptEngineFactory;

public class Bash implements NativeShell {

  public ProcessBuilder createProcess(File commandAsFile) {
    return new ProcessBuilder("bash", commandAsFile.getAbsolutePath());
  }

  public ProcessBuilder createProcess(String command) {
    return new ProcessBuilder("bash", "-c", command);
  }

  @Override
  public String getInstalledVersionCommand() {
    return "echo -n $BASH_VERSION";
  }

  @Override
  public String getMajorVersionCommand() {
    return "echo -n $BASH_VERSINFO";
  }

  @Override
  public ScriptEngineFactory getScriptEngineFactory() {
    return new UserSpaceBashScriptEngineFactory();
  }

  @Override
  public String getFileExtension() {
    return ".sh";
  }
}
