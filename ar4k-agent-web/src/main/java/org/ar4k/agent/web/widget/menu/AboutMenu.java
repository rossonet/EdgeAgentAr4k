package org.ar4k.agent.web.widget.menu;

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
public class AboutMenu implements AgentMenu {

	private IMainView mainView = null;

	private IFrame aboutMenu = new IFrame();

	@Override
	public void setMainView(IMainView mainView) {
		this.mainView = mainView;
		aboutMenu.setSrc("https://www.rossonet.net/");
		Style style = aboutMenu.getStyle();
		style.set("padding", "3px");
		style.set("border", "0px");
		style.set("overflow", "hidden");
		aboutMenu.setSizeFull();
	}

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public void addMenuWidget(MenuBar menuBar) {
		menuBar.addItem("ABOUT", e -> showHelp());
	}

	private void showHelp() {
		mainView.hide();
		aboutMenu.setVisible(true);
	}

	@Override
	public void setVisibleTrue() {
		aboutMenu.setVisible(true);
	}

	@Override
	public List<Component> getLayots() {
		List<Component> l = new ArrayList<>();
		l.add(aboutMenu);
		return l;
	}

	public IMainView getMainView() {
		return mainView;
	}

	@Override
	public String toString() {
		return "About Menù";
	}

	@Override
	public Integer getMenuOrderNumber() {
		return 9000;
	}

}
