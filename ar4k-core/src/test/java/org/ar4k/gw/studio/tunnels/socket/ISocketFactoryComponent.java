package org.ar4k.gw.studio.tunnels.socket;

import javax.net.SocketFactory;

import org.ar4k.agent.core.Ar4kComponent;

public interface ISocketFactoryComponent extends Ar4kComponent {

  public SocketFactory getSocketFactory();
}
