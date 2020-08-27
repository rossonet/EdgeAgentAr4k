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
package org.ar4k.agent.console;

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.AnimaHomunculus;
import org.ar4k.agent.core.RpcConversation;
import org.jline.utils.AttributedString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

/**
 * Gestione prompt dinamico per interazione da linea di comando.
 * 
 * @author Anndrea Ambrosini
 */

@Component
public class CustomPromptProvider implements PromptProvider {

  @Autowired
  Anima anima;

  @Autowired
  private AnimaHomunculus animaHomunculus;

  @Override
  public AttributedString getPrompt() {
    EdgeConfig wc = null;
    Authentication a = SecurityContextHolder.getContext().getAuthentication();
    if (a != null) {
      SessionInformation session = animaHomunculus.getAllSessions(a, false).get(0);
      wc = ((RpcConversation) anima.getRpc(session.getSessionId())).getWorkingConfig();
    }
    AnsiColor colore = AnsiColor.BLUE;
    String testo = "AGENT:> ";
    if (anima.getState() != null) {
      testo = anima.getState().toString() + ":> ";
      if (wc != null) {
        colore = wc.promptColor;
        testo = "-" + anima.getState().toString() + "- " + wc.prompt + ":# ";
      }
    }
    AttributedString prompt = null;
    if (a != null) {
      prompt = new AttributedString(
          AnsiOutput.toString(colore, "[", AnsiColor.YELLOW, a.getName(), colore, "] " + testo, AnsiColor.DEFAULT));
    } else {
      prompt = new AttributedString(AnsiOutput.toString(colore, testo, AnsiColor.DEFAULT));
    }
    return prompt;
  }
}