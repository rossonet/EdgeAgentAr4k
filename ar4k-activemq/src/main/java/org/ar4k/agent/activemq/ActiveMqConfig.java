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
package org.ar4k.agent.activemq;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.Ar4kComponent;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione server ActiveMQ
 */
public class ActiveMqConfig extends AbstractServiceConfig {

  private static final long serialVersionUID = 6435014475100329645L;

  @Parameter(names = "--portMqtt", description = "port to expose the MQTT service")
  public String portMqtt = "1883";

  @Parameter(names = "--portMqttSsl", description = "port to expose the MQTT service SSL")
  public String portMqttSsl = "8883";

  @Parameter(names = "--portWebService", description = "port to expose the STOP service")
  public String portWebService = "9080";

  @Parameter(names = "--keyForSslInKeystore", description = "alias for key in anima keystore")
  public String keyForSslInKeystore = "activemq";

  @Override
  public Ar4kComponent instantiate() {
    ActiveMqService ss = new ActiveMqService();
    ss.setConfiguration(this);
    return ss;
  }

  @Override
  public int getPriority() {
    return 9;
  }

  @Override
  public boolean isSpringBean() {
    return false;
  }
}
