package org.ar4k.agent.core.data;

import java.io.Serializable;
import java.util.Date;

import org.ar4k.agent.core.data.messages.InternalMessage;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.messaging.Message;

public class BagMessage implements Serializable {

	// private static final Gson gson = new GsonBuilder().setLenient().create();

	private static final long serialVersionUID = -5601362670594700641L;

	private static transient final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton()
			.getLoggerFactory().getLogger(BagMessage.class.toString());

	private InternalMessage<?> message = null;
	private long time = 0;

	public BagMessage(InternalMessage<?> received) {
		message = received;
		time = new Date().getTime();
	}

	public BagMessage(final String line) {
		try {
			if (line != null) {
				JSONObject json;
				json = new JSONObject(line);
				time = json.getLong("ts");
				message = (InternalMessage<?>) Class.forName(json.getString("t")).newInstance();
				message.setObjectPayload(json.get("v"));
			}
		} catch (final Exception e) {
			logger.logException("line -> " + line, e);
		}
	}

	public String getLine() {
		try {
			// return ConfigHelper.toBase64(this);
			final JSONObject json = new JSONObject();
			json.put("ts", time);
			json.put("v", message.getPayload());
			json.put("t", message.getClass().getName());
			// System.out.println("*** " + json.toString());
			return json.toString();
		} catch (final Exception e) {
			logger.logException(e);
			return null;
		}
	}

	public Message<?> getMessage() {
		return message;
	}

	public long getTime() {
		return time;
	}

}
