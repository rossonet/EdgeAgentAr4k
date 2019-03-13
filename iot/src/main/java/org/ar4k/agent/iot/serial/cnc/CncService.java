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
package org.ar4k.agent.iot.serial.cnc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.iot.serial.SerialService;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio collegamento macchine CNC via seriale/telnet
 *
 */
public class CncService extends SerialService {

  long conteggioLoop = 0;

  // iniettata vedi set/get
  private CncConfig configuration = null;

  // iniettata vedi set/get
  private Anima anima = null;

  @Override
  public synchronized void loop() {
    super.loop();
    conteggioLoop++;
    // invia i comandi periodici
    for (TriggerCommand c : configuration.cronCommands) {
      c.fire(this);
    }

    // elabora il routing delle code per espressione regolare
    String messaggioDaElaborare = null;
    if (lastMessage != null && ((ArrayBlockingQueue<String>) lastMessage).size() > 0) {
      messaggioDaElaborare = lastMessage.poll();
    }
    if (messaggioDaElaborare != null && messaggioDaElaborare != "") {
      for (RouterMessagesCnc ricerca : configuration.replies) {
        Pattern p = Pattern.compile(ricerca.regExp);
        Matcher m = p.matcher(messaggioDaElaborare);
        if (m.find()) {
          // TODO: gestire con JMS
          // anima.camelContext.createProducerTemplate().sendBody(ricerca.camelEndpoint,
          // messaggioDaElaborare);
        } else {
          // System.out.println("scarto: [" + messaggioDaElaborare + "] per " +
          // ricerca.regExp);
        }
      }
    }
  }

  @Override
  public CncConfig getConfiguration() {
    return configuration;
  }

  @Override
  public void setConfiguration(ServiceConfig configuration) {
    super.setConfiguration(configuration);
    this.configuration = ((CncConfig) configuration);
  }

  @Override
  public Anima getAnima() {
    return anima;
  }

  @Override
  public void setAnima(Anima anima) {
    super.setAnima(anima);
    this.anima = anima;
  }

}
