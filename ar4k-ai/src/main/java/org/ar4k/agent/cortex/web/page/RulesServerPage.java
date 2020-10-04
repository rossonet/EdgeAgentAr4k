package org.ar4k.agent.cortex.web.page;

import org.ar4k.agent.core.interfaces.AgentWebTab;
import org.ar4k.agent.core.interfaces.IScadaAgent;
import org.ar4k.agent.cortex.drools.DroolsConfig;
import org.ar4k.agent.web.interfaces.AgentTab;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@AgentWebTab
public class RulesServerPage implements AgentTab {

	@Override
	public boolean isActive(IScadaAgent beaconAgentWrapper) {
		if (beaconAgentWrapper != null && beaconAgentWrapper.getProvides() != null) {
			return beaconAgentWrapper.getProvides().contains(DroolsConfig.class.getCanonicalName());
		} else {
			return false;
		}
	}

	@Override
	public String getTabName() {
		return "RULES";
	}

	@Override
	public String getClassName() {
		return "console-rules";
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
		return 100;
	}

}
