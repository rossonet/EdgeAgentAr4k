package org.ar4k.agent.console.chat.sshd;

import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.shell.ShellFactory;
import org.ar4k.agent.core.Anima;
import org.springframework.shell.Shell;

public class Ar4kAnimaShellFactory implements ShellFactory {

  private Anima anima = null;
  private Shell shell = null;

  public Ar4kAnimaShellFactory(Anima anima, Shell shell) {
    this.anima = anima;
    this.shell = shell;
  }

  @Override
  public Command create() {
    AnimaCommand command = new AnimaCommand(anima, shell);
    return command;
  }

}
