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
package org.ar4k.agent.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.ar4k.agent.config.validator.ServiceStatusValidator;

import org.ar4k.agent.core.Ar4kService.ServiceStates;
import org.joda.time.Instant;

import com.beust.jcommander.Parameter;

/**
 * 
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 * 
 *         Configurazione astratta servizio Agente Ar4k.
 * 
 */

public abstract class ServiceConfig implements ConfigSeed {

  private static final long serialVersionUID = 4934166738722721336L;

  public Instant creationDate = new Instant();
  public Instant lastUpdate = new Instant();
  public UUID uniqueId = UUID.randomUUID();

  @Parameter(names = "--name", description = "service name", required = true)
  public String name;

  @Parameter(names = "--description", description = "service description")
  public String description;

  @Parameter(names = "--version", description = "service version")
  public String version;

  @Parameter(names = "--context", description = "service context")
  public String context = "main-context";

  @Parameter(names = "--groups", description = "service groups (multi selection)", variableArity = true)
  public Collection<String> groups = new ArrayList<String>();

  @Parameter(names = "--ports", description = "service tcp port reserved for this host (multi selection)", variableArity = true)
  public Collection<Integer> ports = new ArrayList<Integer>();

  @Parameter(names = "--tags", description = "service tags (multi selection)", variableArity = true)
  public Collection<String> tags;

  @Parameter(names = "--provides", description = "what service provides (multi selection)", variableArity = true)
  public Collection<String> provides;

  @Parameter(names = "--required", description = "what is required by the service to run. It is correlated to -provides (multi selection)", variableArity = true)
  public Collection<String> required;

  @Parameter(names = "--note", description = "service note text")
  public String note;

  @Parameter(names = "--comment", description = "service comment text")
  public String comment;

  // @Parameter(names = "--data", description = "additional data for this service
  // (multi selection)", variableArity = true)
  public Map<String, Object> data = new HashMap<String, Object>();

  @Parameter(names = "--clockRunnableClass", description = "clock for the runnable thread")
  public int clockRunnableClass = 1000;

  @Parameter(names = "--targetRunLevel", description = "the default runlevel for the service when the system start", validateWith = ServiceStatusValidator.class)
  public ServiceStates targetRunLevel = ServiceStates.RUNNING;

  @Parameter(names = "--pausable", description = "is the server pausable?", arity = 0)
  public boolean pausable = false;

  @Parameter(names = "--usableWithCron", description = "is the server usable in cron mode?", arity = 0)
  public boolean usableWithCron = false;

  @Parameter(names = "--otpAdminSeed", description = "seed for OTP authentication")
  public String otpAdminSeed = null;

  @Parameter(names = "--priority", description = "the priority of the service when the system start")
  public int priority = 200;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Instant getCreationDate() {
    return creationDate;
  }

  @Override
  public Instant getLastUpdateDate() {
    return lastUpdate;
  }

  @Override
  public UUID getUniqueId() {
    return uniqueId;
  }

}
