package org.ar4k.agent.farm.web.page;

import org.ar4k.agent.web.AgentWebTab;
import org.ar4k.agent.web.IScadaAgent;
import org.ar4k.agent.web.interfaces.AgentTab;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@AgentWebTab
public class ApplicationServerPage implements AgentTab {

	@Override
	public boolean isActive(IScadaAgent beaconAgentWrapper) {
		return false;
	}

	@Override
	public String getTabName() {
		return "DOCKER";
	}

	@Override
	public String getClassName() {
		return "console-docker";
	}

	@Override
	public Div getPage(IScadaAgent beaconAgentWrapper) {
		Div externalDiv = new Div();
		VerticalLayout div = new VerticalLayout();
		div.add(new Text("PROVIDES: " + beaconAgentWrapper.getProvides()));
		div.add(new Text("REQUIRED: " + beaconAgentWrapper.getRequired()));
		externalDiv.setWidth("95vw");
		externalDiv.setHeight("85vh");
		externalDiv.add(div);
		return externalDiv;
	}

	@Override
	public int getActivePriority() {
		return 650;
	}

	@Override
	public Integer getTabOrderNumber() {
		return 90;
	}

}
