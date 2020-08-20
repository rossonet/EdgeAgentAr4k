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
package org.ar4k.agent.cortex.drools;

import java.util.EnumSet;

import org.ar4k.agent.cortex.drools.DroolsService.DroolsServiceUpdateAction;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
public class DroolsServiceUpdateActionValidator implements IParameterValidator {

	@Override
	public void validate(String name, String value) throws ParameterException {
		try {
			DroolsServiceUpdateAction.valueOf(value);
		} catch (final java.lang.IllegalArgumentException aa) {
			final StringBuilder b = new StringBuilder();
			EnumSet.allOf(DroolsServiceUpdateAction.class).forEach(v -> {
				b.append(v.toString() + " ");
			});
			throw new ParameterException("Parameter " + name + " should be in " + b.toString());
		}
	}
}
