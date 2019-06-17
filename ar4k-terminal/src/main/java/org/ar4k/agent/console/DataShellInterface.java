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
package org.ar4k.agent.console;

import java.util.Collection;

import javax.validation.Valid;

import org.ar4k.agent.core.data.channels.IDirectChannel;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Interfaccia gestione servizi dati.
 */

@ShellCommandGroup("Data Server Commands")
@ShellComponent
@EnableMBeanExport
@ManagedResource(objectName = "bean:name=dataInterface", description = "Ar4k Agent Data Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "dataInterface")
@RestController
@RequestMapping("/dataInterface")
public class DataShellInterface extends AbstractShellHelper {

  @ShellMethod(value = "List all data channels in Spring Integration enviroments", group = "Data Server Commands")
  @ManagedOperation
  public Collection<String> listSpringDataChannels() {
    Collection<String> result = anima.getDataAddress().listSpringIntegrationChannels();
    return result;
  }

  @ShellMethod(value = "List all managed data channels", group = "Data Server Commands")
  @ManagedOperation
  public Collection<String> listDataChannels() {
    return anima.getDataAddress().listChannels();
  }

  @ShellMethod(value = "Add direct data channel to the address space", group = "Data Server Commands")
  @ManagedOperation
  public void addDataDirectChannel(@ShellOption(optOut = true) @Valid IDirectChannel dataChannel) {
    anima.getDataAddress().addDataChannel(dataChannel);
  }

  @ShellMethod(value = "Remove data channel from the address space", group = "Data Server Commands")
  @ManagedOperation
  public void removeDataDirectChannel(@ShellOption(help = "target channel id to remove") String idChannel) {
    anima.getDataAddress().removeDataChannel(idChannel);
  }

  @ShellMethod(value = "Clear address space", group = "Data Server Commands")
  @ManagedOperation
  public void clearDataAddressSpace() {
    anima.getDataAddress().clearDataChannels();
  }

}
