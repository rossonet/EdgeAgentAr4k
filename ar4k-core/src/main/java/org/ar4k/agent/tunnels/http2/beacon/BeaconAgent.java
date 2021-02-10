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

//TODO Beacon Agent to Agent per unire due server Beacon in modalità distribuita e cluster ( namespace unico? )
//TODO Beacon Agent to Agent deve avere funzioni di gestione della sicurezza dei dati sottoscritti con logica sui certificati
// unisce oggetti con livelli di sicurezza pari o diversa in termine di utilizzo dei comandi (ShellScript) e dati.
// attenzione al deny of service. Ci deve essere un controllo sulla frequenza di sottoscrizione e di esecuzione dei comandi
public class BeaconAgent implements AutoCloseable {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconAgent.class.toString());

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

	public void addRequestForAgent(RequestToAgent req) {
		cmdCalls.offer(req);
	}

	@Override
	public void close() throws Exception {
		cmdCalls.clear();
	}

	public String getAgentUniqueName() {
		return registerReply.getRegisterCode();
	}

	public List<RequestToAgent> getCommandsToBeExecute() {
		List<RequestToAgent> r = new ArrayList<>();
		while (!cmdCalls.isEmpty()) {
			r.add(cmdCalls.poll());
		}
		lastCall = Instant.now();
		return r;
	}

	public JSONObject getHardwareInfoAsJson() {
		return hardwareInfo;
	}

	public Instant getLastCall() {
		return lastCall;
	}

	public int getPollingFrequency() {
		return registerReply.getMonitoringFrequency();
	}

	public RegisterReply getRegisterReply() {
		return registerReply;
	}

	public RegisterRequest getRegisterRequest() {
		return registerRequest;
	}

	public String getShortDescription() {
		return "agent id: " + getAgentUniqueName() + ", last contact: " + lastCall + ", commands queue: "
				+ cmdCalls.size();
	}

	public long getTimestampRegistration() {
		return registerRequest.getTime().getSeconds();
	}

	public void setHardwareInfo(JSONObject hardwareInfo) {
		this.hardwareInfo = hardwareInfo;
	}

	@Override
	public String toString() {
		return "BeaconAgent [lastCall=" + lastCall + ", registerRequest=" + registerRequest + ", registerReply="
				+ registerReply + "]";
	}

}
