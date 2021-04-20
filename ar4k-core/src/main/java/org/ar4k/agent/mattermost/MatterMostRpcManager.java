package org.ar4k.agent.mattermost;

import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.mattermost.model.Channel;
import org.ar4k.agent.mattermost.model.Post;
import org.ar4k.agent.mattermost.model.Team;
import org.ar4k.agent.mattermost.model.User;

public class MatterMostRpcManager implements MatterMostCallBack {

	private final RpcConversation rpc;

	public MatterMostRpcManager(RpcConversation rpc) {
		this.rpc = rpc;
	}

	public MatterMostCallBack getCallBack() {
		return this;
	}

	@Override
	public void connectionStarted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNewChannel(Channel channel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNewUser(User checkedUser) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNewTeam(Team team) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNewPost(Post post) {
		// TODO Auto-generated method stub

	}

}
