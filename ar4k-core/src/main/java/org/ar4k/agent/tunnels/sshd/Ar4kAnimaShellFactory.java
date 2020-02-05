package org.ar4k.agent.tunnels.sshd;

import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.shell.ShellFactory;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.springframework.shell.Shell;

public class Ar4kAnimaShellFactory implements ShellFactory {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(Ar4kAnimaShellFactory.class.toString());

  private final Anima anima;
  private final Shell shell;

  public Ar4kAnimaShellFactory(Anima anima, Shell shell) {
    this.anima = anima;
    this.shell = shell;
    logger.warn("Remote connection from SSH to the agent");
  }

  @Override
  public Command create() {
    AnimaCommand command = new AnimaCommand(anima, shell);
    return command;
  }

}