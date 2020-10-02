package org.ar4k.agent.web.widget.menu;

import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.console.MainView;
import org.ar4k.agent.design.AgentMenu;
import org.ar4k.agent.design.AgentWebMenu;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.dom.Style;

@AgentWebMenu
public class SeedMenu implements AgentMenu {

	@SuppressWarnings("unused")
	private static final String MD_HELP = "## EDGE AGENT GUIDE\n" + "\n"
			+ "![Work in progress](https://raw.githubusercontent.com/mydearxym/coderplanets_admin/dev/static/waji.png)\n";

	private MainView mainView = null;

	private IFrame seedManagerMenu = new IFrame();

	@Override
	public void setMainView(MainView mainView) {
		this.mainView = mainView;
		seedManagerMenu.setSrc("https://www.rossonet.net/");
		Style style = seedManagerMenu.getStyle();
		style.set("padding", "3px");
		style.set("border", "0px");
		style.set("overflow", "hidden");
		// Node node = Parser.builder().build().parse(MD_HELP);
		// divHelp.getElement().setProperty("innerHTML",
		// HtmlRenderer.builder().build().render(node));
		seedManagerMenu.setSizeFull();
	}

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public void addMenuWidget(MenuBar menuBar) {
		menuBar.addItem("CONTAINERS MANAGER", e -> showSeedMenu());
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

	public MainView getMainView() {
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
