package org.ar4k.agent.console;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.web.scada.BeaconClientWrapper;
import org.ar4k.agent.web.scada.ScadaAgentWrapper;
import org.ar4k.agent.web.scada.ScadaBeaconService;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.ReconnectDialogConfiguration;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

@Route("")
@Theme(value = Material.class)
@PageTitle("Rossonet Scada")
public class MainView extends VerticalLayout {

	private static final long serialVersionUID = 53637205682774475L;

	@SuppressWarnings("unused")
	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(MainView.class.toString());

	private ScadaBeaconService scadaBeaconService = Homunculus.getApplicationContext().getBean(ScadaBeaconService.class);

	private final TextField serverFilterText = new TextField();
	private final TextField agentFilterText = new TextField();
	private final MenuBar menuBar = new MenuBar();
	private final Grid<BeaconClientWrapper> gridServer = new Grid<>(BeaconClientWrapper.class);
	private final Grid<ScadaAgentWrapper> gridClient = new Grid<>(ScadaAgentWrapper.class);
	private final Board board = new Board();
	private final BeaconServerForm beaconServerForm;
	private BeaconAgentDialog beaconAgentForm;

	public MainView() {
		configureMenu();
		board.addRow(menuBar);
		// add(beaconAgentForm);
		configureGridServer();
		configureGridClient();
		configuretReconnection();
		configureFilterServers();
		configureFilterAgents();
		setMargin(false);
		setSpacing(false);
		board.addRow(serverFilterText);
		board.addRow(gridServer);
		board.addRow(gridClient);
		gridServer.getColumns().forEach(col -> col.setAutoWidth(true));
		gridClient.getColumns().forEach(col -> col.setAutoWidth(true));
		add(board);
		beaconServerForm = new BeaconServerForm(this);
		add(beaconServerForm);
		// listBeaconServers();
		listBeaconAgents();
	}

	private void configureGridClient() {
		gridClient.setColumns("name", "lastContact", "processors", "osName", "osVersion", "totalMemoryMB",
				"freeMemoryMB", "javaVm", "commandsCount");
		gridClient.asSingleSelect().addValueChangeListener(event -> editbeaconAgentWrapper(event.getValue()));
	}

	private void editbeaconAgentWrapper(ScadaAgentWrapper beaconAgent) {
		if (beaconAgent != null) {
			beaconAgentForm = new BeaconAgentDialog(this, beaconAgent);
			beaconAgentForm.setVisible(true);
			add(beaconAgentForm);
			beaconAgentForm.open();
		}
	}

	private void configureGridServer() {
		gridServer.setColumns("company", "context", "host", "port", "status", "registrationStatus", "agentsCount",
				"discoveryPort", "discoveryFilter");
		gridServer.asSingleSelect().addValueChangeListener(event -> editbeaconClientWrapper(event.getValue()));
	}

	public void editbeaconClientWrapper(BeaconClientWrapper beaconClientWrapper) {
		if (beaconClientWrapper == null) {
			closeEditBeaconClientWrapper();
		} else {
			beaconServerForm.editBeaconConnection(beaconClientWrapper);
			beaconServerForm.setVisible(true);
			beaconServerForm.addClassName("editing");
		}
	}

	private void closeEditBeaconClientWrapper() {
		beaconServerForm.closeForm();
	}

	void clearGridServer() {
		gridServer.asSingleSelect().clear();
	}

	public void clearGridAgent() {
		gridClient.asSingleSelect().clear();
	}

	private void configuretReconnection() {
		final ReconnectDialogConfiguration configuration = UI.getCurrent().getReconnectDialogConfiguration();
		configuration.setDialogText("Server disconnected. Please wait reconnection...");
		configuration.setDialogTextGaveUp("Server connected");
		configuration.setDialogModal(true);
	}

	private void configureMenu() {
		menuBar.addItem("Dashboard", e -> gotToDashboard());
		final MenuItem beaconAgents = menuBar.addItem("Agents");
		final SubMenu agentSubMenu = beaconAgents.getSubMenu();
		agentSubMenu.addItem("List", e -> listBeaconAgents());
		agentSubMenu.addItem("Create new agent configuration", e -> createBeaconAgent());
		final MenuItem beaconServers = menuBar.addItem("Beacon Servers");
		final SubMenu serverSubMenu = beaconServers.getSubMenu();
		serverSubMenu.addItem("List", e -> listBeaconServers());
		serverSubMenu.addItem("Add", e -> addBeaconServer());
		menuBar.addItem("Sign Out", e -> SecurityContextHolder.clearContext());
	}

	private void gotToDashboard() {
		// TODO Auto-generated method stub
	}

	private void addBeaconServer() {
		gridServer.asSingleSelect().clear();
		final BeaconClientWrapper beaconClientWrapper = new BeaconClientWrapper();
		scadaBeaconService.addClientServer(beaconClientWrapper);
		beaconServerForm.addBeaconConnection(beaconClientWrapper);
		gridServer.setVisible(true);
		gridClient.setVisible(false);
		serverFilterText.setVisible(true);
		agentFilterText.setVisible(false);
		beaconServerForm.setVisible(true);
		beaconServerForm.addClassName("new");
	}

	private void listBeaconServers() {
		closeEditBeaconClientWrapper();
		gridServer.setVisible(true);
		gridClient.setVisible(false);
		serverFilterText.setVisible(true);
		agentFilterText.setVisible(false);
		updateListBeaconServer();
	}

	private void createBeaconAgent() {
		// TODO Metodo per creare la configurazione di un nuovo agente
	}

	private void listBeaconAgents() {
		closeEditBeaconClientWrapper();
		gridServer.setVisible(false);
		gridClient.setVisible(true);
		serverFilterText.setVisible(false);
		agentFilterText.setVisible(true);
		updateListBeaconAgent();
	}

	private void configureFilterServers() {
		serverFilterText.setPlaceholder("Filter on beacon servers...");
		serverFilterText.setClearButtonVisible(true);
		serverFilterText.setValueChangeMode(ValueChangeMode.LAZY);
		serverFilterText.addValueChangeListener(e -> updateListBeaconServer());
	}

	private void configureFilterAgents() {
		agentFilterText.setPlaceholder("Filter on agents...");
		agentFilterText.setClearButtonVisible(true);
		agentFilterText.setValueChangeMode(ValueChangeMode.LAZY);
		agentFilterText.addValueChangeListener(e -> updateListBeaconAgent());
	}

	public void updateListBeaconServer() {
		gridServer.setItems(scadaBeaconService.getBeaconServersList(serverFilterText.getValue()));
	}

	private void updateListBeaconAgent() {
		gridClient.setItems(scadaBeaconService.getClients(agentFilterText.getValue()));

	}

}
