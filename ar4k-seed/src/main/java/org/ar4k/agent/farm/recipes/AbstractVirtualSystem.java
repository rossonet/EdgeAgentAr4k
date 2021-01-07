package org.ar4k.agent.farm.recipes;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.ar4k.agent.core.interfaces.ManagedArchives;
import org.ar4k.agent.farm.ManagedNetworkInterface;
import org.ar4k.agent.farm.ManagedVirtualSystem;
import org.json.JSONObject;

public abstract class AbstractVirtualSystem implements ManagedVirtualSystem {

	@Override
	public void setAutoStart(boolean isAutostartEnable) {
		// TODO setAutoStart in AbstractVirtualSystem

	}

	@Override
	public boolean isAutostartEnabled() {
		// TODO isAutostartEnabled in AbstractVirtualSystem
		return false;
	}

	@Override
	public Map<String, ManagedArchives> getArchives() {
		// TODO getArchives in AbstractVirtualSystem
		return null;
	}

	@Override
	public List<Provider> getSupportedProviders() {
		// TODO getSupportedProviders in AbstractVirtualSystem
		return null;
	}

	@Override
	public Map<String, ManagedNetworkInterface> getNetworks() {
		// TODO getNetworks in AbstractVirtualSystem
		return null;
	}

	@Override
	public SystemStatus getStatus() {
		// TODO getStatus in AbstractVirtualSystem
		return null;
	}

	@Override
	public SystemStatus start() {
		// TODO start in AbstractVirtualSystem
		return null;
	}

	@Override
	public SystemStatus stop() {
		// TODO stop in AbstractVirtualSystem
		return null;
	}

	@Override
	public SystemStatus remove() {
		// TODO remove in AbstractVirtualSystem
		return null;
	}

	@Override
	public JSONObject getJSONStatus() {
		// TODO getJSONStatus in AbstractVirtualSystem
		return null;
	}

	@Override
	public String getLog() {
		// TODO getLog in AbstractVirtualSystem
		return null;
	}

	@Override
	public Path getLogPath() {
		// TODO getLogPath in AbstractVirtualSystem
		return null;
	}

	@Override
	public String execOnContainer(String targetReference, String command, long timeout) {
		// TODO execOnContainer in AbstractVirtualSystem
		return null;
	}

}
