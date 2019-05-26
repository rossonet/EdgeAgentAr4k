/*
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
package org.ar4k.agent.tunnels.socket;

import java.io.IOException;

import javax.net.SocketFactory;

import org.ar4k.agent.config.ConfigSeed;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Classe astratta base da implementare per la realizzazione di tunnel
 *         gestiti dall'agente.
 *
 */
public abstract class AbstractSocketFactoryComponent implements ISocketFactoryComponent {

  private String beanName = null;

  private AbstractSocketFactoryConfig configuration = null;

  protected SocketFactory socketFactory = null;

  @Override
  public AbstractSocketFactoryConfig getConfiguration() {
    return configuration;
  }

  @Override
  public void setConfiguration(ConfigSeed configuration) {
    this.configuration = ((AbstractSocketFactoryConfig) configuration);
  }

  @Override
  public SocketFactory getSocketFactory() {
    return socketFactory;
  }

  @Override
  public void setBeanName(String name) {
    beanName = name;
  }

  public String getBeanName() {
    return beanName;
  }

  @Override
  public void close() throws IOException {
    socketFactory = null;
  }
}
