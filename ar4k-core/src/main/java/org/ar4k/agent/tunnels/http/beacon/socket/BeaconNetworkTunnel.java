package org.ar4k.agent.tunnels.http.beacon.socket;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.ar4k.agent.config.network.NetworkConfig;
import org.ar4k.agent.config.network.NetworkTunnel;
import org.ar4k.agent.config.network.NetworkConfig.NetworkMode;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http.grpc.beacon.Agent;
import org.ar4k.agent.tunnels.http.grpc.beacon.ResponseNetworkChannel;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelMessage;
import org.ar4k.agent.tunnels.http.grpc.beacon.TunnelServiceV1Grpc.TunnelServiceV1Stub;

import io.netty.channel.ChannelHandlerContext;

public class BeaconNetworkTunnel implements NetworkTunnel {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconNetworkTunnel.class.toString());
	private static final int PACKET_CHUNK_LIMIT = 192;
	public static final long LAST_MESSAGE_FROM_BEACON_SERVER_TIMEOUT = 2500;
	static final int BEACON_DELAY_RECONNECTION = 1500;
	static final int RETRY_LIMIT = 15;
	public static final int WAIT_WHILE_DELAY = 1200;
	public static final long SYNC_TIME_OUT = 20000;
	static final int DELAY_SOFT_CHECK = 3500;
	static final long PING_FROM_BEACON_SERVER_CHECK_FACTOR = 2;

	public static final boolean TRACE_LOG_IN_INFO = true;

	private final TunnelServiceV1Stub asyncStubTunnel;

	private long tunnelUniqueId;
	private long uniqueClassId = 0;
	private final NetworkConfig config;
	private final boolean isStartingFromMe;
	private final Agent meAgent;
	private Agent remoteAgent = null;
	private NetworkMode myRoleMode = null;
	private BeaconConnection beaconConnection = null;
	private BeaconNetworkReceiver networkReceiver = null;
	private final List<SessionTunnel> sessions = new LinkedList<>();

	private boolean activeTunnel = true;
	private final AtomicLong packetSend = new AtomicLong(0);
	private final AtomicLong packetReceived = new AtomicLong(0);
	private final AtomicLong packetError = new AtomicLong(0);
	private final AtomicLong packetControl = new AtomicLong(0);
	private final AtomicLong lastControlMessage = new AtomicLong(new Date().getTime());

	public BeaconNetworkTunnel(Agent me, NetworkConfig config, boolean ownerRequest,
			TunnelServiceV1Stub asyncStubTunnel, String tunnelId) {
		this.meAgent = me;
		this.config = config;
		this.isStartingFromMe = ownerRequest;
		tunnelUniqueId = Long.valueOf(tunnelId);
		this.asyncStubTunnel = asyncStubTunnel;
		if ((config.getNetworkModeRequested().equals(NetworkMode.CLIENT) && ownerRequest)
				|| config.getNetworkModeRequested().equals(NetworkMode.SERVER) && !ownerRequest) {
			myRoleMode = NetworkMode.CLIENT;
		} else {
			myRoleMode = NetworkMode.SERVER;
		}
		if (TRACE_LOG_IN_INFO)
			logger.info(me.getAgentUniqueName() + " created BeaconNetworkTunnel tunnel id {} role {}", tunnelUniqueId,
					myRoleMode);
	}

	@Override
	public void init() {
		uniqueClassId = UUID.randomUUID().getMostSignificantBits();
		networkReceiver = new BeaconNetworkReceiver(this, uniqueClassId, PACKET_CHUNK_LIMIT);
		if (myRoleMode.equals(NetworkMode.SERVER)) {
			try {
				networkReceiver.getOrCreateServerSocketChannel(uniqueClassId);
			} catch (final Exception e) {
				logger.logException(e);
			}
			if (TRACE_LOG_IN_INFO)
				logger.info("tunnel server TCP id {} created", tunnelUniqueId);
		}
		if (TRACE_LOG_IN_INFO)
			logger.info("{} started NetworkHub tunnel id {} role: {}", meAgent.getAgentUniqueName(), tunnelUniqueId,
					myRoleMode);
		beaconConnection = new BeaconConnection(this);
	}

	@Override
	public void setResponseNetworkChannel(ResponseNetworkChannel response) {
		this.tunnelUniqueId = response.getTargeId();
	}

	@Override
	public void setRemoteAgent(Agent remoteAgent) {
		this.remoteAgent = remoteAgent;
	}

	@Override
	public NetworkConfig getConfig() {
		return config;
	}

	@Override
	public BeaconNetworkReceiver getNetworkReceiver() {
		return networkReceiver;
	}

	@Override
	public long getUniqueClassId() {
		return uniqueClassId;
	}

	@Override
	public long getTunnelId() {
		return tunnelUniqueId;
	}

	@Override
	public Agent getRemoteAgent() {
		return remoteAgent;
	}

	@Override
	public long getPacketSend() {
		return packetSend.get();
	}

	@Override
	public long getPacketReceived() {
		return packetReceived.get();
	}

	@Override
	public long getPacketControl() {
		return packetControl.get();
	}

	@Override
	public long getPacketError() {
		return packetError.get();
	}

	Agent getMe() {
		return meAgent;
	}

	boolean isActiveTunnel() {
		return activeTunnel;
	}

	TunnelServiceV1Stub getAsyncStubTunnel() {
		return asyncStubTunnel;
	}

	boolean imTheClient() {
		return (getConfig().getNetworkModeRequested().equals(NetworkMode.CLIENT) && isStartingFromMe)
				|| getConfig().getNetworkModeRequested().equals(NetworkMode.SERVER) && !isStartingFromMe;
	}

	@Override
	public void beaconObserverComplete(long targetId) {
		activeTunnel = false;
		kill();
		if (TRACE_LOG_IN_INFO) {
			logger.info("BeaconObserver completed on {} tunnelId: {}", meAgent.getAgentUniqueName(), targetId);
		}
	}

	@Override
	public void close() throws Exception {
		kill();
		if (TRACE_LOG_IN_INFO)
			logger.info("closed BeaconNetworkTunnel on {} id: {} role: {}", meAgent.getAgentUniqueName(),
					tunnelUniqueId, myRoleMode);
	}

	@Override
	public void kill() {
		for (final SessionTunnel m : sessions) {
			try {
				m.close();
			} catch (final Exception e) {
				logger.logException(e);
			}
		}
		sessions.clear();

		activeTunnel = false;
		beaconConnection.close();
		if (networkReceiver != null) {
			try {
				networkReceiver.close();
			} catch (final Exception e) {
				logger.logException("EXCEPTION killing BeaconNetworkTunnel on " + meAgent.getAgentUniqueName() + " id: "
						+ tunnelUniqueId + " role: " + myRoleMode, e);
			}
		}
		networkReceiver = null;
		if (TRACE_LOG_IN_INFO)
			logger.info("killed BeaconNetworkTunnel on {} id: {} role: {}", meAgent.getAgentUniqueName(),
					tunnelUniqueId, myRoleMode);
	}

	boolean isBeaconConnectionOk() {
		try {
			return (beaconConnection != null && beaconConnection.isBeaconToServerOnline());
		} catch (final Exception e) {
			logger.logException(e);
			return false;
		}
	}

	void sendMessageToBeaconTunnel(TunnelMessage tunnelMessage) {
		beaconConnection.toBeaconServerOnNext(tunnelMessage);
	}

	@Override
	public void selfCheckIfNeeded() {
		if (beaconConnection != null) {
			beaconConnection.selfCheckIfNeeded();
		}
		nextActionAllSessions();
	}

	NetworkMode getMyRoleMode() {
		return myRoleMode;
	}

	void nextActionAllSessions() {
		if (networkReceiver != null) {
			for (final SessionTunnel s : sessions) {
				networkReceiver.nextAction(s.getSessionId(), s.getLastNettyContext());
			}
		}

	}

	@Override
	public int getWaitingPackagesCount() {
		return sessions.stream().mapToInt(SessionTunnel::countInputCachedMessages).sum();
	}

	void incrementPacketSend() {
		updateNetworkActiviti();
		packetSend.incrementAndGet();
	}

	void incrementPacketError() {
		updateNetworkActiviti();
		packetError.incrementAndGet();
	}

	void incrementPacketControl() {
		updateNetworkActiviti();
		packetControl.incrementAndGet();
	}

	void incrementPacketReceived() {
		updateNetworkActiviti();
		packetReceived.incrementAndGet();
	}

	private void updateNetworkActiviti() {
		networkReceiver.updateNetworkActiviti();
	}

	SessionTunnel getOrCreateSessionById(long sessionId) {
		SessionTunnel result = null;
		for (final SessionTunnel s : sessions) {
			if (s.getSessionId() == sessionId) {
				result = s;
				break;
			}
		}
		if (result == null) {
			result = new SessionTunnel(sessionId);
			sessions.add(result);
			if (TRACE_LOG_IN_INFO) {
				logger.info("-- NEW SESSION OBJECT CREATED FOR SESSIONID {} --", sessionId);
			}
		}
		return result;
	}

	void removeSessionData(long sessionId) {
		SessionTunnel toDelete = null;
		for (final SessionTunnel s : sessions) {
			if (s.getSessionId() == sessionId) {
				toDelete = s;
				break;
			}
		}
		if (toDelete != null) {
			try {
				toDelete.close();
			} catch (final Exception e) {
				logger.logException(e);
			}
			sessions.remove(toDelete);
		}
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("BeaconNetworkTunnel [tunnelUniqueId=").append(tunnelUniqueId).append(", uniqueClassId=")
				.append(uniqueClassId).append(", ");
		if (config != null)
			builder.append("config=").append(config).append(", ");
		builder.append("isStartingFromMe=").append(isStartingFromMe).append(", ");
		if (meAgent != null)
			builder.append("meAgent=").append(meAgent).append(", ");
		if (remoteAgent != null)
			builder.append("remoteAgent=").append(remoteAgent).append(", ");
		if (myRoleMode != null)
			builder.append("myRoleMode=").append(myRoleMode).append(", ");
		if (beaconConnection != null)
			builder.append("beaconConnection=").append(beaconConnection).append(", ");
		if (networkReceiver != null)
			builder.append("networkReceiver=").append(networkReceiver).append(", ");
		builder.append("activeTunnel=").append(activeTunnel).append(", ");
		builder.append("packetSend=").append(packetSend).append(", ");
		builder.append("packetReceived=").append(packetReceived).append(", ");
		builder.append("packetError=").append(packetError).append(", ");
		builder.append("packetControl=").append(packetControl).append(", ");
		builder.append("]");
		return builder.toString();
	}

	public void registerSocketContext(long sessionId, ChannelHandlerContext ctx) {
		getOrCreateSessionById(sessionId).setLastNettyContext(ctx);
	}

	long getLastAckSent(long sessionId) {
		return getOrCreateSessionById(sessionId).getLastAckSent();
	}

	long getLastAckReceived(long sessionId) {
		return getOrCreateSessionById(sessionId).getLastAckReceived();
	}

	long getProgressiveNetworkToBeacon(long clientSerialId) {
		return getOrCreateSessionById(clientSerialId).getNextProgressiveNetworkToBeacon();
	}

	void addInputCachedMessage(long sessionId, long messageId, MessageCached messageCached) {
		getOrCreateSessionById(sessionId).addInputCachedMessage(messageId, messageCached);

	}

	void addOutputCachedMessages(long sessionId, long messageId, MessageCached messageCached) {
		getOrCreateSessionById(sessionId).addOutputCachedMessage(messageId, messageCached);
	}

	Map<Long, MessageCached> getOutputCachedMessages(long sessionId) {
		return getOrCreateSessionById(sessionId).getOutputCachedMessagesMap();
	}

	Map<Long, MessageCached> getInputCachedMessages(long sessionId) {
		return getOrCreateSessionById(sessionId).getInputCachedMessagesMap();
	}

	void removeIputCachedMessage(long sessionId, Long messageId) {
		getOrCreateSessionById(sessionId).removeIputCachedMessage(messageId);
	}

	void removeOutputCachedMessage(long sessionId, long messageId) {
		getOrCreateSessionById(sessionId).removeOutputCachedMessage(messageId);
	}

	Map<Long, MessageCached> getAllInputCachedMessages() {
		final Map<Long, MessageCached> result = new HashMap<>();
		for (final SessionTunnel s : sessions) {
			result.putAll(s.getInputCachedMessagesMap());
		}
		return result;
	}

	Map<Long, MessageCached> getAllOutputCachedMessages() {
		final Map<Long, MessageCached> result = new HashMap<>();
		for (final SessionTunnel s : sessions) {
			result.putAll(s.getOutputCachedMessagesMap());
		}
		return result;
	}

	void setLastControlMessage() {
		lastControlMessage.set(new Date().getTime());
	}

	long getLastControlMessage() {
		return lastControlMessage.get();
	}

	void callChannelClientComplete(long sessionId) {
		if (TRACE_LOG_IN_INFO) {
			logger.info("callChannelClientComplete {}", sessionId);
		}
		getOrCreateSessionById(sessionId).callChannelClientComplete();
	}

	void callChannelClientException(long sessionId) {
		if (TRACE_LOG_IN_INFO) {
			logger.info("callChannelClientException {}", sessionId);
		}
		getOrCreateSessionById(sessionId).callChannelClientException();
	}

	void callChannelServerComplete(long sessionId) {
		if (TRACE_LOG_IN_INFO) {
			logger.info("callChannelServerComplete {}", sessionId);
		}
		getOrCreateSessionById(sessionId).callChannelServerComplete();
	}

	void callChannelServerException(long sessionId) {
		if (TRACE_LOG_IN_INFO) {
			logger.info("callChannelServerException {}", sessionId);
		}
		getOrCreateSessionById(sessionId).callChannelServerException();
	}

	String reportDetails() {
		final StringBuilder sb = new StringBuilder();
		if (TRACE_LOG_IN_INFO) {
			sb.append("\n--------------------\nDETAILS BeaconNetworkReceiver\n");
			sb.append("ROLE: " + myRoleMode + "\n");
			if (myRoleMode.equals(NetworkMode.CLIENT)) {
				sb.append("- clientChannelHandler -> " + networkReceiver.getClientChannelHandler().entrySet().stream()
						.map(n -> (n.getKey() + "["
								+ ((n.getValue().channel() != null) ? n.getValue().channel().isActive() : "disabled")
								+ "]"))
						.collect(Collectors.joining(", ")) + "\n");
			}
			if (myRoleMode.equals(NetworkMode.SERVER)) {
				sb.append("- serverChannelHandler -> " + networkReceiver.getServerChannelHandler().entrySet().stream()
						.map(n -> (n.getKey() + "[" + (n.getValue().isActive()) + "]"))
						.collect(Collectors.joining(", ")) + "\n");
				sb.append("- mainServerHandler -> " + ((networkReceiver.getMainServerHandler() != null
						&& networkReceiver.getMainServerHandler().channel() != null)
								? networkReceiver.getMainServerHandler().channel().isActive()
								: "disabled")
						+ "\n");
			}
			sb.append("- packetSemaphores ["
					+ getAllInputCachedMessages().size() + "] -> " + getAllInputCachedMessages().entrySet().stream()
							.map(n -> (n.getKey() + "[" + n.getValue() + "]")).collect(Collectors.joining(", "))
					+ "\n");
			sb.append("--------------------\n");
			if (!getAllInputCachedMessages().isEmpty()) {
				sb.append("INPUT MESSAGE IN CACHE\n");
				for (final MessageCached m : getAllInputCachedMessages().values()) {
					sb.append(". " + m.toString() + "\n");
				}
			}
			if (!getAllOutputCachedMessages().isEmpty()) {
				sb.append("OUTPUT MESSAGE IN CACHE\n");
				for (final MessageCached m : getAllOutputCachedMessages().values()) {
					sb.append(". " + m.toString() + "\n");
				}
			}
		}
		return sb.toString();
	}

}
