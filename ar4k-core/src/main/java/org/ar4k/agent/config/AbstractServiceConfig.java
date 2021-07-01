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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.ar4k.agent.config.validator.ServiceStatusValidator;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.interfaces.EdgeComponent.ServiceStatus;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.joda.time.Instant;

import com.beust.jcommander.Parameter;

/**
 *
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione astratta servizio con parametri generici
 *
 * @see org.ar4k.agent.config.EdgeConfig
 * @see org.ar4k.agent.core.services.AbstractEdgeService
 * @see org.ar4k.agent.core.interfaces.ServiceConfig
 *
 */

public abstract class AbstractServiceConfig implements ServiceConfig {

	private static final long serialVersionUID = 4934166738722721736L;

	public transient Homunculus homunculus;

	public long creationDate = new Instant().getMillis();
	public long lastUpdate = new Instant().getMillis();
	public String uniqueId = UUID.randomUUID().toString();

	@Parameter(names = "--name", description = "service name", required = true)
	public String name;

	@Parameter(names = "--description", description = "service description", required = false)
	public String description = null;

	@Parameter(names = "--version", description = "service version", required = false)
	public String version = null;

	/**
	 * per installazioni multi contesto.
	 */
	@Parameter(names = "--context", description = "service context", required = false)
	public String context = "main-context";

	@Parameter(names = "--groups", description = "service groups (multi selection)", variableArity = true, required = false)
	public List<String> groups = new ArrayList<>();

	/**
	 * eventuali porte riservate per il servizio
	 */
	@Parameter(names = "--ports", description = "service tcp port reserved for this host (multi selection)", variableArity = true, required = false)
	public List<Integer> ports = new ArrayList<>();

	@Parameter(names = "--tags", description = "service tags (multi selection)", variableArity = true, required = false)
	public List<String> tags = new ArrayList<>();

	/**
	 * per dipendenze tra i servizi
	 */
	@Parameter(names = "--provides", description = "what service provides (multi selection)", variableArity = true, required = false)
	public List<String> provides = new ArrayList<>();

	/**
	 * per dipendenze tra i servizi
	 */
	@Parameter(names = "--required", description = "what is required by the service to run. It is correlated to -provides (multi selection)", variableArity = true, required = false)
	public List<String> required = new ArrayList<>();

	@Parameter(names = "--note", description = "service note text", required = false)
	public String note = null;

	@Parameter(names = "--comment", description = "service comment text", required = false)
	public String comment = null;

	/**
	 * per servizi gestiti esternamente alla JVM impostare true
	 */
	@Parameter(names = "--remote", description = "true if the service is managed outside the JVM", required = false)
	public boolean remote = false;

	/**
	 * dati addizionali per la configurazione del servizio in mappa
	 */
	@Parameter(names = "--serviceData", description = "additional data for this service(multi selection)", arity = 0)
	public Map<String, Object> data = new HashMap<>();

	/**
	 * intervallo esecuzione watchdog su servizio
	 */
	@Parameter(names = "--clockRunnableWatchDog", description = "interval for watchdog runnable thread")
	public int watchDogInterval = 5000;

	@Parameter(names = "--startOnInit", description = "start service on init agent?")
	public boolean startOnInit = true;

	public boolean startOnInit() {
		return startOnInit;
	}

	/**
	 * timeout per il check del servizio regolare (watchdog)
	 */
	@Parameter(names = "--watchDogTimeout", description = "timeout for the watchdog task")
	public int watchDogTimeout = 120000;

	/**
	 * numero massimo di verifiche prima di riavviare in automatico il servizio. Se
	 * 0 non riavvia mai. (watchdog)
	 */
	@Parameter(names = "--maxRestartRetries", description = "max watchdog retries before the fault. 0 = no limits")
	public int maxRestartRetries = 0;

	@Parameter(names = "--targetRunLevel", description = "the default runlevel for the service when the system start", validateWith = ServiceStatusValidator.class)
	public ServiceStatus targetRunLevel = ServiceStatus.RUNNING;

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
	public String getDescription() {
		return description;
	}

	@Override
	public List<String> getTags() {
		return tags;
	}

	@Override
	public long getCreationDate() {
		return creationDate;
	}

	@Override
	public long getLastUpdate() {
		return lastUpdate;
	}

	@Override
	public String getUniqueId() {
		return uniqueId;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public boolean isSpringBean() {
		return false;
	}

	@Override
	public int getWatchDogInterval() {
		return watchDogInterval;
	}

	@Override
	public int getMaxRestartRetries() {
		return maxRestartRetries;
	}

	@Override
	public int getWatchDogTimeout() {
		return watchDogTimeout;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	public List<Integer> getPorts() {
		return ports;
	}

	public void setPorts(List<Integer> ports) {
		this.ports = ports;
	}

	@Override
	public List<String> getProvides() {
		if (provides != null && !provides.contains(getClass().getCanonicalName())) {
			provides.add(getClass().getCanonicalName());
			return provides;
		} else {
			List<String> p = new ArrayList<>();
			p.add(getClass().getCanonicalName());
			return p;
		}
	}

	public void setProvides(List<String> provides) {
		this.provides = provides;
	}

	@Override
	public List<String> getRequired() {
		return required;
	}

	public void setRequired(List<String> required) {
		this.required = required;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isRemote() {
		return remote;
	}

	public void setRemote(boolean remote) {
		this.remote = remote;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public int getClockRunnableClass() {
		return watchDogInterval;
	}

	public void setClockRunnableClass(int clockRunnableClass) {
		this.watchDogInterval = clockRunnableClass;
	}

	public int getTimeoutWatchDog() {
		return watchDogTimeout;
	}

	public void setTimeoutWatchDog(int timeoutWatchDog) {
		this.watchDogTimeout = timeoutWatchDog;
	}

	public int getWatchDogRetries() {
		return maxRestartRetries;
	}

	public void setWatchDogRetries(int watchDogRetries) {
		this.maxRestartRetries = watchDogRetries;
	}

	public ServiceStatus getTargetRunLevel() {
		return targetRunLevel;
	}

	public void setTargetRunLevel(ServiceStatus targetRunLevel) {
		this.targetRunLevel = targetRunLevel;
	}

	public boolean isPausable() {
		return pausable;
	}

	public void setPausable(boolean pausable) {
		this.pausable = pausable;
	}

	public boolean isUsableWithCron() {
		return usableWithCron;
	}

	public void setUsableWithCron(boolean usableWithCron) {
		this.usableWithCron = usableWithCron;
	}

	public String getOtpAdminSeed() {
		return otpAdminSeed;
	}

	public void setOtpAdminSeed(String otpAdminSeed) {
		this.otpAdminSeed = otpAdminSeed;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AbstractServiceConfig [");
		if (creationDate != 0) {
			builder.append("creationDate=");
			builder.append(creationDate);
			builder.append(", ");
		}
		if (lastUpdate != 0) {
			builder.append("lastUpdate=");
			builder.append(lastUpdate);
			builder.append(", ");
		}
		if (uniqueId != null) {
			builder.append("uniqueId=");
			builder.append(uniqueId);
			builder.append(", ");
		}
		if (name != null) {
			builder.append("name=");
			builder.append(name);
			builder.append(", ");
		}
		if (description != null) {
			builder.append("description=");
			builder.append(description);
			builder.append(", ");
		}
		if (version != null) {
			builder.append("version=");
			builder.append(version);
			builder.append(", ");
		}
		if (context != null) {
			builder.append("context=");
			builder.append(context);
			builder.append(", ");
		}
		if (groups != null) {
			builder.append("groups=");
			builder.append(groups);
			builder.append(", ");
		}
		if (ports != null) {
			builder.append("ports=");
			builder.append(ports);
			builder.append(", ");
		}
		if (tags != null) {
			builder.append("tags=");
			builder.append(tags);
			builder.append(", ");
		}
		if (provides != null) {
			builder.append("provides=");
			builder.append(provides);
			builder.append(", ");
		}
		if (required != null) {
			builder.append("required=");
			builder.append(required);
			builder.append(", ");
		}
		if (note != null) {
			builder.append("note=");
			builder.append(note);
			builder.append(", ");
		}
		if (comment != null) {
			builder.append("comment=");
			builder.append(comment);
			builder.append(", ");
		}
		builder.append("remote=");
		builder.append(remote);
		builder.append(", ");
		if (data != null) {
			builder.append("data=");
			builder.append(data);
			builder.append(", ");
		}
		builder.append("clockRunnableClass=");
		builder.append(watchDogInterval);
		builder.append(", startOnInit=");
		builder.append(startOnInit);
		builder.append(", watchDogTimeout=");
		builder.append(watchDogTimeout);
		builder.append(", maxRestartRetries=");
		builder.append(maxRestartRetries);
		builder.append(", ");
		if (targetRunLevel != null) {
			builder.append("targetRunLevel=");
			builder.append(targetRunLevel);
			builder.append(", ");
		}
		builder.append("pausable=");
		builder.append(pausable);
		builder.append(", usableWithCron=");
		builder.append(usableWithCron);
		builder.append(", priority=");
		builder.append(priority);
		builder.append("]");
		return builder.toString();
	}

}
