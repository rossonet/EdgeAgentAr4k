package org.ar4k.agent.console.chat.sshd.firstCommand;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;

//import org.apache.sshd.common.util.io.IoUtils;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.shell.InvertedShell;
import org.apache.sshd.server.shell.InvertedShellWrapper;

public class Ar4kInvertedShellWrapper extends InvertedShellWrapper {

  private InvertedShell shellMark = null;
 // private int bufferSizeMark = IoUtils.DEFAULT_COPY_SIZE;

  public Ar4kInvertedShellWrapper(InvertedShell shell, Executor executor, boolean shutdownExecutor, int bufferSize) {
    super(shell, executor, shutdownExecutor, bufferSize);
    this.shellMark = shell;
   // this.bufferSizeMark = bufferSize;
  }

  public Ar4kInvertedShellWrapper(InvertedShell createInvertedShell) {
    super(createInvertedShell);
    this.shellMark = createInvertedShell;
  }

  @Override
  public synchronized void start(Environment env) throws IOException {
    super.start(env);
    OutputStream is = shellMark.getInputStream();
    String command = "uname -a\n";
    is.flush();
    is.write(command.getBytes(StandardCharsets.UTF_8));
    is.flush();
    is = null;
  }
}
