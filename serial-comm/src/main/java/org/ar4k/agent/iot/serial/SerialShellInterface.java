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
package org.ar4k.agent.iot.serial;

import javax.validation.Valid;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.iot.serial.SerialConfig;
import org.ar4k.agent.iot.serial.SerialService;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Interfaccia da linea di comando per configurazione della connessione
 *         seriale.
 *
 */

@ShellCommandGroup("Serial Commands")
@ShellComponent
@EnableMBeanExport
@ManagedResource(objectName = "bean:name=serialInterface", description = "Ar4k Agent Main Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "serialInterface")
@RestController
@RequestMapping("/serialInterface")
@ConditionalOnProperty(name = "ar4k.serial", havingValue = "true")
public class SerialShellInterface {

  @Autowired
  ApplicationContext applicationContext;

  @Autowired
  Anima anima;

  @SuppressWarnings("unused")
  private Availability testSelectedConfigOk() {
    return anima.getWorkingConfig() != null ? Availability.available()
        : Availability.unavailable("you have to select a config before");
  }

  @ShellMethod(value = "List serial ports attached to this host", group = "Serial Commands")
  @ManagedOperation
  public String listSerialPorts() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(SerialService.getSerialDevice());
  }

  @ShellMethod(value = "Add a serial interface service to the selected configuration", group = "Serial Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public void addSerialService(@ShellOption(optOut = true) @Valid SerialConfig service) {
    anima.getWorkingConfig().services.add(service);
  }

}