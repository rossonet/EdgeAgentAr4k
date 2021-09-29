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
package org.ar4k.agent.modbus.slave;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.services.EdgeComponent;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione modbus slave
 */
public class ModbusSlaveConfig extends AbstractServiceConfig {


	/**
	 * 
	 */
	private static final long serialVersionUID = -7857808112578882353L;

	@Override
	public EdgeComponent instantiate() {
		ModbusSlaveService ss = new ModbusSlaveService();
		ss.setConfiguration(this);
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
