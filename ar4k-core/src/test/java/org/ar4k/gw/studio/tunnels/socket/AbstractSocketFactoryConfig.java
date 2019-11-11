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

import java.util.Collection;
import java.util.UUID;

import org.ar4k.agent.config.PotConfig;
import org.joda.time.Instant;

import com.beust.jcommander.Parameter;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione astratta servizio con funzionalità di tunnel.
 */
public abstract class AbstractSocketFactoryConfig implements PotConfig {

  private static final long serialVersionUID = 2256280745924059640L;

  private Instant creationDate = new Instant();
  private Instant lastUpdate = new Instant();
  private String uniqueId = UUID.randomUUID().toString();

  @Parameter(names = "--name", description = "tunnel name", required = true)
  public String name;

  @Parameter(names = "--description", description = "tunnel description", required = false)
  public String description;

  @Parameter(names = "--tags", description = "tunnel tags (multi selection)", variableArity = true, required = false)
  public Collection<String> tags;

  @Parameter(names = "--priority", description = "priority for the tunnel low values are before, high values are after", required = true)
  public int priority = 0;

  @Parameter(names = "--note", description = "note related to this tunnel", required = false)
  public String note = null;

  @Override
  public Instant getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Instant creationDate) {
    this.creationDate = creationDate;
  }

  @Override
  public Instant getLastUpdateDate() {
    return lastUpdate;
  }

  public void setLastUpdate(Instant lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

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
  public Collection<String> getTags() {
    return tags;
  }
}
