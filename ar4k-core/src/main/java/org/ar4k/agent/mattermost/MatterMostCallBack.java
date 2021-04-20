package org.ar4k.agent.mattermost;

import org.ar4k.agent.mattermost.model.Channel;
import org.ar4k.agent.mattermost.model.Post;
import org.ar4k.agent.mattermost.model.Team;
import org.ar4k.agent.mattermost.model.User;

public interface MatterMostCallBack {

	void connectionStarted();

	void onNewChannel(Channel channel);

	void onNewUser(User checkedUser);

	void onNewTeam(Team team);

	void onNewPost(Post post);

}
