package org.ar4k.agent.core.interfaces;

public interface IBeaconProvisioningAuthorization {

	boolean isFoundBy(String filter);

	String getIdRequest();

	String getName();

	String getDisplayKey();

	String getShortDescription();

	long getRegistrationTimeRequest();

	String getRequestCsr();

	String getJsonHealth();

}
