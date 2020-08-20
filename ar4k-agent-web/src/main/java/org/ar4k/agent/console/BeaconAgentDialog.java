package org.ar4k.agent.console;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.web.scada.ScadaAgentWrapper;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

public class BeaconAgentDialog extends Dialog implements AutoCloseable {

	@SuppressWarnings("unused")
	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconAgentDialog.class.toString());

	private static final long serialVersionUID = -7889305640288767762L;

	@SuppressWarnings("unused")
	private final MainView mainView;

	private final ScadaAgentWrapper beaconAgentWrapper;

	private Div pageDashboard;

	private Div pageXpra;

	private Div pageCommands;

	public BeaconAgentDialog(MainView mainView, ScadaAgentWrapper beaconAgent) {
		beaconAgentWrapper = beaconAgent;
		this.mainView = mainView;
		add(getMultiTab());
		setResizable(true);
		setCloseOnEsc(true);
		setCloseOnOutsideClick(true);
		setWidthFull();
		setHeightFull();
		startingMonitoring();
	}

	private void startingMonitoring() {
		logger.info("start monitoring " + beaconAgentWrapper.getName());
		activateXpraConsole();
	}

	private void activateXpraConsole() {
		beaconAgentWrapper.activateXpraConnection();

	}

	@Override
	public void close() {
		super.close();
		stopMonitoring();
	}

	private void stopMonitoring() {
		logger.info("stop monitoring " + beaconAgentWrapper.getName());
		beaconAgentWrapper.sendDisconnectToXpraConnection();
		// TODO fine procedura monitoring attivo

	}

	private Component getMultiTab() {
		final Tab homeTab = new Tab("General data");
		homeTab.setClassName("primary");
		pageDashboard = new Div();
		pageDashboard.setText("data");

		final Tab xpraTab = new Tab("Console");
		pageXpra = new Div();
		pageXpra.setText("Page#2");
		pageXpra.setVisible(false);

		final Tab commandsTab = new Tab("RPC Console");
		pageCommands = new Div();
		pageCommands.setText("Page#3");
		pageCommands.setVisible(false);

		final Map<Tab, Component> tabsToPages = new HashMap<>();
		tabsToPages.put(homeTab, pageDashboard);
		tabsToPages.put(xpraTab, pageXpra);
		tabsToPages.put(commandsTab, pageCommands);
		final Tabs tabs = new Tabs(homeTab, xpraTab, commandsTab);
		new Div(pageDashboard, pageXpra, pageCommands);
		final Set<Component> pagesShown = Stream.of(pageDashboard).collect(Collectors.toSet());

		tabs.addSelectedChangeListener(event -> {
			pagesShown.forEach(page -> page.setVisible(false));
			pagesShown.clear();
			final Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
			selectedPage.setVisible(true);
			pagesShown.add(selectedPage);
		});
		return tabs;
	}

	public ScadaAgentWrapper getAgent() {
		return beaconAgentWrapper;
	}

}
