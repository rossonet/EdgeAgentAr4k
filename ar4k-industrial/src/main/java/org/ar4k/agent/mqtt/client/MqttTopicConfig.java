package org.ar4k.agent.mqtt.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import com.beust.jcommander.Parameter;

public class MqttTopicConfig {

	@Parameter(names = "--topic", description = "mqtt topic")
	public String topic;

	@Parameter(names = "--qosRead", description = "quality of service for read")
	public int qosRead = 0;

	@Parameter(names = "--qosWrite", description = "quality of service for write")
	public int qosWrite = 1;

	@Parameter(names = "--jsonChannel", description = "name of the channel field in json syntax")
	public String jsonChannel = "id";

	@Parameter(names = "--jsonQuality", description = "name of the quality field in json syntax")
	public String jsonQuality = "q";

	@Parameter(names = "--jsonValue", description = "name of the value field in json syntax")
	public String jsonValue = "v";

	@Parameter(names = "--jsonSourceTime", description = "name of the source time field in json syntax")
	public String jsonSourceTime = "t";

	@Parameter(names = "--jsonServerTime", description = "name of the server time field in json syntax")
	public String jsonServerTime = "ts";

	@Parameter(names = "--jsonDataType", description = "name of the data type field in json syntax")
	public String jsonDataType = "f";

	@Parameter(names = "--internalTargetChannel", description = "internal channel to send the update from the topic")
	public String readChannel = null;

	@Parameter(names = "--internalWriteChannel", description = "internal channel to write data to the topic")
	public String writeChannel = null;

	@Parameter(names = "--fatherOfChannels", description = "directory channel for message topic")
	public String fatherOfChannels = null;

	@Parameter(names = "--scopeOfChannels", description = "scope for the parent channel. If null take the default of the address space")
	public String scopeOfChannels = null;

	@Parameter(names = "--tags", description = "channel tags (multi selection)", variableArity = true, required = false)
	public Collection<String> tags = new ArrayList<>();

	public String uuid = UUID.randomUUID().toString();

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MqttTopicConfig [");
		if (topic != null) {
			builder.append("topic=");
			builder.append(topic);
			builder.append(", ");
		}
		builder.append("qosRead=");
		builder.append(qosRead);
		builder.append(", qosWrite=");
		builder.append(qosWrite);
		builder.append(", ");
		if (jsonChannel != null) {
			builder.append("jsonChannel=");
			builder.append(jsonChannel);
			builder.append(", ");
		}
		if (jsonQuality != null) {
			builder.append("jsonQuality=");
			builder.append(jsonQuality);
			builder.append(", ");
		}
		if (jsonValue != null) {
			builder.append("jsonValue=");
			builder.append(jsonValue);
			builder.append(", ");
		}
		if (jsonSourceTime != null) {
			builder.append("jsonSourceTime=");
			builder.append(jsonSourceTime);
			builder.append(", ");
		}
		if (jsonServerTime != null) {
			builder.append("jsonServerTime=");
			builder.append(jsonServerTime);
			builder.append(", ");
		}
		if (jsonDataType != null) {
			builder.append("jsonDataType=");
			builder.append(jsonDataType);
			builder.append(", ");
		}
		if (readChannel != null) {
			builder.append("readChannel=");
			builder.append(readChannel);
			builder.append(", ");
		}
		if (writeChannel != null) {
			builder.append("writeChannel=");
			builder.append(writeChannel);
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
