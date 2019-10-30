package org.ar4k.agent.rpc.process;

public interface AgentProcess extends AutoCloseable {

  boolean isAlive();

  String getLabel();

  void setLabel(String label);

  String getOutput();

  String getErrors();

  void eval(String script);

}
