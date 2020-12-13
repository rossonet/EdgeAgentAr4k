package org.ar4k.agent.cortex.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.core.interfaces.AgentWebMenu;
import org.ar4k.agent.core.interfaces.IMainView;
import org.ar4k.agent.core.interfaces.IScadaAgent;
import org.ar4k.agent.cortex.drools.DroolsConfig;
import org.ar4k.agent.web.interfaces.AgentMenu;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.dom.Style;

@AgentWebMenu
public class KieWorkbenchMenu implements AgentMenu {

	private IMainView mainView = null;

	private IFrame ideMenu = new IFrame();

	@Override
	public void setMainView(IMainView mainView) {
		this.mainView = mainView;
		ideMenu.setSrc("https://www.rossonet.net/");
		Style style = ideMenu.getStyle();
		style.set("padding", "3px");
		style.set("border", "0px");
		style.set("overflow", "hidden");
		ideMenu.setSizeFull();
	}

	@Override
	public boolean isActive() {
		boolean result = false;
		for (IScadaAgent beaconAgentWrapper : mainView.getAllAgents()) {
			if (beaconAgentWrapper.getProvides().contains(DroolsConfig.class.getCanonicalName())) {
				result = true;
				break;
			}
		}
		return result;
	}

	@Override
	public void addMenuWidget(MenuBar menuBar) {
		menuBar.addItem("RULES IDE", e -> showIdeMenu());
	}

	private void showIdeMenu() {
		mainView.hide();
		ideMenu.setVisible(true);
	}

	@Override
	public void setVisibleTrue() {
		ideMenu.setVisible(true);
	}

	@Override
	public List<Component> getLayots() {
		List<Component> l = new ArrayList<>();
		l.add(ideMenu);
		return l;
	}

	public IMainView getMainView() {
		return mainView;
	}

	@Override
	public String toString() {
		return "Kie Workbench (Drools IDE) Menù";
	}

	@Override
	public Integer getMenuOrderNumber() {
		return 2400;
	}

}
