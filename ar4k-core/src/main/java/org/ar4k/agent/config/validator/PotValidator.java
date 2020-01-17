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
package org.ar4k.agent.config.validator;

import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 *
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Validatore input per i servizi di tipo tunnel nella configurazione
 *         selezionata.
 */
public class PotValidator implements IParameterValidator {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(PotValidator.class.toString());

  @Override
  public void validate(String name, String value) throws ParameterException {
    Anima anima = (Anima) Anima.getApplicationContext().getBean("anima");
    boolean ok = false;
    StringBuilder r = new StringBuilder();
    for (ServiceConfig a : anima.getRuntimeConfig().pots) {
      try {
        ConfigSeed b = a;
        if (b.getName().equals(value)) {
          ok = true;
        }
        r.append(b.getName() + " ");
      } catch (Exception ee) {
        logger.logException(ee);
      }
    }
    if (ok == false) {
      throw new ParameterException("Parameter " + name + " should be in " + r);
    }
  }
}
