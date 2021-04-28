package org.ar4k.agent.web.widget.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.ar4k.agent.console.BeaconServerForm;
import org.ar4k.agent.console.ProvisioningServerForm;
import org.ar4k.agent.core.interfaces.AgentWebMenu;
import org.ar4k.agent.core.interfaces.IBeaconClientScadaWrapper;
import org.ar4k.agent.core.interfaces.IBeaconProvisioningAuthorization;
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
public class BeaconServerAndProvisioningMenu implements AgentMenu {

	private IMainView mainView = null;

	private final TextField serverFilterText = new TextField();
	private final Grid<IBeaconClientScadaWrapper> gridServer = new Grid<>(IBeaconClientScadaWrapper.class);

	private final Grid<IBeaconProvisioningAuthorization> gridProvisioningRequest = new Grid<>(
			IBeaconProvisioningAuthorization.class);
	private BeaconServerForm beaconServerForm = null;

	private ProvisioningServerForm provisioningServerForm = null;

	@Override
	public void setMainView(IMainView mainView) {
		this.mainView = mainView;
		configureFilterServers();
		this.beaconServerForm = new BeaconServerForm(this);
		this.provisioningServerForm = new ProvisioningServerForm(this);
		gridServer.getColumns().forEach(col -> col.setAutoWidth(true));
		gridProvisioningRequest.getColumns().forEach(col -> col.setAutoWidth(true));
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
		serverSubMenu.addItem("PROVISIONING AUTHORIZATION", e -> listProvisioningAuthorization());
		configureGridServer();
		configureProvisioningAuthoritation();
	}

	private void listBeaconServers() {
		mainView.hide();
		gridProvisioningRequest.setVisible(false);
		provisioningServerForm.setVisible(false);
		gridServer.setVisible(true);
		configureFilterServers();
		serverFilterText.setVisible(true);
		updateListBeaconServer();
	}

	private void listProvisioningAuthorization() {
		mainView.hide();
		gridServer.setVisible(false);
		gridProvisioningRequest.setVisible(true);
		beaconServerForm.setVisible(false);
		configureFilterProvisioningAuthorization();
		serverFilterText.setVisible(true);
		updateListProvisioningAuthorization();
	}

	private void addBeaconServer() {
		gridServer.asSingleSelect().clear();
		final BeaconClientWrapper beaconClientWrapper = new BeaconClientWrapper();
		mainView.addClientServer(beaconClientWrapper);
		beaconServerForm.addBeaconConnection(beaconClientWrapper);
		provisioningServerForm.setVisible(false);
		mainView.hide();
		gridProvisioningRequest.setVisible(false);
		gridServer.setVisible(true);
		configureFilterServers();
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

	private void configureFilterProvisioningAuthorization() {
		serverFilterText.setPlaceholder("Filter on provisioning...");
		serverFilterText.setClearButtonVisible(true);
		serverFilterText.setValueChangeMode(ValueChangeMode.LAZY);
		serverFilterText.addValueChangeListener(e -> updateListProvisioningAuthorization());
	}

	public void updateListBeaconServer() {
		provisioningServerForm.setVisible(false);
		configureFilterServers();
		gridServer.setItems(mainView.getBeaconServersList(serverFilterText.getValue()));
	}

	public void updateListProvisioningAuthorization() {
		beaconServerForm.setVisible(false);
		configureFilterProvisioningAuthorization();
		final Collection<IBeaconProvisioningAuthorization> provisioningAuthorizationList = mainView
				.getProvisioningAuthorizationList(serverFilterText.getValue());
		StringBuilder sb = new StringBuilder();
		for (IBeaconProvisioningAuthorization i : provisioningAuthorizationList) {
			sb.append(i.getIdRequest() + " = " + i.getApprovedDataString());
		}
		System.out.println("****** list -> " + sb.toString());
		gridProvisioningRequest.setItems(provisioningAuthorizationList);
	}

	private void configureGridServer() {
		gridServer.setColumns("company", "context", "host", "port", "status", "registrationStatus", "agentsCount",
				"discoveryPort", "discoveryFilter");
		gridServer.asSingleSelect().addValueChangeListener(event -> editBeaconServerWrapper(event.getValue()));
	}

	private void configureProvisioningAuthoritation() {
		gridProvisioningRequest.setColumns("idRequest", "approved", "name", "displayKey", "shortDescription",
				"registrationTimeRequestString");
		gridProvisioningRequest.asSingleSelect()
				.addValueChangeListener(event -> editProvisioningWrapper(event.getValue()));
	}

	private void editProvisioningWrapper(IBeaconProvisioningAuthorization provisioningRecord) {
		configureFilterProvisioningAuthorization();
		provisioningServerForm.editBeaconProvisioning(provisioningRecord);
		provisioningServerForm.setVisible(true);
		beaconServerForm.setVisible(false);
		provisioningServerForm.addClassName("editing");
	}

	public void editBeaconServerWrapper(IBeaconClientScadaWrapper beaconClientWrapper) {
		configureFilterServers();
		beaconServerForm.editBeaconConnection(beaconClientWrapper);
		beaconServerForm.setVisible(true);
		provisioningServerForm.setVisible(false);
		beaconServerForm.addClassName("editing");
	}

	@Override
	public void setVisibleTrue() {
		// inutilizzato
	}

	@Override
	public List<Component> getLayots() {
		List<Component> l = new ArrayList<>();
		l.add(serverFilterText);
		l.add(gridServer);
		l.add(gridProvisioningRequest);
		l.add(beaconServerForm);
		l.add(provisioningServerForm);
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

	public void approveBeaconClient(IBeaconProvisioningAuthorization beaconProvisioning) {
		mainView.approveProvisioningRequest(beaconProvisioning);
	}

}
