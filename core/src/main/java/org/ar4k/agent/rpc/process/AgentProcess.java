package org.ar4k.agent.rpc.process;

public interface AgentProcess {

  public boolean isAlive();

  public String getLabel();

  public void setLabel(String label);

  public String getOutput();

  public String getErrors();

}
