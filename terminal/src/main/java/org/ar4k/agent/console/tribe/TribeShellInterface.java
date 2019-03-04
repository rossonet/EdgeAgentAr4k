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
package org.ar4k.agent.console.tribe;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.ar4k.agent.config.tribe.TribeConfig;
import org.ar4k.agent.core.Anima;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.atomix.cluster.Member;
import io.atomix.core.Atomix;
import io.atomix.core.profile.Profile;
import io.atomix.primitive.partition.PartitionGroup;
import io.atomix.utils.net.Address;

/**
 * 
 * Interfaccia da linea di comando per per la gestione dell'aggregazione in
 * comunità di microservice.
 * 
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 */

@ShellCommandGroup("Tribe Commands")
@ShellComponent
@EnableMBeanExport
@ManagedResource(objectName = "bean:name=tribeInterface", description = "Ar4k Agent Tribe Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "tribeInterface")
@RestController
@RequestMapping("/tribeInterface")
@ConditionalOnProperty(name = "ar4k.tribe", havingValue = "true")
public class TribeShellInterface {

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	Anima anima;

	List<Atomix> selectedAtomix = new ArrayList<Atomix>();

	@Override
	protected void finalize() {
	}

	@SuppressWarnings("unused")
	private Availability testSelectedConfigOk() {
		return anima.getWorkingConfig() != null ? Availability.available()
				: Availability.unavailable("you have to select a config before");
	}

	@ShellMethod(value = "Create a Atomix cluster with 3 local nodes on different ports", group = "Tribe Commands")
	@ManagedOperation
	public void tribeFoundation(
			@ShellOption(help = "The TCP port for bind the Atomix istance 1", defaultValue = "5000") Integer port0,
			@ShellOption(help = "The TCP port for bind the Atomix istance 1", defaultValue = "5001") Integer port1,
			@ShellOption(help = "The TCP port for bind the Atomix istance 1", defaultValue = "5002") Integer port2,
			@ShellOption(help = "The name for the this node in the cluster", defaultValue = "ar4kMaster") String hostAlias,
			@ShellOption(help = "Multicast address for dicovery", defaultValue = "224.0.0.66") String multicastAddress,
			@ShellOption(help = "Multicast port", defaultValue = "5066") Integer multicastPort) {
		int contatore = 0;
		Integer[] ports = { port0, port1, port2 };
		for (Integer port : ports) {
			Atomix.Builder builder = Atomix.builder();
			builder.withLocalMember(Member.builder().withId(hostAlias + "-" + String.valueOf(contatore))
					.withAddress("localhost:" + String.valueOf(port)).build());
			builder.withProfiles(Profile.CONSENSUS, Profile.DATA_GRID);
			builder.withMulticastEnabled();
			builder.withMulticastAddress(Address.from(multicastAddress + ":" + String.valueOf(multicastPort)));
			Atomix atomix = builder.build();
			atomix.start();
			selectedAtomix.add(atomix);
			contatore++;
		}
	}

	@ShellMethod(value = "list the tribe joined", group = "Tribe Commands")
	@ManagedOperation
	// @ShellMethodAvailability("testSelectedConfigOk")
	public void listTribe() {
		for (Atomix target : selectedAtomix) {
			System.out.println("istance: " + target.getMembershipService().getLocalMember().toString() + "; members: "
					+ target.getMembershipService().getMembers().size());
			System.out.println("partitions:");
			for (PartitionGroup pt : target.getPartitionService().getPartitionGroups()) {
				System.out.print(
						pt.name() + " " + pt.type().name() + " (" + String.valueOf(pt.getPartitions().size()) + "); ");
			}
			System.out.println("\n---------------------------------------------");
		}
	}

	@ShellMethod(value = "Join to a existing Atomix cluster", group = "Tribe Commands")
	@ManagedOperation
	// @ShellMethodAvailability("testSelectedConfigOk")
	public void tribeJoin(
			@ShellOption(help = "The TCP port for bind the Atomix istance", defaultValue = "5009") Integer port,
			@ShellOption(help = "The name for the this node in the cluster", defaultValue = "ar4kSlave") String hostAlias,
			@ShellOption(help = "Multicast address for dicovery", defaultValue = "224.0.0.66") String multicastAddress,
			@ShellOption(help = "Multicast port", defaultValue = "5066") Integer multicastPort) {
		Atomix.Builder builder = Atomix.builder();
		builder.withLocalMember(
				Member.builder().withId(hostAlias).withAddress("localhost:" + String.valueOf(port)).build());
		builder.withProfiles(Profile.CONSENSUS, Profile.DATA_GRID);
		builder.withMulticastEnabled();
		builder.withMulticastAddress(Address.from(multicastAddress + ":" + String.valueOf(multicastPort)));
		Atomix atomix = builder.build();
		atomix.start();
		selectedAtomix.add(atomix);
	}

	@ShellMethod(value = "Add tribe config to the selected configuration", group = "Tribe Commands")
	@ManagedOperation
	@ShellMethodAvailability("testSelectedConfigOk")
	public void addTribe(@ShellOption(optOut = true) @Valid TribeConfig tribe) {
		anima.getWorkingConfig().beans.add(tribe);
	}

}
