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
package org.ar4k.gw.studio.tunnels.socket;

import java.util.List;

import org.ar4k.agent.config.AbstractServiceConfig;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione astratta servizio con funzionalità di tunnel.
 */
public abstract class AbstractSocketFactoryConfig extends AbstractServiceConfig {

  private static final long serialVersionUID = 2256280745924059640L;

  @Override
  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public int getPriority() {
    return priority;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public List<String> getTags() {
    return tags;
  }
}
