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

  public RegisterRequest getRegisterRequest() {
    return registerRequest;
  }

  public RegisterReply getRegisterReply() {
    return registerReply;
  }

  public String getAgentUniqueName() {
    return registerReply.getRegisterCode();
  }

  public JSONObject getHardwareInfoAsJson() {
    return hardwareInfo;
  }

  public List<RequestToAgent> getCommandsToBeExecute() {
    List<RequestToAgent> r = new ArrayList<>();
    while (!cmdCalls.isEmpty()) {
      r.add(cmdCalls.poll());
    }
    lastCall = Instant.now();
    return r;
  }

  public void addRequestForAgent(RequestToAgent req) {
    cmdCalls.offer(req);
  }

  public int getPollingFrequency() {
    return registerReply.getMonitoringFrequency();
  }

  public long getTimestampRegistration() {
    return registerRequest.getTime().getSeconds();
  }

  @Override
  public void close() throws Exception {
    cmdCalls.clear();
  }

  public Instant getLastCall() {
    return lastCall;
  }

  public void setHardwareInfo(JSONObject hardwareInfo) {
    this.hardwareInfo = hardwareInfo;
  }

  @Override
  public String toString() {
    return "BeaconAgent [lastCall=" + lastCall + ", registerRequest=" + registerRequest + ", registerReply="
        + registerReply + "]";
  }

  public String getShortDescription() {
    return "agent id: " + getAgentUniqueName() + ", last contact: " + lastCall + ", commands queue: " + cmdCalls.size();
  }

}
