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
package org.ar4k.agent.hazelcast;

import java.util.Set;

import javax.validation.Valid;

import org.ar4k.agent.helper.AbstractShellHelper;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hazelcast.core.Member;

/**
 *
 * Interfaccia da linea di comando per per la gestione dell'aggregazione in
 * comunità di microservice.
 *
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 */

@ShellCommandGroup("Hazelcast Commands")
@ShellComponent
@RestController
@RequestMapping("/hazelcastInterface")

// TO______DO implementare comandi in console per gestire mappe dati serializzate in json dei servizi hazelcast attivi

public class HazelcastShellInterface extends AbstractShellHelper {

	HazelcastComponent hazelcastInstance = null;

	protected Availability testHazelCastNodeNull() {
		return hazelcastInstance == null ? Availability.available()
				: Availability.unavailable("a Hazelcast node exists with status " + hazelcastInstance);
	}

	protected Availability testHazelcastNodeRunning() {
		return hazelcastInstance != null ? Availability.available()
				: Availability.unavailable("no Hazelcast client are running");
	}

	@ShellMethod(value = "Create Hazelcast", group = "Hazelcast Commands")
	@ManagedOperation
	@ShellMethodAvailability("testHazelCastNodeNull")
	public void hazelcastJoin(@ShellOption(optOut = true) @Valid HazelcastConfig tribe) {
		hazelcastInstance = new HazelcastComponent(tribe);
		hazelcastInstance.init();
	}

	@ShellMethod(value = "Get status of Hazelcast istance", group = "Hazelcast Commands")
	@ManagedOperation
	@ShellMethodAvailability("testHazelcastNodeRunning")
	public String hazelcastStatus() {
		return hazelcastInstance.toString();
	}

	@ShellMethod(value = "List nodes joined to the cluster", group = "Hazelcast Commands")
	@ManagedOperation
	@ShellMethodAvailability("testHazelcastNodeRunning")
	public Set<Member> hazelcastList() {
		return hazelcastInstance.createOrGetHazelcastInstance().getCluster().getMembers();
	}

	@ShellMethod(value = "Stop Hazelcast instance", group = "Hazelcast Commands")
	@ManagedOperation
	@ShellMethodAvailability("testHazelcastNodeRunning")
	public void hazelcastStop() {
		hazelcastInstance.kill();
		hazelcastInstance = null;
	}

	@ShellMethod(value = "Add Hazelcast config to the selected configuration", group = "Hazelcast Commands")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addHazelcast(@ShellOption(optOut = true) @Valid HazelcastConfig tribe) {
		getWorkingConfig().pots.add(tribe);
	}

}
