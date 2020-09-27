package org.ar4k.agent.web.widget.agent;

import org.ar4k.agent.design.AgentTab;
import org.ar4k.agent.design.AgentWebTab;
import org.ar4k.agent.web.scada.ScadaAgentWrapper;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;

@AgentWebTab
public class AgentStatusPage implements AgentTab {

	private ScadaAgentWrapper beaconAgentWrapper = null;

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public String getTabName() {
		return "STATUS";
	}

	@Override
	public String getClassName() {
		return "tab-status";
	}

	@Override
	public Div getPage(ScadaAgentWrapper beaconAgentWrapper) {
		Div div = new Div();
		this.beaconAgentWrapper = beaconAgentWrapper;
		div.add(new TextField("STATUS " + this.beaconAgentWrapper.getName()));
		div.setSizeFull();
		return div;
	}

	@Override
	public int getActivePriority() {
		return 10000;
	}

	@Override
	public int compareTo(AgentTab arg0) {
		return getTabOrderNumber().compareTo(arg0.getTabOrderNumber());
	}

	@Override
	public Integer getTabOrderNumber() {
		return 10;
	}

}
