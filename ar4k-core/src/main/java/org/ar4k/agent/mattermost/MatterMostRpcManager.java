package org.ar4k.agent.mattermost;

import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.mattermost.model.Channel;
import org.ar4k.agent.mattermost.model.Post;
import org.ar4k.agent.mattermost.model.Team;
import org.ar4k.agent.mattermost.model.User;

public class MatterMostRpcManager implements MatterMostCallBack {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(MatterMostRpcManager.class.toString());

	private final RpcConversation rpc;
	private MatterMostClientAr4k mattermostClient = null;
	private boolean activeConnestion = false;
	private String myUserId = null;
	private String myNickname = null;

	public MatterMostRpcManager(RpcConversation rpc) {
		this.rpc = rpc;
	}

	public MatterMostCallBack getCallBack() {
		return this;
	}

	@Override
	public void connectionStarted() {
		this.activeConnestion = true;
	}

	@Override
	public void onNewChannel(Channel channel) {
		// per ora non usato
	}

	@Override
	public void onNewUser(User checkedUser) {
		// per ora non usato
	}

	@Override
	public void onNewTeam(Team team) {
		// per ora non usato
	}

	@Override
	public void onNewPost(Post post) {
		try {
			final String userId = post.getUserId();
			if (mattermostClient != null && !userId.equals(this.myUserId)) {
				final String channelId = post.getChannelId();
				final String message = post.getMessage();
				if (mattermostClient.isDirectChannel(channelId)) {
					final String reply = rpc.elaborateMessage(message);
					mattermostClient.sendPost(channelId, reply);
				} else {
					if (message.startsWith("@" + this.myNickname)) {
						final int count = this.myNickname.length()+2;
						final String cleanCommand = message.substring(count);
						final String reply = rpc.elaborateMessage(cleanCommand);
						mattermostClient.sendPost(channelId, reply);
					}
				}
			}
		} catch (Exception a) {
			logger.logException(a);
		}
	}

	public void setMattermostClient(MatterMostClientAr4k mattermostClient) {
		this.mattermostClient = mattermostClient;
		this.myUserId = mattermostClient.getMe().getId();
		this.myNickname = mattermostClient.getMe().getUsername();
	}

}
