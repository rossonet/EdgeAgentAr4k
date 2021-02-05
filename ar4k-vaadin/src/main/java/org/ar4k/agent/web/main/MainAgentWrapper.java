package org.ar4k.agent.web.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ar4k.agent.config.network.NetworkTunnel;
import org.ar4k.agent.core.interfaces.IBeaconClient;
import org.ar4k.agent.core.interfaces.IScadaAgent;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Command;
import org.json.JSONObject;

import com.google.protobuf.ByteString;

public class MainAgentWrapper implements IScadaAgent {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(MainAgentWrapper.class.toString());

	private final Agent agent;
	private final IBeaconClient beaconClient;

	public MainAgentWrapper(IBeaconClient beaconClient, Agent agent) {
		this.beaconClient = beaconClient;
		this.agent = agent;
	}

	@Override
	public boolean isFoundBy(String filter) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getName() {
		return agent.getAgentUniqueName();

	}

	@Override
	public String getDescription() {
		return agent.getShortDescription();

	}

	@Override
	public String getLastContact() {
		return new Date(agent.getLastContact().getSeconds() * 1000).toString();

	}

	@Override
	public int getCommandsCount() {
		return beaconClient.listCommadsOnAgent(agent.getAgentUniqueName()).getCommandsCount();
	}

	@Override
	public String getOsVersion() {
		return new JSONObject(agent.getJsonHardwareInfo()).getString("piOSVersion");
	}

	@Override
	public String getOsName() {
		return new JSONObject(agent.getJsonHardwareInfo()).getString("piOSName");
	}

	@Override
	public long getFreeMemoryMB() {
		return new JSONObject(agent.getJsonHardwareInfo()).getLong("piFreeMemory") / 1024 / 1024;
	}

	@Override
	public long getTotalMemoryMB() {
		return new JSONObject(agent.getJsonHardwareInfo()).getLong("piTotalMemory") / 1024 / 1024;
	}

	@Override
	public String getProcessors() {
		final JSONObject jsonObject = new JSONObject(agent.getJsonHardwareInfo());
		return String.valueOf(Integer.valueOf(jsonObject.getString("piProcessor")) + 1) + " x "
				+ jsonObject.getString("piCPUModelName");
	}

	@Override
	public String getAgentConfigJson() {
		return beaconClient.getConfigFromAgent(agent.getAgentUniqueName()).getJsonConfig();
	}

	@Override
	public String getAgentHelp() {
		return beaconClient.runCommadsOnAgent(agent.getAgentUniqueName(), "help").getReply();
	}

	@Override
	public String getAgentConfigYml() {
		return beaconClient.getConfigFromAgent(agent.getAgentUniqueName()).getYmlConfig();
	}

	@Override
	public String execCommand(String command) {
		return beaconClient.runCommadsOnAgent(agent.getAgentUniqueName(), command).getReply();
	}

	@Override
	public List<String> completeCommand(String command) {
		List<String> result = new ArrayList<>();
		for (ByteString propose : beaconClient.runCompletitionOnAgent(agent.getAgentUniqueName(), command)
				.getRepliesList().asByteStringList()) {
			result.add(propose.toStringUtf8());
		}
		return result;
	}

	@Override
	public List<String> listCommands() {
		final List<String> returnList = new ArrayList<>();
		for (Command c : beaconClient.listCommadsOnAgent(agent.getAgentUniqueName()).getCommandsList()) {
			returnList.add(c.getCommand());
		}
		return returnList;
	}

	@Override
	public List<String> listTunnelsDescription() {
		final List<String> returnList = new ArrayList<>();
		for (NetworkTunnel c : beaconClient.getTunnels()) {
			returnList.add(c.toString());
		}
		return returnList;
	}

	@Override
	public String getJavaVm() {
		final JSONObject jsonObject = new JSONObject(agent.getJsonHardwareInfo());
		return jsonObject.getString("piJavaVM") + " " + jsonObject.getString("piJavaVersion");
	}

	@Override
	public String getJsonHardwareInfo() {
		return agent.getJsonHardwareInfo();
	}

	@Override
	public List<String> getProvides() {
		return beaconClient.getRuntimeProvides(agent.getAgentUniqueName()).getListDatasList();
	}

	@Override
	public List<String> getRequired() {
		return beaconClient.getRuntimeRequired(agent.getAgentUniqueName()).getListDatasList();
	}

	@Override
	public Agent getAgent() {
		return agent;
	}

}
