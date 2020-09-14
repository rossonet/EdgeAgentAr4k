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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.validation.Valid;

import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.ssh.client.AbstractSshTunnel;
import org.ar4k.agent.tunnels.ssh.client.SSHUserInfo;
import org.ar4k.agent.tunnels.ssh.client.SshLocalConfig;
import org.ar4k.agent.tunnels.ssh.client.SshLocalTunnel;
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

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

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

  protected static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(SshShellInterface.class.toString());

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

  @ShellMethod(value = "run ssh tunnel that trasport a remote port to a local one", group = "Tunnel Commands")
  @ManagedOperation
  public String runSshTunnelRemoteToLocalSsh(@ShellOption(optOut = true) @Valid SshRemoteConfig sshRemoteConfig) {
    try {
      SshRemoteTunnel tunnelSsh = sshRemoteConfig.instantiate();
      tunnelSsh.setHomunculus(homunculus);
      tunnelSsh.init();
      tunnels.put(UUID.randomUUID().toString(), tunnelSsh);
      logger.info("runned tunnel ssh remote " + tunnelSsh);
      return tunnelSsh.toString();
    } catch (Exception a) {
      return EdgeLogger.stackTraceToString(a);
    }
  }

  @ShellMethod(value = "run ssh tunnel that trasport a local port to a remote one", group = "Tunnel Commands")
  @ManagedOperation
  public String runSshTunnelLocalToRemoteSsh(@ShellOption(optOut = true) @Valid SshLocalConfig sshLocalConfig) {
    try {
      SshLocalTunnel tunnelSsh = sshLocalConfig.instantiate();
      tunnelSsh.setHomunculus(homunculus);
      tunnelSsh.init();
      tunnels.put(UUID.randomUUID().toString(), tunnelSsh);
      logger.info("runned tunnel ssh local " + tunnelSsh);
      return tunnelSsh.toString();
    } catch (Exception a) {
      return EdgeLogger.stackTraceToString(a);
    }
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

  public static String execCommandOnRemoteSshHost(String authkey, String username, String password, String host,
      int port, String command) throws IOException, JSchException {
    JSch jsch = new JSch();
    if (authkey != null && !authkey.isEmpty())
      logger.info("auth ssh using key file: " + authkey);
    jsch.addIdentity(authkey);
    Session session = jsch.getSession(username, host, port);
    SSHUserInfo ui = new SSHUserInfo();
    if (password != null && !password.isEmpty())
      ui.setPassword(password);
    ui.setTrust(true);
    session.setUserInfo(ui);
    session.setDaemonThread(true);
    session.connect();
    Channel channel = session.openChannel("exec");
    ((ChannelExec) channel).setCommand(command);
    // X Forwarding
    // channel.setXForwarding(true);
    // channel.setInputStream(System.in);
    channel.setInputStream(null);
    // channel.setOutputStream(System.out);
    // FileOutputStream fos=new FileOutputStream("/tmp/stderr");
    // ((ChannelExec)channel).setErrStream(fos);
    ((ChannelExec) channel).setErrStream(System.err);
    InputStream in = channel.getInputStream();
    channel.connect();
    StringBuilder outBuff = new StringBuilder();
    byte[] tmp = new byte[1024];
    while (true) {
      while (in.available() > 0) {
        int i = in.read(tmp, 0, 1024);
        if (i < 0)
          break;
        outBuff.append(new String(tmp, 0, i));
      }
      if (channel.isClosed()) {
        if (in.available() > 0)
          continue;
        logger.info("exit-status ssh: " + channel.getExitStatus());
        break;
      }
      try {
        Thread.sleep(1000);
      } catch (Exception ee) {
      }
    }
    channel.disconnect();
    session.disconnect();
    logger.info("ssh on " + host + " return " + outBuff.toString());
    return outBuff.toString();
  }

}
