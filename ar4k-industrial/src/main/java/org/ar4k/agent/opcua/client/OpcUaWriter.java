package org.ar4k.agent.opcua.client;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import org.ar4k.agent.core.data.messages.BooleanMessage;
import org.ar4k.agent.core.data.messages.ChatMessage;
import org.ar4k.agent.core.data.messages.DoubleMessage;
import org.ar4k.agent.core.data.messages.FloatMessage;
import org.ar4k.agent.core.data.messages.HealthMessage;
import org.ar4k.agent.core.data.messages.IndustrialMessage;
import org.ar4k.agent.core.data.messages.IntegerMessage;
import org.ar4k.agent.core.data.messages.JSONMessage;
import org.ar4k.agent.core.data.messages.LoggerMessage;
import org.ar4k.agent.core.data.messages.LongMessage;
import org.ar4k.agent.core.data.messages.StringMessage;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.opcua.utils.OpcUaUtils;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

public class OpcUaWriter implements MessageHandler {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(OpcUaWriter.class);

	private final OpcUaClientNodeConfig singleNode;
	private final OpcUaClientService opcUaClientService;

	public OpcUaWriter(OpcUaClientService opcUaClientService, OpcUaClientNodeConfig singleNode) {
		this.opcUaClientService = opcUaClientService;
		this.singleNode = singleNode;
	}

	@Override
	public void handleMessage(Message<?> message) {
		String value = null;
		if (message instanceof DoubleMessage) {
			value = message.getPayload().toString();
		} else if (message instanceof HealthMessage) {
			value = message.getPayload().toString();
		} else if (message instanceof BooleanMessage) {
			value = message.getPayload().toString();
		} else if (message instanceof FloatMessage) {
			value = message.getPayload().toString();
		} else if (message instanceof IntegerMessage) {
			value = message.getPayload().toString();
		} else if (message instanceof LoggerMessage) {
			value = message.getPayload().toString();
		} else if (message instanceof LongMessage) {
			value = message.getPayload().toString();
		} else if (message instanceof StringMessage) {
			value = message.getPayload().toString();
		}

		else if (message instanceof ChatMessage) {
			value = ((ChatMessage) message).getPayload().getMessage();
		}

		else if (message instanceof IndustrialMessage) {
			value = ((IndustrialMessage) message).getPayload().getValue();
		}

		else if (message instanceof JSONMessage) {
			value = ((JSONMessage) message).getPayload().toString();
		} else {
			value = message.getPayload().toString();
		}
		try {
			OpcUaUtils.writeValueToOpc(singleNode.nodeId, value, null, opcUaClientService.getOpcUaClient());
		} catch (UnsupportedEncodingException | UaException | InterruptedException | ExecutionException exception) {
			logger.error("sending message to " + singleNode.nodeId, exception);
		}

	}

}
