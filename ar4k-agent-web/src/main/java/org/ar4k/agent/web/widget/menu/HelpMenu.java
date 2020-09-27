package org.ar4k.agent.web.widget.menu;

import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.console.MainView;
import org.ar4k.agent.design.AgentMenu;
import org.ar4k.agent.design.AgentWebMenu;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;

@AgentWebMenu
public class HelpMenu implements AgentMenu {

	private static final String MD_HELP = "## EDGE AGENT GUIDE\n" + "\n"
			+ "![Work in progress](https://raw.githubusercontent.com/mydearxym/coderplanets_admin/dev/static/waji.png)\n";

	private MainView mainView = null;

	private Div divHelp = new Div();

	@Override
	public void setMainView(MainView mainView) {
		this.mainView = mainView;
		// TODO Help Online
		divHelp.setSizeFull();
		divHelp.getStyle().set("padding", "6px");
		divHelp.getStyle().set("border", "1px dotted #8c7373");
		Node node = Parser.builder().build().parse(MD_HELP);
		divHelp.getElement().setProperty("innerHTML", HtmlRenderer.builder().build().render(node));
	}

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public void addMenuWidget(MenuBar menuBar) {
		menuBar.addItem("HELP", e -> showHelp());
	}

	private void showHelp() {
		mainView.hide();
		divHelp.setVisible(true);
	}

	@Override
	public void setVisibleTrue() {
		divHelp.setVisible(true);
	}

	@Override
	public List<Component> getLayots() {
		List<Component> l = new ArrayList<>();
		l.add(divHelp);
		return l;
	}

	public MainView getMainView() {
		return mainView;
	}

	@Override
	public String toString() {
		return "HelpMenu";
	}

	@Override
	public Integer getMenuOrderNumber() {
		return 10000;
	}

}
