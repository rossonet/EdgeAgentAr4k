package org.ar4k.agent.helper;

import java.util.Map;

import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Anima.AnimaStates;
import org.ar4k.agent.core.RpcConversation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.Availability;

public abstract class AbstractShellHelper {

  @Value("${ar4k.test}")
  protected boolean flagTestOk = false;

  @Autowired
  protected ApplicationContext applicationContext;

  @Autowired
  protected Anima anima;

  protected String sessionId = null;

  protected Availability testOk() {
    return flagTestOk ? Availability.available()
        : Availability.unavailable("test command not available in this configuration");
  }

  protected Availability sessionOk() {
    return sessionId != null ? Availability.available() : Availability.unavailable("you must login in the system");
  }

  protected Availability sessionOkOrStatusInit() {
    return (sessionId != null || anima.getState().equals(AnimaStates.INIT)) ? Availability.available()
        : Availability.unavailable("you must login in the system or to be in INIT status");
  }

  protected Availability sessionFalse() {
    return sessionId == null ? Availability.available()
        : Availability.unavailable("you have a valide session. Logout from the system before");
  }

  protected Availability testSelectedConfigOk() {
    Availability result = Availability.unavailable("you have to login in the agent before");
    if (sessionOk().equals(Availability.available())) {
      result = getWorkingConfig() != null ? Availability.available()
          : Availability.unavailable("you have to select a config before");
    }
    return result;
  }

  protected Ar4kConfig getWorkingConfig() {
    if (sessionId != null) {
      return ((RpcConversation) anima.getRpc(sessionId)).getWorkingConfig();
    } else
      return null;
  }

  protected void setWorkingConfig(Ar4kConfig config) {
    Map<String, Ar4kConfig> actualConfig = ((RpcConversation) anima.getRpc(sessionId)).getConfigurations();
    if (config != null && !actualConfig.containsValue(config))
      addConfig(config);
    if (config != null)
      ((RpcConversation) anima.getRpc(sessionId)).setWorkingConfig(config.getName());
    else
      ((RpcConversation) anima.getRpc(sessionId)).setWorkingConfig(null);
  }

  protected Availability testRuntimeConfigOk() {
    return anima.getRuntimeConfig() != null ? Availability.available()
        : Availability.unavailable("you have to configure a runtime config before");
  }

  protected Availability testListConfigOk() {
    Availability result = Availability.unavailable("you have to login in the agent before");
    if (sessionOk().equals(Availability.available())) {
      result = getConfigs().size() > 0 ? Availability.available()
          : Availability.unavailable("there are no configs in memory");
    }
    return result;
  }

  protected Map<String, Ar4kConfig> getConfigs() {
    return ((RpcConversation) anima.getRpc(sessionId)).getConfigurations();
  }

  protected void addConfig(Ar4kConfig config) {
    ((RpcConversation) anima.getRpc(sessionId)).getConfigurations().put(config.getName(), config);
  }

  protected Availability testIsRunningOk() {
    return anima.isRunning() ? Availability.available() : Availability.unavailable("there are no configs in memory");
  }
}
