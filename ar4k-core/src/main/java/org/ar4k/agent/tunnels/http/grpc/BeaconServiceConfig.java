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
package org.ar4k.agent.tunnels.http.grpc;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.config.json.BeaconServiceConfigJsonAdapter;

import com.beust.jcommander.Parameter;
import com.google.gson.TypeAdapter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione Beacon Server.
 */
public class BeaconServiceConfig extends AbstractServiceConfig {

  private static final long serialVersionUID = -8036646566388899515L;

  @Parameter(names = "--port", description = "the port for run the Beacon endpoint", required = true)
  public int port = 6599;

  public BeaconService instantiate() {
    BeaconService ss = new BeaconService();
    ss.setConfiguration(this);
    return ss;
  }

  @Override
  public TypeAdapter<? extends ConfigSeed> getJsonTypeAdapter() {
    return new BeaconServiceConfigJsonAdapter();
  }

}
