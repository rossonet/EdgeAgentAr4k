package org.ar4k.agent.mattermost.service;

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

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Gestore servizio per connessioni rossonet mm.
 *
 */
public class RossonetChatService implements EdgeComponent {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(RossonetChatService.class.toString());

	// iniettata vedi set/get
	private RossonetChatConfig configuration = null;
	private final static Gson gson = new GsonBuilder().create();

	private Homunculus homunculus = null;

	private DataAddress dataspace = null;

	private ServiceStatus serviceStatus = ServiceStatus.INIT;

	@Override
	public RossonetChatConfig getConfiguration() {
		return configuration;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		this.configuration = ((RossonetChatConfig) configuration);
	}

	@Override
	public void init() {
		//TODO

	}

	@Override
	public void close() throws IOException {
		kill();
	}

	@Override
	public void kill() {
		//TODO

	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		return serviceStatus;
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
		return null;
		//TODO
	}

}
