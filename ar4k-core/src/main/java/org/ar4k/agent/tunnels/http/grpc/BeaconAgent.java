package org.ar4k.agent.tunnels.http.grpc;

import org.ar4k.agent.tunnels.http.grpc.beacon.RegisterReply;
import org.ar4k.agent.tunnels.http.grpc.beacon.RegisterRequest;
import org.json.JSONObject;

public class BeaconAgent {

  private final RegisterRequest registerRequest;
  private final RegisterReply registerReply;

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

  public String getSecretKey() {
    return registerRequest.getSecretKey();
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
        + getHardwareInfoAsJson().toString(2) + ", getSecretKey()=" + getSecretKey() + ", getPollingFrequency()="
        + getPollingFrequency() + ", getTimestampRegistration()=" + getTimestampRegistration() + "]";
  }

}
