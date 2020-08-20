package org.ar4k.agent.core.data.generator.dataType;

import java.util.Date;

import org.ar4k.agent.core.data.generator.FormatGenerator;
import org.ar4k.agent.core.data.messages.Ar4kMessage;
import org.ar4k.agent.core.data.messages.JSONMessage;
import org.json.JSONObject;

public class JsonNumericSimulator implements FormatGenerator {

	private static final String valuePlaceholder = "%value%";
	private static final String nodePlaceholder = "%nodeId%";
	private static final String timePlaceholder = "%time%";
	private final String patternJson;
	private final String nodeId;

	public JsonNumericSimulator(String nodeId, String patternJson) {
		this.patternJson = patternJson;
		this.nodeId = nodeId;
	}

	@Override
	public Ar4kMessage<?> format(Object actual) {
		final JSONMessage message = new JSONMessage();
		final String elaborateString = patternJson.replace(nodePlaceholder, nodeId)
				.replace(timePlaceholder, String.valueOf(new Date().getTime()))
				.replace(valuePlaceholder, actual.toString());
		final JSONObject json = new JSONObject(elaborateString);
		message.setPayload(json);
		return message;
	}

	@Override
	public boolean isNumber() {
		return true;
	}

}
