package org.ar4k.agent.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolation;

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.data.messages.InternalMessage;
import org.ar4k.agent.core.data.messages.StringMessage;
import org.ar4k.agent.keystore.KeystoreConfig;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.rpc.IHomunculus;
import org.ar4k.agent.rpc.RpcExecutor;
import org.ar4k.agent.rpc.process.AgentProcess;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.Input;
import org.springframework.shell.MethodTarget;
import org.springframework.shell.ParameterValidationException;
import org.springframework.shell.Shell;

import com.beust.jcommander.ParameterException;

/**
 * singola conversazione via protocollo RPC
 *
 * @author andrea
 *
 */
public class RpcConversation implements RpcExecutor {

  private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(RpcConversation.class.toString());

  private Map<String, EdgeConfig> configurations = new HashMap<>();
  private Map<String, KeystoreConfig> keyStores = new HashMap<>();
  private Map<String, ServiceConfig> components = new HashMap<>();
  private Map<String, AgentProcess> scriptSessions = new HashMap<>();
  private IHomunculus homunculus = null;
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
  public InternalMessage<? extends String> elaborateMessage(InternalMessage<? extends String> message) {
    StringMessage reply = new StringMessage();
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
  public void setHomunculus(IHomunculus homunculus) {
    this.homunculus = homunculus;
  }

  public Map<String, EdgeConfig> getConfigurations() {
    return configurations;
  }

  public void setConfigurations(Map<String, EdgeConfig> configurations) {
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

  protected IHomunculus getHomunculus() {
    return homunculus;
  }

  public EdgeConfig getWorkingConfig() {
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
    if (scriptSessions != null && !scriptSessions.isEmpty()) {
      for (AgentProcess p : scriptSessions.values()) {
        p.close();
      }
      scriptSessions.clear();
    }
    if (configurations != null && !configurations.isEmpty()) {
      configurations.clear();
    }
    if (keyStores != null && !keyStores.isEmpty()) {
      keyStores.clear();
    }
    if (components != null && !components.isEmpty()) {
      components.clear();
    }
    if (homunculus != null) {
      homunculus.close();
    }
    logger.debug("rpc coversation closed");
  }

  @Override
  public String toString() {
    return "RpcConversation [configurations=" + configurations.size() + ", keyStores=" + keyStores.size()
        + ", components=" + components.size() + ", scriptSessions=" + scriptSessions.size() + ", homunculus="
        + homunculus.toString() + ", workingConfig=" + workingConfig + "]";
  }

}
