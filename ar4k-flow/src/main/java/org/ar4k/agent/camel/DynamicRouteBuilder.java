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
package org.ar4k.agent.camel;

import java.util.UUID;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
public class DynamicRouteBuilder extends RouteBuilder {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(DynamicRouteBuilder.class.toString());

  private String from;
  private String to;

  public DynamicRouteBuilder(CamelContext camelContext, String from, String to) {
    super(camelContext);
    this.from = from;
    this.to = to;
  }

  private String idRotta = UUID.randomUUID().toString();

  @Override
  public void configure() throws Exception {
    try {
      from(from).routeId(idRotta).to(to);
    } catch (Exception ee) {
      logger.logException(ee);
    }
  }

  public String getIdRotta() {
    return idRotta;
  }

}
