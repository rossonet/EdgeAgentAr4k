/**
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    */
package org.ar4k.agent.console.chat.sshd;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.AbstractAr4kService;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.json.JSONObject;
import org.springframework.shell.Shell;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Gestore servizio per connessioni ssh.
 *
 */
public class SshdHomunculusService extends AbstractAr4kService {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(SshdHomunculusService.class.toString());

  // iniettata vedi set/get
  private SshdHomunculusConfig configuration = null;
  private SshServer server = null;

  @Override
  @PostConstruct
  public void postCostructor() {
    super.postCostructor();
  }

  @Override
  public synchronized void loop() {

  }

  @Override
  public SshdHomunculusConfig getConfiguration() {
    return configuration;
  }

  @Override
  public void setConfiguration(AbstractServiceConfig configuration) {
    super.setConfiguration(configuration);
    this.configuration = ((SshdHomunculusConfig) configuration);
  }

  @Override
  public void kill() {
    super.kill();
  }

  @Override
  public void init() {
    server = SshServer.setUpDefaultServer();
    server.setHost(configuration.broadcastAddress);
    server.setPort(configuration.port);
    server.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
    logger.warn("keys for sshd server generated");
    Ar4kAnimaShellFactory shellFactory = new Ar4kAnimaShellFactory(Anima.getApplicationContext().getBean(Anima.class),
        Anima.getApplicationContext().getBean(Shell.class));
    server.setShellFactory(shellFactory);
    try {
      server.start();
      logger.info("starting sshd server on " + getStatusString());
    } catch (IOException e) {
      logger.logException(e);
    }

  }

  @Override
  public void setConfiguration(ConfigSeed configuration) {
    this.configuration = (SshdHomunculusConfig) configuration;
  }

  @Override
  public String getStatusString() {
    return server != null ? (server.getHost() + ":" + server.getPort()) : "stopped";
  }

  @Override
  public JSONObject getStatusJson() {
    JSONObject o = new JSONObject();
    o.put("status", getStatusString());
    return o;
  }

  @Override
  public void close() throws IOException {
    stop();
    if (server != null)
      server.close();
  }

  @Override
  public void stop() {
    if (server != null)
      try {
        server.stop();
      } catch (IOException e) {
        logger.logException(e);
      }

  }

}
