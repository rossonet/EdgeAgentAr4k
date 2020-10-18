package org.ar4k.agent.core.interfaces;

import java.util.List;

import org.ar4k.agent.tunnels.http2.grpc.beacon.Agent;

public interface IScadaAgent {

	boolean isFoundBy(String filter);

	Agent getAgent();

	String getName();

	String getDescription();

	String getLastContact();

	int getCommandsCount();

	String getOsVersion();

	String getOsName();

	long getFreeMemoryMB();

	long getTotalMemoryMB();

	String getProcessors();

	String getAgentConfigJson();

	String getAgentHelp();

	String getAgentConfigYml();

	List<String> listCommands();

	List<String> listTunnelsDescription();

	String getJavaVm();

	String getJsonHardwareInfo();

	List<String> getProvides();

	List<String> getRequired();

	String execCommand(String command);

	List<String> completeCommand(String command);

}