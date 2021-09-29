package org.ar4k.agent.core.services;

/**
 * interfaccia gestione servizi da Anima
 * 
 * @author andrea
 *
 * @param <S> tipo pot
 * @see org.ar4k.agent.core.services.EdgeComponent
 *
 */
public interface ServiceComponent<S extends EdgeComponent> extends AutoCloseable {

  S getPot();

  boolean isRunning();

  void start();

  void stop();

}
