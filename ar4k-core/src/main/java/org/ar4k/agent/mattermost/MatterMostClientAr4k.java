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
import org.ar4k.agent.mattermost.model.Post;
import org.ar4k.agent.mattermost.model.PostList;
import org.ar4k.agent.mattermost.model.Team;
import org.ar4k.agent.mattermost.model.TeamList;
import org.ar4k.agent.mattermost.model.User;
import org.ar4k.agent.mattermost.model.UserList;


@ClientEndpoint
public class MatterMostClientAr4k implements MessageHandler {

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
	public static final int DELAY_CHECK_TEAMS = 5* 60 * 1000;
	public static final int DELAY_CHECK_CHANNELS = 2* 60 * 1000;
	public static final int DELAY_CLEAN_POSTS = 6* 60 * 1000;
	public static final int DELAY_CHECK_USERS = 7* 60 * 1000;

	private static final Level LOG_LEVEL = Level.FINE;

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(MatterMostClientAr4k.class.toString());

	private final MattermostClient client;

	private  MatterMostWebSocketHandler webSocketChannel =null;

	private final String rossonetChatServer;

	private Timer timerScheduler = new Timer();
	private final Map<String, Channel> workedChannels = new HashMap<>();

	private final Map<String, Post> workedMessages = new HashMap<>();

	private final Map<Long,String> dateForMessages = new HashMap<>();

	private final Map<String, Team> workedTeams = new HashMap<>();

	private boolean firstSynchronizeDone = false;

	private final Map<String, User> workedUsers = new HashMap<>();
	private long lastTeamsTime = 0;
	private long lastUsersTime= 0;
	private long lastChannelsTime = 0;
	private long lastPostsTime = 0;
	private long lastCleanPostsTime = 0;


	public MatterMostClientAr4k(String rossonetChatServer, String rossonetChatUserName, String rossonetChatPassword,
			String rossonetChatToken) {
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
			prepareNewConnection();
			//System.out.println("*********** getUsers " + client.getUsers().readEntity());
		}
		try {
			webSocketChannel = new MatterMostWebSocketHandler(new URI(rossonetChatServer.replace("http://", "ws://").replace("https://", "wss://")+API_V4_WEBSOCKET));
			webSocketChannel.startSession(client.getAuthToken());
			webSocketChannel.addMessageHandler(this);
		} catch (final Exception e) {
			webSocketChannel=null;
			logger.logException(e);
		}
		final ReadNewMessageTask readNewMessageTask = new ReadNewMessageTask();
		timerScheduler.schedule(readNewMessageTask, DELAY_CHECK_POSTS, DELAY_CHECK_POSTS);
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
			//final long fromTimeFake = new Date().getTime()-(24*60*60*1000);
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

	public void reportStatusInLog() {
		logger.info(rossonetChatServer + " connected:" + isConnected());
	}

	public Post sendPost(final String channelId, String message) {
		final Post post = new Post();
		post.setMessage(message);
		post.setChannelId(channelId);
		final Post postResult = client.createPost(post).readEntity();
		return postResult;
	}

	private void onNewChannel(Channel channel) {
		logger.info("new channel configured " + channel);
		// TODO Auto-generated method stub

	}

	private void onNewPost(Post post) {
		logger.info("new post received " + post);
		// TODO Auto-generated method stub

	}

	private void onNewTeam(Team team) {
		logger.info("new team configured " + team);
		// TODO Auto-generated method stub

	}

	private void refreshDataFromServer() {
		final long time = new Date().getTime();
		refreshTeams(time);
		refreshUsers(time);
		refreshChannels(time);
		refreshPosts(time);
		removeOldMessages(time);
	}

	private void removeOldMessages(long time) {
		if (lastCleanPostsTime == 0 || lastCleanPostsTime +  DELAY_CLEAN_POSTS < time) {
			final  Map<Long, String> toDelete  = new HashMap<>();
			for (final Entry<Long, String> message : dateForMessages.entrySet()) {
				if (message.getKey() +DELAY_CLEAN_POSTS< time) {
					toDelete.put(message.getKey(),message.getValue());
				}
			}
			if (!toDelete.isEmpty()) {
				for (final Entry<Long, String> messageToDelete : toDelete.entrySet()) {
					workedMessages.remove(messageToDelete.getValue());
					dateForMessages.remove(messageToDelete.getKey());
				}
			}
			lastCleanPostsTime=time;
		}
	}

	private void refreshTeams(final long time) {
		//teams
		if (lastTeamsTime == 0 || lastTeamsTime +  DELAY_CHECK_TEAMS < time) {
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
			lastTeamsTime=time;
		}
		System.out.println("teams -> "+workedTeams);
	}

	private void refreshUsers(final long time) {
		// users
		if (lastUsersTime == 0 || lastUsersTime +  DELAY_CHECK_USERS < time) {
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
			lastUsersTime=time;
		}
		System.out.println("users -> "+workedUsers);
	}

	private void refreshChannels(final long time) {
		if (lastChannelsTime == 0 || lastChannelsTime +  DELAY_CHECK_CHANNELS < time) {
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
			lastChannelsTime=time;
		}
		System.out.println("channels -> "+workedChannels);
	}

	private void refreshPosts(final long time) {
		if (lastPostsTime == 0 || lastPostsTime +  DELAY_CHECK_POSTS < time) {
			for (final Channel checkedChannel : workedChannels.values()) {
				final PostList listPostsSince = listPostsSince(checkedChannel.getId(),
						time - (2 * DELAY_CHECK_POSTS));
				if (listPostsSince != null) {
					for (final Post checkedPost : listPostsSince.getPosts().values()) {
						if (!workedMessages.containsKey(checkedPost.getId())) {
							if (firstSynchronizeDone) {
								onNewPost(checkedPost);
							}
							dateForMessages.put(time, checkedPost.getId());
							workedMessages.put(checkedPost.getId(), checkedPost);
						}
					}
				}
			}
			lastPostsTime=time;
		}
		System.out.println("posts -> "+workedMessages);
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
		// TODO Auto-generated method stub

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
	public void handleMessage(String message) {
		System.out.println("************************** "+message);

	}

}
