package org.ar4k.agent.watson;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.messages.ChatMessage;
import org.ar4k.agent.core.data.messages.StringMessage;
import org.ar4k.agent.core.interfaces.EdgeChannel;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.helper.StringUtils;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.mattermost.ChatPayload;
import org.ar4k.agent.rpc.RpcExecutor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.SubscribableChannel;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.MessageContextStateless;
import com.ibm.watson.assistant.v2.model.MessageInputStateless;
import com.ibm.watson.assistant.v2.model.MessageResponseStateless;
import com.ibm.watson.assistant.v2.model.MessageStatelessOptions;
import com.ibm.watson.assistant.v2.model.MessageStatelessOptions.Builder;
import com.ibm.watson.assistant.v2.model.RuntimeResponseGeneric;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Gestore servizio per connessioni sshd.
 *
 */
public class WatsonService implements EdgeComponent, MessageHandler {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(WatsonService.class);
	// iniettata vedi set/get
	private WatsonConfig configuration = null;
	private DataAddress dataspace = null;

	private Homunculus homunculus = null;

	private ServiceStatus serviceStatus = ServiceStatus.INIT;

	private Set<EdgeChannel> inputChannels = new HashSet<>();

	private Set<EdgeChannel> outputChannels = new HashSet<>();

	private EdgeChannel statusChannel = null;

	private Assistant assistant = null;
	private Map<String, MessageContextStateless> sessions = new HashMap<>();
	private Map<String, RpcExecutor> rpcSessions = new HashMap<>();

	@Override
	public void close() throws IOException {
		kill();
	}

	@Override
	public WatsonConfig getConfiguration() {
		return configuration;
	}

	@Override
	public DataAddress getDataAddress() {
		return dataspace;
	}

	@Override
	public Homunculus getHomunculus() {
		return homunculus;
	}

	@Override
	public String getServiceName() {
		return getConfiguration().getName();
	}

	@Override
	public void init() {
		setDataspace();
		IamAuthenticator authenticator = new IamAuthenticator(getConfiguration().apiKey);
		assistant = new Assistant(getConfiguration().watsonVersion, authenticator);
		assistant.setServiceUrl(getConfiguration().url);
	}

	private void setDataspace() {
		statusChannel = dataspace.createOrGetDataChannel("status", IPublishSubscribeChannel.class,
				"status of Watson connection", homunculus.getDataAddress().getSystemChannel(), (String) null,
				ConfigHelper.mergeTags(Arrays.asList("status", "text"), getConfiguration().getTags()), this);
		try {
			inputChannels.addAll(homunculus.getDataAddress()
					.getDataChannels(StringUtils.dataChannelFilterFromString(getConfiguration().channelInput)));
			outputChannels.addAll(homunculus.getDataAddress()
					.getDataChannels(StringUtils.dataChannelFilterFromString(getConfiguration().channelOutput)));
		} catch (IOException exception) {
			logger.logException(exception);
		}
		registerInputChannel();
	}

	private void registerInputChannel() {
		for (EdgeChannel c : inputChannels) {
			if (c instanceof SubscribableChannel) {
				((SubscribableChannel) c).subscribe(this);
			} else if (c instanceof PollableChannel) {
				logger.error("Watson input channel not works with a pollable input channel");
			}
		}

	}

	@Override
	public void kill() {
		serviceStatus = ServiceStatus.KILLED;
		if (assistant != null) {
			assistant = null;
		}

	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		this.configuration = ((WatsonConfig) configuration);
	}

	@Override
	public void setDataAddress(DataAddress dataAddress) {
		dataspace = dataAddress;
	}

	@Override
	public void setHomunculus(Homunculus homunculus) {
		this.homunculus = homunculus;
	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		final StringMessage message = new StringMessage();
		message.setPayload(serviceStatus.toString());
		statusChannel.getChannel().send(message);
		return serviceStatus;
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		try {
			if (message instanceof ChatMessage) {
				if (assistant != null) {
					final ChatPayload cm = ((ChatMessage) message).getPayload();
					sendMessageToWatson(cm);
				} else {
					logger.error("try to send message without connection " + message.getPayload());
				}
			} else {
				logger.error("received bad message type in write queue " + message.getPayload());
			}
		} catch (Exception a) {
			logger.logException(a);
		}
	}

	private void sendMessageToWatson(ChatPayload cm) {
		if (cm.isDirectMessage() || cm.isMentioned()) {
			MessageInputStateless input = new MessageInputStateless.Builder().messageType("text")
					.text(cm.getMessage().replace("\n", "").replace("\r", "")).build();
			Builder optionsBuilder = new MessageStatelessOptions.Builder().assistantId(getConfiguration().assitantId)
					.input(input);
			if (sessions.containsKey(cm.getUserId())) {
				optionsBuilder.context(sessions.get(cm.getUserId()));
			}
			MessageResponseStateless response = assistant.messageStateless(optionsBuilder.build()).execute()
					.getResult();
			sessions.put(cm.getUserId(), response.getContext());
			sendReplyToCaller(cm.getUserId(), cm.getChannelId(), response);
		} else {
			logger.info("not reply to message not direct and in where I'm not metioned");
		}
	}

	private void sendReplyToCaller(String userId, String channelId, MessageResponseStateless response) {
		final ChatPayload cp = new ChatPayload();
		cp.setChannelId(channelId);
		StringBuilder stringReply = new StringBuilder();
		logger.debug("watson reply -> " + response.toString());
		for (RuntimeResponseGeneric l : response.getOutput().getGeneric()) {
			if (l.text() != null) {
				if (l.text().startsWith(getConfiguration().actionTag)) {
					runAction(userId, l.text().substring(getConfiguration().actionTag.length() + 1), stringReply);
				} else {
					stringReply.append(l.text() + "\n");
				}
			}
		}
		cp.setMessage(stringReply.toString());
		final ChatMessage cm = new ChatMessage();
		cm.setPayload(cp);
		for (EdgeChannel oc : outputChannels) {
			oc.getChannel().send(cm);
		}
	}

	private void runAction(String userId, String command, StringBuilder stringReply) {
		try {
			logger.info("RUN ACTION REQUIRED BY WATSON -> " + command);
			if (!rpcSessions.containsKey(userId)) {
				final String sessionId = "watson-" + userId;
				homunculus.getHomunculusSession().registerNewSession(sessionId, sessionId);
				rpcSessions.put(userId, homunculus.getRpc(sessionId));
			}
			stringReply.append(rpcSessions.get(userId).elaborateMessage(command) + "\n");
		} catch (Exception e) {
			logger.logException(e);
			stringReply.append("EXCEPTION WITH COMMAND " + command + "\n" + EdgeLogger.stackTraceToString(e));
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WatsonService [");
		if (serviceStatus != null) {
			builder.append("serviceStatus=");
			builder.append(serviceStatus);
			builder.append(", ");
		}
		if (inputChannels != null) {
			builder.append("inputChannels=");
			builder.append(inputChannels);
			builder.append(", ");
		}
		if (outputChannels != null) {
			builder.append("outputChannels=");
			builder.append(outputChannels);
			builder.append(", ");
		}
		if (statusChannel != null) {
			builder.append("statusChannel=");
			builder.append(statusChannel);
			builder.append(", ");
		}
		if (assistant != null) {
			builder.append("assistant=");
			builder.append(assistant);
			builder.append(", ");
		}
		if (sessions != null) {
			builder.append("sessions=");
			builder.append(sessions);
			builder.append(", ");
		}
		if (rpcSessions != null) {
			builder.append("rpcSessions=");
			builder.append(rpcSessions);
		}
		builder.append("]");
		return builder.toString();
	}

}
