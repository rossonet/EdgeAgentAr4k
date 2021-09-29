package org.ar4k.agent.web.interfaces;

import org.ar4k.agent.web.IScadaAgent;

import com.vaadin.flow.component.Component;

public interface AgentTab extends Comparable<AgentTab> {

	boolean isActive(IScadaAgent beaconAgentWrapper);

	String getTabName();

	String getClassName();

	Component getPage(IScadaAgent beaconAgentWrapper);

	int getActivePriority();

	Integer getTabOrderNumber();

	@Override
	public default int compareTo(AgentTab arg0) {
		return getTabOrderNumber().compareTo(getTabOrderNumber());
	}

}
