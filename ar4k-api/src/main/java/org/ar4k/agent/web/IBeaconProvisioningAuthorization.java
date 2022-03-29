package org.ar4k.agent.web;

public interface IBeaconProvisioningAuthorization {

	boolean isFoundBy(String filter);

	String getIdRequest();

	String getName();

	String getDisplayKey();

	boolean getApproved();

	String getShortDescription();

	long getRegistrationTimeRequest();

	String getRegistrationTimeRequestString();

	String getCsr();

	String getCertificationDetails();

	String getJsonHealth();

	// TODO sistemare il feedback di approved
	long getApprovedData();

	String getRequestCsr();

	void setIdRequest(String idRequest);

	void setName(String name);

	void setDisplayKey(String displayKey);

	void setShortDescription(String shortDescription);

	void setRegistrationTimeRequest(long registrationTimeRequest);

	void setRequestCsr(String requestCsr);

	void setJsonHealth(String jsonHealth);

	void setApproved(boolean approved);

	void setApprovedData(long approvedData);

	boolean isApproved();

	String getApprovedDataString();

	void setRegistrationTimeRequestString(String notUsed);

	void setCsr(String notUsed);

	void setApprovedDataString(String notUsed);

	void setCertificationDetails(String notUsed);

}
