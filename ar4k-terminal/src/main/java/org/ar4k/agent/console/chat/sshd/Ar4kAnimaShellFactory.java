package org.ar4k.agent.console.chat.sshd;

import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.shell.ShellFactory;
import org.ar4k.agent.core.Anima;

public class Ar4kAnimaShellFactory implements ShellFactory {

  private Anima anima = null;

  public Ar4kAnimaShellFactory(Anima anima) {
    this.anima = anima;
  }

  @Override
  public Command create() {
    AnimaCommand command = new AnimaCommand(anima);
    return command;
  }

}
