package org.ar4k.agent.core;

public interface ServiceComponent<S extends Ar4kComponent> extends AutoCloseable {

  S getPot();

  void start();

  void stop();

  boolean isRunning();

}
