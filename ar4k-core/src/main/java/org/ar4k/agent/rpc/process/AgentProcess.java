package org.ar4k.agent.rpc.process;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

public interface AgentProcess extends AutoCloseable {

	static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(AgentProcess.class);

	boolean isAlive();

	String getLabel();

	void setLabel(String label);

	String getOutput();

	String getErrors();

	void eval(String script);

}
