package org.ar4k.agent.core;

/**
 * interfaccia gestione servizi da Anima
 * 
 * @author andrea
 *
 * @param <S> tipo pot
 * @see org.ar4k.agent.core.EdgeComponent
 *
 */
public interface ServiceComponent<S extends EdgeComponent> extends AutoCloseable {

  S getPot();

  void start();

  void stop();

  boolean isRunning();

}
