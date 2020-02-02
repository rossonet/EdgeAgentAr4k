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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.validation.Valid;

import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.tunnels.ssh.client.AbstractSshTunnel;
import org.ar4k.agent.tunnels.ssh.client.SshLocalConfig;
import org.ar4k.agent.tunnels.ssh.client.SshRemoteConfig;
import org.ar4k.agent.tunnels.ssh.client.SshRemoteTunnel;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Interfaccia a linea di comando per gestione tunnel SSH.
 */

@ShellCommandGroup("Tunnel Commands")
@ShellComponent
//@EnableMBeanExport
//@ManagedResource(objectName = "bean:name=sshInterface", description = "Ar4k Agent Ssh Tunnel", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "sshInterface")
@RestController
@RequestMapping("/sshInterface")
public class SshShellInterface extends AbstractShellHelper {

  private Map<String, AbstractSshTunnel> tunnels = new HashMap<>();

  @ShellMethod(value = "Add a ssh endpoint to the selected configuration that transports a local port to a remote one", group = "Tunnel Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public void addSshTunnelLocalPortToRemote(@ShellOption(optOut = true) @Valid SshLocalConfig service) {
    getWorkingConfig().pots.add(service);
  }

  @ShellMethod(value = "Add a ssh endpoint to the selected configuration that trasport a remote port to a local one", group = "Tunnel Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public void addSshTunnelRemotePortToLocale(@ShellOption(optOut = true) @Valid SshRemoteConfig service) {
    getWorkingConfig().pots.add(service);
  }

  @ShellMethod(value = "Add a ssh endpoint to the selected configuration that trasport a remote port to a local one", group = "Tunnel Commands")
  @ManagedOperation
  public void runSshTunnelToLocalSsh(@ShellOption(help = "remote ssh server") String hostTarget,
      @ShellOption(help = "remote ssh port") String portTarget,
      @ShellOption(help = "remote ssh user") String userAccount,
      @ShellOption(help = "remote ssh password") String password,
      @ShellOption(help = "remote port to bind on the server for the ssh connection") String portBindRemote) {
    SshRemoteConfig config = new SshRemoteConfig();
    config.redirectServer = "127.0.0.1";
    config.redirectPort = 22;
    config.bindHost = "0.0.0.0";
    config.bindPort = Integer.valueOf(portBindRemote);
    config.host = hostTarget;
    config.port = Integer.valueOf(portTarget);
    config.username = userAccount;
    config.password = password;
    SshRemoteTunnel tunnelSsh = config.instantiate();
    tunnelSsh.setAnima(anima);
    tunnelSsh.init();
    tunnels.put(UUID.randomUUID().toString(), tunnelSsh);
  }

  @ShellMethod(value = "List ssh tunnels", group = "Tunnel Commands")
  @ManagedOperation
  public String listSshTunnels() {
    StringBuilder sb = new StringBuilder();
    for (Entry<String, AbstractSshTunnel> a : tunnels.entrySet()) {
      sb.append(a.getKey() + " -> " + a.getValue().toString());
    }
    return sb.toString();
  }

  @ShellMethod(value = "Kill a tunnel", group = "Tunnel Commands")
  @ManagedOperation
  public void removeSshTunnels(@ShellOption(help = "tunnelId") String tunnelId) {
    tunnels.get(tunnelId).kill();
    tunnels.remove(tunnelId);
  }

}
