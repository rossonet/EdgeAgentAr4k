package org.ar4k.agent.console.chat.sshd.firstCommand;

import java.io.IOException;
import java.util.List;

import org.apache.sshd.common.channel.PtyMode;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.shell.ProcessShell;

public class Ar4kProcessShell extends ProcessShell {

  public Ar4kProcessShell(List<String> resolveEffectiveCommand) {
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
