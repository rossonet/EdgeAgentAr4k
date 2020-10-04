package org.ar4k.agent.web.widget.agent;

import org.ar4k.agent.core.interfaces.AgentWebTab;
import org.ar4k.agent.core.interfaces.IScadaAgent;
import org.ar4k.agent.rpc.process.xpra.XpraConfig;
import org.ar4k.agent.web.interfaces.AgentTab;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;

@AgentWebTab
public class XpraConsolePage implements AgentTab {

	@Override
	public boolean isActive(IScadaAgent beaconAgentWrapper) {
		if (beaconAgentWrapper != null && beaconAgentWrapper.getProvides() != null) {
			return beaconAgentWrapper.getProvides().contains(XpraConfig.class.getCanonicalName());
		} else {
			return false;
		}
	}

	@Override
	public String getTabName() {
		return "XPRA";
	}

	@Override
	public String getClassName() {
		return "console-xpra";
	}

	@Override
	public Div getPage(IScadaAgent beaconAgentWrapper) {
		Div div = new Div();
		div.add(new Text("xpra console"));
		div.setWidth("95vw");
		div.setHeight("85vh");
		return div;
	}

	@Override
	public int getActivePriority() {
		return 900;
	}

	@Override
	public Integer getTabOrderNumber() {
		return 2000;
	}

}
