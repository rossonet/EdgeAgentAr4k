package org.ar4k.agent.mattermost.service;

import java.io.IOException;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.interfaces.EdgeChannel;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.mattermost.MatterMostCallBack;
import org.ar4k.agent.mattermost.MatterMostClientAr4k;
import org.ar4k.agent.mattermost.model.Channel;
import org.ar4k.agent.mattermost.model.Post;
import org.ar4k.agent.mattermost.model.Team;
import org.ar4k.agent.mattermost.model.User;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Gestore servizio per connessioni rossonet mm.
 *
 */
public class RossonetChatService implements EdgeComponent, MatterMostCallBack {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(RossonetChatService.class.toString());

	// iniettata vedi set/get
	private RossonetChatConfig configuration = null;
	private final static Gson gson = new GsonBuilder().create();

	private Homunculus homunculus = null;

	private DataAddress dataspace = null;

	private ServiceStatus serviceStatus = ServiceStatus.INIT;

	private MatterMostClientAr4k mattermostClient;

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
		setDataspace();
		this.mattermostClient = new MatterMostClientAr4k(getConfiguration().mmServer, getConfiguration().username,
				getConfiguration().password, getConfiguration().token, this);

	}

	private void setDataspace() {
		final EdgeChannel requestCommand = dataspace.createOrGetDataChannel(null, IPublishSubscribeChannel.class,
				"requested command on ssh", (String) null, (String) null, null, this);
		final EdgeChannel replyCommand = dataspace.createOrGetDataChannel(null, IPublishSubscribeChannel.class,
				"reply command to ssh", (String) null, (String) null, null, this);
		final EdgeChannel status = dataspace.createOrGetDataChannel(null, IPublishSubscribeChannel.class,
				"reply command to ssh", (String) null, (String) null, null, this);

	}

	@Override
	public void close() throws IOException {
		kill();
	}

	@Override
	public void kill() {
		// TODO

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
		// TODO descrione come json
	}

	@Override
	public void connectionStarted() {
		// TODO portare su data

	}

	@Override
	public void onNewChannel(Channel channel) {
		// TODO portare su data

	}

	@Override
	public void onNewUser(User checkedUser) {
		// TODO portare su data

	}

	@Override
	public void onNewTeam(Team team) {
		// TODO portare su data

	}

	@Override
	public void onNewPost(Post post) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getServiceName() {
		return getConfiguration().getName();
	}

}
