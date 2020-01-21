package org.ar4k.agent.network;

import org.ar4k.agent.exception.ServiceInitException;

public interface NetworkTunnel extends AutoCloseable {
  public static enum NetworkMode {
    SERVER_TO_BYTES_TCP, BYTES_TO_SERVER_TCP, SERVER_TO_BYTES_UDP, BYTES_TO_SERVER_UDP
  }

  NetworkConfig getConfig();

  void init() throws ServiceInitException;

  void kill();

  NetworkHub getHub();

}
