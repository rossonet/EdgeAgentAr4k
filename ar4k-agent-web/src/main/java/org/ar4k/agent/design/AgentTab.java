package org.ar4k.agent.design;

import org.ar4k.agent.web.scada.ScadaAgentWrapper;

import com.vaadin.flow.component.html.Div;

public interface AgentTab extends Comparable<AgentTab> {

	boolean isActive();

	String getTabName();

	String getClassName();

	Div getPage(ScadaAgentWrapper beaconAgentWrapper);

	int getActivePriority();

	Integer getTabOrderNumber();

	@Override
	public default int compareTo(AgentTab arg0) {
		return getTabOrderNumber().compareTo(getTabOrderNumber());
	}

}
