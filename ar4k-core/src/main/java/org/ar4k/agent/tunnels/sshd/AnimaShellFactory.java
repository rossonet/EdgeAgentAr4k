package org.ar4k.agent.tunnels.sshd;

import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.shell.ShellFactory;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.springframework.shell.Shell;

public class AnimaShellFactory implements ShellFactory {

  private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(AnimaShellFactory.class.toString());

  private final Anima anima;
  private final Shell shell;

  public AnimaShellFactory(Anima anima, Shell shell) {
    this.anima = anima;
    this.shell = shell;
    logger.warn("Remote connection from SSH to the agent");
  }

  @Override
  public Command create() {
    SshdAnimaCommand command = new SshdAnimaCommand(anima, shell);
    return command;
  }

}
