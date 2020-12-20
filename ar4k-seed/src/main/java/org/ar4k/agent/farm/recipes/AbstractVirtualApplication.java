package org.ar4k.agent.farm.recipes;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.ar4k.agent.core.interfaces.ManagedArchives;
import org.ar4k.agent.farm.ManagedNetworkInterface;
import org.ar4k.agent.farm.ManagedVirtualApplication;
import org.json.JSONObject;

public abstract class AbstractVirtualApplication implements ManagedVirtualApplication {

	@Override
	public void setAutoStart(boolean isAutostartEnable) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAutostartEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, ManagedArchives> getArchives() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Provider> getSupportedProviders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, ManagedNetworkInterface> getNetworks() {
		// TODO Auto-generated method stub
		return null;
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
