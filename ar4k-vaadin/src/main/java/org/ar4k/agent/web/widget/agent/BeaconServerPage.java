package org.ar4k.agent.web.widget.agent;

import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.core.interfaces.AgentWebTab;
import org.ar4k.agent.core.interfaces.IScadaAgent;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http2.beacon.BeaconServiceConfig;
import org.ar4k.agent.web.interfaces.AgentTab;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;

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
		Grid<StatusProvidesRequiredLineString> grid = new Grid<>(StatusProvidesRequiredLineString.class);
		grid.getStyle().set("position", "absolute");
		List<StatusProvidesRequiredLineString> lines = new ArrayList<>();
		for (String provider : beaconAgentWrapper.getProvides()) {
			lines.add(new StatusProvidesRequiredLineString(provider, true));
		}
		for (String requirement : beaconAgentWrapper.getRequired()) {
			lines.add(new StatusProvidesRequiredLineString(requirement, false));
		}
		grid.setItems(lines);
		grid.setMaxWidth("95vw");
		grid.setMaxHeight("85vh");
		grid.setSizeFull();
		grid.getColumns().forEach(col -> col.setAutoWidth(true));
		externalDiv.add(grid);
		externalDiv.setWidth("95vw");
		externalDiv.setHeight("85vh");
		externalDiv.setSizeFull();
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

	public final class StatusProvidesRequiredLineString {
		private final String provider;
		private final String requirement;

		public StatusProvidesRequiredLineString(String function, boolean provider) {
			if (provider) {
				this.provider = function;
				this.requirement = "";
			} else {
				this.provider = "";
				this.requirement = function;
			}
		}

		public String getRequirement() {
			return requirement;
		}

		public String getProvider() {
			return provider;
		}

	}

}
