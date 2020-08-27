package org.ar4k.agent.tunnels.sshd;

import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.session.ServerSession;
import org.ar4k.agent.core.Anima;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * Used to authenticate users based on a password.
 * 
 * @author andrea
 *
 */
public class AnimaPasswordAuthenticator implements PasswordAuthenticator {

	private final Anima anima;

	public AnimaPasswordAuthenticator(Anima anima) {
		this.anima = anima;
	}

	@Override
	public boolean authenticate(String username, String password, ServerSession session)
			throws PasswordChangeRequiredException, AsyncAuthException {
		final UsernamePasswordAuthenticationToken request = new UsernamePasswordAuthenticationToken(username, password);
		final Authentication result = anima.getAuthenticationManager().authenticate(request);
		if (result.isAuthenticated()) {
			return true;
		} else {
			return false;
		}
	}

}
