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
		// TODO setAutoStart in AbstractVirtualApplication

	}

	@Override
	public boolean isAutostartEnabled() {
		// TODO isAutostartEnabled in AbstractVirtualApplication
		return false;
	}

	@Override
	public Map<String, ManagedArchives> getArchives() {
		// TODO getArchives in AbstractVirtualApplication
		return null;
	}

	@Override
	public List<Provider> getSupportedProviders() {
		// TODO getSupportedProviders in AbstractVirtualApplication
		return null;
	}

	@Override
	public Map<String, ManagedNetworkInterface> getNetworks() {
		// TODO getNetworks in AbstractVirtualApplication
		return null;
	}

	@Override
	public SystemStatus getStatus() {
		// TODO getStatus in AbstractVirtualApplication
		return null;
	}

	@Override
	public SystemStatus start() {
		// TODO start in AbstractVirtualApplication
		return null;
	}

	@Override
	public SystemStatus stop() {
		// TODO stop in AbstractVirtualApplication
		return null;
	}

	@Override
	public SystemStatus remove() {
		// TODO remove in AbstractVirtualApplication
		return null;
	}

	@Override
	public JSONObject getJSONStatus() {
		// TODO getJSONStatus in AbstractVirtualApplication
		return null;
	}

	@Override
	public String getLog() {
		// TODO getLog in AbstractVirtualApplication
		return null;
	}

	@Override
	public Path getLogPath() {
		// TODO getLogPath in AbstractVirtualApplication
		return null;
	}

	@Override
	public String execOnContainer(String targetReference, String command, long timeout) {
		// TODO execOnContainer in AbstractVirtualApplication
		return null;
	}

}
