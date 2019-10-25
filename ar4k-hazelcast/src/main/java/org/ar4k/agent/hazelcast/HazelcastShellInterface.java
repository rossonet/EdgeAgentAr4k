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

import java.util.List;

import javax.validation.Valid;

import org.ar4k.agent.helper.AbstractShellHelper;
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

import io.atomix.core.set.DistributedSet;

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
@ManagedResource(objectName = "bean:name=hazelcastInterface", description = "Ar4k Agent Hazelcast Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "hazelcastInterface")
@RestController
@RequestMapping("/hazelcastInterface")

// TODO mappa le variabili definite in conf al bus e mette a disposizione un bean per accedere a tipi dati in cluster (tipo ecss)

public class HazelcastShellInterface extends AbstractShellHelper {

  HazelcastComponent selectedAtomix = null;

  protected Availability testAtomixNodeNull() {
    return selectedAtomix == null ? Availability.available()
        : Availability.unavailable("a Atomix node exists with status " + selectedAtomix.getStatusString());
  }

  protected Availability testAtomixNodeRunning() {
    return selectedAtomix != null ? Availability.available() : Availability.unavailable("no Beacon client are running");
  }

  @ShellMethod(value = "Create Atomix node", group = "Tribe Commands")
  @ManagedOperation
  @ShellMethodAvailability("testAtomixNodeNull")
  public void tribeJoin(@ShellOption(optOut = true) @Valid HazelcastConfig tribe) {
    selectedAtomix = new HazelcastComponent(tribe);
    selectedAtomix.init();
  }

  @ShellMethod(value = "Get status for Atomix node", group = "Tribe Commands")
  @ManagedOperation
  @ShellMethodAvailability("testAtomixNodeRunning")
  public String tribeStatus() {
    return selectedAtomix.getStatusString();
  }

  @ShellMethod(value = "List nodes joined to the cluster", group = "Tribe Commands")
  @ManagedOperation
  @ShellMethodAvailability("testAtomixNodeRunning")
  public List<String> tribeList() {
    return selectedAtomix.listAtomixNodes();
  }

  @ShellMethod(value = "Stop Atomix node", group = "Tribe Commands")
  @ManagedOperation
  @ShellMethodAvailability("testAtomixNodeRunning")
  public void tribeStop() {
    selectedAtomix.kill();
    selectedAtomix = null;
  }

  @ShellMethod(value = "Add tribe config to the selected configuration", group = "Tribe Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public void addTribe(@ShellOption(optOut = true) @Valid HazelcastConfig tribe) {
    getWorkingConfig().pots.add(tribe);
  }

  @ShellMethod(value = "List data in Atomix", group = "Tribe Commands")
  @ManagedOperation
  @ShellMethodAvailability("testAtomixNodeRunning")
  public DistributedSet<String> atomixKeySet() {
    return selectedAtomix.getAtomixMap().keySet();
  }

  @ShellMethod(value = "Put data in Atomix map", group = "Tribe Commands")
  @ManagedOperation
  @ShellMethodAvailability("testAtomixNodeRunning")
  public void atomixPut(@ShellOption(help = "key for the data. If the key exists will be override") String key,
      @ShellOption(help = "value for the key") String value) {
    selectedAtomix.getAtomixMap().put(key, value);
  }

  @ShellMethod(value = "Get data in Atomix map", group = "Tribe Commands")
  @ManagedOperation
  @ShellMethodAvailability("testAtomixNodeRunning")
  public Object atomixGet(@ShellOption(help = "key for the data") String key) {
    return selectedAtomix.getAtomixMap().get(key);
  }

}
