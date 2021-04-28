package org.ar4k.agent.core.interfaces;

/**
 * interfaccia gestione servizi da Anima
 * 
 * @author andrea
 *
 * @param <S> tipo pot
 * @see org.ar4k.agent.core.interfaces.EdgeComponent
 *
 */
public interface ServiceComponent<S extends EdgeComponent> extends AutoCloseable {

  S getPot();

  boolean isRunning();

  void start();

  void stop();

}
