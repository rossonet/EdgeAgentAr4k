package org.ar4k.agent.core.mattermost;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.rossonet.mm.MattermostRestClient;
import org.rossonet.mm.client.model.Channel;
import org.rossonet.mm.client.model.Post;
import org.rossonet.mm.client.model.Team;
import org.rossonet.mm.client.model.User;


public class MatterMostClientAr4k {

	private class ReadNewMessageTask extends TimerTask {

		@Override
		public void run() {
			try {
				refreshDataFromServer();
			} catch (Exception ex) {
				logger.warn("during refresh on " + rossonetChatServer, ex);
			}
		}
	}

	public static final int DELAY_CHECK = 20 * 1000;

	private static final Level LOG_LEVEL = Level.INFO;

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(MatterMostClientAr4k.class.toString());

	private final MattermostRestClient client;

	private final String rossonetChatServer;

	private Timer timerScheduler = new Timer();
	private final Map<String, Channel> workedChannels = new HashMap<>();

	private final Map<String, Post> workedMessages = new HashMap<>();

	private final Map<String, Team> workedTeams = new HashMap<>();

	private boolean firstSynchronizeDone = false;

	private final Map<String, User> workedUsers = new HashMap<>();

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
			System.out.println("*********** getUsers " + client.getUsers().readEntity());
		}
		ReadNewMessageTask readNewMessageTask = new ReadNewMessageTask();
		timerScheduler.schedule(readNewMessageTask, DELAY_CHECK, DELAY_CHECK);
	}

	private void prepareNewConnection() {
		workedTeams.clear();
		workedChannels.clear();
		workedMessages.clear();
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
		} catch (Exception a) {
			logger.trace("in team list ", a);
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
		} catch (Exception a) {
			logger.trace("in user list ", a);
			return null;
		}
	}

	public boolean isConnected() {
		return client.getMe().getRawResponse().getStatus() == 200;
	}

	private PostList listPostsSince(final String channelId, final long fromTime) {
		try {
			final ApiResponse<PostList> postsSince = client.getPostsSince(channelId, fromTime);
			if (!postsSince.hasError() && postsSince.checkStatusOk().readEntity()) {
				return postsSince.readEntity();
			} else {
				return null;
			}
		} catch (Exception a) {
			logger.trace("in post list ", a);
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
		Post post = new Post();
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
		final TeamList allTeams = getTeamsFromRemote();
		if (allTeams != null)
			for (Team checkedTeam : allTeams) {
				if (!workedTeams.containsKey(checkedTeam.getId())) {
					if (firstSynchronizeDone) {
						onNewTeam(checkedTeam);
					}
					workedTeams.put(checkedTeam.getId(), checkedTeam);
				}
			}
		final UserList usersFromRemote = getUsersFromRemote();
		if (usersFromRemote != null)
			for (User checkedUser : usersFromRemote) {
				if (!workedUsers.containsKey(checkedUser.getId())) {
					if (firstSynchronizeDone) {
						onNewUser(checkedUser);
					}
					workedUsers.put(checkedUser.getId(), checkedUser);
				}
				if (allTeams != null)
					for (Team teamLooking : allTeams) {
						final ChannelList usersChannels = getUsersChannels(teamLooking, checkedUser);
						if (usersChannels != null)
							for (Channel checkedChannel : usersChannels) {
								if (!workedChannels.containsKey(checkedChannel.getId())) {
									if (firstSynchronizeDone) {
										onNewChannel(checkedChannel);
									}
									workedChannels.put(checkedChannel.getId(), checkedChannel);
								}
								final PostList listPostsSince = listPostsSince(checkedChannel.getId(),
										new Date().getTime() - (2 * DELAY_CHECK));
								if (listPostsSince != null)
									for (Post checkedPost : listPostsSince.getPosts().values()) {
										if (!workedMessages.containsKey(checkedPost.getId())) {
											if (firstSynchronizeDone) {
												onNewPost(checkedPost);
											}
											workedMessages.put(checkedPost.getId(), checkedPost);
										}
									}
							}
					}
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
		} catch (Exception a) {
			logger.trace("in channel list ", a);
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

}
