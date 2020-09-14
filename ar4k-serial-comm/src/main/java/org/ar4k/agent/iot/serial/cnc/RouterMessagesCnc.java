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
package org.ar4k.agent.iot.serial.cnc;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.interfaces.EdgeChannel;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Filtro con espressione regolare per l'input ottenuto via
 *         seriale/telnet da una CNC con endpoint camel di destinazione.
 *
 */
public class RouterMessagesCnc implements Serializable, compilePattern {

	private static final long serialVersionUID = 7183637837535639684L;

	@Parameter(names = "--regExp", description = "regular expression to find")
	public String regExp = ".*";

	@Parameter(names = "--endpoint", description = "internal queue for the messages found by the regular expression")
	public String endpoint = null;

	private Homunculus homunculus = Homunculus.getApplicationContext().getBean(Homunculus.class);

	private transient IPublishSubscribeChannel cacheChannel = null;
	private transient Pattern pattern = null;

	public boolean matches(String testString) {
		compilePattern();
		return pattern.matcher(testString).matches();
	}

	private void compilePattern() {
		if (pattern == null) {
			pattern = Pattern.compile(regExp);
		}
	}

	public String getElaboratedMessage(String testString) {
		compilePattern();
		return testString;
	}

	public IPublishSubscribeChannel getAr4kChannel(String fatherOfChannels, String scopeOfChannels) {
		if (cacheChannel == null) {
			final EdgeChannel father = Homunculus.getApplicationContext().getBean(Homunculus.class).getDataAddress()
					.createOrGetDataChannel(endpoint, IPublishSubscribeChannel.class, "CNC root node", (String) null,
							null, homunculus.getTags());
			cacheChannel = (IPublishSubscribeChannel) Homunculus.getApplicationContext().getBean(Homunculus.class)
					.getDataAddress().createOrGetDataChannel(endpoint, IPublishSubscribeChannel.class,
							"cache data of the CNC", father,
							scopeOfChannels != null ? scopeOfChannels : homunculus.getDataAddress().getDefaultScope(),
							homunculus.getTags());
		}
		return cacheChannel;
	}

}
