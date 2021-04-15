package org.ar4k.agent.core.mattermost;

import java.util.logging.Level;

import net.bis5.mattermost.client4.MattermostClient;

public class MatterMostClientAr4k {

	private final MattermostClient client;

	public MatterMostClientAr4k() {
		// client = new MattermostClient("YOUR-MATTERMOST-URL");
		client = MattermostClient.builder().url("YOUR-MATTERMOST-URL").logLevel(Level.INFO).ignoreUnknownProperties()
				.build();

		// Login by id + password
		client.login("loginId", "password");
		// Login by Personal Access Token
		client.setAccessToken("token");
	}

}
