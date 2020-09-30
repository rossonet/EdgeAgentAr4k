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
package org.ar4k.agent.activemq;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.interfaces.EdgeComponent;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione server ActiveMQ
 */
public class ActiveMqConfig extends AbstractServiceConfig {

	private static final long serialVersionUID = 6435014475100329645L;

	@Parameter(names = "--beanName", description = "the beanName for the Spring registration")
	public String beanName = "activemq-instance";

	@Parameter(names = "--secured", description = "active security (true/false)")
	public boolean secured = false;

	@Parameter(names = "--portMqtt", description = "port to expose the MQTT service")
	public String portMqtt = "1883";

	@Parameter(names = "--portMqttSsl", description = "port to expose the MQTT service SSL")
	public String portMqttSsl = "8883";

	@Parameter(names = "--portWebService", description = "port to expose the STOP service")
	public String portWebService = "9080";

	@Parameter(names = "--keyForSslInKeystore", description = "alias for key in anima keystore")
	public String keyForSslInKeystore = "activemq";

	@Parameter(names = "--discoveryName", description = "discovery name for cluster")
	public String discoveryName = "discovery-artemis";

	@Parameter(names = "--broadcastPeriod", description = "broadcast period for cluster")
	public long broadcastPeriod = 600;

	@Parameter(names = "--clusterName", description = "set a cluster name to active the cluster or null to disable it")
	public String clusterName = null;

	@Parameter(names = "--groupAddress", description = "group address for cluster")
	public String groupAddress = "231.7.7.7";

	@Parameter(names = "--groupPort", description = "group port for cluster")
	public int groupPort = 8563;

	@Parameter(names = "--clusterRetryInterval", description = "cluster retry interval")
	public long clusterRetryInterval = 500;

	@Parameter(names = "--trunkPort", description = "port for cluster communication")
	public int trunkPort = 2053;

	@Parameter(names = "--clusterTimeWait", description = "time wait for cluster formation in ms")
	public long clusterTimeWait = 60000L;

	public final TimeUnit clusterUnit = TimeUnit.MILLISECONDS;

	@Parameter(names = "--clusterIterations", description = "number of retry for the cluster formation")
	public int clusterIterations = 10;

	@Parameter(names = "--clusterServers", description = "numbers of cluster nodes to wait before the service starting")
	public int clusterServers = 1;

	@Parameter(names = "--maxHops", description = "maxHops for cluster topology")
	public int maxHops = 1;

	@Parameter(names = "--clusterStaticHosts", description = "hosta for clustering")
	public List<String> clusterStaticHosts = new ArrayList<>();

	@Override
	public EdgeComponent instantiate() {
		final ActiveMqService ss = new ActiveMqService();
		ss.setConfiguration(this);
		return ss;
	}

	@Override
	public int getPriority() {
		return 9;
	}

	@Override
	public boolean isSpringBean() {
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ActiveMqConfig [");
		if (beanName != null)
			builder.append("beanName=").append(beanName).append(", ");
		builder.append("secured=").append(secured).append(", ");
		if (portMqtt != null)
			builder.append("portMqtt=").append(portMqtt).append(", ");
		if (portMqttSsl != null)
			builder.append("portMqttSsl=").append(portMqttSsl).append(", ");
		if (portWebService != null)
			builder.append("portWebService=").append(portWebService).append(", ");
		if (keyForSslInKeystore != null)
			builder.append("keyForSslInKeystore=").append(keyForSslInKeystore).append(", ");
		if (discoveryName != null)
			builder.append("discoveryName=").append(discoveryName).append(", ");
		builder.append("broadcastPeriod=").append(broadcastPeriod).append(", ");
		if (clusterName != null)
			builder.append("clusterName=").append(clusterName).append(", ");
		if (groupAddress != null)
			builder.append("groupAddress=").append(groupAddress).append(", ");
		builder.append("groupPort=").append(groupPort).append(", clusterRetryInterval=").append(clusterRetryInterval)
				.append(", trunkPort=").append(trunkPort).append(", clusterTimeWait=").append(clusterTimeWait)
				.append(", ");
		if (clusterUnit != null)
			builder.append("clusterUnit=").append(clusterUnit).append(", ");
		builder.append("clusterIterations=").append(clusterIterations).append(", clusterServers=")
				.append(clusterServers).append(", maxHops=").append(maxHops).append(", ");
		if (clusterStaticHosts != null)
			builder.append("clusterStaticHosts=").append(clusterStaticHosts);
		builder.append("]");
		return builder.toString();
	}

}
