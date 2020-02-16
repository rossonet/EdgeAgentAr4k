package org.ar4k.agent.tunnels.sshd;

import java.nio.file.Path;
import java.security.PublicKey;

import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.config.keys.AuthorizedKeysAuthenticator;
import org.apache.sshd.server.session.ServerSession;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;

public class Ar4kPublickeyAuthenticator extends AuthorizedKeysAuthenticator {

  public Ar4kPublickeyAuthenticator(Path file) {
    super(file);
  }

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(Ar4kPublickeyAuthenticator.class.toString());

  @Override
  public boolean authenticate(String username, PublicKey key, ServerSession session) throws AsyncAuthException {
    if (key != null && key.getFormat().equals("X.509")) {
      logger.info("ssh auth key for sshd access:\n" + key);
    }
    return super.authenticate(username, key, session);
  }

}
