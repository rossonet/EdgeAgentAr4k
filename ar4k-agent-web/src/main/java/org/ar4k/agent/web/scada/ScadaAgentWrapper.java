package org.ar4k.agent.web.scada;

import java.util.Date;

import org.ar4k.agent.core.IBeaconClient;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.json.JSONObject;

public class ScadaAgentWrapper {

	private final Agent agent;
	private final IBeaconClient beaconClient;

	public ScadaAgentWrapper(IBeaconClient beaconClient, Agent agent) {
		this.beaconClient = beaconClient;
		this.agent = agent;
	}

	public boolean isFoundBy(String filter) {
		// TODO Auto-generated method stub
		return true;
	}

	public Agent getAgent() {
		return agent;
	}

	public String getName() {
		return agent.getAgentUniqueName();

	}

	public String getDescription() {
		return agent.getShortDescription();

	}

	public String getLastContact() {
		return new Date(agent.getLastContact().getSeconds() * 1000).toString();

	}

	public int getCommandsCount() {
		return beaconClient.listCommadsOnAgent(agent.getAgentUniqueName()).getCommandsCount();
	}

	public String getOsVersion() {
		return new JSONObject(agent.getJsonHardwareInfo()).getString("piOSVersion");
	}

	public String getOsName() {
		return new JSONObject(agent.getJsonHardwareInfo()).getString("piOSName");
	}

	public long getFreeMemoryMB() {
		return new JSONObject(agent.getJsonHardwareInfo()).getLong("piFreeMemory") / 1024 / 1024;
	}

	public long getTotalMemoryMB() {
		return new JSONObject(agent.getJsonHardwareInfo()).getLong("piTotalMemory") / 1024 / 1024;
	}

	public String getProcessors() {
		final JSONObject jsonObject = new JSONObject(agent.getJsonHardwareInfo());
		return jsonObject.getString("piProcessor") + " x " + jsonObject.getString("piCPUModelName");
	}

	public String getJavaVm() {
		final JSONObject jsonObject = new JSONObject(agent.getJsonHardwareInfo());
		return jsonObject.getString("piJavaVM") + " " + jsonObject.getString("piJavaVersion");
	}

	public void activateXpraConnection() {

		// TODO Auto-generated method stub

	}

	public void sendDisconnectToXpraConnection() {
		// TODO Auto-generated method stub

	}

}