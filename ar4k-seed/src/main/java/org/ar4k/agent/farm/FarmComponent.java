package org.ar4k.agent.farm;

import java.io.IOException;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class FarmComponent implements EdgeComponent {

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(FarmComponent.class.toString());

	private Homunculus homunculus = null;

	private FarmConfig configuration = null;

	private String beanName = null;

	// TODO DataAddress
	private DataAddress dataspace = null;

	public FarmComponent(Homunculus homunculus, FarmConfig tribeConfig) {
		this.configuration = tribeConfig;
		this.homunculus = homunculus;
	}

	public FarmComponent(FarmConfig tribeConfig) {
		homunculus = tribeConfig.homunculus;
		this.configuration = tribeConfig;
	}

	@Override
	public ServiceConfig getConfiguration() {
		return configuration;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		this.configuration = (FarmConfig) configuration;
	}

	public String getBeanName() {
		return beanName;
	}

	@Override
	public void close() throws IOException {
		kill();
	}

	@Override
	public void init() {

	}

	@Override
	public void kill() {

	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		return ServiceStatus.RUNNING;
	}

	@Override
	public Homunculus getHomunculus() {
		return homunculus;
	}

	@Override
	public DataAddress getDataAddress() {
		return dataspace;
	}

	@Override
	public void setDataAddress(DataAddress dataAddress) {
		dataspace = dataAddress;
	}

	@Override
	public void setHomunculus(Homunculus homunculus) {
		this.homunculus = homunculus;
	}

	@Override
	public JSONObject getDescriptionJson() {
		return new JSONObject(gson.toJsonTree(configuration).getAsString());
	}

}
