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
package org.ar4k.agent.farm.openshift;

import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.farm.FarmConfig;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione OpenShift farm
 */
public class OpenShiftFarmConfig extends FarmConfig {

	private static final long serialVersionUID = -864167599161787378L;

	/*
	 * @Parameter(names = "--clusterName", description =
	 * "server url in format opc.tcp://localhost:53530/Server") public String
	 * serverUrl = "opc.tcp://localhost:53530/Server";
	 */

	@Override
	public EdgeComponent instantiate() {
		OpenShiftFarmComponent ss = new OpenShiftFarmComponent(this);
		return ss;
	}

	@Override
	public int getPriority() {
		return 6;
	}

	@Override
	public boolean isSpringBean() {
		return false;
	}
}
