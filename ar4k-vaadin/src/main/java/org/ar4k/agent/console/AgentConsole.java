package org.ar4k.agent.console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ar4k.agent.core.EdgeAgentCore;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;

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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.DomEventListener;

public class AgentConsole extends VerticalLayout {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	private static final long serialVersionUID = 5471247263204515469L;

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(AgentConsole.class);
	private static final CharSequence COMPLETION_CHAR = "?";
	private static final String WEB_CONSOLE_SESSION = "WEB_SESSION_CONSOLE";

	private Homunculus me = EdgeAgentCore.getApplicationContextStatic().getBean(EdgeAgentCore.class);

	private XTerm xterm = null;

	private List<String> history = new ArrayList<String>();

	public AgentConsole() {
		add(getMainDiv());
		me.getHomunculusSession().registerNewSession(WEB_CONSOLE_SESSION, WEB_CONSOLE_SESSION);
		logger.debug("local console started for web");
	}

	private String getPrompt() {
		return ANSI_RED + me.getAgentUniqueName() + ANSI_GREEN + " > " + ANSI_RESET;
	}

	private Div getMainDiv() {
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

	private void elaborateCompleteCommand(String line) {
		final String cleanedLine = line.replace(me.getAgentUniqueName() + " >", "");
		if (!cleanedLine.isEmpty()) {
			List<String> replies = completeCommand(cleanedLine.trim());
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

	private List<String> completeCommand(String command) {
		final List<String> m = Arrays.asList(StringUtils.split(command));
		final List<String> clean = new ArrayList<>(m.size());
		int pos = 0;
		int word = 0;
		int counter = 0;
		for (final String p : m) {
			if (p.contains(COMPLETION_CHAR)) {
				word = counter;
				pos = p.indexOf(COMPLETION_CHAR.toString());
				if (!p.equals(COMPLETION_CHAR.toString())) {
					// System.out.println("add " + p.replace(COMPLETION_CHAR, ""));
					clean.add(p.replace(COMPLETION_CHAR, ""));
				} else {
					// System.out.println("add " + p);
					clean.add(p);
				}
			} else {
				clean.add(p);
			}
			counter++;
		}
		final CompletionContext context = new CompletionContext(clean, word, pos);
		final List<CompletionProposal> listCompletionProposal = me.getRpc(WEB_CONSOLE_SESSION).complete(context);
		final List<String> result = new ArrayList<>();
		for (CompletionProposal s : listCompletionProposal) {
			result.add(s.displayText());
		}
		return result;
	}

	private void elaborateCommand(String line) {
		final String cleanedLine = line.replace(me.getAgentUniqueName() + " >", "");
		history.add(cleanedLine);
		if (!cleanedLine.isEmpty()) {
			xterm.writeln(execCommand(cleanedLine.trim()));
		}
		xterm.write(getPrompt());
	}

	private String execCommand(String command) {
		return me.getRpc(WEB_CONSOLE_SESSION).elaborateMessage(command);
	}
}
