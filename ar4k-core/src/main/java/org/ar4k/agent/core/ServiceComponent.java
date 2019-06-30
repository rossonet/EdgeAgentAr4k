package org.ar4k.agent.core;

// componente gestito come servizio
public interface ServiceComponent extends Ar4kComponent, Runnable {

  public Anima getAnima();

  public void setAnima(Anima anima);

  public void start();
  
  public void stop();

}
