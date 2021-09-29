package org.ar4k.agent.console;

import java.util.Arrays;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.web.IBeaconProvisioningAuthorization;
import org.ar4k.agent.web.IMainView;
import org.ar4k.agent.web.main.BeaconClientWrapper;
import org.ar4k.agent.web.widget.menu.BeaconServerAndProvisioningMenu;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class ProvisioningServerForm extends FormLayout {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(ProvisioningServerForm.class);

	private static final long serialVersionUID = -7889305640122267762L;

	private Binder<IBeaconProvisioningAuthorization> binder = new BeanValidationBinder<>(
			IBeaconProvisioningAuthorization.class);
	private TextField displayKey = new TextField("Display key");

	private TextField idRequest = new TextField("Id request");

	private TextArea jsonHealth = new TextArea("JSON health");

	private TextField name = new TextField("Name");

	private TextField registrationTimeRequestString = new TextField("Registration time");

	private TextArea csr = new TextArea("CSR");

	private Checkbox approved = new Checkbox(false);

	private TextField approvedDataString = new TextField("Approved data");

	private TextArea certificationDetails = new TextArea("Cert details");

	private TextField shortDescription = new TextField("Short description");

	private final IMainView mainView;

	private Button approve = new Button("Approve this request");
	private Button close = new Button("Close");

	private IBeaconProvisioningAuthorization beaconClientProvisioningRecord;

	private final BeaconServerAndProvisioningMenu beaconServerAndProvisioningMenu;

	public ProvisioningServerForm(BeaconServerAndProvisioningMenu beaconServerAndProvisioningMenu) {
		this.beaconServerAndProvisioningMenu = beaconServerAndProvisioningMenu;
		this.mainView = beaconServerAndProvisioningMenu.getMainView();
		jsonHealth.setMaxHeight("280px");
		csr.setMaxHeight("90px");
		certificationDetails.setMaxHeight("90px");
		addClassName("contact-form");
		add(idRequest, name, displayKey, approved, approvedDataString, certificationDetails, shortDescription,
				registrationTimeRequestString, csr, jsonHealth, createButtons());
		try {
			binder.bindInstanceFields(this);
		} catch (final Exception a) {
			logger.info(a.getMessage());
			logger.info("ProvisioningServerForm: " + Arrays.toString(BeaconServerForm.class.getDeclaredFields())
					+ "\nIBeaconProvisioningAuthorization: "
					+ Arrays.toString(BeaconClientWrapper.class.getDeclaredFields()));
		}
	}

	private VerticalLayout createButtons() {
		final VerticalLayout verticalLayout = new VerticalLayout(createButtonsLayoutApprove(),
				createButtonsLayoutClose());
		verticalLayout.setSizeFull();
		return verticalLayout;
	}

	private HorizontalLayout createButtonsLayoutApprove() {
		approve.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
		approve.addClickShortcut(Key.ENTER);
		approve.addClickListener(event -> validateAndApprove());
		return new HorizontalLayout(approve);
	}

	private HorizontalLayout createButtonsLayoutClose() {
		close.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
		close.addClickShortcut(Key.ESCAPE);
		close.addClickListener(event -> fireEvent(new CloseEvent(this)));
		return new HorizontalLayout(close);
	}

	private void validateAndApprove() {
		if (beaconClientProvisioningRecord != null) {
			fireEvent(new ApproveEvent(this, beaconClientProvisioningRecord, mainView));
		}
	}

	public IBeaconProvisioningAuthorization getBeaconClientWrapper() {
		return beaconClientProvisioningRecord;
	}

	private void setBeaconClientProvisioningRecord(IBeaconProvisioningAuthorization beaconClientProvisioningRecord) {
		this.beaconClientProvisioningRecord = beaconClientProvisioningRecord;
	}

	public void editBeaconProvisioning(IBeaconProvisioningAuthorization beaconClientProvisioning) {
		if (beaconClientProvisioning != null) {
			logger.debug("selected: " + beaconClientProvisioning + " host:" + beaconClientProvisioning.getIdRequest());
			approve.setVisible(true);
			close.setVisible(true);
			this.setBeaconClientProvisioningRecord(beaconClientProvisioning);
			binder.readBean(this.beaconClientProvisioningRecord);
		} else {
			logger.debug("selected null");
		}
	}

	// Events
	public static abstract class BeaconServerFormEvent extends ComponentEvent<ProvisioningServerForm> {
		private static final long serialVersionUID = 8827352050115461890L;
		private IBeaconProvisioningAuthorization beaconProvisioning;

		protected BeaconServerFormEvent(ProvisioningServerForm source,
				IBeaconProvisioningAuthorization beaconProvisioning) {
			super(source, false);
			this.beaconProvisioning = beaconProvisioning;
		}

		public IBeaconProvisioningAuthorization getContact() {
			return beaconProvisioning;
		}
	}

	public class ApproveEvent extends BeaconServerFormEvent {
		private static final long serialVersionUID = -7021301590382929238L;

		ApproveEvent(ProvisioningServerForm source, IBeaconProvisioningAuthorization beaconProvisioning,
				IMainView mainView) {
			super(source, beaconProvisioning);
			beaconServerAndProvisioningMenu.approveBeaconClient(beaconProvisioning);
			beaconServerAndProvisioningMenu.updateListProvisioningAuthorization();
		}
	}

	public class CloseEvent extends BeaconServerFormEvent {
		private static final long serialVersionUID = -5021301590382929238L;

		CloseEvent(ProvisioningServerForm source) {
			super(source, null);
			source.closeForm();
		}
	}

	void closeForm() {
		editBeaconProvisioning(null);
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
