package org.ar4k.agent.web.widget.agent;

import org.ar4k.agent.core.interfaces.AgentWebTab;
import org.ar4k.agent.core.interfaces.IScadaAgent;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http2.beacon.BeaconServiceConfig;
import org.ar4k.agent.web.interfaces.AgentTab;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@AgentWebTab
public class BeaconServerPage implements AgentTab {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconServerPage.class.toString());

	@Override
	public boolean isActive(IScadaAgent beaconAgentWrapper) {
		if (beaconAgentWrapper != null && beaconAgentWrapper.getProvides() != null) {
			return beaconAgentWrapper.getProvides().contains(BeaconServiceConfig.class.getCanonicalName());
		} else {
			return false;
		}
	}

	@Override
	public String getTabName() {
		return "BEACON SERVER";
	}

	@Override
	public String getClassName() {
		return "console-beacon-server";
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
		return 600;
	}

	@Override
	public Integer getTabOrderNumber() {
		return 20;
	}

}
