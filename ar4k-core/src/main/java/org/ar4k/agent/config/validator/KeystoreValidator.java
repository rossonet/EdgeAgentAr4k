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
package org.ar4k.agent.config.validator;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.keystore.KeystoreConfig;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Validatore da linea di comando per keystore collegato ad Anima.
 *
 */
public class KeystoreValidator implements IParameterValidator {

  @Override
  public void validate(String name, String value) throws ParameterException {
    Anima anima = (Anima) Anima.getApplicationContext().getBean("anima");
    boolean ok = false;
    for (KeystoreConfig a : anima.getKeyStores()) {
      if (a.label.equals(value)) {
        ok = true;
      }
    }
    if (ok == false) {
      StringBuilder b = new StringBuilder();
      anima.getKeyStores().forEach(v -> {
        b.append(v.label + " ");
      });
      throw new ParameterException("Parameter " + name + " should be in " + b.toString());
    }
  }
}