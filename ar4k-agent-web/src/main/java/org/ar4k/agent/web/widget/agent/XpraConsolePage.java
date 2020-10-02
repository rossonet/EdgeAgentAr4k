package org.ar4k.agent.web.widget.agent;

import org.ar4k.agent.design.AgentTab;
import org.ar4k.agent.design.AgentWebTab;
import org.ar4k.agent.web.scada.ScadaAgentWrapper;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;

@AgentWebTab
public class XpraConsolePage implements AgentTab {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	private ScadaAgentWrapper beaconAgentWrapper = null;

	@Override
	public boolean isActive() {
		return true;
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
	public Div getPage(ScadaAgentWrapper beaconAgentWrapper) {
		this.beaconAgentWrapper = beaconAgentWrapper;
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
