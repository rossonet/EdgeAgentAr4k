package org.ar4k.agent.rpc;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.session.SessionRegistry;

// attore rpc. La "personalità" dell'agente
public interface IHomunculusRpc extends AutoCloseable,SessionRegistry, ApplicationListener<SessionDestroyedEvent> {

}
