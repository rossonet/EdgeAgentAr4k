package org.ar4k.agent.activemq;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.activemq.artemis.core.security.CheckType;
import org.apache.activemq.artemis.core.security.Role;
import org.apache.activemq.artemis.spi.core.protocol.RemotingConnection;
import org.apache.activemq.artemis.spi.core.security.ActiveMQSecurityManager3;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;

public class ActiveMqSecurityManager implements ActiveMQSecurityManager3, AutoCloseable {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(ActiveMqSecurityManager.class.toString());

  private final Map<String, String> cachedPasswords = new HashMap<>();
  private final Map<String, Set<String>> cachedRoles = new HashMap<>();
  private final Map<String, Long> cachedTimeoutPasswords = new HashMap<>();
  private final Map<String, Long> cachedTimeoutRoles = new HashMap<>();
  private final int timeOutPassword = 300;
  private final int timeOutRole = 300;

  public ActiveMqSecurityManager() {
    super();
  }

  @Override
  public boolean validateUser(String user, String password) {
    if (!(timeOutPassword == 0)) {
      checkTtlPasswordMap(user);
    }
    boolean checkPasswordOk = false;
    if (!(timeOutPassword == 0) && cachedPasswords.containsKey(user)) {
      checkPasswordOk = cachedPasswords.get(user).equals(password);
    } else {
      checkPasswordOk = verifyUserPassword(user, password);
    }
    logger.info("password validation " + user + " password " + password + " -> " + checkPasswordOk);
    return checkPasswordOk;
  }

  @Override
  public String validateUser(String user, String password, RemotingConnection remotingConnection) {
    return validateUser(user, password) ? user : null;
  }

  @Override
  public boolean validateUserAndRole(String user, String password, Set<Role> roles, CheckType checkType) {
    return validateUser(user, password);
  }

  @SuppressWarnings("unused")
  private boolean verifyUserPassword(String user, String password) {
    if (true) { // implementare controllo
      if (!(timeOutPassword == 0)) {
        cachedTimeoutPasswords.put(user, Instant.now().getEpochSecond());
        cachedPasswords.put(user, password);
      }
      return true;
    } else {
      return false;
    }
  }

  private void checkTtlPasswordMap(String user) {
    if (cachedTimeoutPasswords.containsKey(user)
        && (cachedTimeoutPasswords.get(user) + timeOutPassword) < Instant.now().getEpochSecond()) {
      logger.info("cached password for " + user + " removed");
      cachedPasswords.remove(user);
    }
  }

  @SuppressWarnings("unused")
  private String verifyUserRole(String user, String password, String address) {
    if (true) { // implementare check role
      if (!(timeOutRole == 0)) {
        cachedTimeoutRoles.put(user, Instant.now().getEpochSecond());
        cachedRoles.get(user).add(address);
      }
      return user;
    } else {
      return null;
    }
  }

  private void checkTtlRolesMap(String user) {
    if (cachedTimeoutRoles.containsKey(user)
        && (cachedTimeoutRoles.get(user) + timeOutRole) < Instant.now().getEpochSecond()) {
      cachedTimeoutRoles.remove(user);
      logger.info("cached role for " + user + " removed");
    }
  }

  private void popolateListRolesMap(String user) {
    if (!cachedRoles.containsKey(user)) {
      cachedRoles.put(user, new HashSet<>());
    }
  }

  @Override
  public String validateUserAndRole(String user, String password, Set<Role> roles, CheckType checkType, String address,
      RemotingConnection remotingConnection) {
    if (!(timeOutRole == 0)) {
      checkTtlRolesMap(user);
      popolateListRolesMap(user);
    }
    String checkRoleOk = user;
    if (timeOutRole == 0 || !cachedRoles.get(user).contains(address)) {
      checkRoleOk = verifyUserRole(user, password, address);
    }
    logger.info("role validation " + user + " roles size " + roles.size() + " check type " + checkType.name() + " -> "
        + checkRoleOk);
    return checkRoleOk;
  }

  @Override
  public void close() throws Exception {
    cachedPasswords.clear();
    cachedRoles.clear();
    cachedTimeoutPasswords.clear();
    cachedTimeoutRoles.clear();
  }

}
