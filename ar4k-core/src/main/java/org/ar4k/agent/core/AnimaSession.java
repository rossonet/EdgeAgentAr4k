package org.ar4k.agent.core;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.core.session.SessionInformation;
//import org.springframework.session.Session;

public class AnimaSession extends SessionInformation {

  public AnimaSession(Object principal, String sessionId, Date lastRequest) {
    super(principal, sessionId, lastRequest);
  }

  private static final long serialVersionUID = -7693658135933008107L;

  private Map<String, Object> sessionAttrs = new HashMap<>();

  @SuppressWarnings("unchecked")
  public <T> T getAttribute(String attributeName) {
    return (T) this.sessionAttrs.get(attributeName);
  }

  public Set<String> getAttributeNames() {
    return new HashSet<>(this.sessionAttrs.keySet());
  }

  public void setAttribute(String attributeName, Object attributeValue) {
    if (attributeValue == null) {
      removeAttribute(attributeName);
    } else {
      this.sessionAttrs.put(attributeName, attributeValue);
    }
  }

  public void removeAttribute(String attributeName) {
    this.sessionAttrs.remove(attributeName);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof SessionInformation && getSessionId().equals(((SessionInformation) obj).getSessionId());
  }

  @Override
  public int hashCode() {
    return getSessionId().hashCode();
  }

  public static String generateId() {
    return UUID.randomUUID().toString();
  }

}
