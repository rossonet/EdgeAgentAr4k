package org.ar4k.agent.tunnels.http2.beacon;

import java.util.List;

import org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterReply;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.RequestToAgent;
import org.joda.time.Instant;
import org.json.JSONObject;

public interface IBeaconAgent extends AutoCloseable {

	void addRequestForAgent(RequestToAgent req);

	String getAgentUniqueName();

	List<RequestToAgent> getCommandsToBeExecute();

	JSONObject getHardwareInfoAsJson();

	Instant getLastCall();

	int getPollingFrequency();

	RegisterReply getRegisterReply();

	RegisterRequest getRegisterRequest();

	String getShortDescription();

	long getTimestampRegistration();

	void setHardwareInfo(JSONObject hardwareInfo);

}