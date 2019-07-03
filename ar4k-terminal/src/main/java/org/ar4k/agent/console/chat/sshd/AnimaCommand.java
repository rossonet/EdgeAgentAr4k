package org.ar4k.agent.console.chat.sshd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.ar4k.agent.rpc.RpcExecutor;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;

public class AnimaCommand implements Command {

  private static final CharSequence COMPLETION_CHAR = "?";

  private Anima anima = null;
  private RpcExecutor executor = null;
  private final String sessionId = UUID.randomUUID().toString();
  private ExitCallback exitCallback = null;
  private OutputStream errorStream = null;
  private OutputStream outputStream = null;
  private InputStream inputStream = null;

  private boolean running = true;

  public AnimaCommand(Anima anima) {
    this.anima = anima;
  }

  private void sendMessage(String txt) throws IOException {
    outputStream.flush();
    outputStream.write(txt.getBytes(StandardCharsets.UTF_8));
    outputStream.flush();
  }

  @Override
  public void start(Environment env) throws IOException {
    executor = anima.getRpc(sessionId);
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    while (running && reader.ready()) {
      String message = reader.readLine();
      if (!message.contains(COMPLETION_CHAR)) {
        sendMessage(executor.elaborateMessage(message));
      } else {
        String response = completeMessage(message);
        sendMessage(response);
      }
    }
    running = false;
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

}
