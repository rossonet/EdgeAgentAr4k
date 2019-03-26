package org.ar4k.agent.console.chat;

import javax.validation.ConstraintViolation;

import org.springframework.shell.Input;
import org.springframework.shell.ParameterValidationException;
import org.springframework.shell.Shell;

import com.beust.jcommander.ParameterException;

public class BaseShellChatRpcExecutor implements ChatRpcExecutor {

  private Shell shell = null;

  @Override
  public String elaborateMessage(String message) {
    Input i = new Input() {
      @Override
      public String rawText() {
        return message;
      }
    };
    String result = "";
    try {
      Object o = shell.evaluate(i);
      if (o != null)
        result = o.toString();
      else
        result = "ok";
    } catch (ParameterValidationException | ParameterException a) {
      if (a instanceof ParameterValidationException)
        for (ConstraintViolation<Object> s : ((ParameterValidationException) a).getConstraintViolations()) {
          result += s.getMessage() + "\n";
        }
      if (a instanceof ParameterException) {
        result += ((ParameterException) a).getMessage();
      }
      result += "Details of the error have been omitted. You can use the stacktrace command to print the full stacktrace.";
    }
    return result;
  }

  @Override
  public ChatRpcMessage<? extends String> elaborateMessage(ChatRpcMessage<? extends String> message) {
    StringChatRpcMessage<String> reply = new StringChatRpcMessage<>();
    reply.setPayload(elaborateMessage(message.getPayload()));
    return reply;
  }

  public Shell getShell() {
    return shell;
  }

  public void setShell(Shell shell) {
    this.shell = shell;
  }

}
