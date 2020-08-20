package org.ar4k.agent.core;

/**
 * interfaccia gestione servizi da Anima
 * 
 * @author andrea
 *
 * @param <S> tipo pot
 * @see org.ar4k.agent.core.Ar4kComponent
 *
 */
public interface ServiceComponent<S extends Ar4kComponent> extends AutoCloseable {

  S getPot();

  void start();

  void stop();

  boolean isRunning();

}
