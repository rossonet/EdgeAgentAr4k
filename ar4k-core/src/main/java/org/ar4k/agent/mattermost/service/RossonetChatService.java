package org.ar4k.agent.mattermost.service;

import java.io.IOException;
import java.util.Arrays;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.messages.ChatMessage;
import org.ar4k.agent.core.data.messages.JSONMessage;
import org.ar4k.agent.core.data.messages.StringMessage;
import org.ar4k.agent.core.interfaces.EdgeChannel;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.mattermost.ChatPayload;
import org.ar4k.agent.mattermost.MatterMostCallBack;
import org.ar4k.agent.mattermost.MatterMostClientAr4k;
import org.ar4k.agent.mattermost.model.Channel;
import org.ar4k.agent.mattermost.model.Post;
import org.ar4k.agent.mattermost.model.Team;
import org.ar4k.agent.mattermost.model.User;
import org.json.JSONObject;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Gestore servizio per connessioni rossonet mm.
 *
 */
public class RossonetChatService implements EdgeComponent, MatterMostCallBack, MessageHandler {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(RossonetChatService.class.toString());

	private final static Gson gson = new GsonBuilder().create();
	// iniettata vedi set/get
	private RossonetChatConfig configuration = null;

	private Homunculus homunculus = null;

	private DataAddress dataspace = null;

	private ServiceStatus serviceStatus = ServiceStatus.INIT;

	private MatterMostClientAr4k mattermostClient = null;

	private EdgeChannel requestCommandChannel = null;

	private EdgeChannel statusChannel = null;

	private EdgeChannel newUserChannel = null;

	private EdgeChannel newChannelChannel = null;

	private EdgeChannel newTeamChannel = null;

	private EdgeChannel writeCommandChannel = null;

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
		newUserChannel = dataspace.createOrGetDataChannel("user", IPublishSubscribeChannel.class,
				"new users registered or changed", (String) null, (String) null, Arrays.asList("new_user"), this);
		newChannelChannel = dataspace.createOrGetDataChannel("channel", IPublishSubscribeChannel.class,
				"new channel registered or changed", (String) null, (String) null, Arrays.asList("new_channel"), this);
		newTeamChannel = dataspace.createOrGetDataChannel("team", IPublishSubscribeChannel.class,
				"new team registered or changed", (String) null, (String) null, Arrays.asList("new_team"), this);
		requestCommandChannel = dataspace.createOrGetDataChannel("request", IPublishSubscribeChannel.class,
				"new messages received from remote", (String) null, (String) null,
				Arrays.asList("request", "received", "new_post", "new_message"), this);
		writeCommandChannel = dataspace.createOrGetDataChannel("write", IPublishSubscribeChannel.class,
				"command queue to send message to remote", (String) null, (String) null, Arrays.asList("send", "write"),
				this);
		((SubscribableChannel) writeCommandChannel.getChannel()).subscribe(this);
		statusChannel = dataspace.createOrGetDataChannel("status", IPublishSubscribeChannel.class,
				"status of matermost connection", (String) null, (String) null, Arrays.asList("status", "text"), this);

	}

	@Override
	public void close() throws IOException {
		kill();
	}

	@Override
	public void kill() {
		serviceStatus = ServiceStatus.KILLED;
		if (this.mattermostClient != null) {
			try {
				mattermostClient.close();
			} catch (Exception exception) {
				logger.logException(exception);
			}
		}
	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		if (mattermostClient != null && mattermostClient.isConnected()) {
			serviceStatus = ServiceStatus.RUNNING;
		} else {
			if (!serviceStatus.equals(ServiceStatus.INIT) && !serviceStatus.equals(ServiceStatus.KILLED)) {
				serviceStatus = ServiceStatus.FAULT;
			}
		}
		if (statusChannel != null && serviceStatus != null) {
			final StringMessage message = new StringMessage();
			message.setPayload(serviceStatus.toString());
			statusChannel.getChannel().send(message);
		}
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
		JSONObject json = new JSONObject(gson.toJson(configuration));
		json.put("status", serviceStatus.toString());
		return json;
	}

	@Override
	public void connectionStarted() {
		serviceStatus = ServiceStatus.RUNNING;
	}

	@Override
	public void onNewChannel(Channel channel) {
		if (newChannelChannel != null) {
			ChatPayload payload = new ChatPayload();
			payload.setId(channel.getId());
			payload.setChannelId(channel.getId());
			payload.setCreatorId(channel.getCreatorId());
			payload.setCreateAt(channel.getCreateAt());
			payload.setUpdateAt(channel.getUpdateAt());
			payload.setDeleteAt(channel.getDeleteAt());
			payload.setDisplayName(channel.getDisplayName());
			payload.setHeader(channel.getHeader());
			payload.setName(channel.getName());
			payload.setPurpose(channel.getPurpose());
			payload.setSchemeId(channel.getSchemeId());
			payload.setTeamId(channel.getTeamId());
			payload.setType(channel.getType() != null ? channel.getType().toString() : "NaN");
			ChatMessage message = new ChatMessage();
			message.setPayload(payload);
			newChannelChannel.getChannel().send(message);
		}
	}

	@Override
	public void onNewUser(User checkedUser) {
		if (newUserChannel != null) {
			ChatPayload payload = new ChatPayload();
			payload.setId(checkedUser.getId());
			payload.setUserId(checkedUser.getId());
			payload.setFirstName(checkedUser.getFirstName());
			payload.setLastName(checkedUser.getLastName());
			payload.setCreateAt(checkedUser.getCreateAt());
			payload.setUpdateAt(checkedUser.getUpdateAt());
			payload.setDeleteAt(checkedUser.getDeleteAt());
			payload.setAuthData(checkedUser.getAuthData());
			payload.setlocale(checkedUser.getLocale());
			payload.setNickname(checkedUser.getNickname());
			payload.setUsername(checkedUser.getUsername());
			payload.setBotDescription(checkedUser.getBotDescription());
			payload.setEmail(checkedUser.getEmail());
			payload.setPosition(checkedUser.getPosition());
			payload.setRoles(checkedUser.getRoles());
			ChatMessage message = new ChatMessage();
			message.setPayload(payload);
			newUserChannel.getChannel().send(message);
		}

	}

	@Override
	public void onNewTeam(Team team) {
		if (newTeamChannel != null) {
			ChatPayload payload = new ChatPayload();
			payload.setId(team.getId());
			payload.setTeamId(team.getId());
			payload.setCompanyName(team.getCompanyName());
			payload.setCreateAt(team.getCreateAt());
			payload.setUpdateAt(team.getUpdateAt());
			payload.setDeleteAt(team.getDeleteAt());
			payload.setDisplayName(team.getDisplayName());
			payload.setDescription(team.getDescription());
			payload.setName(team.getName());
			payload.setEmail(team.getEmail());
			payload.setSchemeId(team.getSchemeId());
			payload.setAllowedDomains(team.getAllowedDomains());
			payload.setInviteId(team.getInviteId());
			payload.setType(team.getType() != null ? team.getType().toString() : "NaN");
			ChatMessage message = new ChatMessage();
			message.setPayload(payload);
			newTeamChannel.getChannel().send(message);
		}
	}

	@Override
	public void onNewPost(Post post) {
		if (requestCommandChannel != null) {
			ChatPayload payload = new ChatPayload();
			payload.setId(post.getId());
			payload.setMessage(post.getMessage());
			payload.setCreateAt(post.getCreateAt());
			payload.setEditAt(post.getEditAt());
			payload.setUpdateAt(post.getUpdateAt());
			payload.setDeleteAt(post.getDeleteAt());
			payload.setChannelId(post.getChannelId());
			payload.setUserId(post.getUserId());
			payload.setRootId(post.getRootId());
			payload.setParentId(post.getParentId());
			payload.setOriginalId(post.getOriginalId());
			payload.setHashtags(post.getHashtags());
			payload.setType(post.getType() != null ? post.getType().toString() : "NaN");
			ChatMessage message = new ChatMessage();
			message.setPayload(payload);
			requestCommandChannel.getChannel().send(message);
		}
	}

	@Override
	public String getServiceName() {
		return getConfiguration().getName();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RossonetChatService [configuration=");
		builder.append(configuration);
		builder.append(", serviceStatus=");
		builder.append(serviceStatus);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public void handleMessage(Message<?> message) {
		if (message instanceof ChatMessage) {
			if (mattermostClient != null) {
				final ChatPayload cm = ((ChatMessage) message).getPayload();
				mattermostClient.sendPost(cm.getChannelId(), cm.getMessage());
			} else {
				logger.error("try to send message without connection " + message.getPayload());
			}
		} else if (message instanceof JSONMessage) {
			if (mattermostClient != null) {
				final JSONObject json = ((JSONMessage) message).getPayload();
				mattermostClient.sendPost(json.getString("channelId"), json.getString("message"));
			} else {
				logger.error("try to send message without connection " + message.getPayload());
			}
		} else {
			logger.error("received bad message type in write queue " + message.getPayload());
		}

	}

}
