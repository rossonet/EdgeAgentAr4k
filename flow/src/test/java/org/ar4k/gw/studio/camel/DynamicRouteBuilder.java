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
package org.ar4k.gw.studio.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. <andrea.ambrosini@rossonet.com>
 *
 */
public class DynamicRouteBuilder extends RouteBuilder {

	private final String from;
	private final String to;

	public DynamicRouteBuilder(CamelContext camelContext, String from, String to) {
		super(camelContext);
		this.from = from;
		this.to = to;
	}

	@Override
	public void configure() throws Exception {
		from(from).to(to);
	}

}
