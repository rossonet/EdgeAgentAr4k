package org.ar4k.agent.cortex;

/**
 * Componente base micro configurazione
 */

import java.io.Serializable;

public interface Meme extends Serializable, Cloneable {

  public String toJson();

  public Meme fromJson(String json);

  public String toBase64();

  public Meme fromBase64(String base64);

}
