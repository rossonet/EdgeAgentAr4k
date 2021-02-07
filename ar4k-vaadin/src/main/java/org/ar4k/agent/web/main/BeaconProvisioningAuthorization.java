package org.ar4k.agent.web.main;

import org.ar4k.agent.core.interfaces.IBeaconProvisioningAuthorization;
import org.ar4k.agent.tunnels.http2.grpc.beacon.AgentRequest;

public class BeaconProvisioningAuthorization implements IBeaconProvisioningAuthorization {

	private final String idRequest;
	private final String name;
	private final String displayKey;
	private final String shortDescription;
	private final long registrationTimeRequest;
	private final String requestCsr;
	private final String jsonHealth;

	public BeaconProvisioningAuthorization(AgentRequest agentRequest) {
		this.idRequest = agentRequest.getIdRequest();
		this.displayKey = agentRequest.getRequest().getDisplayKey();
		this.jsonHealth = agentRequest.getRequest().getJsonHealth();
		this.name = agentRequest.getRequest().getName();
		this.requestCsr = agentRequest.getRequest().getRequestCsr();
		this.shortDescription = agentRequest.getRequest().getShortDescription();
		this.registrationTimeRequest = agentRequest.getRequest().getTime().getSeconds();
	}

	@Override
	public boolean isFoundBy(String filter) {
		return name.contains(filter) || shortDescription.contains(filter) || displayKey.contains(filter);
	}

	@Override
	public String getIdRequest() {
		return idRequest;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDisplayKey() {
		return displayKey;
	}

	@Override
	public String getShortDescription() {
		return shortDescription;
	}

	@Override
	public long getRegistrationTimeRequest() {
		return registrationTimeRequest;
	}

	@Override
	public String getRequestCsr() {
		return requestCsr;
	}

	@Override
	public String getJsonHealth() {
		return jsonHealth;
	}

}
