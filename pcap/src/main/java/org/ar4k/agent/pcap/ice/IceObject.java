package org.ar4k.agent.pcap.ice;

import java.io.IOException;
import java.io.Serializable;

public interface IceObject extends Serializable, Cloneable {

  public void valueOf(byte[] bytes) throws IOException;

  public char getTypeCode();

}
