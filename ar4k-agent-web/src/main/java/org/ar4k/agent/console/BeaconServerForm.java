package org.ar4k.agent.console;

import java.util.Arrays;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.web.scada.BeaconClientWrapper;
import org.ar4k.agent.web.widget.menu.BeaconServerMenu;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class BeaconServerForm extends FormLayout {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BeaconServerForm.class.toString());

	private static final long serialVersionUID = -7889305640288767762L;

	private Binder<BeaconClientWrapper> binder = new BeanValidationBinder<>(BeaconClientWrapper.class);

	private TextField host = new TextField("Target host");

	private IntegerField port = new IntegerField("Target port");

	private IntegerField discoveryPort = new IntegerField("Discovery port");

	private TextField discoveryFilter = new TextField("Discovery filter");

	private TextField aliasBeaconClientInKeystore = new TextField("SSL - beacon alias");

	private TextField certFile = new TextField("SSL - cert file path");

	private TextField certChainFile = new TextField("SSL - cert Chain file path");

	private TextField privateFile = new TextField("SSL - private key file path");

	private TextField beaconCaChainPem = new TextField("SSL - Beacon CA");

	private TextField company = new TextField("Company label");

	private TextField context = new TextField("Context label");

	private final MainView mainView;

	// private ListSelect<String> tags = new ListSelect<String>("Tags");

	private Button save = new Button("Save new Beacon connection");
	private Button update = new Button("Update Beacon connection");
	private Button delete = new Button("Delete Beacon connection");
	private Button close = new Button("Close");

	private BeaconClientWrapper beaconClientWrapper;

	private final BeaconServerMenu beaconServerMenu;

	public BeaconServerForm(BeaconServerMenu beaconServerMenu) {
		this.beaconServerMenu = beaconServerMenu;
		this.mainView = beaconServerMenu.getMainView();
		addClassName("contact-form");
		add(host, port, discoveryPort, discoveryFilter, aliasBeaconClientInKeystore, certFile, certChainFile,
				privateFile, beaconCaChainPem, company, context, createButtons());
		try {
			binder.bindInstanceFields(this);
		} catch (final Exception a) {
			logger.info(a.getMessage());
			logger.info("BeaconServerForm: " + Arrays.toString(BeaconServerForm.class.getDeclaredFields())
					+ "\nBeaconClientWrapper: " + Arrays.toString(BeaconClientWrapper.class.getDeclaredFields()));
		}
	}

	public void editBeaconConnection(BeaconClientWrapper beaconClientWrapper) {
		if (beaconClientWrapper != null) {
			logger.debug("selected: " + beaconClientWrapper + " host:" + beaconClientWrapper.getHost());
			save.setVisible(false);
			update.setVisible(true);
			this.setBeaconClientWrapper(beaconClientWrapper);
			binder.readBean(this.beaconClientWrapper);
		} else {
			logger.debug("selected null");
		}
	}

	public void addBeaconConnection(BeaconClientWrapper beaconClientWrapper) {
		if (beaconClientWrapper != null) {
			logger.debug("new: " + beaconClientWrapper);
			save.setVisible(true);
			update.setVisible(false);
			this.setBeaconClientWrapper(beaconClientWrapper);
			binder.readBean(this.beaconClientWrapper);
		} else {
			logger.info("selected null");
		}

	}

	private VerticalLayout createButtons() {
		final VerticalLayout verticalLayout = new VerticalLayout(createButtonsLayoutSaveUpdate(),
				createButtonsLayoutDeleteClose());
		verticalLayout.setSizeFull();
		return verticalLayout;
	}

	private HorizontalLayout createButtonsLayoutSaveUpdate() {
		save.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
		save.addClickShortcut(Key.ENTER);
		save.addClickListener(event -> validateAndSave());
		binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
		update.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
		update.addClickListener(event -> validateAndSave());
		binder.addStatusChangeListener(e -> update.setEnabled(binder.isValid()));
		return new HorizontalLayout(save, update);
	}

	private HorizontalLayout createButtonsLayoutDeleteClose() {
		delete.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
		close.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
		close.addClickShortcut(Key.ESCAPE);
		delete.addClickListener(event -> fireEvent(new DeleteEvent(this, beaconClientWrapper, mainView)));
		close.addClickListener(event -> fireEvent(new CloseEvent(this)));
		return new HorizontalLayout(delete, close);
	}

	private void validateAndSave() {
		try {
			binder.writeBean(beaconClientWrapper);
			fireEvent(new SaveEvent(this, beaconClientWrapper, mainView));
		} catch (final ValidationException e) {
			logger.logException(e);
		}
	}

	public BeaconClientWrapper getBeaconClientWrapper() {
		return beaconClientWrapper;
	}

	private void setBeaconClientWrapper(BeaconClientWrapper beaconClientWrapper) {
		this.beaconClientWrapper = beaconClientWrapper;
	}

	// Events
	public static abstract class BeaconServerFormEvent extends ComponentEvent<BeaconServerForm> {

		private static final long serialVersionUID = 8827352050113884890L;
		private BeaconClientWrapper beaconClientWrapper;

		protected BeaconServerFormEvent(BeaconServerForm source, BeaconClientWrapper beaconClientWrapper) {
			super(source, false);
			this.beaconClientWrapper = beaconClientWrapper;
		}

		public BeaconClientWrapper getContact() {
			return beaconClientWrapper;
		}
	}

	public class SaveEvent extends BeaconServerFormEvent {

		private static final long serialVersionUID = -7021301590382929238L;

		SaveEvent(BeaconServerForm source, BeaconClientWrapper beaconClientWrapper, MainView mainView) {
			super(source, beaconClientWrapper);
			beaconServerMenu.updateListBeaconServer();
		}
	}

	public class DeleteEvent extends BeaconServerFormEvent {

		private static final long serialVersionUID = -6021301590382929238L;

		DeleteEvent(BeaconServerForm source, BeaconClientWrapper beaconClientWrapper, MainView mainView) {
			super(source, beaconClientWrapper);
			beaconServerMenu.updateListBeaconServer();
		}

	}

	public class CloseEvent extends BeaconServerFormEvent {

		private static final long serialVersionUID = -5021301590382929238L;

		CloseEvent(BeaconServerForm source) {
			super(source, null);
			source.closeForm();
		}
	}

	void closeForm() {
		editBeaconConnection(null);
		setVisible(false);
		removeClassName("editing");
		removeClassName("new");
	}

	@Override
	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}

}
