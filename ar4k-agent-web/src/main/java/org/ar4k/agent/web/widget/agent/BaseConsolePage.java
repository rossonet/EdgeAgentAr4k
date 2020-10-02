package org.ar4k.agent.web.widget.agent;

import org.ar4k.agent.design.AgentTab;
import org.ar4k.agent.design.AgentWebTab;
import org.ar4k.agent.web.scada.ScadaAgentWrapper;

import com.flowingcode.vaadin.addons.xterm.ITerminalOptions.BellStyle;
import com.flowingcode.vaadin.addons.xterm.ITerminalOptions.CursorStyle;
import com.flowingcode.vaadin.addons.xterm.XTerm;
import com.flowingcode.vaadin.addons.xterm.XTermClipboard;
import com.flowingcode.vaadin.addons.xterm.XTermClipboard.UseSystemClipboard;
import com.flowingcode.vaadin.addons.xterm.XTermFit;
import com.vaadin.flow.component.html.Div;

@AgentWebTab
public class BaseConsolePage implements AgentTab {

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
		return "CONSOLE";
	}

	@Override
	public String getClassName() {
		return "console-status";
	}

	@Override
	public Div getPage(ScadaAgentWrapper beaconAgentWrapper) {
		this.beaconAgentWrapper = beaconAgentWrapper;
		Div div = new Div();
		XTerm xterm = new XTerm();
		xterm.writeln(ANSI_RESET + "Type " + ANSI_RED + "help" + ANSI_RESET + " to list the available commands\n" + "\n"
				+ "You can use the " + ANSI_RED + "TAB" + ANSI_RESET + " completation;\n" + "the " + ANSI_RED + "CTRL-R"
				+ ANSI_RESET + " to invoke the reverse-i-search in the history and\n"
				+ "you can run a sequence of commands saved in a file.\n\n");
		xterm.write(getPrompt());
		xterm.setCursorBlink(true);
		xterm.setCursorStyle(CursorStyle.BLOCK);
		xterm.setBellStyle(BellStyle.SOUND);
		xterm.loadFeature(new XTermClipboard(), clipboard -> {
			clipboard.setCopySelection(true);
			clipboard.setUseSystemClipboard(UseSystemClipboard.READWRITE);
			clipboard.setPasteWithRightClick(true);
		});
		xterm.focus();
		xterm.getFeature(XTermFit.class).ifPresent(fit -> {
			fit.fit();
		});
		// xterm.setHeight("90vh");
		div.add(xterm);
		div.setWidth("95vw");
		div.setHeight("85vh");
		xterm.setSizeFull();
		return div;
	}

	private String getPrompt() {
		return ANSI_RED + beaconAgentWrapper.getName() + ANSI_GREEN + " > " + ANSI_RESET;
	}

	@Override
	public int getActivePriority() {
		return 1000;
	}

	@Override
	public Integer getTabOrderNumber() {
		return 1000;
	}

}
