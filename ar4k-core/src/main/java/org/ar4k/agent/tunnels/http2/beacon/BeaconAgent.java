package org.ar4k.agent.tunnels.http2.beacon;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RequestToAgent;
import org.joda.time.Instant;
import org.json.JSONObject;

public class BeaconAgent implements IBeaconAgent {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(BeaconAgent.class);

	private static final int queueSize = 50;

	private Instant lastCall = Instant.now();

	private final RegisterRequest registerRequest;
	private final RegisterReply registerReply;
	private final Queue<RequestToAgent> cmdCalls = new ArrayBlockingQueue<>(queueSize);

	private JSONObject hardwareInfo;

	public BeaconAgent(RegisterRequest registerRequest, RegisterReply registerReply) {
		this.registerRequest = registerRequest;
		this.registerReply = registerReply;
		try {
			this.hardwareInfo = new JSONObject(registerRequest.getJsonHealth());
		} catch (Exception a) {
			logger.logException(a);
		}
	}

	@Override
	public void addRequestForAgent(RequestToAgent req) {
		cmdCalls.offer(req);
	}

	@Override
	public void close() throws Exception {
		cmdCalls.clear();
	}

	@Override
	public String getAgentUniqueName() {
		return registerReply.getRegisterCode();
	}

	@Override
	public List<RequestToAgent> getCommandsToBeExecute() {
		List<RequestToAgent> r = new ArrayList<>();
		while (!cmdCalls.isEmpty()) {
			r.add(cmdCalls.poll());
		}
		lastCall = Instant.now();
		return r;
	}

	@Override
	public JSONObject getHardwareInfoAsJson() {
		return hardwareInfo;
	}

	@Override
	public Instant getLastCall() {
		return lastCall;
	}

	@Override
	public int getPollingFrequency() {
		return registerReply.getMonitoringFrequency();
	}

	@Override
	public RegisterReply getRegisterReply() {
		return registerReply;
	}

	@Override
	public RegisterRequest getRegisterRequest() {
		return registerRequest;
	}

	@Override
	public String getShortDescription() {
		return "agent id: " + getAgentUniqueName() + ", last contact: " + lastCall + ", commands queue: "
				+ cmdCalls.size();
	}

	@Override
	public long getTimestampRegistration() {
		return registerRequest.getTime().getSeconds();
	}

	@Override
	public void setHardwareInfo(JSONObject hardwareInfo) {
		this.hardwareInfo = hardwareInfo;
	}

	@Override
	public String toString() {
		return "BeaconAgent [lastCall=" + lastCall + ", registerRequest=" + registerRequest + ", registerReply="
				+ registerReply + "]";
	}

}
