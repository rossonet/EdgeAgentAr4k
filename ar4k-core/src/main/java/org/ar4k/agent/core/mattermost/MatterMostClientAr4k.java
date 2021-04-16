package org.ar4k.agent.core.mattermost;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.rossonet.mm.Configuration;
import org.rossonet.mm.MattermostApiException;
import org.rossonet.mm.MattermostRestClient;
import org.rossonet.mm.client.model.Body56;
import org.rossonet.mm.client.model.Channel;
import org.rossonet.mm.client.model.Post;
import org.rossonet.mm.client.model.PostList;
import org.rossonet.mm.client.model.SystemStatusResponse;
import org.rossonet.mm.client.model.Team;
import org.rossonet.mm.client.model.User;
import org.rossonet.mm.mattermost.ChannelsApi;
import org.rossonet.mm.mattermost.PostsApi;
import org.rossonet.mm.mattermost.SystemApi;
import org.rossonet.mm.mattermost.TeamsApi;
import org.rossonet.mm.mattermost.UsersApi;

public class MatterMostClientAr4k {

	private static final String ME = "me";

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
		client = Configuration.getDefaultApiClient();
		if (rossonetChatUserName != null && !rossonetChatUserName.isEmpty() && rossonetChatPassword != null
				&& !rossonetChatPassword.isEmpty()) {
			client.setUsername(rossonetChatUserName);
			client.setPassword(rossonetChatPassword);
		} else if (rossonetChatToken != null && !rossonetChatToken.isEmpty()) {
			client.setAccessToken(rossonetChatToken);
		} else {
			logger.error("no valid account or token to connect to chat server " + rossonetChatServer);
		}
		try {
			if (isConnected()) {
				prepareNewConnection();
			}
		} catch (final MattermostApiException e) {
			logger.logException(e);
		}
		final ReadNewMessageTask readNewMessageTask = new ReadNewMessageTask();
		timerScheduler.schedule(readNewMessageTask, DELAY_CHECK, DELAY_CHECK);
	}

	private void prepareNewConnection() {
		workedTeams.clear();
		workedChannels.clear();
		workedMessages.clear();
		try {
			refreshDataFromServer();
		} catch (final MattermostApiException e) {
			logger.logException(e);
		}
		firstSynchronizeDone = true;
	}

	public User getMe() throws MattermostApiException {
		final UsersApi channel = new UsersApi();
		return channel.usersUserIdGet(ME);
	}

	public Post getPost(Post postQuery) throws MattermostApiException {
		final PostsApi channel = new PostsApi();
		return channel.postsPostIdGet(postQuery.getId());
	}

	private List<Team> getTeamsFromRemote() throws MattermostApiException {
		final TeamsApi channel = new TeamsApi();
		return channel.teamsGet(0, 100, true);
	}

	private List<User> getUsersFromRemote() throws MattermostApiException {
		final UsersApi channel = new UsersApi();
		final Integer page = 0; // Integer | The page to select.
		final Integer perPage = 100; // Integer | The number of users per page. There is a maximum limit of 200 users
		// per page.
		final String inTeam = null; // String | The ID of the team to get users for.
		final String notInTeam = null; // String | The ID of the team to exclude users for. Must not be used with
		// \"in_team\" query parameter.
		final String inChannel = null; // String | The ID of the channel to get users for.
		final String notInChannel = null; // String | The ID of the channel to exclude users for. Must be used with
		// \"in_channel\" query parameter.
		final String inGroup = null; // String | The ID of the group to get users for. Must have `manage_system`
		// permission.
		final Boolean groupConstrained = false; // Boolean | When used with `not_in_channel` or `not_in_team`, returns only
		// the users that are allowed to join the channel or team based on its group
		// constrains.
		final Boolean withoutTeam = true; // Boolean | Whether or not to list users that are not on any team. This option
		// takes precendence over `in_team`, `in_channel`, and `not_in_channel`.
		final Boolean active = true; // Boolean | Whether or not to list only users that are active. This option
		// cannot be used along with the `inactive` option.
		final Boolean inactive = false; // Boolean | Whether or not to list only users that are deactivated. This option
		// cannot be used along with the `active` option.
		final String role = null; // String | Returns users that have this role.
		final String sort = null; // String | Sort is only available in conjunction with certain options below.
		// The paging parameter is also always available. ##### `in_team` Can be \"\",
		// \"last_activity_at\" or \"create_at\". When left blank, sorting is done by
		// username. __Minimum server version__: 4.0 ##### `in_channel` Can be \"\",
		// \"status\". When left blank, sorting is done by username. `status` will sort
		// by User's current status (Online, Away, DND, Offline), then by Username.
		// __Minimum server version__: 4.7
		final String roles = null; // String | Comma separated string used to filter users based on any of the
		// specified system roles Example: `?roles=system_admin,system_user` will return
		// users that are either system admins or system users __Minimum server
		// version__: 5.26
		final String channelRoles = "channelRoles_example"; // String | Comma separated string used to filter users based on
		// any of the specified channel roles, can only be used in
		// conjunction with `in_channel` Example:
		// `?in_channel=4eb6axxw7fg3je5iyasnfudc5y&channel_roles=channel_user`
		// will return users that are only channel users and not admins
		// or guests __Minimum server version__: 5.26
		final String teamRoles = null; // String | Comma separated string used to filter users based on any of the
		// specified team roles, can only be used in conjunction with `in_team` Example:
		// `?in_team=4eb6axxw7fg3je5iyasnfudc5y&team_roles=team_user` will return users
		// that are only team users and not admins or guests __Minimum server version__:
		// 5.26
		return channel.usersGet(page, perPage, inTeam, notInTeam, inChannel, notInChannel, inGroup, groupConstrained,
				withoutTeam, active, inactive, role, sort, roles, channelRoles, teamRoles);
	}

	public boolean isConnected() throws MattermostApiException {
		return pingServer().equals("OK");
	}

	private PostList listPostsSince(final String channelId, final long fromTime) throws MattermostApiException {
		final PostsApi apiInstance = new PostsApi();
		final Integer page = 0; // Integer | The page to select
		final Integer perPage = 100; // Integer | The number of posts per page
		final Integer since = (int) (fromTime); // Integer | Provide a non-zero value in Unix time milliseconds to select posts modified after that time
		final String before = "before_example"; // String | A post id to select the posts that came before this one
		final String after = "after_example"; // String | A post id to select the posts that came after this one
		return apiInstance.channelsChannelIdPostsGet(channelId, page, perPage, since, before, after);
	}

	public SystemStatusResponse pingServer() throws MattermostApiException {
		final SystemApi apiInstance = new SystemApi();
		final Boolean getServerStatus = true; // Boolean | Check the status of the database and file storage as well
		return apiInstance.systemPingGet(getServerStatus);
	}

	public void reportStatusInLog() throws MattermostApiException {
		logger.info(rossonetChatServer + " connected:" + isConnected());
	}

	public boolean sendPost(final String channelId, String message) {
		final PostsApi apiInstance = new PostsApi();
		final Body56 post = new Body56(); // Body56 | Post object to create
		post.setMessage(message);
		post.setChannelId(channelId);
		final Boolean setOnline = true; // Boolean | Whether to set the user status as online or not.
		try {
			apiInstance.postsPost(post, setOnline);
			return true;
		} catch (final MattermostApiException e) {
			logger.logException(e);
			return false;
		}

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

	private void refreshDataFromServer() throws MattermostApiException {
		final List<Team> allTeams = getTeamsFromRemote();
		if (allTeams != null)
			for (final Team checkedTeam : allTeams) {
				if (!workedTeams.containsKey(checkedTeam.getId())) {
					if (firstSynchronizeDone) {
						onNewTeam(checkedTeam);
					}
					workedTeams.put(checkedTeam.getId(), checkedTeam);
				}
			}
		final List<User> usersFromRemote = getUsersFromRemote();
		if (usersFromRemote != null)
			for (final User checkedUser : usersFromRemote) {
				if (!workedUsers.containsKey(checkedUser.getId())) {
					if (firstSynchronizeDone) {
						onNewUser(checkedUser);
					}
					workedUsers.put(checkedUser.getId(), checkedUser);
				}
				if (allTeams != null)
					for (final Team teamLooking : allTeams) {
						final List<Channel> usersChannels = getUsersChannels(teamLooking, checkedUser);
						if (usersChannels != null)
							for (final Channel checkedChannel : usersChannels) {
								if (!workedChannels.containsKey(checkedChannel.getId())) {
									if (firstSynchronizeDone) {
										onNewChannel(checkedChannel);
									}
									workedChannels.put(checkedChannel.getId(), checkedChannel);
								}
								final PostList listPostsSince = listPostsSince(checkedChannel.getId(),
										new Date().getTime() - (2 * DELAY_CHECK));
								if (listPostsSince != null)
									for (final Post checkedPost : listPostsSince.getPosts().values()) {
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

	private List<Channel> getUsersChannels(Team team, User user) throws MattermostApiException {
		final ChannelsApi apiInstance = new ChannelsApi();
		final String notAssociatedToGroup = null; // String | Group GUID
		final Integer page = 0; // Integer |
		final Integer perPage = 100; // Integer |
		final Boolean excludeDefaultChannels = false; // Boolean | Whether to exclude default channels (ex Town Square, Off-Topic) from the results.
		return apiInstance.channelsGet(notAssociatedToGroup, page, perPage, excludeDefaultChannels);
	}

	private void onNewUser(User checkedUser) {
		// TODO Auto-generated method stub

	}

	public Map<String, Channel> getChannels() {
		try {
			refreshDataFromServer();
		} catch (final MattermostApiException e) {
			logger.logException(e);
		}
		return workedChannels;
	}

	public Map<String, Post> getPosts() {
		try {
			refreshDataFromServer();
		} catch (final MattermostApiException e) {
			logger.logException(e);
		}
		return workedMessages;
	}

	public Map<String, Team> getTeams() {
		try {
			refreshDataFromServer();
		} catch (final MattermostApiException e) {
			logger.logException(e);
		}
		return workedTeams;
	}

	public Map<String, User> getUsers() {
		try {
			refreshDataFromServer();
		} catch (final MattermostApiException e) {
			logger.logException(e);
		}
		return workedUsers;
	}

}
