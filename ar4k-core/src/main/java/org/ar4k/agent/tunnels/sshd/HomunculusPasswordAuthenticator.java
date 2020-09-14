package org.ar4k.agent.tunnels.sshd;

import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.session.ServerSession;
import org.ar4k.agent.core.Homunculus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * Used to authenticate users based on a password.
 * 
 * @author andrea
 *
 */
public class HomunculusPasswordAuthenticator implements PasswordAuthenticator {

	private final Homunculus homunculus;

	public HomunculusPasswordAuthenticator(Homunculus homunculus) {
		this.homunculus = homunculus;
	}

	@Override
	public boolean authenticate(String username, String password, ServerSession session)
			throws PasswordChangeRequiredException, AsyncAuthException {
		final UsernamePasswordAuthenticationToken request = new UsernamePasswordAuthenticationToken(username, password);
		final Authentication result = homunculus.getAuthenticationManager().authenticate(request);
		if (result.isAuthenticated()) {
			return true;
		} else {
			return false;
		}
	}

}
