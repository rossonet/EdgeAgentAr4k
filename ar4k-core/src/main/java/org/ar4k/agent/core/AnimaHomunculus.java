package org.ar4k.agent.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ar4k.agent.rpc.Homunculus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class AnimaHomunculus implements Homunculus, SessionRegistry, ApplicationListener<SessionDestroyedEvent> {

  @Autowired
  private Anima anima;

  protected final Log logger = LogFactory.getLog(AnimaHomunculus.class);
  private final ConcurrentMap<Object, Set<String>> principals;
  private final Map<String, SessionInformation> sessionIds;
  private final Map<String, RpcConversation> rpcIds;

  public AnimaHomunculus() {
    this.principals = new ConcurrentHashMap<>();
    this.sessionIds = new ConcurrentHashMap<>();
    this.rpcIds = new ConcurrentHashMap<>();
  }

  public AnimaHomunculus(ConcurrentMap<Object, Set<String>> principals, Map<String, SessionInformation> sessionIds) {
    this.principals = principals;
    this.sessionIds = sessionIds;
    this.rpcIds = new ConcurrentHashMap<>();
  }

  @Override
  public List<Object> getAllPrincipals() {
    return new ArrayList<>(principals.keySet());
  }

  @Override
  public List<SessionInformation> getAllSessions(Object principal, boolean includeExpiredSessions) {
    List<SessionInformation> list = null;
    if (principal != null && principals.containsKey(principal)) {
      final Set<String> sessionsUsedByPrincipal = principals.get(principal);
      if (sessionsUsedByPrincipal == null) {
        return Collections.emptyList();
      }
      list = new ArrayList<>(sessionsUsedByPrincipal.size());
      for (String sessionId : sessionsUsedByPrincipal) {
        SessionInformation sessionInformation = getSessionInformation(sessionId);
        if (sessionInformation == null) {
          continue;
        }
        if (includeExpiredSessions || !sessionInformation.isExpired()) {
          list.add(sessionInformation);
        }
      }
    } else {
      list = new ArrayList<>();
    }
    return list;
  }

  @Override
  public SessionInformation getSessionInformation(String sessionId) {
    Assert.hasText(sessionId, "SessionId required as per interface contract");
    return sessionIds.get(sessionId);
  }

  public RpcConversation getRpc(String sessionId) {
    Assert.hasText(sessionId, "SessionId required as per interface contract");
    return rpcIds.get(sessionId);
  }

  @Override
  public void onApplicationEvent(SessionDestroyedEvent event) {
    String sessionId = event.getId();
    removeSessionInformation(sessionId);
  }

  @Override
  public void refreshLastRequest(String sessionId) {
    Assert.hasText(sessionId, "SessionId required as per interface contract");
    SessionInformation info = getSessionInformation(sessionId);
    if (info != null) {
      info.refreshLastRequest();
    }
  }

  @Override
  public void registerNewSession(String sessionId, Object principal) {
    Assert.hasText(sessionId, "SessionId required as per interface contract");
    Assert.notNull(principal, "Principal required as per interface contract");
    if (logger.isDebugEnabled()) {
      logger.debug("Registering session " + sessionId + ", for principal " + principal);
    }
    if (getSessionInformation(sessionId) != null) {
      removeSessionInformation(sessionId);
    }
    sessionIds.put(sessionId, new SessionInformation(principal, sessionId, new Date()));
    Set<String> sessionsUsedByPrincipal = principals.computeIfAbsent(principal, key -> new CopyOnWriteArraySet<>());
    sessionsUsedByPrincipal.add(sessionId);
    RpcConversation rpc = new RpcConversation();
    rpc.setHomunculus(this);
    rpcIds.put(sessionId, rpc);
    if (logger.isTraceEnabled()) {
      logger.trace("Sessions used by '" + principal + "' : " + sessionsUsedByPrincipal);
    }
  }

  @Override
  public void removeSessionInformation(String sessionId) {
    Assert.hasText(sessionId, "SessionId required as per interface contract");
    SessionInformation info = getSessionInformation(sessionId);
    if (info == null) {
      return;
    }
    if (logger.isTraceEnabled()) {
      logger.debug("Removing session " + sessionId + " from set of registered sessions");
    }
    sessionIds.remove(sessionId);
    Set<String> sessionsUsedByPrincipal = principals.get(info.getPrincipal());
    if (sessionsUsedByPrincipal == null) {
      return;
    }
    if (logger.isDebugEnabled()) {
      logger.debug("Removing session " + sessionId + " from principal's set of registered sessions");
    }
    sessionsUsedByPrincipal.remove(sessionId);
    if (sessionsUsedByPrincipal.isEmpty()) {
      if (logger.isDebugEnabled()) {
        logger.debug("Removing principal " + info.getPrincipal() + " from registry");
      }
      principals.remove(info.getPrincipal());
    }
    if (logger.isTraceEnabled()) {
      logger.trace("Sessions used by '" + info.getPrincipal() + "' : " + sessionsUsedByPrincipal);
    }
  }

  public Anima getAnima() {
    return anima;
  }

  @Override
  public void close() throws Exception {
    // TODO Auto-generated method stub
  }

}