package org.ar4k.agent.tunnels.http2.beacon;

import java.util.Date;
import java.util.UUID;

import org.ar4k.agent.tunnels.http2.grpc.beacon.RegisterRequest;
import org.ar4k.agent.tunnels.http2.grpc.beacon.Timestamp;

public class RegistrationRequest {
	public final String idRequest = UUID.randomUUID().toString();
	public String idAuth = null;
	public final String name;
	public final String requestCsr;
	public final String consoleKey;
	public String targetAlias = null;
	public final String jsonHealth;
	public boolean approved = false;
	public Date lastCall = new Date();
	public final Timestamp time;
	public Timestamp approvedDate = null;
	public Timestamp completed = null;
	public String pemApproved = null;
	public final String shortDescription;
	public String note = null;

	public RegistrationRequest(RegisterRequest request) {
		consoleKey = request.getDisplayKey();
		jsonHealth = request.getJsonHealth();
		name = request.getName();
		requestCsr = request.getRequestCsr();
		shortDescription = request.getShortDescription();
		time = request.getTime();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || obj.getClass() != this.getClass())
			return false;
		RegistrationRequest geek = (RegistrationRequest) obj;
		return (geek.name == this.name && geek.requestCsr == this.requestCsr && geek.consoleKey == this.consoleKey);
	}

	public RegisterRequest getRegisterRequest() {
		return RegisterRequest.newBuilder().setDisplayKey(consoleKey).setJsonHealth(jsonHealth).setName(name)
				.setRequestCsr(requestCsr).setTime(time).build();
	}

}
