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
package org.ar4k.agent.console.chat.irc;

import javax.annotation.PostConstruct;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.AbstractAr4kService;

import com.google.gson.JsonElement;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 * 
 *         Gestore servizio per connessioni irc.
 * 
 */
public class IrcHomunculusService extends AbstractAr4kService {

  // iniettata vedi set/get
  private IrcHomunculusConfig configuration = null;

  @Override
  @PostConstruct
  public void postCostructor() {
    super.postCostructor();
  }

  @Override
  public synchronized void loop() {

  }

  @Override
  public IrcHomunculusConfig getConfiguration() {
    return configuration;
  }

  @Override
  public void setConfiguration(AbstractServiceConfig configuration) {
    super.setConfiguration(configuration);
    this.configuration = ((IrcHomunculusConfig) configuration);
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
    this.configuration = (IrcHomunculusConfig) configuration;
  }

  @Override
  public String getStatusString() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JsonElement getStatusJson() {
    // TODO Auto-generated method stub
    return null;
  }

}
