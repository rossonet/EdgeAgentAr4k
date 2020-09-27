package org.ar4k.agent.web.widget.agent;

import org.ar4k.agent.design.AgentTab;
import org.ar4k.agent.design.AgentWebTab;
import org.ar4k.agent.web.scada.ScadaAgentWrapper;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;

@AgentWebTab
public class ServiceStatusPage implements AgentTab {

	private ScadaAgentWrapper beaconAgentWrapper = null;

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public String getTabName() {
		return "SERVICES";
	}

	@Override
	public String getClassName() {
		return "service-status";
	}

	@Override
	public Div getPage(ScadaAgentWrapper beaconAgentWrapper) {
		this.beaconAgentWrapper = beaconAgentWrapper;
		Div div = new Div();
		div.add(new TextField("SERVICE " + this.beaconAgentWrapper.getName()));
		div.setSizeFull();
		return div;
	}

	@Override
	public int getActivePriority() {
		return 9000;
	}

	@Override
	public Integer getTabOrderNumber() {
		return 1000;
	}

}
