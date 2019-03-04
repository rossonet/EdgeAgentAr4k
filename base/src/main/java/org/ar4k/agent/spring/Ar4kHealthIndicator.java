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
package org.ar4k.agent.spring;

import java.util.Map;

import org.ar4k.agent.helper.Utils;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

/**
 * Attuatore Spring Boot per il servizio di health.
 * 
 * @author Andrea Ambrosini
 *
 * 
 */
@Component
public class Ar4kHealthIndicator extends AbstractHealthIndicator {

  public Ar4kHealthIndicator() {
  }

  @Override
  protected void doHealthCheck(Health.Builder builder) throws Exception {
    try {
      Map<String, Object> rit = Utils.getSystemInfo();
      for (String chiave : rit.keySet()) {
        builder.withDetail(chiave, rit.get(chiave));
      }
      builder.up();
    } catch (Exception ex) {
      builder.down(ex);
    }
  }
}
