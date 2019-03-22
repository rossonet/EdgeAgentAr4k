package org.ar4k.agent.tunnel;

import javax.net.SocketFactory;

import org.ar4k.agent.core.Ar4kComponent;

public interface TunnelComponent extends Ar4kComponent {

  public SocketFactory getSocketFactory();
}
