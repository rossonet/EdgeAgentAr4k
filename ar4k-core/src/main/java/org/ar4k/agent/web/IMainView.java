package org.ar4k.agent.web;

import java.util.Collection;

import org.ar4k.agent.tunnels.http2.beacon.IBeaconClientScadaWrapper;

public interface IMainView {

	Collection<IScadaAgent> getAllAgents();

	void hide();

	void addClientServer(IBeaconClientScadaWrapper beaconClientWrapper);

	Collection<IBeaconClientScadaWrapper> getBeaconServersList(String value);

	Collection<IBeaconProvisioningAuthorization> getProvisioningAuthorizationList(String value);

	void approveProvisioningRequest(IBeaconProvisioningAuthorization beaconProvisioning);

}