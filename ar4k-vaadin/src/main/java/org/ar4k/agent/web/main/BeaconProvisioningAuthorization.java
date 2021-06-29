package org.ar4k.agent.web.main;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;

import org.ar4k.agent.core.interfaces.IBeaconProvisioningAuthorization;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http2.grpc.beacon.AgentRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

public class BeaconProvisioningAuthorization implements IBeaconProvisioningAuthorization {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder
			.getClassLogger(BeaconProvisioningAuthorization.class);

	private String idRequest;
	private String name;
	private String displayKey;
	private String shortDescription;
	private long registrationTimeRequest;
	private String requestCsr;
	private String jsonHealth;

	private boolean approved;

	private long approvedData;

	public BeaconProvisioningAuthorization(AgentRequest agentRequest) {
		this.idRequest = agentRequest.getIdRequest();
		this.displayKey = agentRequest.getRequest().getDisplayKey();
		this.jsonHealth = agentRequest.getRequest().getJsonHealth();
		this.name = agentRequest.getRequest().getName();
		this.requestCsr = agentRequest.getRequest().getRequestCsr();
		this.shortDescription = agentRequest.getRequest().getShortDescription();
		this.registrationTimeRequest = agentRequest.getRequest().getTime().getSeconds();
		this.approved = agentRequest.getApproved().getSeconds() != 0;
		this.approvedData = agentRequest.getApproved().getSeconds();
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
	public String getCsr() {
		return requestCsr;
	}

	@Override
	public String getJsonHealth() {
		return jsonHealth;
	}

	@Override
	public String getRegistrationTimeRequestString() {
		return new Date(registrationTimeRequest).toString();
	}

	@Override
	public String getCertificationDetails() {
		try {
			PKCS10CertificationRequest csrCert = new PKCS10CertificationRequest(Base64.getDecoder().decode(requestCsr));
			return csrCert.getSubject().toString();
		} catch (IOException exception) {
			logger.logException(exception);
			return "ERROR DECODING CERT -> " + exception.getMessage();
		}

	}

	@Override
	public boolean getApproved() {
		System.out.println("****** getApproved -> " + approved);
		return approved;
	}

	@Override
	public boolean isApproved() {
		System.out.println("****** isApproved -> " + approved);
		return approved;
	}

	@Override
	public long getApprovedData() {
		System.out.println("****** getApprovedData -> " + approvedData);
		return approvedData;
	}

	@Override
	public String getApprovedDataString() {
		return new Date(approvedData).toString();
	}

	@Override
	public String getRequestCsr() {
		return requestCsr;
	}

	@Override
	public void setIdRequest(String idRequest) {
		this.idRequest = idRequest;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setDisplayKey(String displayKey) {
		this.displayKey = displayKey;
	}

	@Override
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	@Override
	public void setRegistrationTimeRequest(long registrationTimeRequest) {
		this.registrationTimeRequest = registrationTimeRequest;
	}

	@Override
	public void setRequestCsr(String requestCsr) {
		this.requestCsr = requestCsr;
	}

	@Override
	public void setJsonHealth(String jsonHealth) {
		System.out.println("****** setJsonHealth " + jsonHealth);
		this.jsonHealth = jsonHealth;
	}

	@Override
	public void setApproved(boolean approved) {
		System.out.println("****** setApproved " + approved);
		this.approved = approved;
	}

	@Override
	public void setApprovedData(long approvedData) {
		System.out.println("****** setApprovedData " + approvedData);
		this.approvedData = approvedData;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BeaconProvisioningAuthorization [idRequest=");
		builder.append(idRequest);
		builder.append(", name=");
		builder.append(name);
		builder.append(", displayKey=");
		builder.append(displayKey);
		builder.append(", shortDescription=");
		builder.append(shortDescription);
		builder.append(", registrationTimeRequest=");
		builder.append(registrationTimeRequest);
		builder.append(", requestCsr=");
		builder.append(requestCsr);
		builder.append(", jsonHealth=");
		builder.append(jsonHealth);
		builder.append(", approved=");
		builder.append(approved);
		builder.append(", approvedData=");
		builder.append(approvedData);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public void setRegistrationTimeRequestString(String notUsed) {
		System.out.println("****** setRegistrationTimeRequestString " + notUsed);
		// not used

	}

	@Override
	public void setCsr(String notUsed) {
		System.out.println("****** setCsr " + notUsed);
		// not used

	}

	@Override
	public void setApprovedDataString(String notUsed) {
		System.out.println("****** setApprovedDataString " + notUsed);
		// not used

	}

	@Override
	public void setCertificationDetails(String notUsed) {
		System.out.println("****** setCertificationDetails " + notUsed);
		// not used

	}

}
