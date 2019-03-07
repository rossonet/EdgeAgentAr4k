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
package org.ar4k.agent.xmpp;

import javax.annotation.PostConstruct;

import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.Ar4kService;
import org.json.JSONObject;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 * 
 *         Gestore servizio per connessioni socket.
 * 
 */
public class JabberHomunculusService extends Ar4kService {

  // iniettata vedi set/get
  private JabberHomunculusConfig configuration = null;

  @Override
  @PostConstruct
  public void postCostructor() {
    super.postCostructor();
  }

  @Override
  public synchronized void loop() {

  }

  @Override
  public JabberHomunculusConfig getConfiguration() {
    return configuration;
  }

  @Override
  public void setConfiguration(ServiceConfig configuration) {
    super.setConfiguration(configuration);
    this.configuration = ((JabberHomunculusConfig) configuration);
  }

  @Override
  public void kill() {
    super.kill();
  }

  @Override
  protected void finalize() {
  }

  @Override
  public void init() {

  }

  @Override
  public void setConfiguration(ConfigSeed configuration) {
    this.configuration = (JabberHomunculusConfig) configuration;
  }

  @Override
  public String getStatusString() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JSONObject getStatusJson() {
    // TODO Auto-generated method stub
    return null;
  }

}
