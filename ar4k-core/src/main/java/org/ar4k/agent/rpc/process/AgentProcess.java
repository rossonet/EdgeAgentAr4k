package org.ar4k.agent.rpc.process;

import java.io.Closeable;

public interface AgentProcess extends Closeable {

  public boolean isAlive();

  public String getLabel();

  public void setLabel(String label);

  public String getOutput();

  public String getErrors();

}
