package org.ar4k.agent.console.chat.sshd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.command.Command;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.rpc.RpcExecutor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.Shell;

public class AnimaCommand implements Command {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(AbstractShellHelper.class.toString());

  private static final CharSequence COMPLETION_CHAR = "?";
  private static final CharSequence EXIT_MESSAGE = "exit";

  private Shell shell = null;
  private Anima anima = null;
  private RpcExecutor executor = null;
  private final String sessionId = UUID.randomUUID().toString();
  private ExitCallback exitCallback = null;
  private OutputStream errorStream = null;
  private OutputStream outputStream = null;
  private InputStream inputStream = null;

  private boolean running = true;
  Thread runner = null;

  public AnimaCommand(Anima homunculus, Shell shell) {
    this.anima = homunculus;
    this.shell = shell;
  }

  private void sendMessage(String txt) throws IOException {
    outputStream.flush();
    outputStream.write(txt.getBytes(StandardCharsets.UTF_8));
    outputStream.flush();
  }

  @Override
  public void start(Environment env) throws IOException {
    logger.info("Run SSHD executer");
    running();
  }

  private void running() throws IOException {
    runner = new Thread(new LoopRunner(), "sshd_" + sessionId);
    runner.start();
  }

  private String completeMessage(String message) {
    List<String> m = Arrays.asList(StringUtils.split(message));
    List<String> clean = new ArrayList<>(m.size());
    int pos = 0;
    int word = 0;
    int counter = 0;
    for (String p : m) {
      if (p.contains(COMPLETION_CHAR)) {
        word = counter;
        pos = p.indexOf(COMPLETION_CHAR.toString());
        if (!p.equals(COMPLETION_CHAR.toString())) {
          // System.out.println("add " + p.replace(COMPLETION_CHAR, ""));
          clean.add(p.replace(COMPLETION_CHAR, ""));
        } else {
          // System.out.println("add " + p);
          clean.add(p);
        }
      } else {
        clean.add(p);
      }
      counter++;
    }
    CompletionContext context = new CompletionContext(clean, word, pos);
    List<CompletionProposal> listCompletionProposal = executor.complete(context);
    StringBuffer response = new StringBuffer();
    for (CompletionProposal prop : listCompletionProposal) {
      response.append(prop.toString() + (prop.description() != null ? " => " + prop.description() : "") + "\n");
    }
    return response.toString();
  }

  @Override
  public void destroy() throws Exception {
    running = false;
    inputStream.close();
    outputStream.flush();
    outputStream.close();
    errorStream.flush();
    errorStream.close();
    executor = null;
  }

  @Override
  public void setInputStream(InputStream in) {
    this.inputStream = in;
  }

  @Override
  public void setOutputStream(OutputStream out) {
    this.outputStream = out;
  }

  @Override
  public void setErrorStream(OutputStream err) {
    this.errorStream = err;
  }

  @Override
  public void setExitCallback(ExitCallback callback) {
    this.exitCallback = callback;
  }

  private void readLineString(InputStream inputStream, StringBuilder messageBuilder, boolean echo) throws IOException {
    int c = 0;
    do {
      if (inputStream.available() > 0) {
        c = inputStream.read();
        // logger.warn("*** remote char -> " + (char) c);
        if (echo) {
          sendMessage(String.valueOf((char) c));
        } else {
          sendMessage("x");
        }
        if (((int) c) == 13)
          break;
        messageBuilder.append((char) c);
      }
    } while (c != -1);
  }

  private class LoopRunner implements Runnable {
    private String username = null;
    private String password = null;
    private boolean logged = false;

    @Override
    public void run() {
      logger.info("*** start remote sshd session");
      while (running) {
        try {
          // logger.debug("*** while");
          if (!logged) {
            if (username == null || username.isEmpty()) {
              StringBuilder usernameBuilder = new StringBuilder();
              sendMessage("USERNAME: ");
              readLineString(inputStream, usernameBuilder, true);
              username = usernameBuilder.toString();
              sendMessage(
                  "\r                                                                                                                   \r");
            }
            if (password == null || password.isEmpty()) {
              StringBuilder passwordBuilder = new StringBuilder();
              sendMessage("PASSWORD: ");
              readLineString(inputStream, passwordBuilder, false);
              password = passwordBuilder.toString();
              sendMessage(
                  "\r                                                                                                                   \r");
            }
            if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
              try {
                String session = anima.loginAgent(username, password, sessionId);
                if (anima.isSessionValid(session) && session.equals(sessionId)) {
                  executor = anima.getRpc(sessionId);
                  ((RpcConversation) executor).setShell(shell);
                  logged = true;
                  printPrompt();
                } else {
                  username = null;
                  password = null;
                  sendMessage("FAILED!\n\r");
                  logger.info("failed login for " + username);
                }
              } catch (NullPointerException | AuthenticationException e) {
                username = null;
                password = null;
                sendMessage("FAILED!\n\r");
                logger.info("failed login for " + username);
              }
            }
          } else {
            StringBuilder messageBuilder = new StringBuilder();
            readLineString(inputStream, messageBuilder, true);
            String message = messageBuilder.toString();
            if (message != null && !message.isEmpty()) {
              if (!message.contains(COMPLETION_CHAR)) {
                String elaborateMessage = executor.elaborateMessage(message);
                // logger.debug("*** remote command " + message + " -> " + elaborateMessage);
                sendMessage("\n\r" + elaborateMessage.replaceAll("\n", "\n\r"));
                printPrompt();
              } else if (message.equals(EXIT_MESSAGE + "\n")) {
                running = false;
              } else {
                String response = completeMessage(message);
                sendMessage("\n\r" + response.replaceAll("\n", "\n\r"));
                printPrompt();
              }
            } else {
              sendMessage("\n\r");
              printPrompt();
            }
          }
        } catch (IOException e) {
          logger.logException(e);
        }
      }
      try

      {
        outputStream.flush();
      } catch (IOException e) {
        logger.logException(e);
      }
      logger.warn("*** exit from remote sshd session");
      running = false;
      exitCallback.onExit(0, "bye...");
    }

    private void printPrompt() {
      try {
        sendMessage(username + "@" + anima.getAgentUniqueName() + " # ");
      } catch (IOException e) {
        logger.logException(e);
      }

    }

  }

}