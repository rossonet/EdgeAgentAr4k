package org.ar4k.agent.console.rpc;

import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolation;

import org.ar4k.agent.rpc.Ar4kSession;
import org.ar4k.agent.rpc.RpcExecutor;
import org.ar4k.agent.rpc.RpcMessage;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.Input;
import org.springframework.shell.MethodTarget;
import org.springframework.shell.ParameterValidationException;
import org.springframework.shell.Shell;

import com.beust.jcommander.ParameterException;

public class BaseShellChatRpcExecutor implements RpcExecutor {

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
  public RpcMessage<? extends String> elaborateMessage(RpcMessage<? extends String> message) {
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

  @Override
  public Map<String, MethodTarget> listCommands() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<CompletionProposal> complete(CompletionContext context) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Ar4kSession getSession() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setSession(Ar4kSession session) {
    // TODO Auto-generated method stub
    
  }

}
