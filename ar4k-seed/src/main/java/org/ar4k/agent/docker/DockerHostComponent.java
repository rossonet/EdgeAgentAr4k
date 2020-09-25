package org.ar4k.agent.docker;

import java.util.List;

import org.ar4k.agent.core.interfaces.ManagedArchives;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.farm.FarmComponent;
import org.ar4k.agent.farm.ManagedHost;
import org.ar4k.agent.farm.ManagedNetworkInterface;
import org.ar4k.agent.farm.ManagedVirtualSystem;
import org.ar4k.agent.farm.SystemStatus;
import org.json.JSONObject;

import com.github.dockerjava.api.DockerClient;

public class DockerHostComponent extends FarmComponent {

	private DockerHostConfig farmConfig;

	DockerClient dockerClient = null;

	public DockerHostComponent(DockerHostConfig farmConfig) {
		super(farmConfig);
		this.farmConfig = farmConfig;
	}

	@Override
	public ServiceConfig getConfiguration() {
		return farmConfig;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		this.farmConfig = (DockerHostConfig) configuration;
	}

	@Override
	public List<ManagedVirtualSystem> getManagedSystems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SystemStatus getSystemStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init() throws ServiceInitException {
		// TODO Auto-generated method stub

	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub

	}

	@Override
	public JSONObject getDescriptionJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ManagedNetworkInterface> getManagedNetworks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ManagedArchives> getManagedArchives() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ManagedHost> getManagedHosts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pruneSystem() {
		// TODO Auto-generated method stub

	}

	@Override
	public ConnectionState getConnectionState() {
		// TODO Auto-generated method stub
		return null;
	}

}
