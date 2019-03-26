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

import org.ar4k.agent.core.Anima;
import org.jline.utils.AttributedString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
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

  @Override
  public AttributedString getPrompt() {
    AnsiColor colore = AnsiColor.BLUE;
    String testo = "AGENT:> ";
    if (anima.getState() != null)
      testo = anima.getState().toString() + ":> ";
    if (anima.getWorkingConfig() != null) {
      colore = anima.getWorkingConfig().promptColor;
      testo = anima.getWorkingConfig().prompt + ":# ";
    }
    AttributedString prompt = new AttributedString(AnsiOutput.toString(colore, testo, AnsiColor.DEFAULT));
    return prompt;
  }
}