package org.ar4k.agent.mattermost;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import javax.websocket.ClientEndpoint;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.mattermost.MatterMostWebSocketHandler.MessageHandler;
import org.ar4k.agent.mattermost.client4.ApiResponse;
import org.ar4k.agent.mattermost.client4.MattermostClient;
import org.ar4k.agent.mattermost.model.Channel;
import org.ar4k.agent.mattermost.model.ChannelList;
import org.ar4k.agent.mattermost.model.ChannelType;
import org.ar4k.agent.mattermost.model.Post;
import org.ar4k.agent.mattermost.model.PostList;
import org.ar4k.agent.mattermost.model.Team;
import org.ar4k.agent.mattermost.model.TeamList;
import org.ar4k.agent.mattermost.model.User;
import org.ar4k.agent.mattermost.model.UserList;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@ClientEndpoint
public class MatterMostClientAr4k implements MessageHandler, AutoCloseable {

	private static final String API_V4_WEBSOCKET = "/api/v4/websocket";

	private class ReadNewMessageTask extends TimerTask {

		@Override
		public void run() {
			try {
				refreshDataFromServer();
			} catch (final Exception ex) {
				logger.warn("during refresh on " + rossonetChatServer, ex);
			}
		}
	}

	public static final int DELAY_CHECK_POSTS = 40 * 1000;
	public static final int DELAY_CHECK_TEAMS = 5 * 60 * 1000;
	public static final int DELAY_CHECK_CHANNELS = 2 * 60 * 1000;
	public static final int DELAY_CLEAN_POSTS = 11 * 60 * 1000;
	public static final int DELAY_CHECK_USERS = 7 * 60 * 1000;

	private static final Level LOG_LEVEL = Level.FINE;

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(MatterMostClientAr4k.class);

	private final MattermostClient client;

	private MatterMostWebSocketHandler webSocketChannel = null;

	private final String rossonetChatServer;

	private Timer timerScheduler = new Timer();
	private final Map<String, Channel> workedChannels = new HashMap<>();

	private final Map<String, Post> workedMessages = new HashMap<>();

	private final Map<Long, String> dateForMessages = new HashMap<>();

	private final Map<String, Team> workedTeams = new HashMap<>();

	private boolean firstSynchronizeDone = false;

	private final Map<String, User> workedUsers = new HashMap<>();
	private long lastTeamsTime = 0;
	private long lastUsersTime = 0;
	private long lastChannelsTime = 0;
	private long lastPostsTime = 0;
	private long lastCleanPostsTime = 0;
	private final MatterMostCallBack callBack;

	public MatterMostClientAr4k(String rossonetChatServer, String rossonetChatUserName, String rossonetChatPassword,
			String rossonetChatToken, MatterMostCallBack callback) {
		this.callBack = callback;
		this.rossonetChatServer = rossonetChatServer;
		client = MattermostClient.builder().url(rossonetChatServer).logLevel(LOG_LEVEL).ignoreUnknownProperties()
				.build();
		if (rossonetChatUserName != null && !rossonetChatUserName.isEmpty() && rossonetChatPassword != null
				&& !rossonetChatPassword.isEmpty()) {
			client.login(rossonetChatUserName, rossonetChatPassword);
		} else if (rossonetChatToken != null && !rossonetChatToken.isEmpty()) {
			client.setAccessToken(rossonetChatToken);
		} else {
			logger.error("no valid account or token to connect to chat server " + rossonetChatServer);
		}
		if (isConnected()) {
			try {
				webSocketChannel = new MatterMostWebSocketHandler(
						new URI(rossonetChatServer.replace("http://", "ws://").replace("https://", "wss://")
								+ API_V4_WEBSOCKET));
				webSocketChannel.startSession(client.getAuthToken());
				webSocketChannel.addMessageHandler(this);
			} catch (final Exception e) {
				webSocketChannel = null;
				logger.logException(e);
			}
			prepareNewConnection();
			// System.out.println("*********** getUsers " + client.getUsers().readEntity());
			final ReadNewMessageTask readNewMessageTask = new ReadNewMessageTask();
			timerScheduler.schedule(readNewMessageTask, DELAY_CHECK_POSTS, DELAY_CHECK_POSTS);
			callBack.connectionStarted();
		} else {
			logger.error("no session to chat server " + rossonetChatServer);
		}
	}

	private void prepareNewConnection() {
		workedTeams.clear();
		workedChannels.clear();
		workedMessages.clear();
		dateForMessages.clear();
		refreshDataFromServer();
		firstSynchronizeDone = true;
	}

	public User getMe() {
		return client.getMe().readEntity();
	}

	public Post getPost(Post postRead) {
		return client.getPost(postRead.getId()).readEntity();
	}

	private TeamList getTeamsFromRemote() {
		try {
			final ApiResponse<TeamList> teams = client.getAllTeams();
			if (!teams.hasError()) {
				return teams.readEntity();
			} else {
				return null;
			}
		} catch (final Exception a) {
			logger.error("in team list ", a);
			return null;
		}
	}

	private UserList getUsersFromRemote() {
		try {
			final ApiResponse<UserList> users = client.getUsers();
			if (!users.hasError()) {
				return users.readEntity();
			} else {
				return null;
			}
		} catch (final Exception a) {
			logger.error("in user list ", a);
			return null;
		}
	}

	public boolean isConnected() {
		return client.getMe().getRawResponse().getStatus() == 200;
	}

	private PostList listPostsSince(final String channelId, final long fromTime) {
		try {
			final ApiResponse<PostList> postsSince = client.getPostsSince(channelId, fromTime);
			return postsSince.readEntity();

		} catch (final Exception a) {
			logger.error("in post list ", a);
			return null;
		}
	}

	public Boolean pingServer() {
		return client.getPing().readEntity();
	}

	public Post sendPost(final String channelId, String message) {
		final Post post = new Post();
		post.setMessage(message);
		post.setChannelId(channelId);
		final Post postResult = client.createPost(post).readEntity();
		return postResult;
	}

	private void onNewChannel(Channel channel) {
		logger.debug("new channel configured " + channel);
		callBack.onNewChannel(channel);

	}

	private void onNewPost(Post post) {
		logger.debug("new post received " + post);
		callBack.onNewPost(post);

	}

	private void onNewTeam(Team team) {
		logger.debug("new team configured " + team);
		callBack.onNewTeam(team);
	}

	private void refreshDataFromServer() {
		final long time = new Date().getTime();
		if (!(webSocketChannel != null && webSocketChannel.isActive())) {
			refreshPosts(time);
		}
		refreshTeams(time);
		refreshUsers(time);
		refreshChannels(time);
		removeOldMessages(time);
	}

	private void removeOldMessages(long time) {
		if (lastCleanPostsTime == 0 || lastCleanPostsTime + DELAY_CLEAN_POSTS < time) {
			final Map<Long, String> toDelete = new HashMap<>();
			for (final Entry<Long, String> message : dateForMessages.entrySet()) {
				if (message.getKey() + DELAY_CLEAN_POSTS < time) {
					toDelete.put(message.getKey(), message.getValue());
				}
			}
			if (!toDelete.isEmpty()) {
				for (final Entry<Long, String> messageToDelete : toDelete.entrySet()) {
					workedMessages.remove(messageToDelete.getValue());
					dateForMessages.remove(messageToDelete.getKey());
				}
			}
			lastCleanPostsTime = time;
		}
	}

	private void refreshTeams(final long time) {
		// teams
		if (lastTeamsTime == 0 || lastTeamsTime + DELAY_CHECK_TEAMS < time) {
			final TeamList allTeams = getTeamsFromRemote();
			if (allTeams != null) {
				for (final Team checkedTeam : allTeams) {
					if (!workedTeams.containsKey(checkedTeam.getId())) {
						if (firstSynchronizeDone) {
							onNewTeam(checkedTeam);
						}
						workedTeams.put(checkedTeam.getId(), checkedTeam);
					}
				}
			}
			lastTeamsTime = time;
		}
		// System.out.println("teams -> " + workedTeams);
	}

	private void refreshUsers(final long time) {
		// users
		if (lastUsersTime == 0 || lastUsersTime + DELAY_CHECK_USERS < time) {
			final UserList usersFromRemote = getUsersFromRemote();
			if (usersFromRemote != null) {
				for (final User checkedUser : usersFromRemote) {
					if (!workedUsers.containsKey(checkedUser.getId())) {
						if (firstSynchronizeDone) {
							onNewUser(checkedUser);
						}
						workedUsers.put(checkedUser.getId(), checkedUser);
					}
				}
			}
			lastUsersTime = time;
		}
		// System.out.println("users -> " + workedUsers);
	}

	private void refreshChannels(final long time) {
		if (lastChannelsTime == 0 || lastChannelsTime + DELAY_CHECK_CHANNELS < time) {
			for (final User checkedUser : workedUsers.values()) {
				for (final Team teamLooking : workedTeams.values()) {
					final ChannelList usersChannels = getUsersChannels(teamLooking, checkedUser);
					if (usersChannels != null) {
						for (final Channel checkedChannel : usersChannels) {
							if (!workedChannels.containsKey(checkedChannel.getId())) {
								if (firstSynchronizeDone) {
									onNewChannel(checkedChannel);
								}
								workedChannels.put(checkedChannel.getId(), checkedChannel);
							}
						}
					}
				}
			}
			lastChannelsTime = time;
		}
		// System.out.println("channels -> " + workedChannels);
	}

	private void refreshPosts(final long time) {
		if (lastPostsTime == 0 || lastPostsTime + DELAY_CHECK_POSTS < time) {
			for (final Channel checkedChannel : workedChannels.values()) {
				final PostList listPostsSince = listPostsSince(checkedChannel.getId(), time - (2 * DELAY_CHECK_POSTS));
				if (listPostsSince != null) {
					for (final Post checkedPost : listPostsSince.getPosts().values()) {
						checkPostedMessage(time, checkedPost);
					}
				}
			}
			lastPostsTime = time;
		}
		// System.out.println("posts -> " + workedMessages);
	}

	private void checkPostedMessage(final long time, final Post checkedPost) {
		if (!workedMessages.containsKey(checkedPost.getId())) {
			if (firstSynchronizeDone) {
				onNewPost(checkedPost);
			}
			dateForMessages.put(time, checkedPost.getId());
			workedMessages.put(checkedPost.getId(), checkedPost);
		}
	}

	private ChannelList getUsersChannels(Team team, User user) {
		try {
			final ApiResponse<ChannelList> channelsForTeamForUser = client.getChannelsForTeamForUser(team.getId(),
					user.getId());
			if (!channelsForTeamForUser.hasError()) {
				return channelsForTeamForUser.readEntity();
			} else {
				return null;
			}
		} catch (final Exception a) {
			logger.error("in channel list ", a);
			return null;
		}
	}

	private void onNewUser(User checkedUser) {
		callBack.onNewUser(checkedUser);
	}

	public Map<String, Channel> getChannels() {
		refreshDataFromServer();
		return workedChannels;
	}

	public Map<String, Post> getPosts() {
		refreshDataFromServer();
		return workedMessages;
	}

	public Map<String, Team> getTeams() {
		refreshDataFromServer();
		return workedTeams;
	}

	public Map<String, User> getUsers() {
		refreshDataFromServer();
		return workedUsers;
	}

	@Override
	public void handleMessage(JSONObject message) {
		if (message.has("event")) {
			switch (message.getString("event")) {
			case ("added_to_team"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				/*
				 * { "broadcast": { "user_id": "ifumy6je6b8jdff5yi7ohokwze", "team_id": "",
				 * "channel_id": "", "omit_users": null }, "data": { "user_id":
				 * "ifumy6je6b8jdff5yi7ohokwze", "team_id": "hffh7ry3y7nzmq8aasrhxhienh" },
				 * "event": "added_to_team", "seq": 13 }
				 */
				break;
			case ("authentication_challenge"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("channel_converted"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				/*
				 * { "broadcast": { "user_id": "", "team_id": "hffh7ry3y7nzmq8aasrhxhienh",
				 * "channel_id": "", "omit_users": null }, "data": {"channel_id":
				 * "agibznfpapdk9ebdwpc399nmey"}, "event": "channel_converted", "seq": 25 }
				 */
				break;
			case ("channel_created"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("channel_deleted"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				/*
				 * { "broadcast": { "user_id": "", "team_id": "hffh7ry3y7nzmq8aasrhxhienh",
				 * "channel_id": "", "omit_users": null }, "data": { "delete_at": 1618994491292,
				 * "channel_id": "9tjrznnkyjnp8pxa5eokfrui7r" }, "event": "channel_deleted",
				 * "seq": 34 }
				 */
				break;
			case ("channel_member_updated"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("channel_updated"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				/*
				 * { "broadcast": { "user_id": "", "team_id": "", "channel_id":
				 * "agibznfpapdk9ebdwpc399nmey", "omit_users": null }, "data": {"channel":
				 * "{\"id\":\"agibznfpapdk9ebdwpc399nmey\",\"create_at\":1618994185794,\"update_at\":1618994225026,\"delete_at\":0,\"team_id\":\"hffh7ry3y7nzmq8aasrhxhienh\",\"type\":\"O\",\"display_name\":\"prova\",\"name\":\"prova\",\"header\":\"titolo\",\"purpose\":\"\",\"last_post_at\":1618994197159,\"total_msg_count\":0,\"extra_update_at\":0,\"creator_id\":\"yogedfqb8td98pyc1b6iemxtje\",\"scheme_id\":\"\",\"props\":null,\"group_constrained\":false}"
				 * }, "event": "channel_updated", "seq": 21 }
				 */
				break;
			case ("channel_viewed"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("config_changed"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("delete_team"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("direct_added"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("emoji_added"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("ephemeral_message"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("group_added"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("hello"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				/*
				 * { "broadcast": { "user_id": "ifumy6je6b8jdff5yi7ohokwze", "team_id": "",
				 * "channel_id": "", "omit_users": null }, "data": {"server_version":
				 * "5.30.0.5.31.0.283742429026e2e5404ae708ae1d45ce.false"}, "event": "hello",
				 * "seq": 0 }
				 */
				break;
			case ("leave_team"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				/*
				 * { "broadcast": { "user_id": "", "team_id": "hffh7ry3y7nzmq8aasrhxhienh",
				 * "channel_id": "", "omit_users": null }, "data": { "user_id":
				 * "yogedfqb8td98pyc1b6iemxtje", "team_id": "hffh7ry3y7nzmq8aasrhxhienh" },
				 * "event": "leave_team", "seq": 38 }
				 */
				break;
			case ("license_changed"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("memberrole_updated"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("new_user"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("plugin_disabled"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("plugin_enabled"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("plugin_statuses_changed"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("post_deleted"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				/*
				 * { "broadcast": { "user_id": "", "team_id": "", "channel_id":
				 * "qgsi9rgzzbbwbgms511msjndda", "omit_users": null }, "data": { "post":
				 * "{\"id\":\"xpu75dkiit878xhc85bjn7mkjw\",\"create_at\":1618995822794,\"update_at\":1618995822794,\"edit_at\":0,\"delete_at\":0,\"is_pinned\":false,\"user_id\":\"yogedfqb8td98pyc1b6iemxtje\",\"channel_id\":\"qgsi9rgzzbbwbgms511msjndda\",\"root_id\":\"\",\"parent_id\":\"\",\"original_id\":\"\",\"message\":\"ggg\",\"type\":\"\",\"props\":{\"disable_group_highlight\":true},\"hashtags\":\"\",\"pending_post_id\":\"\",\"reply_count\":0,\"metadata\":{}}",
				 * "delete_by": "yogedfqb8td98pyc1b6iemxtje" }, "event": "post_deleted", "seq":
				 * 5 }
				 * 
				 */
				break;
			case ("post_edited"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("post_unread"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));

				break;
			case ("posted"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				final String postInString = message.getJSONObject("data").getString("post");
				try {
					ObjectMapper mapper = new ObjectMapper();
					mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
					Post newPost = mapper.readerFor(Post.class).readValue(postInString);
					checkPostedMessage(new Date().getTime(), newPost);
				} catch (JsonProcessingException exception) {
					logger.logException(exception);
				}
				break;
			case ("preference_changed"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("preferences_changed"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("preferences_deleted"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("reaction_added"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("reaction_removed"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("response"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("role_updated"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			case ("status_change"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				/*
				 * { "broadcast": { "user_id": "ifumy6je6b8jdff5yi7ohokwze", "team_id": "",
				 * "channel_id": "", "omit_users": null }, "data": { "user_id":
				 * "ifumy6je6b8jdff5yi7ohokwze", "status": "away" }, "event": "status_change",
				 * "seq": 10 }
				 */
				/*
				 * { "broadcast": { "user_id": "ifumy6je6b8jdff5yi7ohokwze", "team_id": "",
				 * "channel_id": "", "omit_users": null }, "data": { "user_id":
				 * "ifumy6je6b8jdff5yi7ohokwze", "status": "online" }, "event": "status_change",
				 * "seq": 1 t
				 */
				break;
			case ("typing"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				/*
				 * { "broadcast": { "user_id": "", "team_id": "", "channel_id":
				 * "q9mh7czwdbdp3gq998ghqwz3cc", "omit_users": {"yogedfqb8td98pyc1b6iemxtje":
				 * true} }, "data": { "user_id": "yogedfqb8td98pyc1b6iemxtje", "parent_id": ""
				 * }, "event": "typing", "seq": 2 }
				 */
				break;
			case ("update_team"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				/*
				 * { "broadcast": { "user_id": "", "team_id": "hffh7ry3y7nzmq8aasrhxhienh",
				 * "channel_id": "", "omit_users": null }, "data": {"team":
				 * "{\"id\":\"hffh7ry3y7nzmq8aasrhxhienh\",\"create_at\":1618993853182,\"update_at\":1618994829951,\"delete_at\":0,\"display_name\":\"team-test-5\",\"name\":\"team-test-2\",\"description\":\"\",\"email\":\"\",\"type\":\"O\",\"company_name\":\"\",\"allowed_domains\":\"\",\"invite_id\":\"\",\"allow_open_invite\":false,\"scheme_id\":null,\"group_constrained\":null}"
				 * }, "event": "update_team", "seq": 36 }
				 */
				break;
			case ("user_added"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				/*
				 * { "broadcast": { "user_id": "", "team_id": "", "channel_id":
				 * "ptx9buoqn7y7byy5sw19xiania", "omit_users": null }, "data": { "user_id":
				 * "ifumy6je6b8jdff5yi7ohokwze", "team_id": "onne1bty4byoiqonj6fm9ox47y" },
				 * "event": "user_added", "seq": 6 }
				 */
				break;
			case ("user_removed"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				/*
				 * { "broadcast": { "user_id": "ifumy6je6b8jdff5yi7ohokwze", "team_id": "",
				 * "channel_id": "", "omit_users": null }, "data": { "remover_id":
				 * "yogedfqb8td98pyc1b6iemxtje", "channel_id": "agibznfpapdk9ebdwpc399nmey" },
				 * "event": "user_removed", "seq": 26 }
				 */
				break;
			case ("user_role_updated"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				/*
				 * { "broadcast": { "user_id": "ifumy6je6b8jdff5yi7ohokwze", "team_id": "",
				 * "channel_id": "", "omit_users": null }, "data": { "user_id":
				 * "ifumy6je6b8jdff5yi7ohokwze", "roles": "system_user" }, "event":
				 * "user_role_updated", "seq": 6 }
				 */
				break;
			case ("user_updated"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				/*
				 * { "broadcast": { "user_id": "", "team_id": "", "channel_id": "",
				 * "omit_users": null }, "data": {"user": { "auth_service": "", "timezone": {
				 * "manualTimezone": "", "useAutomaticTimezone": "true", "automaticTimezone":
				 * "Europe/Rome" }, "roles": "system_admin system_user", "last_name":
				 * "Ambrosini", "locale": "it", "auth_data": "", "last_picture_update":
				 * 1618324151750, "update_at": 1618994616644, "nickname": "", "delete_at": 0,
				 * "id": "yogedfqb8td98pyc1b6iemxtje", "position": "primo_maggio", "create_at":
				 * 1618250883955, "first_name": "Andrea", "email":
				 * "andrea.ambrosini@rossonet.com", "username": "andrea.ambrosini" }}, "event":
				 * "user_updated", "seq": 35 }
				 * 
				 */
				break;
			case ("dialog_opened"):
				logger.debug("event " + message.getString("event") + " -> " + message.toString(2));
				break;
			}
		} else {
			logger.debug("received web socket message from " + rossonetChatServer + " without event tag");
			logger.debug("received web socket message " + message.toString(2));
		}
	}

	public MatterMostCallBack getCallBack() {
		return callBack;
	}

	public boolean isDirectChannel(String channelId) {
		return workedChannels.get(channelId).getType().equals(ChannelType.Direct);
	}

	@Override
	public void close() throws Exception {
		if (client != null) {
			client.close();
		}

	}

}
