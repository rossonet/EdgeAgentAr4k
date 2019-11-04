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

import java.io.Serializable;
import java.util.regex.Pattern;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.data.Ar4kChannel;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Filtro con espressione regolare per l'input ottenuto via
 *         seriale/telnet da una CNC con endpoint camel di destinazione.
 *
 */
public class RouterMessagesCnc implements Serializable, compilePattern {

  private static final long serialVersionUID = 7183637837535639684L;

  @Parameter(names = "--regExp", description = "regular expression to find")
  public String regExp = ".*";

  @Parameter(names = "--endpoint", description = "internal queue for the messages found by the regular expression")
  public String endpoint = null;

  private Anima anima = Anima.getApplicationContext().getBean(Anima.class);

  private transient IPublishSubscribeChannel cacheChannel = null;
  private transient Pattern pattern = null;

  public boolean matches(String testString) {
    compilePattern();
    return pattern.matcher(testString).matches();
  }

  private void compilePattern() {
    if (pattern == null) {
      pattern = Pattern.compile(regExp);
    }
  }

  public String getElaboratedMessage(String testString) {
    compilePattern();
    return testString;
  }

  public IPublishSubscribeChannel getAr4kChannel(String fatherOfChannels, String scopeOfChannels) {
    if (cacheChannel == null) {
      Ar4kChannel father = Anima.getApplicationContext().getBean(Anima.class).getDataAddress()
          .createOrGetDataChannel(endpoint, IPublishSubscribeChannel.class, (String) null, null);
      cacheChannel = (IPublishSubscribeChannel) Anima.getApplicationContext().getBean(Anima.class).getDataAddress()
          .createOrGetDataChannel(endpoint, IPublishSubscribeChannel.class, father,
              scopeOfChannels != null ? scopeOfChannels : anima.getDataAddress().getDefaultScope());
    }
    return cacheChannel;
  }

}
