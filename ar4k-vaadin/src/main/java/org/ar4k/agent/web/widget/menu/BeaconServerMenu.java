package org.ar4k.agent.web.widget.menu;

import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.console.BeaconServerForm;
import org.ar4k.agent.core.interfaces.AgentWebMenu;
import org.ar4k.agent.core.interfaces.IBeaconClientScadaWrapper;
import org.ar4k.agent.core.interfaces.IMainView;
import org.ar4k.agent.web.interfaces.AgentMenu;
import org.ar4k.agent.web.main.BeaconClientWrapper;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

@AgentWebMenu
public class BeaconServerMenu implements AgentMenu {

	private IMainView mainView = null;

	private final TextField serverFilterText = new TextField();
	private final Grid<IBeaconClientScadaWrapper> gridServer = new Grid<>(IBeaconClientScadaWrapper.class);
	private BeaconServerForm beaconServerForm = null;

	@Override
	public void setMainView(IMainView mainView) {
		this.mainView = mainView;
		configureFilterServers();
		this.beaconServerForm = new BeaconServerForm(this);
		gridServer.getColumns().forEach(col -> col.setAutoWidth(true));
	}

	// sempre attivo il client web può collegarsi ovunque
	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public void addMenuWidget(MenuBar menuBar) {
		final MenuItem beaconServers = menuBar.addItem("Beacon Servers");
		final SubMenu serverSubMenu = beaconServers.getSubMenu();
		serverSubMenu.addItem("LIST", e -> listBeaconServers());
		serverSubMenu.addItem("ADD", e -> addBeaconServer());
		configureGridServer();
	}

	private void listBeaconServers() {
		mainView.hide();
		gridServer.setVisible(true);
		serverFilterText.setVisible(true);
		updateListBeaconServer();
	}

	private void addBeaconServer() {
		gridServer.asSingleSelect().clear();
		final BeaconClientWrapper beaconClientWrapper = new BeaconClientWrapper();
		mainView.addClientServer(beaconClientWrapper);
		beaconServerForm.addBeaconConnection(beaconClientWrapper);
		mainView.hide();
		gridServer.setVisible(true);
		serverFilterText.setVisible(true);
		beaconServerForm.setVisible(true);
		beaconServerForm.addClassName("new");
	}

	private void configureFilterServers() {
		serverFilterText.setPlaceholder("Filter on beacon servers...");
		serverFilterText.setClearButtonVisible(true);
		serverFilterText.setValueChangeMode(ValueChangeMode.LAZY);
		serverFilterText.addValueChangeListener(e -> updateListBeaconServer());
	}

	public void updateListBeaconServer() {
		gridServer.setItems(mainView.getBeaconServersList(serverFilterText.getValue()));
	}

	private void configureGridServer() {
		gridServer.setColumns("company", "context", "host", "port", "status", "registrationStatus", "agentsCount",
				"discoveryPort", "discoveryFilter");
		gridServer.asSingleSelect().addValueChangeListener(event -> editbeaconServerWrapper(event.getValue()));
	}

	public void editbeaconServerWrapper(IBeaconClientScadaWrapper beaconClientWrapper) {
		beaconServerForm.editBeaconConnection(beaconClientWrapper);
		beaconServerForm.setVisible(true);
		beaconServerForm.addClassName("editing");
	}

	@Override
	public void setVisibleTrue() {
		gridServer.setVisible(true);
	}

	@Override
	public List<Component> getLayots() {
		List<Component> l = new ArrayList<>();
		l.add(serverFilterText);
		l.add(gridServer);
		l.add(beaconServerForm);
		return l;
	}

	public IMainView getMainView() {
		return mainView;
	}

	@Override
	public String toString() {
		return "BeaconServer Menù";
	}

	@Override
	public int compareTo(AgentMenu arg0) {
		return getMenuOrderNumber().compareTo(arg0.getMenuOrderNumber());
	}

	@Override
	public Integer getMenuOrderNumber() {
		return 2000;
	}

}
