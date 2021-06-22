package org.ar4k.agent.console;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.interfaces.AgentWebMenu;
import org.ar4k.agent.core.interfaces.IBeaconClientScadaWrapper;
import org.ar4k.agent.core.interfaces.IBeaconProvisioningAuthorization;
import org.ar4k.agent.core.interfaces.IMainView;
import org.ar4k.agent.core.interfaces.IScadaAgent;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.web.interfaces.AgentMenu;
import org.ar4k.agent.web.main.MainBeaconService;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.security.core.context.SecurityContextHolder;

//import com.vaadin.annotations.StyleSheet;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ReconnectDialogConfiguration;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

@Route(value = "ar4k-console", absolute = true)
@Theme(value = Material.class, variant = Material.DARK)
@PageTitle("Ar4k Console")
@StyleSheet("frontend://edge.css")
public class MainView extends VerticalLayout implements IMainView {

	private static final long serialVersionUID = 53637205682774475L;

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(MainView.class.toString());

	public static final String PACKET_SEARCH_BASE = "org.ar4k.agent";

	private transient MainBeaconService mainBeaconService = Homunculus.getApplicationContext()
			.getBean(MainBeaconService.class);

	private final TextField agentFilterText = new TextField();
	private final MenuBar menuBar = new MenuBar();
	private final Set<AgentMenu> agentMenus = new HashSet<>();

	private final Set<Component> components = new HashSet<>();

	private final Grid<IScadaAgent> gridClient = new Grid<>(IScadaAgent.class);
	// private final Board board = new Board();
	private BeaconAgentDialog beaconAgentForm = null;

	public MainView() {
		addClassName("main-view");
		configureMenu();
		setMargin(true);
		add(menuBar);
		configureGridClient();
		configuretReconnection();
		configureFilterAgents();
		setMargin(false);
		setSpacing(false);
		setSizeFull();
		setMinWidth("600px");
		for (AgentMenu mi : agentMenus) {
			for (Component c : mi.getLayots()) {
				components.add(c);
				add(c);
			}
		}
		hideAllCustomObjects();
		add(agentFilterText);
		add(gridClient);
		gridClient.getColumns().forEach(col -> col.setAutoWidth(true));
		listBeaconAgents();
	}

	private void configureGridClient() {
		gridClient.setColumns("name", "lastContact", "processors", "osName", "osVersion", "totalMemoryMB",
				"freeMemoryMB", "javaVm");
		gridClient.asSingleSelect().addValueChangeListener(event -> editbeaconAgentWrapper(event.getValue()));
	}

	private void editbeaconAgentWrapper(IScadaAgent beaconAgent) {
		if (beaconAgent != null) {
			beaconAgentForm = new BeaconAgentDialog(this, beaconAgent);
			beaconAgentForm.setVisible(true);
			add(beaconAgentForm);
			beaconAgentForm.open();
		}
	}

	private void configuretReconnection() {
		final ReconnectDialogConfiguration configuration = UI.getCurrent().getReconnectDialogConfiguration();
		configuration.setDialogText("Server disconnected. Please wait reconnection...");
		configuration.setDialogTextGaveUp("Server connected");
		configuration.setDialogModal(true);
	}

	private void configureMenu() {
		final MenuItem beaconAgents = menuBar.addItem("Agents");
		final SubMenu agentSubMenu = beaconAgents.getSubMenu();
		agentSubMenu.addItem("LIST", e -> listBeaconAgents());
		agentSubMenu.addItem("CREATE NEW AGENT CONFIGURATION", e -> createBeaconAgentConfig());
		try {
			addCustomMenus(menuBar);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			logger.logException(e1);
		}
		menuBar.addItem("SIGN OUT", e -> SecurityContextHolder.clearContext());
	}

	private void addCustomMenus(MenuBar menuRoot)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<AgentMenu> agentMenusTarget = getAgentMenus();
		Collections.sort(agentMenusTarget);
		for (AgentMenu am : agentMenusTarget) {
			// System.out.println("found menù " + am);
			am.addMenuWidget(menuRoot);
			agentMenus.add(am);
		}

	}

	private List<AgentMenu> getAgentMenus() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<AgentMenu> annotatedMenus = getAnnotatedMenus(PACKET_SEARCH_BASE);
		List<AgentMenu> finalMenus = new ArrayList<>();
		for (AgentMenu a : annotatedMenus) {
			if (a.isActive()) {
				finalMenus.add(a);
			} else {
				logger.info("menu " + a.toString() + " is disabled");
			}
		}
		return finalMenus;
	}

	private List<AgentMenu> getAnnotatedMenus(String packageBaseSearch)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
				false);
		provider.addIncludeFilter(new AnnotationTypeFilter(AgentWebMenu.class));
		final Set<BeanDefinition> classes = provider.findCandidateComponents(packageBaseSearch);
		final List<AgentMenu> rit = new ArrayList<>();
		for (BeanDefinition c : classes) {
			final String classTarget = c.getBeanClassName();
			AgentMenu aMenu = createMenu(classTarget);
			aMenu.setMainView(this);
			rit.add(aMenu);
		}
		return rit;
	}

	private AgentMenu createMenu(String classTarget)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final Class<? extends AgentMenu> clazz = (Class<? extends AgentMenu>) Class.forName(classTarget);
		final Constructor<? extends AgentMenu> constructor = clazz.getConstructor();
		return constructor.newInstance();
	}

	private void createBeaconAgentConfig() {
		// TODO Metodo per creare la configurazione di un nuovo agente
	}

	private void listBeaconAgents() {
		hideAllCustomObjects();
		agentFilterText.setVisible(true);
		gridClient.setVisible(true);
		updateListBeaconAgent();
	}

	private void configureFilterAgents() {
		agentFilterText.setPlaceholder("Filter on agents...");
		agentFilterText.setClearButtonVisible(true);
		agentFilterText.setValueChangeMode(ValueChangeMode.LAZY);
		agentFilterText.addValueChangeListener(e -> updateListBeaconAgent());
	}

	private void updateListBeaconAgent() {
		gridClient.setItems(mainBeaconService.getClients(agentFilterText.getValue()));

	}

	@Override
	public Collection<IScadaAgent> getAllAgents() {
		return mainBeaconService.getClients(null);
	}

	@Override
	public void hide() {
		hideAllCustomObjects();
		gridClient.setVisible(false);
		agentFilterText.setVisible(false);
		if (beaconAgentForm != null)
			beaconAgentForm.setVisible(false);
	}

	private void hideAllCustomObjects() {
		for (Component i : components) {
			i.setVisible(false);
		}
	}

	@Override
	public void addClientServer(IBeaconClientScadaWrapper beaconClientWrapper) {
		mainBeaconService.addClientServer(beaconClientWrapper);
	}

	@Override
	public Collection<IBeaconClientScadaWrapper> getBeaconServersList(String value) {
		return mainBeaconService.getBeaconServersList(value);
	}

	@Override
	public Collection<IBeaconProvisioningAuthorization> getProvisioningAuthorizationList(String value) {
		return mainBeaconService.getProvisioningAuthorizationList(value);
	}

	@Override
	public void approveProvisioningRequest(IBeaconProvisioningAuthorization beaconProvisioning) {
		mainBeaconService.approveRequestProvisioning(beaconProvisioning);
	}

}
