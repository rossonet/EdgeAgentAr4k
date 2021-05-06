package org.ar4k.agent.mattermost;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.ClientEndpointConfig.Configurator;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler.Whole;
import javax.websocket.Session;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.json.JSONObject;

public class MatterMostWebSocketHandler extends Endpoint {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(MatterMostWebSocketHandler.class.toString());

	Session userSession = null;
	private MessageHandler messageHandler;

	private boolean active = false;

	private Whole<String> sessionHandler = new Whole<String>() {

		@Override
		public void onMessage(String message) {
			try {
				if (messageHandler != null) {
					JSONObject o = new JSONObject(message);
					messageHandler.handleMessage(o);
					active = true;
				}
			} catch (Exception a) {
				active = false;
				logger.warn("in websocket message ", a);
			}
		}

	};

	public MatterMostWebSocketHandler(URI endpointURI) {
		try {
			final ClientManager client = ClientManager.createClient();
			logger.info("WS TARGET -> " + endpointURI);
			Configurator clientEndpointConfigurator = new Configurator() {
				public void beforeRequest(Map<String, List<String>> headers) {
					headers.remove("Origin");
					/*
					 * for (Entry<String, List<String>> line:headers.entrySet()) {
					 * System.out.println("HEADER -> "+line.getKey()); for (String v:
					 * line.getValue()) { System.out.println("v: "+v); } }
					 */
				}

			};
			ClientEndpointConfig clientConfigurator = ClientEndpointConfig.Builder.create()
					.configurator(clientEndpointConfigurator).build();
			if (endpointURI.toString().startsWith("wss://")) {
				// logger.info("Mattermost ws in SSL");
				final SSLContext sslContext = SSLContext.getDefault();
				final SSLEngineConfigurator sslEngineConfigurator = new SSLEngineConfigurator(sslContext, true, true,
						true);
				client.getProperties().put(ClientProperties.SSL_ENGINE_CONFIGURATOR, sslEngineConfigurator);
				client.getProperties().put(ClientProperties.REDIRECT_ENABLED, true);
				client.getProperties().put(ClientProperties.RETRY_AFTER_SERVICE_UNAVAILABLE, true);
				// System.out.println("--- "+client.getInstalledExtensions());
				client.connectToServer(this, clientConfigurator, endpointURI);
				active = true;
			} else {
				client.connectToServer(this, clientConfigurator, endpointURI);
				active = true;
			}
		} catch (final Exception e) {
			active = false;
			logger.logException(e);
		}
	}

	@Override
	public void onClose(Session userSession, CloseReason reason) {
		// System.out.println("-------------- close ws session -> " +
		// reason.getReasonPhrase());
		this.userSession = null;
		active = false;
	}

	public void addMessageHandler(MessageHandler msgHandler) {
		this.messageHandler = msgHandler;
	}

	public void sendMessage(String message) {
		try {
			// System.out.println("-------------- send message to ws session -> " +
			// message);
			this.userSession.getAsyncRemote().sendText(message);
		} catch (Exception e) {
			active = false;
			logger.logException(e);
		}
	}

	public static interface MessageHandler {
		public void handleMessage(JSONObject message);
	}

	public void startSession(String authToken) {
		final JSONObject o = new JSONObject();
		o.put("seq", 1);
		o.put("action", "authentication_challenge");
		final JSONObject data = new JSONObject();
		data.put("token", authToken);
		o.put("data", data);
		// System.out.println("SEND WSS: " + o.toString());
		sendMessage(o.toString());
	}

	@Override
	public void onOpen(Session session, EndpointConfig config) {
		// System.out.println("-------------- new ws session -> " + session.getId());
		session.addMessageHandler(String.class, sessionHandler);
		this.userSession = session;
		active = true;
	}

	public boolean isActive() {
		return userSession != null && userSession.isOpen() && active;
	}
}
