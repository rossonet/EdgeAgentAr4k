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
package org.ar4k.agent.console.chat.irc;

//TODO mappa un servizio irc al bus come client

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.ar4k.agent.helper.AbstractShellHelper;
import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.Client.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.shell.Availability;
import org.springframework.shell.Shell;
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
 *         Interfaccia gestione servizi rpc via IRC.
 */

@ShellCommandGroup("IRC Commands")
@ShellComponent
//@EnableMBeanExport
//@ManagedResource(objectName = "bean:name=ircInterface", description = "Ar4k Agent IRC RCP Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "ircInterface")
@RestController
@RequestMapping("/ircInterface")
public class IrcShellInterface extends AbstractShellHelper {
  @Autowired
  Shell shell;

  Client client = null;

  @SuppressWarnings("unused")
  private Availability testClientUsed() {
    return client == null ? Availability.available()
        : Availability.unavailable("the irc client is used, please disconnect before");
  }

  @SuppressWarnings("unused")
  private Availability testClientFree() {
    return client != null ? Availability.available() : Availability.unavailable("the irc client is not connected");
  }

  @ShellMethod(value = "Add a irc remote management connection to the selected configuration", group = "IRC Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public void addIrcManagerToSelectedConfig(@ShellOption(optOut = true) @Valid IrcHomunculusConfig service) {
    getWorkingConfig().pots.add(service);
  }

  @ShellMethod(value = "Start IRC remote management connection", group = "IRC Commands")
  @ManagedOperation
  @ShellMethodAvailability("testClientUsed")
  public void startIrcRemoteManager(
      @ShellOption(help = "the ircd server host", defaultValue = "celestini.rossonet.net") String host,
      @ShellOption(help = "the ircd server port", defaultValue = "6667") int port,
      @ShellOption(help = "the nickname on the chat", defaultValue = "testAr4kAgent") String nickName,
      @ShellOption(help = "the stanza to login", defaultValue = "#cmd") String stanza,
      @ShellOption(help = "true if the connection is encrypted", defaultValue = "false", arity = 0) boolean ssl,
      @ShellOption(help = "true for print the debug message in terminal", defaultValue = "false", arity = 0) boolean debug) {
    client = connectToServer(nickName, host, port, ssl, debug);
    client.addChannel(stanza);
    client.getEventManager().registerEventListener(new IrcConnectionHandlerHomunculus(client, shell));
  }

  @ShellMethod(value = "Stop IRC remote management connection", group = "IRC Commands")
  @ManagedOperation
  @ShellMethodAvailability("testClientFree")
  public void stopIrcRemoteManager() {
    client.shutdown();
    client = null;
  }

  public static final Client connectToServer(String nickName, String host, int port, boolean ssl, boolean debug) {
    Builder builder = Client.builder().nick(nickName).server().host(host).port(port).secure(ssl).then();
    if (debug == true) {
      SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
      builder.listeners().input(line -> System.out.println(sdf.format(new Date()) + ' ' + "[I] " + line));
      builder.listeners().output(line -> System.out.println(sdf.format(new Date()) + ' ' + "[O] " + line));
      builder.listeners().exception(Throwable::printStackTrace);
    }
    Client client = builder.build();
    client.connect();
    System.out.println(client.getServerInfo().toString());
    return client;
  }

}
