package org.ar4k.agent.docker;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.ar4k.agent.core.interfaces.ManagedArchives;
import org.ar4k.agent.farm.ManagedNetworkInterface;
import org.ar4k.agent.farm.ManagedVirtualSystem;
import org.json.JSONObject;

import com.beust.jcommander.Parameter;

public class DockerContainer implements Serializable, ManagedVirtualSystem {

	private static final long serialVersionUID = 3146839827634554625L;

	@Parameter(names = "--image", description = "docker image")
	public String image;

	@Parameter(names = "--sharedNetworkPorts", description = "network ports")
	public Map<String, DockerNetworkPort> sharedNetworkPorts = new HashMap<>();

	@Parameter(names = "--environmentVariables", description = "environment variables")
	public Map<String, String> environmentVariables = new HashMap<>();

	@Parameter(names = "--sharedArchives", description = "network ports")
	public Map<String, DockerArchive> sharedArchives = new HashMap<>();

	@Parameter(names = "--isAdministrative", description = "is able to administrate the Docker system")
	public boolean administrative = false;

	@Parameter(names = "--isAutostartEnabled", description = "ia auto start enabled")
	public boolean autostartEnabled = true;

	@Parameter(names = "--postScript", description = "to run after start")
	public String postScript;

	@Override
	public void setAutoStart(boolean isAutostartEnable) {
		autostartEnabled = isAutostartEnable;

	}

	@Override
	public boolean isAutostartEnabled() {
		return autostartEnabled;
	}

	@Override
	public Map<String, ManagedArchives> getArchives() {
		Map<String, ManagedArchives> result = new HashMap<>();
		for (Entry<String, DockerArchive> e : sharedArchives.entrySet()) {
			result.put(e.getKey(), e.getValue());
		}
		return result;
	}

	@Override
	public Map<String, ManagedNetworkInterface> getNetworks() {
		Map<String, ManagedNetworkInterface> result = new HashMap<>();
		for (Entry<String, DockerNetworkPort> e : sharedNetworkPorts.entrySet()) {
			result.put(e.getKey(), e.getValue());
		}
		return result;
	}

	@Override
	public SystemStatus getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SystemStatus start() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SystemStatus stop() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SystemStatus remove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getJSONStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLog() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path getLogPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String execOnContainer(String targetReference, String command, long timeout) {
		// TODO Auto-generated method stub
		return null;
	}

}
