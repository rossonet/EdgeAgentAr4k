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
package org.ar4k.agent.farm.local;

import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.farm.FarmConfig;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione LocalAccount farm
 */
public class LocalAccountFarmConfig extends FarmConfig {

	@Parameter(names = "--localDirectory", description = "local directory to run applications")
	public String localDirectory = "./local-runner";

	@Parameter(names = "--useRootAccount", description = "run farm as root account (true/false). It uses the sudo function", arity = 1)
	public boolean useRootAccount = false;

	private static final long serialVersionUID = -864167599161787378L;

	@Override
	public EdgeComponent instantiate() {
		final LocalAccountFarmComponent ss = new LocalAccountFarmComponent(this);
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
