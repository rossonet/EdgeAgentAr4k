package org.ar4k.agent.helper;

import java.util.List;
import java.util.Map;

import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Anima.AnimaStates;
import org.ar4k.agent.core.RpcConversation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.shell.Availability;

public abstract class AbstractShellHelper {

  @Value("${ar4k.test}")
  protected boolean flagTestOk = false;

  @Autowired
  protected ApplicationContext applicationContext;

  @Autowired
  protected Anima anima;

  @Autowired
  protected SessionRegistry sessionRegistry;

  protected String getSessionId() {
    List<SessionInformation> ss = sessionRegistry.getAllSessions(SecurityContextHolder.getContext().getAuthentication(), false);
    return ss.isEmpty() ? null : ss.get(0).getSessionId();
  }

  protected Availability testOk() {
    return flagTestOk ? Availability.available()
        : Availability.unavailable("test command not available in this configuration");
  }

  protected Availability sessionOk() {
    return getSessionId() != null ? Availability.available()
        : Availability.unavailable("you must login in the system before");
  }

  protected Availability sessionOkOrStatusInit() {
    return (getSessionId() != null || anima.getState().equals(AnimaStates.INIT)) ? Availability.available()
        : Availability.unavailable("you must login in the system or to be in INIT status");
  }

  protected Availability sessionFalse() {
    return getSessionId() == null ? Availability.available()
        : Availability.unavailable("you have a valide session. Logout from the system before");
  }

  protected Availability testSelectedConfigOk() {
    Availability result = Availability.unavailable("you have to login in the agent before");
    if (getSessionId() != null) {
      result = getWorkingConfig() != null ? Availability.available()
          : Availability.unavailable("you have to select a config before");
    }
    return result;
  }

  protected Ar4kConfig getWorkingConfig() {
    if (getSessionId() != null) {
      return ((RpcConversation) anima.getRpc(getSessionId())).getWorkingConfig();
    } else
      return null;
  }

  protected void setWorkingConfig(Ar4kConfig config) {
    Map<String, Ar4kConfig> actualConfig = ((RpcConversation) anima.getRpc(getSessionId())).getConfigurations();
    if (config != null && !actualConfig.containsValue(config))
      addConfig(config);
    if (config != null)
      ((RpcConversation) anima.getRpc(getSessionId())).setWorkingConfig(config.getName());
    else
      ((RpcConversation) anima.getRpc(getSessionId())).setWorkingConfig(null);
  }

  protected Availability testRuntimeConfigOk() {
    return anima.getRuntimeConfig() != null ? Availability.available()
        : Availability.unavailable("you have to configure a runtime config before");
  }

  protected Availability testListConfigOk() {
    Availability result = Availability.unavailable("you have to login in the agent before");
    if (getSessionId() != null) {
      result = getConfigs().size() > 0 ? Availability.available()
          : Availability.unavailable("there are no configs in memory");
    }
    return result;
  }

  protected Map<String, Ar4kConfig> getConfigs() {
    return ((RpcConversation) anima.getRpc(getSessionId())).getConfigurations();
  }

  protected void addConfig(Ar4kConfig config) {
    ((RpcConversation) anima.getRpc(getSessionId())).getConfigurations().put(config.getName(), config);
  }

  protected Availability testIsRunningOk() {
    return anima.isRunning() ? Availability.available() : Availability.unavailable("there are no configs in memory");
  }
}
