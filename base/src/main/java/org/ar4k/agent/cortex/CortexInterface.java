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
package org.ar4k.agent.cortex;

import java.io.Console;

import javax.annotation.PostConstruct;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.cortex.conversationBot.RegExBrocaBot;
import org.ar4k.agent.cortex.conversationBot.exceptions.HomunculusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Interfaccia da linea di comando per configurazione della connessione
 *         seriale.
 *
 */

@ShellCommandGroup("Cortex Commands")
@ShellComponent
@EnableMBeanExport
@ManagedResource(objectName = "bean:name=cortexInterface", description = "Cortex Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "cortexInterface")
@RestController
@RequestMapping("/cortexInterface")
public class CortexInterface {

  @Autowired
  ApplicationContext applicationContext;

  @Autowired
  Anima anima;

  RegExBrocaBot<?> chatBot = null;

  private String endChatString = "!end";

  @Override
  protected void finalize() {
  }

  @SuppressWarnings("unused")
  private Availability testSelectedConfigOk() {
    return anima.getWorkingConfig() != null ? Availability.available()
        : Availability.unavailable("you have to select a config before");
  }

  @PostConstruct
  private void avvioChatChannel() {
    // TODO: chatBot = new RegExBot(anima);
  }

  @ShellMethod(value = "Chat with the Ar4k Agent as divinity -consciousness chat-", group = "Chat Commands")
  @ManagedOperation
  @GetMapping("chatToAnimaAsDivinity")
  public void chatToAnimaAsDivinity() throws HomunculusException {
    Console console = System.console();
    boolean attivo = true;
    System.out.println(AnsiOutput.toString(AnsiColor.RED,
        "\n\n\nType " + endChatString + " as a message to stop the conversation.\n\n\n", AnsiColor.DEFAULT));
    while (attivo) {
      String messaggio = console.readLine(AnsiOutput.toString(AnsiColor.RED, "YOU [divinity] -> ", AnsiColor.DEFAULT));
      if (messaggio.equals(endChatString)) {
        attivo = false;
        break;
      }
      String risposta = AnsiOutput.toString(AnsiColor.GREEN, "Ar4k Agent: ", AnsiColor.DEFAULT,
          chatBot.consoleGodQuery(messaggio));
      System.out.println(risposta);
    }
  }

  @ShellMethod(value = "Send a message to the Anima of the bot as divinity -consciousness chat-", group = "Chat Commands")
  @ManagedOperation
  @GetMapping("sendMessageToAnimaAsDivinity")
  public String sendMessageToAnimaAsDivinity(String message) throws HomunculusException {
    return chatBot.consoleGodQuery(message);
  }

  @ShellMethod(value = "Reset the memory of the consciousness chat", group = "Chat Commands")
  @ManagedOperation
  @GetMapping("resetConsciousnessExperience")
  public String resetConsciousnessExperience() {
    chatBot.reset();
    return "memory erased";
  }

}
