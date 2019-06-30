package org.ar4k.agent.tunnels.http.grpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest;
import org.ar4k.agent.tunnels.http.grpc.beacon.RequestToAgent;
import org.json.JSONObject;

public class BeaconAgent {

  private static final int queueSize = 50;

  private final RegisterRequest registerRequest;
  private final RegisterReply registerReply;
  private final Queue<RequestToAgent> cmdCalls = new ArrayBlockingQueue<>(queueSize);

  public BeaconAgent(RegisterRequest registerRequest, RegisterReply registerReply) {
    this.registerRequest = registerRequest;
    this.registerReply = registerReply;
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
    return new JSONObject(registerRequest.getJsonHealth());
  }

  public List<RequestToAgent> getCommandsToBeExecute() {
    List<RequestToAgent> r = new ArrayList<>();
    while (!cmdCalls.isEmpty()) {
      r.add(cmdCalls.poll());
    }
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
  public String toString() {
    return "BeaconAgent [getAgentUniqueName()=" + getAgentUniqueName() + ", getHardwareInfoAsJson()="
        + getHardwareInfoAsJson().toString(2) + ", getPollingFrequency()=" + getPollingFrequency()
        + ", getTimestampRegistration()=" + getTimestampRegistration() + "]";
  }

}
