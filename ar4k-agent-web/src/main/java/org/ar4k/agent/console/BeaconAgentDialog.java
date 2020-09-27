package org.ar4k.agent.console;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ar4k.agent.console.AgentTabTemplate.ClickTabEvent;
import org.ar4k.agent.design.AgentTab;
import org.ar4k.agent.design.AgentWebTab;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.web.scada.ScadaAgentWrapper;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

public class BeaconAgentDialog extends Dialog implements AutoCloseable {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconAgentDialog.class.toString());

	private static final long serialVersionUID = -7889305640288767762L;

	@SuppressWarnings("unused")
	private final MainView mainView;

	private final ScadaAgentWrapper beaconAgentWrapper;

	private final Map<Tab, Component> tabsToPages = new HashMap<>();

	private Component selectedComponent = null;

	final Tabs tabsComponent = new Tabs();

	public BeaconAgentDialog(MainView mainView, ScadaAgentWrapper beaconAgent) {
		Div divComponent = new Div();
		beaconAgentWrapper = beaconAgent;
		this.mainView = mainView;
		divComponent.getStyle().set("padding", "6px");
		int selected = 0;
		tabsComponent.setAutoselect(false);
		add(tabsComponent);
		try {
			selected = getMultiTab();
			for (Component p : tabsToPages.values()) {
				divComponent.add(p);
			}
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.logException(e);
		}
		add(divComponent);
		tabsComponent.setSelectedIndex(selected);
		setResizable(true);
		setCloseOnEsc(true);
		setCloseOnOutsideClick(true);
		setMaxWidth("96vw");
		setSizeFull();
		startingMonitoring();
	}

	private void startingMonitoring() {
		logger.info("start monitoring " + beaconAgentWrapper.getName());
	}

	@Override
	public void close() {
		super.close();
		stopMonitoring();
	}

	private void stopMonitoring() {
		logger.info("stop monitoring " + beaconAgentWrapper.getName());
	}

	private int getMultiTab() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int actualPriority = 0;
		List<AgentTab> agentTabsTarget = getAgentTabs();
		Collections.sort(agentTabsTarget);
		int tabIndex = 0;
		int tabIndexSelected = 0;
		for (AgentTab tab : agentTabsTarget) {
			final AgentTabTemplate t = new AgentTabTemplate(tab.getTabName(), tabIndex);
			if (tab.getClassName() != null)
				t.setClassName(tab.getClassName());
			final Div page = tab.getPage(beaconAgentWrapper);
			if (tab.getActivePriority() > actualPriority) {
				selectedComponent = page;
				tabIndexSelected = tabIndex;
				selectedComponent.setVisible(true);
				actualPriority = tab.getActivePriority();
				// logger.info("page started " + page + " priority " + tab.getActivePriority());
			} else {
				page.setVisible(false);
			}
			t.addClickListener(new ComponentEventListener<ClickTabEvent>() {

				private static final long serialVersionUID = -7383189338139138484L;

				@Override
				public void onComponentEvent(ClickTabEvent event) {
					// logger.info("clicked page " + event.getIndex());
					selectedComponent.setVisible(false);
					selectedComponent = tabsToPages.get(event.getSource());
					selectedComponent.setVisible(true);
					tabsComponent.setSelectedIndex(event.getIndex());
				}

			});
			tabsToPages.put(t, page);
			t.setEnabled(true);
			tabsComponent.addComponentAtIndex(tabIndex, t);
			tabIndex++;
		}
		return tabIndexSelected;
	}

	private List<AgentTab> getAgentTabs() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<AgentTab> annotatedTabs = getAnnotatedTabs(MainView.PACKET_SEARCH_BASE);
		List<AgentTab> finalTabs = new ArrayList<>();
		for (AgentTab a : annotatedTabs) {
			if (a.isActive()) {
				finalTabs.add(a);
			}
		}
		return finalTabs;
	}

	private List<AgentTab> getAnnotatedTabs(String packageBaseSearch)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
				false);
		provider.addIncludeFilter(new AnnotationTypeFilter(AgentWebTab.class));
		final Set<BeanDefinition> classes = provider.findCandidateComponents(packageBaseSearch);
		final List<AgentTab> rit = new ArrayList<>();
		for (BeanDefinition c : classes) {
			final String classTarget = c.getBeanClassName();
			rit.add(createAgentTab(classTarget));
		}
		return rit;
	}

	private AgentTab createAgentTab(String classTarget)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final Class<? extends AgentTab> clazz = (Class<? extends AgentTab>) Class.forName(classTarget);
		final Constructor<? extends AgentTab> constructor = clazz.getConstructor();
		return constructor.newInstance();
	}

	public ScadaAgentWrapper getAgent() {
		return beaconAgentWrapper;
	}

}
