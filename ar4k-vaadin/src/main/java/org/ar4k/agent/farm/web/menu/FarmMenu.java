package org.ar4k.agent.farm.web.menu;

import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.core.interfaces.AgentWebMenu;
import org.ar4k.agent.core.interfaces.IMainView;
import org.ar4k.agent.web.interfaces.AgentMenu;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.dom.Style;

@AgentWebMenu
public class FarmMenu implements AgentMenu {

	private IMainView mainView = null;

	private IFrame seedManagerMenu = new IFrame();

	@Override
	public void setMainView(IMainView mainView) {
		this.mainView = mainView;
		seedManagerMenu.setSrc("https://www.rossonet.net/");
		Style style = seedManagerMenu.getStyle();
		style.set("padding", "3px");
		style.set("border", "0px");
		style.set("overflow", "hidden");
		seedManagerMenu.setSizeFull();
	}

	@Override
	public boolean isActive() {
		boolean result = false;

		return result;
	}

	@Override
	public void addMenuWidget(MenuBar menuBar) {
		menuBar.addItem("DOCKER MANAGER", e -> showSeedMenu());
	}

	private void showSeedMenu() {
		mainView.hide();
		seedManagerMenu.setVisible(true);
	}

	@Override
	public void setVisibleTrue() {
		seedManagerMenu.setVisible(true);
	}

	@Override
	public List<Component> getLayots() {
		List<Component> l = new ArrayList<>();
		l.add(seedManagerMenu);
		return l;
	}

	public IMainView getMainView() {
		return mainView;
	}

	@Override
	public String toString() {
		return "Container Manager (Seed) Menù";
	}

	@Override
	public Integer getMenuOrderNumber() {
		return 2200;
	}

}
