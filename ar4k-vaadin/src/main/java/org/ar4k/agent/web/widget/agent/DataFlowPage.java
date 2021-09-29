package org.ar4k.agent.web.widget.agent;

import org.ar4k.agent.web.AgentWebTab;
import org.ar4k.agent.web.IScadaAgent;
import org.ar4k.agent.web.interfaces.AgentTab;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;

@AgentWebTab
public class DataFlowPage implements AgentTab {

	@Override
	public boolean isActive(IScadaAgent beaconAgentWrapper) {
		return true;
	}

	@Override
	public String getTabName() {
		return "DATA FLOW";
	}

	@Override
	public String getClassName() {
		return "console-data";
	}

	@Override
	public Div getPage(IScadaAgent beaconAgentWrapper) {
		Div div = new Div();
		div.add(new Text("data console"));
		div.setWidth("95vw");
		div.setHeight("85vh");
		return div;
	}

	@Override
	public int getActivePriority() {
		return 800;
	}

	@Override
	public Integer getTabOrderNumber() {
		return 40;
	}

}
