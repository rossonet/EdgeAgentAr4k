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
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.core.interfaces.EdgeComponent.ServiceStatus;
import org.joda.time.Instant;

import com.beust.jcommander.Parameter;

/**
 *
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione astratta servizio con parametri generici
 *
 * @see org.ar4k.agent.config.EdgeConfig
 * @see org.ar4k.agent.core.AbstractEdgeService
 * @see org.ar4k.agent.core.interfaces.ServiceConfig
 *
 */

public abstract class AbstractServiceConfig implements ServiceConfig {

	private static final long serialVersionUID = 4934166738722721736L;

	public transient Homunculus homunculus;

	private Instant creationDate = new Instant();
	private Instant lastUpdate = new Instant();
	private String uniqueId = UUID.randomUUID().toString();

	@Parameter(names = "--name", description = "service name", required = true)
	public String name;

	@Parameter(names = "--description", description = "service description", required = false)
	public String description;

	@Parameter(names = "--version", description = "service version", required = false)
	public String version;

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
	public List<String> tags;

	/**
	 * per dipendenze tra i servizi
	 */
	@Parameter(names = "--provides", description = "what service provides (multi selection)", variableArity = true, required = false)
	public List<String> provides;

	/**
	 * per dipendenze tra i servizi
	 */
	@Parameter(names = "--required", description = "what is required by the service to run. It is correlated to -provides (multi selection)", variableArity = true, required = false)
	public List<String> required;

	@Parameter(names = "--note", description = "service note text", required = false)
	public String note;

	@Parameter(names = "--comment", description = "service comment text", required = false)
	public String comment;

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
	public int clockRunnableClass = 5000;

	/**
	 * timeout per il check del servizio regolare (watchdog)
	 */
	@Parameter(names = "--timeoutWatchDog", description = "timeout for the watchdog task")
	public int timeoutWatchDog = 120000;

	/**
	 * numero massimo di verifiche prima di riavviare in automatico il servizio. Se
	 * 0 non riavvia mai. (watchdog)
	 */
	@Parameter(names = "--watchDogRetries", description = "max watchdog retries before the fault. 0 = no limits")
	public int watchDogRetries = 0;

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
	public Instant getCreationDate() {
		return creationDate;
	}

	@Override
	public Instant getLastUpdateDate() {
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
		return clockRunnableClass;
	}

	@Override
	public int getMaxRestartRetries() {
		return watchDogRetries;
	}

	@Override
	public int getWatchDogTimeout() {
		return timeoutWatchDog;
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

	public List<String> getProvides() {
		return provides;
	}

	public void setProvides(List<String> provides) {
		this.provides = provides;
	}

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
		return clockRunnableClass;
	}

	public void setClockRunnableClass(int clockRunnableClass) {
		this.clockRunnableClass = clockRunnableClass;
	}

	public int getTimeoutWatchDog() {
		return timeoutWatchDog;
	}

	public void setTimeoutWatchDog(int timeoutWatchDog) {
		this.timeoutWatchDog = timeoutWatchDog;
	}

	public int getWatchDogRetries() {
		return watchDogRetries;
	}

	public void setWatchDogRetries(int watchDogRetries) {
		this.watchDogRetries = watchDogRetries;
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
		final StringBuilder builder = new StringBuilder();
		builder.append("AbstractServiceConfig [creationDate=").append(creationDate).append(", lastUpdate=")
				.append(lastUpdate).append(", uniqueId=").append(uniqueId).append(", name=").append(name)
				.append(", description=").append(description).append(", version=").append(version).append(", context=")
				.append(context).append(", groups=").append(groups).append(", ports=").append(ports).append(", tags=")
				.append(tags).append(", provides=").append(provides).append(", required=").append(required)
				.append(", note=").append(note).append(", comment=").append(comment).append(", remote=").append(remote)
				.append(", data=").append(data).append(", timeoutWatchDog=").append(timeoutWatchDog)
				.append(", watchDogRetries=").append(watchDogRetries).append(", targetRunLevel=").append(targetRunLevel)
				.append(", pausable=").append(pausable).append(", usableWithCron=").append(usableWithCron)
				.append(", priority=").append(priority).append("]");
		return builder.toString();
	}

}
