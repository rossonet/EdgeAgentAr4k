package org.ar4k.agent.web.widget.agent;

import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.core.interfaces.AgentWebTab;
import org.ar4k.agent.core.interfaces.IScadaAgent;
import org.ar4k.agent.web.interfaces.AgentTab;

import com.flowingcode.vaadin.addons.xterm.ITerminalOptions.BellStyle;
import com.flowingcode.vaadin.addons.xterm.ITerminalOptions.CursorStyle;
import com.flowingcode.vaadin.addons.xterm.XTerm;
import com.flowingcode.vaadin.addons.xterm.XTermClipboard;
import com.flowingcode.vaadin.addons.xterm.XTermClipboard.UseSystemClipboard;
import com.flowingcode.vaadin.addons.xterm.XTermConsole;
import com.flowingcode.vaadin.addons.xterm.XTermConsole.LineEvent;
import com.flowingcode.vaadin.addons.xterm.XTermFit;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.DomEventListener;

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

	private IScadaAgent beaconAgentWrapper = null;
	private XTerm xterm = null;
	private XTermConsole console = null;

	private List<String> history = new ArrayList<String>();

	@Override
	public boolean isActive(IScadaAgent beaconAgentWrapper) {
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

	private void elaborateCommand(String line) {
		final String cleanedLine = line.replace(beaconAgentWrapper.getName() + " >", "");
		history.add(cleanedLine);
		if (!cleanedLine.isEmpty()) {
			xterm.writeln(beaconAgentWrapper.execCommand(cleanedLine.trim()));
		}
		xterm.write(getPrompt());
	}

	private void elaborateCompleteCommand(String line) {
		final String cleanedLine = line.replace(beaconAgentWrapper.getName() + " >", "");
		if (!cleanedLine.isEmpty()) {
			List<String> replies = beaconAgentWrapper.completeCommand(cleanedLine.trim());
			for (String rl : replies) {
				xterm.writeln(rl);
			}
			if (replies.size() == 1) {
				xterm.write(getPrompt() + replies.get(0));
			} else {
				if (replies.size() > 1) {
					int subCounter = 0;
					boolean compare = true;
					String baseLine = null;
					while (compare && subCounter < 1000) {
						baseLine = null;
						for (String s : replies) {
							if (baseLine == null) {
								baseLine = s.substring(0, subCounter);
							} else {
								if (!baseLine.equals(s.substring(0, subCounter))) {
									compare = false;
									break;
								}
							}
						}
						subCounter++;
					}
					xterm.write(getPrompt() + baseLine.substring(0, baseLine.length() - 1));
				} else {
					xterm.write(getPrompt());
				}
			}
		} else {
			xterm.write(getPrompt());
		}
	}

	@Override
	public Div getPage(IScadaAgent beaconAgentWrapper) {
		this.beaconAgentWrapper = beaconAgentWrapper;
		Div div = new Div();
		xterm = new XTerm();
		xterm.writeln(ANSI_RESET + "Type " + ANSI_RED + "help" + ANSI_RESET + " to list the available commands\n" + "\n"
				+ "You can use the " + ANSI_RED + "?" + ANSI_RESET + " for completation;\n" + "the " + ANSI_RED
				+ "CTRL-R" + ANSI_RESET + " to invoke the reverse-i-search in the history and\n"
				+ "you can run a sequence of commands saved in a file.\n\n");
		xterm.write(getPrompt());
		xterm.setCursorBlink(true);
		xterm.setCursorStyle(CursorStyle.UNDERLINE);
		xterm.setBellStyle(BellStyle.SOUND);
		xterm.setTabStopWidth(1);
		xterm.loadFeature(new XTermClipboard(), clipboard -> {
			clipboard.setCopySelection(true);
			clipboard.setUseSystemClipboard(UseSystemClipboard.READWRITE);
			clipboard.setPasteWithRightClick(true);
		});
		xterm.loadFeature(new XTermConsole(), termConsole -> {
			termConsole.addLineListener(new ComponentEventListener<XTermConsole.LineEvent>() {

				private static final long serialVersionUID = -3691030603955654942L;

				@Override
				public void onComponentEvent(LineEvent event) {
					final String line = event.getLine();
					if (line.contains("?")) {
						elaborateCompleteCommand(line);
					} else {
						elaborateCommand(line);
					}
				}
			});

		});

		DomEventListener listenerCtrlR = new DomEventListener() {

			private static final long serialVersionUID = 482487852683589453L;

			@Override
			public void handleEvent(DomEvent event) {
				for (String s : history) {
					xterm.writeln(s);
				}
			}
		};

		DomEventListener listenerArraowUp = new DomEventListener() {

			private static final long serialVersionUID = 482487857683589453L;

			@Override
			public void handleEvent(DomEvent event) {
				xterm.writeln("Freccia SU");

			}
		};

		DomEventListener listenerArrowDown = new DomEventListener() {

			private static final long serialVersionUID = 482497852683589453L;

			@Override
			public void handleEvent(DomEvent event) {
				xterm.writeln("Freccia GIU");

			}
		};
		xterm.addCustomKeyListener(listenerCtrlR, Key.KEY_R, KeyModifier.CONTROL);
		xterm.addCustomKeyListener(listenerArraowUp, Key.ARROW_UP);
		xterm.addCustomKeyListener(listenerArrowDown, Key.ARROW_DOWN);
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
