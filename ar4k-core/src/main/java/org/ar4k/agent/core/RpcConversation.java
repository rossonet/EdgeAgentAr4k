package org.ar4k.agent.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolation;

import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.data.messages.StringChatRpcMessage;
import org.ar4k.agent.keystore.KeystoreConfig;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.rpc.Homunculus;
import org.ar4k.agent.rpc.RpcExecutor;
import org.ar4k.agent.rpc.RpcMessage;
import org.ar4k.agent.rpc.process.AgentProcess;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.Input;
import org.springframework.shell.MethodTarget;
import org.springframework.shell.ParameterValidationException;
import org.springframework.shell.Shell;

import com.beust.jcommander.ParameterException;

// una singola conversazione via RPC
public class RpcConversation implements RpcExecutor {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(RpcConversation.class.toString());

  private Map<String, Ar4kConfig> configurations = new HashMap<>();
  private Map<String, KeystoreConfig> keyStores = new HashMap<>();
  private Map<String, ServiceConfig> components = new HashMap<>();
  private Map<String, AgentProcess> scriptSessions = new HashMap<>();
  private Homunculus homunculus = null;
  private String workingConfig = null;

  private final Shell shell;

  public RpcConversation(Shell shell) {
    this.shell = shell;
  }

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
    } catch (Exception a) {
      if (a instanceof ParameterValidationException)
        for (ConstraintViolation<Object> s : ((ParameterValidationException) a).getConstraintViolations()) {
          result += s.getMessage() + "\n";
        }
      if (a instanceof ParameterException) {
        result += ((ParameterException) a).getMessage();
      }
      result += "Details of the error have been omitted. You can use the stacktrace command to print the full stacktrace.";
      logger.logException(a);
    }
    return result;
  }

  @Override
  public RpcMessage<? extends String> elaborateMessage(RpcMessage<? extends String> message) {
    StringChatRpcMessage reply = new StringChatRpcMessage();
    reply.setPayload(elaborateMessage(message.getPayload()));
    return reply;
  }

  @Override
  public Map<String, MethodTarget> listCommands() {
    return shell.listCommands();
  }

  @Override
  public List<CompletionProposal> complete(CompletionContext context) {
    return shell.complete(context);
  }

  @Override
  public void setHomunculus(Homunculus homunculus) {
    this.homunculus = homunculus;
  }

  public Map<String, Ar4kConfig> getConfigurations() {
    return configurations;
  }

  public void setConfigurations(Map<String, Ar4kConfig> configurations) {
    this.configurations = configurations;
  }

  public Map<String, KeystoreConfig> getKeyStores() {
    return keyStores;
  }

  public void setKeyStores(Map<String, KeystoreConfig> keyStores) {
    this.keyStores = keyStores;
  }

  public Map<String, ServiceConfig> getComponents() {
    return components;
  }

  public void setComponents(Map<String, ServiceConfig> components) {
    this.components = components;
  }

  protected Homunculus getHomunculus() {
    return homunculus;
  }

  public Ar4kConfig getWorkingConfig() {
    return workingConfig != null ? configurations.get(workingConfig) : null;
  }

  public void setWorkingConfig(String workingConfig) {
    this.workingConfig = workingConfig;
  }

  public Shell getShell() {
    return shell;
  }

  public Map<String, AgentProcess> getScriptSessions() {
    return scriptSessions;
  }

  public void setScriptSessions(Map<String, AgentProcess> scriptSessions) {
    this.scriptSessions = scriptSessions;
  }

  @Override
  public void close() throws Exception {
    logger.debug("rpc coversation closed");
  }

}
