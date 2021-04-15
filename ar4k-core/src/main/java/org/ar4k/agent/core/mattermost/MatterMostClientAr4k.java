package org.ar4k.agent.core.mattermost;

import java.util.List;
import java.util.logging.Level;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.User;

public class MatterMostClientAr4k {

	private static final Level LOG_LEVEL = Level.FINE;

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(MatterMostClientAr4k.class.toString());

	// private static final String rossonetChatServer = "https://mm.rossonet.net";

	private final MattermostClient client;

	private final String rossonetChatServer;

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
	}

	public void reportStatusInLog() {
		logger.info(rossonetChatServer + " connected:" + reportStatus());
	}

	public boolean reportStatus() {
		return client.getMe().getRawResponse().getStatus() == 200;
	}

	public List<User> getUsersWithoutTeam() {
		return client.getUsersWithoutTeam().readEntity();
	}

	public String getMe() {
		return String.valueOf(client.getMe().readEntity().toString());
	}

}
