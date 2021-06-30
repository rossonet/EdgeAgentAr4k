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
package org.ar4k.agent.opcua.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.ar4k.agent.industrial.Enumerator.DataChangeTrigger;
import org.ar4k.agent.industrial.Enumerator.DeadbandType;
import org.ar4k.agent.industrial.validators.DataChangeTriggerValidator;
import org.ar4k.agent.industrial.validators.DeadbandTypeValidator;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         sottocrizione a nodo opc ua semplice o multiplo con espressione regolare
 */
public class OpcUaClientNodeConfig implements Serializable {

	private static final long serialVersionUID = 970930410169105077L;

	@Parameter(names = "--nodeId", description = "Node ID")
	public String nodeId = null;

	@Parameter(names = "--group", description = "the subscription group")
	public String group = "default-group";

	@Parameter(names = "--samplingInterval", description = "sampling interval")
	public Double samplingInterval = 1000.0;

	@Parameter(names = "--publishInterval", description = "publish interval")
	public int publishInterval = 2000;

	@Parameter(names = "--deadbandValue", description = "deadband value")
	public double deadbandValue = 0;

	@Parameter(names = "--queueSize", description = "queue size")
	public int queueSize = 10;

	@Parameter(names = "--discardOldest", description = "discard oldest")
	public boolean discardOldest = true;

	@Parameter(names = "--deadbandType", description = "dead band type for the node", validateWith = DeadbandTypeValidator.class)
	public DeadbandType deadbandType = DeadbandType.none;

	@Parameter(names = "--dataChangeTrigger", description = "data change trigger type for the node", validateWith = DataChangeTriggerValidator.class)
	public DataChangeTrigger dataChangeTrigger = DataChangeTrigger.statusOrValueOrTimestamp;

	@Parameter(names = "--internalTargetChannel", description = "internal channel to send the update from the node")
	public String readChannel = null;

	@Parameter(names = "--internalWriteChannel", description = "internal channel to write data to the node")
	public String writeChannel = null;

	@Parameter(names = "--fatherOfChannels", description = "directory channel for message topics")
	public String fatherOfChannels = null;

	@Parameter(names = "--scopeOfChannels", description = "scope for the parent channel. If null take the default of the address space")
	public String scopeOfChannels = null;

	@Parameter(names = "--tags", description = "channel tags (multi selection)", variableArity = true, required = false)
	public Collection<String> tags = new ArrayList<>();

	public String uuid = UUID.randomUUID().toString();

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OpcUaClientNodeConfig [");
		if (nodeId != null) {
			builder.append("nodeRegEx=");
			builder.append(nodeId);
			builder.append(", ");
		}
		builder.append("samplingInterval=");
		builder.append(samplingInterval);
		builder.append(", publishInterval=");
		builder.append(publishInterval);
		builder.append(", deadbandValue=");
		builder.append(deadbandValue);
		builder.append(", queueSize=");
		builder.append(queueSize);
		builder.append(", discardOldest=");
		builder.append(discardOldest);
		builder.append(", ");
		if (deadbandType != null) {
			builder.append("deadbandType=");
			builder.append(deadbandType);
			builder.append(", ");
		}
		if (dataChangeTrigger != null) {
			builder.append("dataChangeTrigger=");
			builder.append(dataChangeTrigger);
			builder.append(", ");
		}
		if (readChannel != null) {
			builder.append("readChannel=");
			builder.append(readChannel);
			builder.append(", ");
		}
		if (fatherOfChannels != null) {
			builder.append("fatherOfChannels=");
			builder.append(fatherOfChannels);
			builder.append(", ");
		}
		if (scopeOfChannels != null) {
			builder.append("scopeOfChannels=");
			builder.append(scopeOfChannels);
			builder.append(", ");
		}
		if (tags != null) {
			builder.append("tags=");
			builder.append(tags);
			builder.append(", ");
		}
		if (uuid != null) {
			builder.append("uuid=");
			builder.append(uuid);
		}
		builder.append("]");
		return builder.toString();
	}

}
