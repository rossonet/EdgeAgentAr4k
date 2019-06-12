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
package org.ar4k.agent.tunnels.ssh.client;

import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.AbstractAr4kService;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 * 
 *         Servizio tunnel SSH.
 *
 */
public abstract class AbstractSshTunnel extends AbstractAr4kService {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(AbstractSshTunnel.class.toString());

  private AbstractSshConfig configuration = null;

  private JSch jsch = null;
  private Session session = null;

  protected Session connect() {
    try {
      jsch = new JSch();
      if (configuration.authkey != null)
        jsch.addIdentity(configuration.authkey);
      session = jsch.getSession(configuration.username, configuration.host, configuration.port);
      SSHUserInfo ui = new SSHUserInfo();
      if (configuration.password != null)
        ui.setPassword(configuration.password);
      session.setUserInfo(ui);
      session.connect();
    } catch (Exception e) {
      logger.logException(e);
    }
    return session;
  }

  @Override
  public AbstractSshConfig getConfiguration() {
    return configuration;
  }

  @Override
  public void setConfiguration(ConfigSeed configuration) {
    this.configuration = ((AbstractSshConfig) configuration);
  }

  @Override
  public void close() {
    if (session != null)
      session.disconnect();
    session = null;
    jsch = null;
  }

  @Override
  public String getStatusString() {
    return "ssh//" + session.getHost() + ":" + session.getPort() + " ["
        + (session.isConnected() ? "connected" : "disconnected") + "]";
  }

  @Override
  public JsonElement getStatusJson() {
    Gson gson = new GsonBuilder().create();
    return gson.toJsonTree(session);
  }

}
