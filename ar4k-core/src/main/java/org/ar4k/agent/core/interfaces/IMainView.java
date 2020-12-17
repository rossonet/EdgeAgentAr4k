package org.ar4k.agent.core.interfaces;

import java.util.Collection;

public interface IMainView {

	Collection<IScadaAgent> getAllAgents();

	void hide();

	void addClientServer(IBeaconClientScadaWrapper beaconClientWrapper);

	Collection<IBeaconClientScadaWrapper> getBeaconServersList(String value);

}