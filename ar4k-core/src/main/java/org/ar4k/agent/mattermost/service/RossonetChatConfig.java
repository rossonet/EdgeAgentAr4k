package org.ar4k.agent.mattermost.service;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.interfaces.EdgeComponent;

import com.beust.jcommander.Parameter;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione servizio connessione MatterMost.
 *
 */
public class RossonetChatConfig extends AbstractServiceConfig {

	@Parameter(names = "--mm-server", description = "the mm chat server")
	public String mmServer = "https://mm.rossonet.net";
	@Parameter(names = "--username", description = "username for connection")
	public String username = null;
	@Parameter(names = "--password", description = "password for connection")
	public String password = null;
	@Parameter(names = "--token", description = "authorized keys for bot")
	public String token = null;

	private static final long serialVersionUID = 6301077946480730173L;

	@Override
	public EdgeComponent instantiate() {
		final RossonetChatService ss = new RossonetChatService();
		ss.setConfiguration(this);
		return ss;
	}

	@Override
	public int getPriority() {
		return 18;
	}

	@Override
	public boolean isSpringBean() {
		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RossonetChatConfig [mmServer=");
		builder.append(mmServer);
		builder.append(", username=");
		builder.append(username);
		builder.append("]");
		return builder.toString();
	}

}
