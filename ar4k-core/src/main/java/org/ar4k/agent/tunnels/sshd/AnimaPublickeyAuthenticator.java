package org.ar4k.agent.tunnels.sshd;

import java.nio.file.Path;
import java.security.PublicKey;

import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.config.keys.AuthorizedKeysAuthenticator;
import org.apache.sshd.server.session.ServerSession;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

/**
 * Uses the authorized keys file to implement PublickeyAuthenticator while
 * automatically re-loading the keys if the file has changed when a new
 * authentication request is received. Note: by default, the only validation of
 * the username is that it is not null/empty - see isValidUsername(String,
 * ServerSession)
 * 
 * @author andrea
 *
 */
public class AnimaPublickeyAuthenticator extends AuthorizedKeysAuthenticator {

	public AnimaPublickeyAuthenticator(Path file) {
		super(file);
	}

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(AnimaPublickeyAuthenticator.class.toString());

	@Override
	public boolean authenticate(String username, PublicKey key, ServerSession session) throws AsyncAuthException {
		if (key != null && key.getFormat().equals("X.509")) {
			logger.info("ssh auth key for sshd access:\n" + key);
		}
		return super.authenticate(username, key, session);
	}

}
