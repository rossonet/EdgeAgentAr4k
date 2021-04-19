package org.ar4k.agent.mattermost;

import java.net.URI;

import javax.net.ssl.SSLContext;
import javax.websocket.ClientEndpoint;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.json.JSONObject;

@ClientEndpoint
public class MatterMostWebSocketHandler extends javax.websocket.Endpoint{

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(MatterMostWebSocketHandler.class.toString());

	Session userSession = null;
	private MessageHandler messageHandler;

	public MatterMostWebSocketHandler(URI endpointURI) {
		try {
			final WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			if (endpointURI.toString().startsWith("wss://")) {
				final SSLContext sslContext = SSLContext.getInstance("SSL");
				final ClientEndpointConfig config = (ClientEndpointConfig) ClientEndpointConfig.Builder.create().build()
						.getUserProperties().put("org.apache.websocket.SSL_CONTEXT", sslContext);
				container.connectToServer(this,config, endpointURI);
			} else {
				container.connectToServer(this, endpointURI);
			}
		} catch (final Exception e) {
			logger.logException(e);
		}
	}


	@Override
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

	@Override
	public void onOpen(Session session, EndpointConfig config) {
		// TODO Auto-generated method stub

	}
}
