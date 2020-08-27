package org.ar4k.gw.studio.tunnels.socket;

import javax.net.SocketFactory;

import org.ar4k.agent.core.EdgeComponent;

public interface ISocketFactoryComponent extends EdgeComponent {

  public SocketFactory getSocketFactory();
}
