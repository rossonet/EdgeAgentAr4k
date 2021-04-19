package org.ar4k.agent.mattermost;

import java.net.URI;

import javax.net.ssl.SSLContext;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.json.JSONObject;

@ClientEndpoint
public class MatterMostWebSocketHandler {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(MatterMostWebSocketHandler.class.toString());

	Session userSession = null;
	private MessageHandler messageHandler;

	public MatterMostWebSocketHandler(URI endpointURI) {
		try {
			final ClientManager client = ClientManager.createClient();
			logger.info("WS TARGET -> ");
			if (endpointURI.toString().startsWith("wss://")) {
				logger.info("Mattermost ws in SSL");
				final SSLContext sslContext = SSLContext.getDefault();
				final SSLEngineConfigurator sslEngineConfigurator =
						new SSLEngineConfigurator(sslContext, true, false, false);
				client.getProperties().put(ClientProperties.SSL_ENGINE_CONFIGURATOR,
						sslEngineConfigurator);
				client.connectToServer(this, endpointURI);
			} else {
				client.connectToServer(this, endpointURI);
			}
		} catch (final Exception e) {
			logger.logException(e);
		}
	}

	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		this.userSession = null;
	}

	@OnMessage
	public void onMessage(String message) {
		if (this.messageHandler != null)
			this.messageHandler.handleMessage(message);
	}

	public void addMessageHandler(MessageHandler msgHandler) {
		this.messageHandler = msgHandler;
	}

	public void sendMessage(String message) {
		this.userSession.getAsyncRemote().sendText(message);
	}

	public static interface MessageHandler {
		public void handleMessage(String message);
	}

	public void startSession(String authToken) {
		final JSONObject o = new JSONObject();
		o.put("seq", 1);
		o.put("action", "authentication_challenge");
		final JSONObject data = new JSONObject();
		data.put("token", authToken);
		o.put("data", data);
		System.out.println("SEND WSS: " + o.toString());
		sendMessage(o.toString());
	}

	@OnOpen
	public void onOpen(Session session) {
		this.userSession=session;
	}
}
